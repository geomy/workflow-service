package org.ng.workflow.workitems.service;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.ng.workflow.utils.CustomGeneratedData;
import org.ng.workflow.utils.XmlParserHelper;
import org.ng.workflow.workitems.dto.*;
import org.ng.workflow.workitems.enumerations.CallbackStatus;
import org.ng.workflow.workitems.enumerations.ProcessResponseType;
import org.ng.workflow.workitems.models.*;
import org.ng.workflow.workitems.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.utils.ApplicationResponse;
import org.ng.workflow.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemTransactionRepository itemTransactionRepository;
    @Autowired
    ProcessActorRepository processActorRepository;
    @Autowired
    ParentProcessRepository parentProcessRepository;
    @Autowired
    SubProcessRepository subProcessRepository;
    @Autowired
    ProcessMappingRepository processMappingRepository;
    @Autowired
    ItemDetailsRepository itemDetailsRepository;
    @Autowired
    XmlParserHelper xmlParserHelper;
    @Autowired
    WorkflowStateRepository stateRepository;
    @Autowired
    WorkflowRoleRepository roleRepository;
    @Autowired
    ProcessResponseRepository processResponseRepository;
    @Value("${http.request.connection.timeout}")
    private String CONNECTION_TIMEOUT;
    @Value("${http.request.read.timeout}")
    private String READ_TIMEOUT;
    @Value("${initial.item.state}")
    private String initialItemState;

    @Override
    @Transactional
    public ApplicationResponse<PostItemPayload> createWorkItem(PostItemPayload itemDto) {
        ParentProcess parentProcess = this.parentProcessRepository.findByName(itemDto.getParentProcess().getName());
        if (parentProcess == null) {
            log.error("ACTION: REGISTER_WORK_ITEM, ERROR>>>> parent process {} does not exists", itemDto.getParentProcess().getName());
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Parent process does exists");
        }
        WorkflowState workflowState = this.stateRepository.findByName(initialItemState);
        if (workflowState == null) {
            log.error("Initial item state is not configured");
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Initial item state is not configured");
        }
        /* Validate validity of subprocesses passed */
        List<ProcessMapping> mappings = this.processMappingRepository.findByParentProcess(parentProcess);
        List<ItemDetails> itemDetails = new ArrayList<>();
        for (SubProcessReq subProcessDto : itemDto.getParentProcess().getSubProcesses()) {
            SubProcess subProcess = this.subProcessRepository.findByName(subProcessDto.getName());
            if (subProcess == null) {
                log.error("ACTION: REGISTER_WORK_ITEM, ERROR>>>> Sub process {} does not exists", subProcessDto.getName());
                return new ApplicationResponse<>(ResponseCode.FAIL, null, "Sub process " + subProcessDto.getName() + " does exists");
            }
            List<ProcessMapping> subProcessMapping = mappings.stream().filter(p -> p.getSubProcessId().equals(subProcess.getId())).collect(Collectors.toList());
            if (subProcessMapping.isEmpty()) {
                log.error("ACTION: REGISTER_WORK_ITEM, ERROR>>>> Sub process {} is not mapped with parent process {}", subProcessDto.getName(), itemDto.getParentProcess().getName());
                return new ApplicationResponse<>(ResponseCode.FAIL, null, "Sub process " + subProcessDto.getName() + " is not mapped with parent process");
            }
            ProcessMapping processMapping = subProcessMapping.get(0);
            String headers = CustomGeneratedData.convertObjectToJson(subProcessDto.getActionHeaders());
            itemDetails.add(new ItemDetails(null, subProcess, subProcessDto.getEndpoint(), subProcessDto.getPayload(), headers, processMapping.getSequenceNo()));
        }
        Item item = new Item(parentProcess, workflowState, itemDto.getDescription());
        item.setInitiator(itemDto.getInitiator());
        item.setOrganisation(itemDto.getOrganisation());
        String additionalProperties = CustomGeneratedData.convertObjectToJson(itemDto.getAdditionalProperties());
        item.setAdditionalProperties(additionalProperties);
        Item newItem = this.itemRepository.saveAndFlush(item);
        if (newItem.getId() != null) {
            /* create work item transactions */
            List<ProcessActor> actors = this.processActorRepository.findByProcessOrderBySequenceNoAsc(parentProcess);
            ItemTransaction initialTransaction = new ItemTransaction(newItem, "INITIATE", "New request for approval initiated",
                    LocalDateTime.now(), item.getInitiator(), actors.get(0));
            ItemTransaction nextTransaction = new ItemTransaction(newItem, null, null,
                    null, null, actors.get(1));
            List<ItemTransaction> itemTransactions = new ArrayList<>(Arrays.asList(initialTransaction, nextTransaction));
            this.itemTransactionRepository.saveAll(itemTransactions);

            /* create work item details */
            itemDetails.forEach(itemDetail -> {
                itemDetail.setItem(newItem);
            });
            this.itemDetailsRepository.saveAll(itemDetails);
        }
        return new ApplicationResponse<>(ResponseCode.SUCCESS, null, "Request processed successfully");
    }

    @Override
    @Transactional
    public ApplicationResponse<List<PutItemResponse>> updateWorkItem(UpdateItemDto updateItemDto, Long id) {
        Optional<Item> itemOptional = this.itemRepository.findById(id);
        List<PutItemResponse> response = new ArrayList<>();
        if (!itemOptional.isPresent()) {
            log.error("ACTION: UPDATE_WORK_ITEM, ERROR>>>> Item {} does not exists", id);
            return new ApplicationResponse<>(ResponseCode.RECORD_DOES_NOT_EXIST, null);
        } else {
            Item item = itemOptional.get();
            /* Only pending work item is allowed to be updated */
            WorkflowState pendingState = this.stateRepository.findByName(ItemStatus.IN_PROGRESS.name());
            if (pendingState == null) {
                log.error("Item {} is not pending ", item.getId());
                return new ApplicationResponse<>(ResponseCode.FAIL, null, "Item is not pending");
            }
            if (!item.getState().getId().equals(pendingState.getId())) {
                log.error("ACTION: UPDATE_WORK_ITEM, ERROR>>>> Item {} is not in progress ", item.getId());
                return new ApplicationResponse<>(ResponseCode.FAIL, response, "The work item is not in IN_PROGRESS");
            }
            List<ItemTransaction> transactions = this.itemTransactionRepository.findByItemOrderByIdDesc(item);
            if (!transactions.isEmpty()) {
                List<ProcessActor> actors = this.processActorRepository.findByProcessOrderBySequenceNoAsc(item.getParentProcess());
                ItemTransaction lastEntry = transactions.get(0);
                ProcessActor lastActor = actors.get(actors.size() - 1);
                Optional<ProcessActor> currentActorOptional = actors.stream().filter(x -> x.getId().equals(lastEntry.getProcessActor().getId())).findFirst();
                ProcessActor currentActor = currentActorOptional.get();
                if (currentActor.getActorName() != null && currentActor.getActorName() != updateItemDto.getActor()) {
                    log.error("ACTION: UPDATE_WORK_ITEM, ERROR>>>> Actor {} is not allowed to approve this item", updateItemDto.getActor());
                    return new ApplicationResponse<>(ResponseCode.FAIL, response, "Actor is not allowed to approve this item");
                }
                if (!currentActor.getRole().getName().equals(updateItemDto.getRole())) {
                    log.error("ACTION: UPDATE_WORK_ITEM, ERROR>>>> Role {} is forbidden  to approve item {}", updateItemDto.getRole(), item.getId());
                    return new ApplicationResponse<>(ResponseCode.FAIL, response, "Role is forbidden  to approve this item");
                }

                lastEntry.setAction(updateItemDto.getAction());
                lastEntry.setRemarks(updateItemDto.getComments());
                lastEntry.setActionDate(LocalDateTime.now());
                lastEntry.setActor(updateItemDto.getActor());
                this.itemTransactionRepository.saveAndFlush(lastEntry);
                if (updateItemDto.getAction().equals(ItemActionTypes.REJECT.name())) {
                    WorkflowState rejectedState = this.stateRepository.findByName(ItemStatus.REJECTED.name());
                    if (rejectedState != null) {
                        item.setState(rejectedState);
                    }
                    this.itemRepository.save(item);
                    return new ApplicationResponse<>(ResponseCode.SUCCESS, response);
                }
                /* The request is approved */
                response = processApprovedItem(currentActor, lastActor, actors, item, pendingState);

            }
            return new ApplicationResponse<>(ResponseCode.SUCCESS, response);
        }
    }

    private List<PutItemResponse> processApprovedItem(ProcessActor lastActor, ProcessActor currentActor, List<ProcessActor> actors, Item item,
                                                      WorkflowState pendingState) {
        List<PutItemResponse> response = new ArrayList<>();
        if (!currentActor.getSequenceNo().equals(lastActor.getSequenceNo())) {
            //Item is approved in transition level
            //Add new row in item_transaction table for next approve
            ProcessActor nextActor = actors.stream().filter(x -> x.getId().equals(currentActor.getId() + 1)).findFirst().get();
            ItemTransaction nextTransaction = new ItemTransaction(item, null, null,
                    LocalDateTime.now(), null, nextActor);
            this.itemTransactionRepository.saveAndFlush(nextTransaction);
        } else {
            //Item is approved at final stage
            List<ItemDetails> itemDetails = this.itemDetailsRepository.findByItem(item);
            for (ItemDetails itemDetail : itemDetails) {
                /* Skip sending http request if the process is already posted */
                if (itemDetail.getStatus() != null && itemDetail.getStatus().equals(CallbackStatus.SUCCESS.name())) {
                    continue;
                }
                /* Get required headers for the process */
                Map<String, String> requestHeaders = new LinkedHashMap<>();
                if (itemDetail.getSubProcess().getHeaders() != null) {
                    requestHeaders = CustomGeneratedData.convertJSONToObject(Map.class, itemDetail.getSubProcess().getHeaders());
                }
                /* Get  headers based on item */
                if (itemDetail.getActionHeaders() != null) {
                    Map<String, String> itemHeaders = CustomGeneratedData.convertJSONToObject(Map.class, itemDetail.getActionHeaders());
                    requestHeaders.putAll(itemHeaders);
                }
                log.info("************ Sending approved item [" + item.getId() + "] to third party for finalization **********************");
                CallbackRequestResponse callbackResponse = httpRequest(getRequestPayload(itemDetail.getPayload(), itemDetail.getSubProcess()), itemDetail.getSubProcess().getDomain() + itemDetail.getEndpoint(),
                        itemDetail.getSubProcess(), requestHeaders);
                itemDetail.setStatus(callbackResponse.getStatus());
                itemDetail.setMessage(callbackResponse.getRawResponse());
                this.itemDetailsRepository.save(itemDetail);

                if (callbackResponse.getStatus().equals(CallbackStatus.SUCCESS.name())) {
                    response.add(new PutItemResponse(itemDetail.getSubProcess().getName(), CallbackStatus.SUCCESS.name(), "Request approved successfully"));
                } else {
                    response.add(new PutItemResponse(itemDetail.getSubProcess().getName(), CallbackStatus.FAILED.name(),
                            getFailureReason(callbackResponse.getRawResponse(), itemDetail.getSubProcess())));
                }
            }
            /* Update item status */
            List<PutItemResponse> processResponse = response.stream().filter(r -> r.getStatus().equals(CallbackStatus.FAILED.name())).collect(Collectors.toList());
            if (!processResponse.isEmpty()) {
                item.setState(pendingState);
                /* Add last item transaction entry to allow approval retries */
                ItemTransaction newTransaction = new ItemTransaction(item, null, null,
                        LocalDateTime.now(), null, lastActor);
                this.itemTransactionRepository.save(newTransaction);
                this.itemRepository.save(item);
            } else {
                WorkflowState approvedState = this.stateRepository.findByName(ItemStatus.APPROVED.name());
                if (approvedState != null) {
                    item.setState(approvedState);
                }
                this.itemRepository.save(item);
            }
        }
        return response;
    }

    private String getRequestPayload(String payload, SubProcess subProcess) {
        ProcessResponse processResponse = this.processResponseRepository.findBySubProcess(subProcess);
        if (processResponse.getResponseType().equals(ProcessResponseType.XML.name())) {
            return CustomGeneratedData.convertJson2Xml(payload);
        }
        return payload;
    }

    private String getFailureReason(String rawResponse, SubProcess process) {
        ProcessResponse processResponse = this.processResponseRepository.findBySubProcess(process);
        if (processResponse.getResponseType().equals(ProcessResponseType.XML.name())) {
            XmlParserHelper helper = this.xmlParserHelper.parse(rawResponse);
            return helper.extractXmlStringNamed(processResponse.getResponseMessageTag());
        } else if (processResponse.getResponseType().equals(ProcessResponseType.JSON.name())) {
            /* process error message in json response body*/
            return null;
        }
        return "Request approval failed";
    }

    @Override
    public ApplicationResponse<List<ItemResponse>> getPendingItems(String initiator, String role, String process) {
        WorkflowState workflowState = this.stateRepository.findByName(initialItemState);
        if (workflowState == null) {
            log.error("Initial item state is not configured");
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Initial item state is not configured");
        }
        WorkflowRole workflowRole = this.roleRepository.findByName(role);
        if (workflowRole == null) {
            log.error("Role {} does not exists ", role);
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Role does not exists");
        }
        ParentProcess parentProcess = this.parentProcessRepository.findByName(process);
        if (parentProcess == null) {
            log.error("Parent process {} does not exists ", role);
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Parent process does not exists");
        }
        List<PendingItemsProj> itemProjections = this.itemRepository.getPendingItems(initiator, workflowRole.getId(), workflowState.getId(), parentProcess.getId());

        List<ItemResponse> items = new ArrayList<>();
        for (PendingItemsProj proj : itemProjections) {
            Optional<Item> itemOptional = this.itemRepository.findById(proj.getItemId());
            if (itemOptional.isPresent()) {
                Item item = itemOptional.get();
                ItemResponse response = new ItemResponse();
                response.setDescription(item.getDescription());
                response.setInitiator(item.getInitiator());
                response.setReference(item.getExternalReference());
                response.setOrganisation(item.getOrganisation());
                response.setAdditionalProperties(item.getAdditionalProperties());
                response.setItemId(proj.getItemId());

                ParentProcessDto parentProcessDto = new ParentProcessDto();
                parentProcessDto.setDescription(item.getParentProcess().getDescription());
                parentProcessDto.setParentProcessId(item.getParentProcess().getId());
                parentProcessDto.setName(item.getParentProcess().getName());
                List<SubProcessDto> subProcesses = new ArrayList<>();
                for (ItemDetails itemDetail : item.getItemDetails()) {
                    subProcesses.add(new SubProcessDto(itemDetail.getSubProcess().getName(), itemDetail.getEndpoint(),
                            itemDetail.getPayload(), itemDetail.getId()));
                }
                parentProcessDto.setSubProcesses(subProcesses);

                StateDto state = new StateDto();
                state.setStateId(proj.getItemStateId());
                state.setName(proj.getItemStatus());
                state.setDescription(proj.getItemStateDescription());
                response.setState(state);

                response.setParentProcess(parentProcessDto);
                items.add(response);
            }
        }
        return new ApplicationResponse<>(ResponseCode.SUCCESS, items);
    }

    @Override
    public ApplicationResponse<List<Item>> getItems(SearchItemDto searchItemDto) {
        return null;
    }

    @Override
    public ApplicationResponse<List<ItemResponse>> getCompletedItems(String initiator, String role, String requestType, String process) {
        WorkflowState workflowState = this.stateRepository.findByName(initialItemState);
        if (workflowState == null) {
            log.error("Initial item state is not configured");
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Initial item state is not configured");
        }
        WorkflowRole workflowRole = this.roleRepository.findByName(role);
        if (workflowRole == null) {
            log.error("Role {} does not exists ", role);
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Role does not exists");
        }
        ParentProcess parentProcess = this.parentProcessRepository.findByName(process);
        if (parentProcess == null) {
            log.error("Parent process {} does not exists ", role);
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Parent process does not exists");
        }
        List<PendingItemsProj> itemProjections;
        if (requestType.equals("RECEIVED")) {
            itemProjections = this.itemRepository.getReceivedItems(initiator, workflowRole.getId(), workflowState.getId(), parentProcess.getId());
        } else if (requestType.equals("SENT")) {
            itemProjections = this.itemRepository.getSentItems(initiator, workflowRole.getId(), workflowState.getId(), parentProcess.getId());
        } else if (requestType.equals("ALL")) {
            itemProjections = this.itemRepository.getAllItemsByRole(workflowRole.getId(), workflowState.getId(), parentProcess.getId());
        } else {
            log.error("Invalid requestType {}", requestType);
            return new ApplicationResponse<>(ResponseCode.FAIL, null, "Invalid requestType");
        }
        List<ItemResponse> items = new ArrayList<>();
        for (PendingItemsProj proj : itemProjections) {
            Optional<Item> itemOptional = this.itemRepository.findById(proj.getItemId());
            if (itemOptional.isPresent()) {
                Item item = itemOptional.get();
                ItemResponse response = new ItemResponse();
                response.setDescription(item.getDescription());
                response.setInitiator(item.getInitiator());
                response.setReference(item.getExternalReference());
                response.setOrganisation(item.getOrganisation());
                response.setAdditionalProperties(item.getAdditionalProperties());
                response.setItemId(proj.getItemId());

                ParentProcessDto parentProcessDto = new ParentProcessDto();
                parentProcessDto.setDescription(item.getParentProcess().getDescription());
                parentProcessDto.setParentProcessId(item.getParentProcess().getId());
                parentProcessDto.setName(item.getParentProcess().getName());
                List<SubProcessDto> subProcesses = new ArrayList<>();
                for (ItemDetails itemDetail : item.getItemDetails()) {
                    subProcesses.add(new SubProcessDto(itemDetail.getSubProcess().getName(), itemDetail.getEndpoint(),
                            itemDetail.getPayload(), itemDetail.getId()));
                }
                parentProcessDto.setSubProcesses(subProcesses);
                StateDto state = new StateDto();
                state.setStateId(proj.getItemStateId());
                state.setName(proj.getItemStatus());
                state.setDescription(proj.getItemStateDescription());
                response.setState(state);
                response.setParentProcess(parentProcessDto);
                items.add(response);
            }
        }
        return new ApplicationResponse<>(ResponseCode.SUCCESS, items);
    }

    @Override
    public ApplicationResponse<List<ItemTransactionResponse>> getItemHistory(Long itemId) {
        List<ItemTransaction> transactions = this.itemTransactionRepository.findByItemId(itemId);
        List<ItemTransactionResponse> responses = new ArrayList<>();
        for (ItemTransaction transaction : transactions) {
            String actor = transaction.getProcessActor().getActorName() != null ? transaction.getProcessActor().getActorName() : transaction.getProcessActor().getRole().getName();
            responses.add(new ItemTransactionResponse(transaction.getAction(), transaction.getRemarks(), transaction.getActionDate(),
                    transaction.getActor() == null ? actor : transaction.getActor()));
        }
        return new ApplicationResponse<>(ResponseCode.SUCCESS, responses);
    }

    public CallbackRequestResponse httpRequest(String requestBody, String requestURL, SubProcess subProcess, Map<String, String> headersMap) {
        HttpRequestBase httpRequest = new HttpRequestBase() {
            @Override
            public String getMethod() {
                return subProcess.getMethod();
            }
        };

        try {
            StringEntity stringEntity = new StringEntity(requestBody);
            switch (subProcess.getMethod().toUpperCase()) {
                case "PATCH":
                    httpRequest = new HttpPatch(requestURL);
                    ((HttpPatch) httpRequest).setEntity(stringEntity);
                    break;
                case "POST":
                    httpRequest = new HttpPost(requestURL);
                    ((HttpPost) httpRequest).setEntity(stringEntity);
                    break;
                case "PUT":
                    httpRequest = new HttpPut(requestURL);
                    ((HttpPut) httpRequest).setEntity(stringEntity);
                    break;
                case "DELETE":
                    httpRequest = new HttpDelete(requestURL);
                    break;
                default:
                    log.error("Unsupported HTTP method: {}", subProcess.getMethod());
                    return new CallbackRequestResponse(CallbackStatus.FAILED.name(), "Unsupported HTTP method provided");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Request processing exception, Error {}", e.getMessage());
            return new CallbackRequestResponse(CallbackStatus.FAILED.name(), "Request processing exception");
        }
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Integer.parseInt(this.CONNECTION_TIMEOUT))
                .setSocketTimeout(Integer.parseInt(this.READ_TIMEOUT))
                .build();
        httpRequest.setConfig(requestConfig);

        List<String> headers = new ArrayList<>();
        for (Map.Entry<String, String> header : headersMap.entrySet()) {
            httpRequest.addHeader(header.getKey(), header.getValue());
            headers.add(header.toString());
        }
        log.info(" URL  :  {}", requestURL);
        log.info(" Method  :  {}", subProcess.getMethod());
        log.info(" Headers  :  {}", headers);
        log.info(" Request  :  {}", requestBody);
        CloseableHttpResponse response;
        // Define a postRequest request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            response = httpClient.execute(httpRequest);
            String responseString = EntityUtils.toString(response.getEntity());
            log.info("{ Response Code :  " + response.getStatusLine().getStatusCode() + ", Request body : " + requestBody + ",  Response Body : " + responseString + " }");
            response.close();
            if (isSuccessResponse(responseString, subProcess)) {
                return new CallbackRequestResponse(CallbackStatus.SUCCESS.name(), responseString);
            }
            return new CallbackRequestResponse(CallbackStatus.FAILED.name(), responseString);
        } catch (IOException e) {
            log.error("Request processing exception, Error {}", e.getMessage());
            return new CallbackRequestResponse(CallbackStatus.FAILED.name(), "<message>Request processing exception, Error " + e.getMessage() + "</message>");
        }
    }

    private boolean isSuccessResponse(String rawResponse, SubProcess process) {
        ProcessResponse processResponse = this.processResponseRepository.findBySubProcess(process);
        if (processResponse.getResponseType().equals(ProcessResponseType.XML.name())) {
            XmlParserHelper helper = this.xmlParserHelper.parse(rawResponse);
            String responseCode = helper.extractXmlStringNamed(processResponse.getResponseCodeTag());
            return responseCode.equals(processResponse.getSuccessResponseCode());
        } else if (processResponse.getResponseType().equals(ProcessResponseType.JSON.name())) {
            /* get error message in json response body*/
            return true;
        }
        return false;
    }

}

