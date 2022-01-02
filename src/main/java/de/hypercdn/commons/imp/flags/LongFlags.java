package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;
import de.hypercdn.commons.api.flags.BitFlags;

/**
 * The type Long flags.
 */
public abstract class LongFlags implements BitFlags<Long>{

	private long value;

	/**
	 * Instantiates a new Long flags.
	 *
	 * @param value the value
	 */
	public LongFlags(long value){
		this.value = value;
	}

	public synchronized Long getValue(){
		return value;
	}

	@Override
	public synchronized void set(BitFlag... flags){
		for(var flag : flags){
			value |= 1L << flag.getBitPos();
		}
	}

	@Override
	public synchronized void unset(BitFlag... flags){
		for(var flag : flags){
			value &= ~(1L << flag.getBitPos());
		}
	}

	@Override
	public synchronized boolean has(BitFlag flag){
		return ((value >> flag.getBitPos()) & 1L) == 1;
	}

}
