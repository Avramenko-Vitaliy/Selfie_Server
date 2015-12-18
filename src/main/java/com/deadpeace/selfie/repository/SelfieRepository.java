package com.deadpeace.selfie.repository;

import com.deadpeace.selfie.client.SelfieSvcApi;
import com.deadpeace.selfie.model.Selfie;
import com.deadpeace.selfie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Виталий on 07.10.2015.
 */

@Repository
public interface SelfieRepository extends JpaRepository<Selfie,Long>
{
    Selfie findById(@Param(SelfieSvcApi.ID_PARAM)long id);

    List<Selfie> findByCreatorOrderByDateDesc(@Param(SelfieSvcApi.CREATOR_PARAM) User user);
}
