package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;
import de.hypercdn.commons.api.flags.BitFlags;

/**
 * The type Integer flags.
 */
public abstract class IntegerFlags implements BitFlags<Integer>{

	private int value;

	/**
	 * Instantiates a new Integer flags.
	 *
	 * @param value the value
	 */
	public IntegerFlags(int value){
		this.value = value;
	}

	public synchronized Integer getValue(){
		return value;
	}

	@Override
	public synchronized void set(BitFlag... flags){
		for(var flag : flags){
			value |= 1 << flag.getBitPos();
		}
	}

	@Override
	public synchronized void unset(BitFlag... flags){
		for(var flag : flags){
			value &= ~(1 << flag.getBitPos());
		}
	}

	@Override
	public synchronized boolean has(BitFlag flag){
		return ((value >> flag.getBitPos()) & 1) == 1;
	}

}
