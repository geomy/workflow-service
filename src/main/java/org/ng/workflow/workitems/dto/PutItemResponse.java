package org.ng.workflow.workitems.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutItemResponse {
    private String process;
    private String status;
    private String message;
}
