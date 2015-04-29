package com.FCI.SWE.Models.Notification;

import org.json.simple.JSONArray;

import com.FCI.SWE.ServicesModels.NotificationEntity;

public class LikePostNotification extends Notification {

	@Override
	public String excute(NotificationEntity Entity, String uname) {
		

		JSONArray array = new JSONArray();
		array = Entity.getPostNotification(uname);
		return array.toJSONString();
	}

}
