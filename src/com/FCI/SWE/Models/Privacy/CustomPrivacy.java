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
	public ArrayList<Integer> getAudience(ArrayList<String> audience, 
			ArrayList<Integer> audienceId, long activeUserId){
		
		for(int i=0;i<audience.size();i++){
			String name = audience.get(i);
			UserEntity user = new UserEntity();
			user = UserEntity.getUserUpdate(name);
			audienceId.add((int) user.getId());
		}
		return audienceId;

	}



}
