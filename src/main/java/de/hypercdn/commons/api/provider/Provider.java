package de.hypercdn.commons.api.provider;

public interface Provider<I, O> {

    O getStored(I i);

    O getAndStore(I i);

    void removeStored(I i);

    void clear();

    int size();

}
