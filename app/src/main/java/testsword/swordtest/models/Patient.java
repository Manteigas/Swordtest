package testsword.swordtest.models;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by André Manteigas on 25/09/2017.
 */

public class Patient {
    private String name;
    private GregorianCalendar birthday;
    private String address;
    private String pathology;
    private int photoID;
    private String compliance;

    public Patient(){}

    public Patient(String name, String birthday, String address, String pathology, int photoID, String compliance) {
        this.name = name;
        this.birthday = stringToGregorianCalendar(birthday);
        this.address = address;
        this.pathology = pathology;
        this.photoID = photoID;
        this.compliance = compliance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GregorianCalendar getBirthday() {
        return birthday;
    }

    public void setBirthday(GregorianCalendar birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

    public int getPhoto() {
        return photoID;
    }

    public void setPhoto(int photo) {
        this.photoID = photo;
    }

    public String getCompliance() {
        return compliance;
    }

    public void setCompliance(String compliance) {
        this.compliance = compliance;
    }

    private GregorianCalendar stringToGregorianCalendar(String birthday) {
        GregorianCalendar calendar = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        Date date = null;

        try {
            date = dateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);

        return calendar;
    }
}
