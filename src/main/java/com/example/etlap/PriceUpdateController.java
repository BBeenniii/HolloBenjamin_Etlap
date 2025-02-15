package com.example.etlap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PriceUpdateController {
    @FXML
    private ChoiceBox<String> increaseType;
    @FXML
    private TextField increaseValue;
    @FXML
    private Button okButton;

    private Integer increaseAmount = null;
    private boolean isPercentage = false;

    @FXML
    public void initialize() {
        okButton.setOnAction(event -> {
            String valueText = increaseValue.getText();
            if (valueText.isEmpty()) {
                System.out.println("Nincs megadva érték!");
                return;
            }

            try {
                increaseAmount = Integer.parseInt(valueText);
                isPercentage = increaseType.getValue().equals("Százalékos (%)");
            }
            catch (NumberFormatException e) {
                System.out.println("Hibás érték!");
                return;
            }

            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        });
    }

    public Integer getIncreaseAmount() {
        return increaseAmount;
    }

    public boolean isPercentage() {
        return isPercentage;
    }
}