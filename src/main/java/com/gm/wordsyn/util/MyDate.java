package com.gm.wordsyn.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {
    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}
