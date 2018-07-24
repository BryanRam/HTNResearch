/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner;

import java.util.LinkedList;
import java.util.Vector;

import HTNPlanner.CompoundTasks.c_Act;
import enumerate.Action;
import enumerate.State;
import aiinterface.AIInterface;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;

public abstract class PrimitiveTask extends Task
{

	public Action myAction;
	protected Pair<SimCharacter,SimCharacter> newCharacterData;
	
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters)
	{
		boolean holds = true;
		Vector<MotionData> motionData = Planner.INSTANCE.GetMotionData(true);
		MotionData mData = motionData.elementAt(this.myAction.ordinal());

		//check whether we have enough energy to perform the action
		int enRequired = mData.getAttackStartAddEnergy()*-1; 
		if(currentSimCharacters.m_a.getEnergy() < enRequired)
		{
			return false;
		}
		
		
		if(mData.attackHitDamage <= 0)
		{
			return true;
		}
		

		//>>>>>>>>>>the following checks are for attack actions - other tasks should override this method<<<<
		
		//check hit box only for non-projectile attacks
		if(mData.getAttackActive() < Helper.PROJECTILE_ACTIVE_FRAMES)
		{		
			if(!Helper.BoxesIntersect( mData.getAttackHitArea(), currentSimCharacters.m_a, currentSimCharacters.m_b))
			{
				//if this is an air attack it might be executed for "movement" in air
				if(!mData.state.equals(State.AIR) ||  mData.speedX == 0 || !Planner.INSTANCE.moveAwayInAir)
				{
					holds = false;
				}
				if(Planner.INSTANCE.projectileAboveMe && !currentSimCharacters.m_a.getState().equals(State.AIR))
				{
					holds = false;
					if(Helper.DEBUG_PRECONDITIONS)
					{
						System.out.println("projectile above me - no air projectile attack!");
					}
				}
			}
		}
		//projectile attack
		else
		{
			int diffH=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(), 
					 currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
			
			boolean in = Helper.ProjectileIntersects(currentSimCharacters.m_a, currentSimCharacters.m_b, 
					mData.attackSpeedX, mData.attackStartUp);
			
			if(in)
			{
				if(Helper.DEBUG_PRECONDITIONS)
				{
					System.out.println(this.myAction.name()+ " while opp is jumping");
				}
				return true;
			}
			else
			{
				//facing right
				if(currentSimCharacters.m_a.isFront())
				{
					if(currentSimCharacters.m_b.getX() <= currentSimCharacters.m_a.getX() && !Planner.INSTANCE.moveAwayInAir)
					{
						holds = false;
						if(Helper.DEBUG_PRECONDITIONS)
						{
							System.out.println("I'm not facing the opponent - no projectile attack");
						}
					}
				}
				//facing left
				else
				{
					if(currentSimCharacters.m_b.getX() >= currentSimCharacters.m_a.getX() && !Planner.INSTANCE.moveAwayInAir)
					{
						holds = false;
						if(Helper.DEBUG_PRECONDITIONS)
						{
							System.out.println("I'm not facing the opponent - no projectile attack");
						}
					}
				}
				
				int diffV=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getTop(), currentSimCharacters.m_a.getBottom(), 
						 currentSimCharacters.m_b.getBottom(), currentSimCharacters.m_b.getTop());
				//we are on different heights
				if(mData.state==State.AIR )
				{
					if(currentSimCharacters.m_a.getState().equals(State.AIR))
					{
						holds = false;
						if(Helper.DEBUG_PRECONDITIONS)
						{
							System.out.println("I'm in air - cannot shoot air projectile");
						}
					}
					else
					{
						if(Planner.INSTANCE.projectileAboveMe)
						{
							holds = false;
							if(Helper.DEBUG_PRECONDITIONS)
							{
								System.out.println("projectile above me - no air projectile attack!");
							}
						}
						//oopp is landing close
						if(currentSimCharacters.m_b.getSpeedY()>0 && diffH < 100)
						{
							holds = false;
							if(Helper.DEBUG_PRECONDITIONS)
							{
								System.out.println("opp landing close - no air projectile attack!");
							}
						}
					}
					if(currentSimCharacters.m_b.getBottom()>=600)
					{
						if(Helper.DEBUG_PRECONDITIONS)
						{
							System.out.println("opponent is not in air, no projectile attack " + this.name);
						}
						holds = false;
					}
				}
				else 
				{
					//holds = false;
					
					if(diffV >= 150)
					{
						if(Helper.DEBUG_PRECONDITIONS)
						{
							System.out.println("we are on different heights, no projectile attack " + this.name);
						}
						holds = false;
					}
				}
				
				
				if(diffH > mData.getAttackActive() * mData.attackSpeedX)
				{
					if(Helper.DEBUG_PRECONDITIONS)
					{
						System.out.println("too far for projectile attack " + this.name );
					}
					holds = false;
				}
				
				if(diffH < 320 && !currentSimCharacters.m_b.getState().equals(State.AIR)
						&& Planner.INSTANCE.GetGameData().getCharacterName(true).equals("ZEN"))
				{
					if(Helper.DEBUG_PRECONDITIONS)
					{
						System.out.println("ZEN: opp too close to shoot");
					}
					holds = false;
				}
				if(diffH > 300 && !currentSimCharacters.m_b.getState().equals(State.AIR)
						&& Planner.INSTANCE.GetGameData().getCharacterName(true).equals("GARNET"))
				{
					if(Helper.DEBUG_PRECONDITIONS)
					{
						System.out.println("ZEN: opp too far to shoot");
					}
					holds = false;
				}
			}
		}
		return holds;
	}

	public Pair<SimCharacter,SimCharacter> Decompose(Pair<SimCharacter, SimCharacter> prevSimCharacters, LinkedList<Task> prevTasks) 
	{
		this.linkedTasks = (LinkedList<Task>) prevTasks.clone();
		this.linkedTasks.add(this);
		
		if(!this.CheckPreconditions(prevSimCharacters))
		{
			if(Helper.DEBUG_METHOD_DECOMPOSITION)
			{
				System.out.println("Prim. task " + this.name + " could not be applied");
			}
			return null;
		}
		return Simulate(prevSimCharacters);
	}
	
	public Pair<SimCharacter, SimCharacter> Simulate(Pair<SimCharacter, SimCharacter> prevSimCharacters) 
	{
		if(this.myAction == Action.NEUTRAL)
		{	
			return prevSimCharacters;
		}
		
		SimCharacter mySimCharacter = prevSimCharacters.m_a.clone();
        SimCharacter oppSimCharacter = prevSimCharacters.m_b.clone(); 
        
        Pair<SimCharacter,SimCharacter> toChangeCharacterData = new Pair<SimCharacter,SimCharacter>(mySimCharacter, oppSimCharacter);
        
        //get the predicted opp action for simulation and precond. checking
        Action oppAction = Action.STAND;
        if(!Planner.INSTANCE.nextOppAct.isEmpty())
        {
        	oppAction = Planner.INSTANCE.nextOppAct.getFirst();
        }
     
        Planner.INSTANCE.AddPlanStep(this);
        
        //do not simulate projectiles
        int damage = Planner.INSTANCE.GetMotionData(true).elementAt(myAction.ordinal()).attackHitDamage;
        
		if(Planner.INSTANCE.GetMotionData(true).elementAt(this.myAction.ordinal()).getAttackActive() >= Helper.PROJECTILE_ACTIVE_FRAMES)
		{
	    	if(!Helper.REWARD_FROM_EXECUTION)
			{
    			this.IncreaseSuccessStatistics(damage);
			}
	        return toChangeCharacterData;
		}
		
        long currTime = System.currentTimeMillis();
		if(currTime - Planner.INSTANCE.time >= 16.6)
		{
			if(Helper.DEBUG_TIME_OUT)
			{
				System.out.println( " Task " + this.name + " : no computation time left to simulate action.>>>>>>>>>>>>>");
			}
			
			if(!Helper.REWARD_FROM_EXECUTION)
			{
	        	this.IncreaseSuccessStatistics(damage);
			}
			return toChangeCharacterData;
		}	
		
        int hpDiff = Planner.INSTANCE.simulator.simulate(myAction, oppAction, mySimCharacter, oppSimCharacter, Planner.INSTANCE.GetSimAttack());     
   
        if(!Helper.REWARD_FROM_EXECUTION)
		{
        	this.IncreaseSuccessStatistics(damage);
       // this.IncreaseSuccessStatistics(hpDiff);
		}
		return toChangeCharacterData;
	}

	
	@Override
	public void IncreaseSuccessStatistics(float currentReward)
	{
		float totalBonus = 0;
		//size()-1 because we increase for this one afterwards
		for(int i = this.linkedTasks.size()-2; i>=0; --i)
		{
			float bonus = i < this.linkedTasks.size()-2 ? this.linkedTasks.get(i+1).GetBonus() : 0;
			
			totalBonus+= bonus;
			this.linkedTasks.get(i).AddBonus(totalBonus);
			this.linkedTasks.get(i).IncreaseSuccessStatistics(currentReward);
			if(Helper.REWARD_FROM_EXECUTION)
			{
				this.linkedTasks.get(i).FlushMethodRewards();
			}
		}

		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Prim. task " + this.name + " curr reward = " + currentReward);
		}
	}
	
	public boolean EnoughSpaceForward(SimCharacter currentSimCharacter, SimCharacter otherSimCharacter, int speed)
	{
		//facing left
		if(!currentSimCharacter.isFront() && currentSimCharacter.getLeft() < speed)
		{
			return false;
		}
		
		//facing right
		if(currentSimCharacter.isFront() && currentSimCharacter.getRight() > Planner.INSTANCE.GetGameData().getStageWidth() - speed)
		{
			return false;
		}
		
		//facing left
		if(!currentSimCharacter.isFront() && currentSimCharacter.getLeft() < otherSimCharacter.getRight()+speed)
		{
			return false; 
		}
		
		//facing right
		if(currentSimCharacter.isFront() && currentSimCharacter.getRight()  > otherSimCharacter.getLeft() - speed)
		{
			return false;  
		}
		
		return true;
	}
	
	protected boolean EnoughSpaceBackward(SimCharacter currentSimCharacter, int speed)
	{
		//facing right
		if(currentSimCharacter.isFront() && currentSimCharacter.getLeft() < - speed)
		{
			return false;
		}
		
		//facing left
		if(!currentSimCharacter.isFront() && currentSimCharacter.getRight() > Planner.INSTANCE.GetGameData().getStageWidth() + speed)
		{
			return false;
		}
		
		return true;
	}

	@Override
	protected void FlushMethodRewards()
	{}
}
