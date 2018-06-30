package com.phds.phd.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="conspects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conspect {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "conspectFile")
    private String conspectFile;

    @Override
    public String toString() {
        return name ;
    }
}
