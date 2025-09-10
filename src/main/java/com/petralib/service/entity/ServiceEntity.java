package com.petralib.service.entity;

import com.petralib.project.entity.ProjectEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "services")
@Getter
@Setter
@ToString
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    ProjectEntity project;

    @Column(name = "service_name" , nullable = false)
    String name;

    String description;

    @Column(name = "service_path" , nullable = false)
    String path;
}
