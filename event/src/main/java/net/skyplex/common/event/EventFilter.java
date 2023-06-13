package net.skyplex.common.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a filter for a specific {@link Object} type.
 * <p>
 * The handler represents a "target" of the event. This can be used
 * to create filters for all events of a specific type using information
 * about the target.
 *
 * @param <E> The event type to filter
 * @param <H> The handler type to filter on.
 */
public interface EventFilter<E, H> {

    EventFilter<Object, ?> ALL = from(Object.class, null, null);

    static <E, H> EventFilter<E, H> from(@NotNull Class<E> eventType,
                                         @Nullable Class<H> handlerType,
                                         @Nullable Function<E, H> handlerGetter) {
        return new EventFilter<>() {
            @Override
            public @Nullable H getHandler(@NotNull E event) {
                return handlerGetter != null ? handlerGetter.apply(event) : null;
            }

            @Override
            public @NotNull Class<E> eventType() {
                return eventType;
            }

            @Override
            public @Nullable Class<H> handlerType() {
                return handlerType;
            }
        };
    }

    /**
     * Gets the handler for the given event instance, or null if the event
     * type has no handler.
     *
     * @param event The event instance
     * @return The handler, if it exists for the given event
     */
    @Nullable H getHandler(@NotNull E event);

    @ApiStatus.Internal
    default @Nullable H castHandler(@NotNull Object event) {
        //noinspection unchecked
        return getHandler((E) event);
    }

    /**
     * The event type to filter on.
     *
     * @return The event type.
     */
    @NotNull Class<E> eventType();

    /**
     * The type returned by {@link #getHandler(Object)}.
     *
     * @return the handler type, null if not any
     */
    @Nullable Class<H> handlerType();
}
