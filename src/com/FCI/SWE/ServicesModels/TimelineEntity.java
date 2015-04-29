package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;



public class TimelineEntity {

	public static String getUserPosts(long userId) throws ParseException{
		ArrayList<Integer> postIds = new ArrayList<Integer>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("PostAudience");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			String ids =entity.getProperty("userId").toString();
			Object object=null;
			JSONArray arrayObj=null;
			JSONParser jsonParser=new JSONParser();
			object=jsonParser.parse(ids);
			arrayObj=(JSONArray) object;
			JSONObject object2 = new JSONObject();
			object2.put("userId", userId);
			
			if (arrayObj.contains(object2)) {
				postIds.add((Integer) entity.getProperty("postId"));

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

		return array.toJSONString();

	}
	
	
	public static String getPagePosts(long pageId){
		ArrayList<Integer> postIds = new ArrayList<Integer>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query gaeQuery = new Query("pagePosts");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			
			if ((long)entity.getProperty("pageId") == pageId) {
				postIds.add((Integer) entity.getProperty("post"));

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

		return array.toJSONString();

	}
}
