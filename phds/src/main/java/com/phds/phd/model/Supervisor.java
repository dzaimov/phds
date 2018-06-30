package com.phds.phd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="supervisors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supervisor {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "degree")
    private String degree;

    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return degree + " " + name;
    }

}
