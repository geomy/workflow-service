package org.ng.workflow.workitems.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ItemDto {
    private String process;
    private String description;
    private String payload;
    private String additionalDetails;
    private String initiator;
}
