package com.phds.main.ui.controllers;

import com.phds.common.FileHelper;
import com.phds.phd.model.Conspect;
import com.phds.phd.model.Exam;
import com.phds.phd.service.ConspectService;
import com.phds.phd.service.ExamService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
public class AddExamDbController implements Initializable {


    @FXML private Label ordinanceLbl;
    @FXML private Label protocolLbl;
    @FXML private Label examlbl;
    @FXML private ComboBox conspectBox;
    @FXML private Button addExam;
    @FXML private TextField nameTxt;
    @FXML private Button addFileProtocol;
    @FXML private Button addFileOrdinance;
    @FXML private Button addFileExam;
    @FXML private Button closeBtn;

    private String ordinanceFilePath;
    private String protocolFilePath;
    private String examFilePath;

    @Autowired private ConspectService conspectService;
    @Autowired private ExamService examService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conspectBox.getItems().addAll(FXCollections.observableArrayList(conspectService.getConspects()));
        conspectBox.setCellFactory(new Callback<ListView<Conspect>,ListCell<Conspect>>(){

            @Override
            public ListCell<Conspect> call(ListView<Conspect> p) {

                final ListCell<Conspect> cell = new ListCell<Conspect>(){

                    @Override
                    protected void updateItem(Conspect t, boolean bln) {
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
    public void addingOrdinanceFile(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            Node node = (Node) event.getSource();
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (file != null) {
                ordinanceLbl.setText(file.getName());
                ordinanceFilePath = FileHelper.copyFileUsingStream(file, file.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addingExamFile(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            Node node = (Node) event.getSource();
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (file != null) {
                examlbl.setText(file.getName());
                examFilePath = FileHelper.copyFileUsingStream(file, file.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addingProtocolFile(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            Node node = (Node) event.getSource();
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (file != null) {
                protocolLbl.setText(file.getName());
                protocolFilePath = FileHelper.copyFileUsingStream(file, file.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createExam(ActionEvent event) throws Exception {
        if(validateInput()) {
            try {
                Conspect conspect =(Conspect)conspectBox.getSelectionModel().getSelectedItem();

                Exam examToAdd = Exam.builder()
                        .name(nameTxt.getText())
                        .conspectId(conspect.getId())
                        .fileOrdinance(ordinanceFilePath)
                        .fileProtocol(protocolFilePath)
                        .fileExam(examFilePath)
                        .build();
                examService.insertExam(examToAdd);

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
        if(validateCombobox(conspectBox)
                || validateTextField(nameTxt)
                || examFilePath == null
                || protocolFilePath == null
                || ordinanceFilePath == null) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
