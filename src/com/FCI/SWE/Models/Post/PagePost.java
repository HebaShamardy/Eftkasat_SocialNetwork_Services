package com.FCI.SWE.Models.Post;

import com.FCI.SWE.Models.Privacy.Privacy;
import com.FCI.SWE.ServicesModels.PageEntity;

public class PagePost implements Post {

	@Override
	public long excute(String content, long adminId, long pageId,
			String pageName, String feeling) {
		long postId = PageEntity.addPost(pageId, pageName, "page", content,
				adminId, feeling);
		return postId;
	}

}
