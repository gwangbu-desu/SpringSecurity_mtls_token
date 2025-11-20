package com.example.jpa.persistence;

import com.example.jpa.entity.Parent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentRepository extends JpaRepository<Parent,Long> {
    @EntityGraph(attributePaths = {"children"})
    List<Parent> findAll();

    @Query("select distinct p from Parent p join fetch p.children")
    List<Parent> findAllNew2();
}
