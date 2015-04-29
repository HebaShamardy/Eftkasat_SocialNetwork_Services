package com.FCI.SWE.Services;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.MessageEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

/**
 * This class contains REST services, also contains action function for Android
 * application
 * 
 * @author Noha Magdy
 * @author Heba Shamardy
 * @author Noha Hegazy
 * @author Neama Foaud
 * @author Nehal Khaled
 * @version 1.1
 * @since 2015-04-5
 *
 */
@Path("/message")
@Produces(MediaType.TEXT_PLAIN)
public class MessageServices {

	@POST
	@Path("/sendNewMessage")
	public String sendNewMessageService(@FormParam("uname") String uname,
			@FormParam("convTitle") String convTitle,
			@FormParam("msgContent") String content, ArrayList<String> users) {
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		for (int i = 0; i < users.size(); i++) {
			obj.put("name", users.get(i));
			arr.add(obj);
		}
		long convId = MessageEntity.saveConversation(convTitle);
		long msgId = MessageEntity.saveMessage(uname, content, convId);
		MessageEntity.saveUsersInConversation(uname, arr, convId);
		MessageEntity.saveMessageStatus(uname, msgId, convId);
		JSONObject object = new JSONObject();
		object.put("Status", "sent");
		/*
		 * object.put("email", uemail); object.put("friend email", femail);
		 */

		return object.toString();

	}

	@POST
	@Path("/replyMessage")
	public String replyMessageService(@FormParam("uname") String uname,
			@FormParam("convTitle") String convTitle,
			@FormParam("msgContent") String content) {

		long convId = MessageEntity.getConversationId(convTitle);
		long msgId = MessageEntity.saveMessage(uname, content, convId);
		MessageEntity.saveMessageStatus(uname, msgId, convId);
		JSONObject object = new JSONObject();
		object.put("Status", "sent");

		return object.toString();

	}

}
