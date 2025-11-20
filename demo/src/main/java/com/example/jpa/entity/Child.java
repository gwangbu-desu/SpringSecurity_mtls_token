package com.example.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Child {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
