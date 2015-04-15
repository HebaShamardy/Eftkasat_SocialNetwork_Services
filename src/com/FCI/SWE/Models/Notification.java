package com.FCI.SWE.Models;

import com.FCI.SWE.ServicesModels.NotificationEntity;

public abstract class Notification 
{
	public abstract String excute(NotificationEntity Entity , String uemail);
}
