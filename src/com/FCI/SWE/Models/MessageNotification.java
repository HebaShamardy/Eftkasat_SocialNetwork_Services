package com.FCI.SWE.Models;

import org.json.simple.JSONArray;

import com.FCI.SWE.ServicesModels.NotificationEntity;

public class MessageNotification extends Notification 
{
	@Override
	public String excute(NotificationEntity entity ,  String uemail) 
	{
		JSONArray array=new JSONArray();
		array = entity.getMessageNotification(uemail);
		return array.toJSONString();
	}

}
