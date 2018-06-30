package com.phds.main.ui.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.phds.common.*;
import com.phds.phd.model.*;
import com.phds.phd.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
@Getter
@Setter
public class ViewPhdController implements Initializable {

    @FXML private ImageView backroundImg;
    @FXML private TableColumn<Publication, String> subjectPubClmn;
    @FXML private TableColumn<Publication, String> datePubClmn;
    @FXML private Button viewPublicationBtn;
    @FXML private Button removePulicationBtn;
    @FXML private Button addPublicationBtn;
    @FXML private Button viewExamBtn;
    @FXML private Button increaseCourseBtn;
    @FXML private Button reduceCourseBtn;
    @FXML private Button removeExamBtn;
    @FXML private TableColumn<PhdsExams, String> subjectClmn;
    @FXML private TableColumn<PhdsExams, String> dateClmn;
    @FXML private TableColumn<PhdsExams, String> gradeClmn;
    @FXML private TextArea annotationTxt;
    @FXML private Pane rootPane;
    @FXML private Button closeBtn;
    @FXML private TextField specialtyTxt;
    @FXML private TextField typeTxt;
    @FXML private TextField courseTxt;
    @FXML private TextField fsTxt;
    @FXML private TextArea professionalTxt;
    @FXML private TextField dateBegginingTxt;
    @FXML private TextField dateGraduationTxt;
    @FXML private ListView<Supervisor> supervisorsBox;
    @FXML private TableView examsTable;
    @FXML private TableView<Publication> publicationsTable;
    @FXML private TextArea nameTxt;

    private Phd phd;
    private List<Supervisor> supervisors;
    private PdfFileCreator pdfFileCreator;

    @Autowired public PhdService phdService;
    @Autowired private ProfessionalService professionalService;
    @Autowired private AnnotationService annotationService;
    @Autowired private SupervisorService supervisorService;
    @Autowired private ExamService examService;
    @Autowired private PublicationService publicationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("C:\\Users\\Melike\\IdeaProjects\\phds\\src\\main\\resources\\tu-sofia.jpg");
        Image image = new Image(file.toURI().toString());
        backroundImg.setImage(image);
        backroundImg.setFitHeight(600);
        examsTable.setPlaceholder(new Label("Няма съществуващи записи"));
        publicationsTable.setPlaceholder(new Label("Няма съществуващи записи"));
        pdfFileCreator = new PdfFileCreator();
    }

    public void inint(Phd phd) {
        this.phd = phd;
        examsTable.getItems().clear();
        supervisorsBox.getItems().clear();
        publicationsTable.getItems().clear();

        supervisors = new ArrayList<>();
        annotationTxt.setText(annotationService.findById(phd.getAnnotationId()).getSubject());

        nameTxt.setText(phd.getName());
        specialtyTxt.setText(phd.getSpecialty());
        typeTxt.setText(phd.getType());
        courseTxt.setText(String.valueOf(phd.getCurrentYear()));
        fsTxt.setText(phd.getDissertationApprovedNumber() + "/" + DateHelper.formatDate(phd.getDissertationApprovedDate()));
        Professional professional = professionalService.findById(phd.getProfessionalId());
        professionalTxt.setText(professional.getCode() + " " + professional.getName());
        dateBegginingTxt.setText(DateHelper.formatDate(phd.getBegginingDate()));
        dateGraduationTxt.setText(DateHelper.formatDate(phd.getGraduationDate()));
        Annotation annotation = annotationService.findById(phd.getAnnotationId());
        annotationTxt.setText(annotation.getSubject());

        for (PhdsSupervisors phdsSupervisors : phdService.getPhdsSupervisors()) {
            if(phdsSupervisors.getPhdId() == phd.getId()) {
                Supervisor supervisor = supervisorService.findById(phdsSupervisors.getSupervisorId());
                supervisorsBox.getItems().add(supervisor);
                supervisors.add(supervisor);
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

        examsTable.getItems().addAll(phdService.findExamByPhdId(phd.getId()));
        if(!examsTable.getItems().isEmpty()) {
            subjectClmn.setCellValueFactory(e-> new SimpleStringProperty(examService.findById(e.getValue().getExamId()).getName()));
            dateClmn.setCellValueFactory(e-> new SimpleStringProperty(DateHelper.formatDate(e.getValue().getDate())));
            gradeClmn.setCellValueFactory(e-> new SimpleStringProperty(String.valueOf(e.getValue().getFinalGrade())));
        }
        removeExamBtn.disableProperty().bind(examsTable.getSelectionModel().selectedItemProperty().isNull());
        viewExamBtn.disableProperty().bind(examsTable.getSelectionModel().selectedItemProperty().isNull());

        List<Publication> publications = publicationService.getPublication().stream()
                .filter(p -> p.getPhdId() == phd.getId())
                .collect(Collectors.toList());
        publicationsTable.getItems().clear();
        publicationsTable.getItems().addAll(publications);
        if(!publicationsTable.getItems().isEmpty()) {
            subjectPubClmn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getTitle()));
            datePubClmn.setCellValueFactory(p-> new SimpleStringProperty(DateHelper.formatDate(p.getValue().getCreatedDate())));
        }
        removePulicationBtn.disableProperty().bind(publicationsTable.getSelectionModel().selectedItemProperty().isNull());
        viewPublicationBtn.disableProperty().bind(publicationsTable.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    public void removePhd(ActionEvent event) throws Exception {
        try {
            phdService.removePhd(phd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editPhd(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../editPhd.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            EditPhdController editPhdController = fxmlLoader.getController();
            editPhdController.setPhd(phd);
            editPhdController.init();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            inint(phdService.findById(phd.getId()));

        } catch(Exception e) {
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
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            if(addExamController.getPhdsExams() != null) {
                PhdsExams phdsExamsToAdd = addExamController.getPhdsExams();
                phdsExamsToAdd.setPhdId(phd.getId());
                phdService.insertPhdsExam(phdsExamsToAdd);
            }

            examsTable.getItems().clear();
            examsTable.getItems().addAll(phdService.findExamByPhdId(phd.getId()));
            if(!examsTable.getItems().isEmpty()) {
                subjectClmn.setCellValueFactory(e-> new SimpleStringProperty(examService.findById(e.getValue().getExamId()).getName()));
                dateClmn.setCellValueFactory(e-> new SimpleStringProperty(DateHelper.formatDate(e.getValue().getDate())));
                gradeClmn.setCellValueFactory(e-> new SimpleStringProperty(String.valueOf(e.getValue().getFinalGrade())));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeExam(ActionEvent event) throws Exception {
        try {
            PhdsExams examToRemove =  (PhdsExams)examsTable.getSelectionModel().getSelectedItem();
            phdService.removePhdExam(examToRemove);

            examsTable.getItems().clear();
            examsTable.getItems().addAll(FXCollections.observableArrayList(phdService.findExamByPhdId(phd.getId())));

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewExam(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../viewExam.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            ViewExamController viewExamController = fxmlLoader.getController();
            viewExamController.init((PhdsExams)examsTable.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddingPublication(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addPublications.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddPublicationController addPublicationController = fxmlLoader.getController();
            addPublicationController.inint(phd.getId(), null);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            List<Publication> publications = publicationService.getPublication().stream()
                    .filter(p -> p.getPhdId() == phd.getId())
                    .collect(Collectors.toList());
            publicationsTable.getItems().clear();
            publicationsTable.getItems().addAll(publications);
            if(!publicationsTable.getItems().isEmpty()) {
                subjectPubClmn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getTitle()));
                dateClmn.setCellValueFactory(p-> new SimpleStringProperty(DateHelper.formatDate(p.getValue().getDate())));
            }
            removePulicationBtn.disableProperty().bind(publicationsTable.getSelectionModel().selectedItemProperty().isNull());
            viewPublicationBtn.disableProperty().bind(publicationsTable.getSelectionModel().selectedItemProperty().isNull());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEditPublication(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addPublications.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddPublicationController addPublicationController = fxmlLoader.getController();
            addPublicationController.inint(phd.getId(), (Publication) publicationsTable.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            List<Publication> publications = publicationService.getPublication().stream()
                    .filter(p -> p.getPhdId() == phd.getId())
                    .collect(Collectors.toList());
            publicationsTable.getItems().clear();
            publicationsTable.getItems().addAll(publications);
            if(!publicationsTable.getItems().isEmpty()) {
                subjectPubClmn.setCellValueFactory(p-> new SimpleStringProperty(p.getValue().getTitle()));
                dateClmn.setCellValueFactory(p-> new SimpleStringProperty(DateHelper.formatDate(p.getValue().getDate())));
            }
            removePulicationBtn.disableProperty().bind(publicationsTable.getSelectionModel().selectedItemProperty().isNull());
            viewPublicationBtn.disableProperty().bind(publicationsTable.getSelectionModel().selectedItemProperty().isNull());

            inint(phdService.findById(phd.getId()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removePublication(ActionEvent event) throws Exception {
        try {
            Publication publicationToRemove =  (Publication) publicationsTable.getSelectionModel().getSelectedItem();
            publicationService.removePublication(publicationToRemove);

            publicationsTable.getItems().clear();
            List<Publication> publications = publicationService.getPublication().stream()
                    .filter(p -> p.getPhdId() == phd.getId())
                    .collect(Collectors.toList());
            publicationsTable.getItems().addAll(publications);
        } catch(Exception e) {
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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewAnnottation(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addAnnotation.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddAnnotationController addAnnotationController = fxmlLoader.getController();
            addAnnotationController.init(annotationService.findById(phd.getAnnotationId()), supervisors);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            annotationTxt.setText(annotationService.findById(phd.getAnnotationId()).getSubject());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateIndividualPlanFile(ActionEvent event) throws Exception {
        try {
            String filePath = FileHelper.createPdfFile((phd.getName() + "IndividulaPlan"),
                    pdfFileCreator.createIndividualPlan(
                    phd,
                    professionalService.findById(phd.getProfessionalId()),
                    annotationService.findById(phd.getAnnotationId()),
                    supervisors));
            phd.setIndividualPlanFilePath(filePath);
            phdService.updatePhd(phd);
            FileHelper.openFile(filePath);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
