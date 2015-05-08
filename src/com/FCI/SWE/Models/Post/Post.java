package com.FCI.SWE.Models.Post;

import com.FCI.SWE.Models.Privacy.Privacy;

public interface Post {

	public long excute(String content, long activeUserId, long timelineId,
			String timelineUser, String feeling,String privacyType, String audience);
}
