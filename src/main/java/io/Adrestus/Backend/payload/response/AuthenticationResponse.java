package io.Adrestus.Backend.payload.response;

import java.util.Objects;

public class AuthenticationResponse {

    private String token;
    private double expiration;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, double expiration) {
        this.token = token;
        this.expiration = expiration;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public double getExpiration() {
        return expiration;
    }

    public void setExpiration(double expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationResponse that = (AuthenticationResponse) o;
        return Double.compare(that.expiration, expiration) == 0 && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expiration);
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "token='" + token + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
