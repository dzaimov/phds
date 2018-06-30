package com.phds.common;

import java.io.IOException;

import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.phds.phd.model.Annotation;
import com.phds.phd.model.Phd;
import com.phds.phd.model.Professional;
import com.phds.phd.model.Supervisor;

import static com.phds.common.DateHelper.*;

public class PdfFileCreator {

    private static Font catFont = FontFactory.getFont("C:\\Windows\\Fonts\\Times.ttf","ISO-8859-5", 18, Font.NORMAL);
    private static Font subFont = FontFactory.getFont("C:\\Windows\\Fonts\\Times.ttf","ISO-8859-5", 18, Font.BOLD);
    private static Font smallNormal = FontFactory.getFont("C:\\Windows\\Fonts\\Times.ttf","ISO-8859-5", 12, Font.NORMAL);
    private static Font smallBold = FontFactory.getFont("C:\\Windows\\Fonts\\Times.ttf","ISO-8859-5", 12, Font.BOLD);

    public Paragraph createIndividualPlan(Phd phd, Professional professional, Annotation annotation, List<Supervisor> supervisors) throws IOException, DocumentException {
        Paragraph preface = new Paragraph();

        PdfPTable tittle = new PdfPTable(1);
        tittle.setWidthPercentage(100);
        tittle.addCell(getCell("ТЕХНИЧЕСКИ УНИВЕРСИТЕТ - СОФИЯ", PdfPCell.ALIGN_CENTER, catFont));
        preface.add(tittle);

        LineSeparator l = new LineSeparator();
        preface.add(new Chunk(l));

        addEmptyLine(preface, 1);

        PdfPTable beggining = new PdfPTable(2);
        beggining.setWidthPercentage(100);
        beggining.addCell(getCell("Докторантура: " + phd.getType(), PdfPCell.ALIGN_LEFT, smallNormal));
        beggining.addCell(getCell("ФКСТ", PdfPCell.ALIGN_RIGHT, smallNormal));
        preface.add(beggining);

        PdfPTable cathether = new PdfPTable(1);
        cathether.setWidthPercentage(100);
        cathether.addCell(getCell("КС", PdfPCell.ALIGN_RIGHT, smallNormal));
        preface.add(cathether);

        addEmptyLine(preface, 1);

        PdfPTable subTittle = new PdfPTable(1);
        subTittle.setWidthPercentage(100);
        subTittle.addCell(getCell("ИНДИВИДУАЛЕН ПЛАН ЗА РАБОТА НА ДОКТОРАНТА", PdfPCell.ALIGN_CENTER, subFont));
        preface.add(subTittle);

        addEmptyLine(preface, 2);

        PdfPTable name = new PdfPTable(2);
        name.setWidthPercentage(100);
        name.addCell(getCell("1. Име, бащино и фамилно ", PdfPCell.ALIGN_LEFT, smallNormal));
        name.addCell(getCell(phd.getName(), PdfPCell.ALIGN_LEFT, smallBold));
        preface.add(name);

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("2. Дата на зачисляване в докторантурата "
                + formatDate(phd.getDissertationApprovedDate())
                + " Заповед N " + phd.getDissertationApprovedNumber()
                + " от " + formatDate(phd.getDissertationApprovedDate()),
                smallNormal));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("3. Срок на завършване на докторантурата"
                + "     "
                + formatDate(phd.getGraduationDate()),
                smallNormal));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("4. Професионално направление "
                + professional.getCode()
                + " "
                + professional.getName(),
                smallNormal));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("5. Тема на дисертационна работа "
                + annotation.getSubject()
                + "     "
                + "отвърдена от факултетния (научен) съвет в заседание от "
                + + phd.getDissertationApprovedNumber()
                + "/" + formatDate(phd.getDissertationApprovedDate()),
                smallNormal));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("6. Научни ръководители: "
                + supervisors.get(0),
                smallNormal));
        for (int i = 1; i < supervisors.size(); i++) {
            preface.add(new Paragraph("                         "
                    + supervisors.get(i).toString(),
                    smallNormal));
        }

        addEmptyLine(preface, 1);

        preface.add(new Paragraph("7. Индивидуалният план за работа на докторанта е утвърден от факултетния (научен) съвет в заседание от"
                + "................................"
                + "протокол N .... от ......... 2018",
                smallNormal));


        addEmptyLine(preface, 2);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(getCell("УТВЪРЖДАВАМ: ", PdfPCell.ALIGN_RIGHT, catFont));
        table.addCell(getCell("РЕКТОР: .....................", PdfPCell.ALIGN_RIGHT, subFont));
        table.addCell(getCell("                            ", PdfPCell.ALIGN_RIGHT, subFont));
        preface.add(table);

        PdfPTable decan = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.addCell(getCell("ДЕКАН: .....................", PdfPCell.ALIGN_RIGHT, subFont));
        preface.add(decan);


        return preface;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private PdfPCell getCell(String text, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
