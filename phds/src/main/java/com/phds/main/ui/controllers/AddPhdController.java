package com.phds.main.ui.controllers;

import com.phds.common.DateHelper;
import com.phds.common.FileHelper;
import com.phds.common.SpringApplicationContext;
import com.phds.phd.model.*;
import com.phds.phd.service.ExamService;
import com.phds.phd.service.PhdService;
import com.phds.phd.service.ProfessionalService;
import com.phds.phd.service.SupervisorService;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateListView;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
@Setter
public class AddPhdController implements Initializable {

    @FXML
    private DatePicker fsDate;
    @FXML
    private TextField fsNumTxt;
    @FXML
    private TextField specialtyTxt;
    @FXML
    private ChoiceBox courseBox;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker graduationDate;
    @FXML
    private TextField nameTxt;
    @FXML
    private RadioButton regularBtn;
    @FXML
    private RadioButton inAbsentiaBtn;
    @FXML
    private RadioButton freeBtn;
    @FXML
    private RadioButton distBtn;
    @FXML
    private Button removeExam;
    @FXML
    private TableColumn<PhdsExams, String> subjectClmn;
    @FXML
    private TableColumn<PhdsExams, String> dateClmn;
    @FXML
    private TableColumn<PhdsExams, String> gradeClmn;
    @FXML
    private TableView examsTable;
    @FXML
    private Button removeSupervisorBtn;
    @FXML
    private Pane rootPane;
    @FXML
    private ImageView backroundImg;
    @FXML
    private ListView supervisorsBox;
    @FXML
    private ComboBox<Professional> professionalsBox;
    @FXML
    private Button addAnnotationBtn;
    @FXML
    private TextArea annotatoinSubjectTxt;
    @FXML
    private Button closeBtn;

    private ToggleGroup toggleGroup;

    @Autowired
    public PhdService phdService;
    @Autowired
    private ProfessionalService professionalService;
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private ExamService examService;

    private Annotation annotation;
    private Professional professional;
    private List<Supervisor> choosedSupervisors;
    private List<PhdsExams> phdsExams;
    private int currentYear = 0;
    private int phdId;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("C:\\Users\\Melike\\IdeaProjects\\phds\\src\\main\\resources\\tu-sofiq.jpg");
        Image image = new Image(file.toURI().toString());
        backroundImg.setImage(image);
        backroundImg.setFitHeight(600);

        fsNumTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                fsNumTxt.setText(oldValue);
            }
        });

        toggleGroup = new ToggleGroup();
        regularBtn.setToggleGroup(toggleGroup);
        regularBtn.setUserData("редовна");
        inAbsentiaBtn.setToggleGroup(toggleGroup);
        inAbsentiaBtn.setUserData("задочна");
        freeBtn.setToggleGroup(toggleGroup);
        freeBtn.setUserData("свободна");
        distBtn.setToggleGroup(toggleGroup);
        distBtn.setUserData("дистанционна");
        regularBtn.setSelected(true);
        courseBox.getItems().addAll("първи", "втори", "трети");
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

        courseBox.getSelectionModel().selectFirst();

        professionalsBox.getItems().addAll(FXCollections.observableArrayList(professionalService.getProfessionals()));
        professionalsBox.setCellFactory(new Callback<ListView<Professional>, ListCell<Professional>>() {

            @Override
            public ListCell<Professional> call(ListView<Professional> p) {

                final ListCell<Professional> cell = new ListCell<Professional>() {

                    @Override
                    protected void updateItem(Professional t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getCode() + " " + t.getName());
                        } else {
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });

        choosedSupervisors = new ArrayList<>();
        supervisorsBox.getItems().addAll(FXCollections.observableArrayList(choosedSupervisors));
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
        removeSupervisorBtn.disableProperty().bind(supervisorsBox.getSelectionModel().selectedItemProperty().isNull());

        phdsExams = new ArrayList<>();
        examsTable.setPlaceholder(new Label("Няма съществуващи записи"));
        examsTable.getItems().addAll(phdsExams);
        if (!examsTable.getItems().isEmpty()) {
            subjectClmn.setCellValueFactory(e -> new SimpleStringProperty(examService.findById(e.getValue().getExamId()).getName()));
            dateClmn.setCellValueFactory(e -> new SimpleStringProperty(DateHelper.formatDate(e.getValue().getDate())));
            gradeClmn.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(e.getValue().getFinalGrade())));
        }

        removeExam.disableProperty().bind(supervisorsBox.getSelectionModel().selectedItemProperty().isNull());
    }


    @FXML
    public void createPhd(ActionEvent event) throws Exception {
        if (validateInput()) {
            try {
                Instant instantStartDate = startDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Instant instantGraduationDate = graduationDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Instant instantFsDate = fsDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
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
                Phd phdToAdd = Phd.builder()
                        .name(nameTxt.getText())
                        .type((String) toggleGroup.getSelectedToggle().getUserData())
                        .begginingDate(Date.from(instantStartDate))
                        .graduationDate(Date.from(instantGraduationDate))
                        .currentYear(currentYear)
                        .specialty(specialtyTxt.getText())
                        .dissertationApprovedNumber(Integer.valueOf(fsNumTxt.getText()))
                        .dissertationApprovedDate(Date.from(instantFsDate))
                        .professionalId(professionalsBox.getSelectionModel().getSelectedItem().getId())
                        .annotationId(annotation.getId())
                        .build();

                phdId = phdService.insertPhd(phdToAdd);

                for (Supervisor supervisor : choosedSupervisors) {
                    phdService.insertPhdsSupervisor(PhdsSupervisors.builder()
                            .phdId(phdId)
                            .supervisorId(supervisor.getId())
                            .build());
                }

                if (phdsExams != null && !phdsExams.isEmpty()) {
                    for (PhdsExams phdsExam : phdsExams) {
                        phdsExam.setPhdId(phdId);
                        phdService.insertPhdsExam(phdsExam);
                    }
                }
                closeWindow(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void openAddingAnnotation(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addAnnotation.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddAnnotationController addAnnotationController = fxmlLoader.getController();
            System.out.println(annotation);
            addAnnotationController.init(annotation, choosedSupervisors);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node) event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            annotation = addAnnotationController.getAnnotation();
            if (annotation != null) {
                annotatoinSubjectTxt.setText(annotation.getSubject());
                addAnnotationBtn.setText("Редактирай");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddingProfessional(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addProfesionals.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node) event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            professionalsBox.getItems().clear();
            professionalsBox.getItems().addAll(FXCollections.observableArrayList(professionalService.getProfessionals()));
        } catch (Exception e) {
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
                for (Supervisor existingSupervisor : choosedSupervisors) {
                    if (existingSupervisor.getId() == supervisorToAdd.getId()) {
                        toAdd = false;
                    }
                }
                if (toAdd) {
                    choosedSupervisors.add(supervisorToAdd);
                }
                toAdd = true;
            }
            supervisorsBox.getItems().clear();
            supervisorsBox.getItems().addAll(FXCollections.observableArrayList(choosedSupervisors));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeSupervisors(ActionEvent event) throws Exception {
        try {
            List<Supervisor> supervisorsToRemove = supervisorsBox.getSelectionModel().getSelectedItems();
            for (Supervisor supervisorToRemove : supervisorsToRemove) {
                for (Iterator<Supervisor> iter = choosedSupervisors.listIterator(); iter.hasNext(); ) {
                    Supervisor supervisor = iter.next();
                    if (supervisor.getId() == supervisorToRemove.getId()) {
                        iter.remove();
                    }
                }
            }

            supervisorsBox.getItems().clear();
            supervisorsBox.getItems().addAll(FXCollections.observableArrayList(choosedSupervisors));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddingExam(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addExam.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddExamController addExamController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node) event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            if (addExamController.getPhdsExams() != null) {
                phdsExams.add(addExamController.getPhdsExams());
            }
            examsTable.getItems().clear();
            examsTable.getItems().addAll(phdsExams);
            if (!examsTable.getItems().isEmpty()) {
                subjectClmn.setCellValueFactory(e -> new SimpleStringProperty(examService.findById(e.getValue().getExamId()).getName()));
                dateClmn.setCellValueFactory(e -> new SimpleStringProperty(DateHelper.formatDate(e.getValue().getDate())));
                gradeClmn.setCellValueFactory(e -> new SimpleStringProperty(String.valueOf(e.getValue().getFinalGrade())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeExam(ActionEvent event) throws Exception {
        try {
            PhdsExams examToRemove = (PhdsExams) examsTable.getSelectionModel().getSelectedItem();
            for (Iterator<PhdsExams> iter = phdsExams.listIterator(); iter.hasNext(); ) {
                PhdsExams phdsExams = iter.next();
                if (examToRemove.getExamId() == phdsExams.getExamId() && examToRemove.getDate().toString().equals(phdsExams.getDate().toString())) {
                    iter.remove();
                }
            }

            examsTable.getItems().clear();
            examsTable.getItems().addAll(FXCollections.observableArrayList(phdsExams));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeWindow(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../main.fxml"));
            loader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Pane addViewPane = (Pane) loader.load();
            rootPane.getChildren().setAll(addViewPane);
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
                || validateListView(supervisorsBox)
                || annotation == null) {
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