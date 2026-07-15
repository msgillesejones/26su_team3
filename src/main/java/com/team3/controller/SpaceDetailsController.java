package com.team3.controller;

import com.team3.model.Space;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SpaceDetailsController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label capacityLabel;

    @FXML
    private Label featuresLabel;

    public void setSpace(Space space) {
        nameLabel.setText(space.getName());
        capacityLabel.setText("Capacity: " + space.getCapacity());
        featuresLabel.setText("Features: " + space.getFeatures());
    }
}

