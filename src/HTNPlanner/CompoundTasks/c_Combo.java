/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;
import java.util.Deque;

import enumerate.Action;
import util.Helper;
import HTNPlanner.CompoundTask;
import HTNPlanner.Planner;
import HTNPlanner.PrimitiveTask;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;
import HTNPlanner.Methods.Combos.*;


public class c_Combo extends CompoundTask 
{
	//how often each method was used to decompose this task
	protected static int[] methodsUsed;
	
	//how often each method was used to decompose this task
	protected static float[] methodsSucceeded;
	
	protected static int selected;
	
	protected static float succeeded;
	
	protected static float maxReward = UCBPlanner.EPSILON;
	
	public c_Combo()
	{
		super();
		
		this.name = "c_Combo";
		
		this.methods.add(new m_FirstHit());
		this.methods.add(new m_SecondHit());
		this.methods.add(new m_ThirdHit());
		this.methods.add(new m_FourthHit());
		
		
		c_Combo.methodsUsed = new int[this.methods.size()];
		c_Combo.methodsSucceeded = new float[this.methods.size()];
	}
	
	@Override
	protected void IncreaseSelectionStatistics(int methodIndex) 
	{
		this.selected ++;
		int amount = c_Combo.methodsUsed[methodIndex]++;
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
		
		double numSelectedMethod = (double)c_Combo.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
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
		
		float numSelected = (float)c_Combo.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		float numSucceeded = c_Combo.methodsSucceeded[methodIndex]/maxReward;
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
		IncreaseSuccessStatistics(c_Combo.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(this.indMethodSelected).name + " succeeded in task " + this.name + ", total reward of method: " + c_Combo.methodsSucceeded[indMethodSelected] + " and task" + this.succeeded
					+ " av reward of m: " + c_Combo.methodsSucceeded[indMethodSelected]/c_Combo.methodsUsed[indMethodSelected]);
		}	
	}
	
	@Override
	protected void FlushMethodRewards()
	{
		super.FlushMethodRewards();
		
		if(this.tempReward > c_Combo.maxReward)
		{
			c_Combo.maxReward = this.tempReward;
		}		
	}
	
	@Override
	public void AddBonus(float bonus)
	{
		this.tempReward += bonus;
	}

}