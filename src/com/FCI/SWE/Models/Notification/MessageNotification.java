package com.FCI.SWE.Models.Notification;

import org.json.simple.JSONArray;

import com.FCI.SWE.ServicesModels.NotificationEntity;

public class MessageNotification extends Notification {
	@Override
	public String excute(NotificationEntity entity, String uname) {
		JSONArray array = new JSONArray();
		array = entity.getMessageNotification(uname);
		return array.toJSONString();
	}

}
