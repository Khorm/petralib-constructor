package com.petralib.variable.entity;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.variable.Multiplicity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "variable")
@Getter
@Setter
@ToString
public class VariableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id", nullable = false)
    Long id;

    @Column(name = "variable_name")
    String name;

    String description;

//    @ManyToOne
//    @JoinColumn(name = "type_id", insertable = false, updatable = false)
//    TypeEntity type;
    String type;

    @Enumerated(EnumType.STRING)
    Multiplicity multiplicity;

    @ManyToOne
    @JoinColumn(name = "block_id", insertable = false, updatable = false)
    @ToString.Exclude
    BlockEntity block;


}
