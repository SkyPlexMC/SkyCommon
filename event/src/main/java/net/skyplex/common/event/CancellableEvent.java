package net.skyplex.common.event;

/**
 * Represents an {@link Event} which can be cancelled.
 * Called using {@link EventNode#callCancellable(Event, Runnable)}.
 */
public interface CancellableEvent extends Event {

    /**
     * Gets if the {@link Event} should be cancelled or not.
     *
     * @return true if the event should be cancelled
     */
    boolean isCancelled();

    /**
     * Marks the {@link Event} as cancelled or not.
     *
     * @param cancel true if the event should be cancelled, false otherwise
     */
    void setCancelled(boolean cancel);
}
