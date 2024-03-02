package org.ng.workflow.workitems.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name="process_actor")
@Setter
@Getter
@NoArgsConstructor
public class ProcessActor extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "parent_process_id", referencedColumnName = "id")
    private ParentProcess process;
    @ManyToOne()
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private WorkflowRole role;
    private Integer sequenceNo = 0;
    private String actorName;


    public ProcessActor(ParentProcess process, WorkflowRole role, Integer sequenceNo) {
        this.process = process;
        this.role = role;
        this.sequenceNo = sequenceNo;
    }
}
