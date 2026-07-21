package controller;

public class ReservationController {
    private final java.util.List<model.ReservationRecord> reservations =
            new java.util.ArrayList<>();
    public ReservationController() {
    }
    public void addReservation(model.ReservationRecord reservation) {
        reservations.add(reservation);
    }
    public java.util.List<model.ReservationRecord> getReservations() {
        return reservations;
    }
    public java.util.List<model.ReservationRecord> getReservationsForUser(String userName) {
        java.util.List<model.ReservationRecord> userReservations =
                new java.util.ArrayList<>();

        for (model.ReservationRecord reservation : reservations) {
            if (reservation.matchesUser(userName)) {
                userReservations.add(reservation);
            }
        }

        return userReservations;
    }
    public boolean cancelReservation(model.ReservationRecord reservation) {
        return reservations.remove(reservation);
    }
    public boolean modifyReservation(model.ReservationRecord oldReservation,
                                     model.ReservationRecord newReservation) {
        int index = reservations.indexOf(oldReservation);

        if (index == -1) {
            return false;
        }

        reservations.set(index, newReservation);
        return true;
    }
    public boolean isAvailable(String spaceName, String date, String startTime, String endTime) {
        for (model.ReservationRecord reservation : reservations) {
            boolean sameSpace = reservation.getSpaceName().equalsIgnoreCase(spaceName);
            boolean sameDate = reservation.getDate().equals(date);

            if (sameSpace && sameDate) {
                boolean overlaps = startTime.compareTo(reservation.getEndTime()) < 0
                        && endTime.compareTo(reservation.getStartTime()) > 0;

                if (overlaps) {
                    return false;
                }
            }
        }

        return true;
    }
}
