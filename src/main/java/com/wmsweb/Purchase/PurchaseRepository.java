// 
// Decompiled by Procyon v0.5.36
// 

package com.wmsweb.Purchase;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    @Modifying
    @Query(value = "insert into purchase (order_id,permit_no,sku,qty,date,status)values(?1,?2,?3,?4,?5,0)", nativeQuery = true)
    @Transactional
    int insertData(long order_id, String permit_no, String sku, int qty, String date);

    @Modifying
    @Query(value = "insert into purchase (order_id,permit_no,sku,qty,date,status,bay_no,batch_no)values(?1,?2,?3,?4,?5,1,?6,?7)", nativeQuery = true)
    @Transactional
    int insertData(long order_id, String permit_no, String sku, int qty, String date,String bay,String batch_no);

    @Modifying
    @Query(value = "update purchase set status=1 where order_id=?1", nativeQuery = true)
    @Transactional
    int updatePurchaseStatus(long order_id);

    @Modifying
    @Query(value = "update purchase set qty=?1 where order_id=?2 and sku=?3", nativeQuery = true)
    @Transactional
    int updatePurchaseQty(int qty,long order_id,String sku);

    @Query("select pr from Purchase pr where status=0")
    List<Purchase> getPurchaseDetails();

    @Query("select pr from Purchase pr where status=0 and sku=?1 and order_id=?2")
    List<Purchase> getPurchaseDetails(String sku, long order_id);

    @Query("select pr from Purchase pr where order_id=?1 and sku=?2")
    List<Purchase> getPurchaseList(long order_id,String sku);

    @Query("select pr from Purchase pr where order_id=?1")
    List<Purchase> getQuantity(long order_id);

    @Query("select pr from Purchase pr where sku=?1 and order_id=?2")
    List<Purchase> update(String sku,long order_id);

    @Modifying
    @Query(value = "update purchase set status=3 where order_id=?1", nativeQuery = true)
    @Transactional
    int runChangeStatusWithOrderId(long order_id);

    @Modifying
    @Query(value = "update purchase set status=4 where order_id=?1", nativeQuery = true)
    @Transactional
    int changeStatusWithOrderId(long order_id);


    @Modifying
    @Query(value = "update purchase set qty=?1 where order_id=?2", nativeQuery = true)
    @Transactional
    int updatePurchaseQty(int qty,long order_id);


    @Modifying
    @Query(value = "update  purchase set status=2 where order_id=?1", nativeQuery = true)
    @Transactional
    public int purchaseOrderComplete(long order_id);

    @Query("select od from Purchase od where order_id=?1 ")
    List<Purchase> getPurchseQuantity(long order_id);

    @Modifying
    @Query(value = "update  purchase set qty=?1 where order_id=?2 and sku=?3", nativeQuery = true)
    @Transactional
    public int purchaseQuantity(int qty, long order_id,String sku);

    @Modifying
    @Query(value = "update purchase set qty=?1 where order_id=?2 and sku=?3 and batch_no=?4 and bay=?5", nativeQuery = true)
    @Transactional
    public int updatePurchaseQty(int qty, long order_id,String sku,String batch_no,String bay);

    @Query("select od from Purchase od where order_id=?1 and sku=?2")
    List<Purchase> getPurchseSkuQuantity(long order_id,String sku);

    @Query("select od from Purchase od where order_id=?1 and sku=?2 and batch_no=?3 and bay=?4")
    List<Purchase>getPurchaseQtyBatchNo(long order_id,String sku,String batch_no,String bay);




}
