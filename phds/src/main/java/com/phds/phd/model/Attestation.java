package com.phds.phd.model;

import com.phds.common.DateHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="attestations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attestation {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "phdId")
    private int phdId;

    @Column(name = "approvedDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date approvedDate;

    @Column(name = "protocolNumber")
    private int protocolNumber;

    @Column(name = "protocolDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date protocolDate;

    @Column(name = "file")
    private String file;

    @Override
    public String toString() {
        return protocolNumber + "/" + DateHelper.formatDate(protocolDate) + " от " + DateHelper.formatDate(approvedDate);
    }
}
