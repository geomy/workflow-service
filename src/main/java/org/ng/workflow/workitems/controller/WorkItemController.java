package org.ng.workflow.workitems.controller;

import java.util.List;

import io.swagger.annotations.Api;
import org.ng.workflow.utils.ApplicationResponse;
import org.ng.workflow.utils.ResponseCode;
import org.ng.workflow.workitems.dto.*;
import org.ng.workflow.workitems.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/items")
@Api(tags = "Work Items")
public class WorkItemController {
    @Autowired
    ItemService itemService;

    @GetMapping(value = "pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationResponse<List<ItemResponse>>> getPendingItems(
            @RequestParam String initiator, @RequestParam String role,
            @RequestParam String process) {
        ApplicationResponse<List<ItemResponse>> response = this.itemService.getPendingItems(initiator, role, process);
        if (response.getCode().equals(ResponseCode.SUCCESS)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <ApplicationResponse<List<ItemResponse>>> getCompletedItems(
            @RequestParam String initiator,
            @RequestParam String role,
            @RequestParam String requestType,
            @RequestParam(required = false) String process) {

        ApplicationResponse<List<ItemResponse>> response = new ApplicationResponse<>(ResponseCode.FAIL, null);
        if (requestType.equals("PENDING")) {
            response = this.itemService.getPendingItems(initiator, role, process);
        } else {
            response = this.itemService.getCompletedItems(initiator, role, requestType, process);
        }
        if (response.getCode().equals(ResponseCode.SUCCESS)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationResponse<PostItemPayload>> postItem(@RequestBody() PostItemPayload itemDto) {
        ApplicationResponse<PostItemPayload> response = this.itemService.createWorkItem(itemDto);
        if (response.getCode().equals(ResponseCode.SUCCESS)) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationResponse<List<PutItemResponse>>> updateItem(@RequestBody() UpdateItemDto itemDto, @PathVariable(name = "id") Long id) {
        ApplicationResponse<List<PutItemResponse>> response = this.itemService.updateWorkItem(itemDto, id);
        if (response.getCode().equals(ResponseCode.SUCCESS)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/{itemId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemTransactionResponse>> getItemHistory(
            @PathVariable Long itemId) {
        List<ItemTransactionResponse> response = this.itemService.getItemHistory(itemId).getData();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
