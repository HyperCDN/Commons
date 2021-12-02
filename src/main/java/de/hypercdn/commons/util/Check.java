package de.hypercdn.commons.util;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The type Check.
 *
 * @param <T> the type parameter
 */
public class Check<T>{

	private Predicate<T> predicate = t -> true;

	private Check(){}

	/**
	 * That check.
	 *
	 * @return the check
	 */
	public static Check<Object> that(){
		return new Check<>();
	}

	/**
	 * For object boolean.
	 *
	 * @param t the t
	 *
	 * @return the boolean
	 */
	public boolean forObject(Object t){
		return predicate.test((T) t);
	}

	/**
	 * Satisfies check.
	 *
	 * @param predicate the predicate
	 *
	 * @return the check
	 */
	public Check<T> satisfies(Predicate<T> predicate){
		Objects.requireNonNull(predicate);
		this.predicate = this.predicate.and(predicate);
		return this;
	}

	/**
	 * As check.
	 *
	 * @param <O>    the type parameter
	 * @param oClass the o class
	 *
	 * @return the check
	 */
	public <O> Check<O> as(Class<O> oClass){
		return (Check<O>) this;
	}


	/**
	 * Is assignable from check.
	 *
	 * @param <O>   the type parameter
	 * @param clazz the clazz
	 *
	 * @return the check
	 */
	public <O> Check<O> isAssignableFrom(Class<O> clazz){
		return as(clazz).satisfies(r -> clazz.isAssignableFrom(r.getClass()));
	}

	/**
	 * Is blank check.
	 *
	 * @return the check
	 */
	public Check<T> isBlank(){
		return isNonNull().satisfies(o -> o instanceof String string && string.isBlank());
	}

	/**
	 * Is not blank check.
	 *
	 * @return the check
	 */
	public Check<T> isNotBlank(){
		return isNonNull().satisfies(o -> o instanceof String string && !string.isBlank());
	}

	/**
	 * Is null check.
	 *
	 * @return the check
	 */
	public Check<T> isNull(){
		return satisfies(Objects::isNull);
	}

	/**
	 * Is non null check.
	 *
	 * @return the check
	 */
	public Check<T> isNonNull(){
		return satisfies(Objects::nonNull);
	}

	/**
	 * Does equal to check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> doesEqualTo(T t){
		return isNonNull().satisfies(o -> o.equals(t));
	}

	/**
	 * Does not equal to check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> doesNotEqualTo(T t){
		return isNonNull().satisfies(o -> o.equals(t));
	}

	/**
	 * Does equal to any of check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> doesEqualToAnyOf(Set<T> t){
		return satisfies(t::contains);
	}

	/**
	 * Does not equal to any of check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> doesNotEqualToAnyOf(Set<T> t){
		return satisfies(o -> !t.contains(o));
	}

	/**
	 * Is larger than check.
	 *
	 * @param comparable the comparable
	 *
	 * @return the check
	 */
	public Check<T> isLargerThan(Comparable<T> comparable){
		return satisfies(o -> comparable.compareTo(o) < 0);
	}

	/**
	 * Is smaller than check.
	 *
	 * @param comparable the comparable
	 *
	 * @return the check
	 */
	public Check<T> isSmallerThan(Comparable<T> comparable){
		return satisfies(o -> comparable.compareTo(o) > 0);
	}

	/**
	 * Is in range of check.
	 *
	 * @param min the min
	 * @param max the max
	 *
	 * @return the check
	 */
	public Check<T> isInRangeOf(Comparable<T> min, Comparable<T> max){
		return satisfies(o -> min.compareTo(o) <= 0 && max.compareTo(o) >= 0);
	}

	/**
	 * Is out of range of check.
	 *
	 * @param min the min
	 * @param max the max
	 *
	 * @return the check
	 */
	public Check<T> isOutOfRangeOf(Comparable<T> min, Comparable<T> max){
		return satisfies(o -> min.compareTo(o) > 0 && max.compareTo(o) < 0);
	}

	/**
	 * Is larger than check.
	 *
	 * @param number the number
	 *
	 * @return the check
	 */
	public Check<T> isLargerThan(Number number){
		return satisfies(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, number) > 0);
	}

	/**
	 * Is smaller than check.
	 *
	 * @param number the number
	 *
	 * @return the check
	 */
	public Check<T> isSmallerThan(Number number){
		return satisfies(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, number) < 0);
	}

	/**
	 * Is in range of check.
	 *
	 * @param min the min
	 * @param max the max
	 *
	 * @return the check
	 */
	public Check<T> isInRangeOf(Number min, Number max){
		return satisfies(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, min) >= 0 && NumberUtil.compare(oNumber, max) <= 0);
	}

	/**
	 * Is out of range of check.
	 *
	 * @param min the min
	 * @param max the max
	 *
	 * @return the check
	 */
	public Check<T> isOutOfRangeOf(Number min, Number max){
		return satisfies(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, min) < 0 && NumberUtil.compare(oNumber, max) > 0);
	}

}
