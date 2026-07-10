package com.team3.controller;

import com.team3.model.Space;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ViewAllSpacesController {

    @FXML
    private TableView<Space> table;

    @FXML
    private TableColumn<Space, String> nameColumn;

    @FXML
    private TableColumn<Space, Integer> capacityColumn;

    @FXML
    private TableColumn<Space, String> featuresColumn;

    public void initialize() {

        // Set up columns
        nameColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getName())
        );

        capacityColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCapacity()).asObject()
        );

        featuresColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getFeatures())
        );

        // Sample data
        table.getItems().addAll(
            new Space("Conference Room", 20, "Projector, Whiteboard"),
            new Space("Auditorium", 200, "Stage, Sound System"),
            new Space("Study Room", 6, "Quiet Space")
        );

        // Double-click handler
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Space selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openDetails(selected);
                }
            }
        });
    }

    private void openDetails(Space space) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/_6su_team3/view-space-details.fxml")
            );

            Parent root = loader.load();

            SpaceDetailsController controller = loader.getController();
            controller.setSpace(space);

            Stage stage = new Stage();
            stage.setTitle("Space Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
