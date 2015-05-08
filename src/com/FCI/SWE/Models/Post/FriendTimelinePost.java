package com.FCI.SWE.Models.Post;

import javax.ws.rs.FormParam;

import com.FCI.SWE.Models.Privacy.Privacy;
import com.FCI.SWE.ServicesModels.PostEntity;

public class FriendTimelinePost implements Post {

	@Override
	public long excute(String content, long activeUserId, long timelineId,
			String timelineUser, String feeling,String privacyType, String audience) {
		long postId = PostEntity.savePost(content, activeUserId, "friend",
				timelineUser, feeling,privacyType, audience);
		
		return postId;
	}

}
