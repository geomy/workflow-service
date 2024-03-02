package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.ParentProcess;
import org.ng.workflow.workitems.models.ProcessMapping;
import org.ng.workflow.workitems.models.SubProcess;
import org.ng.workflow.workitems.repository.ParentProcessRepository;
import org.ng.workflow.workitems.repository.ProcessMappingRepository;
import org.ng.workflow.workitems.repository.SubProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProcessMappingSeeder {

    @Autowired
    private ProcessMappingRepository processMappingRepository;
    @Autowired
    private ParentProcessRepository parentProcessRepository;
    @Autowired
    private SubProcessRepository subProcessRepository;

    public void see() {
        List<String> processes = new ArrayList<>(Arrays.asList("REVERSE_TRANSACTION", "MOBILE_ACTIVATION", "CUSTOMER_REFUND", "CUSTOMER_REFUND_FILE"));
        processes.forEach(process -> {
            ParentProcess parentProcess = this.parentProcessRepository.findByName(process);
            SubProcess subProcess = this.subProcessRepository.findByName(process);
            if (parentProcess != null && subProcess != null) {
                ProcessMapping pm = this.processMappingRepository.findByParentProcessAndSubProcessId(parentProcess, subProcess.getId());
                if (pm == null) {
                    log.info("****** Seeding process mapping {} ************", process);
                    ProcessMapping processMapping = new ProcessMapping();
                    processMapping.setParentProcess(parentProcess);
                    processMapping.setSubProcessId(subProcess.getId());
                    processMapping.setSequenceNo(1);
                    this.processMappingRepository.save(processMapping);
                }
            }
        });
    }
}
