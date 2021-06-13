package tr.yildiz.wardrobe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wardrobeAppDB";
    private static final String TABLE_DRAWER = "drawers";
    private static final String KEY_DRAWERID = "drawerid";
    private static final String KEY_DRAWERNAME = "drawername";

    private static final String TABLE_CLOTHES = "clothes";
    private static final String KEY_CLOTHINGID = "clothingid";
    private static final String KEY_TYPE = "type";
    private static final String KEY_COLOR = "color";
    private static final String KEY_TEXTURE = "texture";
    private static final String KEY_BUYDATE = "buydate";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PHOTO = "photo";

    private static final String TABLE_CLOTHESDRAWERS = "clothesdrawers";
    //private static final String KEY_CLOTHINGID = "clothingid";
    //private static final String KEY_DRAWERID = "drawerid";

    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVENTID = "eventid";
    private static final String KEY_EVENTNAME = "eventname";
    private static final String KEY_EVENTTYPE = "eventtype";
    private static final String KEY_EVENTDATE = "eventdate";
    private static final String KEY_EVENTLOCATION = "eventlocation";
    private static final String KEY_EVENTDRESS = "eventdress"; //presuming this is to keep track of an individual user's dress history

    private static final String TABLE_CLOTHESEVENTS = "clothesevents";
    //private static final String KEY_CLOTHINGID = "clothingid";
    //private static final String KEY_EVENTID = "eventid";

    private static final String TABLE_COMBINATIONS = "combinations";
    private static final String KEY_COMBINATIONID = "combinationid";
    private static final String KEY_COMBINATIONNAME = "combinationname";
    private static final String KEY_HEAD = "head";
    private static final String KEY_FACE = "face";
    private static final String KEY_UPPERBODY = "upper";
    private static final String KEY_LOWERBODY = "lower";
    private static final String KEY_FEET = "feet";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_DRAWER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DRAWER + "("
                + KEY_DRAWERID + " INTEGER PRIMARY KEY," + KEY_DRAWERNAME + " TEXT" + ")";
        db.execSQL(CREATE_DRAWER_TABLE);

        String CREATE_CLOTHES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLOTHES + "("
                + KEY_CLOTHINGID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT,"
                + KEY_COLOR + " TEXT," + KEY_TEXTURE + " TEXT," + KEY_BUYDATE + " TEXT,"
                + KEY_PRICE + " TEXT," + KEY_PHOTO + " TEXT" + ")";
        db.execSQL(CREATE_CLOTHES_TABLE);

        String CREATE_CLOTHESDRAWER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLOTHESDRAWERS + "("
                + KEY_DRAWERID + " INTEGER," + KEY_CLOTHINGID + " INTEGER" + ")";
        db.execSQL(CREATE_CLOTHESDRAWER_TABLE);

        String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + "("
                + KEY_EVENTID + " INTEGER PRIMARY KEY," + KEY_EVENTNAME + " TEXT,"
                + KEY_EVENTTYPE + " TEXT," + KEY_EVENTDATE + " TEXT,"
                + KEY_EVENTLOCATION + " TEXT," + KEY_EVENTDRESS + " INTEGER" + ")";
        db.execSQL(CREATE_EVENT_TABLE);

        String CREATE_CLOTHESEVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLOTHESEVENTS + "("
                + KEY_EVENTID + " INTEGER," + KEY_CLOTHINGID + " INTEGER" + ")";
        db.execSQL(CREATE_CLOTHESEVENT_TABLE);

        String CREATE_COMBINATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COMBINATIONS + "("
                + KEY_COMBINATIONID + " INTEGER PRIMARY KEY," + KEY_COMBINATIONNAME + " TEXT,"
                + KEY_HEAD + " TEXT," + KEY_FACE + " TEXT,"
                + KEY_UPPERBODY + "  TEXT," + KEY_LOWERBODY + " TEXT," + KEY_FEET +  " TEXT" + ")";
        db.execSQL(CREATE_COMBINATIONS_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAWER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOTHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOTHESDRAWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMBINATIONS);


        onCreate(db);
    }

    public void addDrawer(String drawername) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRAWERNAME, drawername);

        db.insert(TABLE_DRAWER, null, values);
        db.close();
    }

    public void deleteDrawer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRAWER, KEY_DRAWERID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public List<Drawer> getAllDrawers() {
        List<Drawer> drawerList = new ArrayList<Drawer>();
        String selectQuery = "SELECT  * FROM " + TABLE_DRAWER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Drawer drawer = new Drawer(cursor.getString(1));
                drawer.setId(Integer.parseInt(cursor.getString(0)));
                drawerList.add(drawer);
            } while (cursor.moveToNext());
        }

        return drawerList;
    }

    public void addDress(Dress dress) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, dress.getType());
        values.put(KEY_COLOR, dress.getColor());
        values.put(KEY_TEXTURE, dress.getTexture());
        values.put(KEY_BUYDATE, dress.getBuyDate());
        values.put(KEY_PRICE, dress.getCost());
        values.put(KEY_PHOTO, dress.getPhoto());

        db.insert(TABLE_CLOTHES, null, values);
        db.close();
    }

    public void deleteDress(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLOTHES, KEY_CLOTHINGID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void updateDress(Dress dress) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, dress.getType());
        values.put(KEY_COLOR, dress.getColor());
        values.put(KEY_BUYDATE, dress.getBuyDate());
        values.put(KEY_PRICE, dress.getCost());
        values.put(KEY_PHOTO, dress.getPhoto());

        db.update(TABLE_CLOTHES, values, KEY_CLOTHINGID + " = ?",
                new String[]{String.valueOf(dress.getId())});
    }

    public List<Dress> getAllDresses() {
        List<Dress> dressList = new ArrayList<Dress>();
        String selectQuery = "SELECT  * FROM " + TABLE_CLOTHES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Dress dress = new Dress(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                dress.setId(Integer.parseInt(cursor.getString(0)));
                dressList.add(dress);
            } while (cursor.moveToNext());
        }

        return dressList;
    }

    public void addDressToDrawer(int drawerID, int dressID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRAWERID, drawerID);
        values.put(KEY_CLOTHINGID, dressID);

        db.insert(TABLE_CLOTHESDRAWERS, null, values);
        db.close();
    }

    public List<Dress> getDrawerDresses(int id) {
        List<Dress> dressList = new ArrayList<Dress>();
        String selectQuery = "SELECT  * FROM " + TABLE_CLOTHES + ", " + TABLE_CLOTHESDRAWERS + " where " +
                "?" + " = " +TABLE_CLOTHESDRAWERS + "." + KEY_DRAWERID + " and " + TABLE_CLOTHESDRAWERS + "." + KEY_CLOTHINGID
                + " = " + TABLE_CLOTHES + "." + KEY_CLOTHINGID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                Dress dress = new Dress(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                dress.setId(Integer.parseInt(cursor.getString(0)));
                dressList.add(dress);
            } while (cursor.moveToNext());
        }

        return dressList;
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENTNAME, event.getEventName());
        values.put(KEY_EVENTTYPE, event.getEventType());
        values.put(KEY_EVENTDATE, event.getEventDate());
        values.put(KEY_EVENTLOCATION, event.getEventLocation());

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public void deleteEvent(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_EVENTID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENTNAME, event.getEventName());
        values.put(KEY_EVENTTYPE, event.getEventType());
        values.put(KEY_EVENTDATE, event.getEventDate());
        values.put(KEY_EVENTLOCATION, event.getEventLocation());

        db.update(TABLE_EVENTS, values, KEY_EVENTID + " = ?",
                new String[]{String.valueOf(event.getEventId())});
    }

    public Event getEvent(int id){
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " where " + KEY_EVENTID + " = ?";
        Event event;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(id)});

        if (cursor.moveToFirst()) {
            event = new Event(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            event.setEventId(Integer.parseInt(cursor.getString(0)));
            return event;
        }

        return null;
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                event.setEventId(Integer.parseInt(cursor.getString(0)));
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        return eventList;
    }


    public void addDressToEvent(int eventID, int dressID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENTID, eventID);
        values.put(KEY_CLOTHINGID, dressID);

        db.insert(TABLE_CLOTHESEVENTS, null, values);
        db.close();
    }

    public void deleteAllDressesFromEvent(int eventID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLOTHESEVENTS, KEY_EVENTID + " = ?",
                new String[] { String.valueOf(eventID) });
        db.close();
    }

    public List<Integer> getEventDresses(int id) {
        List<Integer> dressList = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM " + TABLE_CLOTHES + ", " + TABLE_CLOTHESEVENTS + " where " +
                "?" + " = " +TABLE_CLOTHESEVENTS + "." + KEY_EVENTID + " and " + TABLE_CLOTHESEVENTS + "." + KEY_CLOTHINGID
                + " = " + TABLE_CLOTHES + "." + KEY_CLOTHINGID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                Dress dress = new Dress(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                dress.setId(Integer.parseInt(cursor.getString(0)));
                dressList.add(dress.getId());
            } while (cursor.moveToNext());
        }

        return dressList;
    }

}
