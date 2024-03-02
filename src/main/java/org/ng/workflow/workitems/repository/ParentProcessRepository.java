package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.ParentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentProcessRepository extends JpaRepository<ParentProcess, Long> {
    ParentProcess findByName(String name);
}
