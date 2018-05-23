package fr.unice.polytech.polynews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.polynews.models.Mishap;

public class Database extends SQLiteOpenHelper {

    private static final String Mishap_ID = "idMishap";
    private static final String Mishap_TITLE = "titleMishap";
    private static final String Mishap_CATEGORY = "category";
    private static final String Mishap_DESCRIPTION = "description";
    private static final String Mishap_PLACE = "place";
    private static final String Mishap_LATITUDE = "latitude";
    private static final String Mishap_LONGITUDE = "longitude";
    private static final String Mishap_URGENCY = "urgency";
    private static final String Mishap_EMAIL = "email";
    private static final String Mishap_STATE = "state";
    private static final String Mishap_DATE = "dateMishap";
    private static final String Mishap_PHONE = "phone";
    private static final String Mishap_IMAGE1 = "image1";
    private static final String Mishap_IMAGE2 = "image2";
    private static final String Mishap_IMAGE3 = "image3";

    private static final String Mishap_TABLE_NAME = "Mishap";

    private static final String Mishap_CREATE_TABLE =
            "CREATE TABLE "+ Mishap_TABLE_NAME +" ( "+ Mishap_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Mishap_TITLE+" TEXT NOT NULL CHECK (length(titleMishap) > 0),"+
                    Mishap_CATEGORY + " TEXT CHECK (category IN ('Informatique','Eléctrique','Plomberie', 'Propreté', 'Autre')), "+
                    Mishap_DESCRIPTION + " TEXT, " +
                    Mishap_LATITUDE + " DECIMAL(3,10),"+
                    Mishap_LONGITUDE +" DECIMAL(3,10),"+
                    Mishap_URGENCY + " TEXT, "+
                    Mishap_EMAIL +" TEXT, "+
                    Mishap_STATE + " TEXT CHECK (state IN ('Non traité','En cours','Résolu')),"+
                    Mishap_DATE +" TEXT, "+
                    Mishap_PLACE +" TEXT, "+
                    Mishap_PHONE+" TEXT, "+
                    Mishap_IMAGE1+" BLOB, "+
                    Mishap_IMAGE2+" BLOB, "+
                    Mishap_IMAGE3+" BLOB) ";

    private static final String Mishap_INSERT = "INSERT INTO Mishap(titleMishap, category, description, latitude, longitude, urgency, email, state, dateMishap, phone, place, image1, image2, image3) " +
            "VALUES ('A chair is broken', 'Informatique', 'A chair is broken in the room 0+123','37.4219983', '-122.084', 'false', 'marion@etu.unice.fr', 'Non traité', '16/05/18', '', 'E-235', null, null, null);";
    private static final String Mishap_INSERT2 =  " INSERT INTO Mishap(titleMishap, category, description, latitude, longitude, urgency, email, state, dateMishap, phone, place, image1, image2, image3)" +
            "VALUES ('Defaulting distributor', 'Plomberie', 'The distributor in the west building has an important problem, contact me for more details','50.002', '140.5', 'true', 'alexis@etu.unice.fr', 'En cours', '10/05/18', '1651010102', '', null, null, null);";
    private static final String Mishap_INSERT3 = "INSERT INTO Mishap(titleMishap, category, description, latitude, longitude, urgency, email, state, dateMishap, phone, place, image1, image2, image3)" +
            "VALUES ('Video projector problem', 'Autre', 'The video projector in the room E+355 make some strange noise, I think its a good idea to check it', '37.4219983', '-122', 'true', 'ruheureuh@unice.fr', 'Résolu', '14/05/18', '0651010101', 'Amphi Nord', null, null, null);";


    private static final String PHOTO_TABLE_NAME = "photos";
    private static final String PHOTO_ID = "idPhoto";
    private static final String PHOTO_URL = "url";

    private static final String PHOTOS_CREATE_TABLE =
            "CREATE TABLE "+ PHOTO_TABLE_NAME +" ( "+ PHOTO_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    Mishap_ID +" INTEGER NOT NULL,"+
                    PHOTO_URL + " TEXT NOT NULL)";


    private static final String Mishap_DROP_TABLE = "DROP TABLE IF EXISTS " + Mishap_TABLE_NAME+";";


    private static String DB_NAME = "polynews_database";
    private final Context myContext;

    public Database(Context context){
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        //onUpgrade(db,0,0); //RESET LA BDD, A MODIF
        db.execSQL(Mishap_CREATE_TABLE);
        db.execSQL(PHOTOS_CREATE_TABLE);
        db.execSQL(Mishap_INSERT);
        db.execSQL(Mishap_INSERT2);
        db.execSQL(Mishap_INSERT3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Mishap_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null)
            db.close();
        super.close();
    }

    public Mishap getMishap(Cursor c){
        int idMishap = c.getInt(0);
        String titleMishap = c.getString(1);
        String category = c.getString(2);
        String description = c.getString(3);
        double latitude = c.getDouble(4);
        double longitude = c.getDouble(5);
        boolean urgency = Boolean.parseBoolean(c.getString(6));
        String email = c.getString(7);
        String state = c.getString(8);
        String date = c.getString(9);
        String place = c.getString(10);
        String phone = c.getString(11);
        byte[] image1 = c.getBlob(12);
        byte[] image2 = c.getBlob(13);
        byte[] image3 = c.getBlob(14);


        return new Mishap(idMishap, titleMishap, category, description, latitude, longitude,
                urgency, email, state, date, phone, place, image1, image2, image3);
    }

    public Mishap getMishap(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Mishap WHERE " + Mishap_ID  + " = " + id, null);
        c.moveToFirst();
        return getMishap(c);
    }

    public List<Mishap> getAllMishaps(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Mishap", null);
        c.moveToFirst();
        List<Mishap> Mishaps = new ArrayList<>();
        while(!c.isAfterLast()){
            Mishaps.add(getMishap(c));
            c.moveToNext();
        }
        c.close();
        return Mishaps;
    }

    public long addMishap(Mishap Mishap){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(Mishap_TITLE, Mishap.getTitleMishap());
        values.put(Mishap_CATEGORY, Mishap.getCategory());
        values.put(Mishap_DESCRIPTION, Mishap.getDescription());
        values.put(Mishap_LATITUDE, Mishap.getLatitude());
        values.put(Mishap_LONGITUDE, Mishap.getLongitude());
        values.put(Mishap_URGENCY, String.valueOf(Mishap.getUrgency()));
        values.put(Mishap_EMAIL, Mishap.getEmail());
        values.put(Mishap_STATE, Mishap.getState());
        values.put(Mishap_DATE, Mishap.getDate());
        values.put(Mishap_PHONE, Mishap.getPhone());
        values.put(Mishap_PLACE, Mishap.getPlace());
        values.put(Mishap_IMAGE1, Mishap.getImage1());
        values.put(Mishap_IMAGE2, Mishap.getImage2());
        values.put(Mishap_IMAGE3, Mishap.getImage3());

        long id  = db.insert(Mishap_TABLE_NAME, null, values);
        db.close();
        return id;
    }
    /*
        public List<Photo> getPictures(Mishap Mishap){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM photos WHERE " + Mishap_ID + "=" + Mishap.getIdMishap(), null);
            c.moveToFirst();
            List<Photo> photos = new ArrayList<>();
            while(!c.isAfterLast()){
                long idPhoto = c.getLong(0);
                int idMishap = c.getInt(1);
                String url= c.getString(2);
                Photo photo = new Photo((int)idPhoto,idMishap, url);
                photos.add(photo);
                c.moveToNext();
            }
            c.close();
            return photos;
        }
    */
    public void addPicture(long id, String url){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(Mishap_ID, id);
        values.put(PHOTO_URL, url);

        db.insert(PHOTO_TABLE_NAME, null ,values);
        db.close();
    }

    public void deleteMishap(Mishap Mishap){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(Mishap_TABLE_NAME, Mishap_ID +"=?", new String[]{String.valueOf(Mishap.getIdMishap())});
        db.close();
    }
}
