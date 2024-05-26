package org.halder.fithomebackend.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String image_url;

    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @Column(nullable = false)
    private Boolean equipmentRequired;

    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "exercises", fetch = FetchType.LAZY)
    private Set<Routine> routines;
}

