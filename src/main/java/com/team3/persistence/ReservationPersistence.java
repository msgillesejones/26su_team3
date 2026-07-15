package com.team3.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team3.model.ReservationRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReservationPersistence {

    private static final String FILE_NAME = "reservations.json";

    public void saveReservations(List<ReservationRecord> reservations) {
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(reservations, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ReservationRecord> loadReservations() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file)) {
            Type reservationListType =
                    new TypeToken<List<ReservationRecord>>() { }.getType();

            List<ReservationRecord> reservations =
                    gson.fromJson(reader, reservationListType);

            if (reservations == null) {
                return new ArrayList<>();
            }

            return reservations;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}