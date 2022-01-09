package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;

/**
 * Generic implementation of a bitflag using a long value
 */
public class LongFlag implements BitFlag<Long>{

	private final long value;

	public LongFlag(long value){
		this.value = value;
	}

	public LongFlag(int... bits){
		var temp = 0;
		for(int bit : bits){
			temp |= 1L << bit;
		}
		value = temp;
	}

	@Override
	public Long getRawValue(){
		return value;
	}

	@Override
	public BitFlag<Long> and(BitFlag<Long> other){
		return new LongFlag(getRawValue() & other.getRawValue());
	}

	@Override
	public BitFlag<Long> or(BitFlag<Long> other){
		return new LongFlag(getRawValue() | other.getRawValue());
	}

}
