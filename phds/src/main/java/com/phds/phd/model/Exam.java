package com.phds.phd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "fileOrdinance")
    private String fileOrdinance;

    @Column(name = "fileProtocol")
    private String fileProtocol;

    @Column(name = "fileExam")
    private String fileExam;

    @Column(name = "conspectId")
    private int conspectId;

    @Override
    public String toString() {
        return name ;
    }
}
