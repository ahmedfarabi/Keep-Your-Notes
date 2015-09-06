package ac.bd.keepyournotes;

import java.util.Calendar;
import ac.bd.keepyournotes.FileInput;
import ac.bd.keepyournotes.NotesActivity;
import ac.bd.keepyournotes.Note;
import ac.bd.keepyournotes.NoteInput;
import ac.bd.keepyournotes.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NotesActivity extends Activity {

	protected NoteInput noteInput;
	ViewNote viewNote;
	protected EditText etNote,etTitle;
	private TextView title,note;
	protected LocationManager locMgr;
	public Calendar reminder = null;
	int backButtonCount = 0;
	int i = (int) System.currentTimeMillis();
	TimePicker myTimePicker;
	DatePicker myDatePicker;
	Button buttonstartSetDialog;
	TimePickerDialog timePickerDialog;
	DatePickerDialog datePickerDialog;
	Intent intent;
	 
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes);

		
		noteInput = new FileInput(this);
		viewNote = new ViewNote();
		intent= new Intent(this, AlarmReceiver.class);
		
		title=(TextView)findViewById(R.id.title);
		note=(TextView)findViewById(R.id.note);

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
	}

	@Override
	protected void onResume() {
		super.onResume();
		locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locMgr.removeUpdates(locListener);
	}

	private LocationListener locListener = new LocationListener() {
		public void onLocationChanged(Location location) {}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	
	
	 
	private OnClickListener saveButtonListener = new OnClickListener() {

		public void onClick(View v) {
		
			if (etNote.getText().length() > 0 && etTitle.getText().length() > 0 ) {
				Note note = new Note(reminder,i,etTitle.getText().toString(), etNote.getText().toString());
				Location loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (loc != null) {
					note.setLatitude(loc.getLatitude());
					note.setLongitude(loc.getLongitude());
				}
				noteInput.add(note);
				
			}
			else
				toast();
			
			if (etNote.getText().length() > 0 && etTitle.getText().length() > 0 ) {
				startActivity(new Intent(NotesActivity.this, ShowNotes.class));}
			i++;
		}
	};
	void toast(){
		Toast.makeText(this, "            Empty field!\nPlease enter a note with title! ", Toast.LENGTH_SHORT).show();
	}
	
	private OnClickListener reminderButtonListener = new OnClickListener() {

		public void onClick(View v) {
			openDatePickerDialog();
		    openTimePickerDialog(false);
		}
	};
	
	//http://learnandroideasily.blogspot.com/2013/06/datepicker-and-timepicker-dialog-in.html

	 void openTimePickerDialog(boolean is24r){
		  Calendar calendar = Calendar.getInstance();
		  
		  timePickerDialog = new TimePickerDialog(
				  NotesActivity.this, 
		    onTimeSetListener, 
		    calendar.get(Calendar.HOUR_OF_DAY), 
		    calendar.get(Calendar.MINUTE), 
		    is24r);
		  timePickerDialog.setTitle("Set Reminder Time");       
		  timePickerDialog.show();

		 }
		 
		 void openDatePickerDialog(){
			  Calendar calendar = Calendar.getInstance();
			  
			  datePickerDialog = new DatePickerDialog(
					  NotesActivity.this, 
			    onDateSetListener, 
			    calendar.get(Calendar.YEAR), 
			    calendar.get(Calendar.MONTH),
			    calendar.get(Calendar.DATE));
			  datePickerDialog.setTitle("Set Reminder Date");  
			        
			  datePickerDialog.show();

			 }
	
		 Calendar calNow = Calendar.getInstance();
		   Calendar calSet = (Calendar) calNow.clone();

		    OnTimeSetListener onTimeSetListener
		    = new OnTimeSetListener(){

		  @Override
		  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		   
		   calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
		   calSet.set(Calendar.MINUTE, minute);
		   calSet.set(Calendar.SECOND, 0);
		   calSet.set(Calendar.MILLISECOND, 0);
		   
		   reminder(calSet,0);
		  }};
		 
			  
		  OnDateSetListener onDateSetListener
		    = new OnDateSetListener(){

		  public void onDateSet(DatePicker view, int year, int month,int date) {

		   calSet.set(Calendar.YEAR, year);
		   calSet.set(Calendar.MONTH, month);
		   calSet.set(Calendar.DATE, date);
		 
		   reminder(calSet,1);
		  }};
		  
		  public void reminder(Calendar rem,int c){
			  
			  reminder = rem;
			  if(c == 1){
				setAlarm(reminder);
				 
			  }
		  }
		  
		  
		  //http://android-er.blogspot.com/2010/10/simple-example-of-alarm-service-using.html
		  
		  public void setAlarm(Calendar targetCal) {
			  String s = "Check Your Note Titled: ";
			  intent.putExtra("title",s+etTitle.getText().toString());
			  intent.putExtra("note","Note: "+etNote.getText().toString());
			  PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			  AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			  Toast.makeText(getApplicationContext(), "Reminder is set at "+targetCal.getTime(), Toast.LENGTH_SHORT).show();
			  alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
			  //i++;
		    }
		  
		  
		 //http://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
		  /**
		   * Back button listener.
		   * Will close the application if the back button pressed twice.
		   */
		  public void onBackPressed()
		  {
		    
			  if(backButtonCount >= 1)
		      {
				Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(NotesActivity.this, ShowNotes.class));
		      }
		      else
		      {
		    	  Toast.makeText(this, "Please save before you exit or your note will be removed.", Toast.LENGTH_SHORT).show();
		          backButtonCount++;
		      }
		  }
	
}