package com.wmsweb.companyCode;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCodeRepository extends CrudRepository<CompanyCode,Long> {

    @Query("select td.company_code from CompanyCode td")
    int getCompanyCode();
}
