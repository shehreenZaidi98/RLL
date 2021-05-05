package com.wmsweb.manualOrder;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ManualOrderRepository extends CrudRepository<ManualOrder, Long>{

    @Query("select mo from ManualOrder mo where date between ?1 and ?2")
    List<ManualOrder> getManualOrder(String fromDate,String toDate);
	

}
