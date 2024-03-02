package org.ng.workflow.workitems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTransactionDto {
    private String actor;
    private String action;
    private String remarks;
    private LocalDateTime actionDate;
}
