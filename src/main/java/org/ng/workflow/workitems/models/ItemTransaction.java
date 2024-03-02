package org.ng.workflow.workitems.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item_transaction")
@Setter
@Getter
@NoArgsConstructor
public class ItemTransaction extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @ManyToOne()
    @JoinColumn(name = "process_actor_id", referencedColumnName = "id")
    private ProcessActor processActor;
    private String action;
    private String remarks;
    private LocalDateTime actionDate;
    private String actor;
    @Transient
    private String actorDisplayName;
    public ItemTransaction(Item item, String action, String remarks, LocalDateTime actionDate, String actor,
                           ProcessActor processActor) {
        this.item = item;
        this.action = action;
        this.remarks = remarks;
        this.actionDate = actionDate;
        this.actor = actor;
        this.processActor = processActor;
    }
}
