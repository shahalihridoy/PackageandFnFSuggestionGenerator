package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Md Shah Ali on 31-Mar-18.
 */

public class Database extends SQLiteOpenHelper {

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists call_history(number varchar, duration varchar, callTime varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS call_history");
        onCreate(db);
    }

    public boolean insertdata(String number, String duration, String time)
    {
        SQLiteDatabase sdb=this.getWritableDatabase();
        sdb.execSQL("insert into call_history values('"+number+"','"+duration+"','"+time+"')");
        return true;
    }

    public Cursor tenSecondPulse()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select number,sum(round(duration/10+0.5)*10),callTime from call_history group by number order by sum(round(duration/10+0.5)*10) DESC", null);
        return c;
    }
    public Cursor oneSecondPulse(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select number,sum(duration),callTime from call_history group by number order by sum(duration) DESC", null);
        return c;
    }
    public Cursor rawData(){
        return this.getReadableDatabase().rawQuery("select number,duration,callTime from call_history",null);
    }
    public void deleteTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS call_history");
        onCreate(db);
    }
}
