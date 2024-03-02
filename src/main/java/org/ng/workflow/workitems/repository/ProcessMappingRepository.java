package org.ng.workflow.workitems.repository;


import org.ng.workflow.workitems.models.ParentProcess;
import org.ng.workflow.workitems.models.ProcessMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessMappingRepository extends JpaRepository<ProcessMapping, Long> {
    List<ProcessMapping> findByParentProcess(ParentProcess parentProcess);
    ProcessMapping findByParentProcessAndSubProcessId(ParentProcess parentProcess, Long subProcessId);
}
