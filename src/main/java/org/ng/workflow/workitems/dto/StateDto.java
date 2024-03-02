package org.ng.workflow.workitems.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StateDto {
    private Long stateId;
    private String name;
    private String description;
}
