package com.team3.controller;

import java.util.Map;
import java.util.TreeMap;

import com.team3.persistence.ReservationPersistence;
import com.team3.model.ReservationRecord;

public class ReservationController {
    private final java.util.List<ReservationRecord> reservations =
            new java.util.ArrayList<>();

    private final ReservationPersistence persistence;

    public ReservationController() {
        this(new ReservationPersistence(), true);
    }

    public ReservationController(boolean persistenceEnabled) {
        this(new ReservationPersistence(), persistenceEnabled);
    }

    public ReservationController(ReservationPersistence persistence) {
        this(persistence, true);
    }

    public ReservationController(
            ReservationPersistence persistence,
            boolean persistenceEnabled
    ) {
        this.persistence = persistence;

        if (persistenceEnabled) {
            reservations.addAll(persistence.loadReservations());
        }
    }
    public boolean adminCreateReservation(
            ReservationRecord reservation,
            boolean isAdmin
    ) {
        if (!isAdmin) {
            return false;
        }

        return addReservation(reservation);
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
        persistence.saveReservations(reservations);
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
        boolean removed = reservations.remove(reservation);

        if (removed) {
            persistence.saveReservations(reservations);
        }

        return removed;
    }
    public boolean modifyReservationForUser(
            ReservationRecord oldReservation,
            ReservationRecord newReservation,
            String loggedInUserName
    ) {
        if (loggedInUserName == null
                || !oldReservation.matchesUser(loggedInUserName)) {
            return false;
        }

        return modifyReservation(oldReservation, newReservation);
    }
    public boolean adminModifyReservation(
            ReservationRecord oldReservation,
            ReservationRecord newReservation,
            boolean isAdmin
    ) {
        if (!isAdmin) {
            return false;
        }

        return modifyReservation(oldReservation, newReservation);
    }
    public boolean adminCancelReservation(
            ReservationRecord reservation,
            boolean isAdmin
    ) {
        if (!isAdmin) {
            return false;
        }

        return cancelReservation(reservation);
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
        persistence.saveReservations(reservations);
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
    // US23 View All Reservations - returns every reservation in chronological order.
    public java.util.List<ReservationRecord> getAllReservations() {
        java.util.List<ReservationRecord> allReservations =
                new java.util.ArrayList<>(reservations);

        allReservations.sort(
                java.util.Comparator
                        .comparing(ReservationRecord::getDate)
                        .thenComparing(ReservationRecord::getStartTime)
        );

        return allReservations;
    }

    // US22 Daily Summary - groups reservation counts by date in chronological order.
    public Map<String, Integer> getDailySummary() {
        Map<String, Integer> dailySummary = new TreeMap<>();

        for (ReservationRecord reservation : reservations) {
            String date = reservation.getDate();

            dailySummary.put(
                    date,
                    dailySummary.getOrDefault(date, 0) + 1
            );
        }

        return dailySummary;
    }

    // US12 Suggest Times - returns available one-hour reservation times for a space and date.
    public java.util.List<String> suggestAvailableTimes(String spaceName, String date) {
        java.util.List<String> suggestedTimes = new java.util.ArrayList<>();

        java.time.LocalTime openingTime = java.time.LocalTime.of(8, 0);
        java.time.LocalTime closingTime = java.time.LocalTime.of(18, 0);

        java.time.LocalTime startTime = openingTime;

        while (!startTime.plusHours(1).isAfter(closingTime)) {
            java.time.LocalTime endTime = startTime.plusHours(1);

            String start = startTime.toString();
            String end = endTime.toString();

            if (isAvailable(spaceName, date, start, end)) {
                suggestedTimes.add(start + " - " + end);
            }

            startTime = startTime.plusHours(1);
        }

        return suggestedTimes;
    }
}
