package com.example.jpa.service;

import com.example.jpa.entity.Child;
import com.example.jpa.entity.Parent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaTestServiceTest {
    @PersistenceContext
    @Autowired
    private EntityManager em;

    @Autowired
    private JpaTestService service;


    @Test
    void init() {
        service.init();
    }

    @Test
    @Name("N+1 발생 쿼리")
    void test1() {
        em.clear();
        service.test();
    }
    @Test
    @Name("N+1 발생 쿼리 수정 by EntityGraph")
    void test2() {

        em.clear();
        service.test2();
    }
    @Test
    @Name("N+1 발생 쿼리 수정 by fetch join")
    void test3() {

        em.clear();
        service.test3();
    }
}