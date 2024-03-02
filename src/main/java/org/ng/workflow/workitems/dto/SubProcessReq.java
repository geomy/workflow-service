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
public class SubProcessReq {
    public String name;
    public String endpoint;
    public String payload;
    private Map<String, String> actionHeaders;
}
