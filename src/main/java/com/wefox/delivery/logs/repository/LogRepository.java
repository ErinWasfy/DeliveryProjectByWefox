package com.wefox.delivery.logs.repository;

import com.wefox.delivery.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<ErrorLog,Integer> {

}
