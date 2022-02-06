package de.hypercdn.commons.api.time;

import java.time.Duration;

public interface Stopwatch{

	/**
	 * Sets the current timestamp as start value with default precision
	 */
	default void start(){
		start(Precision.DEFAULT);
	}

	/**
	 * Sets the current timestamp as start value
	 *
	 * @param precision of the timestamp
	 */
	void start(Precision precision);

	/**
	 * Sets the current timestamp as stop value
	 */
	void stop();

	/**
	 * Will execute the specified code and measure its runtime using the start and stop methods internally
	 *
	 * @param runnable to execute
	 */
	default void measure(Runnable runnable){
		measure(Precision.DEFAULT, runnable);
	}

	/**
	 * Will execute the specified code and measure its runtime using the start and stop methods internally
	 *
	 * @param precision of the timestamp
	 * @param runnable  to execute
	 */
	default void measure(Precision precision, Runnable runnable){
		start(precision);
		runnable.run();
		stop();
	}

	/**
	 * Returns the totally accumulated duration
	 *
	 * @return duration
	 */
	Duration getTotalDuration();

	/**
	 * Returns the duration of the last run
	 *
	 * @return duration
	 */
	Duration getLastDuration();

	/**
	 * Resets the stopwatch
	 */
	void reset();

	/**
	 * Will calibrate the stopwatch for all measurement precisions
	 */
	default void calibrate(){
		for(var pre : Precision.values()){
			calibrate(pre);
		}
	}

	/**
	 * Calibrates the stopwatch for the given measurement precision
	 *
	 * @param precision to calibrate for
	 */
	void calibrate(Precision precision);

	Duration getCalibrationValueFor(Precision precision);

	enum Precision{
		DEFAULT,
		HIGH
	}

}
