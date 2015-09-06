package ac.bd.keepyournotes;

import java.text.SimpleDateFormat;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNote extends NotesActivity implements TextToSpeech.OnInitListener {
	
	private TextView view;
	public  static Note note;
	public  static Note note2;
	private TextToSpeech tts;
	private EditNote editNote;
	private NoteInput noteInput;
	int backButtonCount = 0;
	static final int DIALOG_REMINDER = 0;
	
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_note);
		
		view = (TextView)findViewById(R.id.view);
		view.setText(this.toString());
		editNote = new EditNote();
		noteInput = new FileInput(this);
		
		
		// get references to the Buttons created in main.xml
		Button edit = (Button) findViewById(R.id.edit);
		Button alarm = (Button) findViewById(R.id.alarm);
		Button listen = (Button) findViewById(R.id.listen);
		Button delete = (Button) findViewById(R.id.delete);
		

		// attach ButtonListeners to the Buttons
		edit.setOnClickListener(editButtonListener);
		alarm.setOnClickListener(alarmButtonListener);
		listen.setOnClickListener(listenButtonListener);
		delete.setOnClickListener(deleteButtonListener);
		
		// setup a TTS object
		tts = new TextToSpeech(this, this);
		
	}
	
	void view(Note n){
		 note = n;
	}
	
	
	private OnClickListener editButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub	
			
			editNote.edit(note.getReminder(),note.gettitle(),note.getMessage());
			editNote.remove(note);
			startActivity(new Intent(ViewNote.this,EditNote.class));
			//showNote.updateAdapter();
		}
		
	};
	private OnClickListener alarmButtonListener = new OnClickListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View arg0) {
			showDialog(DIALOG_REMINDER);
		}
		
	};
	private OnClickListener listenButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub		
			//tts.speak(note.getTitle(), TextToSpeech.QUEUE_FLUSH, null);
			tts.speak(note.getMessage(), TextToSpeech.QUEUE_FLUSH, null);
		}
		
	};
	// when app exits, shutdown TTS
		@Override
		public void onDestroy() {
			super.onDestroy();
			if (tts != null) {
				tts.stop();
				tts.shutdown();
			}
		}
	private OnClickListener deleteButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			noteInput.remove(note);
			startActivity(new Intent(ViewNote.this, ShowNotes.class));
		}
		
	};
	
	//http://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
	
	public void onBackPressed()
	  {
	      
		if(backButtonCount >= 1)
	      {
	          Intent intent = new Intent(Intent.ACTION_MAIN);
	          intent.addCategory(Intent.CATEGORY_HOME);
	          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	          startActivity(intent);
	      }
	      else
	      {
	          Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
	          startActivity(new Intent(ViewNote.this, ShowNotes.class));
	          backButtonCount++;
	      }
	  }
		  
	
	//Lab-2 & 3 Android tic-tac-toe activity
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		case DIALOG_REMINDER:
			builder.setTitle(R.string.reminder_choose);
				final CharSequence[] levels = {
					getResources().getString(R.string.reminder_on),
					getResources().getString(R.string.reminder_off)};
					
			
					int selected=-1;
					if(note.getReminder()==null)
						selected=0;
					else 
						selected=1;
			
			builder.setSingleChoiceItems(levels, selected,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						dialog.dismiss(); // Close dialog
						
						switch (item){
						case 0:
							editNote.edit(note.getReminder(),note.gettitle(),note.getMessage());
							editNote.remove(note);
							startActivity(new Intent(ViewNote.this,EditNote.class));
														
							break;
						case 1:
							Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
							PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),note.getReminder_id(), intent, 0);
							AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
							alarmManager.cancel(pendingIntent);
							note2 = new Note(null,note.getReminder_id(),note.gettitle(), note.getMessage());
							noteInput.remove(note);
							noteInput.add(note2);
							startActivity(new Intent(ViewNote.this, ViewNote.class));
							break;
						}
						
						Toast.makeText(getApplicationContext(), levels[item],Toast.LENGTH_SHORT).show();
						
				}
			});
			dialog = builder.create();
			break;
		}
		return dialog;
	}
	
		  
	public String toString() {
		String s = null;
		if(note2 == null){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss z");
        s= "Location: " + "\n";
        s += "Lat: " + note.getLatitude() + "  ";
        s += "Long: " + note.getLongitude() +"\n";
        s += "Date: " + dateFormat.format(note.getDate()) + "\n";
        if(note.getReminder()== null)
        	s += "Reminder: Off" +"\n";
        else
        	s += "Reminder: " + note.getReminder().getTime()+"\n";
        s += "\nTitle: " + note.gettitle()+ "\n"; 
        s += "\nNote:\n" + note.getMessage();
        //return s;
        //note=null;
		}
		else {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy/MM/dd HH:mm:ss z");
	        s = "Location: " + "\n";
	        s += "Lat: " + note2.getLatitude() + "  ";
	        s += "Long: " + note2.getLongitude() +"\n";
	        s += "Date: " + dateFormat.format(note2.getDate()) + "\n";
	        s += "Reminder: Off" +"\n";
	        s += "\nTitle: " + note2.gettitle()+ "\n"; 
	        s += "\nNote:\n" + note2.getMessage();
	        //return s;
	        note2 = null;
		}
		return s;
    }

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}


}
