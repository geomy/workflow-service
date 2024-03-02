package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.WorkflowState;
import org.ng.workflow.workitems.repository.WorkflowStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WorkflowStateSeeder {
    @Autowired
    private WorkflowStateRepository workflowStateRepository;

    public void seed() {
        List<WorkflowState> states = new ArrayList<>();

        states.add(new WorkflowState("IN_PROGRESS", "Item is in pending"));
        states.add(new WorkflowState("REJECTED", "Item is rejected"));
        states.add(new WorkflowState("FAILED", "Item failed approval"));
        states.add(new WorkflowState("APPROVED", "Item is approved"));

        states.forEach((state) -> {
            if (workflowStateRepository.findByName(state.getName()) == null) {
                log.info("**********Seeding workflow state {}  **********", state.getName());;
                state.setCreatedAt(LocalDateTime.now());
                workflowStateRepository.save(state);
            }
        });
    }
}
