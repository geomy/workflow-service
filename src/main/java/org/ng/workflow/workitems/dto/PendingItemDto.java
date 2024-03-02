package org.ng.workflow.workitems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingItemDto {
    private Long itemId;
    private String process;
    private String description;
    private String status;
    private String createdBy;
    private LocalDateTime createDate;
    private LocalDateTime approvedDate;
    private List<ItemTransactionDto> comments;
    private String payload;
    public PendingItemDto(Long itemId, String process, String description, String status, LocalDateTime createDate, LocalDateTime approvedDate, List<ItemTransactionDto> comments) {
        this.itemId = itemId;
        this.process = process;
        this.description = description;
        this.status = status;
        this.createDate = createDate;
        this.approvedDate = approvedDate;
        this.comments = comments;
    }
}
