package com.FCI.SWE.ServicesModels;

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

public class MessageEntity {

	public MessageEntity() {

	}

	public static long saveMessage(String uname, String msgContent, long convId) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Message");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		// System.out.println("Size = " + list.size());

		try {
			Entity employee = new Entity("Message", list.size() + 2);

			employee.setProperty("uname", uname);
			employee.setProperty("content", msgContent);
			employee.setProperty("convId", convId);
			datastore.put(employee);

			txn.commit();

		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		txn = datastore.beginTransaction();
		gaeQuery = new Query("Message");
		pq = datastore.prepare(gaeQuery);
		list = pq.asList(FetchOptions.Builder.withDefaults());
		long returnId = -1;

		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("uname").toString().equals(uname)
					&& entity.getProperty("content").toString()
							.equals(msgContent)
					&& entity.getProperty("convId").toString().equals(convId)) {

				returnId = (long) entity.getKey().getId();

			}
		}
		return returnId;

	}

	public static long saveConversation(String title) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Conversation");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		try {
			Entity employee = new Entity("Conversation", list.size() + 2);

			employee.setProperty("ConvTitle", title);

			datastore.put(employee);

			txn.commit();
		} finally {
			
		}
		txn = datastore.beginTransaction();
		gaeQuery = new Query("Conversation");
		pq = datastore.prepare(gaeQuery);
		list = pq.asList(FetchOptions.Builder.withDefaults());
		long returnId = -1;
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("title").toString().equals(title)) {

				returnId = (long) entity.getKey().getId();

			}
		}
		return returnId;
	}

	public static void saveUsersInConversation(String uname, JSONArray users,
			long convId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userInConversation");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		// System.out.println("Size = " + list.size());
		Entity employee = new Entity("userInConversation", list.size() + 2);
		employee.setProperty("uname", uname);
		employee.setProperty("convID", convId);
		datastore.put(employee);
		txn.commit();

		try {
			for (int i = 0; i < users.size(); i++) {
				txn = datastore.beginTransaction();
				gaeQuery = new Query("userInConversation");
				pq = datastore.prepare(gaeQuery);
				list = pq.asList(FetchOptions.Builder.withDefaults());
				employee = new Entity("userInConversation", list.size() + 2);
				JSONObject obj = new JSONObject();
				obj = (JSONObject) users.get(i);
				uname = (String) obj.get("name");
				employee.setProperty("uname", uname);
				employee.setProperty("convID", convId);
				datastore.put(employee);
				txn.commit();
			}

		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

	}

	public static void saveMessageStatus(String uname, long Msgid, long convId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("MessageStatus");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());

		try {
			Entity employee = new Entity("MessageStatus", list.size() + 2);

			employee.setProperty("uname", uname);
			employee.setProperty("convId", convId);
			employee.setProperty("Msgid", Msgid);
			employee.setProperty("Status", "notSeen");
			datastore.put(employee);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

	}

	public static long getConversationId(String title) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("Conversation");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("title").toString().equals(title)) {

				long returnId = (int) entity.getKey().getId();
				return returnId;
			}
		}

		return -1;
	}

	/*
	 * public static String[] getReceivers(long id, String uname) { int size =
	 * 1000; int count = 0; long tempId = 0; String msg = null; String[] arr =
	 * new String[size]; DatastoreService datastore = DatastoreServiceFactory
	 * .getDatastoreService(); MessageEntity m = new MessageEntity(); Query
	 * gaeQuery = new Query("Notify"); PreparedQuery pq =
	 * datastore.prepare(gaeQuery); for (Entity entity : pq.asIterable()) { if
	 * (entity.getProperty("convId").equals(id)) { String temp =
	 * entity.getProperty("convId").toString(); tempId = Integer.parseInt(temp);
	 * arr[count] = entity.getProperty("uname").toString(); count++; } msg +=
	 * msg; m.SaveMessage(uname, msg, tempId); for (int i = 0; i < arr.length;
	 * i++) { if (arr[i].equalsIgnoreCase(uname)) { // arraylist } }
	 * 
	 * }
	 * 
	 * return null;
	 * 
	 * }
	 */

	/*
	 * public static void msgStatus(String uname, String msgContent, long
	 * convId) { long MsgId=0; DatastoreService datastore =
	 * DatastoreServiceFactory .getDatastoreService(); MessageEntity m = new
	 * MessageEntity(); Query gaeQuery = new Query("Message"); PreparedQuery pq
	 * = datastore.prepare(gaeQuery); for (Entity entity : pq.asIterable()) { if
	 * (entity.getProperty("uname").toString().equals(uname)) { String temp =
	 * entity.getProperty("Msgid").toString(); MsgId = Integer.parseInt(temp);
	 * 
	 * } // long id = entity.getProperty("Msgid").toString();
	 * m.MessageStatus(uname, MsgId, "Not Seen", convId);
	 * 
	 * 
	 * }
	 * 
	 * }
	 */

}
