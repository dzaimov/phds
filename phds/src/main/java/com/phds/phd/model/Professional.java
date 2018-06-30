package com.phds.phd.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="professionals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Professional {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "conspectId")
    private int conspecId;

    @Override
    public String toString() {
        return code + " " + name;
    }
}
