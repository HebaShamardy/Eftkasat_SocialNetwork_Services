package com.FCI.SWE.ServicesModels;

import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class PostEntity {

	private int NumberOfLikes;
	private String Content;
	private String Feeling;
	private int PostID;
	public PageEntity page;

	public PostEntity() {
		// TODO Auto-generated constructor stub
	}

	public void setPostID(int id) {
		PostID = id;
	}

	public int getPostID() {
		return PostID;
	}

	public static long savePost(String Content, long ActiveUserId, String type,
			String timelineUser, String feeling) {
		long postId = -1;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		System.out.println("Size = " + list.size());

		try {
			Entity employee = new Entity("Post", list.size() + 2);

			employee.setProperty("Content", Content);
			employee.setProperty("ActiveUserId", ActiveUserId);
			employee.setProperty("TimelineName", timelineUser);
			employee.setProperty("NumberOflikes", 0);
			employee.setProperty("NumberOfSeen", 0);
			employee.setProperty("Feeling", feeling);
			employee.setProperty("PostType", type);
			employee.setProperty("PeopleWhoLike", "");
			datastore.put(employee);
			txn.commit();

		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}

		}

		for (Entity entity : pq.asIterable()) {
			postId = entity.getKey().getId();

		}

		return postId;

	}

	public static JSONObject GetcontentPost(long PostId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Post");

		PreparedQuery pq = datastore.prepare(gaeQuery);
		JSONObject returnedPost = new JSONObject();
		for (Entity entity : pq.asIterable()) {
			
			if (entity.getKey().getId()==PostId) {
				returnedPost.put("content", entity.getProperty("Conent").toString());
				returnedPost.put("feeling", entity.getProperty("Feeling").toString());
				break;

			}
		}

		return returnedPost;

	}

/*	public static void SharePost(long PostId, String ActiveUser,
			long ActiveUserId) {
		// String FreindsIds;
		// String Content = GetcontentPost(PostId);
		// WRTPTOnHisTimeLine(Content, ActiveUser, "");

		/*
		 * DatastoreService datastore = DatastoreServiceFactory
		 * .getDatastoreService(); Query gaeQuery = new Query("Freinds"); String
		 * Friend_email; long temp_friendID; PreparedQuery pq =
		 * datastore.prepare(gaeQuery); for (Entity entity : pq.asIterable()) {
		 * for (int i = 0; i < 20; i++) { if
		 * (entity.getProperty("ActiveUserID").toString() .equals(ActiveUserId))
		 * { Friend_email = entity.getProperty("friend email") .toString();
		 * WRTPTFreindTimeLine(Content, Friend_email, ActiveUser, "");
		 * 
		 * } }
		 * 
		 * }
		 *

	}*/
	
	public static void saveAudienceForPost(long PostId, ArrayList<Integer> audience) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("PostAudience");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());



		try {
			Entity employee = new Entity("User", list.size() + 2);

			employee.setProperty("postId", PostId);
			JSONArray array = new JSONArray();
			for(int i=0;i<audience.size();i++){
				int id = audience.get(i);
				JSONObject object = new JSONObject();
				object.put("userId", id);
				array.add(object);
			}
			employee.setProperty("userIds", array.toJSONString());
			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}


	}

	public static String LikePost(long postId, String ActiveUser) {
		int num_likes;
		String delim = "/";

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		String[] arr;

		for (Entity entity : pq.asIterable()) {
			long id =entity.getKey().getId();
			if (id == postId) {
				arr = entity.getProperty("PeopleWhoLike").toString()
						.split(delim);

				for (int i = 0; i < arr.length; i++) {
					if (!arr[i].equals(ActiveUser) && i == arr.length - 1) {
						num_likes = (int) entity.getProperty("NumberOfLikes");
						num_likes++;
						entity.setProperty("NumberOfLikes", num_likes);

						String allUsersLikedPost = entity.getProperty(
								"PeopleWhoLike").toString()
								+ "/" + ActiveUser;
						entity.setProperty("PeopleWhoLike", allUsersLikedPost);
						datastore.put(entity);
						txn.commit();
						DatastoreService datastore2 = DatastoreServiceFactory
								.getDatastoreService();
						Transaction txn2 = datastore.beginTransaction();
						Query gaeQuery2 = new Query("postNotification");
						PreparedQuery pq2 = datastore.prepare(gaeQuery);
						List<Entity> list2 = pq.asList(FetchOptions.Builder.withDefaults());

						try {
							Entity employee = new Entity("postNotification", list.size() + 2);

							employee.setProperty("name", ActiveUser);
							employee.setProperty("post", postId);

							datastore.put(employee);
							txn.commit();
						} finally {
							if (txn.isActive()) {
								txn.rollback();
							}
						}

						return "Liked";
					} else if (arr[i].equals(ActiveUser)) {
						return "already liked";
					}
				}

			}
		}
		return "Error";

	}
}
