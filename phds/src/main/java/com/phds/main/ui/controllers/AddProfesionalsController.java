package com.phds.main.ui.controllers;

import com.phds.common.SpringApplicationContext;
import com.phds.phd.model.Annotation;
import com.phds.phd.model.Conspect;
import com.phds.phd.model.Professional;
import com.phds.phd.service.ConspectService;
import com.phds.phd.service.ProfessionalService;
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
import java.util.ResourceBundle;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
public class AddProfesionalsController implements Initializable {
    @FXML private TextField codeTxt;
    @FXML private TextField nameTxr;
    @FXML private ComboBox<Conspect> conspectsBox;
    @FXML private Button addConspectBtn;
    @FXML private Button addProfBtn;
    @FXML private Button closeBtn;

    @Autowired
    private ConspectService conspectService;
    @Autowired
    private ProfessionalService professionalService;


    private Professional professional;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conspectsBox.getItems().addAll(FXCollections.observableArrayList(conspectService.getConspects()));
        conspectsBox.setCellFactory(new Callback<ListView<Conspect>,ListCell<Conspect>>(){

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
    public void createProfessionals(ActionEvent event) throws Exception {
        try {
            Conspect conspect =(Conspect)conspectsBox.getSelectionModel().getSelectedItem();

            professional = Professional.builder()
                    .code(codeTxt.getText())
                    .name(nameTxr.getText())
                    .conspecId(conspect.getId())
                    .build();
            System.out.println(professional);
            professionalService.insertProfessional(professional);

            Stage stage = (Stage) closeBtn.getScene().getWindow();
            // do what you have to do
            stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void closeProfessionalWindow(ActionEvent event) throws Exception {
        try {
            // get a handle to the stage
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            // do what you have to do
            stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addConspect(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addConspect.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            conspectsBox.getItems().addAll(conspectService.getConspects());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Моля попълнете всички задължителни полета отбелязани с *", ButtonType.OK);
        alert.setTitle("Грешка");
        if(validateTextField(codeTxt)
                || validateTextField(nameTxr)
                || validateCombobox(conspectsBox)) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
