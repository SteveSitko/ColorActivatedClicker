package clicker;

import java.util.Random;

public class TimerArgs {

	private int delayDuration;			// Time in ms of the standard delay added before a click
	
	private int longDelayChance;		// Chance out of 100 that a long-delay will occur
	private int longDelayDurationMin;	// Min ms of the long delay
	private int longDelayDurationMax;	// Max ms of the long delay
	
	private int normalDelayBase;		// Base delay

	private int delayOffsetMin;			
	private int delayOffsetMax;
	
	private Random rand;
	
	public TimerArgs() {
		assignDefaults();
	}
	
	// Calculate the delay before the timer fires again
	public int calculateTimerDelay() {
		delayDuration = 0;
		
		delayDuration += calculateLongDelay();
		delayDuration += calculateNormalDelay();
		
		return delayDuration;
	}
	
	// Gets the randomly-occurring long delay value; returns 0 if the long delay does not occur
	private int calculateLongDelay() {
		rand = new Random();
		int longDelayDuration = 0;
		int randomRolledNumber = rand.nextInt(100);									// Pick a number from 0 to 99 inclusive
		boolean longDelayOccurs = (randomRolledNumber <= (longDelayChance - 1));	// If number roll is between 0 and chance, long delay occurs
		
		if (longDelayOccurs) longDelayDuration = rand.nextInt((longDelayDurationMax - longDelayDurationMin) + 1) + longDelayDurationMin;
		return longDelayDuration;
	}
	
	private int calculateNormalDelay() {
		rand = new Random();
		int normalDelay = normalDelayBase;
		
		normalDelay += calculateNormalDelayOffset();
		
		return normalDelay;
	}
	
	private int calculateNormalDelayOffset() {
		rand = new Random();
		boolean offsetIsPositive = rand.nextInt(2) == 0;						// If rand rolls 0, add offset; if 1, subtract
		int delayOffset = rand.nextInt((delayOffsetMax - delayOffsetMin) + 1) + delayOffsetMin;
		
		if (!offsetIsPositive) delayOffset *= -1; 
		
		return delayOffset;
	}
	
	// Set default values 
	public void assignDefaults() {
		longDelayChance = 2;
		longDelayDurationMin = 2000;
		longDelayDurationMax = 7000;
		normalDelayBase = 500;
		delayOffsetMin = 21;
		delayOffsetMax = 211;
	}
	
	// Getters, Setters
	
	
	public int getNormalDelayBase() {
		return normalDelayBase;
	}

	public void setNormalDelayBase(int normalDelayBase) {
		this.normalDelayBase = normalDelayBase;
	}

	public int getLongDelayChance() {
		return longDelayChance;
	}

	public void setLongDelayChance(int longDelayChance) {
		this.longDelayChance = longDelayChance;
	}

	public int getLongDelayDurationMin() {
		return longDelayDurationMin;
	}

	public void setLongDelayDurationMin(int longDelayDurationMin) {
		this.longDelayDurationMin = longDelayDurationMin;
	}

	public int getLongDelayDurationMax() {
		return longDelayDurationMax;
	}

	public void setLongDelayDurationMax(int longDelayDurationMax) {
		this.longDelayDurationMax = longDelayDurationMax;
	}

	public int getDelayOffsetMin() {
		return delayOffsetMin;
	}

	public void setDelayOffsetMin(int delayOffsetMin) {
		this.delayOffsetMin = delayOffsetMin;
	}

	public int getDelayOffsetMax() {
		return delayOffsetMax;
	}

	public void setDelayOffsetMax(int delayOffsetMax) {
		this.delayOffsetMax = delayOffsetMax;
	}
}
