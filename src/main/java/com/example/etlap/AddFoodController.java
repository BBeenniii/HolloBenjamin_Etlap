package com.example.etlap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.example.etlap.Database;

public class AddFoodController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField priceField;
    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addNewFood());
    }

    private void addNewFood() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String priceText = priceField.getText();
        String description = "Nincs leírás";

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
            System.out.println("Minden mezőt ki kell tölteni!");
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceText);
        }
        catch (NumberFormatException e) {
            System.out.println("Az árnak számnak kell lennie!");
            return;
        }

        String sql = "INSERT INTO etlap (nev, kategoria, ar, leiras) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setInt(3, price);
            stmt.setString(4, description); // Leírás hozzáadása
            stmt.executeUpdate();
            System.out.println("Étel sikeresen hozzáadva!");
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();

        }
        catch (SQLException e) {
            System.out.println("Hiba az adatbázisművelet során: " + e.getMessage());
        }
    }
}