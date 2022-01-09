package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;
import de.hypercdn.commons.api.flags.BitFlags;

/**
 * Abstract implementation of BitFlags based on an integer
 */
public abstract class IntegerFlags implements BitFlags<Integer>{

	private int value;

	public IntegerFlags(int value){
		this.value = value;
	}

	public synchronized Integer getValue(){
		return value;
	}

	@Override
	public synchronized void set(BitFlag<Integer>... flags){
		for(var flag : flags){
			value |= flag.getRawValue();
		}
	}

	@Override
	public synchronized void unset(BitFlag<Integer>... flags){
		for(var flag : flags){
			value &= ~flag.getRawValue();
		}
	}

	@Override
	public synchronized boolean has(BitFlag<Integer> flag){
		return (value & flag.getRawValue()) == flag.getRawValue();
	}

}
