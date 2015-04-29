package com.FCI.SWE.Models.Notification;

import com.FCI.SWE.ServicesModels.NotificationEntity;

public abstract class Notification {
	public abstract String excute(NotificationEntity Entity, String uemail);
}
