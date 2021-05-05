


package com.wmsweb.SortingPurchase;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SortingPurchaseRepository extends CrudRepository<SortingPurchase, Long> {
    @Modifying
    @Query(value = "insert into sorting_purchase(order_id,permit_no,sku,batch_no,bay,qty,status,date)values(?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
    @Transactional
    int insertData(long order_id, String permit_no, String sku, String batch_no, String bay, int qty, int Status, String date);

    @Modifying
    @Query(value = "update sorting_purchase set status=?2 where id=?1", nativeQuery = true)
    @Transactional
    int updateSortingPurchaseStatus(long id, int status);

    @Query("select sp from SortingPurchase sp where status=0")
    List<SortingPurchase> getSortingPurchase();

    @Query("select sp from SortingPurchase sp where sku=?1 and batch_no=?2 and bay=?3")
    List<SortingPurchase> getSortingPurchase(String sku,String batch_no,String bay);

    @Query("select sp from SortingPurchase sp where status=0 and sku=?1")
    List<SortingPurchase> getSortingPurchase(String sku);

    @Query("select od from SortingPurchase od where order_id=?1")
    List<SortingPurchase> getOrderProduct(long order_id);

    @Modifying
    @Query(value = "update sorting_purchase set status=3 where order_id=?1", nativeQuery = true)
    @Transactional
    int runUpdateWithOrderId(long order_id);

    @Modifying
    @Query(value = "update sorting_purchase set status=4 where order_id=?1", nativeQuery = true)
    @Transactional
    int updateWithOrderId(long order_id);

    @Modifying
    @Query(value = "update sorting_purchase set status=1 where order_id=?1", nativeQuery = true)
    @Transactional
    int updateStatus(long order_id);

    @Modifying
    @Query(value = "update sorting_purchase set qty=?1 where order_id=?2 and batch_no=?3 and sku=?4 and bay=?5", nativeQuery = true)
    @Transactional
    public int updateQty(int qty, long order_id, String batch_no, String sku, String bay);

    @Modifying
    @Query(value = "update sorting_purchase set qty=?1,status=?6 where order_id=?2 and batch_no=?3 and sku=?4 and bay=?5", nativeQuery = true)
    @Transactional
    public int updateQty(int qty, long order_id, String batch_no, String sku, String bay,int status);

    @Modifying
    @Query(value = "update sorting_purchase set qty=0 where order_id=?1", nativeQuery = true)
    @Transactional
    public int updateQty( long order_id);

    @Modifying
    @Query(value = "delete from  sorting_purchase where qty=0 or status=4", nativeQuery = true)
    @Transactional
    public int deleteQty();

    @Modifying
    @Query(value = "delete from  sorting_purchase where order_id=?1", nativeQuery = true)
    @Transactional
    public int deleteQty(long order_id);


    @Modifying
    @Query(value = "update sorting_purchase set qty=?1 where order_id=?2 and sku=?3 and batch_no=?4 and bay=?5", nativeQuery = true)
    @Transactional
    public int updateSortingQty(int qty, long order_id,String sku,String batch_no,String bay);

    @Query("select od from SortingPurchase od where order_id=?1 and sku=?2 and batch_no=?3 and bay=?4")
    List<SortingPurchase> getSortingQty(long order_id,String sku,String batch_no,String bay);


}
