package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.Item;
import org.ng.workflow.workitems.models.ItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ItemDetailsRepository extends JpaRepository<ItemDetails, Long> {
    List<ItemDetails> findByItem(Item item);
}
