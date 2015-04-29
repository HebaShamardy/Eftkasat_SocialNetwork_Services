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

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("name").equals(name))
				return "Found";
		}
		try {
			datastore.prepare(gaeQuery);
			Entity employee = new Entity("Page");
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

	public static String likePage(int pageID, String uname) {
		int likesNumber = 0, ID = 0;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userPageLikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			ID = (int) k.getId();
			if (entity.getProperty("username").equals(uname) && ID == pageID)
				return "AlreadyLiked";
		}

		gaeQuery = new Query("Page");
		pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			ID = (int) k.getId();

			if (ID == pageID) {
				likesNumber = (int) entity.getProperty("numberOfLikes");
				likesNumber++;
				entity.setProperty("numberOfLikes", likesNumber);
				datastore.put(entity);
				txn.commit();
			}
		}
		if (txn.isActive()) {
			txn.rollback();
		}
		Query gaeQuery1 = new Query("userPageLikes");
		PreparedQuery pq1 = datastore.prepare(gaeQuery1);
		Entity employee = new Entity("userPageLikes");
		employee.setProperty("pageId", pageID);
		employee.setProperty("username", uname);
		datastore.put(employee);
		txn.commit();
		return "Liked";
	}

	public static String unlikePage(int pageID, String uname) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userPageLikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("pageId").toString().equals(pageID)
					&& entity.getProperty("username").toString().equals(uname)) {
				Key k = entity.getKey();
				datastore.delete(k);
				txn.commit();
				return "Unliked";
			}
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		int ID = -1, likesNumber = 0;
		gaeQuery = new Query("Page");
		pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			ID = (int) k.getId();

			if (ID == pageID) {
				likesNumber = (int) entity.getProperty("numberOfLikes");
				likesNumber--;
				entity.setProperty("numberOfLikes", likesNumber);
				datastore.put(entity);
				txn.commit();
			}
		}
		if (txn.isActive()) {
			txn.rollback();
		}
		return "Failed to found";
	}

	public static JSONArray getPageActiveUsers(int pageID) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		JSONArray likedUsers = new JSONArray();

		Query gaeQuery = new Query("userPageLikes");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("pageId").toString().equals(pageID)) {
				JSONObject object = new JSONObject();
				String uname = entity.getProperty("username").toString();
				object.put("username", uname);
				likedUsers.add(object);
			}
		}
		return likedUsers;
	}

	public static JSONArray showUnlikedPgaes(String uname) {
		ArrayList<Integer> pagesIDs = new ArrayList<Integer>();
		ArrayList<Integer> unlikedPagesIDs = new ArrayList<Integer>();
		JSONArray unlikedPagesNames = new JSONArray();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("Page");
		PreparedQuery pq = datastore.prepare(gaeQuery);

		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			pagesIDs.add((int) k.getId());
		}
		datastore = DatastoreServiceFactory.getDatastoreService();
		// Transaction txn = datastore.beginTransaction();
		gaeQuery = new Query("userPageLikes");
		pq = datastore.prepare(gaeQuery);

		for (int i = 0; i < pagesIDs.size(); i++) {
			for (Entity entity : pq.asIterable()) {
				if ((entity.getProperty("pageId").toString().equals(pagesIDs
						.get(i)))
						&& entity.getProperty("username").toString()
								.equals(uname))
					continue;
				else
					unlikedPagesIDs.add(pagesIDs.get(i));
			}
		}
		gaeQuery = new Query("Page");
		pq = datastore.prepare(gaeQuery);

		for (int i = 0; i < unlikedPagesIDs.size(); i++) {
			for (Entity entity : pq.asIterable()) {
				if (entity.getProperty("pageId").toString()
						.equals(unlikedPagesIDs.get(i))) {
					JSONObject object = new JSONObject();
					String pageName = entity.getProperty("name").toString();
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
		String numberOfSeen = "";
		for (Entity entity : pq.asIterable()) {
			Key k = entity.getKey();
			int id = (int) k.getId();
			if (id == postID)
				numberOfSeen = entity.getProperty("NumberOfSeen").toString();
		}
		return numberOfSeen;
	}
}
