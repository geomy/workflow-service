package org.ng.workflow.utils.seeders;


import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.workitems.models.ParentProcess;
import org.ng.workflow.workitems.models.ProcessActor;
import org.ng.workflow.workitems.models.WorkflowRole;
import org.ng.workflow.workitems.repository.ParentProcessRepository;
import org.ng.workflow.workitems.repository.ProcessActorRepository;
import org.ng.workflow.workitems.repository.WorkflowRoleRepository;
import org.ng.workflow.workitems.repository.WorkflowStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProcessActorSeeder {
    @Autowired
    private ParentProcessRepository parentProcessRepository;
    @Autowired
    private ProcessActorRepository processActorRepository;
    @Autowired
    private WorkflowRoleRepository workflowRoleRepository;

    public void seed() {
        List<String> processes = new ArrayList<>(Arrays.asList("REVERSE_TRANSACTION", "MOBILE_ACTIVATION", "CUSTOMER_REFUND",
                "CUSTOMER_REFUND_FILE"));
        processes.forEach(process -> {
            if (process.equals("REVERSE_TRANSACTION") || process.equals("CUSTOMER_REFUND") || process.equals("CUSTOMER_REFUND_FILE")) {
                ParentProcess parentProcess = this.parentProcessRepository.findByName(process);
                List<String> roles = new ArrayList<>(Arrays.asList("OPERATION", "CHECKER"));

                int sequence = 1;
                for (String role : roles) {
                    WorkflowRole workflowRole = this.workflowRoleRepository.findByName(role);
                    if (workflowRole != null) {
                        ProcessActor pa = this.processActorRepository.findByProcessAndRole(parentProcess, workflowRole);
                        if (pa == null) {
                            log.info("******* Seeding process role actor {}", workflowRole.getName());
                            ProcessActor actor = new ProcessActor(parentProcess, workflowRole, sequence);
                            this.processActorRepository.save(actor);
                        }
                    }
                    sequence++;
                }
            }
            if (process.equals("MOBILE_ACTIVATION")) {
                ParentProcess parentProcess = this.parentProcessRepository.findByName(process);
                List<String> roles = new ArrayList<>(Arrays.asList("SUPER_ADMIN", "CHECKER"));
                int sequence = 1;
                for (String role : roles) {
                    WorkflowRole workflowRole = this.workflowRoleRepository.findByName(role);
                    if (workflowRole != null) {
                        ProcessActor pa = this.processActorRepository.findByProcessAndRole(parentProcess, workflowRole);
                        if (pa == null) {
                            log.info("******* Seeding process role actor {}", workflowRole.getName());
                            ProcessActor actor = new ProcessActor(parentProcess, workflowRole, sequence);
                            this.processActorRepository.save(actor);
                        }
                    }
                    sequence++;
                }
            }
        });
    }
}
