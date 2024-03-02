package org.ng.workflow.workitems.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ItemResponse {
    private StateDto state;
    private ParentProcessDto parentProcess;
    private String description;
    private String reference;
    private String organisation;
    private String initiator;
    private String additionalProperties;
    private Long itemId;
}
