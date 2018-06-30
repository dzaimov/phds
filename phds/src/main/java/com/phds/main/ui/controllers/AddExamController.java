package com.phds.main.ui.controllers;

import com.phds.common.SpringApplicationContext;
import com.phds.phd.model.Exam;
import com.phds.phd.model.PhdsExams;
import com.phds.phd.service.ExamService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
public class AddExamController implements Initializable {
    @FXML private Button addExamDBBtn;
    @FXML private Button closeBtn;
    @FXML private Button addExam;
    @FXML private DatePicker dateBox;
    @FXML private TextField finalGradeTxt;
    @FXML private TextField theoryGradeTxt;
    @FXML private TextField speakingGradeTxt;
    @FXML private ComboBox<Exam> subjectBox;

    private Exam exam;
    private PhdsExams phdsExams;

    @Autowired private ExamService examService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectBox.getItems().addAll(FXCollections.observableArrayList(examService.getExams()));
        subjectBox.setCellFactory(new Callback<ListView<Exam>,ListCell<Exam>>(){
            @Override
            public ListCell<Exam> call(ListView<Exam> p) {

                final ListCell<Exam> cell = new ListCell<Exam>(){

                    @Override
                    protected void updateItem(Exam t, boolean bln) {
                        super.updateItem(t, bln);
                        if(t != null){
                            setText(t.getName());
                        }else{
                            setText(null);
                        }
                    }

                };
                return cell;
            }
        });

    }

    @FXML
    public void openAddExamDB(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addExamDb.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            subjectBox.getItems().clear();
            subjectBox.getItems().addAll(FXCollections.observableArrayList(examService.getExams()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createExam(ActionEvent event) throws Exception {
        if (validateInput()) {
            try {
                Instant instant = dateBox.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                PhdsExams phdsExams = PhdsExams.builder()
                        .examId(subjectBox.getSelectionModel().getSelectedItem().getId())
                        .date(Date.from(instant))
                        .gradeTheory(Double.valueOf(theoryGradeTxt.getText()))
                        .gradeSpeak(Double.valueOf(speakingGradeTxt.getText()))
                        .finalGrade(Double.valueOf(finalGradeTxt.getText()))
                        .build();

                this.phdsExams = phdsExams;
                // get a handle to the stage
                Stage stage = (Stage) closeBtn.getScene().getWindow();
                // do what you have to do
                stage.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
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

    private boolean validateInput() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Моля попълнете всички задължителни полета отбелязани с *", ButtonType.OK);
        alert.setTitle("Грешка");
        if(validateCombobox(subjectBox)
                || dateBox.getValue() == null
                || validateTextField(theoryGradeTxt)
                || validateTextField(speakingGradeTxt)
                || validateTextField(finalGradeTxt)) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
