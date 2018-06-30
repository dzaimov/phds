package com.phds.main.ui.controllers;

import com.phds.common.DateHelper;
import com.phds.common.ValidationUtil;
import com.phds.phd.model.PhdsExams;
import com.phds.phd.service.ExamService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

@Controller
@Getter
public class ViewExamController implements Initializable {

    @FXML private TextField theoryGradeTxt;
    @FXML private TextField speakingGradeTxt;
    @FXML private TextField finalGradeTxt;
    @FXML private TextField subjectTxt;
    @FXML private TextField dateTxt;
    @FXML private Button closeBtn;

    private PhdsExams phdsExams;

    @Autowired
    private ExamService examService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(PhdsExams phdsExams) {
        this.phdsExams = phdsExams;
        dateTxt.setText(DateHelper.formatDate(phdsExams.getDate()));
        subjectTxt.setText(examService.findById(phdsExams.getExamId()).getName());
        finalGradeTxt.setText(String.valueOf(phdsExams.getFinalGrade()));
        speakingGradeTxt.setText(String.valueOf(phdsExams.getGradeSpeak()));
        theoryGradeTxt.setText(String.valueOf(phdsExams.getGradeTheory()));
    }

    @FXML
    public void closeExamWindow(ActionEvent event) throws Exception {
        try {
            // get a handle to the stage
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            // do what you have to do
            stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
