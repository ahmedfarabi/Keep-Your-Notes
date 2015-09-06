package ac.bd.keepyournotes;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import android.content.Context;

	@SuppressWarnings("serial")
	public class FileInput implements NoteInput {
		
		private Context context;
		private final String FILENAME = "notepad";
			
		public FileInput(Context context) {

			this.context = context;
		}
		
		//http://developer.android.com/reference/java/io/FileInputStream.html
		
		@SuppressWarnings("unchecked")
		private ArrayList<Note> createArrayList() {
			
			// check to see if the file already exists
			File file = new File(context.getFilesDir() + "/" +  FILENAME);
			if (!file.exists()) {
				return new ArrayList<Note>();
			}
			
			// files exists -- read in the stored ArrayList<Note>
			ArrayList<Note> allNotes = null;
			try {
				FileInputStream fis = context.openFileInput(FILENAME);
				ObjectInputStream reader = new ObjectInputStream(fis);
				allNotes = (ArrayList<Note>) reader.readObject();
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return allNotes;
		}
		
		
		private void saveArrayList(ArrayList<Note> allNotes) {
			ObjectOutputStream writer;
			try {
				FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
				writer = new ObjectOutputStream(fos);
				writer.writeObject(allNotes);
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		
		public void add(Note note) {
			ArrayList<Note> allNotes = createArrayList();
			allNotes.add(note);
			saveArrayList(allNotes);
		}

		
		public void remove(Note note) {
			ArrayList<Note> allNotes = createArrayList();
			allNotes.remove(note);
			saveArrayList(allNotes);
		}

		
		
		public ArrayList<Note> getAll() {
			return createArrayList();
		}	
}
