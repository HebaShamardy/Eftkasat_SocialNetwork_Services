package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.User;
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
	public UserEntity()
	{
		
	}
	private void setId(long id){
		this.id = id;
	}
	
	public long getId(){
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

		Query gaeQuery = new Query("users");
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
			if (entity.getProperty("friend email").toString().equals(email)&&
					entity.getProperty("status").toString().equals("Pending")) {
				JSONObject object = new JSONObject();
				String uemail=entity.getProperty("email").toString();
				object.put("email", uemail);
				requests.add(object);
			}
		}

		return requests;
	}
	


	
	public static Boolean saveFriendRequest(String uemail ,String femail) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		
		
		try {
		Entity employee = new Entity("Friends", list.size() + 2);

		employee.setProperty("email", uemail);
		employee.setProperty("friend email", femail);
		employee.setProperty("status","Pending");
		datastore.put(employee);
		txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;

}
	
	public static Boolean saveFriend(String uemail ,String femail) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(femail)
					&& entity.getProperty("friend email").toString().equals(uemail)) {
			 entity.setProperty("status","Accepted");
			 datastore.put(entity);
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
	public static Boolean deleteRequest(String uemail ,String femail) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("email").toString().equals(femail)
					&& entity.getProperty("friend email").toString().equals(uemail)) {
				Key k=entity.getKey();
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

public Boolean saveUser() {
	DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();
	Transaction txn = datastore.beginTransaction();
	Query gaeQuery = new Query("users");
	PreparedQuery pq = datastore.prepare(gaeQuery);
	List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
	System.out.println("Size = " + list.size());
	
	try {
	Entity employee = new Entity("users", list.size() + 2);

	employee.setProperty("name", this.name);
	employee.setProperty("email", this.email);
	employee.setProperty("password", this.password);
	
	datastore.put(employee);
	txn.commit();
	}finally{
		if (txn.isActive()) {
	        txn.rollback();
	    }
	}
	return true;

}
}
