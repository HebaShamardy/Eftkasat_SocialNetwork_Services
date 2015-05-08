package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class NotificationEntity {
	public JSONArray getFriendRequestNotification(String uemail) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		JSONArray requests = new JSONArray();

		Query gaeQuery = new Query("Friends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("friendEmail").toString().equals(uemail)
					&& entity.getProperty("status").toString()
							.equals("Pending")) {
				JSONObject object = new JSONObject();
				String email = entity.getProperty("email").toString();
				object.put("email", email);
				requests.add(object);
			}
		}

		return requests;
	}

	public JSONArray getMessageNotification(String uname) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Transaction txn = datastore.beginTransaction();
		JSONArray requests = new JSONArray();
		
		Query gaeQuery = new Query("notifications");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("user name").toString().equals(uname)) {
				JSONObject object = new JSONObject();
				String uname1 = entity.getProperty("name").toString();
				object.put("name", uname1);
				requests.add(object);
			}
		}
		return requests;
	}
	
	public JSONArray getPostNotification(String uname) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Transaction txn = datastore.beginTransaction();
		ArrayList<Integer> postIds = new ArrayList<>();

		Query gaeQuery = new Query("postNotification");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").toString().equals(uname)) {
				JSONObject object = new JSONObject();
				 long postId = (long) entity.getProperty("post");
				postIds.add((int)postId);
			}
		}
		
		
		JSONArray array = new JSONArray();

		Query gaeQuery1 = new Query("Post");
		PreparedQuery pq1 = datastore.prepare(gaeQuery1);
		for (Entity entity : pq1.asIterable()) {
			for (int i = 0; i < postIds.size(); i++) {
				long id = entity.getKey().getId();
				if (id==postIds.get(i)) {
					long userID = (long) entity.getProperty("ActiveUserId");
					UserEntity user = UserEntity.getUserData(userID);
					JSONObject object = new JSONObject();
					object.put("postID", entity.getProperty("ID/Name"));
					object.put("postContent", entity.getProperty("Content")
							.toString());
					object.put("username", user.getName());
					object.put("userId", entity.getProperty("ActiveUserId"));
					object.put("feeling", entity.getProperty("Feeling")
							.toString());
					object.put("seen", entity.getProperty("NumberOfSeen"));
					object.put("likes", entity.getProperty("NumberOflikes"));
					object.put("timeline", entity.getProperty("TimelineName"));
					object.put("people", entity.getProperty("PeopleWhoLike"));
					array.add(object);

				}

			}

		}

		return array;
	}
}
