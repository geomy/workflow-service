package org.ng.workflow.workitems.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParentProcessReq {
    public String name;
    public ArrayList<SubProcessReq> subProcesses;
}
