package ac.bd.keepyournotes;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

 
	
	//http://www.appsrox.com/android/tutorials/remindme/#1
	
	@SuppressWarnings("deprecation")
	@Override
 	public void onReceive(Context context, Intent intent) {
	 	
		 Toast.makeText(context, "Keep Your Notes\n    Reminder!", Toast.LENGTH_LONG).show();
		 
		 long id = intent.getLongExtra("id", 0);
		 
		 String title = intent.getStringExtra("title");
		 String note = intent.getStringExtra("note");
	      
	     Notification n = new Notification(R.drawable.icon, title+"\n"+note ,System.currentTimeMillis());
	     PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
	      
	     n.setLatestEventInfo(context, "Keep Your Notes", title, pi);
	     // TODO check user preferences
	     n.defaults |= Notification.DEFAULT_VIBRATE;
	     n.defaults |= Notification.DEFAULT_SOUND;       
	     n.flags |= Notification.FLAG_AUTO_CANCEL;       
	      
	     NotificationManager nm = (NotificationManager) 
	                                 context.getSystemService(Context.NOTIFICATION_SERVICE);
	     nm.notify((int)id, n);
		 
 	}
 
}
