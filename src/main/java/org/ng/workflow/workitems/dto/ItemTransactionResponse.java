package org.ng.workflow.workitems.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemTransactionResponse {
    private String action;
    private String remarks;
    private LocalDateTime actionDate;
    private String actor;
}
