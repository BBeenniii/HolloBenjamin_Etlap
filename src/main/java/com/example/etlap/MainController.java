package com.example.etlap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MainController {
    @FXML
    private TableView<Etel> etlapTable;
    @FXML
    private TableColumn<Etel, String> nameColumn;
    @FXML
    private TableColumn<Etel, String> categoryColumn;
    @FXML
    private TableColumn<Etel, Integer> priceColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button increasePriceButton;

    @FXML
    private void deleteSelectedFood() {
        Etel selectedFood = etlapTable.getSelectionModel().getSelectedItem();

        if (selectedFood == null) {
            System.out.println("Nincs kiválasztva étel!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Megerősítés");
        alert.setHeaderText("Biztosan törölni szeretnéd?");
        alert.setContentText("Az étel neve: " + selectedFood.getNev());

        if (alert.showAndWait().get() == ButtonType.OK) {
            String sql = "DELETE FROM etlap WHERE id = ?";

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, selectedFood.getId());
                stmt.executeUpdate();
                System.out.println("Étel törölve!");
                etlapTable.setItems(getEtlapLista());

            }
            catch (SQLException e) {
                System.out.println("Hiba a törlés során: " + e.getMessage());
            }
        }
    }

    @FXML
    private void increasePrice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("price-update-dialog.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Áremelés");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

            PriceUpdateController controller = loader.getController();
            Integer amount = controller.getIncreaseAmount();
            boolean isPercentage = controller.isPercentage();

            if (amount == null)
                return;

            Etel selectedFood = etlapTable.getSelectionModel().getSelectedItem();
            String sql;

            if (selectedFood == null) {
                sql = isPercentage ?
                        "UPDATE etlap SET ar = ar + (ar * ? / 100)" :
                        "UPDATE etlap SET ar = ar + ?";
            }
            else {
                sql = isPercentage ?
                        "UPDATE etlap SET ar = ar + (ar * ? / 100) WHERE id = ?" :
                        "UPDATE etlap SET ar = ar + ? WHERE id = ?";
            }

            try (Connection conn = Database.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, amount);
                if (selectedFood != null)
                    stmt.setInt(2, selectedFood.getId());

                stmt.executeUpdate();
                System.out.println("Áremelés sikeres!");
                etlapTable.setItems(getEtlapLista());
            }
            catch (SQLException e) {
                System.out.println("Hiba az áremelés során: " + e.getMessage());
            }

        }
        catch (IOException e) {
            System.out.println("Hiba az áremelés ablak megnyitásakor: " + e.getMessage());
        }
    }


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nev"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("ar"));
        etlapTable.setItems(getEtlapLista());
        addButton.setOnAction(event -> openAddFoodWindow());
        deleteButton.setOnAction(event -> deleteSelectedFood());
        increasePriceButton.setOnAction(event -> increasePrice());
    }

    private ObservableList<Etel> getEtlapLista() {
        return FXCollections.observableArrayList(EtelDB.getEtlap());
    }

    private void openAddFoodWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-food-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Új étel hozzáadása");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
            etlapTable.setItems(getEtlapLista());

        }
        catch (IOException e) {
            System.out.println("Hiba az új ablak megnyitásakor: " + e.getMessage());
        }
    }
}