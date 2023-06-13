package net.skyplex.common.event;

/**
 * Represents an {@link Object} which can be cancelled.
 * Called using {@link EventNode#callCancellable(Object, Runnable)}.
 */
public interface CancellableEvent {

    /**
     * Gets if the {@link Object} should be cancelled or not.
     *
     * @return true if the event should be cancelled
     */
    boolean isCancelled();

    /**
     * Marks the {@link Object} as cancelled or not.
     *
     * @param cancel true if the event should be cancelled, false otherwise
     */
    void setCancelled(boolean cancel);
}
