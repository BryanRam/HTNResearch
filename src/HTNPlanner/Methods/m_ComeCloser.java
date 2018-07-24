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
import aiinterface.AIInterface;
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

public class m_ComeCloser extends Method 
{
	public m_ComeCloser()
	{
		super();
		
		this.name = "m_ComeCloser";
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		boolean holds = true;
		
		if(!Planner.INSTANCE.GetGameData().getCharacterName(true).equals("GARNET"))
		{
			return false;
		}
		
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		
		this.tasksToDecompose.clear();
		if(dist > 600)
		{
			this.tasksToDecompose.add(new p_DASH());
		}
		else if(dist > 100)
		{
			this.tasksToDecompose.add(new p_AIR_D_DF_FA());
		}	
		else
		{
			this.tasksToDecompose.add(new p_BACK_STEP());
		}
			
		

		return holds;
	}
}
