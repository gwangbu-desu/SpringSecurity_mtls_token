package com.example.jpa.persistence;

import com.example.jpa.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {
}
