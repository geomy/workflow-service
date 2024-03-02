package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.ParentProcess;
import org.ng.workflow.workitems.models.SubProcess;
import org.ng.workflow.workitems.repository.ParentProcessRepository;
import org.ng.workflow.workitems.repository.SubProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SubProcessSeeder {
    @Autowired
    private SubProcessRepository subProcessRepository;

    public void seed() {
        List<SubProcess> parentProcessList = new ArrayList<>();

        parentProcessList.add(new SubProcess("REVERSE_TRANSACTION", "Process to reverse transaction","POST","http://localhost:8091","{\"content-type\":\"text/xml\"}"));
        parentProcessList.add(new SubProcess("MOBILE_ACTIVATION", "Activate mobile phone","POST","http://localhost:8091","{\"content-type\":\"text/xml\"}"));
        parentProcessList.add(new SubProcess("CUSTOMER_REFUND", "Refund customer transaction","POST","http://localhost:8091","{\"content-type\":\"text/xml\"}"));
        parentProcessList.add(new SubProcess("CUSTOMER_REFUND_FILE", "Refund customer transaction by file","POST","http://localhost:8091","{\"content-type\":\"text/xml\"}"));

        parentProcessList.forEach((process) -> {
            if (subProcessRepository.findByName(process.getName()) == null) {
                log.info("**********Seeding sub process {}  **********", process.getName());
                subProcessRepository.save(process);
            }
        });
    }
}
