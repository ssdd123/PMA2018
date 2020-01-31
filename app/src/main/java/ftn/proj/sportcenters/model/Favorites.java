package ftn.proj.sportcenters.model;

import java.util.Date;

public class Favorites {
    private long id;
    private long userId;
    private long sportCenterId;

    public Favorites() {
    }

    public Favorites(long id, long userId, long sportCenterId) {
        this.id = id;
        this.userId = userId;
        this.sportCenterId = sportCenterId;
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
}
