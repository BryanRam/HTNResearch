/*******************************************************************************
 * HTN Fighter 
 * Created by Xenija Neufeld, 2017
 ******************************************************************************/
package HTNPlanner.CompoundTasks;

import java.io.PrintWriter;

import aiinterface.AIInterface;
import util.Helper;
import HTNPlanner.CompoundTask;
import HTNPlanner.Planner;
import HTNPlanner.UCBPlanner;
import HTNPlanner.Methods.*;

public class c_Move extends CompoundTask
{
	//how often each method was used to decompose this task
		protected static int[] methodsUsed;
		
		//how often each method was used to decompose this task
		protected static float[] methodsSucceeded;
		
		protected static int selected;
		
		protected static float succeeded;
		
		protected static float maxReward = UCBPlanner.EPSILON;
		
		public c_Move()
		{
			super();
			
			this.name = "c_Move";
			
			this.methods.add(new m_ComeCloser());
			this.methods.add(new m_KeepDistance());
			
		
			//this.methods.add(new m_Forwards());
			//this.methods.add(new m_Backwards());
			//this.methods.add(new m_Jump());	
			//this.methods.add(new m_Crouch());
			
			c_Move.methodsUsed = new int[this.methods.size()];
			c_Move.methodsSucceeded = new float[this.methods.size()];
		}
		
		@Override
		protected void IncreaseSelectionStatistics(int methodIndex) 
		{
			this.selected ++;
			c_Move.methodsUsed[methodIndex]++;
			int amount = c_Move.methodsUsed[methodIndex];
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
	//			System.out.println(this.name + ", method " + this.methods.get(i).name + " exploitation " + exploitation + ", exploration " + exploration);
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
			
			double numSelectedMethod = (double)c_Move.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
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
			
			float numSelected = (float)c_Move.methodsUsed[methodIndex] + UCBPlanner.EPSILON;
			float numSucceeded = c_Move.methodsSucceeded[methodIndex]/maxReward;
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

			IncreaseSuccessStatistics(c_Move.methodsSucceeded, this.indMethodSelected,  currentReward, this.succeeded);
			if(Helper.DEBUG_UCB_STATICTICS)
			{
				System.out.println("Method " + this.methods.get(this.indMethodSelected).name + " succeeded in task " + this.name + " " + c_Move.methodsSucceeded[indMethodSelected] + "/" + this.succeeded + " times"
						+ " av reward of m: " + c_Move.methodsSucceeded[indMethodSelected]/c_Move.methodsUsed[indMethodSelected]);

			}	
		}

		@Override
		protected void FlushMethodRewards()
		{
			super.FlushMethodRewards();
			
			if(this.tempReward > c_Move.maxReward)
			{
				c_Move.maxReward = this.tempReward;
			}
		}
}