package io.Adrestus.Backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public final class AccountStateObject implements Serializable {
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "zoneid", nullable = false)
    private int zoneId;


    public AccountStateObject() {
    }

    public AccountStateObject(String address, int zoneId) {
        this.address = address;
        this.zoneId = zoneId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountStateObject that = (AccountStateObject) o;
        return zoneId == that.zoneId && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, zoneId);
    }

    @Override
    public String toString() {
        return "AccountStateObject{" +
                "address='" + address + '\'' +
                ", zoneId=" + zoneId +
                '}';
    }
}
