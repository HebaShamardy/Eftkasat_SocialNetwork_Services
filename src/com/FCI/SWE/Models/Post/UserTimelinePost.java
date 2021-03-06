package com.FCI.SWE.Models.Post;

import com.FCI.SWE.Models.Privacy.Privacy;
import com.FCI.SWE.ServicesModels.PostEntity;

public class UserTimelinePost implements Post {

	@Override
	public long excute(String content, long activeUserId, long timelineId,
			String timelineUser, String feeling,String privacyType, String audience) {
		long postId = PostEntity.savePost(content, activeUserId, "user",
				timelineUser, feeling,privacyType, audience);
		return postId;
	}

}
