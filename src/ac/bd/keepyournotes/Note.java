package ac.bd.keepyournotes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;



@SuppressWarnings("serial")
public class Note extends Activity implements Serializable {

    private double latitude;
    private double longitude;
    private Date date;
    private String title;
    private Calendar reminder; 
    private String message;
    private int reminder_id =0;

    
    //note constructor
    public Note(double latitude, double longitude, Date date, Calendar reminder,int reminder_id,String title,String message) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.reminder = reminder;
        this.reminder_id = reminder_id;
        this.title = title;
        this.message = message;
    }

   
    public Note(Note note) {
        this.latitude = note.latitude;
        this.longitude = note.longitude;
        this.date = (Date) note.date.clone();
        this.reminder = note.reminder;
        this.reminder_id = note.reminder_id;
        this.title = new String (note.title);
        this.message = new String(note.message);
    }

  
    public Note(Calendar reminder,int reminder_id,String title,String message) {
    	this.title = title;
    	this.reminder = reminder;
    	this.reminder_id = reminder_id;
        this.message = message;
        latitude = 0.0;
        longitude = 0.0;
        date = new Date();
    }

    
  
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Note other = (Note) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (Double.doubleToLongBits(latitude) != Double
                .doubleToLongBits(other.latitude)) {
            return false;
        }
        if (Double.doubleToLongBits(longitude) != Double
                .doubleToLongBits(other.longitude)) {
            return false;
        }
       
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        return true;
    }

    //getter methods
    public Date getDate() {
        return date;
    }

   
    public double getLatitude() {
        return latitude;
    }

   
    public double getLongitude() {
        return longitude;
    }
    
    public Calendar getReminder() {
        return reminder;
    }
    
    public int getReminder_id() {
        return reminder_id;
    }
   
    public String gettitle() {
        return title;
    }

    
    public String getMessage() {
        return message;
    }

   //setter methods
    public void setDate(Date date) {
        this.date = date;
    }

    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public void setReminder(Calendar reminder) {
        this.reminder = reminder;
        //this.reminder_id++;
    }
    
    public void setReminder_id(int reminder_id) {
        this.reminder_id = reminder_id;
        //this.reminder_id++;
    }
    
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
    public void setMessage(String message) {
        this.message = message;
    }
    

    //toString method to print
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
        
        String s = "Date: " + dateFormat.format(this.getDate()) + "\n";
        s += "Title: " + gettitle()+ "\n";
        return s;
    }
}

