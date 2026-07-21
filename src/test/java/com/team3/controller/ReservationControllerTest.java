package com.team3.controller;

import com.team3.model.ReservationRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationControllerTest {

    @Test
    public void validReservationCanBeAdded() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        boolean result = controller.addReservation(reservation);

        assertTrue(result);
    }
    @Test
    public void reservationOverTwoHoursIsRejected() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "13:00"
        );

        boolean result = controller.addReservation(reservation);

        assertFalse(result);
    }
    @Test
    public void overlappingReservationIsRejected() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord firstReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord overlappingReservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-12",
                "10:30",
                "11:30"
        );

        controller.addReservation(firstReservation);

        boolean result = controller.addReservation(overlappingReservation);

        assertFalse(result);
    }
    @Test
    public void endTimeBeforeStartTimeIsRejected() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "11:00",
                "10:00"
        );

        boolean result = controller.addReservation(reservation);

        assertFalse(result);
    }
    @Test
    public void reservationWithinTenMinuteBufferIsRejected() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord firstReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord secondReservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-12",
                "11:05",
                "12:00"
        );

        controller.addReservation(firstReservation);

        boolean result = controller.addReservation(secondReservation);

        assertFalse(result);
    }
    @Test
    public void onlyUsersReservationsAreReturned() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord gilleseReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord jessicaReservation = new ReservationRecord(
                "Auditorium",
                "Jessica",
                "2026-07-12",
                "12:00",
                "13:00"
        );

        controller.addReservation(gilleseReservation);
        controller.addReservation(jessicaReservation);

        java.util.List<ReservationRecord> results =
                controller.getReservationsForUser("Gillese");

        assertEquals(1, results.size());
        assertEquals("Gillese", results.get(0).getUserName());
    }
    @Test
    public void userReservationsAreSortedByDateAndStartTime() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord laterReservation = new ReservationRecord(
                "Auditorium",
                "Gillese",
                "2026-07-13",
                "12:00",
                "13:00"
        );

        ReservationRecord earlierReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        controller.addReservation(laterReservation);
        controller.addReservation(earlierReservation);

        java.util.List<ReservationRecord> results =
                controller.getReservationsForUser("Gillese");

        assertEquals("2026-07-12", results.get(0).getDate());
        assertEquals("10:00", results.get(0).getStartTime());
    }
    @Test
    public void reservationCanBeCancelled() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        controller.addReservation(reservation);

        boolean cancelled = controller.cancelReservation(reservation);

        assertTrue(cancelled);
        assertEquals(0, controller.getReservations().size());
    }
    @Test
    public void reservationCanBeModified() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord originalReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord updatedReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "12:00",
                "13:00"
        );

        controller.addReservation(originalReservation);

        boolean modified = controller.modifyReservation(
                originalReservation,
                updatedReservation
        );

        assertTrue(modified);
        assertEquals(
                "12:00",
                controller.getReservations().get(0).getStartTime()
        );
    }
    @Test
    public void invalidReservationModificationIsRejected() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord originalReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord invalidReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "13:00",
                "10:00"
        );

        controller.addReservation(originalReservation);

        boolean modified = controller.modifyReservation(
                originalReservation,
                invalidReservation
        );

        assertFalse(modified);
        assertEquals(
                "10:00",
                controller.getReservations().get(0).getStartTime()
        );
    }

    @Test
    public void availableTimesAreSuggested() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-20",
                "10:00",
                "11:00"
        );

        controller.addReservation(reservation);

        java.util.List<String> suggestions =
                controller.suggestAvailableTimes("Study Room", "2026-07-20");

        assertFalse(suggestions.contains("10:00 - 11:00"));
        assertTrue(suggestions.contains("08:00 - 09:00"));
    }
    @Test
    public void ownerCanModifyOwnReservation() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord originalReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord updatedReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "12:00",
                "13:00"
        );

        controller.addReservation(originalReservation);

        boolean modified = controller.modifyReservationForUser(
                originalReservation,
                updatedReservation,
                "Gillese"
        );

        assertTrue(modified);
        assertEquals(
                "12:00",
                controller.getReservations().get(0).getStartTime()
        );
    }
    @Test
    public void nonOwnerCannotModifyReservation() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord originalReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "10:00",
                "11:00"
        );

        ReservationRecord updatedReservation = new ReservationRecord(
                "Study Room",
                "Gillese",
                "2026-07-12",
                "12:00",
                "13:00"
        );

        controller.addReservation(originalReservation);

        boolean modified = controller.modifyReservationForUser(
                originalReservation,
                updatedReservation,
                "Jessica"
        );

        assertFalse(modified);
        assertEquals(
                "10:00",
                controller.getReservations().get(0).getStartTime()
        );
    }
    @Test
    public void adminCanCreateReservationForAnotherUser() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-22",
                "10:00",
                "11:00"
        );

        boolean created = controller.adminCreateReservation(
                reservation,
                true
        );

        assertTrue(created);
        assertEquals(1, controller.getReservationsForUser("Jessica").size());
    }
    @Test
    public void nonAdminCannotCreateReservationForAnotherUser() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-22",
                "10:00",
                "11:00"
        );

        boolean created = controller.adminCreateReservation(
                reservation,
                false
        );

        assertFalse(created);
        assertEquals(0, controller.getReservations().size());
    }
    @Test
    public void adminCanModifyAnotherUsersReservation() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord originalReservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-23",
                "10:00",
                "11:00"
        );

        ReservationRecord updatedReservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-23",
                "12:00",
                "13:00"
        );

        controller.addReservation(originalReservation);

        boolean modified = controller.adminModifyReservation(
                originalReservation,
                updatedReservation,
                true
        );

        assertTrue(modified);
        assertEquals(
                "12:00",
                controller.getReservations().get(0).getStartTime()
        );
    }
    @Test
    public void nonAdminCannotModifyAnotherUsersReservation() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord originalReservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-23",
                "10:00",
                "11:00"
        );

        ReservationRecord updatedReservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-23",
                "12:00",
                "13:00"
        );

        controller.addReservation(originalReservation);

        boolean modified = controller.adminModifyReservation(
                originalReservation,
                updatedReservation,
                false
        );

        assertFalse(modified);
        assertEquals(
                "10:00",
                controller.getReservations().get(0).getStartTime()
        );
    }
    @Test
    public void adminCanCancelAnotherUsersReservation() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-24",
                "10:00",
                "11:00"
        );

        controller.addReservation(reservation);

        boolean cancelled = controller.adminCancelReservation(
                reservation,
                true
        );

        assertTrue(cancelled);
        assertEquals(0, controller.getReservations().size());
    }
    @Test
    public void nonAdminCannotCancelAnotherUsersReservation() {
        ReservationController controller = new ReservationController(false);

        ReservationRecord reservation = new ReservationRecord(
                "Study Room",
                "Jessica",
                "2026-07-24",
                "10:00",
                "11:00"
        );

        controller.addReservation(reservation);

        boolean cancelled = controller.adminCancelReservation(
                reservation,
                false
        );

        assertFalse(cancelled);
        assertEquals(1, controller.getReservations().size());
    }
}