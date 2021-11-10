package de.hypercdn.commons.api.properties.misc;

public interface Suspendable {

    boolean isSuspended();

    boolean suspend();

    boolean resume();

    default boolean suspend(boolean enable){
        return enable ? suspend() : resume();
    }

}
