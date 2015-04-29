package com.FCI.SWE.Models.Privacy;

import java.util.ArrayList;

import com.FCI.SWE.ServicesModels.PostEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

public class CustomPrivacy extends Privacy {

	@Override
	public void savePostAudience(long postId, ArrayList<Integer> usersId) {
		PostEntity.saveAudienceForPost(postId, usersId);
		
	}
	
	@Override
	public ArrayList<Integer> getAudience(String audience, 
			ArrayList<Integer> audienceId, long activeUserId){
		String delim = "/";
		String[] audienceName = audience.split(delim);
		
		for(int i=0;i<audienceName.length;i++){
			String name = audienceName[i];
			UserEntity user = new UserEntity();
			user = UserEntity.getUserUpdate(name);
			audienceId.add((int) user.getId());
		}
		return audienceId;

	}



}
