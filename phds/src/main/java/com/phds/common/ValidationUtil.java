package com.phds.common;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtil {

    public static boolean validateTextField(TextField textField) {
        return textField.getText().trim().isEmpty();
    }

    public static boolean validateTextArea(TextArea textArea) {
        return textArea.getText().trim().isEmpty();
    }

    public static boolean validateCombobox(ComboBox comboBox) {
        return comboBox.getSelectionModel().getSelectedItem() == null;
    }

    public static boolean validateListView(ListView listView) {
        return listView.getItems().isEmpty();
    }

}
