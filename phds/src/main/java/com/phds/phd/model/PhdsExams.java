package com.phds.phd.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="phds_exams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhdsExams {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "phdId")
    private int phdId;

    @Column(name = "examId")
    private int examId;

    @Column(name = "date")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date;

    @Column(name = "gradeTheory")
    private double gradeTheory;

    @Column(name = "gradeSpeak")
    private double gradeSpeak;

    @Column(name = "finalGrade")
    private double finalGrade;


}
