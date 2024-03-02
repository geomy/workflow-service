package org.ng.workflow.workitems.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ng.workflow.common.models.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ProcessResponse extends BaseEntity {
    @ManyToOne()
    @JoinColumn(name = "sub_process_id", referencedColumnName = "id", unique = true)
    private SubProcess subProcess;
    private String responseType;
    private String responseMessageTag;
    private String responseCodeTag;
    private String successResponseCode;

    public ProcessResponse(SubProcess subProcess, String responseType, String responseMessageTag, String responseCodeTag,
                           String successResponseCode){
        this.subProcess=subProcess;
        this.responseType = responseType;
        this.responseMessageTag = responseMessageTag;
        this.responseCodeTag = responseCodeTag;
        this.successResponseCode = successResponseCode;

    }
}
