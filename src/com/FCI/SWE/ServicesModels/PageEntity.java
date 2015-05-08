package com.FCI.SWE.ServicesModels;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class PageEntity {

	public static String createPage(String name, String type, String category,
			String uname) {
		new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").equals(name))
				return "Found";
		}
		try {
			datastore.prepare(gaeQuery);
			Entity employee = new Entity("Page" , list.size()+1);
			employee.setProperty("name", name);
			employee.setProperty("type", type);
			employee.setProperty("category", category);
			employee.setProperty("numberOfLikes", 0);
			employee.setProperty("adminName", uname);
			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return "Created successfully";
	}

	public static long addPost(long pageId, String pageName, String type,
			String content, long userId, String feeling) {
		int postId = 0;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		try {
			Entity employee = new Entity("Post", list.size() + 2);
			employee.setProperty("Content", content);
			employee.setProperty("ActiveUserId", userId);
			employee.setProperty("TimelineName", pageName);
			employee.setProperty("NumberOflikes", 0);
			employee.setProperty("NumberOfSeen", 0);
			employee.setProperty("Feeling", feeling);
			employee.setProperty("PostType", type);
			employee.setProperty("PeopleWhoLike ", "");
			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

		gaeQuery = new Query("pagePosts");
		pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			postId = (int) k.getId();
		}
		gaeQuery = new Query("pagePosts");
		pq = datastore.prepare(gaeQuery);
		try {
			Entity employee = new Entity("pagePosts");

			employee.setProperty("pageId", pageId);
			employee.setProperty("post", postId);

			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

		return postId;
	}

	public static String likePage(long pageID, String uname) {
		long likesNumber = 0, ID = 0;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userPageLikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			ID = (long) entity.getProperty("pageId");
		
			if (entity.getProperty("username").equals(uname) && ID == pageID)
				return "AlreadyLiked";
		}
		ID=0;
		txn = datastore.beginTransaction();
		gaeQuery = new Query("Page");
		pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			ID = (long) k.getId();

			if (ID == pageID) {
				likesNumber = (long) entity.getProperty("numberOfLikes");
				likesNumber++;
				entity.setProperty("numberOfLikes", likesNumber);
				datastore.put(entity);
				txn.commit();
			}
		}
		if (txn.isActive()) {
			txn.rollback();
		}
		txn = datastore.beginTransaction();
		gaeQuery = new Query("userPageLikes");
		pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		Entity employee = new Entity("userPageLikes", list.size() + 2);
		employee.setProperty("pageId", pageID);
		employee.setProperty("username", uname);
		datastore.put(employee);
		txn.commit();
		if (txn.isActive()) {
			txn.rollback();
		}
		return "Liked";
	}

	public static String unlikePage(long pageID, String uname) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userPageLikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if ((long)entity.getProperty("pageId")==pageID
					&& entity.getProperty("username").toString().equals(uname)) {
				Key k = entity.getKey();
				datastore.delete(k);
				txn.commit();
			}
			
		}
		long ID = -1, likesNumber = 0;
		txn = datastore.beginTransaction();
		gaeQuery = new Query("Page");
		pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			ID = (long) k.getId();

			if (ID == pageID) {
				likesNumber = (long) entity.getProperty("numberOfLikes");
				likesNumber--;
				entity.setProperty("numberOfLikes", likesNumber);
				datastore.put(entity);
				txn.commit();
			}
		}
		
		return "Unliked";
	}

	public static JSONArray getPageActiveUsers(long pageID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		JSONArray likedUsers = new JSONArray();

		Query gaeQuery = new Query("userPageLikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if ((long)entity.getProperty("pageId")==pageID) {
				JSONObject object = new JSONObject();
				String uname = entity.getProperty("username").toString();
				object.put("username", uname);
				likedUsers.add(object);
			}
		}
		return likedUsers;
	}

	public static JSONArray showUnlikedPgaes(String uname) {
		ArrayList<Long> pagesIDs = new ArrayList<Long>();
		ArrayList<Long> unlikedPagesIDs = new ArrayList<Long>();
		JSONArray unlikedPagesNames = new JSONArray();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if(!entity.getProperty("adminName").equals(uname)){
			Key k = entity.getKey();
			pagesIDs.add((long) k.getId());
			}
		}
		txn = datastore.beginTransaction();
		gaeQuery = new Query("userPageLikes");
		pq = datastore.prepare(gaeQuery);

		for (int i = 0; i < pagesIDs.size(); i++) {
			for (Entity entity : pq.asIterable()) {
				if ((long)entity.getProperty("pageId")==pagesIDs.get(i)
						&& entity.getProperty("username").toString()
								.equals(uname))
					continue;
				else
					unlikedPagesIDs.add(pagesIDs.get(i));
			}
		}
		txn = datastore.beginTransaction();
		gaeQuery = new Query("Page");
		pq = datastore.prepare(gaeQuery);

		for (int i = 0; i < unlikedPagesIDs.size(); i++) {
			for (Entity entity : pq.asIterable()) {
				Key k = entity.getKey();
				long ID = (long) k.getId();
				if (ID == unlikedPagesIDs.get(i)) {
					JSONObject object = new JSONObject();
					String pageName = entity.getProperty("name").toString();
					object.put("pageId", unlikedPagesIDs.get(i));
					object.put("name", pageName);
					unlikedPagesNames.add(object);
					break;
				}
			}
		}
		return unlikedPagesNames;
	}

	public static String getPostSeen(int postID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Post");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		int numberOfSeen = 0;
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			int id = (int) k.getId();
			if (id == postID)
				numberOfSeen = (int)entity.getProperty("NumberOfSeen");
		}
		return Integer.toString(numberOfSeen);
	}
}
