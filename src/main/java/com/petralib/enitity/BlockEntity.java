package com.petralib.enitity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "block")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id", nullable = false)
    Long id;

    @Column(name = "block_name" , nullable = false)
    String name;

    String description;
}
