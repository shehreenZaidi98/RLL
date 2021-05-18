package com.wmsweb.clientRegistration;

import com.wmsweb.login.Login;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface ClientRegistrationRepo extends CrudRepository<ClientRegistration,Long> {

    @Modifying
    @Query(value = "insert into client_registration(party_name,phone,email,gst,pan,address,Country," +
            "state,pin_code)values(?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
    @Transactional
    int insertClientRegistration(String party_name, String phone,String email,String gst,String pan,String address,
                  String Country,String state,String pin_code);


    @Query("select t.party_name from ClientRegistration t")
    Set<String> getPartyName();


    @Query("select l from ClientRegistration l where party_name=?1")
    List<ClientRegistration> getClientRegistration(String party_name);

}
