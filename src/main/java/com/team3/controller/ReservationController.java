package com.team3.controller;

import com.team3.persistence.ReservationPersistence;
import com.team3.model.ReservationRecord;

public class ReservationController {
    private final java.util.List<ReservationRecord> reservations =
            new java.util.ArrayList<>();

    private final ReservationPersistence persistence =
            new ReservationPersistence();

    public ReservationController() {
        reservations.addAll(persistence.loadReservations());
    }
    public boolean addReservation(ReservationRecord reservation) {
        if (!isValidTimeRange(
                reservation.getStartTime(),
                reservation.getEndTime()
        )) {
            return false;
        }
        boolean available = isAvailable(
                reservation.getSpaceName(),
                reservation.getDate(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (!available) {
            return false;
        }

        reservations.add(reservation);
        return true;
    }
    public java.util.List<ReservationRecord> getReservations() {
        return reservations;
    }
    public java.util.List<ReservationRecord> getReservationsForUser(String userName) {
        java.util.List<ReservationRecord> userReservations =
                new java.util.ArrayList<>();

        for (ReservationRecord reservation : reservations) {
            if (reservation.matchesUser(userName)) {
                userReservations.add(reservation);
            }
        }

        userReservations.sort(
                java.util.Comparator
                        .comparing(ReservationRecord::getDate)
                        .thenComparing(ReservationRecord::getStartTime)
        );

        return userReservations;
    }
    public boolean cancelReservation(ReservationRecord reservation) {
        return reservations.remove(reservation);
    }
    public boolean modifyReservation(ReservationRecord oldReservation,
                                     ReservationRecord newReservation) {
        int index = reservations.indexOf(oldReservation);

        if (index == -1) {
            return false;
        }

        if (!isValidTimeRange(
                newReservation.getStartTime(),
                newReservation.getEndTime()
        )) {
            return false;
        }

        reservations.remove(index);

        boolean available = isAvailable(
                newReservation.getSpaceName(),
                newReservation.getDate(),
                newReservation.getStartTime(),
                newReservation.getEndTime()
        );

        if (!available) {
            reservations.add(index, oldReservation);
            return false;
        }

        reservations.add(index, newReservation);
        return true;
    }
    public boolean isAvailable(String spaceName, String date, String startTime, String endTime) {
        for (ReservationRecord reservation : reservations) {
            boolean sameSpace = reservation.getSpaceName().equalsIgnoreCase(spaceName);
            boolean sameDate = reservation.getDate().equals(date);

            if (sameSpace && sameDate) {
                java.time.LocalTime requestedStart = java.time.LocalTime.parse(startTime);
                java.time.LocalTime requestedEnd = java.time.LocalTime.parse(endTime);

                java.time.LocalTime existingStart =
                        java.time.LocalTime.parse(reservation.getStartTime());
                java.time.LocalTime existingEnd =
                        java.time.LocalTime.parse(reservation.getEndTime());

                boolean overlapsOrBreaksBuffer =
                        requestedStart.isBefore(existingEnd.plusMinutes(10))
                                && requestedEnd.isAfter(existingStart.minusMinutes(10));

                if (overlapsOrBreaksBuffer) {
                    return false;
                }
            }
        }

        return true;
    }
    private boolean isValidTimeRange(String startTime, String endTime) {
        try {
            java.time.LocalTime start = java.time.LocalTime.parse(startTime);
            java.time.LocalTime end = java.time.LocalTime.parse(endTime);

            if (!end.isAfter(start)) {
                return false;
            }

            long durationMinutes =
                    java.time.Duration.between(start, end).toMinutes();

            return durationMinutes <= 120;
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }
}
