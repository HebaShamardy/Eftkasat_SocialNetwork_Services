package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.HashTagEntity;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class HashTagServices {
	

	@POST
	@Path("/searchHashtagServices")
	public String searchHashtagServices(@FormParam("Content") String content) {
		return HashTagEntity.searchHashtag(content);

	}

	@POST
	@Path("/showTrendsHashtag")
	public String showTrendsHashtagServices() {
		return HashTagEntity.getTrends();
	}
}
