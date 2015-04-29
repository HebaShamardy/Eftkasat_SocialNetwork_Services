package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class HashTagEntity {

	public static String HashTagDB(String Hashname, long postid) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("HashTag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("Size = " + list.size());

		try {
			Entity employee = new Entity("HashTag", list.size() + 2);

			employee.setProperty("HashTagContent", Hashname);
			employee.setProperty("Post_ID", postid + "/");
			employee.setProperty("Counter", 0);

			datastore.put(employee);
			txn.commit();
			return "Saved";
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

	}

	public static void addHashTagtoTrends(String HashTeagName, String posts,
			int counter) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("HashTagTrends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("Size = " + list.size());

		try {
			Entity employee = new Entity("HashTagTrends", list.size() + 2);

			employee.setProperty("HashTagContent", HashTeagName);
			employee.setProperty("Counter", 10);
			employee.setProperty("posts", posts);

			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

	}

	public static String SaveHashtag(long Postid, String ContentHashtag) {
		String result = "";
		int counter = 0;
		String temp;
		// int counterArray [] = null ;
		// ArrayList<Integer> counterArray=new ArrayList<>();
		JSONArray counterArray = new JSONArray();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("HashTag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("HashTagContent").toString()
					.equals(ContentHashtag)) {

				counter = (int) entity.getProperty("Counter");
				counter++;

				entity.setProperty("Counter", counter);
				temp = entity.getProperty("Post_ID").toString();
				temp = temp + Postid + "/";

				entity.setProperty("Post_ID", temp);
				datastore.put(entity);
				txn.commit();
				result = "Saved";
			}

		}
		if (!result.equals("Posted")) {

			result = HashTagDB(ContentHashtag, Postid);
		}
		if (txn.isActive()) {
			txn.rollback();
		}

		for (Entity entity : pq.asIterable()) {
			if (!entity.getProperty("Counter").equals(0)) {
				JSONObject object = new JSONObject();
				object.put("hashragName", entity.getProperty("HashTagContent")
						.toString());
				object.put("counter", entity.getProperty("Counter"));
				object.put("posts_ids", entity.getProperty("Post_ID"));
				counterArray.add(object);
			}

		}

		// sort counterArray

		int temp2, temp3;
		int swap;
		for (int i = 0; i < counterArray.size() - 1; i++) {
			JSONObject object = new JSONObject();
			object = (JSONObject) counterArray.get(i);
			temp2 = (int) object.get("counter");
			for (int j = i + 1; j < counterArray.size(); j++) {
				JSONObject object2 = new JSONObject();
				object2 = (JSONObject) counterArray.get(j);
				temp3 = (int) object.get("counter");
				if (temp3 < temp2) {
					counterArray.set(i, object2);
					counterArray.set(j, object);
				}
			}

		}

		for (int i = 0; i < 10; i++) {
			JSONObject object = new JSONObject();
			object = (JSONObject) counterArray.get(i);
			addHashTagtoTrends((String) object.get("hashragName"),
					(String) object.get("posts_ids"),
					(int) object.get("counter"));

		}
		return result;

	}

	public static String searchHashtag(String Content) {
		String[] posts = null;
		String delim = "/";
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("HashTag");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {

			if (entity.getProperty("HashTagContent").toString().equals(Content)) {
				posts = entity.getProperty("Post_ID").toString().split(delim);
				break;

			}

		}
		JSONArray array = new JSONArray();

		Query gaeQuery1 = new Query("Post");
		PreparedQuery pq1 = datastore.prepare(gaeQuery1);
		for (Entity entity : pq1.asIterable()) {
			for (int i = 0; i < posts.length; i++) {
				if (entity.getProperty("ID/Name").toString().equals(posts[i])) {
					long userId = (long) entity.getProperty("ActiveUserId");
					UserEntity user = UserEntity.getUserData(userId);
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

		return array.toJSONString();
	}
	
	public static String getTrends() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("HashTagTrends");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONArray array=new JSONArray();
		for (Entity entity : pq.asIterable()) {
			JSONObject object = new JSONObject();
			object.put("hashtag", entity.getProperty("HashTagContent"));
			array.add(object);
		}

		return array.toJSONString();
	}

}