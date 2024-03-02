package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.SubProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubProcessRepository extends JpaRepository<SubProcess, Long> {
    SubProcess findByName(String name);
}
