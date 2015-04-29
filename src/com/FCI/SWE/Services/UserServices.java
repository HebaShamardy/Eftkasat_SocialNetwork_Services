package com.FCI.SWE.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.ServicesModels.UserEntity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query;

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
 * @since 2014-02-12
 *
 */
@Path("/user")
@Produces(MediaType.TEXT_PLAIN)
public class UserServices {

	/**
	 * Registration Rest service, this service will be called to make
	 * registration. This function will store user data in data store
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided password
	 * @return Status json
	 */
	@POST
	@Path("/RegistrationService")
	public String registrationService(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		UserEntity user = new UserEntity();
		Boolean bool = user.saveUser(uname, email, pass);
		JSONObject object = new JSONObject();
		if (bool)
			object.put("Status", "OK");
		else
			object.put("Status", "Failed");
		return object.toString();
	}

	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return user in json format
	 */
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("password") String pass,
			@FormParam("email") String email) {
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(email, pass);
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
			object.put("id", user.getId());
		}
		return object.toString();

	}

	/**
	 * Accept Friend Rest service, this service will be called to accept a
	 * friend request that was sent to the active user. This function will store
	 * the two friends' email in data store.
	 * 
	 * @param uemail
	 *            provided current user email
	 * @param femail
	 *            provided the accepted friend email
	 * @return Status json
	 */
	@POST
	@Path("/AddFriendService")
	public String addFriend(@FormParam("uemail") String uemail,
			@FormParam("femail") String femail) {
		Boolean found = UserEntity.saveFriend(uemail, femail);
		JSONObject object = new JSONObject();
		if (found)
			object.put("Status", "ok");
		else
			object.put("Status", "Not Found");
		return object.toString();
	}

	/**
	 * Deny friend request Rest service, this service will be called to deny a
	 * request that was sent to current user. This function will delete the
	 * request that is denied from data store
	 * 
	 * @param uemail
	 *            provided current user email
	 * @param femail
	 *            provided the friend email
	 * 
	 * @return Status json
	 */
	@POST
	@Path("/denyFriendService")
	public String denyFriendRequset(@FormParam("uemail") String uemail,
			@FormParam("femail") String femail) {
		UserEntity.deleteRequest(uemail, femail);
		JSONObject object = new JSONObject();

		object.put("Status", "ok");

		return object.toString();
	}

	/**
	 * send friend request Rest service, this service will be called to send a
	 * request to a user of the system by providing his email. This function
	 * will store a friend request by storing two email in data store
	 * 
	 * @param uemail
	 *            provided current user email
	 * @param femail
	 *            provided the friend email that a request will be sent to
	 * 
	 * @return Status json
	 */

	@POST
	@Path("/sendFriendRequest")
	public String sendFriendRequest(@FormParam("uemail") String uemail,
			@FormParam("femail") String femail) {

		JSONObject object = UserEntity.saveFriendRequest(uemail, femail);

		object.put("Status", "OK");
		object.put("email", uemail);
		object.put("friend email", femail);

		return object.toString();

	}

	/**
	 * show friend requests Rest service, this service will be called to show
	 * friend requests sent to the current user of the system by providing his
	 * email. This function will show the friend requests by searching by his
	 * email in data store.
	 * 
	 * @param uemail
	 *            provided current user email
	 * 
	 * 
	 * @return Status JSONArray
	 */

	@POST
	@Path("/showFriendRequests")
	public String showFriendRequests(@FormParam("uemail") String uemail) {
		JSONArray array = UserEntity.getFriendRequests(uemail);
		if (array.isEmpty())
			return "No friend requests";
		return array.toJSONString();

	}

}