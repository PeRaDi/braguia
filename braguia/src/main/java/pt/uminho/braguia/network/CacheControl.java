package pt.uminho.braguia.network;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CacheControl {

    private final Long refreshRateMilliseconds;
    private Date lastUpdateDate;
    private final boolean expirable;

    public CacheControl(Long refreshRateMilliseconds, boolean expirable) {
        this.refreshRateMilliseconds = refreshRateMilliseconds;
        this.expirable = refreshRateMilliseconds <= TimeUnit.SECONDS.toMillis(0) ? false : expirable;
    }

    public void refresh() {
        lastUpdateDate = new Date();
    }

    public boolean isExpired() {
        if (lastUpdateDate == null) {
            return true;
        }
        if (!expirable) {
            return false;
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
