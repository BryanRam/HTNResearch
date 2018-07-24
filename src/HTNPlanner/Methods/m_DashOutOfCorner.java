/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Deque;
import java.util.Iterator;
import java.util.Vector;

import enumerate.Action;
import enumerate.AttackAction;
import enumerate.State;
import mizunoAI_simulator.SimAttack;
import mizunoAI_simulator.SimCharacter;
import struct.HitArea;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_DashOutOfCorner extends Method 
{
	public m_DashOutOfCorner()
	{
		super();
		
		this.name = "m_DashOutOfCorner";
		this.tasksToDecompose.add(new p_DASH());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		//for speedrunning league always approach opponent
		if(currentSimCharacters.m_a.getHp() > 1000)
		{
			return false;
		}
		
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		if(dist >280)
		{
			holds = false;
		}
		
		if(!currentSimCharacters.m_b.getState().equals(State.AIR) || currentSimCharacters.m_b.getBottom() > 600)
		{
			holds = false;
		}
		//avoid being in corners
		if( (currentSimCharacters.m_a.getLeft()>40 && currentSimCharacters.m_a.isFront()) || (currentSimCharacters.m_a.getRight() < Planner.INSTANCE.GetGameData().getStageWidth()-40 && !currentSimCharacters.m_a.isFront()))
		{
			holds = false;			
		}
	
		return holds;
	}
}
