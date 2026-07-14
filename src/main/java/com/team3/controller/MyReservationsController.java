package com.team3.controller;

import com.team3.model.ReservationRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class MyReservationsController {

    @FXML
    private TextField userField;

    @FXML
    private TextField newStartTimeField;

    @FXML
    private TextField newEndTimeField;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<ReservationRecord> reservationTable;

    @FXML
    private TableColumn<ReservationRecord, String> spaceColumn;

    @FXML
    private TableColumn<ReservationRecord, String> dateColumn;

    @FXML
    private TableColumn<ReservationRecord, String> timeColumn;

    private ReservationController reservationController;

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    public void initialize() {
        spaceColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSpaceName())
        );

        dateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDate())
        );

        timeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStartTime()
                                + " - "
                                + data.getValue().getEndTime()
                )
        );
    }

    @FXML
    public void loadReservations() {
        String userName = userField.getText().trim();

        if (userName.isEmpty()) {
            messageLabel.setText("Please enter a user name.");
            reservationTable.getItems().clear();
            return;
        }

        List<ReservationRecord> reservations =
                reservationController.getReservationsForUser(userName);

        reservationTable.getItems().setAll(reservations);

        if (reservations.isEmpty()) {
            messageLabel.setText("No reservations found.");
        } else {
            messageLabel.setText("Reservations loaded successfully.");
        }
    }
    @FXML
    public void cancelReservation() {
        ReservationRecord selected =
                reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Please select a reservation to cancel.");
            return;
        }

        boolean cancelled =
                reservationController.cancelReservation(selected);

        if (cancelled) {
            loadReservations();
            messageLabel.setText("Reservation cancelled successfully.");
        } else {
            messageLabel.setText("Unable to cancel reservation.");
        }
    }
    @FXML
    public void modifyReservation() {
        ReservationRecord selected =
                reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Please select a reservation to modify.");
            return;
        }

        String newStartTime = newStartTimeField.getText().trim();
        String newEndTime = newEndTimeField.getText().trim();

        if (newStartTime.isEmpty() || newEndTime.isEmpty()) {
            messageLabel.setText("Please enter a new start and end time.");
            return;
        }

        ReservationRecord updatedReservation = new ReservationRecord(
                selected.getSpaceName(),
                selected.getUserName(),
                selected.getDate(),
                newStartTime,
                newEndTime
        );

        boolean modified = reservationController.modifyReservation(
                selected,
                updatedReservation
        );

        if (modified) {
            loadReservations();
            messageLabel.setText("Reservation modified successfully.");
            newStartTimeField.clear();
            newEndTimeField.clear();
        } else {
            messageLabel.setText("Unable to modify reservation. Check the time or availability.");
        }
    }
}
