package com.FCI.SWE.Models.Privacy;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.FCI.SWE.ServicesModels.UserEntity;

public class Privacy {
	public void savePostAudience(long postId,ArrayList<Integer> usersId){
		
	}
	
	public Privacy privacyType(String type , ArrayList<Integer> audienceId, long activeUserId){
		Privacy privacy=new PublicPrivacy();
		if (type.equals("friends")) {
			privacy = new FriendsPrivacy();
			JSONArray array=new JSONArray();
			array = UserEntity.getFriends(activeUserId);
			for(int i=0;i<array.size();i++){
				JSONObject object = new JSONObject();
				object = (JSONObject) array.get(i);
				int id = (int) object.get("id");
				audienceId.add(id);
			}

		} else if (type.equals("public")) {
			privacy = new PublicPrivacy();
			audienceId = UserEntity.getAllUsersIds();
		} else if (type.equals("custom")) {
			privacy = new CustomPrivacy();
			

		}
		return privacy;

	}
	
	public ArrayList<Integer> getAudience(ArrayList<String> audience, ArrayList<Integer> audienceId, long activeUserId){
		return null;

	}
}
