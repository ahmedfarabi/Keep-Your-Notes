package ac.bd.keepyournotes;


import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNote extends NotesActivity {
	
	static String t,m;
	private static Calendar c;
	private static Note n;
	int backButtonCount = 0;
	int i = (int) System.currentTimeMillis();
	
	
	public void edit(Calendar reminder,String title,String message){
		
		c=reminder;
		t=title;
		m=message;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);
		
		// get a reference to the EditText created in main.xml
		etTitle = (EditText)findViewById(R.id.etTitle);
		etNote = (EditText)findViewById(R.id.etNote);
		
		// get references to the Buttons created in main.xml
		Button save = (Button) findViewById(R.id.save);
		Button reminder = (Button) findViewById(R.id.reminder);

		// attach ButtonListeners to the Buttons
		save.setOnClickListener(saveButtonListener);
		reminder.setOnClickListener(reminderButtonListener);

		// request access to the shared location service
		locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		etTitle.setText(t);
		etNote.setText(m); 
		
	}
	

	private OnClickListener saveButtonListener = new OnClickListener() {

		public void onClick(View v) {
	
			if (etNote.getText().length() > 0 && etTitle.getText().length() > 0 && reminder == null) {
				Note note = new Note(c,i,etTitle.getText().toString(), etNote.getText().toString());
				Location loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (loc != null) {
					note.setLatitude(loc.getLatitude());
					note.setLongitude(loc.getLongitude());
				}
				noteInput.add(note);
				noteInput.remove(n);
			}
			else if(reminder!=null){
				Note note = new Note(reminder,i,etTitle.getText().toString(), etNote.getText().toString());
				Location loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (loc != null) {
					note.setLatitude(loc.getLatitude());
					note.setLongitude(loc.getLongitude());
				}
				noteInput.add(note);
				noteInput.remove(n);
			}
			startActivity(new Intent(EditNote.this, ShowNotes.class));
			//i++;
		}
	};
	private OnClickListener reminderButtonListener = new OnClickListener() {

		public void onClick(View v) {
			openDatePickerDialog();
		    openTimePickerDialog(false);
		}
	};
	
	void remove(Note note){
		
		n = note;
	}
	
	
	//http://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
	public void onBackPressed()
	  {
	      
		if(backButtonCount >= 1)
	      {
			Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(EditNote.this, ShowNotes.class));
	      }
	      else
	      {
	    	  Toast.makeText(this, "Please save before you exit or your note will be removed.", Toast.LENGTH_SHORT).show();
	          backButtonCount++;
	          
	      }
	  }

}
