package com.team3.controller;

import com.team3.model.Space;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ViewAllSpacesController {

    @FXML
    private javafx.scene.control.TextField capacityFilterField;

    @FXML
    private TableView<Space> table;

    @FXML
    private TableColumn<Space, String> nameColumn;

    @FXML
    private TableColumn<Space, Integer> capacityColumn;

    @FXML
    private TableColumn<Space, String> featuresColumn;

    @FXML
    private javafx.scene.control.TextField nameSearchField;

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

        // US-5: Search by name
        FilteredList<Space> filteredList = new FilteredList<>(table.getItems(), p -> true);
        table.setItems(filteredList);

        nameSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(space -> {

                if (newValue == null || newValue.isBlank()) {
                    return true;
                }

                String searchText = newValue.toLowerCase();
                return space.getName().toLowerCase().contains(searchText);
            });
        });

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

    public void applyCapacityFilter() {
        String input = capacityFilterField.getText().trim();

        // Validate numeric input
        int minCapacity;
        try {
            minCapacity = Integer.parseInt(input);

            if (minCapacity < 1 || minCapacity > 500) {
                System.out.println("Invalid capacity: must be between 1 and 500.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input: capacity must be numeric.");
            return;
        }

        // Filter spaces
        FilteredList<Space> filtered = table.getItems().filtered(space ->
            space.getCapacity() >= minCapacity
        );

        // Handle no matches
        if (filtered.isEmpty()) {
            System.out.println("No spaces match this capacity.");
            table.getItems().clear();
            return;
        }

        // Update table
        table.getItems().setAll(filtered);
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
