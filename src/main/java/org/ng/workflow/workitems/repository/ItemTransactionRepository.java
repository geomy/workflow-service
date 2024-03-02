package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.models.Item;
import org.ng.workflow.workitems.models.ItemTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTransactionRepository extends JpaRepository<ItemTransaction,Long> {

    List<ItemTransaction> findByItemOrderByIdDesc(Item item);
    @Query("select trans from ItemTransaction trans where trans.item.id = :id")
    List<ItemTransaction> findByItemId(Long id);
}
