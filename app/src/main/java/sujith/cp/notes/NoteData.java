package sujith.cp.notes;

/**
 * Created by sujith on 9/13/15.
 */
public class NoteData {
    String title,data,date;
    boolean locked;
    NoteData(String tit,String dat,String date)
    {
        title=tit;
        data=dat;
        this.date=date;
    }
}
