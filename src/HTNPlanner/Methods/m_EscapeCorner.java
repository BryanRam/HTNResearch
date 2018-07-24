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

public class m_EscapeCorner extends Method 
{
	public m_EscapeCorner()
	{
		super();
		
		this.name = "m_EscapeCorner";
		this.tasksToDecompose.add(new c_EscapeCorner());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
	
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		if(dist >300)
		{
			holds = false;
		}
		//avoid being in corners
		if( (currentSimCharacters.m_a.getLeft()>60 && currentSimCharacters.m_a.isFront()) || (currentSimCharacters.m_a.getRight() < Planner.INSTANCE.GetGameData().getStageWidth()-60 && !currentSimCharacters.m_a.isFront()))
		{
			holds = false;			
		}
	
		return holds;
	}
}
