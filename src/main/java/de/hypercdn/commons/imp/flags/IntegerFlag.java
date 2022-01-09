package de.hypercdn.commons.imp.flags;

import de.hypercdn.commons.api.flags.BitFlag;

/**
 * Generic implementation of a bitflag using an int value
 */
public class IntegerFlag implements BitFlag<Integer>{

	private final int value;

	public IntegerFlag(int value){
		this.value = value;
	}

	public IntegerFlag(int... bits){
		var temp = 0;
		for(int bit : bits){
			temp |= 1 << bit;
		}
		value = temp;
	}

	@Override
	public Integer getRawValue(){
		return value;
	}

	@Override
	public BitFlag<Integer> and(BitFlag<Integer> other){
		return new IntegerFlag(getRawValue() & other.getRawValue());
	}

	@Override
	public BitFlag<Integer> or(BitFlag<Integer> other){
		return new IntegerFlag(getRawValue() | other.getRawValue());
	}

}
