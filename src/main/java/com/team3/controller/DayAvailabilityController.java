package com.team3.controller;
import com.team3.model.TimeSlot;
import com.team3.model.ReservationRecord;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.team3.model.Space;

public class DayAvailabilityController {
    private Space selectedSpace;
    private ReservationController reservationController;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<TimeSlot> scheduleTable;

    @FXML
    private TableColumn<TimeSlot, String> timeColumn;

    @FXML
    private TableColumn<TimeSlot, String> statusColumn;

    @FXML
    private TextField userNameField;

    @FXML
    private Label messageLabel;

    public void setSpace(Space space) {
        this.selectedSpace = space;
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    public void initialize() {
        timeColumn.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getTime())
        );

        statusColumn.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getStatus())
        );
    }

    public void loadAvailability() {

        List<TimeSlot> slots = new ArrayList<>();

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(20, 0);

        while (!start.equals(end)) {
            LocalTime slotEnd = start.plusMinutes(30);
            String timeLabel = start + " - " + slotEnd;

            boolean available = reservationController.isAvailable(
                    selectedSpace.getName(),
                    datePicker.getValue().toString(),
                    start.toString(),
                    slotEnd.toString()
            );

            String status;

            if (available) {
                status = "Available";
            } else {
                status = "Reserved";
            }

            slots.add(new TimeSlot(timeLabel, status));
            start = start.plusMinutes(30);
        }

        scheduleTable.getItems().setAll(slots);
    }
    @FXML
    public void createReservation() {
        TimeSlot selectedTime = scheduleTable.getSelectionModel().getSelectedItem();

        if (selectedSpace == null || datePicker.getValue() == null) {
            messageLabel.setText("Please select a space and date.");
            return;
        }

        if (selectedTime == null) {
            messageLabel.setText("Please select a time slot.");
            return;
        }

        if (!selectedTime.getStatus().equals("Available")) {
            messageLabel.setText("That time slot is already reserved.");
            return;
        }

        String userName = userNameField.getText().trim();

        if (userName.isEmpty()) {
            messageLabel.setText("Please enter a user name.");
            return;
        }

        String[] times = selectedTime.getTime().split(" - ");

        ReservationRecord reservation = new ReservationRecord(
                selectedSpace.getName(),
                userName,
                datePicker.getValue().toString(),
                times[0],
                times[1]
        );

        boolean added = reservationController.addReservation(reservation);

        if (added) {
            messageLabel.setText("Reservation created successfully.");
            loadAvailability();
        } else {
            messageLabel.setText("Unable to create reservation.");
        }
    }
}
