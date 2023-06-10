package kr.or.ddit.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

import kr.or.ddit.vo.MessageVO;


@Controller
public class ChattingHandler extends TextWebSocketHandler {
	


	private static Map<String,Map<String,WebSocketSession>> rooms =Collections.synchronizedMap(new HashMap<String,Map<String,WebSocketSession>>());
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		String msg = message.getPayload();
		Gson gson = new Gson();
		MessageVO vo = gson.fromJson(msg, MessageVO.class);
		if(vo.getType().equals("l")) {
			if(vo.getState().equals("login")) {
				if(rooms.containsKey(vo.getRoom())) {
					rooms.get(vo.getRoom()).put(vo.getSender(), session);
				}else {
					Map<String, WebSocketSession> map = new HashMap<String, WebSocketSession>();
					map.put(vo.getSender(), session);
					rooms.put(vo.getRoom(), map);
				}
				Map<String, WebSocketSession> room = rooms.get(vo.getRoom());
				
				for (String userId : room.keySet()) {
					WebSocketSession wss = room.get(userId);
					wss.sendMessage(message);
				}
			}else {
				if(rooms.containsKey(vo.getRoom())) {
					Map<String, WebSocketSession> room = rooms.get(vo.getRoom());
					if(room.size()==1) {
						rooms.remove(vo.getRoom());
						
					}else {
						room.remove(vo.getSender());
						rooms.put(vo.getRoom(), room);
						for (String userId : room.keySet()) {
							WebSocketSession wss = room.get(userId);
							wss.sendMessage(message);
						}
					}
				}
			}
			
		}else if(vo.getType().equals("m")||vo.getType().equals("f")){
			
			String roomId = vo.getRoom();
			
			if(rooms.containsKey(roomId)) {
				Map<String, WebSocketSession> room = rooms.get(roomId);
				for (String userId : room.keySet()) {
					WebSocketSession wss = room.get(userId);
					wss.sendMessage(message);
				}
			}
			
		}
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionEstablished(session);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
	}
}
