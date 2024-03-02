package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.WorkflowRole;
import org.ng.workflow.workitems.repository.WorkflowRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WorkflowRoleSeeder {
    @Autowired
    private WorkflowRoleRepository workflowRoleRepository;

    public void seed() {
        List<WorkflowRole> roles = new ArrayList<>();

        roles.add(new WorkflowRole("OPERATION", "Role to initiate transactions operations"));
        roles.add(new WorkflowRole("CHECKER", "Role to approved transactions operations"));
        roles.add(new WorkflowRole("SUPER_ADMIN", "Role to initiate mobile activation"));

        roles.forEach((role) -> {
            if (workflowRoleRepository.findByName(role.getName()) == null) {
                log.info("**********Seeding workflow state {}  **********", role.getName());
                role.setCreatedAt(LocalDateTime.now());
                workflowRoleRepository.save(role);
            }
        });
    }
}
