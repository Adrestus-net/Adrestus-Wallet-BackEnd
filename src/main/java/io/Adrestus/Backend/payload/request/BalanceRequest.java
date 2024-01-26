package io.Adrestus.Backend.payload.request;

import java.util.Objects;

public class BalanceRequest {
    private String address;
    private int zone;

    public BalanceRequest() {
    }

    public BalanceRequest(String address, int zone) {
        this.address = address;
        this.zone = zone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceRequest that = (BalanceRequest) o;
        return zone == that.zone && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, zone);
    }
}
