package org.ng.workflow.workitems.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateItemDto {
private String action;
private String comments;
private String actor;
private String role;
}
