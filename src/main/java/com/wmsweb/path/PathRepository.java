package com.wmsweb.path;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PathRepository extends CrudRepository<Path,Integer> {

    @Query("select pt from Path pt where password=?1")
    public List<Path> checkPassword(String password);

}
