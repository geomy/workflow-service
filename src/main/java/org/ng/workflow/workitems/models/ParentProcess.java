package org.ng.workflow.workitems.models;


import lombok.*;
import org.ng.workflow.common.models.BaseEntity;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "parent_process")
public class ParentProcess extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;
    private String description;

    public ParentProcess(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
