package sujith.cp.notes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sujith on 4/10/15.
 */
public class EditNote extends Activity {
    ImageView okButton;
    EditText noteField,titleField;
    static String fName="",fContent="",date="";
    static int index=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit_ui);
        okButton= (ImageView) findViewById(R.id.ok);
        noteField= (EditText) findViewById(R.id.noteText);
        titleField= (EditText) findViewById(R.id.note_edit_title);
        noteField.setText(fContent);
        titleField.setText(fName);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = titleField.getText().toString();
                    String dat = noteField.getText().toString();
                    if(name.length()<1) {
                        if (dat.length() < 14)
                            name = dat;
                        else
                            name=(dat.substring(0,11)+"...");
                    }
                    Note n = new Note(getBaseContext());
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    n.open();
                    if(index==-1) {
                        NoteData noteObj = new NoteData(name, dat, formattedDate);
                        n.createEntry(noteObj);
                    }
                    else
                    {
                        NoteData noteObj = new NoteData(name, dat, date);
                        n.editEntry(noteObj);
                    }
                    n.close();
                } catch (Exception e) {
                    Log.v("", "Something" + e.getMessage().toString());
                } finally{
                    finish();
                    index=-1;
                }
            }
        });




    }

    public static void setView(String name,String dat,String date,int i)
    {
        fName=name;
        fContent=dat;
        EditNote.date=date;
        index=i;

    }
    public static void setView(String name,String dat)
    {
        fName=name;
        fContent=dat;
        index=-1;

    }

    public static void clear()
    {
        fName="";
        fContent="";
        EditNote.date=date;
        index=-1;
    }
}
