package com.wmsweb.FifoViolate;

import com.wmsweb.AdditionDeletion.AdditionDeletion;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FifoViolateRepository extends CrudRepository<FifoViolate,Long> {

    @Modifying
    @Query(value = "insert into fifo_violate(password)values(?1)", nativeQuery = true)
    @Transactional
    int insertFifoPassword(String password);


    @Query("select b1 from FifoViolate b1 where password=?1")
    List<FifoViolate> getPassword(String password);

    @Modifying
    @Query(value = "update fifo_violate set password=?1 where id=?2", nativeQuery = true)
    @Transactional
    int updateFifoPassword(String password,long id);



}
