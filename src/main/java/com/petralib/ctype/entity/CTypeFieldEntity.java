package com.petralib.ctype.entity;

import com.petralib.ctype.enums.Multiplicity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 *
 */
@Entity
@Table(name = "ctype_fields")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class CTypeFieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id", nullable = false)
    Long id;

    @Column(name = "field_name", nullable = false)
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    Multiplicity multiplicity;

    @ManyToOne
    @JoinColumn(name = "owner_ctype_id", nullable = false)
    CTypeEntity owner;

    @ManyToOne
    @JoinColumn(name = "field_ctype_id", nullable = false)
    CTypeEntity fieldType;

    public CTypeFieldEntity(Long id){
        this.id = id;
    }
}
