package org.ng.workflow.workitems.repository;


import org.ng.workflow.workitems.models.ProcessResponse;
import org.ng.workflow.workitems.models.SubProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessResponseRepository extends JpaRepository<ProcessResponse, Long> {
    ProcessResponse findBySubProcess(SubProcess subProcess);
}
