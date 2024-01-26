package io.Adrestus.Backend.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.Adrestus.core.Transaction;

import java.util.List;
import java.util.Objects;

public class ResponseDao {
    @JsonProperty(value = "from")
    private List<Transaction> from;
    @JsonProperty(value = "to")
    private List<Transaction> to;

    @JsonCreator
    public ResponseDao(List<Transaction> from, List<Transaction> to) {
        this.from = from;
        this.to = to;
    }


    public List<Transaction> getFrom() {
        return from;
    }

    public void setFrom(List<Transaction> from) {
        this.from = from;
    }

    public List<Transaction> getTo() {
        return to;
    }

    public void setTo(List<Transaction> to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseDao that = (ResponseDao) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "ResponseDao{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
