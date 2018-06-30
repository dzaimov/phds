package com.phds.phd.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="phds_supervisors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhdsSupervisors {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "phdId")
    private int phdId;

    @Column(name = "supervisorId")
    private int supervisorId;
}
