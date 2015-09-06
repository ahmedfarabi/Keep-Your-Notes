package ac.bd.keepyournotes;


import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ShowNotes extends ListActivity {	
	
	private NoteInput noteInput;
	private ViewNote viewNote;
    int backButtonCount = 0;
    static final int DIALOG_REMOVE = 1;
    static Note noteX;
    private ListView list;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noteInput = new FileInput(this);
		
		viewNote = new ViewNote();
		setContentView(R.layout.list_1st);
		
	
		list = (ListView)findViewById(android.R.id.list);
		
		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(addButtonListener);
		ArrayList<Note> noteList = noteInput.getAll();
		
		
		// fills the list with data from noteInput	
		setListAdapter(new ArrayAdapter<Note>(this,android.R.layout.simple_list_item_1,noteList));
		list.setEmptyView((TextView)findViewById(R.id.empty)); 
		
		// setup a long-click listener
		list.setOnItemLongClickListener(longClickListener);
		
	}
	
	

	private OnClickListener addButtonListener = new OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(ShowNotes.this, NotesActivity.class));
		}
	};
	
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Note note = noteInput.getAll().get(position);
		viewNote.view(note);
		startActivity(new Intent(ShowNotes.this,ViewNote.class));
	}
	
	
	
	// update the adapter
	void updateAdapter() {
		setListAdapter(new ArrayAdapter<Note>(this,android.R.layout.simple_list_item_1, noteInput.getAll()));
	}
	
	
	//http://stackoverflow.com/questions/8846707/how-to-implement-a-long-click-listener-on-a-listview
	
	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@SuppressWarnings("deprecation")
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			noteX = noteInput.getAll().get(position);	
			showDialog(DIALOG_REMOVE);
			return true;
		}
		
	};
	
	//Lab-2 & 3 Android tic-tac-toe activity
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(id) {
		case DIALOG_REMOVE:
			// Create the quit confirmation dialog
			builder.setMessage(R.string.remove_question)
			.setCancelable(false)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					noteInput.remove(noteX);
					updateAdapter();
				}
			})
			.setNegativeButton(R.string.no, null);
		dialog = builder.create();
		break;
		}
		return dialog;
	}
	
	
	//http://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
	
	public void onBackPressed()
	  {

		if(backButtonCount >= 1)
	      {
	          Intent intent = new Intent(Intent.ACTION_MAIN);
	          intent.addCategory(Intent.CATEGORY_HOME);
	          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	          startActivity(intent);
	          System.exit(1);
	      }
	      else
	      {
	          Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
	          backButtonCount++;
	      }
	  }
	
}