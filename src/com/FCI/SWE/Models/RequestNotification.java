package com.FCI.SWE.Models;

import org.json.simple.JSONArray;

import com.FCI.SWE.ServicesModels.NotificationEntity;

public class RequestNotification extends Notification 
{
	
	public RequestNotification(){
		
	}

	@Override
	public String excute(NotificationEntity entity , String uemail) {

		JSONArray array=new JSONArray();
		array = entity.getFriendRequestNotification(uemail);
		return array.toJSONString();
	}

}
