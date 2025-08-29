package com.petralib.block.enitity;

import com.petralib.block.enums.PinType;
import com.petralib.type.enums.Multiplicity;
import com.petralib.type.entity.TypeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "variable")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VariableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id", nullable = false)
    Long id;

    @Column(name = "variable_name")
    String name;

    String description;

    @ManyToOne
    @JoinColumn(name = "var_type_id", nullable = false)
    TypeEntity varType;

    @Enumerated(EnumType.STRING)
    Multiplicity multiplicity;

    @Enumerated(EnumType.STRING)
    @Column(name = "var_pin_type")
    PinType pinType;

    @ManyToOne
    @JoinColumn(name = "block_id", updatable = false)
    @ToString.Exclude
    BlockEntity block;

    public VariableEntity(Long id){
        this.id = id;
    }


}
