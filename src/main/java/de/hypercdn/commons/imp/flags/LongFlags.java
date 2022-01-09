package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;
import de.hypercdn.commons.api.flags.BitFlags;

/**
 * Abstract implementation of BitFlags based on a long
 */
public abstract class LongFlags implements BitFlags<Long>{

	private long value;

	public LongFlags(long value){
		this.value = value;
	}

	public synchronized Long getValue(){
		return value;
	}

	@Override
	public synchronized void set(BitFlag<Long>... flags){
		for(var flag : flags){
			value |= flag.getRawValue();
		}
	}

	@Override
	public synchronized void unset(BitFlag<Long>... flags){
		for(var flag : flags){
			value &= ~flag.getRawValue();
		}
	}

	@Override
	public synchronized boolean has(BitFlag<Long> flag){
		return (value & flag.getRawValue()) == flag.getRawValue();
	}

}
