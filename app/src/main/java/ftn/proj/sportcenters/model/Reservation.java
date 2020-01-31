package ftn.proj.sportcenters.model;

import java.io.Serializable;

public class Reservation implements Serializable {

    private long id;
    private long userId;
    private long sportCenterId;
    private String sportName;
    private double price;
    private String date;
    private String period;
    private long eventId;

    public Reservation() {
    }

    public Reservation(long id, long userId, long sportCenterId, String sportName, double price, String date, String period,long eventId ) {
        this.id = id;
        this.userId = userId;
        this.sportCenterId = sportCenterId;
        this.sportName = sportName;
        this.price = price;
        this.date = date;
        this.period = period;
        this.eventId = eventId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSportCenterId() {
        return sportCenterId;
    }

    public void setSportCenterId(long sportCenterId) {
        this.sportCenterId = sportCenterId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
