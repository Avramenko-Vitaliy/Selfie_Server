package com.deadpeace.selfie.repository;

import com.deadpeace.selfie.client.SelfieSvcApi;
import com.deadpeace.selfie.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Виталий on 07.10.2015.
 */
@Repository
public interface RoleRepository extends JpaRepository<UserRole,Long>
{
    UserRole findById(@Param(SelfieSvcApi.ID_PARAM)long id);

    UserRole findByAuthority(@Param(SelfieSvcApi.AUTHORITY_PARAM)String authority);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN 'true' ELSE 'false' END FROM UserRole r WHERE r.authority = ?1")
    Boolean existsByAuthority(String authority);
}
