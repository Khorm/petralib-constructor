package com.petralib.type.entity;

import com.petralib.type.enums.Multiplicity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "type_variable")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class TypeVariableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "var_id", nullable = false)
    Long id;

    @Column(name = "var_name", nullable = false)
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    Multiplicity multiplicity;

    @ManyToOne
    @JoinColumn(name = "owner_type_id", nullable = false)
    TypeEntity owner;

    @ManyToOne
    @JoinColumn(name = "var_type_id", nullable = false)
    TypeEntity varType;

    public TypeVariableEntity(Long id){
        this.id = id;
    }
}
