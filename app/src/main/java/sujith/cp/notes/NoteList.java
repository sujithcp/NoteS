package sujith.cp.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import java.util.Locale;
import static sujith.cp.notes.R.drawable.speak_ic;

/**
 * Created by sujith on 4/10/15.
 */
public class NoteList extends Activity implements TextToSpeech.OnInitListener {

    ImageView newItem;
    String tmp="";
    Note.DbValues dbVAl;
    TextToSpeech tts;
    static SharedPreferences pref;
    com.baoyz.swipemenulistview.SwipeMenuListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        listView= (com.baoyz.swipemenulistview.SwipeMenuListView) findViewById(R.id.note_list_view);
        newItem=(ImageView)findViewById(R.id.new_item);
        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNote.clear();
                Intent i = new Intent("android.intent.action.EDITNOTE");
                startActivity(i);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Note n = new Note(NoteList.this);
                    n.open();
                    dbVAl = n.getData();
                    NoteView.item = new Item(dbVAl.AL.get(i), n.getData(dbVAl.Dates.get(i)), dbVAl.Dates.get(i));
                    Intent j = new Intent("android.intent.action.NOTEVIEW");
                    startActivity(j);
                    n.close();


                } catch (Exception e) {
                    Log.v("click", tmp);
                }
            }
        });

        SwipeMenuCreator sMenu = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem editItem = new SwipeMenuItem(NoteList.this);
                SwipeMenuItem deleteItem = new SwipeMenuItem(NoteList.this);
                SwipeMenuItem readItem = new SwipeMenuItem(NoteList.this);
                editItem.setWidth(80);
                deleteItem.setWidth(80);
                readItem.setWidth(80);
                //editItem.
                editItem.setIcon(R.drawable.edit_ic);
                deleteItem.setIcon(R.drawable.delete_ic);
                readItem.setIcon(speak_ic);

                editItem.setBackground(R.drawable.title_back);
                deleteItem.setBackground(R.drawable.edit_back);
                readItem.setBackground(R.drawable.title_back);

                swipeMenu.addMenuItem(editItem);
                swipeMenu.addMenuItem(deleteItem);
                swipeMenu.addMenuItem(readItem);

            }
        };

        listView.setMenuCreator(sMenu);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int index) {
                Note N = new Note(NoteList.this);
                try {
                    N.open();
                    dbVAl = N.getData();

                } catch (Exception e) {
                    Log.e("set error", "" + e.getMessage().toString());
                }
                switch (index) {
                    case 0:
                        EditNote.setView(dbVAl.AL.get(i), N.getData(dbVAl.Dates.get(i)), dbVAl.Dates.get(i), i);
                        startActivity(new Intent("android.intent.action.EDITNOTE"));
                        break;
                    case 1:
                        delete(i);
                        setListView();
                        break;
                    case 2:
                        readText(N.getData(dbVAl.Dates.get(i)));
                        break;
                }


                N.close();
                return false;
            }
        });
        setListView();
        tts=new TextToSpeech(NoteList.this,NoteList.this);
        pref=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


    }

    public void setListView()
    {
        Note N = new Note(this);
        try {
            N.open();
            dbVAl=N.getData();

        }
        catch (Exception e)
        {
            Log.e("set error", "" + e.getMessage().toString());
            return;
        }
        listView.setAdapter(new NoteAdapter(getApplicationContext(), N.getData().AL));
        listView.setCloseInterpolator(new BounceInterpolator());
        listView.setOpenInterpolator(new OvershootInterpolator());

        N.close();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts.isSpeaking()) {
            Log.v("","tts stopeed");
            tts.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setListView();
    }

    public void readText(String data)
    {
        try
        {
            Log.v("","Read: "+data);
            tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
        }
        catch (Exception e) {
            Toast.makeText(NoteList.this, "Text to speech error..! " + e.getMessage().toString(), Toast.LENGTH_LONG);
        }
    }

    public void delete(int i)
    {
        final int j=i;
        new AlertDialog.Builder(NoteList.this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Note N=new Note(NoteList.this);
                        try {
                            N.open();
                            N.delete(dbVAl.Dates.get(j));
                            N.close();
                            setListView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onInit(int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tts.setLanguage(Locale.getDefault());
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                new AlertDialog.Builder(NoteList.this)
                        .setTitle("SUJITH C PHILIP")
                        .setMessage("He is an extra ordinary man with immense capabilities.\n\nHis olympic pole-vault record of 6.129m is still unbeaten.\n\nHis brilliant logic is still a mystery for the world...!")
                        .setIcon(R.drawable.me)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

                break;
            case R.id.settings:
                try {
                    startActivity(new Intent("android.intent.action.COLORPREF"));
                }catch (Exception e){Log.e("Pref",e.getMessage()+"");}

        }
        return super.onMenuItemSelected(featureId, item);
    }
}
