package org.ng.workflow.workitems.dto;


import lombok.*;

import java.util.Map;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubProcessDto {
    private String name;
    private String actionEndpoint;
    private String actionPayload;
    private Long processId;
}
