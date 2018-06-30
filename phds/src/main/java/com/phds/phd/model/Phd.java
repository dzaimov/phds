package com.phds.phd.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="phd")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Phd {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "begginingDate")
    @DateTimeFormat(pattern =  "dd.MM.yyyy")
    private Date begginingDate;

    @Column(name = "orderNumber")
    private int orderNumber; //zapoved nomer

    @Column(name = "graduationDate")
    private Date graduationDate;

    @Column(name = "currentYear")
    private int currentYear;

    @Column(name = "specialty")
    private String specialty; //specialnost

    @Column(name = "dissertationApprovedNumber")
    private int dissertationApprovedNumber;

    @Column(name = "dissertationApprovedDate")
    private Date dissertationApprovedDate;

    @Column(name = "professionalId")
    private int professionalId;

    @Column(name = "annotationId")
    private int annotationId;

    @Column(name = "individualPlanFilePath")
    private String individualPlanFilePath;

}
