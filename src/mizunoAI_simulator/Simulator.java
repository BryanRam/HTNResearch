package mizunoAI_simulator;

import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import HTNPlanner.Planner;
import HTNPlanner.UCBPlanner;
import struct.FrameData;
import struct.GameData;
import struct.CharacterData;
import struct.MotionData;
import struct.AttackData;
import util.Helper;
import enumerate.Action;
//import fighting.Attack;

public class Simulator {
	/** simulation time length*/
	private final static int SIMULATE_LIMIT = 60;
	
	private GameData gameData;
	
	private FrameData frameData;
	
	private boolean player;
	
	/** create GameData for simulation*/
	public Simulator(GameData gameData,boolean player){
		this.gameData = gameData;
		this.player = player;
	}
	
	/** set FrameData*/
	public void setFrameData(FrameData frameData){
		this.frameData = frameData;
	}
	
	public SimCharacter getSimCharacter(FrameData a_frameData, boolean myCharacter)
	{
		CharacterData character;
		Vector<MotionData> motionData;

		// copy
		if(player){
			if(myCharacter)
			{
				character = frameData.getCharacter(true);
				motionData = new Vector<MotionData>(gameData.getMotionData(true));
			}
			else
			{
				character = frameData.getCharacter(false);
				motionData = new Vector<MotionData>(gameData.getMotionData(false));
			}
		}else{
			if(myCharacter)
			{
				character = frameData.getCharacter(false);
				motionData = new Vector<MotionData>(gameData.getMotionData(false));
			}
			else
			{
				character = frameData.getCharacter(true);
				motionData = new Vector<MotionData>(gameData.getMotionData(true));
			}
		}
		
		// create CharacterData for simulation

		return new mizunoAI_simulator.SimCharacter(character,motionData, myCharacter? player : !player);
	}
	
	public SimCharacter getSimCharacter(CharacterData character, Vector<MotionData> motionData, boolean myCharacter)
	{
		// create CharacterData for simulation
		return new mizunoAI_simulator.SimCharacter(character,motionData, myCharacter? player : !player);
	}
	
	
	/** simulate and calculate evaluation value when an AI conducts the myAction against the oppAction*/
	public int simulate(Action myAction,Action oppAction , mizunoAI_simulator.SimCharacter simMyCharacter, mizunoAI_simulator.SimCharacter simOppCharacter, Deque<mizunoAI_simulator.SimAttack> simAttack ){
		int mySaveHp;
		int oppSaveHp;
		
		// set AI's HP before simulation
		mySaveHp = new Integer(simMyCharacter.getHp());
		oppSaveHp = new Integer(simOppCharacter.getHp());
		
		
		// if my character's energy is shortage
		if(simMyCharacter.getEnergy()+ simMyCharacter.getMotionVector().elementAt(myAction.ordinal()).attackStartAddEnergy < 0) return -1000;
		
		mizunoAI_simulator.SimFighting simFighting;
		
		/*
		// simAttack is the projectileData of two characters
		Deque<mizunoAI_simulator.SimAttack> simAttack = new LinkedList<mizunoAI_simulator.SimAttack>();
		int size = frameData.getAttack().size();
		// copy attackData to simAttack
		for(int i = 0 ; i < size ; i++){
			Attack temp = frameData.getAttack().pop();
			SimAttack tempSimAttack = new SimAttack(temp);
			simAttack.addLast(tempSimAttack);
			frameData.getAttack().add(temp);
		}  //TODO: should not take current frame data when simulating the plan
		*/
		
		
		// initialize FightingData
		if(player){
			simFighting = new mizunoAI_simulator.SimFighting(simMyCharacter,simOppCharacter,simAttack,myAction,oppAction);
		}
		else{
			simFighting = new mizunoAI_simulator.SimFighting(simOppCharacter,simMyCharacter,simAttack,oppAction,myAction);
		}
		
		long currTime;
		
		
		// get the length of the action from motion data 
		int simLimit = 	simMyCharacter.getMotionVector().elementAt(myAction.ordinal()).getFrameNumber();
		
		if(simMyCharacter.getMotionVector().elementAt(myAction.ordinal()).getAttackActive() >= Helper.PROJECTILE_ACTIVE_FRAMES)
		{
			simLimit = simMyCharacter.getMotionVector().elementAt(myAction.ordinal()).getAttackActive();
			System.out.println("simulating projectile for " + simLimit + " frames");
		}
			
		// simulate the game for simLimit frames
		for(int i = 0 ; i < simLimit ; i++)   //SIMULATE_LIMIT
		{  
			//System.out.println("Action to simulate : " + myAction.name() + " frames " +i + "/"+ simLimit);
			
			currTime = System.currentTimeMillis();
			if(currTime - Planner.INSTANCE.time >= 16.6)
			{
				return (simMyCharacter.getHp() - mySaveHp) - (simOppCharacter.getHp() - oppSaveHp) ;
			}
			
			simFighting.processingFight();
		}
		
		// calculate the evaluation value of the myAction
		return (simMyCharacter.getHp() - mySaveHp) - (simOppCharacter.getHp() - oppSaveHp) ;
		
		//return (simOppCharacter.getHp() - oppSaveHp) / ((simMyCharacter.getHp() - mySaveHp) + (simOppCharacter.getHp() - oppSaveHp) + y);
	}
	

	/** execute simulation using myActData and oppActData*/
	public Action simulate(Deque<Action> myActData,Deque<Action> oppActData,int[] check, mizunoAI_simulator.SimCharacter simMyCharacter, mizunoAI_simulator.SimCharacter simOppCharacter){		
		int myActionSize = myActData.size();
		int[][] result = new int[myActionSize][2];
		int my=0;
		int resultMax = -1000;
		int actNum = 9;
		
		// initialize array
		for(int i = 0 ; i < myActionSize ; i++){
			result[i][0] = 0;
		}
		// set ordinal of each myActData to array
		for(Iterator<Action> myAct = myActData.iterator();myAct.hasNext();my++){
			result[my][1] = myAct.next().ordinal();
		}
		
		my = 0;
		
		// execute simulation by a round robin
		for(Iterator<Action> i = myActData.iterator();i.hasNext();my++){    //TODO: take these actions from planner
			Action myAct = i.next();
			for(Iterator<Action> j = oppActData.iterator();j.hasNext();){  //TODO: take only one/best action?
				Action oppAct = j.next();
				//int resultTemp = this.simulate(myAct,oppAct,  simMyCharacter, simOppCharacter);
				//result[my][0] += resultTemp*check[oppAct.ordinal()];
			}
			
		}
		
		// search the maximum evaluation value and set the ordinal
		for(int i = 0 ; i < myActionSize ; i++){
			if(resultMax < result[i][0]){
				resultMax = result[i][0];
				actNum = result[i][1];
			}
		}
		
		// if all evaluation value are negative value, an AI conducts CROUCH_GUARD
		if(resultMax < 0) return Action.CROUCH_GUARD;
		
		// return the Action with the maximum evaluation value
		return Action.values()[actNum];
	}	
}
