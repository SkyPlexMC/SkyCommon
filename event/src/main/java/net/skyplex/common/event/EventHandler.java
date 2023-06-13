package net.skyplex.common.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an element which can have {@link Object} listeners assigned to it.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface EventHandler<T> {

    @NotNull EventNode<T> eventNode();

}
