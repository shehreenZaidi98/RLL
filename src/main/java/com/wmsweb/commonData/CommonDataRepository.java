// 
// Decompiled by Procyon v0.5.36
// 

package com.wmsweb.commonData;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CommonDataRepository extends CrudRepository<CommonData, Long> {
	//insert into common_data(order_id,type,priority,date,status,truck_bay_no)SELECT * FROM (SELECT ?1,?2,?3,?4,0,?5) as TEMP WHERE NOT EXISTS ( SELECT order_id FROM common_data WHERE order_id =?1 and type=?2 )
    @Modifying
    @Query(value = "insert into common_data(order_id,type,priority,date,status,truck_bay_no)Values(?1,?2,?3,?4,0,?5)", nativeQuery = true)
    @Transactional
    public int insertData(long order_id, String type, String priority, String date,String truck_bay_no);

    @Modifying
    @Query(value = "update  common_data set status=?2 where id=?1", nativeQuery = true)
    @Transactional
    public int updateCommonDataStatus(long id, int status);

    @Modifying
    @Query(value = "update  common_data set status=3 where order_id=?1", nativeQuery = true)
    @Transactional
    public int runCancelOrder(long id);

    @Modifying
    @Query(value = "update  common_data set status=2 where order_id=?1", nativeQuery = true)
    @Transactional
    public int cancelOrder(long id);

    @Query("select cd from CommonData cd where status !=2 and status!=4")
    public List<CommonData> getCommonData();

    @Query("select cd from CommonData cd where id=?1")
    public List<CommonData> getCommonData(long id);

    @Query("select cd from CommonData cd where order_id=?1 ")
    public List<CommonData> getCommonData1(long order_id);

    @Query("select cd from CommonData cd where status=1")
    public List<CommonData> getCommonData1();

    @Modifying
    @Query(value = "update  common_data set status=2 where order_id=?1", nativeQuery = true)
    @Transactional
    public int orderComplete(long order_id);
}
