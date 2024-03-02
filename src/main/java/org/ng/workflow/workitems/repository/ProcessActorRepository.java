package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.ParentProcess;
import org.ng.workflow.workitems.models.ProcessActor;
import org.ng.workflow.workitems.models.WorkflowRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ProcessActorRepository extends JpaRepository<ProcessActor,Long> {
List<ProcessActor> findByProcessOrderBySequenceNoAsc(ParentProcess process);
ProcessActor findByProcessAndRole(ParentProcess process, WorkflowRole role);

}
