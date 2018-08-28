/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import aiinterface.CommandCenter;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import enumerate.Action;
import enumerate.AttackAction;
import enumerate.Position;
import enumerate.State;
//import fighting.Attack;
import HTNPlanner.OrderedPlanner;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;
import HTNPlanner.UCBPlanner;
import util.Helper;
import util.Pair;
import util.ActData;
import struct.AttackData;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.Key;
import struct.MotionData;
import aiinterface.AIInterface;
//import gameInterface.AIInterface;
import simulator.Simulator;

public class HTNFighter implements AIInterface {

	//>>>>>>>>>>>>>>>>>>>>>>>>>>Planner related data<<<<<<<<<<<<<<<<<<<<<<<
	Planner planner;
	Deque<PrimitiveTask> currentPlan;
	PrimitiveTask prevTask = null;
	Action prevAction = null;
	int myPrevHp = 0;
	int oppPrevHp = 0;
	Deque<Action> myPrevActions;
	Deque<Action> oppPrevActions;
	private static final int ACTIONSTOSAVE = 5;
	long framesLeftLastAction = 0;
	
    //>>>>>>>>>>>>>>>>>>>>>>>>>>Game related data<<<<<<<<<<<<<<<<<<<<<<<
    boolean playerB;
    int playerI;
    GameData gameData;
	Key inputKey;
	FrameData frameData;
	CommandCenter command;
	
	private Simulator simulator;
	
	/** my CharacterData*/
	CharacterData my;
	/** opponent's CharacterData*/
	CharacterData opp;
		
	Vector<MotionData> myMotionData;
	Vector<MotionData> oppMotionData;

	String myCharacterName = "";
	
	/** delay frames number*/
	private static final int DELAY = 15;
	
	long time;
	
	/** an opponent's action predicted by k-nn*/
	Deque<Action> oppAct;

	int[] combos;
	int[] combosOpp;

	Vector<Action> myCombo = null;
	Vector<Action> oppCombo =null;
	
	@Override
	public void close() {
	}

	/*@Override
	public String getCharacter() 
	{
		return this.gameData.getCharacterName(this.playerB);
	}*/

	@Override
	public void getInformation(FrameData arg0) {
		frameData = arg0;
		
		time = System.currentTimeMillis();
		planner.time = time;

		if(playerB){
			/*my = frameData.getP1();
			opp = frameData.getP2();*/
			
			my = frameData.getCharacter(true);
			opp = frameData.getCharacter(false);
			
		}else{
			/*my = frameData.getP2();
			opp = frameData.getP1();*/
			
			my = frameData.getCharacter(false);
			opp = frameData.getCharacter(true);
		}
	}

	@Override
	public int initialize(GameData arg0, boolean arg1) 
	{		      
        //Game related data
        gameData = arg0;
		playerB = arg1;
		playerI = playerB ? 1:0;
		inputKey = new Key();
		frameData = new FrameData();
		command = new CommandCenter();
		simulator = gameData.getSimulator();
		 	
		if(playerB)
		{
			/*myMotionData = gameData.getPlayerOneMotion();
			oppMotionData = gameData.getPlayerTwoMotion();
			myCharacterName = gameData.getPlayerOneCharacterName();*/
			
			myMotionData = new Vector<MotionData>(gameData.getMotionData(true));
			oppMotionData = new Vector<MotionData>(gameData.getMotionData(false));
			myCharacterName = gameData.getCharacterName(true);
		}
		else
		{
			/*myMotionData = gameData.getPlayerTwoMotion();
			oppMotionData = gameData.getPlayerOneMotion();
			myCharacterName = gameData.getPlayerTwoCharacterName();*/
			
			myMotionData = new Vector<MotionData>(gameData.getMotionData(false));
			oppMotionData = new Vector<MotionData>(gameData.getMotionData(true));
			myCharacterName = gameData.getCharacterName(false);
		}
		
	
		//Planner related data
		//ZEN uses OrderedPlanner with predefined order
		//String tmpcharname = this.gameData.getCharacterName(this.playerB);
		if(myCharacterName.equals("ZEN"))
		{
			/*
			planner = new OrderedPlanner(playerB, gameData);
			planner.initialized = true;
			//*/
			///*
			planner = new UCBPlanner(playerB, gameData);
			((UCBPlanner)(planner)).ReadAllUCBValues();
			//*/
		}
		//GARNET and LUD use UCBPlanner 
		else
		{
			planner = new UCBPlanner(playerB, gameData);
			((UCBPlanner)(planner)).ReadAllUCBValues();
		}
		
		currentPlan = new LinkedList<PrimitiveTask>();
		myPrevActions = new LinkedList<Action>(); 
		oppPrevActions = new LinkedList<Action>(); 

		this.oppAct = new LinkedList<Action>();

		this.combos = new int[]{0,0,0,0};
		this.combosOpp = new int[]{0,0,0,0};
		
		return 0;
	}

	@Override
	public Key input() {
		return inputKey;
	}

	@Override
	public void roundEnd(int p1Hp, int p2Hp, int frames)
	{
		/*if(Helper.DEBUG_ACTION_EXECUTION)
		{
			System.out.println("combos this round = " + combos[0] + ", " + combos[1] + ", "+ combos[2] + ", "+ combos[3] );
			System.out.println("combos opp = " + combosOpp[0] + ", " + combosOpp[1] + ", "+ combosOpp[2] + ", "+ combosOpp[3] );
		}*/
		this.combos = null;
		this.combosOpp = null;
		
		 myPrevHp = 0;
		 oppPrevHp = 0;
		 
		 System.out.println("in rend");
		 
		 //save UCB values in a file
		if(planner.getClass().getSimpleName().equals("UCBPlanner") && Helper.LEARN_UCB)
		{
			System.out.println("Learning");
			if(Helper.DEBUG_UCB_STATICTICS)
			{
				//System.out.println("printing to: data/aiData/HTNFighter/UCB_"+ this.gameData.getCharacterName(this.player)+".txt");
				//System.out.println("combos opp = " + combosOpp[0] + ", " + combosOpp[1] + ", "+ combosOpp[2] + ", "+ combosOpp[3] );
			}
			((UCBPlanner)(planner)).PrintAllUCBValues();
		}
	}
	
	@Override
	public void processing() 
	{
		if(frameData.getEmptyFlag())
		{
			currentPlan.clear();
			prevAction = Action.NEUTRAL;
			framesLeftLastAction = 0;
			this.myPrevActions.clear();
			this.oppPrevActions.clear();
			this.myCombo = null;
			this.oppCombo = null;
			this.combos = new int[]{0,0,0,0};
			this.combosOpp = new int[]{0,0,0,0};
			return;
		}
		
		//GARNET's or LUD's planner
		if(!planner.initialized)
		{
			//training games for LUD - enable learning
			if(opp.getHp() > 1000 && !myCharacterName.equals("GARNET"))
			{
				Helper.LEARN_UCB = true;
				//no exploitation, exploration only
				UCBPlanner.C = (float) Math.sqrt(9999);
			}
			else
			{
				Helper.LEARN_UCB = false;
				//UCBPlanner.C = (float) 0.5;
				UCBPlanner.C = (float) 2.0;
				planner.initialized = true;
			}
			
		}

		my = playerB ? frameData.getCharacter(true) : frameData.getCharacter(false);
		boolean prevActionExecuted = this.CheckPrevActionSuccess();

		long currTime = System.currentTimeMillis();		
		if(currTime - time > 16.6)
		{
			prevAction = Action.STAND;
			framesLeftLastAction = frameData.getRemainingFramesNumber();
			return;
		}
				
		//myCombo = this.GetCurrentCombo(true);
		//oppCombo = this.GetCurrentCombo(false);
		myCombo = null;
		oppCombo = null;
		
		FrameData newframeData = simulator.simulate(frameData, playerB, this.myPrevActions, this.oppPrevActions, DELAY-1); 

		
	    my = playerB ? newframeData.getCharacter(true) : newframeData.getCharacter(false);
	    opp = playerB ? newframeData.getCharacter(false) : newframeData.getCharacter(true);
	    
	    oppAct.clear();
		if(oppAct.isEmpty())
		{
			oppAct.add(opp.getAction());
		}
		
	    command.setFrameData(newframeData, playerB);
	    		
	    //give a higher weight to exploitation towards the end of 3 rounds?
	    if(Helper.DECREASE_C)
	    {
	    	UpdateUCBWeight();
	    }
	    
		if(command.getSkillFlag())
		{	
			inputKey = command.getSkillKey();
			/*
			if(Helper.DEBUG_ACTION_EXECUTION)
			{
				System.out.println("skill flag");
			}
			*/
			return;
		}

		inputKey.empty(); 
		command.skillCancel();
		
		boolean decisionMade = false;
		Key newKey = null;

		if(this.CanPerformNextAction())
		{
			while(!decisionMade)
			{
				currTime = System.currentTimeMillis();
				Action defaultAction = Action.STAND_GUARD;
				//Action defaultAction = Action.STAND;
				if(myCharacterName.equals("LUD"))
				{
					defaultAction = Action.CROUCH_B;
				}
				 
				if(currTime - time > 16.6)
				{
					newKey = GetKeyFromTask(defaultAction); 
					if(Helper.DEBUG_ACTION_EXECUTION)
					{
						System.out.println("no time " + defaultAction.name());
					}
					prevAction = defaultAction;
					framesLeftLastAction = frameData.getRemainingFramesNumber();
					return;
				}
				
				boolean replan = false;
				
				if(!currentPlan.isEmpty() && !(playerB ?  frameData.getProjectilesByP2() : frameData.getProjectilesByP1()).isEmpty() )
				{
					/*
					if(Helper.DEBUG_ACTION_EXECUTION)
					{
						System.out.println("<<<<<<<<<<<<<<projectiles in air - re-plan!");
					}
					*/
					replan = true;
					currentPlan.clear();
				}
				
				if(!prevActionExecuted)
				{
					if(Helper.DEBUG_ACTION_EXECUTION)
					{
						System.out.println("------- prev action was not executed - re-plan!" + prevAction.name()  + " " +frameData.getRemainingFramesNumber());
					}
					replan = true;
					currentPlan.clear();
				}

					
				//create a new plan
				if(currentPlan.isEmpty() || replan)
				{
					currentPlan = planner.CreatePlan(newframeData, my, opp, myCombo, oppCombo, this.oppAct);
				}
				
				//get next plan task		
				if(!currentPlan.isEmpty())
				{
	
					PrimitiveTask nextTask = currentPlan.getFirst();	
					nextTask = currentPlan.removeFirst();
					 
					 // check whether it is still applicable
					 SimCharacter mySimCharacter = planner.simulator.getSimCharacter(my, myMotionData, true);
				     SimCharacter oppSimCharacter = planner.simulator.getSimCharacter(opp, oppMotionData, false); 
				     mySimCharacter.SetPrevActions(myPrevActions);
			         oppSimCharacter.SetPrevActions(oppPrevActions);
			         
				     boolean holds = nextTask.CheckPreconditions(new Pair<>(mySimCharacter,oppSimCharacter));
				    
				     // it is applicable or we do not have enough time to re-plan
				     if (holds || currTime - time >= 16.6)
				     {
				    	 Key key = null;
				    	 key = GetKeyFromTask(nextTask.myAction);
						 if(key != null)
						 {
							inputKey = key;
							prevAction = nextTask.myAction;
							framesLeftLastAction = frameData.getRemainingFramesNumber();
							AddPrevAction(prevAction, true);							
						 }				
						 if(holds && Helper.REWARD_FROM_EXECUTION && planner.getClass().getSimpleName().equals("UCBPlanner"))
						 {
							 if(Helper.SUCCESS_AS_REWARD)
							 {
								 nextTask.IncreaseSuccessStatistics(1);
							 }
							 else
							 {
								//int hpDiff = (my.hp - myPrevHp) - (opp.hp - oppPrevHp);
								 if(oppPrevHp == 0)
								 {
									 oppPrevHp = opp.getHp();
								 }
								 int hpDiff = -1*(opp.getHp() - oppPrevHp);
								 if(prevTask!= null)
								 {
									 if(Helper.DEBUG_UCB_STATICTICS)
									 {
										 System.out.println("reward for "+ prevTask.name + " " + hpDiff);
									 }
									 prevTask.IncreaseSuccessStatistics(hpDiff);
								 }
							}
							 prevTask = nextTask;
							 myPrevHp = my.getHp();
							 oppPrevHp = opp.getHp();
						 }
						 
						 decisionMade = true;
				     }
				     
				     // if we do not have enough time to re-plan - do it in the next frame
				     if(!holds)
				     {
				    	 if(Helper.DEBUG_ACTION_EXECUTION)
				    	 {
				    		 System.out.println("Task " + nextTask.name + " not applicable. Re-planning.<<<<<<<<<<<<<<<<");
				    	 }
				    	 currentPlan.clear();
				     }
				     
				}
			}
		}
	}
	
	private boolean CanPerformNextAction()
	{
		//control is not always relevant, because it shows what was 15 frames ago
		boolean canPerform =  frameData.getCharacter(playerB).getRemainingFrame() <= 0 || frameData.getCharacter(playerB).isControl();//
		
		if(!prevAction.equals(Action.NEUTRAL) && canPerform && !myMotionData.elementAt(prevAction.ordinal()).control )//&& !prevAction.equals(command.getMyCharacter().action))
		{
			int actionLength = myMotionData.elementAt(prevAction.ordinal()).getFrameNumber();
			if(actionLength <= 15)
			{
				canPerform = canPerform && framesLeftLastAction - frameData.getRemainingFramesNumber() >= actionLength;
			}
			else
			{
				canPerform = canPerform && framesLeftLastAction - frameData.getRemainingFramesNumber() > 15;
			}
		}
		return canPerform;
	}

	private void UpdateUCBWeight()
	{
		
		float round = (float)frameData.getRound()+1;
		/*
		if(round > 0)
		{
			return;
		}
		*/
		
		//3 rounds, each 60 seconds
		float totalTime = 180;
				
		//60 seconds per round
		float timeLeft = frameData.getRemainingTimeMilliseconds()/1000 + (3-round)*60;
						
		UCBPlanner.C = (float) Math.sqrt(15) * (timeLeft/totalTime) + (float) Math.sqrt(2);
		//UCBPlanner.C = (float) 2.0 * (timeLeft/totalTime);
	}
	
	private boolean CheckPrevActionSuccess()
	{
		int actionLength = myMotionData.elementAt(prevAction.ordinal()).getFrameNumber();
		if(actionLength < 15)
		{		
			//System.out.println("action length < 15");
			return true;
		}

		if(framesLeftLastAction - frameData.getRemainingFramesNumber() < 15)
		{
			return true;
		}
		
		if(prevAction.equals(my.getAction()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Key GetKeyFromTask (Action act)
	{		
		if(act == null)
		{
			System.out.println("Invalid action");
			
			return null;
		}
		
		String actName = act.name();
		Key key = null;

		inputKey.empty(); 
		command.skillCancel();
		command.commandCall(actName);
		key = command.getSkillKey();

		if(Helper.DEBUG_ACTION_EXECUTION)
		{
			System.out.println(">>> Getting next action key: " + actName + " time "  +frameData.getRemainingFramesNumber());
		}
		
		return key;
	}
	
	private void AddPrevAction(Action act, boolean me)
	{
		if(me)
		{
			this.myPrevActions.addLast(act);
			if(this.myPrevActions.size() > ACTIONSTOSAVE )
			{
				this.myPrevActions.removeFirst();
			}
			return;
		}
		
		this.oppPrevActions.addLast(act);
		if(this.oppPrevActions.size() > ACTIONSTOSAVE )
		{
			this.oppPrevActions.removeFirst();
		}
	}
	
	/*
	public Vector<Action> GetCurrentCombo(boolean me)
	{
		if(me)
		{
			if(my != null && my.getCurrentCombo() != null && !my.getCurrentCombo().isEmpty() && combos != null)
			{
				if(this.myCombo == null || this.myCombo.size()!= my.getCurrentCombo().size())
				{
					//System.out.println("my combo = " + my.getCurrentCombo().size());
					if(my.getCurrentCombo().size()!=0)
					{
						combos[my.getCurrentCombo().size()-1] ++;
					}
					if(this.myCombo != null && this.myCombo.size() ==  my.getCurrentCombo().size()-1 && this.myCombo.size()!=0)
					{
						combos[this.myCombo.size()-1]--;
					}
				}
				return my.getCurrentCombo(); 
			}
		}
		else
		{
			if(opp != null && opp.getCurrentCombo() != null && !opp.getCurrentCombo().isEmpty() && combosOpp != null)
			{
				if(this.oppCombo == null || this.oppCombo.size()!= opp.getCurrentCombo().size())
				{
					//System.out.println("opponent's combo = " + opp.getCurrentCombo().size());
					
					if(opp.getCurrentCombo().size() != 0)
					{
						combosOpp[opp.getCurrentCombo().size()-1] ++;
					}
					if(this.oppCombo != null && this.oppCombo.size() ==  opp.getCurrentCombo().size()-1 && this.oppCombo.size()!=0)
					{
						combosOpp[this.oppCombo.size()-1]--;
					}
				}
				
				return opp.getCurrentCombo(); 
			}
		}
		return null;
	}
	//*/

}
