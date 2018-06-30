package com.phds.main.ui.controllers;

import com.phds.phd.model.Supervisor;
import com.phds.phd.service.SupervisorService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
public class AddSupervisorController implements Initializable {

    @FXML private ListView<Supervisor> supervisorsBox;
    @FXML private Button chooseSupervisorBtn;
    @FXML private TextField nameTxt;
    @FXML private Button addSupervisorBtn;
    @FXML private Button closeBtn;
    @FXML private ComboBox degreeBox;

    @Autowired
    private SupervisorService supervisorService;

    private List<Supervisor> choosedSupervisors;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        degreeBox.getItems().addAll("проф. д-р", "доц. д-р", "проф. д.т.н.");
        chooseSupervisorBtn.disableProperty().bind(supervisorsBox.getSelectionModel().selectedItemProperty().isNull());
        choosedSupervisors = new ArrayList<>();
        supervisorsBox.getItems().addAll(FXCollections.observableArrayList(supervisorService.getSupervisors()));
        supervisorsBox.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        supervisorsBox.setCellFactory(param -> new ListCell<Supervisor>() {
            @Override
            protected void updateItem(Supervisor item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getDegree() + " " + item.getName());
                }
            }
        });
    }

    @FXML
    public void chooseSupervisors(ActionEvent event) throws Exception {
        try {
            choosedSupervisors = supervisorsBox.getSelectionModel().getSelectedItems();

            // get a handle to the stage
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            // do what you have to do
            stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createSupervisor(ActionEvent event) throws Exception {
        if (validateInput()) {
            try {
                Supervisor supervisorToAdd = Supervisor.builder()
                        .degree((String)degreeBox.getSelectionModel().getSelectedItem())
                        .name(nameTxt.getText())
                        .build();

                supervisorService.insertSupervisors(supervisorToAdd);
                supervisorsBox.getItems().clear();
                supervisorsBox.getItems().addAll(FXCollections.observableArrayList(supervisorService.getSupervisors()));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void closeSupervisorWindow(ActionEvent event) throws Exception {
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
        if(validateCombobox(degreeBox)
                || validateTextField(nameTxt)) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
