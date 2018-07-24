/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;





import java.util.Vector;

import enumerate.Action;
import enumerate.State;
//import fighting.Attack;
import aiinterface.AIInterface;
import HTNPlanner.CompoundTasks.c_Act;
import HTNPlanner.PrimitiveTasks.*;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import mizunoAI_simulator.Simulator;
import struct.CharacterData;
import struct.FrameData;
import struct.GameData;
import struct.AttackData;
import struct.Key;
import struct.MotionData;
import util.Helper;
import util.Pair;

public abstract class Planner 
{
	public Deque<PrimitiveTask> plan;

	
	private PrimitiveTask[] attacks;
	
	private ArrayList<Method> lowAttacks;
	private ArrayList<Method> middleAttacks;
	private ArrayList<Method> highAttacks;
	private ArrayList<Method> throwAttacks;
	
	private ArrayList<Method> projectileAttacks;
	private ArrayList<Method> knockdownAttacks;
	private ArrayList<Method> quickAttacks;
	private ArrayList<Method> slidingAttacks;
	private ArrayList<Method> knockbackAttacks;
	private ArrayList<Method> airAttacksBackwards;
	private ArrayList<Method> airAttacksForewards;
	
	private ArrayList<Method> lowAttacks_sorted;
	private ArrayList<Method> middleAttacks_sorted;
	private ArrayList<Method> highAttacks_sorted;
	private ArrayList<Method> throwAttacks_sorted;
	
	private ArrayList<Method> projectileAttacks_sorted;
	private ArrayList<Method> knockdownAttacks_sorted;
	private ArrayList<Method> quickAttacks_sorted;
	private ArrayList<Method> slidingAttacks_sorted;
	private ArrayList<Method> knockbackAttacks_sorted;
	
	private ArrayList<Action> lowAttacksA;
	private ArrayList<Action> middleAttacksA;
	private ArrayList<Action> highAttacksA;
	private ArrayList<Action> throwAttacksA;
	
	private ArrayList<Action> projectileAttacksA;
	private ArrayList<Action> knockdownAttacksA;
	private ArrayList<Action> quickAttacksA;
	private ArrayList<Action> slidingAttacksA;
	private ArrayList<Action> knockbackAttacksA;

	
	protected Task goalTask;
	
	protected GameData gameData;
	public FrameData frameData;
	public boolean player;
	public int playerI;
 	public Simulator simulator;
	public static Planner INSTANCE;
	public long time;
	public long secondsLeft;	
	public Deque<Action> nextOppAct;
	private Deque<mizunoAI_simulator.SimAttack> simAttack;
	
	public boolean moveAwayInAir = false;
	public boolean projectileAboveMe = false;
	public boolean freeProjectileAvailable = false;
	
	Vector<MotionData> myMotionData;
	Vector<MotionData> oppMotionData;
 	
	public boolean initialized = false;
	
	public Planner(boolean player, GameData gameData)
	{
		Planner.INSTANCE = this;
		this.simulator = new Simulator(gameData, player);
		this.gameData = gameData;
		this.player = player;
		this.playerI = player ? 1 : 0;
		this.plan = new LinkedList<PrimitiveTask>(); 
		this.RegisterAttacks();
		myMotionData =  this.GetMotionData(true);
		oppMotionData  = this.GetMotionData(false);
		simAttack = new LinkedList<mizunoAI_simulator.SimAttack>();
	}
	
	public Deque<PrimitiveTask> CreatePlan(FrameData frameData, CharacterData my, CharacterData opp, Vector<Action> myCombo, Vector<Action> oppCombo, Deque<Action> nextOppAct) 
	{
		this.frameData = frameData;
		//if(frameData.getP1() == null || frameData.getP2() == null)
		if(frameData.getCharacter(true) == null || frameData.getCharacter(false) == null)
        	return this.plan;
		
		//provide current data to the simulator
        simulator.setFrameData(frameData);
        
        //reset temp. vars
        this.nextOppAct = nextOppAct;
        moveAwayInAir = false;
        projectileAboveMe = false;
        Planner.INSTANCE.secondsLeft = frameData.getRemainingTimeMilliseconds()/1000;
        SimCharacter mySimCharacter = new SimCharacter(my, myMotionData, player);
        SimCharacter oppSimCharacter = new SimCharacter(opp, oppMotionData, !player);
        
        //copy current combo info
        if(myCombo != null && !myCombo.isEmpty())
    	{
        	mySimCharacter.setHitNumber(myCombo.size());
        	
    		for(int i = 0; i< myCombo.size(); ++i)
    		{
    			mySimCharacter.AddAttackAction(myCombo.elementAt(i)); 
    		}
    	}
    	
        if(oppCombo != null && !oppCombo.isEmpty())
    	{
        	oppSimCharacter.setHitNumber(oppCombo.size());
            
    		for(int i = 0; i< oppCombo.size(); ++i)
    		{
    			oppSimCharacter.AddAttackAction(oppCombo.elementAt(i)); 
    		}
    	}
        
        //copy current projectileData of the opponent
 		simAttack.clear();

		Deque<AttackData> deq = player ?  frameData.getProjectilesByP2() : frameData.getProjectilesByP1();
 		for(Iterator<AttackData> j = deq.iterator() ; j.hasNext() ; )
		{
			AttackData temp = j.next();
 			SimAttack tempSimAttack = new SimAttack(temp);
 			simAttack.addLast(tempSimAttack);
 		}
        
        Pair<SimCharacter,SimCharacter> initCharacterData = new Pair<SimCharacter,SimCharacter>(mySimCharacter, oppSimCharacter);  
        LinkedList<Task> linkedTasks = new LinkedList<Task>();

        //search for a plan
		this.goalTask.Decompose(initCharacterData, linkedTasks);

		if(this.plan.size() == 0)
		{
			if(Helper.DEBUG_ACTION_EXECUTION)
			{
				System.out.println("No plan could be found");
			}
		}
		else
		{
			String pl = "new plan: ";
			for(Iterator<PrimitiveTask> i = this.plan.iterator() ; i.hasNext() ; )
			{
				pl += i.next().myAction.name();
				pl+= ", ";
			}
			if(Helper.DEBUG_ACTION_EXECUTION)
			{
				System.out.println(pl);
			}
		}
		return this.plan;
	}
	
	public abstract Method SelectNextMethod(CompoundTask cTask, Pair<SimCharacter, SimCharacter> currentSimCharacters, int nextIndex, int[] methodsTried);
	
	public abstract Method SelectNextMethodUCB(CompoundTask cTask, Pair<SimCharacter, SimCharacter> currentSimCharacters, int nextIndex, int[] methodsTried);

	
	public int GetCurrentPlanLength()
	{
		return Planner.INSTANCE.plan.size();
	}

	public void AddPlanStep(PrimitiveTask task)
	{
		if(Helper.DEBUG_PLAN)
		{
			System.out.println("+++ Task " + task.name +" added to the plan.");
		}
		Planner.INSTANCE.plan.addLast(task);
	}
	
	public void RemoveLastPlanStep()
	{
		PrimitiveTask removed = Planner.INSTANCE.plan.removeLast();
		if(Helper.DEBUG_PLAN)
		{
			System.out.println("Task " + removed.name + " removed from the plan.");
		}
	}
	
	public Vector<MotionData> GetMotionData(boolean my)
	{
		Vector<MotionData> motionData;

		// copy
		if(player){
			if(my)
			{
				//motionData = gameData.getPlayerOneMotion();
				motionData = new Vector<MotionData>(gameData.getMotionData(true));
			}
			else
			{
				//motionData = gameData.getPlayerTwoMotion();
				motionData = new Vector<MotionData>(gameData.getMotionData(false));
			}
		}else{
			if(my)
			{
				//motionData = gameData.getPlayerTwoMotion();
				motionData = new Vector<MotionData>(gameData.getMotionData(false));
			}
			else
			{
				//motionData = gameData.getPlayerOneMotion();
				motionData = new Vector<MotionData>(gameData.getMotionData(true));
			}
		}
		return motionData;
	}

	public GameData GetGameData()
	{
		return this.gameData;
	}
	
	public void RegisterAttacks()
	{
		this.lowAttacks = new ArrayList<Method>();
		this.middleAttacks = new ArrayList<Method>();
		this.highAttacks = new ArrayList<Method>();
		this.throwAttacks = new ArrayList<Method>();
		this.projectileAttacks = new ArrayList<Method>();
		this.knockdownAttacks = new ArrayList<Method>();
		this.quickAttacks = new ArrayList<Method>();
		this.slidingAttacks = new ArrayList<Method>();
		this.knockbackAttacks = new ArrayList<Method>();
		this.airAttacksBackwards = new ArrayList<Method>();
		this.airAttacksForewards = new ArrayList<Method>();
		
		this.lowAttacks_sorted = new ArrayList<Method>();
		this.middleAttacks_sorted = new ArrayList<Method>();
		this.highAttacks_sorted = new ArrayList<Method>();
		this.throwAttacks_sorted = new ArrayList<Method>();
		this.projectileAttacks_sorted = new ArrayList<Method>();
		this.knockdownAttacks_sorted = new ArrayList<Method>();
		this.quickAttacks_sorted = new ArrayList<Method>();
		this.slidingAttacks_sorted = new ArrayList<Method>();
		this.knockbackAttacks_sorted = new ArrayList<Method>();
		
		this.lowAttacksA = new ArrayList<Action>();
		this.middleAttacksA = new ArrayList<Action>();
		this.highAttacksA = new ArrayList<Action>();
		this.throwAttacksA = new ArrayList<Action>();
		this.projectileAttacksA = new ArrayList<Action>();
		this.knockdownAttacksA = new ArrayList<Action>();
		this.quickAttacksA = new ArrayList<Action>();
		this.slidingAttacksA = new ArrayList<Action>();
		this.knockbackAttacksA = new ArrayList<Action>();
		
		this.attacks = new PrimitiveTask[]{ new p_THROW_A(), new p_THROW_B(), new p_STAND_A() , 
				new p_STAND_B(), new p_CROUCH_A(), new p_CROUCH_B(), new p_STAND_FA(), new p_STAND_FB(), 
				new p_CROUCH_FA(), new p_CROUCH_FB(), new p_STAND_D_DF_FA(), new p_STAND_D_DF_FB(), new p_STAND_D_DF_FC(),
				new p_STAND_F_D_DFA(), new p_STAND_F_D_DFB(), new p_STAND_D_DB_BA(), new p_STAND_D_DB_BB(), new p_AIR_A(), 
				new p_AIR_B(), new p_AIR_DA(), new p_AIR_DB(), new p_AIR_FA(), new p_AIR_FB(),
				new p_AIR_UA(), new p_AIR_UB(), new p_AIR_D_DF_FA(), new p_AIR_D_DF_FB(), new p_AIR_F_D_DFA(), 
				new p_AIR_F_D_DFB(), new p_AIR_D_DB_BA(), new p_AIR_D_DB_BB()};
		
		Vector<MotionData> motionData = this.GetMotionData(true);
		
		for(int i = 0; i < this.attacks.length; ++i)
		{

			Class c = null;
			try {
				c = Class.forName("HTNPlanner.Methods.Primitive.m_" + this.attacks[i].myAction.name());
			} catch (ClassNotFoundException e) {
				System.out.println("class not found m_" + this.attacks[i].myAction.name());
				continue;
			}
			
			MotionData mData = motionData.elementAt(this.attacks[i].myAction.ordinal());
			int attackType =  mData.attackType;			
					
			if(mData.attackDownProp)
			{
				try 
				{
					this.knockdownAttacks.add((Method) c.newInstance());
					this.InsertSorted(this.knockdownAttacksA, this.attacks[i].myAction);
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added knock-down attack m_" + this.attacks[i].myAction.name());
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				};
			}
			
			if(mData.attackImpactX > 0)
			{
				try 
				{
					this.knockbackAttacks.add((Method) c.newInstance());
					
					this.InsertSorted(this.knockbackAttacksA, this.attacks[i].myAction);

					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added knock-back attack m_" + this.attacks[i].myAction.name());
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				};
			}
			
			if(mData.getAttackActive() >= Helper.PROJECTILE_ACTIVE_FRAMES)
			{
				try 
				{
					this.projectileAttacks.add((Method) c.newInstance());
				
					this.InsertSorted(this.projectileAttacksA, this.attacks[i].myAction);
					
					if(mData.getAttackStartAddEnergy() == 0)
					{
						this.freeProjectileAvailable = true;
					}
					
					
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added projectile attack m_" + this.attacks[i].myAction.name());
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				};
			}
			
			
			if(mData.attackStartUp <= 10)
			{
				try 
				{
					this.quickAttacks.add((Method) c.newInstance());
					this.InsertSorted(this.quickAttacksA, this.attacks[i].myAction);
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added quick attack m_" + this.attacks[i].myAction.name());
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				};
			}
			
			if(mData.state.equals(State.AIR) && mData.speedY < 0)
			{
				try 
				{
					this.airAttacksForewards.add((Method) c.newInstance());
					//this.InsertSorted(this.quickAttacksA, this.attacks[i].myAction);
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added air attack forewards m_" + this.attacks[i].myAction.name());
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				};
				
			}
			
			if(mData.attackDownProp && mData.attackImpactX > 0 && mData.speedX > 0 && mData.speedY==0 
					&& !mData.state.equals(State.AIR))
			{
				try 
				{
					this.slidingAttacks.add((Method) c.newInstance());
					this.InsertSorted(this.slidingAttacksA, this.attacks[i].myAction);
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added sliding attack m_" + this.attacks[i].myAction.name());
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				};
			}			switch (attackType)
			{
				case 1 : 

					try {
						this.highAttacks.add((Method) c.newInstance());
						this.InsertSorted(this.highAttacksA, this.attacks[i].myAction);

						if(Helper.DEBUG_METHOD_CREATION)
						{
							System.out.println("added high attack m_" + this.attacks[i].myAction.name());
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					};
					break;
				
				case 2: 
					try {
						this.middleAttacks.add((Method) c.newInstance());
						this.InsertSorted(this.middleAttacksA, this.attacks[i].myAction);

						if(Helper.DEBUG_METHOD_CREATION)
						{
							System.out.println("added middle attack m_" + this.attacks[i].myAction.name());
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					};
					break;
					
				case 3: 
					try {
						this.lowAttacks.add((Method) c.newInstance());
						this.InsertSorted(this.lowAttacksA, this.attacks[i].myAction);
						if(Helper.DEBUG_METHOD_CREATION)
						{
							System.out.println("added low attack m_" + this.attacks[i].myAction.name());
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					};
					break;
				
				case 4: 
					try {
						this.throwAttacks.add((Method) c.newInstance());
						this.InsertSorted(this.throwAttacksA, this.attacks[i].myAction);
						
						if(Helper.DEBUG_METHOD_CREATION)
						{
							System.out.println("added throw attack m_" + this.attacks[i].myAction.name());
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					};
					break;	
			}
		}
		
		this.ConvertIntoMethods(highAttacksA, highAttacks_sorted);
		this.ConvertIntoMethods(knockdownAttacksA, knockdownAttacks_sorted);
		this.ConvertIntoMethods(lowAttacksA, lowAttacks_sorted);
		this.ConvertIntoMethods(middleAttacksA, middleAttacks_sorted);
		this.ConvertIntoMethods(projectileAttacksA, projectileAttacks_sorted);
		this.ConvertIntoMethods(throwAttacksA, throwAttacks_sorted);
		this.ConvertIntoMethods(quickAttacksA, quickAttacks_sorted);
		this.ConvertIntoMethods(knockbackAttacksA, knockbackAttacks_sorted);
	}
	
	public ArrayList<Method> GetLowAttacks()
	{
		return this.lowAttacks;
	}
	
	public ArrayList<Method> GetMiddleAttacks()
	{
		return this.middleAttacks;
	}
	
	public ArrayList<Method> GetHighAttacks()
	{
		return this.highAttacks;
	}
	
	public ArrayList<Method> GetThrowAttacks()
	{
		return this.throwAttacks;
	}
	
	public ArrayList<Method> GetProjectileAttacks()
	{
		return this.projectileAttacks;
	}
	
	public ArrayList<Method> GetKnockDownAttacks()
	{
		return this.knockdownAttacks;
	}
	
	public ArrayList<Method> GetKnockBackAttacks()
	{
		return this.knockbackAttacks;
	}
	
	public ArrayList<Method> GetQuickAttacks()
	{
		return this.quickAttacks;
	}
	
	public ArrayList<Method> GetSlidingAttacks()
	{
		return this.slidingAttacks;
	}

	public ArrayList<Method> GetLowAttacks_sorted()
	{
		return this.lowAttacks_sorted;
	}
	
	public ArrayList<Method> GetMiddleAttacks_sorted()
	{
		return this.middleAttacks_sorted;
	}
	
	public ArrayList<Method> GetHighAttacks_sorted()
	{
		return this.highAttacks_sorted;
	}
	
	public ArrayList<Method> GetThrowAttacks_sorted()
	{
		return this.throwAttacks_sorted;
	}
	
	public ArrayList<Method> GetProjectileAttacks_sorted()
	{
		return this.projectileAttacks_sorted;
	}
	
	public ArrayList<Method> GetKnockDownAttacks_sorted()
	{
		return this.knockdownAttacks_sorted;
	}
	
	public ArrayList<Method> GetKnockBackAttacks_sorted()
	{
		return this.knockbackAttacks_sorted;
	}
	
	public ArrayList<Method> GetQuickAttacks_sorted()
	{
		return this.quickAttacks_sorted;
	}
	
	public ArrayList<Method> GetSlidingAttacks_sorted()
	{
		return this.slidingAttacks_sorted;
	}

	public ArrayList<Method> GetAirAttacksForewards()
	{
		return this.airAttacksForewards;
	}
	
	public ArrayList<Method> GetAirAttacksBackwards()
	{
		return this.airAttacksBackwards;
	}
	
	public Deque<mizunoAI_simulator.SimAttack> GetSimAttack()
	{
		return this.simAttack;
	}

	private void InsertSorted(ArrayList<Action> list, Action action)
	{
		if(list.isEmpty())
		{
			list.add(action);
			return;
		}
		
		int damage = this.GetMotionData(true).elementAt(action.ordinal()).attackHitDamage;
		
		int min = this.GetMotionData(true).elementAt( list.get(list.size()-1).ordinal()).attackHitDamage;
		int max = this.GetMotionData(true).elementAt( list.get(0).ordinal()).attackHitDamage;
		
		if(damage > max)
		{
			list.add(0, action);
			return;
		}
		
		if(damage <= min)
		{
			list.add(action);
			return;
		}
		
		for(int i=0; i< list.size(); ++i)
		{
			int currDamage = this.GetMotionData(true).elementAt( list.get(i).ordinal()).attackHitDamage;
			if(damage >= currDamage)
			{
				list.add(i, action);
				return;
			}
		}		
	}
	
	private void InsertSortedByRange(ArrayList<Action> list, Action action)
	{
		if(list.isEmpty())
		{
			list.add(action);
			return;
		}
		
		int range = this.GetMotionData(true).elementAt(action.ordinal()).getAttackHitArea().getRight();
		
		int min = this.GetMotionData(true).elementAt( list.get(list.size()-1).ordinal()).getAttackHitArea().getRight();
		int max = this.GetMotionData(true).elementAt( list.get(0).ordinal()).getAttackHitArea().getRight();
		
		if(range > max)
		{
			list.add(0, action);
			return;
		}
		
		if(range <= min)
		{
			list.add(action);
			return;
		}
		
		for(int i=0; i< list.size(); ++i)
		{
			int currRange = this.GetMotionData(true).elementAt( list.get(i).ordinal()).getAttackHitArea().getRight();
			if(range >= currRange)
			{
				list.add(i, action);
				return;
			}
		}		
	}
	
	private void ConvertIntoMethods(ArrayList<Action> actions, ArrayList<Method> methods)
	{
		if(actions.isEmpty())
		{
			System.out.println("no actions");
			return;
		}
		
		for(int i=0; i< actions.size(); ++i)
		{
			int currDamage = this.GetMotionData(true).elementAt( actions.get(i).ordinal()).attackHitDamage;

			Class c = null;
			try {
				c = Class.forName("HTNPlanner.Methods.Primitive.m_" + actions.get(i).name());
			} 
			catch (ClassNotFoundException e) {
				System.out.println("class not found m_" + actions.get(i).name());
				continue;
			};
			try 
			{
				methods.add((Method) c.newInstance());

				if(this.gameData.getCharacterName(this.player).equals("GARNET"))
				{
					int currRange = this.GetMotionData(true).elementAt(actions.get(i).ordinal()).getAttackHitArea().getRight();
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added attack m_" + actions.get(i).name() + " with range " + currRange);
					}
				}
				else
				{
					if(Helper.DEBUG_METHOD_CREATION)
					{
						System.out.println("added attack m_" + actions.get(i).name() + " with damage " + currDamage);
					}

				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			};
		}
	}
}
