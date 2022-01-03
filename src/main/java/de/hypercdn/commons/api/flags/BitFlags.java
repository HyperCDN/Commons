package de.hypercdn.commons.api.flags;

import java.util.List;

public interface BitFlags<T extends Number>{

	void set(BitFlag... flags);

	void unset(BitFlag... flags);

	boolean has(BitFlag flag);

	<E extends BitFlag> List<E> getFlags();

	T getValue();

}
