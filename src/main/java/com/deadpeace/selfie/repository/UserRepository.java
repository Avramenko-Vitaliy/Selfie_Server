package com.deadpeace.selfie.repository;

import com.deadpeace.selfie.client.SelfieSvcApi;
import com.deadpeace.selfie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Виталий on 06.10.2015.
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    User findByUsername(@Param(SelfieSvcApi.USER_NAME_PARAM) String username);
}
