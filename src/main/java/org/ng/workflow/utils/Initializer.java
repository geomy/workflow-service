package org.ng.workflow.utils;

import lombok.extern.slf4j.Slf4j;
import org.ng.workflow.utils.seeders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Initializer implements ApplicationRunner {

    @Autowired
    private SubProcessSeeder subProcessSeeder;
    @Autowired
    private ProcessResponseSeeder processResponseSeeder;
    @Autowired
    private ParentProcessSeeder parentProcessSeeder;
    @Autowired
    private ProcessMappingSeeder processMappingSeeder;
    @Autowired
    private WorkflowRoleSeeder workflowRoleSeeder;
    @Autowired
    private WorkflowStateSeeder workflowStateSeeder;
    @Autowired
    private ProcessActorSeeder processActorSeeder;
    @Value("${disable.seeder.runner}")
    private boolean seederRunnerDisabled;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (seederRunnerDisabled) {
            return;
        }
        log.info("****** START: Running seeders *****");
        this.subProcessSeeder.seed();
        this.processResponseSeeder.seed();

        this.parentProcessSeeder.seed();
        this.processMappingSeeder.see();

        this.workflowRoleSeeder.seed();
        this.workflowStateSeeder.seed();
        this.processActorSeeder.seed();

        log.info("****** END: Running seeders *****");
    }
}
