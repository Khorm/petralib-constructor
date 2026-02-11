package com.petralib.scenario.entity;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.scenario.enums.BeginEndType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "start_stop_points")
@Getter
@Setter
public class BeginEndEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    Long id;

    Long x;

    Long y;

    @Enumerated(EnumType.STRING)
    BeginEndType pointType;

    @ManyToOne
    @JoinColumn(name = "workflow_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    BlockEntity workflow;

    @OneToOne
    @JoinColumn(name = "connected_scenario_block")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    ScenarioBlockEntity connectedBlock;


}
