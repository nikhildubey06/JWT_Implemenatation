package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.RecordEntity;

public interface RecordRepo extends JpaRepository<RecordEntity,Integer>{

}
