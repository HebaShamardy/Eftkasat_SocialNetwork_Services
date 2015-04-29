package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.ServicesModels.HashTagEntity;
import com.FCI.SWE.ServicesModels.PageEntity;
import com.FCI.SWE.ServicesModels.TimelineEntity;



@Path("/timeline")
@Produces(MediaType.TEXT_PLAIN)
public class TimelineServices {


	@SuppressWarnings("unchecked")
	@POST
	@Path("/getUserTimelinePost")
	public String getUserTimelinePostService(@FormParam("userId") int userID) throws ParseException {
		String result = TimelineEntity.getUserPosts(userID);
		return result;
	}


	@SuppressWarnings("unchecked")
	@POST
	@Path("/getHashtagTimelinePost")
	public String getHashtagTimelinePostService(@FormParam("hashWord") String hashtag) {
		String result = HashTagEntity.searchHashtag(hashtag);
		return result;
	}

	

	@SuppressWarnings("unchecked")
	@POST
	@Path("/getPageTimelinePost")
	public String getHashtagTimelinePostService(@FormParam("pageId") int pageID) {
		String result = TimelineEntity.getPagePosts(pageID);
		return result;
	}

	
}
