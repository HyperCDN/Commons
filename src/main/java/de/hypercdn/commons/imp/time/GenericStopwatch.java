package de.hypercdn.commons.imp.time;

import de.hypercdn.commons.api.time.Stopwatch;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GenericStopwatch implements Stopwatch{

	private final AtomicBoolean isActive = new AtomicBoolean(false);
	private final HashMap<Precision, Duration> calibration = new HashMap<>(){{
		for(var pre : Precision.values()){
			put(pre, Duration.ZERO);
		}
	}};
	private Duration duration = Duration.ZERO;
	private Instant start;
	private Instant stop;
	private Precision precision;

	@Override
	public synchronized void start(Precision precision){
		if(!isActive.compareAndSet(false, true)){
			reset();
		}
		// flush
		if(start != null && stop != null){
			start = null;
			stop = null;
		}
		// define start
		this.precision = precision;
		switch(precision){
			case DEFAULT -> {
				start = Instant.ofEpochMilli(System.currentTimeMillis());
			}
			case HIGH -> {
				var startTimeNano = System.nanoTime();
				var milFromNano = startTimeNano / 1_000_000;
				start = Instant.ofEpochMilli(milFromNano).plusNanos(startTimeNano - (milFromNano * 1_000_000));
			}
		}

	}

	@Override
	public synchronized void stop(){
		if(!isActive.compareAndSet(true, false)){
			return;
		}
		// define end
		switch(precision){
			case DEFAULT -> {
				stop = Instant.ofEpochMilli(System.currentTimeMillis());
			}
			case HIGH -> {
				var stopTimeNano = System.nanoTime();
				var milFromNano = stopTimeNano / 1_000_000;
				stop = Instant.ofEpochMilli(milFromNano).plusNanos(stopTimeNano - (milFromNano * 1_000_000));
			}
		}
		// push
		duration = duration.plus(Duration.between(start, stop).minus(getCalibrationValueFor(precision)));
	}

	@Override
	public synchronized void reset(){
		if(isActive.get()){
			stop();
		}
		start = null;
		stop = null;
		duration = Duration.ZERO;
	}

	@Override
	public synchronized void calibrate(Precision precision){
		// stop & reset stopwatch
		reset();
		// reset calibration
		calibration.put(precision, Duration.ZERO);
		// calculate dif
		var total = Duration.ZERO;
		for(int i = 0; i < 1_000_000; i++){
			start(precision);
			stop();
			total = total.plus(getTotalDuration());
			reset();
		}
		var newCal = total.dividedBy(1_000_000);
		// push new
		calibration.put(precision, newCal);
		// reset
		reset();
	}

	@Override
	public Duration getCalibrationValueFor(Precision precision){
		return calibration.get(precision);
	}

	@Override
	public Duration getTotalDuration(){
		return duration;
	}

	@Override
	public Duration getLastDuration(){
		if(start != null && stop != null){
			return Duration.between(start, stop).minus(getCalibrationValueFor(precision));
		}
		return Duration.ZERO;
	}

}
