
package com.wmsweb.transport;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface TransportRepository extends CrudRepository<Transport, Long> {
    @Query("select t.driver_name from Transport t")
    Set<String> getDriverName();

    @Query("select t.contact_no from Transport t")
    Set<String> getContactNo();

    @Query("select t.vehicle_no from Transport t")
    Set<String> getVehicleNo();

    @Query("select t.party_name from Transport t")
    Set<String> getPartyName();

    @Query("select t.address from Transport t")
    Set<String> getAddress();

    @Query("select t from Transport t order by date desc")
    List<Transport> getTransportData();

    @Query("select t from Transport t where day(date)=day(curdate()-1)")
    List<Transport> getAllTransportData();

    @Query("select t from Transport t where order_id=?1")
    List<Transport> getTransportStatus(long oder_id);

    @Modifying
    @Query(value = "update transport set status=?2 where order_id=?1", nativeQuery = true)
    @Transactional
    int updateStatus(long order_id, int status);

    @Query("select t from Transport t where date between ?1 and ?2 ")
    List<Transport> getOrder(String from, String to);

    @Modifying
    @Query(value = "update transport set status=3 where order_id=?1", nativeQuery = true)
    @Transactional
    int changeTransportStatusWithOrderId(long order_id);


    @Modifying
    @Query(value = "update transport set status=?2 where order_id=?1", nativeQuery = true)
    @Transactional
    int updateStatus(long order_id);

    @Modifying
    @Query(value = "update transport set status=4 where order_id=?1", nativeQuery = true)
    @Transactional
    int cancelOrderNotRunning(long order_id);

    @Query("select ts from Transport ts where status!=2 and status!=4")
    List<Transport> getOrderId();

    @Query("select ts from Transport ts where driver_name=''")
    List<Transport> getTransportDetails();

    @Modifying
    @Query(value = "update transport set total_qty=?2,sku=?3 where order_id=?1", nativeQuery = true)
    @Transactional
    public int updateTransport(long order_id, String total_Qty, String sku);

    @Modifying
    @Query(value = "update transport set driver_name=?2,vehicle_no=?3,contact_no=?4,truck_bay_no=?5,vehicle_type=?6 where order_id=?1", nativeQuery = true)
    @Transactional
    public int addDriverDetails(long order_id,String  driver_name, String vehicle_no, String contact_no,String truck_bay,String vehicle_type);

    @Query("select ts from Transport ts where day(date)=?1 and month(date)=?2 and year(date)=?3 and state=?4 and status between 0 and 2")
    List<Transport> getTransportDetails(int day,int month,int year,String state);

    @Query("select ts from Transport ts where date between ?1 and ?2 and state=?3 and status between 0 and 2")
    List<Transport> getTransportDetails(String from,String to,String state);


    @Query("select ts.state from Transport ts where date between ?1 and ?2 and status between 0 and 2")
    List<String> getTransportDetails(String from,String to);

  @Query("select ts.state from Transport ts where day(date)=?1 and month(date)=?2 and year(date)=?3 and status between 0 and 2")
    List<String> getTransportDetails(int day,int month,int year);

    @Query("select ts.truck_bay_no from Transport ts where truck_bay_no!=''")
    Set<String>getTruckBay();

    @Modifying
    @Query(value = "update  transport set status=2 where order_id=?1", nativeQuery = true)
    @Transactional
    public int transportOrderComplete(long order_id);

    @Modifying
    @Query(value = "update  transport set total_qty=?2,sku=?3 where order_id=?1", nativeQuery = true)
    @Transactional
    public int updateSkuAndQty(long order_id,String total_qty,String sku);

    @Query("select ts.order_id from Transport ts where truck_bay_no=?1 and date between ?2 and ?3 and status=1")
    List<String> getOrderIdwithTruckBayNo(String truck_bay_no,String from,String to);

}
