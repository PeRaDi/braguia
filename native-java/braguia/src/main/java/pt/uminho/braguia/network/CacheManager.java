package pt.uminho.braguia.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CacheManager {

    private static final Long DEFAULT_REFRESH_RATE_MILLISECONDS = TimeUnit.SECONDS.toMillis(2);

    private Map<Class, CacheControl> controlMap;

    public CacheManager() {
        controlMap = new ConcurrentHashMap<>();
    }

    public CacheControl control(Class c) {
        if (controlMap.containsKey(c)) {
            return controlMap.get(c);
        }
        controlMap.put(c, new CacheControl(DEFAULT_REFRESH_RATE_MILLISECONDS, true));
        return controlMap.get(c);
    }

}
