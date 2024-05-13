package com.petralib.signal;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.variable.entity.VariableEntity;
import jakarta.persistence.*;

import java.util.List;

//@Entity
//@Table(name = "signal")
public class SignalEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "block")
    List<VariableEntity> variables;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
