package org.ng.workflow.workitems.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="item")
@Setter
@Getter
@NoArgsConstructor
public class Item extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "parent_process_id", referencedColumnName = "id")
    private ParentProcess parentProcess;
    @ManyToOne()
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private WorkflowState state;
    private String description;
    private String organisation;
    private String initiator;
    @Column(name = "additional_properties",columnDefinition = "text" )
    private String additionalProperties;
    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<ItemTransaction> itemTransactions;

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<ItemDetails> itemDetails;
    @Transient
    private String actorDisplayName;
    private String externalReference;

    public Item(ParentProcess parentProcess, WorkflowState state, String description) {
        this.parentProcess = parentProcess;
        this.state = state;
        this.description = description;
    }
}
