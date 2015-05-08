package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
//import com.google.appengine.labs.repackaged.org.json.JSONArray;


import java.util.Scanner;

/**
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity {
	private String name;
	private String email;
	private String password;
	private long id;

	public UserEntity() {
	}

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	private void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}

	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String email, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("user");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(email)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	public static JSONArray getFriendRequests(String email) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		JSONArray requests = new JSONArray();

		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("friendEmail").toString().equals(email)
					&& entity.getProperty("status").toString()
							.equals("Pending")) {
				JSONObject object = new JSONObject();
				String uemail = entity.getProperty("email").toString();
				object.put("email", uemail);
				requests.add(object);
			}
		}

		return requests;
	}
	
	
	public static JSONArray getFriends(long userId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		JSONArray friendsEmails = new JSONArray();
		UserEntity user = UserEntity.getUserData(userId);
		String email = user.getEmail();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if ((entity.getProperty("friendEmail").toString().equals(email)||
					entity.getProperty("email").toString().equals(email))
					&& entity.getProperty("status").toString()
							.equals("Accepted")) {
				JSONObject object = new JSONObject();
				if(entity.getProperty("friendEmail").toString().equals(email)){
					String uemail = entity.getProperty("email").toString();
					object.put("email", uemail);
					friendsEmails.add(object);
				}else if(entity.getProperty("email").toString().equals(email)){
					String uemail = entity.getProperty("friendEmail").toString();
					object.put("email", uemail);
					friendsEmails.add(object);
				}
				
			}
		}
		JSONArray friends = new JSONArray();
		gaeQuery = new Query("User");
		pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			for(int i=0;i<friendsEmails.size();i++){
				if (entity.getProperty("email").toString().equals(friendsEmails.get(i))) {
					JSONObject object = new JSONObject();
						long id = entity.getKey().getId();
						object.put("id", id);
						object.put("friendName", entity.getProperty("name").toString());
						object.put("friendEmail", entity.getProperty("email").toString());
						friends.add(object);
					
					
				}
			}
			
		}

		return friends;
	}

	public static JSONObject saveFriendRequest(String uemail, String femail) {
		JSONObject object = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("User");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(femail)) {
				DatastoreService datastore2 = DatastoreServiceFactory
						.getDatastoreService();
				Transaction txn2 = datastore2.beginTransaction();
				Query gaeQuery2 = new Query("Friends");
				PreparedQuery pq2 = datastore2.prepare(gaeQuery2);
				List<Entity> list2 = pq2.asList(FetchOptions.Builder
						.withDefaults());
				for (Entity entity1 : pq2.asIterable()) {
					if (entity1.getProperty("email").toString().equals(uemail)
							&& entity1.getProperty("friendEmail").toString()
									.equals(femail)) {
						object.put("Status", "Request was allready sent");
						return object;
					}
				}

				DatastoreService datastore3 = DatastoreServiceFactory
						.getDatastoreService();
				Transaction txn3 = datastore2.beginTransaction();
				Query gaeQuery3 = new Query("Friends");
				PreparedQuery pq3 = datastore3.prepare(gaeQuery3);
				List<Entity> list3 = pq3.asList(FetchOptions.Builder
						.withDefaults());
				try {

					Entity employee = new Entity("Friends", list3.size() + 2);

					employee.setProperty("email", uemail);
					employee.setProperty("friendEmail", femail);
					employee.setProperty("status", "Pending");
					datastore3.put(employee);
					txn3.commit();
				} finally {
					if (txn3.isActive()) {
						txn3.rollback();
					}
				}
				object.put("Status", "Request is sent");
				return object;
			} 
				
			
		}
		if (txn.isActive()) {
			txn.rollback();
		}
		object.put("Status", "Friend Email not found");
		
		return object;
	}

	public static Boolean saveFriend(String uemail, String femail) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(femail)
					&& entity.getProperty("friendEmail").toString()
							.equals(uemail)) {
				entity.setProperty("status", "Accepted");
				datastore.put(entity);
				txn.commit();
				
			}
			if (txn.isActive()) {
				txn.rollback();
			}

		}

		return true;
	}

	public static Boolean deleteRequest(String uemail, String femail) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(femail)
					&& entity.getProperty("friendEmail").toString()
							.equals(uemail)) {
				Key k = entity.getKey();
				datastore.delete(k);
				txn.commit();
				return true;
			}
			if (txn.isActive()) {
				txn.rollback();
			}

		}

		return false;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser(String name, String email, String password) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("User");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(name)
					|| entity.getProperty("email").toString().equals(email)) {

				return false;
			}
		}

		try {
			Entity employee = new Entity("User", list.size() + 2);

			employee.setProperty("name", name);
			employee.setProperty("email", email);
			employee.setProperty("password", password);

			datastore.put(employee);
			txn.commit();
			return true;
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		

	}

	public static UserEntity getUserUpdate(String username) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("User");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(username)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	public static UserEntity getUserData(long userid) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("User");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getKey().getId() == userid) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				returnedUser.setId(entity.getKey().getId());
				return returnedUser;
			}
		}

		return null;
	}

	public static ArrayList<Integer> getAllUsersIds() {
		ArrayList<Integer> Ids = new ArrayList<Integer>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("User");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			int id = (int) entity.getKey().getId();
			Ids.add(id);
		}
		return Ids;
	}
}
