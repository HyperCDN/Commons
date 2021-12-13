package de.hypercdn.commons.util.check;

import de.hypercdn.commons.util.NumberUtil;

import java.util.*;
import java.util.function.Predicate;

/**
 * The type Check.
 *
 * @param <T> the type parameter
 */
public class Check<T>{

	private final Predicate<T> predicate;
	private final String description;

	private Check<T> parent;
	// x && y || z
	private Check<T> andNext;
	private Check<T> orNext;

	/**
	 * Instantiates a new Check.
	 *
	 * @param predicate   the predicate
	 * @param description the description
	 */
	public Check(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		this.predicate = predicate;
		this.description = description;
	}

	private Check<T> setParent(Check<T> parent){
		this.parent = parent;
		return this;
	}

	private Check<T> getParent(){
		return this.parent;
	}

	private Check<T> setAndNext(Check<T> next){
		this.andNext = next;
		return this;
	}

	private Check<T> setOrNext(Check<T> next){
		this.orNext = next;
		return this;
	}

	/**
	 * With.
	 *
	 * @param t the t
	 */
	public void with(T t){
		var base = this;
		while(base.getParent() != null){
			base = base.getParent();
		}
		var result = base.at(t);
		if(!result.isOk()){
			throw new CheckException(t, result);
		}
	}

	private CheckResult at(T t){
		var hasAndNext = andNext != null;
		var hasOrNext = orNext != null;
		var results = new HashMap<String, CheckResult>(){{
			put("self", predicate.test(t) ? new CheckResult() : new CheckResult(description));
			if(hasAndNext){
				put("and", andNext.at(t));
			}
			if(hasOrNext){
				put("or", orNext.at(t));
			}
		}};
		var self = results.get("self");
		var and = results.get("and");
		var or = results.get("or");

		if(hasAndNext && hasOrNext){ // x && y || z
			if(!(self.isOk() && and.isOk()) && !or.isOk()){
				var description = self.isOk() ? "" : self.getMessage();
				if(!description.isBlank() && !and.isOk()){
					description += " AND ";
				}
				description += and.isOk() ? "" : and.getMessage();
				if(!description.isBlank() && !or.isOk()){
					description += " OR ";
				}
				description += or.isOk() ? "" : or.getMessage();
				if(!description.isBlank()){
					return new CheckResult(description);
				}
			}
			return new CheckResult();
		}else if(hasAndNext){ // x && y
			if(!(self.isOk() && and.isOk())){
				var description = self.isOk() ? "" : self.getMessage();
				if(!description.isBlank() && !and.isOk()){
					description += " AND ";
				}
				description += and.isOk() ? "" : and.getMessage();
				if(!description.isBlank()){
					return new CheckResult(description);
				}
			}
			return new CheckResult();
		}else if(hasOrNext){ // x || z
			if(!(self.isOk() || or.isOk())){
				var description = self.isOk() ? "" : self.getMessage();
				if(!description.isBlank() && !or.isOk()){
					description += " OR ";
				}
				description += or.isOk() ? "" : or.getMessage();
				if(!description.isBlank()){
					return new CheckResult(description);
				}
			}
			return new CheckResult();
		}else {
			return self;
		}
	}


	/**
	 * That check.
	 *
	 * @return the check
	 */
	public static Check<Object> that(){
		return new Check<>((t) -> true, "");
	}

	/**
	 * And check.
	 *
	 * @param predicate   the predicate
	 * @param description the description
	 *
	 * @return the check
	 */
	public Check<T> and(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		var and = new Check<>(predicate, description);
		this.setAndNext(and);
		return and.setParent(this);
	}

	/**
	 * Or check.
	 *
	 * @return the check
	 */
	public Check<T> or(){
		return or((i) -> true, "");
	}

	/**
	 * Or check.
	 *
	 * @param predicate   the predicate
	 * @param description the description
	 *
	 * @return the check
	 */
	public Check<T> or(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		var or = new Check<>(predicate, description);
		this.setOrNext(or);
		return or.setParent(this);
	}

	//https://haste.hypercdn.de/yayohuxele.typescript

	/**
	 * As check.
	 *
	 * @param <O>    the type parameter
	 * @param oClass the o class
	 *
	 * @return the check
	 */
	public <O> Check<O> as(Class<O> oClass){
		Objects.requireNonNull(oClass);
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
		Objects.requireNonNull(clazz);
		return as(clazz).and(r -> clazz.isAssignableFrom(r.getClass()), "is assignable from "+clazz.getSimpleName());
	}

	/**
	 * Is blank check.
	 *
	 * @return the check
	 */
	public Check<T> isBlank(){
		return and(o -> o instanceof String string && string.isBlank(), "is blank");
	}

	/**
	 * Is not blank check.
	 *
	 * @return the check
	 */
	public Check<T> isNotBlank(){
		return and(o -> o instanceof String string && !string.isBlank(), "is not blank");
	}

	/**
	 * Is null check.
	 *
	 * @return the check
	 */
	public Check<T> isNull(){
		return and(Objects::isNull, "is null");
	}

	/**
	 * Is non null check.
	 *
	 * @return the check
	 */
	public Check<T> isNonNull(){
		return and(Objects::nonNull, "is non null");
	}

	/**
	 * Is equal to check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> isEqualTo(T t){
		Objects.requireNonNull(t);
		return and(t::equals, "is equal to "+t);
	}

	/**
	 * Is not equal to check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> isNotEqualTo(T t){
		Objects.requireNonNull(t);
		return and(o -> !t.equals(o), "is not equal to "+t);
	}

	/**
	 * Does equal to any of check.
	 *
	 * @param ts the ts
	 *
	 * @return the check
	 */
	public Check<T> doesEqualToAnyOf(T... ts){
		Objects.requireNonNull(ts);
		return doesEqualToAnyOf(Set.of(ts));
	}

	/**
	 * Does equal to any of check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> doesEqualToAnyOf(Set<T> t){
		Objects.requireNonNull(t);
		return and(t::contains, "equals any of "+ Arrays.toString(t.toArray()));
	}

	/**
	 * Does not equal to any of check.
	 *
	 * @param ts the ts
	 *
	 * @return the check
	 */
	public Check<T> doesNotEqualToAnyOf(T... ts){
		Objects.requireNonNull(ts);
		return doesNotEqualToAnyOf(Set.of(ts));
	}

	/**
	 * Does not equal to any of check.
	 *
	 * @param t the t
	 *
	 * @return the check
	 */
	public Check<T> doesNotEqualToAnyOf(Set<T> t){
		Objects.requireNonNull(t);
		return and(o -> !t.contains(o), "does not equal any of "+ Arrays.toString(t.toArray()));
	}

	/**
	 * Is larger than check.
	 *
	 * @param comparable the comparable
	 *
	 * @return the check
	 */
	public Check<T> isLargerThan(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) < 0, "is larger than "+comparable);
	}

	/**
	 * Is smaller than check.
	 *
	 * @param comparable the comparable
	 *
	 * @return the check
	 */
	public Check<T> isSmallerThan(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) > 0, "is smaller than"+comparable);
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
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> min.compareTo(o) <= 0 && max.compareTo(o) >= 0, "is in range of "+min+" to "+max);
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
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> min.compareTo(o) > 0 && max.compareTo(o) < 0, "is not in range of "+min+" to "+max);
	}

	/**
	 * Is larger than check.
	 *
	 * @param number the number
	 *
	 * @return the check
	 */
	public Check<T> isLargerThan(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, number) > 0, "is larger than "+number);
	}

	/**
	 * Is smaller than check.
	 *
	 * @param number the number
	 *
	 * @return the check
	 */
	public Check<T> isSmallerThan(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, number) < 0, "is smaller than "+number);
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
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, min) >= 0 && NumberUtil.compare(oNumber, max) <= 0, "is in range of "+min+" to "+max);
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
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, min) < 0 && NumberUtil.compare(oNumber, max) > 0, "is not in range of "+min+" to "+max);
	}

}

