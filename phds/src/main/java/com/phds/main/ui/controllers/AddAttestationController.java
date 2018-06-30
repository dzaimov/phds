package com.phds.main.ui.controllers;

import com.phds.common.FileHelper;
import com.phds.phd.model.Attestation;
import com.phds.phd.service.AttestationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
public class AddAttestationController implements Initializable {

    @FXML private Label individulaFileLbl;
    @FXML private TextField protocolNumTxt;
    @FXML private DatePicker dateBox;
    @FXML private DatePicker protocolDate;
    @FXML private Button closeBtn;

    private int phdId;
    private String filePath;

    @Autowired private AttestationService attestationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void inint(int phdId) {
        this.phdId= phdId;
    }

    @FXML
    public void addAttestation(ActionEvent event) throws Exception {
        if(validateInput()) {
            try {
                Instant instantDate = dateBox.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Instant protocoldate = protocolDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Attestation attestation = Attestation.builder()
                        .approvedDate(Date.from(instantDate))
                        .protocolDate(Date.from(protocoldate))
                        .protocolNumber(Integer.valueOf(protocolNumTxt.getText()))
                        .file(filePath)
                        .phdId(phdId)
                        .build();

                attestationService.insertAttestation(attestation);
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
    public void addingFile(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            Node node = (Node) event.getSource();
            File file = fileChooser.showOpenDialog(node.getScene().getWindow());
            if (file != null) {
                individulaFileLbl.setText(file.getName());
                filePath = FileHelper.copyFileUsingStream(file, file.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) throws Exception {
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
        if(validateTextField(protocolNumTxt)
                || dateBox.getValue() == null
                || protocolDate == null
                || filePath.isEmpty()) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
