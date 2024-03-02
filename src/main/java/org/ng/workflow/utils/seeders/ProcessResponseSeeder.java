package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.ProcessResponse;
import org.ng.workflow.workitems.models.SubProcess;
import org.ng.workflow.workitems.repository.ProcessResponseRepository;
import org.ng.workflow.workitems.repository.SubProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProcessResponseSeeder {
    @Autowired
    private SubProcessRepository subProcessRepository;
    @Autowired
    private ProcessResponseRepository processResponseRepository;

    public void seed() {
        List<String> processes = new ArrayList<>(Arrays.asList("REVERSE_TRANSACTION", "MOBILE_ACTIVATION", "CUSTOMER_REFUND", "CUSTOMER_REFUND_FILE"));
        processes.forEach(process -> {
            SubProcess subProcess = this.subProcessRepository.findByName(process);
            if (subProcess != null) {
                ProcessResponse pr = this.processResponseRepository.findBySubProcess(subProcess);
                if (pr == null) {
                    log.info("****** Seeding response for process {} ************", process);
                    ProcessResponse processResponse = new ProcessResponse(subProcess, "XML", "message", "responseCode", "0");
                    this.processResponseRepository.save(processResponse);
                }
            }
        });
    }
}
