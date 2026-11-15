package com.preptrack.question;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @Column(nullable = false, length = 50)
    private String category; // APTITUDE, REASONING, VERBAL, JAVA_OOPS, DBMS, OS, NETWORKS, SQL, DSA

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String icon;
}
