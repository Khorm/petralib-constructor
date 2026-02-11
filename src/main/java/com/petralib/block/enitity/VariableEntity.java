package com.petralib.block.enitity;

import com.petralib.block.enums.PinType;
import com.petralib.ctype.entity.CTypeEntity;
import com.petralib.ctype.enums.Multiplicity;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;

@Entity
@Table(name = "variables")
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
    CTypeEntity varType;

    @Enumerated(EnumType.STRING)
    Multiplicity multiplicity;

    @Enumerated(EnumType.STRING)
    @Column(name = "var_pin_type")
    PinType pinType;

    @ManyToOne
    @JoinColumn(name = "block_id", updatable = false)
    @ToString.Exclude
    BlockEntity block;

    @ManyToOne
    @JoinColumn(name = "scenario_block_id", updatable = false)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    ScenarioBlockEntity scenarioBlock;



//    @OneToMany(mappedBy = "ownerVariable", fetch = FetchType.LAZY)
//    @ToString.Exclude
//    Collection<ScenarioVariableEntity> scenarioAttachedVariables;

    public VariableEntity(Long id){
        this.id = id;
    }


}
