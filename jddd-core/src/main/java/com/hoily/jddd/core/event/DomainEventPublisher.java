package com.hoily.jddd.core.event;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@link DomainEvent} Publisher
 *
 * @author vyckey
 */
public class DomainEventPublisher implements Closeable {
    private static final ThreadLocal<List<DomainEventSubscriber>> SUBSCRIBERS = new ThreadLocal<>();

    protected DomainEventPublisher() {
    }

    public static DomainEventPublisher instance() {
        return new DomainEventPublisher();
    }

    public <E extends DomainEvent> void publish(E domainEvent) {
        List<DomainEventSubscriber> subscribers = DomainEventPublisher.SUBSCRIBERS.get();
        if (subscribers == null) {
            return;
        }
        List<DomainEventSubscriber> cloneSubscribers = new CopyOnWriteArrayList<>(subscribers);
        for (DomainEventSubscriber subscriber : cloneSubscribers) {
            if (subscriber.subscribedToEvent(domainEvent)) {
                subscriber.handleEvent(domainEvent);
            }
        }
    }

    public void subscribe(DomainEventSubscriber subscriber) {
        List<DomainEventSubscriber> subscribers = DomainEventPublisher.SUBSCRIBERS.get();
        if (subscribers == null) {
            subscribers = new ArrayList<>();
            DomainEventPublisher.SUBSCRIBERS.set(subscribers);
        }
        subscribers.add(subscriber);
    }

    public void resetSubscribers() {
        DomainEventPublisher.SUBSCRIBERS.remove();
    }

    @Override
    public void close() {
        SUBSCRIBERS.remove();
    }
}
