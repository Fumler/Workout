package no.whg.workout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Simen L�kken
 * 
 * This class manages the five StrongLifts 5x5 exercises. 
 * 		- Updates weight according to user success and progression increment size.
 * 		- Calculates deloads according to number of fails. 
 * 		- Calculates number of sets according to number of deloads.
 * 		- Works as interface between the exercises and the rest of the application.
 * 
 * 
 *  HOW TO USE:
 *  
 *  getCurrentSession() returns an ArrayList<Exercise> containing the list of exercises 
 *  					in current session
 *  
 *  getSessionByName(String sessionName) 
 *  					returns a specific ArrayList<Exercise> session named "A" or "B"
 *  
 *  getBothSessions()	returns an ArrayList<Exercise> with both sessions merged. First
 *  					"A" then "B". 
 *  
 *  updateSessionWeights(List<Exercise> exercise)
 *  					Takes a list of exercises that has been marked as success or !success
 *  					using the boolean in each exercise, and does appropriate updates.
 *  
 *  createNewExercise(String name, String sessionName)
 *  					Creates a new exercise with default values with "name". Added to session
 *  					name "A", "B" or "BOTH". Returns true if success. 
 *  
 *  deleteExercise(Exercise exerciseToDelete)
 *  					Deletes all references to 'exerciseToDelete'. Returns true if success.
 *
 *  changeWeightUnit()	Modifies all exercise currentWeights, weightIncrements and progressList
 *  					to pounds or kilograms, depending on current unit type. 
 *  					
 *  Also contains information on
 *  			- boolean sessionTypeA 
 *  			- boolean weightUnitKilograms
 *  			- int numberOfSessionsLogged
 */

public class StrongLiftsCalculator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int 			numberOfSessionsLogged;
	private List<Exercise> 	a_session;
	private List<Exercise> 	b_session;
	private boolean 		sessionTypeA;
	private boolean 		weightUnitKilograms;
	
	/*
	 * Constructs a StrongLiftsCalculator and adds default exercises
	 */
	public StrongLiftsCalculator()
	{
		a_session = new ArrayList<Exercise>();
		a_session.add(new Exercise(20, "Squat",			"sqt", 	5, 2.5d));
		a_session.add(new Exercise(20, "Benchpress", 	"bpr",	5, 2.5d));
		a_session.add(new Exercise(30, "Barbell row", 	"brw",	5, 2.5d));
		
		b_session = new ArrayList<Exercise>();
		b_session.add(a_session.get(0));
		b_session.add(new Exercise(20, "Shoulderpress", "spr",	5, 2.5d));
		b_session.add(new Exercise(40, "Deadlift", 		"dlt",  1, 5.0d));
		
		sessionTypeA 			= true;
		weightUnitKilograms = true;
		numberOfSessionsLogged  = 0;	
			
	}
	
	/*
	 * Updates weights for an exercise
	 * 
	 * @param Exercise exercise to update. 
	 */
	private void updateWeightsForExercise(Exercise exercise)
	{
		double currentWeight   = exercise.getCurrentWeight();
		double weightIncrement = exercise.getWeightIncrement();
		
		exercise.setCurrentWeight(currentWeight + weightIncrement);
	}
	
	/*
	 * Selects current session for calculating deloads and sets.
	 */
	private void calculateDeloadsAndSetsForCurrentSession()
	{
		if(sessionTypeA)
		{
			calculateDeloadsAndSets(a_session);
			sessionTypeA = false;
		}
		else
		{
			calculateDeloadsAndSets(b_session);
			sessionTypeA = true;
		}
	}
	
	/*
	 * Actually calculates deloads and sets depending on number of
	 * deloads and fails in each exercise.
	 * 
	 * @param List<Exercise> currentSession session to adjust sets and deloads in.
	 */
	private void calculateDeloadsAndSets(List<Exercise> currentSession)
	{
		double deloadWeight = 0;
		double newWeight	= 0;
		
		for (Exercise exercise : currentSession) 
		{
			if  ((exercise.getNumberOfFails() == 3) 
				&& (exercise.getNumberOfDeloads() != 3))
			{
				deloadWeight = 0.9 * exercise.getCurrentWeight();
				
				while(newWeight < deloadWeight)
				{
					newWeight = newWeight + 2.5;
				}
				
				exercise.setCurrentWeight(newWeight - 2.5);
				exercise.incrementNumberOfDeloads();
				exercise.setNumberOfFails(0);
			}
			
			else if((exercise.getNumberOfDeloads() 	== 2) 
				 && (exercise.getNumberOfSets() 	!= 1))
			{
				exercise.adjustNumberOfSets();
				exercise.setNumberOfDeloads(0);
			}
		}
	}
	
	/*Runs through a session and updates it's weights and number of fails
	 * depending on if it succeeded or not. 
	 * 
	 * @param ArrayList<Exercise> sessiontToUpdate the current session. 
	 */
	public void updateSessionWeights(List<Exercise> sessionToUpdate)
	{
		numberOfSessionsLogged++;
		
		for (Exercise exercise : sessionToUpdate) 
		{
			if(exercise.getSuccess())
			{
				updateWeightsForExercise(exercise);
				exercise.setNumberOfFails(0);
				exercise.setSuccess(false);
			}
			else
			{
				exercise.incrementNumberOfFails();
			}
			
			exercise.updateProgress();
		}
		
		calculateDeloadsAndSetsForCurrentSession();
	}
	
	//************************ SESSIONS *****************************
	
	/* Returns the session asked by parameter. 
	 * 
	 * @param String sessionName
	 * @return List<Exercise> a_session || b_session
	 */
	
	public List<Exercise> getSessionByName(String sessionName)
	{
		if(sessionName == "A")
		{
			return a_session;
		}
		
		else if(sessionName == "B")
		{
			return b_session;
		}
		
		else
		{
			System.out.println("There is no session named: " + sessionName);
			return null;
		}		
	}
	
	/*
	 * Returns the current session
	 * @return List<Exercise>
	 */
	public List<Exercise> getCurrentSession()
	{
		if(sessionTypeA)
		{
			return a_session;		
		}
		
		else
		{
			return b_session;
		}
	}
	
	/*
	 * Returns both sessions in a single list
	 * 
	 * @return List<Exercise> bothSessions
	 */
	public List<Exercise> getBothSessions()
	{
		List<Exercise> bothSessions = new ArrayList<Exercise>();
		for(Exercise exercise : a_session)
		{
			bothSessions.add(exercise);
		}
		for(int i = 1; i < b_session.size(); i++)
		{
			bothSessions.add(b_session.get(i));
		}
		
		return bothSessions;
	}
	
	/*
	 * @return boolean sessionTypeA telling what session is up.
	 */
	public boolean getSessionTypeA()
	{
		return sessionTypeA;
	}
	
	
	/*
	 * @return boolean weightUnitTypeKilograms telling what unit type is used. 
	 */
	public boolean getWeightUnitKilograms()
	{
		return weightUnitKilograms;
	}
	
	
	/*
	 * Changes weight unit type in all exercises. 
	 */
	public void changeWeightUnit()
	{
		if(weightUnitKilograms)
		{
			for(Exercise exercise : getBothSessions())
			{
				changeToPounds(exercise);
			}			
			weightUnitKilograms = false;
		}
		else
		{
			for(Exercise exercise : getBothSessions())
			{
				changeToKilograms(exercise);
			}
			weightUnitKilograms = true;
		}
	}
	
	/*
	 * Actually does unit calculation to pounds. We're not interested in exact conversion
	 * except in the progressList.
	 * 
	 * @param List<Exercise> session
	 */
	private void changeToPounds(Exercise exercise)
	{
		
		exercise.setCurrentWeight(exercise.getCurrentWeight() * 2);
		exercise.setWeightIncrement(exercise.getWeightIncrement() * 2);
		
		List<Double> progressList = exercise.getProgressList();
		
		for(Double loggedWeight : progressList)
		{
			loggedWeight = (double) Math.round((loggedWeight * 2.20462262f) * 100) / 100; 
		}	
		
	}
	
	/*
	 * Actually does unit calculation to pounds. We're not interested in exact conversion except
	 * in the progressList.
	 * 
	 * @param List<Exercise> session
	 */
	private void changeToKilograms(Exercise exercise)
	{
		exercise.setCurrentWeight(exercise.getCurrentWeight() / 2);
		exercise.setWeightIncrement(exercise.getWeightIncrement() / 2);
		
		List<Double> progressList = exercise.getProgressList();
		
		for(Double loggedWeight : progressList)
		{
			loggedWeight = (double) Math.round((loggedWeight * 0.45359237f) * 100) / 100; 
		}
	}
	
	
	/*
	 * @return int numberOfSessionsLogged so far.
	 */
	public int getNumberOfSessionsLogged()
	{
		return numberOfSessionsLogged;
	}
	
	/*Adds a new exercise to session A, B or BOTH. The exercise is given default values
	 * except for it's name. Returns true if successfull. 
	 * 
	 * @param String name
	 * @param String sessionName
	 * @return boolean
	 */
	public boolean createNewExercise(String name, String sessionName)
	{
		if(sessionName == "A")
		{
			a_session.add(new Exercise(name));
			return true;
		}
		else if(sessionName == "B")
		{
			b_session.add(new Exercise(name));
			return true;
		}
		else if(sessionName == "BOTH")
		{
			a_session.add(new Exercise(name));
			b_session.add(a_session.get(a_session.size() - 1));
			return true;
		}
		else
		{
			System.out.println("ERROR! There is no session named: " + name + "!");
			System.out.println("\n\nNO EXERCISE ADDED!!");
			return false;
		}
	}
	
	/*Deletes all references to the parameter object. Returns true if successfull. 
	 * 
	 * @param Exercise exerciseToDelete
	 * @return boolean
	 */
	public boolean deleteExercise(Exercise exerciseToDelete)
	{
		if((!a_session.remove(exerciseToDelete) && (!b_session.remove(exerciseToDelete))))
		{
			System.out.println("ERROR! You are trying to delete a modified or non-existant exercise.");
			System.out.println("\n\nNo exercises deleted!!");
			return false;
		}
		else return true;
	}
	
	
	/*
	 * 
	 */
	public void resetExercisesToDefault()
	{
		a_session.clear();
		b_session.clear();
		
		a_session.add(new Exercise(20, "Squat",			"sqt", 	5, 2.5d));
		a_session.add(new Exercise(20, "Benchpress", 	"bpr",	5, 2.5d));
		a_session.add(new Exercise(30, "Barbell row", 	"brw",	5, 2.5d));
		
		b_session.add(a_session.get(0));
		b_session.add(new Exercise(20, "Shoulderpress", "spr",	5, 2.5d));
		b_session.add(new Exercise(40, "Deadlift", 		"dlt",  5, 5.0d));
		
		sessionTypeA 			= true;
		weightUnitKilograms 	= true;
		numberOfSessionsLogged  = 0;
	}
	
	
}
