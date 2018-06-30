package com.phds.main.ui.controllers;

import com.phds.common.DateHelper;
import com.phds.common.SpringApplicationContext;
import com.phds.common.ValidationUtil;
import com.phds.phd.model.Phd;
import com.phds.phd.model.PhdsExams;
import com.phds.phd.service.PhdService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


@Controller
public class MainController implements Initializable {

    @FXML public Pane rootPane;
    @FXML public TableView<Phd> phdsTable;
    @FXML public TableColumn nameColumn;
    @FXML public TableColumn typeColumn;
    @FXML public TableColumn currentYearColumn;
    @FXML public TableColumn specialtyColumn;
    @FXML public TableColumn<Phd, String>  begginingDateColumn;
    @FXML public TableColumn<Phd, String> graduationDateColumn;
    @FXML public ImageView backroundImg;
    @FXML public Button addBtn;
    @FXML public Button viewBtn;
    @FXML public Button deletebtn;
    @FXML public DatePicker toDate;
    @FXML public DatePicker fromDate;
    @FXML public RadioButton startBtn;
    @FXML public RadioButton finishedBtn;
    @FXML public Label numberLbl;
    @FXML public Button statisticsBtn;

    private ToggleGroup toggleGroup;

    @Autowired private PhdService phdService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializePhdsTable();
        File file = new File("C:\\Users\\Melike\\IdeaProjects\\phds\\src\\main\\resources\\item_tutechnicaluniversitystoyan.jpg");
        Image image = new Image(file.toURI().toString());
        backroundImg.setImage(image);
        backroundImg.setFitHeight(600);

        File logoFile = new File("C:\\Users\\Melike\\IdeaProjects\\phds\\src\\main\\resources\\Logo-TU.png");
        Image logoImg = new Image(logoFile.toURI().toString());
        phdsTable.getSelectionModel().clearSelection();

        deletebtn.disableProperty().bind(phdsTable.getSelectionModel().selectedItemProperty().isNull());
        viewBtn.disableProperty().bind(phdsTable.getSelectionModel().selectedItemProperty().isNull());

        toggleGroup = new ToggleGroup();
        startBtn.setToggleGroup(toggleGroup);
        startBtn.setUserData("start");
        finishedBtn.setToggleGroup(toggleGroup);
        finishedBtn.setUserData("finish");
        startBtn.setSelected(true);
    }


    private void initializePhdsTable() {
        phdsTable.setPlaceholder(new Label("Няма съществуващи записи"));

        ScrollPane sp = new ScrollPane(phdsTable);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        currentYearColumn.setCellValueFactory(new PropertyValueFactory<>("currentYear"));
        specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        phdsTable.getItems().addAll(phdService.getPhds());
        numberLbl.setText(String.valueOf(phdsTable.getItems().size()));
        if(!phdsTable.getItems().isEmpty()) {
            begginingDateColumn.setCellValueFactory(p-> new SimpleStringProperty(
                    DateHelper.formatDate(p.getValue().getBegginingDate())));
            graduationDateColumn.setCellValueFactory(p-> new SimpleStringProperty(
                    DateHelper.formatDate(p.getValue().getGraduationDate())));
        }
    }

    @FXML
    public void doStatistic(ActionEvent event) throws Exception {
        if (fromDate.getValue() != null && toDate.getValue() != null) {
            try {
                Date fromD = Date.from(fromDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                Date toD = Date.from(toDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                List<Phd> phds = phdService.getPhds().stream()
                        .filter(p -> between(p.getBegginingDate(), fromD, toD))
                        .collect(Collectors.toList());

                if(toggleGroup.getSelectedToggle().getUserData().equals("finish")) {
                    phds = phdService.getPhds().stream()
                            .filter(p -> between(p.getGraduationDate(), fromD, toD))
                            .collect(Collectors.toList());
                }

                phdsTable.getItems().clear();
                phdsTable.getItems().addAll(phds);
                numberLbl.setText(String.valueOf(phdsTable.getItems().size()));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public void openAddingPhd(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addPhd.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Pane addViewPane = (Pane) fxmlLoader.load();
            rootPane.getChildren().setAll(addViewPane);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void openStatistics(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../statistics.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Pane addViewPane = (Pane) fxmlLoader.load();
            rootPane.getChildren().setAll(addViewPane);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPhd(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../viewPhd.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Pane addViewPane = (Pane) fxmlLoader.load();
            ViewPhdController viewPhdController = fxmlLoader.getController();
            Phd phd =  (Phd)phdsTable.getSelectionModel().getSelectedItem();
            viewPhdController.inint(phd);
            rootPane.getChildren().setAll(addViewPane);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removePhd(ActionEvent event) throws Exception {
        try {
            Phd phdToRemove =  (Phd)phdsTable.getSelectionModel().getSelectedItem();
            phdService.removePhd(phdToRemove);
            phdsTable.getItems().clear();
            phdsTable.getItems().addAll(phdService.getPhds());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeApp(ActionEvent event) throws Exception {
        try {
            Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
