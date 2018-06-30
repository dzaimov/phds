package com.phds.main.ui.controllers;

import com.phds.common.DateHelper;
import com.phds.common.FileHelper;
import com.phds.common.SpringApplicationContext;
import com.phds.common.ValidationUtil;
import com.phds.phd.model.Phd;
import com.phds.phd.model.PhdsSupervisors;
import com.phds.phd.model.Professional;
import com.phds.phd.model.Supervisor;
import com.phds.phd.service.PhdService;
import com.phds.phd.service.ProfessionalService;
import com.phds.phd.service.SupervisorService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateListView;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
@Setter
public class EditPhdController implements Initializable {

    @FXML private ChoiceBox courseBox;
    @FXML private ListView<Supervisor> supervisorsBox;
    @FXML private Button addSupervisorBtn;
    @FXML private Button removeSupervisorBtn;
    @FXML private TextField fsNumTxt;
    @FXML private DatePicker fsDate;
    @FXML private RadioButton regularBtn;
    @FXML private RadioButton inAbsentiaBtn;
    @FXML private RadioButton freeBtn;
    @FXML private RadioButton distBtn;
    @FXML private DatePicker startDate;
    @FXML private DatePicker graduationDate;
    @FXML private TextField specialtyTxt;
    @FXML private Button closeBtn;
    @FXML private ComboBox<Professional> professionalsBox;
    @FXML private TextField nameTxt;
    private ToggleGroup toggleGroup;

    private Phd phd;

    @Autowired
    public PhdService phdService;
    @Autowired
    private ProfessionalService professionalService;
    @Autowired private SupervisorService supervisorService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toggleGroup = new ToggleGroup();
        regularBtn.setToggleGroup(toggleGroup);
        regularBtn.setUserData("редовна");
        inAbsentiaBtn.setToggleGroup(toggleGroup);
        inAbsentiaBtn.setUserData("задочна");
        freeBtn.setToggleGroup(toggleGroup);
        freeBtn.setUserData("свободна");
        distBtn.setToggleGroup(toggleGroup);
        distBtn.setUserData("дистанционна");

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (toggleGroup.getSelectedToggle() != null) {
                    courseBox.getItems().clear();
                    if (toggleGroup.getSelectedToggle().getUserData().toString().equals("редовна")) {
                        courseBox.getItems().addAll("първи", "втори", "трети");
                    }
                    if (toggleGroup.getSelectedToggle().getUserData().toString().equals("задочна")) {
                        courseBox.getItems().addAll("първи", "втори", "трети", "четвърти");
                    }
                    if (toggleGroup.getSelectedToggle().getUserData().toString().equals("свободна")) {
                        courseBox.getItems().addAll("първи", "втори");
                    }
                    if (toggleGroup.getSelectedToggle().getUserData().toString().equals("дистанционна")) {
                        courseBox.getItems().addAll("първи", "втори", "трети", "четвърти");
                    }
                    courseBox.getSelectionModel().selectFirst();
                }
            }
        });

        removeSupervisorBtn.disableProperty().bind(supervisorsBox.getSelectionModel().selectedItemProperty().isNull());
        courseBox.getItems().clear();
    }

    public void init() {
        nameTxt.setText(phd.getName());

        if(phd.getType().equals("редовна")) {
            regularBtn.setSelected(true);
            courseBox.getItems().addAll("първи", "втори", "трети");
        }
        if(phd.getType().equals("задочна")) {
            inAbsentiaBtn.setSelected(true);
            courseBox.getItems().addAll("първи", "втори", "трети", "четвърти");
        }
        if(phd.getType().equals("свободна")) {
            freeBtn.setSelected(true);
            courseBox.getItems().addAll("първи", "втори");
        }
        if(phd.getType().equals("дистанционна")) {
            distBtn.setSelected(true);
            courseBox.getItems().addAll("първи", "втори", "трети", "четвърти");
        }
        if(phd.getCurrentYear() == 1) {
            courseBox.getSelectionModel().select(0);
        }
        if(phd.getCurrentYear() == 2) {
            courseBox.getSelectionModel().select(1);
        }
        if(phd.getCurrentYear() == 3) {
            courseBox.getSelectionModel().select(2);
        }
        if(phd.getCurrentYear() == 4) {
            courseBox.getSelectionModel().select(3);
        }
        professionalsBox.getItems().addAll(FXCollections.observableArrayList(professionalService.getProfessionals()));
        professionalsBox.setCellFactory(new Callback<ListView<Professional>,ListCell<Professional>>(){

            @Override
            public ListCell<Professional> call(ListView<Professional> p) {

                final ListCell<Professional> cell = new ListCell<Professional>(){

                    @Override
                    protected void updateItem(Professional t, boolean bln) {
                        super.updateItem(t, bln);
                        if(t != null){
                            setText(t.getCode() + " " + t.getName());
                        }else{
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });
        professionalsBox.getSelectionModel().select(professionalService.findById(phd.getProfessionalId()));
        specialtyTxt.setText(phd.getSpecialty());
        fsNumTxt.setText(String.valueOf(phd.getDissertationApprovedNumber()));

        Date approvedDate = phd.getDissertationApprovedDate();
        Calendar approvedDateCalendar = new GregorianCalendar();
        approvedDateCalendar.setTime(approvedDate);
        fsDate.setValue(LocalDate.of(
                approvedDateCalendar.get(Calendar.YEAR),
                approvedDateCalendar.get(Calendar.MONTH) + 1,
                approvedDateCalendar.get(Calendar.DAY_OF_MONTH)));

        Date begginingDate = phd.getBegginingDate();
        Calendar begginingCalendar = new GregorianCalendar();
        begginingCalendar.setTime(begginingDate);
        startDate.setValue(LocalDate.of(
                begginingCalendar.get(Calendar.YEAR),
                begginingCalendar.get(Calendar.MONTH) + 1,
                begginingCalendar.get(Calendar.DAY_OF_MONTH)));

        Date graduationDate1 = phd.getGraduationDate();
        Calendar graduationgCalendar = new GregorianCalendar();
        graduationgCalendar.setTime(graduationDate1);
        graduationDate.setValue(LocalDate.of(
                graduationgCalendar.get(Calendar.YEAR),
                graduationgCalendar.get(Calendar.MONTH) + 1,
                graduationgCalendar.get(Calendar.DAY_OF_MONTH)));

        for (PhdsSupervisors phdsSupervisors : phdService.getPhdsSupervisors()) {
            if(phdsSupervisors.getPhdId() == phd.getId()) {
                Supervisor supervisor = supervisorService.findById(phdsSupervisors.getSupervisorId());
                supervisorsBox.getItems().add(supervisor);
            }
        }

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
    public void openAddingProfessional(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addProfesionals.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            professionalsBox.getItems().clear();
            professionalsBox.getItems().addAll(FXCollections.observableArrayList(professionalService.getProfessionals()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void savePhd(ActionEvent event) throws Exception {
        if (validateInput()) {
            try {
                Instant instantStartDate = startDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Instant instantGraduationDate = graduationDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Instant instantFsDate = fsDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

                phd.setName(nameTxt.getText());
                phd.setType((String)toggleGroup.getSelectedToggle().getUserData());
                phd.setBegginingDate(Date.from(instantStartDate));
                phd.setGraduationDate(Date.from(instantGraduationDate));
                phd.setSpecialty(specialtyTxt.getText());
                phd.setDissertationApprovedNumber(Integer.valueOf(fsNumTxt.getText()));
                phd.setDissertationApprovedDate(Date.from(instantFsDate));
                phd.setProfessionalId(professionalsBox.getSelectionModel().getSelectedItem().getId());

                int currentYear = 0;
                switch ((String) courseBox.getSelectionModel().getSelectedItem()) {
                    case "първи":
                        currentYear = 1;
                        break;
                    case "втори":
                        currentYear = 2;
                        break;
                    case "трети":
                        currentYear = 3;
                        break;
                    case "четвърти":
                        currentYear = 4;
                        break;
                }
                phd.setCurrentYear(currentYear);

                phdService.updatePhd(phd);

                closeWindow(null);

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) throws Exception {
        try {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            // do what you have to do
            stage.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddingSupervisors(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addSupervisors.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddSupervisorController addSupervisorController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node) event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            boolean toAdd = true;
            for (Supervisor supervisorToAdd : addSupervisorController.getChoosedSupervisors()) {
                for (Supervisor existingSupervisor : supervisorsBox.getItems()) {
                    if (existingSupervisor.getId() == supervisorToAdd.getId()) {
                        toAdd = false;
                    }
                }
                if (toAdd) {
                    supervisorsBox.getItems().add(supervisorToAdd);
                }
                toAdd = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeSupervisors(ActionEvent event) throws Exception {
        try {
            List<PhdsSupervisors> phdsSupervisors = phdService.getPhdsSupervisors();
            List<Supervisor> supervisorsToRemove = supervisorsBox.getSelectionModel().getSelectedItems();
            for (Supervisor supervisorToRemove : supervisorsToRemove) {
                for (PhdsSupervisors phdsSupervisor: phdsSupervisors) {
                    if (phdsSupervisor.getSupervisorId() == supervisorToRemove.getId()) {
                        phdService.removePhdSupervisor(phdsSupervisor);
                    }
                }
            }

            supervisorsBox.getItems().clear();

            for (PhdsSupervisors phdsSupervisor : phdService.getPhdsSupervisors()) {
                if(phdsSupervisor.getPhdId() == phd.getId()) {
                    Supervisor supervisor = supervisorService.findById(phdsSupervisor.getSupervisorId());
                    supervisorsBox.getItems().add(supervisor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        Alert alert = new Alert(Alert.AlertType.NONE, "Моля попълнете всички задължителни полета отбелязани с *", ButtonType.OK);
        alert.setTitle("Грешка");
        if (validateTextField(nameTxt)
                || validateTextField(specialtyTxt)
                || validateTextField(fsNumTxt)
                || fsDate.getValue() == null
                || validateCombobox(professionalsBox)
                || startDate.getValue() == null
                || graduationDate.getValue() == null
                || validateListView(supervisorsBox)) {
            alert.showAndWait();
            return false;
        }

        Date startD = Date.from(startDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date graduationD = Date.from(graduationDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if (startD.after(graduationD)) {
            String content = "Датата на завършване " +
                    DateHelper.formatDate(graduationD) +
                    " не може да бъде преди датата на започване " + DateHelper.formatDate(startD);
            alert.setContentText(content);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
