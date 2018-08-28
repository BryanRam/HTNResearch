/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;
import java.util.ArrayList;

import enumerate.Action;
import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;

public class c_MoveInActionRange extends CompoundTask
{
	
	//how often each method was used to decompose this task
	protected static int[] methodsUsed;
	
	//how often each method was used to decompose this task
	protected static float[] methodsSucceeded;
	
	protected static int selected;
	
	protected static float succeeded;
	
	protected static float maxReward = UCBPlanner.EPSILON;
	
	public c_MoveInActionRange()
	{
		super();
		
		this.name = "c_MoveInActionRange";
		
		this.methods.add(new m_MoveInActionRange(Action.STAND));
		
		c_MoveInActionRange.methodsUsed = new int[this.methods.size()];
		c_MoveInActionRange.methodsSucceeded = new float[this.methods.size()];
	}
	
	public c_MoveInActionRange(Action action)
	{
		super();
		
		this.name = "c_MoveInActionRange";
		
		this.methods.clear();
		this.methods.add(new m_MoveInActionRange(action));	
		
		c_MoveInActionRange.methodsUsed = new int[this.methods.size()];
		c_MoveInActionRange.methodsSucceeded = new float[this.methods.size()];
	}
	
	@Override
	protected void IncreaseSelectionStatistics(int methodIndex) 
	{
		this.selected ++;
		c_MoveInActionRange.methodsUsed[methodIndex]++;
		int amount = c_MoveInActionRange.methodsUsed[methodIndex];
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(methodIndex).name + " used in task " + this.name + " " + amount + "/" + this.selected + " times");
		}
	}
	
	@Override
	public void SetUCBValues(String[] values)
	{
	}
	
	@Override
	public void PrintAllUCBValuesOfTask(PrintWriter pw)
	{
		for(int i = 0; i< this.methods.size(); ++i)
		{
			this.methods.get(i).PrintAllUCBValuesOfMethod(pw);
		}
	}
	
	@Override
	public float GetExplorationValForMethod(int methodIndex)
	{
		if(methodIndex >= this.methods.size())
		{
			return -1;
		}
		
		double numSelectedMethod = (double)c_MoveInActionRange.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		
		/*
		double ln = Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = ln/numSelectedMethod ;
		

		float val = (float)(UCBPlanner.C * Math.sqrt(quotient));
		*/
		
		/*double ln = 2 * Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = ln/numSelectedMethod ;
		

		float val = (float)(3 * Math.sqrt(quotient));*/
		
		/*
		double ln = Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = 1.5 * (ln/numSelectedMethod) ;
		
		float val = (float)(Math.sqrt(quotient));
		*/
		
		double ln = Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = UCBPlanner.C * (ln/numSelectedMethod) ;
		
		float val = (float)(Math.sqrt(quotient));
		
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Exploration val for " + this.methods.get(methodIndex).name + " = " + val);
		}
		return val;
	}
	
	@Override
	public float GetExploitationValForMethod(int methodIndex)
	{
		if(methodIndex >= this.methods.size())
		{
			return -1;
		}
		
		float numSelected = (float)c_MoveInActionRange.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		float numSucceeded = c_MoveInActionRange.methodsSucceeded[methodIndex]/maxReward;
		float q = numSucceeded/numSelected;
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Exploitation val for " + this.methods.get(methodIndex).name + " = " + q);
		}
		return q;
		
	}
	
	@Override
	public void IncreaseSuccessStatistics(float currentReward) 
	{
		//float successPortion = (float)1/(float)(this.methods.get(this.indMethodSelected).GetAmountOfTasks());	
		//this.succeeded += successPortion/this.selected;
		
		if(Helper.SUCCESS_AS_REWARD)
		{
			currentReward = currentReward/(float)(this.methods.get(this.indMethodSelected).GetAmountOfTasks());
		}
		
		IncreaseSuccessStatistics(c_MoveInActionRange.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(this.indMethodSelected).name + " succeeded in task " + this.name + " " + c_MoveInActionRange.methodsSucceeded[indMethodSelected] + "/" + this.succeeded + " times"
					+ " av reward of m: " + c_MoveInActionRange.methodsSucceeded[indMethodSelected]/c_MoveInActionRange.methodsUsed[indMethodSelected]);

		}	
	}
	
	@Override
	protected void FlushMethodRewards()
	{
		super.FlushMethodRewards();
		
		if(this.tempReward > c_MoveInActionRange.maxReward)
		{
			c_MoveInActionRange.maxReward = this.tempReward;
		}
	}
	
}
