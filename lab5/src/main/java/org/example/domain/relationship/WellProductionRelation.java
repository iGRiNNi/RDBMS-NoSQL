package org.example.domain.relationship;

import java.time.LocalDateTime;

public class WellProductionRelation {

    private final LocalDateTime measuredAt;
    private final String method;
    private final Boolean verified;

    public WellProductionRelation(LocalDateTime measuredAt,
                                  String method,
                                  Boolean verified) {
        this.measuredAt = measuredAt;
        this.method = method;
        this.verified = verified;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public String getMethod() {
        return method;
    }

    public Boolean getVerified() {
        return verified;
    }

    @Override
    public String toString() {
        return "WellProductionRelation{" +
                "measuredAt=" + measuredAt +
                ", method='" + method + '\'' +
                ", verified=" + verified +
                '}';
    }
}
