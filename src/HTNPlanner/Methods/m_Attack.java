/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.Action;
import enumerate.AttackAction;
import mizunoAI_simulator.SimCharacter;
import aiinterface.CommandCenter;
import struct.MotionData;
import struct.FrameData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Attack extends Method 
{
	private CommandCenter commandCenter;
	private FrameData frameData;
	//private final int energyCost = 30;
	//private final int trappedDistance = 200;
	private final int closeDistance = 175;
	private int farDistance = 500;
	
	public m_Attack()
	{
		super();
		
		this.name = "m_Attack";
		this.tasksToDecompose.add(new c_Attack());
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		
		boolean holds = true;
		
		int distance=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(), 
				 currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		
		if (distance >= farDistance)
			holds = false;
		
		// Calculate distance between characters and action according to
		// the distance
//		int distance=Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(), 
//				 currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
//		
//		if (distance <= closeDistance)
//			holds = false;
//		else if (distance > closeDistance && distance <= farDistance)
//			// attack
//			holds = true;
//		else
//			holds = false;
		
		return holds;
	}
}
