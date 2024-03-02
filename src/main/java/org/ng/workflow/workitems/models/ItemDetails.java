package org.ng.workflow.workitems.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="item_details")
@Setter
@Getter
@NoArgsConstructor
public class ItemDetails extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "itemId", referencedColumnName = "id")
    private Item item;
    @ManyToOne()
    @JoinColumn(name = "process_id", referencedColumnName = "id")
    private SubProcess subProcess;
    private String endpoint;
    @Column(name = "payload", columnDefinition = "text")
    private String payload;
    private String actionHeaders;
    private Integer sequenceNo = 0;
    private String status;
    @Column(name = "message", columnDefinition = "text")
    private String message;
   public ItemDetails(Item item, SubProcess subProcess, String endpoint, String payload, String actionHeaders, int sequenceNo){
       this.item = item;
       this.subProcess = subProcess;
       this.endpoint = endpoint;
       this.payload =payload;
       this.actionHeaders = actionHeaders;
       this.sequenceNo = sequenceNo;
   }

}
