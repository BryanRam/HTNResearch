/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import mizunoAI_simulator.SimCharacter;
import util.Helper;
import util.Pair;
import HTNPlanner.CompoundTask;
import HTNPlanner.Method;
import HTNPlanner.Planner;
import HTNPlanner.Task;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;
import HTNPlanner.Methods.Primitive.*;

public class c_Act extends CompoundTask
{
	
	//how often each method was used to decompose this task
	protected static int[] methodsUsed;
	
	//how often each method was used to decompose this task
	protected static float[] methodsSucceeded;
	
	protected static int selected;
	
	protected static float succeeded;
	
	protected static float maxReward = UCBPlanner.EPSILON;
	
	public c_Act()
	{
		super();
		
		this.name = "c_Act";
		
		
		this.methods.add(new m_EvadeProjectile());
		this.methods.add(new m_EscapeCorner());
		
		this.methods.add(new m_Land());
		
		//this.methods.add(new m_UseCombo());	

		this.methods.add(new m_Attack());
		
		this.methods.add(new m_Move());
		
				
		c_Act.methodsUsed = new int[this.methods.size()];
		c_Act.methodsSucceeded = new float[this.methods.size()];
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
			//System.out.println(this.name + ", method " + this.methods.get(i).name + " exploitation " + exploitation + ", exploration " + exploration);
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
	public Pair<SimCharacter,SimCharacter> Decompose(Pair<SimCharacter, SimCharacter> prevSimCharacters, LinkedList<Task> prevTasks) 
	{
		Pair<SimCharacter,SimCharacter> newCharacterData = super.Decompose(prevSimCharacters, prevTasks);
		if(newCharacterData == null || Planner.INSTANCE.GetCurrentPlanLength()==0)
		{
			//System.out.println("No plan was found - getting movement action");
			newCharacterData = this.methods.get(this.methods.size()-1).DecomposeTask(prevSimCharacters, prevTasks);
		}
		
		return newCharacterData;
	}
	
	@Override
	protected void IncreaseSelectionStatistics(int methodIndex) 
	{	
		this.selected ++;
		c_Act.methodsUsed[methodIndex]++;
		int amount = c_Act.methodsUsed[methodIndex];
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(methodIndex).name + " used in task " + this.name + " " + amount + "/" + this.selected + " times");
		}
	}
	
	@Override
	public float GetExplorationValForMethod(int methodIndex)
	{
		if(methodIndex >= this.methods.size())
		{
			return -1;
		}
		
		double numSelectedMethod = (double)c_Act.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		
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
		
		float numSelected = (float)c_Act.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
		float numSucceeded = c_Act.methodsSucceeded[methodIndex]/maxReward;
		float q = numSucceeded/numSelected;
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Exploitation val for " + this.methods.get(methodIndex).name + " = " + q);
		
		}
		
		if(methodIndex == 0)
		{
			return 999;
		}
		if(methodIndex == 1)
		{
			return 998;
		}
		/*
		if(methodIndex == 2)
		{
			return 990;
		}
		*/
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
		
		IncreaseSuccessStatistics(c_Act.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);
		if(Helper.DEBUG_UCB_STATICTICS)
		{
			System.out.println("Method " + this.methods.get(this.indMethodSelected).name + " succeeded in task " + this.name + ", total reward of method: " + c_Act.methodsSucceeded[indMethodSelected] + " and task" + this.succeeded 
					+ " av reward of m: " + c_Act.methodsSucceeded[indMethodSelected]/c_Act.methodsUsed[indMethodSelected]);
		}	
	}
	
	@Override
	protected void FlushMethodRewards()
	{
		super.FlushMethodRewards();
		
		if(this.tempReward > c_Act.maxReward)
		{
			c_Act.maxReward = this.tempReward;
		}
	}
	
	@Override
	public void AddBonus(float bonus)
	{
		this.tempReward += bonus;
	}
}
