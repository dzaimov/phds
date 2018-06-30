package com.phds.main.ui.controllers;

import com.phds.common.FileHelper;
import com.phds.phd.model.Annotation;
import com.phds.phd.model.Supervisor;
import com.phds.phd.service.AnnotationService;
import com.phds.phd.service.PhdService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.*;

@Controller
@Setter
@Getter
public class AddAnnotationController implements Initializable {

    @FXML private Label titleLbl;
    private String dissertationFilePath;
    private ToggleGroup toggleGroup;

    @FXML private TextField workPlaceTxt;
    @FXML private TextArea opinionTxt;
    @FXML private TextArea textTxt;
    @FXML private Label fileLbl;
    @FXML private TextField subjectTxt;
    @FXML private RadioButton yesBtn;
    @FXML private RadioButton noBtn;
    @FXML private Button closeBtn;
    @FXML private Button createBtn;

    @Autowired private AnnotationService annotationService;

    private Annotation annotation;
    private List<Supervisor> supervisors;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleGroup = new ToggleGroup();
        yesBtn.setToggleGroup(toggleGroup);
        yesBtn.setUserData(true);
        noBtn.setToggleGroup(toggleGroup);
        noBtn.setUserData(false);
        noBtn.setSelected(true);
        if(annotation != null) {
            titleLbl.setText("Редактиране на анотация:");
            workPlaceTxt.setText(annotation.getWorkPlace());
            opinionTxt.setText(annotation.getOpinion());
            textTxt.setText(annotation.getText());
            fileLbl.setText(Paths.get(annotation.getDissertationFile()).getFileName().toString());
            subjectTxt.setText(annotation.getSubject());
            if(annotation.isGrade()){
                yesBtn.setSelected(true);
                noBtn.setSelected(false);
            } else {
                yesBtn.setSelected(false);
                noBtn.setSelected(true);
            }
            createBtn.setText("Запази");
        }
    }

    public void init(Annotation annotation, List<Supervisor> supervisors) {
        this.annotation = annotation;
        this.supervisors = supervisors;
        if(this.annotation != null || supervisors != null) {
            initialize(null, null);
        }
    }

    @FXML
    public void addingDisertationFile(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            Node node = (Node) event.getSource();
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (file != null) {
                fileLbl.setText(file.getName());
                dissertationFilePath = FileHelper.copyFileUsingStream(file, file.getName());;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createAnnotation(ActionEvent event) throws Exception {
        if(validateInput()) {
            try {
                String reviews = null;
                for (int i = 0; i < supervisors.size(); i++) {
                    reviews += (supervisors.get(i).getDegree() + " " + supervisors.get(i).getName());
                    if (i < supervisors.size()) {
                        reviews += ", ";
                    }
                }
                Annotation annotationToAdd = Annotation.builder()
                        .subject(subjectTxt.getText())
                        .text(textTxt.getText())
                        .dissertationFile(dissertationFilePath)
                        .opinion(opinionTxt.getText())
                        .workPlace(workPlaceTxt.getText())
                        .grade((boolean) toggleGroup.getSelectedToggle().getUserData())
                        .reviews(reviews)
                        .build();
                if (annotation != null) {
                    annotationToAdd.setId(annotation.getId());
                    annotationService.updateAnnotation(annotationToAdd);
                } else {
                    int id = annotationService.insertAnnotations(annotationToAdd);

                    annotation = annotationToAdd;
                    annotation.setId(id);

                }
                Stage stage = (Stage) closeBtn.getScene().getWindow();
                // do what you have to do
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void closeAnnotationWindow(ActionEvent event) throws Exception {
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
        if(validateTextField(subjectTxt)
                || validateTextArea(textTxt)
                || validateTextArea(opinionTxt)
                || validateTextField(workPlaceTxt)
                || dissertationFilePath.isEmpty()) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
