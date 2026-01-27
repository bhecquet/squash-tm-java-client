package io.github.bhecquet.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class EntityCache<E extends Entity> {

    private static int defaultRefreshInterval = 300;
    private static boolean enabled = false;
    private static final Object lock = new Object();
    private final int refreshInterval;
    private Instant lastRefresh = Instant.EPOCH;
    private final Map<Integer, E> data = new ConcurrentHashMap<>();

    public EntityCache() {
        this(defaultRefreshInterval);
    }

    /**
     * Creates a cache that will refresh at most every 'refreshInterval' seconds
     *
     * @param refreshInterval interval in seconds
     */
    public EntityCache(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public static void setEnabled(boolean enabled) {
        EntityCache.enabled = enabled;
    }

    public void add(E entity) {
        synchronized (lock) {
            if (enabled) {
                data.put(entity.getId(), entity);
            }
        }
    }

    public E get(int id) {
        synchronized (lock) {
            return data.getOrDefault(id, null);
        }
    }


    private boolean isExpired() {
        return lastRefresh.plusSeconds(refreshInterval).isBefore(Instant.now()) || data.isEmpty();
    }


    public <I extends Entity> List<E> getAll(
            Function<? super I, ? extends List<? extends E>> refresher,
            I input
    ) {
        Objects.requireNonNull(refresher, "refresher");

        synchronized (lock) {
            if (!isExpired()) {
                return new ArrayList<>(data.values());
            }

            // refresh data
            List<? extends E> newData = Objects.requireNonNull(
                    refresher.apply(input),
                    "Le refresher a retourné null"
            );

            if (enabled) {
                data.clear();
                newData.forEach(e -> data.put(e.getId(), e));
                lastRefresh = Instant.now();
                return new ArrayList<>(data.values());
            } else {
                return new ArrayList<>(newData);
            }

        }
    }

    public static void setDefaultRefreshInterval(int refreshInterval) {
        defaultRefreshInterval = refreshInterval;
    }
}
