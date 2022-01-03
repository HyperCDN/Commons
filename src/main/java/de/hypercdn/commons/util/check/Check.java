package de.hypercdn.commons.util.check;

import de.hypercdn.commons.util.NumberUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

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

	public static Check<Object> that(){
		return new Check<>((t) -> true, "");
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

	public Check<T> and(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		var and = new Check<>(predicate, description);
		this.setAndNext(and);
		return and.setParent(this);
	}

	public Check<T> or(){
		return or((i) -> true, "");
	}

	public Check<T> or(Predicate<T> predicate, String description){
		Objects.requireNonNull(predicate);
		Objects.requireNonNull(description);
		var or = new Check<>(predicate, description);
		this.setOrNext(or);
		return or.setParent(this);
	}

	//https://haste.hypercdn.de/yayohuxele.typescript

	public <O> Check<O> as(Class<O> oClass){
		Objects.requireNonNull(oClass);
		return (Check<O>) this;
	}

	public <O> Check<O> isAssignableFrom(Class<O> clazz){
		Objects.requireNonNull(clazz);
		return as(clazz).and(r -> clazz.isAssignableFrom(r.getClass()), "is assignable from " + clazz.getSimpleName());
	}

	public Check<T> isBlank(){
		return and(o -> o instanceof String string && string.isBlank(), "is blank");
	}

	public Check<T> isNotBlank(){
		return and(o -> o instanceof String string && !string.isBlank(), "is not blank");
	}

	public Check<T> isNull(){
		return and(Objects::isNull, "is null");
	}

	public Check<T> isNonNull(){
		return and(Objects::nonNull, "is non null");
	}

	public Check<T> isEqualTo(T t){
		Objects.requireNonNull(t);
		return and(t::equals, "is equal to " + t);
	}

	public Check<T> isNotEqualTo(T t){
		Objects.requireNonNull(t);
		return and(o -> !t.equals(o), "is not equal to " + t);
	}

	public Check<T> doesEqualToAnyOf(T... ts){
		Objects.requireNonNull(ts);
		return doesEqualToAnyOf(Set.of(ts));
	}

	public Check<T> doesEqualToAnyOf(Set<T> t){
		Objects.requireNonNull(t);
		return and(t::contains, "equals any of " + Arrays.toString(t.toArray()));
	}

	public Check<T> doesNotEqualToAnyOf(T... ts){
		Objects.requireNonNull(ts);
		return doesNotEqualToAnyOf(Set.of(ts));
	}

	public Check<T> doesNotEqualToAnyOf(Set<T> t){
		Objects.requireNonNull(t);
		return and(o -> !t.contains(o), "does not equal any of " + Arrays.toString(t.toArray()));
	}

	public Check<T> isLargerThan(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) < 0, "is larger than " + comparable);
	}

	public Check<T> isSmallerThan(Comparable<T> comparable){
		Objects.requireNonNull(comparable);
		return and(o -> comparable.compareTo(o) > 0, "is smaller than" + comparable);
	}

	public Check<T> isInRangeOf(Comparable<T> min, Comparable<T> max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> min.compareTo(o) <= 0 && max.compareTo(o) >= 0, "is in range of " + min + " to " + max);
	}

	public Check<T> isOutOfRangeOf(Comparable<T> min, Comparable<T> max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> min.compareTo(o) > 0 && max.compareTo(o) < 0, "is not in range of " + min + " to " + max);
	}

	public Check<T> isLargerThan(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, number) > 0, "is larger than " + number);
	}

	public Check<T> isSmallerThan(Number number){
		Objects.requireNonNull(number);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, number) < 0, "is smaller than " + number);
	}

	public Check<T> isInRangeOf(Number min, Number max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, min) >= 0 && NumberUtil.compare(oNumber, max) <= 0, "is in range of " + min + " to " + max);
	}

	public Check<T> isOutOfRangeOf(Number min, Number max){
		Objects.requireNonNull(min);
		Objects.requireNonNull(max);
		return and(o -> o instanceof Number oNumber && NumberUtil.compare(oNumber, min) < 0 && NumberUtil.compare(oNumber, max) > 0, "is not in range of " + min + " to " + max);
	}

}

