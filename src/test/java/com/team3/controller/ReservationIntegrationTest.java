package com.team3.controller;

import java.util.Map;

import com.team3.model.ReservationRecord;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.team3.persistence.ReservationPersistence;

import java.nio.file.Files;
import java.nio.file.Path;

public class ReservationIntegrationTest {

    // US8 Create Reservation - Acceptance Test: valid reservation is created successfully.
    @Test
    public void createReservationIntegrationTest() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-20",
                "10:00",
                "11:00"
        );

        boolean created = controller.addReservation(reservation);

        assertTrue(created);
        assertEquals(1, controller.getReservations().size());
    }

    // US9 View My Reservations - Acceptance Test: only the selected user's reservations are displayed.
    @Test
    public void viewUserReservationsIntegrationTest() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord gilleseReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-20",
                "10:00",
                "11:00"
        );

        ReservationRecord jessicaReservation = new ReservationRecord(
                "Auditorium",
                "Jessica",
                "2026-07-21",
                "12:00",
                "13:00"
        );

        controller.addReservation(gilleseReservation);
        controller.addReservation(jessicaReservation);

        List<ReservationRecord> results =
                controller.getReservationsForUser("Gillese");

        assertEquals(1, results.size());
        assertEquals("Gillese", results.get(0).getUserName());
    }
    // US21 Persist Reservations - Acceptance Test
    @Test
    public void persistReservationsIntegrationTest() throws Exception {
        Path tempFile = Files.createTempFile("reservations", ".json");

        ReservationPersistence persistence =
                new ReservationPersistence(tempFile.toString());

        ReservationController controller =
                new ReservationController(persistence, false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-20",
                "10:00",
                "11:00"
        );

        assertTrue(controller.addReservation(reservation));

        ReservationController reloadedController =
                new ReservationController(persistence, true);

        assertEquals(1, reloadedController.getReservations().size());
        assertEquals(
                "Gillese",
                reloadedController.getReservations().get(0).getUserName()
        );
    }
    // US22 Daily Summary - Acceptance Test
    @Test
    public void dailySummaryIntegrationTest() {
        ReservationController controller = new ReservationController(false);

        controller.addReservation(new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-22",
                "10:00",
                "11:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Auditorium",
                "Jessica",
                "2026-07-22",
                "12:00",
                "13:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Conference Room",
                "Skylar",
                "2026-07-23",
                "09:00",
                "10:00"
        ));

        Map<String, Integer> summary = controller.getDailySummary();

        assertEquals(2, summary.size());
        assertEquals(2, summary.get("2026-07-22"));
        assertEquals(1, summary.get("2026-07-23"));
        assertEquals(
                java.util.Arrays.asList("2026-07-22", "2026-07-23"),
                new java.util.ArrayList<>(summary.keySet())
        );
    }
    // US23 View All Reservations - Acceptance Test
    @Test
    public void viewAllReservationsIntegrationTest() {
        ReservationController controller = new ReservationController(false);

        controller.addReservation(new ReservationRecord(
                "Auditorium",
                "Jessica",
                "2026-07-23",
                "12:00",
                "13:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-22",
                "10:00",
                "11:00"
        ));

        java.util.List<ReservationRecord> results =
                controller.getAllReservations();

        assertEquals(2, results.size());
        assertEquals("2026-07-22", results.get(0).getDate());
        assertEquals("Gillese", results.get(0).getUserName());
        assertEquals("2026-07-23", results.get(1).getDate());
        assertEquals("Jessica", results.get(1).getUserName());
    }
    // US24 Usage Report - Acceptance Test
    @Test
    public void usageReportIntegrationTest() {
        ReservationController controller = new ReservationController(false);

        controller.addReservation(new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-22",
                "10:00",
                "11:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-23",
                "12:00",
                "13:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Auditorium",
                "Skylar",
                "2026-07-24",
                "09:00",
                "10:00"
        ));

        java.util.Map<String, Integer> report =
                controller.getUsageReport();

        assertEquals(1, report.get("Auditorium"));
        assertEquals(0, report.get("Conference Room"));
        assertEquals(2, report.get("Study Room"));
    }
    // US25 Filter Reports - Acceptance Test
    @Test
    public void filterReportsByDateRangeIntegrationTest() {
        ReservationController controller = new ReservationController(false);

        controller.addReservation(new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-22",
                "10:00",
                "11:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Conference Room",
                "Skylar",
                "2026-07-23",
                "12:00",
                "13:00"
        ));

        controller.addReservation(new ReservationRecord(
                "Auditorium",
                "Alex",
                "2026-07-24",
                "09:00",
                "10:00"
        ));

        java.util.List<ReservationRecord> results =
                controller.getReservationsByDateRange(
                        "2026-07-22",
                        "2026-07-23"
                );

        assertEquals(2, results.size());
        assertEquals("2026-07-22", results.get(0).getDate());
        assertEquals("2026-07-23", results.get(1).getDate());
    }
}
