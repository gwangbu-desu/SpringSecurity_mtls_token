package com.example.jpa.service;

import com.example.jpa.entity.Child;
import com.example.jpa.entity.Parent;
import com.example.jpa.persistence.ChildRepository;
import com.example.jpa.persistence.ParentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaTestService {
    @PersistenceContext
    private final EntityManager em;

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init(){
        Parent p1 = Parent.builder()
                .name("parent1")
                .build();
        Parent p2 = Parent.builder()
                .name("parent2")
                .build();

        Child c1 = Child.builder()
                .name("child1")
                .parent(p1)
                .build();

        Child c2 = Child.builder()
                .name("child2")
                .parent(p1)
                .build();
        Child c3 = Child.builder()
                .name("child3")
                .parent(p2)
                .build();
        Child c4 = Child.builder()
                .name("child4")
                .parent(p2)
                .build();
        em.persist(p1);
        em.persist(p2);

        em.persist(c1);
        em.persist(c2);
        em.persist(c3);
        em.persist(c4);
        em.flush();
        em.clear();
    }

    @Transactional
    public void test(){
        List<Parent> parents = parentRepository.findAllById(Collections.singleton(1l));

        for (Parent p : parents) {
            System.out.println("자식 개수 = " + p.getChildren().size());
        }
        em.flush();
        em.clear();
    }

    @Transactional
    public void test2(){
        List<Parent> parents = parentRepository.findAll();

        for (Parent p : parents) {
            System.out.println("자식 개수 = " + p.getChildren().size());
        }

        em.flush();
        em.clear();
    }

    @Transactional
    public void test3(){
        List<Parent> parents = parentRepository.findAllNew2();

        for (Parent p : parents) {
            System.out.println("자식 개수 = " + p.getChildren().size());
        }

        em.flush();
        em.clear();
    }

}
