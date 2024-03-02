package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.WorkflowState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowStateRepository extends JpaRepository<WorkflowState,Long> {
    WorkflowState findByName(String name);
}
