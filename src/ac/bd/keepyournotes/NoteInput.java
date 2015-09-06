package ac.bd.keepyournotes;

import java.util.ArrayList;
import java.io.Serializable;

public interface NoteInput extends Serializable {
	public void add(Note note);
	public void remove(Note note);
	public ArrayList<Note> getAll();
}
