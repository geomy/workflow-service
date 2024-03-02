package org.ng.workflow.workitems.repository;

import org.ng.workflow.workitems.dto.PendingItemsProj;
import org.ng.workflow.workitems.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepositoryPostgres extends JpaRepository<Item,Long> {
    @Query(value = "select i.id as itemId,\n" +
            "                       pp.name as \"itemProcess\",\n" +
            "                       s.name as \"itemStatus\",\n" +
            "                       s.id as \"itemStateId\",\n" +
            "                       s.description as \"itemStateDescription\",\n" +
            "                       i.description as \"itemDescription\",\n" +
            "                       i.initiator as \"itemCreatedBy\",\n" +
            "                       i.created_at as \"itemCreatedAt\",\n" +
            "                       it.action_date as \"itemApprovedDate\"\n" +
            "                       from item_transaction it\n" +
            "                       inner join item i on it.item_id = i.id\n" +
            "                       inner join process_actor pa on  it.process_actor_id = pa.id\n" +
            "                       inner join workflow_state s on  i.state_id = s.id\n" +
            "                       inner join workflow_role r on pa.role_id = r.id\n" +
            "                       inner join parent_process pp on  pa.parent_process_id = pp.id\n" +
            "                       where it.actor is null and\n" +
            "                       i.initiator not in(:initiator) and\n" +
            "                        pa.role_id = :roleId and\n" +
            "                        i.parent_process_id = :parentProcessId and\n" +
            "                       i.state_id = :stateId", nativeQuery = true)
    public List<PendingItemsProj> getPendingItems(String initiator, Long roleId, Long stateId, Long parentProcessId);
    @Query(value = "select i.id as \"itemId\", \n" +
            "            pp.name as \"itemProcess\", \n" +
            "            s.name as \"itemStatus\", \n" +
            "            s.id as \"itemStateId\", \n" +
            "            s.description as \"itemStateDescription\", \n" +
            "            i.description as \"itemDescription\", \n" +
            "            i.initiator as \"itemCreatedBy\", \n" +
            "            i.created_at as \"itemCreatedAt\", \n" +
            "            it.action_date as \"itemApprovedDate\" \n" +
            "            from item_transaction it \n" +
            "            inner join item i on i.id = it.item_id \n" +
            "            inner join process_actor pa on pa.id = it.process_actor_id \n" +
            "            inner join workflow_state s on s.id = i.state_id\n" +
            "            inner join workflow_role r on r.id = pa.role_id \n" +
            "            inner join parent_process pp on pp.id = pa.parent_process_id \n" +
            "            where i.actor in(:initiator) and \n" +
            "            pa.role_id in(:roleId) and \n" +
            "            i.parent_process_id = :parentProcessId and\n" +
            "            i.state_id not in(:stateId)", nativeQuery = true)
     List<PendingItemsProj> getReceivedItems(String initiator, Long roleId, Long stateId, Long parentProcessId);
    @Query(value = "select i.id as \"itemId\", \n" +
            "            pp.name as \"itemProcess\", \n" +
            "            s.name as \"itemStatus\", \n" +
            "            s.id as \"itemStateId\", \n" +
            "            s.description as \"itemStateDescription\", \n" +
            "            i.description as \"itemDescription\", \n" +
            "            i.initiator as \"itemCreatedBy\", \n" +
            "            i.created_at as \"itemCreatedAt\", \n" +
            "            it.action_date as \"itemApprovedDate\" \n" +
            "            from item_transaction it \n" +
            "            inner join item i on i.id = it.item_id \n" +
            "            inner join process_actor pa on pa.id = it.process_actor_id \n" +
            "            inner join workflow_state s on s.id = i.state_id\n" +
            "            inner join workflow_role r on r.id = pa.role_id \n" +
            "            inner join parent_process pp on pp.id = pa.parent_process_id \n" +
            "           where  pa.role_id in(:roleId) and \n" +
            "            i.parent_process_id = :parentProcessId and\n" +
            "            i.state_id not in(:stateId)", nativeQuery = true)
    List<PendingItemsProj> getAllItemsByRole(Long roleId, Long stateId, Long parentProcessId);
    @Query(value = "select i.id as \"itemId\", \n" +
            "            pp.name as \"itemProcess\", \n" +
            "            s.name as \"itemStatus\", \n" +
            "            s.id as \"itemStateId\", \n" +
            "            s.description as \"itemStateDescription\", \n" +
            "            i.description as \"itemDescription\", \n" +
            "            i.initiator as \"itemCreatedBy\", \n" +
            "            i.created_at as \"itemCreatedAt\", \n" +
            "            it.action_date as \"itemApprovedDate\" \n" +
            "            from item_transaction it \n" +
            "            inner join item i on i.id = it.item_id \n" +
            "            inner join process_actor pa on pa.id= it.process_actor_id \n" +
            "            inner join workflow_state s on s.id = i.state_id \n" +
            "            inner join workflow_role r on r.id = pa.role_id \n" +
            "            inner join parent_process pp on pp.id = pa.parent_process_id \n" +
            "            where i.initiator in(:initiator) and \n" +
            "            pa.role_id in(:roleId) and \n" +
            "            i.parent_process_id = :parentProcessId and\n" +
            "            i.state_id not in(:stateId)", nativeQuery = true)
     List<PendingItemsProj> getSentItems(String initiator, Long roleId, Long stateId,  Long parentProcessId);
}
