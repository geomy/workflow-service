package org.ng.workflow.workitems.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostItemPayload {
    private ParentProcessReq parentProcess;
    private String description;
    private String reference;
    private String organisation;
    private String initiator;
    private Map<String, String> additionalProperties;
}
