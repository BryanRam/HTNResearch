/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.Methods;

import java.util.Vector;

import enumerate.State;
import aiinterface.AIInterface;
import mizunoAI_simulator.SimCharacter;
import struct.MotionData;
import util.Helper;
import util.Pair;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.CompoundTasks.*;
import HTNPlanner.PrimitiveTasks.*;

public class m_Move extends Method 
{
	public m_Move()
	{
		super();
		
		this.name = "m_Move";
	}
	
	@Override
	public boolean CheckPreconditions(Pair<SimCharacter, SimCharacter> currentSimCharacters) 
	{
		int dist = Helper.DistanceBetweenBoxes(currentSimCharacters.m_a.getLeft(), currentSimCharacters.m_a.getRight(),
				currentSimCharacters.m_b.getLeft(), currentSimCharacters.m_b.getRight());
		
		this.tasksToDecompose.clear();
		
		//for speedrunning league always approach opponent
		if(currentSimCharacters.m_a.getHp() > 1000)
		{
			this.tasksToDecompose.add(new p_DASH());
			return true;
		}
		
	
		if(dist > 400)// && !Planner.INSTANCE.GetGameData().getPlayerOneCharacterName().equals(AIInterface.CHARACTER_GARNET))
		{
			this.tasksToDecompose.add(new p_DASH());
		}
		/*else if(dist > 300 && Planner.INSTANCE.GetGameData().getPlayerOneCharacterName().equals(AIInterface.CHARACTER_GARNET))
		{
			this.tasksToDecompose.add(new p_DASH());
		}	*/
	/*	else if(dist > 200 && Planner.INSTANCE.GetGameData().getPlayerOneCharacterName().equals(AIInterface.CHARACTER_GARNET))
		{
			this.tasksToDecompose.add(new p_STAND());
		}	*/
		else
		{	
			Vector<MotionData> mVector = Planner.INSTANCE.GetMotionData(false);
			MotionData mData = mVector.elementAt(currentSimCharacters.m_b.getAction().ordinal());
			
			/*int recovFrames = mData.frameNumber - (mData.attackActive+ mData.attackStartUp);
			if(currentSimCharacters.m_b.getRemainingFrame() <= recovFrames && mData.attackType > 0 && dist > 50)
			{
				System.out.println(".....opp recovering from an attack - dash! " + currentSimCharacters.m_b.getAction().name());
				this.tasksToDecompose.add(new p_FORWARD_WALK());
			}*/
			/*else if(!currentSimCharacters.m_b.getState().equals(State.AIR))
			{
				System.out.println(".....opp not in air - back step");
				this.tasksToDecompose.add(new p_BACK_STEP());
			}*/
			//else
			{
				//System.out.println(".....opp in air? - p_BACK_STEP");
				this.tasksToDecompose.add(new p_BACK_STEP());
			}
		}
		return true;
	}
}
