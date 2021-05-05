

package com.wmsweb.production;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductionRepository extends CrudRepository<Production, Long> {
    @Modifying
    @Query(value = "insert into production(batch_no,date,qty,sku,bay_no,status)values(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    @Transactional
    int insertProduction(String batch_no, String date, int qty, String sku, String bay_no, String status);

    @Modifying
    @Query(value = "update production set date=?2,qty=?3,status=?6 where batch_no=?1 and sku=?4 and bay_no=?5 and status=?6", nativeQuery = true)
    @Transactional
    int updateProduction(String batch_no, String date, int qty, String sku, String bay_no, String status);

    @Query("select p from Production p where batch_no=?1 and sku=?2 and bay_no=?3 and status=?4")
    List<Production> getProductionData(String batch_no, String sku, String bay_no, String status);

    @Modifying
    @Query(value = "delete from production where qty=0", nativeQuery = true)
    @Transactional
    int deleteProduction();

    @Query("select p from Production p ")
    List<Production> getAllProductionData();
    
    @Query("select p.sku from Production p where status='PASS'")
    List<String> getAllStateList();

    @Query("select p from Production p where day(date)=day(CURDATE()-1) ")
    List<Production> getTodayProductionData();

    @Query("select p from Production p where bay_no=?1")
    List<Production> getAllProductionData(String bayNo);

    @Query("select p from Production p where sku=?1 and status=?2 order by batch_no,date asc")
    List<Production> getProductionData(String sku, String status);

    @Query("select p.sku from Production p where sku like %?1% ")
    List<String> getAllSku(String sku);

    @Query("select sk from Production sk where sku=?1 and status=?2 ")
    List<Production> getQuantity(String sku, String status);

  @Query("select sk from Production sk where sku=?1 and status=?2 and bay_no=?3 and batch_no=?4")
    List<Production> getQuantity(String sku, String status,String bay_no,String batch_no);

    @Query("select p.batch_no from Production p ")
    List<String> getBatchNo();

    @Query("select p from Production p where sku=?1 OR batch_no=?2 OR bay_no=?3")
    List<Production> getSearchProduct(String sku, String batch_no, String bay_no);

    @Query("select p from Production p")
    public List<Production> getProductionComplete();

    @Query("select p.batch_no from Production p where sku=?1")
    public List<String> getBatchNo(String sku);

    @Query("select p.bay_no from Production p where sku=?1 and batch_no=?2")
    public List<String> getBay(String sku,String batch_no);


    @Query("select SUM(p.qty) from Production p where  sku like %?1% and status='PASS'")
    int getAllQty(String sku);


}
