package cz.dahor.todolistapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {

    public static String getToday(){
        Date dat = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd.MM.yyyy");
        return DateFor.format(dat);
    }
}
