package ClientServerApi;

import com.google.gson.Gson;

public class CommandWrapper {
	/**
	 * Command wrapper for sending packages between client and server:
	 * Fields:
	 * 		senderID - sender id of the client (worker, manager, cart, etc. )
	 * 		commandDescriptor - the command to exec on server side.
	 * 		resultDescriptor - result code delivered from server to client.
	 * 		data - a String (in json format) that holds the cmd's arguments in case of a client-to-server package
	 * 			   a String (in json format) that holds the cmd's return value in case of a server-to-client package
	 * 
	 * @author idan atias
	 */

	private int senderID;
	private CommandDescriptor commandDescriptor;
	private ResultDescriptor resultDescriptor;
	private String data;
	
	//Client c'tors:
	public CommandWrapper(int senderID, CommandDescriptor commandDescriptor) {
		this.senderID = senderID;
		this.commandDescriptor = commandDescriptor;
	}
	
	public CommandWrapper(int senderID, CommandDescriptor commandDescriptor, String args){
		this(senderID, commandDescriptor);
		this.data = args;
	}
	
	//Server c'tors:
	public CommandWrapper(ResultDescriptor resultDescriptor, String retVal) {
		this.resultDescriptor = resultDescriptor;
		this.data = retVal;
	}
	
	//Methods:
	public String toGson(){
		return new Gson().toJson(this);
	}
	
	public CommandWrapper fromGson(String cmdWrap) {
		return new Gson().fromJson(cmdWrap, CommandWrapper.class);
	}
	
	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public CommandDescriptor getCommandDescriptor() {
		return commandDescriptor;
	}

	public void setCommandDescriptor(CommandDescriptor ¢) {
		this.commandDescriptor = ¢;
	}

	public ResultDescriptor getResultDescriptor() {
		return resultDescriptor;
	}

	public void setResultDescriptor(ResultDescriptor ¢) {
		this.resultDescriptor = ¢;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
