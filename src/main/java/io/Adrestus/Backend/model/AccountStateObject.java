package io.Adrestus.Backend.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public final class AccountStateObject implements Serializable {
    @Column(name = "accountId", nullable = false)
    private Long accountId;
    @Column(name = "zoneid", nullable = false)
    private int zoneId;


    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountStateObject that = (AccountStateObject) o;
        return zoneId == that.zoneId && Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, zoneId);
    }

    @Override
    public String toString() {
        return "AccountStateObject{" +
                "accountId=" + accountId +
                ", zoneId=" + zoneId +
                '}';
    }
}
