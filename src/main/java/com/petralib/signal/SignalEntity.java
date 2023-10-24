package com.petralib.signal;

import com.petralib.enitity.BlockEntity;
import com.petralib.project.entity.ProjectUserRolesEntity;
import com.petralib.variable.entity.VariableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "signal")
public class SignalEntity extends BlockEntity {

    @OneToMany(mappedBy = "block")
    List<VariableEntity> variables;
}
