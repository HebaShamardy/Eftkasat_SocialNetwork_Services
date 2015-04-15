package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	@Path("/sendMessage")
	public String sendMessageService(@FormParam("uname") String uname,
			@FormParam("fname") String fname, @FormParam("msg") String content) {
		JSONObject object = MessageEntity.saveMessage(uname, fname,content,"Not seen");

		/*object.put("Status", "Not Found");
		object.put("email", uemail);
		object.put("friend email", femail);*/

		return object.toString();

	}

}
