package sujith.cp.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sujith on 4/10/15.
 */
public class NoteView extends Activity {

    TextView noteText,titleText;
    static Item item=null;
    static String note="",title="";
    ImageView editButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_view);
        editButton= (ImageView) findViewById(R.id.edit);
        titleText= (TextView) findViewById(R.id.note_view_title);
        noteText= (TextView) findViewById(R.id.note_view);

        try
        {
            if(item.title.length()>14)
                titleText.setText(item.title.substring(0,12)+"...");
            else
                titleText.setText(item.title);
            noteText.setText(item.data);
        }
        catch(Exception e){Log.e("e", ""+e.getMessage());}

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNote.setView(item.title, item.data, item.date, 1);
                startActivity(new Intent("android.intent.action.EDITNOTE"));
                finish();
            }
        });

    }


}
