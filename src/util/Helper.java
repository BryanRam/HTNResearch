/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package util;

import enumerate.Action;
import enumerate.State;
import HTNPlanner.Planner;
import mizunoAI_simulator.SimCharacter;
import struct.CharacterData;
import struct.HitArea;
import struct.MotionData;

public class Helper 
{
	public static boolean DEBUG_METHOD_DECOMPOSITION = false;
	
	public static boolean DEBUG_HIT_INTERSECTION = false;
	
	public static boolean DEBUG_PLAN = false;
	
	public static boolean DEBUG_ACTION_HISTORY = false;
	
	public static boolean DEBUG_ACTION_EXECUTION = true;
	
	public static boolean DEBUG_PRECONDITIONS = false;
	
	public static boolean DEBUG_TIME_OUT = false;
	
	public static boolean DEBUG_UCB_STATICTICS = true;
	
	public static boolean DEBUG_METHOD_CREATION = true;
	
	
	public static int PROJECTILE_ACTIVE_FRAMES = 65;
	
	public static boolean SIMULATE_PROJECTILES = false;
	
	public static boolean REWARD_FROM_EXECUTION = true;
	
	public static boolean SUCCESS_AS_REWARD = false;
	
	public static boolean DECREASE_C = false;
	
	public static boolean LEARN_UCB = false;
	
	public static boolean BoxesIntersect(HitArea hit, SimCharacter me, SimCharacter other)
	{
		boolean intersects = false;
		
		int hitTopAbs = hit.getTop() + me.getY();
		int hitBottomAbs = hit.getBottom() + me.getY();
		int hitLeftAbs = hit.getLeft() + me.getX();
		int hitRightAbs = hit.getRight() + me.getX();
		
		if(Helper.DEBUG_HIT_INTERSECTION)
		{
			System.out.println("my x y " + me.getX() + " " + me.getY() );
			System.out.println("my box t b l r: " + me.getTop() + " " + me.getBottom() + " " + me.getLeft() + " " + me.getRight());
			System.out.println("i'm facing " + (me.isFront()? "right" : "left"));

		}
		
		//left border inbetween - check height
		if((other.getLeft() >=  hitLeftAbs && other.getLeft() <= hitRightAbs)
				&& (   (other.getTop() >=  hitTopAbs && other.getTop() <= hitBottomAbs)
					|| (other.getBottom() >=  hitTopAbs && other.getBottom() <= hitBottomAbs)
					|| (other.getBottom() >=  hitBottomAbs && other.getTop() <= hitTopAbs)
					|| (other.getBottom() <=  hitBottomAbs && other.getTop() >= hitTopAbs)))
		{
			return true;
		}
		
		
		//right border inbetween - check height
		if((other.getRight() >=  hitLeftAbs && other.getRight() <= hitRightAbs)
				&& (   (other.getTop() >=  hitTopAbs && other.getTop() <= hitBottomAbs)
					|| (other.getBottom() >=  hitTopAbs && other.getBottom() <= hitBottomAbs)
					|| (other.getBottom() >=  hitBottomAbs && other.getTop() <= hitTopAbs)
					|| (other.getBottom() <=  hitBottomAbs && other.getTop() >= hitTopAbs)))
		{
			return true;
		}
		
		
		//around - check height
		if((other.getLeft() <= hitLeftAbs && other.getRight() >=  hitRightAbs)
				&& (   (other.getTop() >=  hitTopAbs && other.getTop() <= hitBottomAbs)
					|| (other.getBottom() >=  hitTopAbs && other.getBottom() <= hitBottomAbs)
					|| (other.getBottom() >=  hitBottomAbs && other.getTop() <= hitTopAbs)
					|| (other.getBottom() <=  hitBottomAbs && other.getTop() >= hitTopAbs)))
		{
			return true;
		}
		
		//inside - check height
		if((other.getLeft() >= hitLeftAbs && other.getRight() <=  hitRightAbs)
				&& (   (other.getTop() >=  hitTopAbs && other.getTop() <= hitBottomAbs)
					|| (other.getBottom() >=  hitTopAbs && other.getBottom() <= hitBottomAbs)
					|| (other.getBottom() >=  hitBottomAbs && other.getTop() <= hitTopAbs)
					|| (other.getBottom() <=  hitBottomAbs && other.getTop() >= hitTopAbs)))
		{
			return true;
		}
		if(Helper.DEBUG_HIT_INTERSECTION)
		{	
			System.out.println("doesnt intersect " );
			System.out.println("oth box t b l r: " + other.getTop() + " " + other.getBottom() + " " + other.getLeft() + " " + other.getRight());
			System.out.println("hit box t b l r: " + hitTopAbs + " " + hitBottomAbs + " " + hitLeftAbs + " " + hitRightAbs);
		}
		return intersects;
	}

	public static boolean EnoughSpaceForward(SimCharacter currentSimCharacter, SimCharacter otherSimCharacter, int speed)
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
	
	public static boolean EnoughSpaceBackward(SimCharacter currentSimCharacter, int speed)
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

	/** 
	 * @param action
	 * @param my
	 * @param opp
	 * @return -1 if backwards, 1 if forwards
	 */
	public static int MovementNeededForAction(MotionData md, SimCharacter me, SimCharacter other)
	{
		HitArea hit = md.getAttackHitArea();
		int hitTopAbs = hit.getTop() + me.getY();
		int hitBottomAbs = hit.getBottom() + me.getY();
		int hitLeftAbs = hit.getLeft() + me.getX();
		int hitRightAbs = hit.getRight() + me.getX();
		

		if(Helper.DEBUG_HIT_INTERSECTION)
		{
			System.out.println("my box t b l r: " + me.getTop() + " " + me.getBottom() + " " + me.getLeft() + " " + me.getRight());
		}


		if(me.isFront())
		{
			
			//need to move forwards
			if(other.getLeft() >=  hitRightAbs)
			{	
				if(Helper.DEBUG_HIT_INTERSECTION)
				{
					System.out.println("!front: movement needed forwards " );
					System.out.println("oth box t b l r: " + other.getTop() + " " + other.getBottom() + " " + other.getLeft() + " " + other.getRight());
					System.out.println("hit box t b l r: " + hitTopAbs + " " + hitBottomAbs + " " + hitLeftAbs + " " + hitRightAbs);
				}
				
				return 1;
			}
			if(other.getRight() <= hitLeftAbs)
			{
				if(Helper.DEBUG_HIT_INTERSECTION)
				{
					System.out.println("!front: movement needed backwards " );
					System.out.println("oth box t b l r: " + other.getTop() + " " + other.getBottom() + " " + other.getLeft() + " " + other.getRight());
					System.out.println("hit box t b l r: " + hitTopAbs + " " + hitBottomAbs + " " + hitLeftAbs + " " + hitRightAbs);
				}
				return -1;
			}
		}
		else if(!me.isFront())
		{
			//need to move forwards
			if(other.getRight() <=  hitLeftAbs)
			{	
				if(Helper.DEBUG_HIT_INTERSECTION)
				{
					System.out.println("!front: movement needed forwards " );
					System.out.println("oth box t b l r: " + other.getTop() + " " + other.getBottom() + " " + other.getLeft() + " " + other.getRight());
					System.out.println("hit box t b l r: " + hitTopAbs + " " + hitBottomAbs + " " + hitLeftAbs + " " + hitRightAbs);
				}
				return 1;
			}
			if(other.getLeft() >= hitRightAbs)
			{
				if(Helper.DEBUG_HIT_INTERSECTION)
				{
					System.out.println("!front: movement needed backwardswards " );
					System.out.println("oth box t b l r: " + other.getTop() + " " + other.getBottom() + " " + other.getLeft() + " " + other.getRight());
					System.out.println("hit box t b l r: " + hitTopAbs + " " + hitBottomAbs + " " + hitLeftAbs + " " + hitRightAbs);
				}
				return -1;
			}
		}
		return 0;
	}
	
	public static int MovementNeededForAction(MotionData md, CharacterData me, CharacterData other)
	{
		//HitArea hit = md.getHit(); 
		HitArea hit = md.getAttackHitArea();
		int hitTopAbs = hit.getTop() + me.getY();
		int hitBottomAbs = hit.getBottom() + me.getY();
		int hitLeftAbs = hit.getLeft() + me.getX();
		int hitRightAbs = hit.getRight() + me.getX();
		
		if(me.isFront())
		{

			//need to move forwards
			if(other.getLeft() >=  hitRightAbs)
			{	
				return 1;
			}	
			//need to move backwards
			if(other.getRight() <= hitLeftAbs)
			{
				return -1;
			}
		}
		else if(!me.isFront())
		{

			//need to move forwards
			if(other.getRight() <=  hitLeftAbs)
			{	
				return 1;
			}
			//need to move backwards
			if(other.getLeft() >= hitRightAbs)
			{
				return -1;
			}
		}
		return 0;
	}

	public static int DistanceBetweenBoxes(int leftA, int rightA, int leftB, int rightB)
	{
		int dist = Math.min(Math.abs(leftA-rightB)  , Math.abs(leftB-rightA));
		return dist;
	}

	public static boolean ProjectileIntersects(SimCharacter me, SimCharacter other, int speed, int startUp)
	{
		boolean intersects = false;
		int dist=Helper.DistanceBetweenBoxes(me.getLeft(), me.getRight(),other.getLeft(), other.getRight());
		int framesTotal = (int)(dist/speed);
		
		//can be hit after 50 frames 
		//rising
		if(other.getSpeedY() < 0)
		{
			//first fourth - needs > 35-49(45-59)fr
			if(other.getBottom() > 367)
			{
				if( Math.abs((353 - speed*40)- dist)<=20 )
				{		
					intersects = true;
				}
			}
			//second fourth - needs 20-35(30-45)fr
			else
			{
				if( Math.abs((270 - speed*27) - dist) <=20 )
				{
					intersects = true;
				}
			}
		}
		else if(other.getSpeedY() > 0)
		{
			//third fourth - needs 5-20(15-30)fr
			if(other.getBottom() <= 442)
			{
				if( Math.abs(( 180 + speed)-dist)<=20 )
				{
					intersects = true;
				}
			}
			//fourth fourth - needs <5(15)fr
			else
			{
				if( Math.abs(100-dist)<=20)
				{
					intersects = true;
				}
			}
		}	
		return intersects;
	}
}
