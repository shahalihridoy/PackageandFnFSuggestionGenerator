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
        db.execSQL("create table if not exists call_history(number varchar, duration varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS call_history");
        onCreate(db);
    }

    public boolean insertdata(String number, String duration)
    {
        SQLiteDatabase sdb=this.getWritableDatabase();
        sdb.execSQL("insert into call_history values('"+number+"','"+duration+"')");
        return true;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select number,sum(duration) from call_history group by number order by sum(duration) DESC", null);
        return c;
    }
    public void deleteTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS call_history");
        onCreate(db);
    }
}
