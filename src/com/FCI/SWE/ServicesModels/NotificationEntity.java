package com.FCI.SWE.ServicesModels;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class NotificationEntity 
{
	public JSONArray getFriendRequestNotification(String uemail)
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		JSONArray requests = new JSONArray();
		
		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("friend email").toString().equals(uemail)&&
					entity.getProperty("status").toString().equals("Pending")) {
				JSONObject object = new JSONObject();
				String email=entity.getProperty("email").toString();
				object.put("email", email);
				requests.add(object);
			}
		}

		return requests;
	}
	public JSONArray getMessageNotification(String uname)
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		JSONArray requests = new JSONArray();
		
		Query gaeQuery = new Query("notifications");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable())
		{
			if (entity.getProperty("user name").toString().equals(uname)) 
			{
				JSONObject object = new JSONObject();
				String uname1=entity.getProperty("name").toString();
				object.put("name", uname1);
				requests.add(object);
			}
		}
		return requests;
	}
}
