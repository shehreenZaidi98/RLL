package com.wmsweb.FilterQty;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository

public interface FilterQtyRepo extends CrudRepository<FilterQty, Long> {

    @Modifying
    @Query(value = "insert into filter_qty (sku,qty,date,line_no,batch_no,bay)values(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    @Transactional
    public int insertData(String sku, int qty, String date, String line_no, String batch_no,String bay);


    @Modifying
    @Query(value = "update  filter_qty set qty=?2 where  date=?3 and sku=?1 and bay=?6 and line_no=?4 and batch_no=?5", nativeQuery = true)
    @Transactional
    public int updateData(String sku, int qty, String date, String line_no, String batch_no,String bay);


    @Query("select p from FilterQty p where date=?1 and sku=?2 and line_no=?3 and batch_no=?4")
    public List<FilterQty> getFilterData(String date, String sku, String line_no, String batch_no);

    @Query("select p from FilterQty p where date=?1 and sku=?2 and line_no=?3 and batch_no=?4 and bay=?5")
    public List<FilterQty> getFilterData(String date, String sku, String line_no, String batch_no,String bay);

    @Query("select p from FilterQty p where date=?1")
    public List<FilterQty> getAllData(String date);
    
     @Query("select p from FilterQty p where date between ?1 and ?2")
    public List<FilterQty> getAllData(String date,String to);



}
