package org.ng.workflow.workitems.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "process_mapping")
public class ProcessMapping extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "parent_process_id", referencedColumnName = "id")
    private ParentProcess parentProcess;
    private Long subProcessId;
    private int sequenceNo = 0;
}
