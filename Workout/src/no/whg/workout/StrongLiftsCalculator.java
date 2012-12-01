package no.whg.workout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Simen Løkken
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
 *  					name "A", "B" or "BOTH".  
 *  					
 *  Also contains information on
 *  			- boolean sessionTypeA 
 *  			- boolean weightUnitTypeKilograms
 *  			- int numberOfSessionsLogged
 */

public class StrongLiftsCalculator {

	private int 			numberOfSessionsLogged;
	private List<Exercise> 	a_session;
	private List<Exercise> 	b_session;
	private boolean 		sessionTypeA;
	private boolean 		weightUnitTypeKilograms;
	
	/*
	 * Constructs a StrongLiftsCalculator and adds default exercises
	 */
	public StrongLiftsCalculator()
	{
		a_session = new ArrayList<Exercise>();
		a_session.add(new Exercise(20, "Squat", 		5, 2.5d));
		a_session.add(new Exercise(20, "Benchpress", 	5, 2.5d));
		a_session.add(new Exercise(30, "Barbell row", 	5, 2.5d));
		
		b_session = new ArrayList<Exercise>();
		b_session.add(a_session.get(0));
		b_session.add(new Exercise(20, "Shoulderpress", 5, 2.5d));
		b_session.add(new Exercise(40, "Deadlift", 		5, 5.0d));
		
		sessionTypeA 			= true;
		weightUnitTypeKilograms = true;
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
			
			else if((exercise.getNumberOfDeloads() 	== 3) 
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
		for(Exercise exercise : b_session)
		{
			bothSessions.add(exercise);
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
	public boolean getWeightUnitTypeKilograms()
	{
		return weightUnitTypeKilograms;
	}
	
	
	/*
	 * @return int numberOfSessionsLogged so far.
	 */
	public int getNumberOfSessionsLogged()
	{
		return numberOfSessionsLogged;
	}
	
	public void createNewExercise(String name, String sessionName)
	{
		if(sessionName == "A")
		{
			a_session.add(new Exercise(name));
		}
		else if(sessionName == "B")
		{
			b_session.add(new Exercise(name));
		}
		else if(sessionName == "BOTH")
		{
			a_session.add(new Exercise(name));
			b_session.add(a_session.get(a_session.size() - 1));
		}
	}
	
	
}
