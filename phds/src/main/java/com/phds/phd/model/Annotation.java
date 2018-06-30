package com.phds.phd.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="annotations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Annotation {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Column(name = "dissertationFile")
    private String dissertationFile;

    @Column(name = "opinion")
    private String opinion;

    @Column(name = "reviews")
    private String reviews;

    @Column(name = "workPlace")
    private String workPlace;

    @Column(name = "grade")
    private boolean grade;
}
