package pt.uminho.braguia.network;

import java.util.Date;

public class CacheControl {

    private final Long refreshRateMilliseconds;
    private Date lastUpdateDate;

    public CacheControl(Long refreshRateMilliseconds) {
        this.refreshRateMilliseconds = refreshRateMilliseconds;
    }

    public Long getRefreshRateMilliseconds() {
        return refreshRateMilliseconds;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void refresh() {
        lastUpdateDate = new Date();
    }

    public boolean isExpired() {
        if (lastUpdateDate == null) {
            return true;
        }
        Date now = new Date();
        return now.getTime() - lastUpdateDate.getTime() > refreshRateMilliseconds;
    }

    @Override
    public String toString() {
        return "CacheControl{" +
                "refreshRateMilliseconds=" + refreshRateMilliseconds +
                ", lastUpdateDate=" + lastUpdateDate +
                ", expired=" + isExpired() +
                '}';
    }
}
