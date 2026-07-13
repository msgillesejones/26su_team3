package com.team3.controller;
import com.team3.model.TimeSlot;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import com.team3.model.Space;

public class DayAvailabilityController {
    private Space selectedSpace;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<TimeSlot> scheduleTable;

    @FXML
    private TableColumn<TimeSlot, String> timeColumn;

    @FXML
    private TableColumn<TimeSlot, String> statusColumn;

    public void setSpace(Space space) {
        this.selectedSpace = space;
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
            String timeLabel = start + " - " + start.plusMinutes(30);
            slots.add(new TimeSlot(timeLabel, "Available"));
            start = start.plusMinutes(30);
        }

        scheduleTable.getItems().setAll(slots);
    }
}
