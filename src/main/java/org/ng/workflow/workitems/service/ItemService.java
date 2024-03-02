package org.ng.workflow.workitems.service;

import org.ng.workflow.utils.ApplicationResponse;
import org.ng.workflow.workitems.dto.*;
import org.ng.workflow.workitems.models.Item;

import java.util.List;

public interface ItemService {
    public ApplicationResponse<PostItemPayload> createWorkItem(PostItemPayload itemDto);
    public ApplicationResponse<List<PutItemResponse>> updateWorkItem(UpdateItemDto updateItemDto, Long id);
    public ApplicationResponse<List<ItemResponse>> getPendingItems(String initiator, String role, String process);

    ApplicationResponse<List<Item>> getItems(SearchItemDto searchItemDto);
    ApplicationResponse<List<ItemResponse>> getCompletedItems(String initiator, String role, String requestType, String process);
    ApplicationResponse<List<ItemTransactionResponse>>  getItemHistory(Long itemId);
}
