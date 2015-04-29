package com.FCI.SWE.Models.Privacy;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.PostEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

public class PublicPrivacy extends Privacy {

	@Override
	public void savePostAudience(long postId, ArrayList<Integer> usersId) {
		PostEntity.saveAudienceForPost(postId, usersId);
		
	}
	
	@Override
	public ArrayList<Integer> getAudience(ArrayList<String> audience, ArrayList<Integer> audienceId, long activeUserId){
		audienceId = UserEntity.getAllUsersIds();
		return audienceId;

	}



}
