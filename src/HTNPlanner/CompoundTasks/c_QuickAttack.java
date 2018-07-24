/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;
import java.util.ArrayList;

import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;

public class c_QuickAttack extends CompoundTask
{
	
	//how often each method was used to decompose this task
	protected static int[] methodsUsed;
	
	//how often each method was used to decompose this task
	protected static float[] methodsSucceeded;
	
	protected static int selected;
	
	protected static float succeeded;
	
	protected static float maxReward = UCBPlanner.EPSILON;
	
	public c_QuickAttack()
	{
		super();
		
		this.name = "c_QuickAttack";
		
		int maxHP = Planner.INSTANCE.frameData == null ? 0 : Math.max(Planner.INSTANCE.frameData.getCharacter(true).getHp(),Planner.INSTANCE.frameData.getCharacter(false).getHp()); 
		if(maxHP > 0 && (maxHP > 9000 || Planner.INSTANCE.frameData.getCharacter(true).getHp() < (int)(maxHP/3)
				|| Planner.INSTANCE.frameData.getCharacter(false).getHp() < (int)(maxHP/3)))
		{
			this.methods = Planner.INSTANCE.GetQuickAttacks_sorted();
		}
		else
		this.methods = Planner.INSTANCE.GetQuickAttacks();

		
		c_QuickAttack.methodsUsed = new int[this.methods.size()];
		c_QuickAttack.methodsSucceeded = new float[this.methods.size()];
	}
	
	@Override
	protected void IncreaseSelectionStatistics(int methodIndex) 
	{
		this.selected ++;
		c_QuickAttack.methodsUsed[methodIndex]++;
		int amount = c_QuickAttack.methodsUsed[methodIndex];
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
	//		System.out.println(this.name + ", method " + this.methods.get(i).name + " exploitation " + exploitation + ", exploration " + exploration);
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
		
		double numSelectedMethod = (double)c_QuickAttack.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		double ln = Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = ln/numSelectedMethod ;

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
		
		float numSelected = (float)c_QuickAttack.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		float numSucceeded = c_QuickAttack.methodsSucceeded[methodIndex]/maxReward;
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
		
		IncreaseSuccessStatistics(c_QuickAttack.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(this.indMethodSelected).name + " succeeded in task " + this.name + ", total reward of method: " + c_QuickAttack.methodsSucceeded[indMethodSelected] + " and task" + this.succeeded
					+ " av reward of m: " + c_QuickAttack.methodsSucceeded[indMethodSelected]/c_QuickAttack.methodsUsed[indMethodSelected]);

		}	
	}
	
	@Override
	protected void FlushMethodRewards()
	{
		super.FlushMethodRewards();
		
		if(this.tempReward > c_QuickAttack.maxReward)
		{
			c_QuickAttack.maxReward = this.tempReward;
		}
	}
}
