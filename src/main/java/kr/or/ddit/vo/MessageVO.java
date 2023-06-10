package kr.or.ddit.vo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MessageVO {
	private String message;
	private String sender;
	private String room;
	private String state;
	private String type;
	private String[] reciever;
	private Set<String> loginUsers;
	
	
	
	
	public Set<String> getLoginUsers() {
		return loginUsers;
	}
	public void setLoginUsers(Set<String> loginUsers) {
		this.loginUsers = loginUsers;
	}
	public String[] getReciever() {
		return reciever;
	}
	public void setReciever(String[] reciever) {
		this.reciever = reciever;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MessageVO [message=" + message + ", sender=" + sender + ", room=" + room + ", state=" + state
				+ ", type=" + type + ", reciever=" + Arrays.toString(reciever) + ", loginUsers=" + loginUsers + "]";
	}
	

	

	
	
}
