
package com.wmsweb.productionPlan;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductionPlanRepository extends CrudRepository<ProductionPlan, Long> {
    @Modifying
    @Query(value = "insert into production_plan(batch_no,date,qty,sku,line_no)values(?1,?2,?3,?4,?5)", nativeQuery = true)
    @Transactional
    int insertProduction_plan(String batch_no, String date, int qty, String sku, String line_no);

    @Modifying
    @Query(value = "update production_plan set qty=?1 where sku=?2 and line_no=?3", nativeQuery = true)
    @Transactional
    int updateProduction_plan(int qty, String sku, String line_no);

    @Query("select pp from ProductionPlan pp where sku=?1 order by batch_no desc")
    List<ProductionPlan> getProductionPlan(String sku);

    @Query("select pp from ProductionPlan pp where  date between ?1 and ?2")
    List<ProductionPlan> getTodayProductionPlan(String fromDate, String toDate);

    @Query("select pp from ProductionPlan pp where sku=?1 and date between ?2 and ?3 and line_no=?4 and batch_no=?5")
    List<ProductionPlan> getTodayProductionPlan(String sku,String fromDate, String toDate,String line_no,
                                                String batch_no);
    @Query("select pp.line_no from ProductionPlan pp")
    List<String>getLineNo();
    
    @Query("select sk from ProductionPlan sk ")
    List<ProductionPlan> getSkuList();

    @Query("select sk from ProductionPlan sk where sku=?1 ")
    List<ProductionPlan> getSkuList(String sku);
    /*
     * @Query("select pp.qty from ProductionPlan pp where sku=?1") public int
     * get_qty(String sku);
     */
}
