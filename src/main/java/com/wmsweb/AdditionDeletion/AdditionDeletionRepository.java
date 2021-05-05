package com.wmsweb.AdditionDeletion;

import com.wmsweb.bayCapacity.BayCapacity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AdditionDeletionRepository extends CrudRepository<AdditionDeletion,Long> {

    @Modifying
    @Query(value = "insert into addition_deletion(password)values(?1)", nativeQuery = true)
    @Transactional
    int insertPassword(String password);

    @Modifying
    @Query(value = "update addition_deletion set password=?1 where id=?2", nativeQuery = true)
    @Transactional
    int updatePassword(String password,long id);

    @Query("select b1 from AdditionDeletion b1 where password=?1")
    List<AdditionDeletion> getPassword(String password);

}
