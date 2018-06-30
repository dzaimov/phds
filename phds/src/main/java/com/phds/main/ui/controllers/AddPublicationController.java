package com.phds.main.ui.controllers;

import com.phds.common.DateHelper;
import com.phds.common.SpringApplicationContext;
import com.phds.phd.model.Attestation;
import com.phds.phd.model.Publication;
import com.phds.phd.service.AttestationService;
import com.phds.phd.service.PublicationService;
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
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.phds.common.ValidationUtil.validateCombobox;
import static com.phds.common.ValidationUtil.validateTextArea;
import static com.phds.common.ValidationUtil.validateTextField;

@Controller
@Getter
public class AddPublicationController implements Initializable {

    @FXML private Button addPublication;
    @FXML private Label tittleLbl;
    @FXML private ComboBox<Attestation> attestationBox;
    @FXML private DatePicker dateBox;
    @FXML private TextField tittleTxt;
    @FXML private TextArea authorsTxt;
    @FXML private TextArea sourcesTxt;
    @FXML private TextField placeTxt;
    @FXML private TextField isbnTxt;
    @FXML private TextField pagesTxt;
    @FXML private Button closeBtn;

    private int phdId;
    private Publication publication;

    @Autowired private AttestationService attestationService;
    @Autowired private PublicationService publicationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        attestationBox.setCellFactory(param -> new ListCell<Attestation>() {
            @Override
            protected void updateItem(Attestation item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getProtocolDate()== null) {
                    setText(null);
                } else {
                    setText(item.getProtocolNumber() + "/" + DateHelper.formatDate(item.getProtocolDate()) + " от " + DateHelper.formatDate(item.getApprovedDate()));
                }
            }
        });
        attestationBox.getSelectionModel().selectFirst();
    }

    public void inint(int phdId, Publication publication) {
        this.phdId = phdId;
        List<Attestation> attestationList = attestationService.getAttestation().stream()
                .filter(a -> a.getPhdId() == phdId)
                .collect(Collectors.toList());
        attestationBox.getItems().addAll(attestationList);

        if(publication != null) {
            this.publication = publication;
            tittleLbl.setText("Редактиране на анотация:");
            attestationBox.getSelectionModel().select(attestationService.findById(publication.getAttestationId()));
            authorsTxt.setText(publication.getAuthors());
            java.util.Date approvedDate = publication.getCreatedDate();
            Calendar approvedDateCalendar = new GregorianCalendar();
            approvedDateCalendar.setTime(approvedDate);
            dateBox.setValue(LocalDate.of(
                    approvedDateCalendar.get(Calendar.YEAR),
                    approvedDateCalendar.get(Calendar.MONTH) + 1,
                    approvedDateCalendar.get(Calendar.DAY_OF_MONTH)));
            isbnTxt.setText(publication.getIsbn());
            pagesTxt.setText(String.valueOf(publication.getPages()));
            placeTxt.setText(publication.getPlace());
            sourcesTxt.setText(publication.getSources());
            tittleTxt.setText(publication.getTitle());
            addPublication.setText("Запази");
        }

    }


    @FXML
    public void createPublication(ActionEvent event) throws Exception {
        if(validateInput()) {
            try {
                Instant instantDate = dateBox.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Publication publicationToAdd = Publication.builder()
                        .attestationId(attestationBox.getSelectionModel().getSelectedItem().getId())
                        .authors(authorsTxt.getText())
                        .createdDate(Date.from(instantDate))
                        .isbn(isbnTxt.getText())
                        .pages(Integer.valueOf(pagesTxt.getText()))
                        .phdId(phdId)
                        .place(placeTxt.getText())
                        .sources(sourcesTxt.getText())
                        .title(tittleTxt.getText())
                        .build();

                if(publication != null) {
                    publicationToAdd.setId(publication.getId());
                    publicationService.updatePublication(publicationToAdd);
                } else {
                    publicationService.insertPublication(publicationToAdd);
                }

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
    public void addAttestation(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../addAttestation.fxml"));
            fxmlLoader.setControllerFactory(SpringApplicationContext.getApplicationContext()::getBean);
            Parent root = fxmlLoader.load();
            AddAttestationController addAttestationController = fxmlLoader.getController();
            addAttestationController.inint(phdId);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initOwner(((Node)event.getTarget()).getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            List<Attestation> attestationList = attestationService.getAttestation().stream()
                    .filter(a -> a.getPhdId() == phdId)
                    .collect(Collectors.toList());
            attestationBox.getItems().addAll(attestationList);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closePublicationWindow(ActionEvent event) throws Exception {
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
        if(validateCombobox(attestationBox)
                || validateTextField(tittleTxt)
                || validateTextArea(authorsTxt)
                || dateBox.getValue() == null
                || validateTextArea(sourcesTxt)
                || validateTextField(placeTxt)
                || validateTextField(isbnTxt)
                || validateTextField(pagesTxt)) {
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
