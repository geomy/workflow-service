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
public class SubProcess extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;
    private String description;
    private String method;
    private String domain;
    private String headers;
    public SubProcess(String name, String description, String method, String domain, String headers) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.domain=domain;
        this.headers=headers;
    }
}
