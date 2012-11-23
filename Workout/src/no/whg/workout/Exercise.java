package no.whg.workout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simen
 * This class describes an Exercise in a StrongLifts context. It is limited to modeling the exercises,
 * so all calculations, managing and storing will take place in the Slcalc-class.
 *
 */

public class Exercise {
	
	private double 			currentWeight;
	private String 			name;
	private int 			numberOfSets;
	private double 			weightIncrement;
	private int 			numberOfFails;
	private int 			numberOfDeloads;
	private int 			numberOfWorkouts;
	private List<Double> 	progressList; 
	private boolean 		success;
	
	public Exercise(int currentWeight,  String name, 
					int numberOfSets , 	double weightIncrement) 
	{
		this.currentWeight 		= currentWeight;
		this.name				= name;
		this.numberOfSets 		= numberOfSets;
		this.weightIncrement 	= weightIncrement;
		
		numberOfFails 			= 0;
		numberOfDeloads 		= 0;
		numberOfWorkouts 		= 0;
		
		progressList 			= new ArrayList<Double>();

		success					= false;
	}
	
	//Progress
	public void updateProgress(){
		numberOfWorkouts++;
		progressList.add(currentWeight);
	}
	
	// Current weight
	public double getCurrentWeight() {
		return currentWeight;
	}
	public void setCurrentWeight(double currentWeight) {
		this.currentWeight = currentWeight;
	}
	
	
	//Number of fails
	public int getNumberOfFails() {
		return numberOfFails;
	}
	public void setNumberOfFails(int numberOfFails) {
		this.numberOfFails = numberOfFails;
	}
	public void incrementNumberOfFails(){
		numberOfFails++;
	}
	
		
	//Number of deloads
	public int getNumberOfDeloads() {
		return numberOfDeloads;
	}
	public void incrementNumberOfDeloads() {
		numberOfDeloads++;
	}
	public void setNumberOfDeloads(int newNumberOfDeloads){
		numberOfDeloads = newNumberOfDeloads;
	}
	
		
	//Number of sets
	public int getNumberOfSets() {
		return numberOfSets;
	}
	public void setNumberOfSets(int numberOfSets) {
		this.numberOfSets = numberOfSets;
	}
	
	public void adjustNumberOfSets()
	{
		if(numberOfSets == 5)
		{
			numberOfSets = 3;
		}
		else if(numberOfSets == 3)
		{
			numberOfSets = 1;
		}
	}
	
	
	//Weights increments
	public double getWeightIncrement() {
		return weightIncrement;
	}
	public void setWeightIncrement(double weightIncrement) {
		this.weightIncrement = weightIncrement;
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	//Name of exercise
	public String getName()
	{
		return name;
	}
	
}
