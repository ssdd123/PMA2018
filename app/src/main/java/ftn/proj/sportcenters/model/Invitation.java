package ftn.proj.sportcenters.model;

public class Invitation {
    private long id;
    private long reservationId;
    private long userId;
    private int accepted;

    public Invitation() {

    }

    public Invitation(long id, long reservationId, long userId, int accepted) {
        this.id = id;
        this.reservationId = reservationId;
        this.userId = userId;
        this.accepted = accepted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReservationId() {
        return reservationId;
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
}
