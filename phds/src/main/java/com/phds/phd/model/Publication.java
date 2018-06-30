package com.phds.phd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="publications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publication {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "phdId")
    private int phdId;

    @Column(name = "title")
    private String title;

    @Column(name = "authors")
    private String authors;

    @Column(name = "sources")
    private String sources;

    @Column(name = "place")
    private String place;

    @Column(name = "createdDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date createdDate;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "pages")
    private int pages;

    @Column(name = "attestationId")
    private int attestationId;
}
