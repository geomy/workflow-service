package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.ParentProcess;
import org.ng.workflow.workitems.repository.ParentProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ParentProcessSeeder {
    @Autowired
    private ParentProcessRepository parentProcessRepository;

    public void seed() {
        List<ParentProcess> parentProcessList = new ArrayList<>();

        parentProcessList.add(new ParentProcess("REVERSE_TRANSACTION", "Process to reverse transaction"));
        parentProcessList.add(new ParentProcess("MOBILE_ACTIVATION", "Activate mobile phone"));
        parentProcessList.add(new ParentProcess("CUSTOMER_REFUND", "Refund customer transaction"));
        parentProcessList.add(new ParentProcess("CUSTOMER_REFUND_FILE", "Refund customer transaction by file"));

        parentProcessList.forEach((process) -> {
            if (parentProcessRepository.findByName(process.getName()) == null) {
                log.info("**********Seeding parent processes {}  **********", process.getName());
                process.setCreatedAt(LocalDateTime.now());
                parentProcessRepository.save(process);
            }
        });
    }
}
