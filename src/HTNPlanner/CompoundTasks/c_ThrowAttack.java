/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;

public class c_ThrowAttack extends CompoundTask
{
	
	//how often each method was used to decompose this task
	protected static int[] methodsUsed;
	
	//how often each method was used to decompose this task
	protected static float[] methodsSucceeded;
	
	protected static int selected;
	
	protected static float succeeded;
	
	protected static float maxReward = UCBPlanner.EPSILON;
	
	public c_ThrowAttack()
	{
		super();
		
		this.name = "c_ThrowAttack";
		
		int maxHP = Planner.INSTANCE.frameData == null ? 0 : Math.max(Planner.INSTANCE.frameData.getCharacter(true).getHp(),Planner.INSTANCE.frameData.getCharacter(false).getHp()); 
		if(maxHP > 0 && (maxHP > 9000 || Planner.INSTANCE.frameData.getCharacter(true).getHp() < (int)(maxHP/3)
				|| Planner.INSTANCE.frameData.getCharacter(false).getHp() < (int)(maxHP/3)))
		{
			this.methods = Planner.INSTANCE.GetThrowAttacks_sorted();
		}
		else
		this.methods = Planner.INSTANCE.GetThrowAttacks();
		
		this.methods.add(new m_Move());
		
		c_ThrowAttack.methodsUsed = new int[this.methods.size()];
		c_ThrowAttack.methodsSucceeded = new float[this.methods.size()];
	}
	
	@Override
	protected void IncreaseSelectionStatistics(int methodIndex) 
	{
		this.selected ++;
		c_ThrowAttack.methodsUsed[methodIndex]++;
		int amount = c_ThrowAttack.methodsUsed[methodIndex];
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(methodIndex).name + " used in task " + this.name + " " + amount + "/" + this.selected + " times");
		}
	}
	
	@Override
	public void SetUCBValues(String[] values)
	{
		/*
		System.out.println("Method size: " + this.methods.size());
		System.out.println("MethodsUsed Size: " + Array.getLength(this.methodsUsed));
		System.out.println("Values Size: " + Array.getLength(values));
		*/
		this.selected = (Integer.parseInt(values[1]));
		this.succeeded = (Float.parseFloat(values[2]));
		for(int i=0; i< /*this.methods.size()*/((Array.getLength(values)-3)/2); ++i)
		{
			/*
			System.out.println("Values Size: " + Array.getLength(values) + 
					" current iterator: " + i + 
					" Methods size: " + this.methods.size() + 
					" current val: " + (3+i+this.methods.size()) );
					
			System.out.println(Arrays.toString(values));
			System.out.println("ParseInt: " + (values[1]));
			//System.out.println("ParseFloat: " + (Float.parseFloat(values[3+2])));
			*/
			System.out.println(values[3+i]);
			this.methodsUsed[i] = (Integer.parseInt(values[3+i]));
			//this.methodsSucceeded[i] = (Float.parseFloat(values[3+i+this.methods.size()]));
			this.methodsSucceeded[i] = (Float.parseFloat(values[3+i+((Array.getLength(values)-3)/2)]));
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
		
		double numSelectedMethod = (double)c_ThrowAttack.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		double ln = Math.log(this.selected);
		ln = this.selected == 0? 0:ln;
		double quotient = ln/numSelectedMethod ;

		float val = (float)(UCBPlanner.C * Math.sqrt(quotient));
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
		
		float numSelected = (float)c_ThrowAttack.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		float numSucceeded = c_ThrowAttack.methodsSucceeded[methodIndex]/maxReward;
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
		
		IncreaseSuccessStatistics(c_ThrowAttack.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(this.indMethodSelected).name + " succeeded in task " + this.name + ", total reward of method: " + c_ThrowAttack.methodsSucceeded[indMethodSelected] + " and task" + this.succeeded
					+ " av reward of m: " + c_ThrowAttack.methodsSucceeded[indMethodSelected]/c_ThrowAttack.methodsUsed[indMethodSelected]);

		}	
	}
	
	@Override
	protected void FlushMethodRewards()
	{
		super.FlushMethodRewards();
		
		if(this.tempReward > c_ThrowAttack.maxReward)
		{
			c_ThrowAttack.maxReward = this.tempReward;
		}
	}
	
}
