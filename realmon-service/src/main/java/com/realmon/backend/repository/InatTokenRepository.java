package com.realmon.backend.repository;


import com.realmon.common.model.entity.InatToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InatTokenRepository extends JpaRepository<InatToken, Long> {
}
