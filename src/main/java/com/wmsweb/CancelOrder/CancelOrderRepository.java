package com.wmsweb.CancelOrder;

import com.wmsweb.FifoViolate.FifoViolate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CancelOrderRepository extends CrudRepository<CancelOrder,Long> {
    @Modifying
    @Query(value = "insert into cancel_order(bay,sku,qty,batch)values(?1,?2,?3,?4)", nativeQuery = true)
    @Transactional
    int insertCancelOrder(String bay,String sku,String qty,String batch);

    @Modifying
    @Query(value = "update cancel_order set bay=?1,qty=?2,batch=?3 where sku=?4", nativeQuery = true)
    @Transactional
    int updateCancelOrder(String bay,String qty,String batch,String sku);


    @Query("select b1 from CancelOrder b1 where sku=?1")
    List<CancelOrder> getSku(String sku);


}
