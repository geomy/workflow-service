package org.ng.workflow.workitems.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table()
public class WorkflowState extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;
    private String description;
    public WorkflowState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
