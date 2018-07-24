import commandcenter.CommandCenter;
import enumerate.Action;

import structs.FrameData;
import structs.GameData;
import structs.Key;
import structs.MotionData;
import gameInterface.AIInterface;

/**
 * This class is a sample AI for Fighting ICE.
 * It gives a very simple sample on how to use the method CancelAbleFrame in the class MotionData.
 * The AI does nothing but displays its opponent@character's return value of CancelAbleFrame.
 * @author Yamamoto, Team Fighting ICE.
 *
 */

public class MotionDataSample implements AIInterface {

	boolean p;
	GameData gd;
	Key inputKey;
	FrameData fd;
	CommandCenter cc;
	
	@Override
	public int initialize(GameData gameData, boolean playerNumber) {
		// TODO Auto-generated method stub
		gd = gameData;
		p = playerNumber;
		
		inputKey = new Key();
		fd = new FrameData();
		cc = new CommandCenter();
		
		return 0;
	}

	@Override
	public void getInformation(FrameData frameData) {
		// TODO Auto-generated method stub
		fd = frameData;
		cc.setFrameData(fd, p);

	}

	@Override
	public void processing() {
		if(!fd.getEmptyFlag()){
			if(fd.getRemainingTime() > 0){
				//  In order to get CancelAbleFrame's information on the current action of the opponent character, first you write as follows:
				Action oppAct = cc.getEnemyCharacter().getAction();
				// If you want the same information on a specific action, say "STAND_A", you can simply write:
				// Action action = Action.STAND_A;
				
				// Next, get the MotionData information on the opponent character's action of interest from GameData.
				// You can access the MotionData information with
				// gd.getPlayer???Motion.elementAt("an instance of action (e.g., oppAct or action)".ordinal())
				MotionData oppMotion = new MotionData();
				if(p)oppMotion = gd.getPlayerTwoMotion().elementAt(oppAct.ordinal());
				else oppMotion = gd.getPlayerOneMotion().elementAt(oppAct.ordinal());
				
				System.out.println(oppMotion.getMotionName()+":cancelable " + oppMotion.getCancelAbleFrame() + " frame.");
			}
		}
	}

	@Override
	public Key input() {
		// TODO Auto-generated method stub
		return inputKey;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public String getCharacter(){
		return CHARACTER_ZEN;
	}

}
