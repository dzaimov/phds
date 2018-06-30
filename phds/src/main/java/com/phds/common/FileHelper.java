package com.phds.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;

import java.io.*;

public class FileHelper {

    public static String copyFileUsingStream(File source, String fileName) throws IOException {
        String destination = "D:\\Phds" + fileName;
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(destination);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
        return destination;
    }

    public static void openFile(String filePath) throws Exception {
        try {
            Process p = Runtime.getRuntime()
                    .exec("rundll32 url.dll,FileProtocolHandler " + filePath);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String createPdfFile(String fileName, Paragraph paragraph) {
        String filePath = "D:\\Phds\\" + fileName + ".pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(paragraph);
            document.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
