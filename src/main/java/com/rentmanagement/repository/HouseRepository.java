package com.rentmanagement.repository;

import com.rentmanagement.entity.House;
import com.rentmanagement.enums.HouseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByStatus(HouseStatus status);
}
