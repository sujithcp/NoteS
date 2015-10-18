package sujith.cp.notes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

/**
 * Created by sujith on 4/10/15.
 */
public class NoteAdapter extends BaseAdapter {

    ArrayList<String> AL;
    Context mContext;
    public   static LayoutInflater inflater;
    public NoteAdapter(Context context,ArrayList<String> al)
    {
        mContext=context;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AL=al;
    }

    @Override
    public int getCount() {
        return AL.size();
    }

    @Override
    public Object getItem(int i) {
        return AL.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        String title=AL.get(i);
        if(title.length()<1)
            title="#####";
        Log.v("","Note: "+title);
        View v = convertView;
        if(v==null)
            v=inflater.inflate(R.layout.list_item,null);
        TextView noteTitle=(TextView)v.findViewById(R.id.note_title);
        ImageView notePic=(ImageView)v.findViewById(R.id.item_pic);
        TextDrawable td = TextDrawable.builder()
                .beginConfig()
                .bold()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound((""+title.charAt(0)).toUpperCase(), ColorGenerator.MATERIAL.getColor(""+title));
        notePic.setImageDrawable(td);
        noteTitle.setText(title);

        return v;

    }
}
