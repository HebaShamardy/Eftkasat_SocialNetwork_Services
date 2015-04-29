package com.FCI.SWE.Services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.FCI.SWE.Models.Notification.MessageNotification;
import com.FCI.SWE.Models.Notification.Notification;
import com.FCI.SWE.Models.Notification.RequestNotification;
import com.FCI.SWE.ServicesModels.NotificationEntity;

/**
 * 
 * @author Heba NotificationHandler using command design pattern
 */

@Path("/notification")
@Produces(MediaType.TEXT_PLAIN)
public class NotificationServices {
	private Notification notification;

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	@POST
	@Path("/notification")
	public String newNotificationHandler(@FormParam("type") String type,
			@FormParam("uemail") String uemail) {
		NotificationEntity notificationEntity = new NotificationEntity();
		if (type.equals("getRequestNotification")) {
			this.setNotification(new RequestNotification());
		}
		if (type.equals("getMessageNotification")) {
			this.setNotification(new MessageNotification());
		} else {
			return "NotValidType";
		}
		return this.notification.excute(notificationEntity, uemail);

	}

}
