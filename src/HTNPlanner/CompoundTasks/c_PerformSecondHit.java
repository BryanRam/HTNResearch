/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;

import util.Helper;
import HTNPlanner.CompoundTask;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;
import HTNPlanner.Methods.Combos.*;


public class c_PerformSecondHit extends CompoundTask 
{
	//how often each method was used to decompose this task
	protected static int[] methodsUsed;
	
	//how often each method was used to decompose this task
	protected static float[] methodsSucceeded;
	
	protected static int selected;
	
	protected static float succeeded;
	
	protected static float maxReward = UCBPlanner.EPSILON;
	
	public c_PerformSecondHit()
	{
		super();
		
		this.name = "c_PerformSecondHit";
		
		this.methods.add(new m_SecondHit_STAND_B());
		this.methods.add(new m_SecondHit_CROUCH_A());
		this.methods.add(new m_SecondHit_STAND_A());

		
		c_PerformSecondHit.methodsUsed = new int[this.methods.size()];
		c_PerformSecondHit.methodsSucceeded = new float[this.methods.size()];
	}
	
	@Override
	protected void IncreaseSelectionStatistics(int methodIndex) 
	{
		this.selected ++;
		int amount = c_PerformSecondHit.methodsUsed[methodIndex]++;
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(methodIndex).name + " used in task " + this.name + " " + amount + "/" + this.selected + " times");
		}
	}
	
	@Override
	public void SetUCBValues(String[] values)
	{
		this.selected = (Integer.parseInt(values[1]));
		this.succeeded = (Float.parseFloat(values[2]));
		for(int i=0; i< this.methods.size(); ++i)
		{
			this.methodsUsed[i] = (Integer.parseInt(values[3+i]));
			this.methodsSucceeded[i] = (Float.parseFloat(values[3+i+this.methods.size()]));
		}
	}
	
	@Override
	public void PrintAllUCBValuesOfTask(PrintWriter pw)
	{
		
		String outp = "";
		outp += this.name;
		outp += ";";
		outp += this.selected;
		outp += ";";
		outp += this.succeeded;
		outp += ";";
		
		for(int i = 0; i< this.methods.size(); ++i)
		{
			float exploitation = GetExploitationValForMethod(i);
			float exploration = GetExplorationValForMethod(i);
		//	System.out.println(this.name + ", method " + this.methods.get(i).name + " exploitation " + exploitation + ", exploration " + exploration);
			outp += this.methodsUsed[i];
			outp += ";";
		}
		
		for(int i = 0; i< this.methods.size(); ++i)
		{
			outp += this.methodsSucceeded[i];
			outp += ";";
		}
		
		pw.println(outp);
		
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
		
		double numSelectedMethod = (double)c_PerformSecondHit.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		double ln = Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = ln/numSelectedMethod;
		
		float val = (float)(UCBPlanner.C * Math.sqrt(quotient));
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Exploration val for " + this.methods.get(methodIndex).name + " = " + val
					+ "this.selected= " + this.selected + ", methd selected = "+numSelectedMethod);
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
		
		float numSelected = (float)c_PerformSecondHit.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		float numSucceeded = c_PerformSecondHit.methodsSucceeded[methodIndex]/maxReward;
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
		
		IncreaseSuccessStatistics(c_PerformSecondHit.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);

	}
	
	@Override
	protected void FlushMethodRewards()
	{
		float bonus = this.GetBonus();
		this.tempReward += bonus;
			
		super.FlushMethodRewards();
		
		if(this.tempReward > c_PerformSecondHit.maxReward)
		{
			c_PerformSecondHit.maxReward = this.tempReward;
		}
	}
	
	@Override
	public float GetBonus()
	{
		float bonus = 0;
		switch (this.indMethodSelected)
		{
			//m_FirstHit_STAND_A
			case 2:
				bonus += (100/7)/4;
				break;
			//m_FirstHit_STAND_B
			case 0:
				bonus += (90/5)/4;
				break;
			//m_FirstHit_CROUCH_A
			case 1:
				bonus += (40/2)/4;
				break;
		}
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("bonus of " + this.name + " = " + bonus);
		}

		return bonus;
	}
}
