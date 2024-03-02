package org.ng.workflow.workitems.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class ParentProcessDto {
    private String name;
    private String description;
    private Long parentProcessId;
    private List<SubProcessDto> subProcesses;
}
