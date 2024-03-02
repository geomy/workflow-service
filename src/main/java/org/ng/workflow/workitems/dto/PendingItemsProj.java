package org.ng.workflow.workitems.dto;

import java.time.LocalDateTime;

 public  interface PendingItemsProj {
     Long getItemId();
     String getItemProcess();
     String getItemStatus();
     String getItemDescription();
     String getItemCreatedBy();
     LocalDateTime getItemCreatedAt();
     LocalDateTime getItemApprovedDate();
     String getItemPayload();
     Long getItemStateId();
     String getItemStateDescription();

}
