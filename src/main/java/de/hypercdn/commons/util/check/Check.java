package de.hypercdn.commons.util.check;

import de.hypercdn.commons.util.CompareUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Used to create human-readable predicate exceptions
 *
 * @param <T> of check
 */
public class Check<T>{

	private final Predicate<T> predicate;
	private final String description;

	private Check<T> parent;
	// x && y || z
	private Check<T> andNext;
	private Check<T> orNext;

	private Check(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		this.predicate = predicate;
		this.description = description;
	}

	/**
	 * Returns an instance of a check
	 *
	 * @return new check
	 */
	public static Check<Object> that(){
		return new Check<>((t) -> true, "");
	}

	/**
	 * Returns an instance of a check
	 *
	 * @param tClass initial class data
	 * @param <T>    type
	 *
	 * @return new check
	 */
	public static <T> Check<T> that(Class<T> tClass){
		return new Check<>((t) -> true, "").as(tClass);
	}

	@Override
	public String toString(){
		return asString(false);
	}

	private Check<T> getBase(){
		var base = this;
		while(base.getParent() != null){
			base = base.getParent();
		}
		return base;
	}

	private Check<T> getParent(){
		return this.parent;
	}

	private Check<T> setParent(Check<T> parent){
		this.parent = parent;
		return this;
	}

	private void setAndNext(Check<T> next){
		this.andNext = next;
	}

	private void setOrNext(Check<T> next){
		this.orNext = next;
	}

	/**
	 * Returns the predicate as string
	 *
	 * @param formatted whether the output needs to be formatted
	 *
	 * @return predicate as string
	 */
	public String asString(boolean formatted){
		return getBase().asString(formatted, 0);
	}

	private String asString(boolean formatted, int layer){
		var string = new StringBuilder();
		if(!description.isBlank()){
			string.append(description);
		}
		if(andNext != null){
			var andNextString = andNext.asString(formatted, layer);
			if(!string.isEmpty() && !andNextString.isBlank()){
				string.append(formatted ? "\n" : " ").append("AND ");
			}
			string.append(andNextString);
		}
		if(orNext != null){
			var orNextString = orNext.asString(formatted, layer + 1);
			if(!string.isEmpty() && !orNextString.isBlank()){
				string.append(formatted ? "\n\t".repeat(layer + 1) : " ").append("OR ");
			}
			string.append(orNextString);
		}
		return string.toString();
	}

	/**
	 * Perform the checks on the provided object
	 *
	 * @param t object to check
	 */
	public void with(T t){
		var result = getBase().at(t);
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

		var description = new StringBuilder();
		if(self != null && !self.isOk()){
			description.append(self.getMessage());
		}
		if(and != null && !and.isOk()){
			if(!description.isEmpty()){
				description.append(" AND ");
			}
			description.append(and.getMessage());
		}
		if(or != null && !or.isOk()){
			if(!description.isEmpty()){
				description.append(" OR ");
			}
			description.append(or.getMessage());
		}
		if((self == null || self.isOk()) && (and == null || and.isOk()) || (or != null && or.isOk())){
			return new CheckResult();
		}
		return new CheckResult(description.toString());
	}

	/**
	 * AND Add a predicate with a description to the check
	 *
	 * @param predicate   to add
	 * @param description of the predicate
	 *
	 * @return extended check
	 */
	public Check<T> and(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		var and = new Check<>(predicate, description);
		this.setAndNext(and);
		return and.setParent(this);
	}

	/**
	 * Add an or predicate to the check
	 *
	 * @return extended check
	 */
	public Check<T> or(){
		return or((i) -> true, "");
	}

	/**
	 * OR Add a predicate with a description to the check
	 *
	 * @param predicate   to add
	 * @param description of the predicate
	 *
	 * @return extended check
	 */
	public Check<T> or(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		var or = new Check<>(predicate, description);
		this.setOrNext(or);
		return or.setParent(this);
	}

	/**
	 * Add an object cast to the check
	 *
	 * @param oClass to cast to
	 * @param <O>
	 *
	 * @return extended check
	 */
	public <O> Check<O> as(Class<O> oClass){
		Objects.requireNonNull(oClass);
		return (Check<O>) this;
	}

	/**
	 * Add an assignable check for the provided class
	 *
	 * @param clazz to check
	 * @param <O>
	 *
	 * @return extended check
	 */
	public <O> Check<O> isAssignableFrom(Class<O> clazz){
		Objects.requireNonNull(clazz);
		return as(clazz).and(r -> clazz.isAssignableFrom(r.getClass()), "is assignable from " + clazz.getSimpleName());
	}

	/**
	 * Add a check if the object is blank
	 *
	 * @return extended check
	 */
	public Check<T> isBlank(){
		return and(o -> o instanceof String string && string.isBlank(), "is blank");
	}

	/**
	 * Add a check if the object is not blank
	 *
	 * @return extended check
	 */
	public Check<T> isNotBlank(){
		return and(o -> o instanceof String string && !string.isBlank(), "is not blank");
	}

	/**
	 * Add a check if the object is null
	 *
	 * @return extended check
	 */
	public Check<T> isNull(){
		return and(Objects::isNull, "is null");
	}

	/**
	 * Add a check if the object is not null
	 *
	 * @return extended check
	 */
	public Check<T> isNonNull(){
		return and(Objects::nonNull, "is non null");
	}

	/**
	 * Add a check if the object is equal to the specified value
	 *
	 * @param t to equal
	 *
	 * @return extended check
	 */
	public Check<T> isEqualTo(T t){
		Objects.requireNonNull(t);
		return and(t::equals, "is equal to " + t);
	}

	/**
	 * Add a check if the object is not equal to the specified value
	 *
	 * @param t to not equal
	 *
	 * @return extended check
	 */
	public Check<T> isNotEqualTo(T t){
		Objects.requireNonNull(t);
		return and(o -> !t.equals(o), "is not equal to " + t);
	}

	/**
	 * Add a check if the object is equal to any of the specified values
	 *
	 * @param ts to equal any of
	 *
	 * @return extended check
	 */
	public Check<T> doesEqualToAnyOf(T... ts){
		Objects.requireNonNull(ts);
		return doesEqualToAnyOf(Set.of(ts));
	}

	/**
	 * Add a check if the object is equal to any of the specified values
	 *
	 * @param t to equal any of
	 *
	 * @return extended check
	 */
	public Check<T> doesEqualToAnyOf(Set<T> t){
		Objects.requireNonNull(t);
		return and(t::contains, "equals any of " + Arrays.toString(t.toArray()));
	}

	/**
	 * Add a check if the object is not equal to any of the specified values
	 *
	 * @param ts to not equal any of
	 *
	 * @return extended check
	 */
	public Check<T> doesNotEqualToAnyOf(T... ts){
		Objects.requireNonNull(ts);
		return doesNotEqualToAnyOf(Set.of(ts));
	}

	/**
	 * Add a check if the object is not equal to any of the specified values
	 *
	 * @param t to not equal any of
	 *
	 * @return extended check
	 */
	public Check<T> doesNotEqualToAnyOf(Set<T> t){
		Objects.requireNonNull(t);
		return and(o -> !t.contains(o), "does not equal any of " + Arrays.toString(t.toArray()));
	}

	/**
	 * Add a check if the object is larger than the specified value
	 *
	 * @param comparable value
	 *
	 * @return extended check
	 */
	public Check<T> isLargerThan(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) < 0, "is larger than " + comparable);
	}

	/**
	 * Add a check if the object is larger than or equal to the specified value
	 *
	 * @param comparable value
	 *
	 * @return extended check
	 */
	public Check<T> isLargerThanOrEqualTo(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) <= 0, "is larger than or equal to " + comparable);
	}

	/**
	 * Add a check if the object is smaller than the specified value
	 *
	 * @param comparable value
	 *
	 * @return extended check
	 */
	public Check<T> isSmallerThan(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) > 0, "is smaller than " + comparable);
	}

	/**
	 * Add a check if the object is smaller than or equal to the specified value
	 *
	 * @param comparable value
	 *
	 * @return extended check
	 */
	public Check<T> isSmallerThanOrEqualTo(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) >= 0, "is smaller than or equal to " + comparable);
	}

	/**
	 * Add a check if the object is within the specified range
	 *
	 * @param min value
	 * @param max value
	 *
	 * @return extended check
	 */
	public Check<T> isInRangeOf(Comparable<T> min, Comparable<T> max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> min.compareTo(o) <= 0 && max.compareTo(o) >= 0, "is in range of " + min + " to " + max);
	}

	/**
	 * Add a check if the object is outside the specified range
	 *
	 * @param min value
	 * @param max value
	 *
	 * @return extended check
	 */
	public Check<T> isOutOfRangeOf(Comparable<T> min, Comparable<T> max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> min.compareTo(o) > 0 && max.compareTo(o) < 0, "is not in range of " + min + " to " + max);
	}

	/**
	 * Add a check if the object is larger than specified
	 *
	 * @param number to check against
	 *
	 * @return extended check
	 */
	public Check<T> isLargerThan(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && CompareUtil.compare(oNumber, number) > 0, "is larger than " + number);
	}

	/**
	 * Add a check if the object is larger than or equal as specified
	 *
	 * @param number to check against
	 *
	 * @return extended check
	 */
	public Check<T> isLargerThanOrEqualTo(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && CompareUtil.compare(oNumber, number) >= 0, "is larger than or equal to " + number);
	}

	/**
	 * Add a check if the object is smaller than specified
	 *
	 * @param number to check against
	 *
	 * @return extended check
	 */
	public Check<T> isSmallerThan(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && CompareUtil.compare(oNumber, number) < 0, "is smaller than " + number);
	}

	/**
	 * Add a check if the object is smaller than or equal as specified
	 *
	 * @param number to check against
	 *
	 * @return extended check
	 */
	public Check<T> isSmallerThanOrEqualTo(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && CompareUtil.compare(oNumber, number) <= 0, "is smaller than or equal to " + number);
	}

	/**
	 * Add a check if the object is within the specified range
	 *
	 * @param min value
	 * @param max value
	 *
	 * @return extended check
	 */
	public Check<T> isInRangeOf(Number min, Number max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> o instanceof Number oNumber && CompareUtil.compare(oNumber, min) >= 0 && CompareUtil.compare(oNumber, max) <= 0, "is in range of " + min + " to " + max);
	}

	/**
	 * Add a check if the object is outside the specified range
	 *
	 * @param min value
	 * @param max value
	 *
	 * @return extended check
	 */
	public Check<T> isOutOfRangeOf(Number min, Number max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> o instanceof Number oNumber && CompareUtil.compare(oNumber, min) < 0 && CompareUtil.compare(oNumber, max) > 0, "is not in range of " + min + " to " + max);
	}

	/**
	 * Add a check for an inner property
	 *
	 * @param map         to access property
	 * @param check       to perform
	 * @param description to display on check failute
	 * @param <TI>        type
	 *
	 * @return extended check
	 */
	public <TI> Check<T> innerCheck(Function<T, TI> map, Check<TI> check, String description){
		return and(o -> {
			try{
				check.with(map.apply(o));
			}
			catch(CheckException e){
				return false;
			}
			return true;
		}, "inner check does not match: " + description);
	}

}

