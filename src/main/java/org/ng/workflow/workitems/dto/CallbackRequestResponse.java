package org.ng.workflow.workitems.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CallbackRequestResponse {
    private String status;
    private String rawResponse;
    public CallbackRequestResponse(String status) {
        this.status = status;
    }
}
