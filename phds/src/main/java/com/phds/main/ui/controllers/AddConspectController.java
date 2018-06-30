package com.phds.main.ui.controllers;

import com.phds.common.FileHelper;
import com.phds.phd.model.Conspect;
import com.phds.phd.service.ConspectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.validateTextField;

@Controller
public class AddConspectController  implements Initializable {

    @FXML private TextField nameTxr;
    @FXML private Button addConspectBtn;
    @FXML private Button addFileBtn;
    @FXML private Button closeBtn;
    @FXML private Label fileLbl;

    @Autowired
    private ConspectService conspectService;

    private String conspectFilePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void createConspect(ActionEvent event) throws Exception {
        if(validateInput()) {
            try {
                Conspect conspectToAdd = Conspect.builder()
                        .name(nameTxr.getText())
                        .conspectFile(conspectFilePath)
                        .build();

                conspectService.insertConspect(conspectToAdd);

                Stage stage = (Stage) closeBtn.getScene().getWindow();
                // do what you have to do
                stage.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addingConspectFile(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            Node node = (Node) event.getSource();
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (file != null) {
                fileLbl.setText(file.getName());
                conspectFilePath = FileHelper.copyFileUsingStream(file, file.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeConspectWindow(ActionEvent event) throws Exception {
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
        if(validateTextField(nameTxr)
                || conspectFilePath.isEmpty()) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
