package com.FCI.SWE.Models.Privacy;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.PostEntity;
import com.FCI.SWE.ServicesModels.UserEntity;

public class FriendsPrivacy extends Privacy {

	@Override
	public void savePostAudience(long postId, ArrayList<Integer> usersId) {
		PostEntity.saveAudienceForPost(postId, usersId);
		
	}

	@Override
	public ArrayList<Integer> getAudience(String audience, ArrayList<Integer> audienceId, long activeUserId){
		JSONArray array=new JSONArray();
		array = UserEntity.getFriends(activeUserId);
		for(int i=0;i<array.size();i++){
			JSONObject object = new JSONObject();
			object = (JSONObject) array.get(i);
			int id = (int) object.get("id");
			audienceId.add(id);
		}
		return audienceId;

	}

}
