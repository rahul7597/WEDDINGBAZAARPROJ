package com.exampleoctober.octoberproj.Registration.RegisterRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exampleoctober.octoberproj.Registration.RegisterEntity.RegisterEntity;

@Repository
public interface RegisterRepo extends JpaRepository<RegisterEntity,Integer>
{

    boolean existsByNumber(@Param("number") String number);

    boolean existsByEmail(@Param("email") String email);

    RegisterEntity findByEmailAndPassword(@Param("email") String email, @Param("password")String password);

    RegisterEntity findByNumberAndPassword(@Param("number") String number, @Param("password")String password);

    RegisterEntity findByEmail(@Param("email") String email);

    RegisterEntity findByNumber(@Param("number")String number);

    RegisterEntity findByEmailOrNumber(@Param("email") String email, @Param("number") String number);
    
}
