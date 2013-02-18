package com.example.bato;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Notification_evening extends Service { //code to set a notification for the morning, based on the time set 
	 	
	@Override
	public void onCreate() 
	{	//where I wanna go when the noti is touched
	
	Intent resultIntent=new Intent(this, ScreenSlidePagerActivity.class);
	PendingIntent pIntent=PendingIntent.getActivity(this,0,resultIntent,0);
	
	//create the notification 
	Notification noti_builder= new Notification.Builder(this)
	.setContentTitle("Write in your diary!")
	.setContentText("Don't forget to write in your diary for today!")
	.setSmallIcon(R.drawable.ic_launcher)
	.setContentIntent(pIntent)
	.build();
	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);	//what does this do!?

    //hide the notification after it is selected!
    noti_builder.flags |=Notification.FLAG_AUTO_CANCEL;
    //notify!!
    notificationManager.notify(1,noti_builder); 

	}
	@Override
		public IBinder onBind(Intent intent) {
		return null;
		}
	
}