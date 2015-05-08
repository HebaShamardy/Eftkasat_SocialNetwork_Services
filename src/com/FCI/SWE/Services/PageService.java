package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.PageEntity;
import com.FCI.SWE.ServicesModels.PostEntity;

@Path("/page")
@Produces(MediaType.TEXT_PLAIN)
public class PageService {
	@SuppressWarnings("unchecked")
	@POST
	@Path("/CreatePageService")
	public String createPageService(@FormParam("name") String name,
			@FormParam("type") String type,
			@FormParam("category") String category,
			@FormParam("uname") String uname) {
		String result = PageEntity.createPage(name, type, category, uname);
		JSONObject object = new JSONObject();
		object.put("Status", result);
		return object.toString();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/LikePageService")
	public String LikePageService(@FormParam("pageId") long pageID,
			@FormParam("username") String uname) {
		String result = PageEntity.likePage(pageID, uname);
		JSONObject object = new JSONObject();
		object.put("Status", result);
		return object.toString();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/UnlikePageService")
	public String UnlikePageService(@FormParam("pageId") long pageID,
			@FormParam("username") String uname) {
		String result = PageEntity.unlikePage(pageID, uname);
		JSONObject object = new JSONObject();
		object.put("Status", result);
		return object.toString();
	}

	@POST
	@Path("/showUsersLikePage")
	public String showUsersLikePage(@FormParam("pageId") long pageID) {
		JSONArray array = PageEntity.getPageActiveUsers(pageID);
		return array.toJSONString();
	}

	@POST
	@Path("/ShowUnlikedPages")
	public String ShowUnlikedPages(@FormParam("username") String uname) {
		JSONArray array = PageEntity.showUnlikedPgaes(uname);
		return array.toJSONString();
	}

	@POST
	@Path("/GetPostSeen")
	public String GetPostSeen(@FormParam("postId") int postID) {
		String seenNymber = PageEntity.getPostSeen(postID);
		return seenNymber;
	}
}
