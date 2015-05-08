package com.FCI.SWE.Services;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.Models.Post.FriendTimelinePost;
import com.FCI.SWE.Models.Post.PagePost;
import com.FCI.SWE.Models.Post.Post;
import com.FCI.SWE.Models.Post.UserTimelinePost;
import com.FCI.SWE.Models.Privacy.CustomPrivacy;
import com.FCI.SWE.Models.Privacy.FriendsPrivacy;
import com.FCI.SWE.Models.Privacy.Privacy;
import com.FCI.SWE.Models.Privacy.PublicPrivacy;
import com.FCI.SWE.ServicesModels.HashTagEntity;
import com.FCI.SWE.ServicesModels.PostEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

@Path("/post")
@Produces(MediaType.TEXT_PLAIN)
public class PostServices 
{
	Post post;

	@POST
	@Path("/createPost")
	public String createPost(@FormParam("timelineType") String timelineType,
			@FormParam("Content") String content,
			@FormParam("UserId") long activeUserId,
			@FormParam("timelineId") long timelineId,
			@FormParam("timeline") String timeline,
			@FormParam("Feeling") String feeling,
			@FormParam("privacyType") String privacyType,
			@FormParam("audience") String audience) 
	{
		
		
		
				
		long postId = -1;
		if (timelineType.equals("friendTimeline")) {
			post = new FriendTimelinePost();
			postId = post.excute(content, activeUserId, timelineId, timeline,
					feeling,privacyType,audience);

		} else if (timelineType.equals("userTimeline")) {
			post = new UserTimelinePost();
			postId = post.excute(content, activeUserId, timelineId, timeline,
					feeling,privacyType,audience);

		} else if (timelineType.equals("pageTimeline")) {
			post = new PagePost();
			postId = post.excute(content, activeUserId, timelineId, timeline,
					feeling,privacyType,audience);

		}
		ArrayList<Integer> audienceId = new ArrayList<Integer>();
		Privacy privacy= new PublicPrivacy();
		privacy = privacy.privacyType(privacyType, audienceId , activeUserId);
		audienceId = privacy.getAudience(audience, audienceId, activeUserId);
		privacy.savePostAudience(postId, audienceId);

		if (content.contains("#")) {
			String arr[] = content.split(" ");
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].contains("#")) {
					HashTagEntity.SaveHashtag(postId, arr[i]);
				}
			}

		}

		return "Posted";
	}

	

	
	
	@POST
	@Path("/LikePostService")
	public String LikePostService(@FormParam("ID") long postId,
			@FormParam("ActiveUser") String ActiveUser) {
		String status = PostEntity.LikePost(postId, ActiveUser);
		JSONObject object = new JSONObject();
		object.put("Status", status);
		return object.toString();
	}

	@POST
	@Path("/SharePostService")
	public String SharePostService(@FormParam("ID") long PostId,
			@FormParam("timelineType") String timelineType,
			@FormParam("timeline") String timeline,
			@FormParam("ActiveUserID") long activeUserId,
			@FormParam("privacyType") String privacyType,
			@FormParam("audience") String audience) {
		
		JSONObject object = new JSONObject();
		object = PostEntity.GetcontentPost(PostId);
		String content,feeling;
		content = object.get("content").toString();
		feeling = object.get("feeling").toString();
		long postId = -1;
		if (timelineType.equals("friendTimeline")) {
			post = new FriendTimelinePost();
			postId = post.excute(content, activeUserId, 0, timeline,
					feeling,privacyType,audience);

		} else if (timelineType.equals("userTimeline")) {
			post = new UserTimelinePost();
			postId = post.excute(content, activeUserId, 0, timeline,
					feeling,privacyType,audience);

		}
		

		ArrayList<Integer> audienceId = new ArrayList<Integer>();
		Privacy privacy= new PublicPrivacy();
		privacy = privacy.privacyType(privacyType, audienceId , activeUserId);
		audienceId = privacy.getAudience(audience, audienceId, activeUserId);
		privacy.savePostAudience(postId, audienceId);

		if (content.contains("#")) {
			String arr[] = content.split(" ");
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].contains("#")) {
					HashTagEntity.SaveHashtag(postId, arr[i]);
				}
			}

		}

		return "shared";

	}

}
