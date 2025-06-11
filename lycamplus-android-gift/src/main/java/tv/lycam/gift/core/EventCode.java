package tv.lycam.gift.core;

public class EventCode{
	
	protected static int CODE_INC = 0;
	
	public static int generateEventCode(){
		return ++CODE_INC;
	}

}
