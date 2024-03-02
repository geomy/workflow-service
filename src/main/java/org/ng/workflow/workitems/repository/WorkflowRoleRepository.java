package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.WorkflowRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkflowRoleRepository extends JpaRepository<WorkflowRole, Long> {
    WorkflowRole findByName(String name);
}
