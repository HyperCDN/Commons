package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;
import de.hypercdn.commons.api.flags.BitFlags;

/**
 * The type Integer flags.
 */
public abstract class IntegerFlags implements BitFlags{

	private int value;

	/**
	 * Instantiates a new Integer flags.
	 *
	 * @param value the value
	 */
	public IntegerFlags(int value){
		this.value = value;
	}

	/**
	 * Get value long.
	 *
	 * @return the long
	 */
	public long getValue(){
		return value;
	}

	@Override
	public void set(BitFlag... flags){
		for(var flag : flags){
			value |= 1L << flag.getBitPos();
		}
	}

	@Override
	public void unset(BitFlag... flags){
		for(var flag : flags){
			value &= ~(1L << flag.getBitPos());
		}
	}

	@Override
	public boolean has(BitFlag flag){
		return ((value >> flag.getBitPos()) & 1) == 1;
	}

}
