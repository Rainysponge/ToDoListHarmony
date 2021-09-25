package com.example.todolistapplication.Utils;

import ohos.agp.components.DatePicker;
import ohos.agp.components.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static int Year = 0;
    public static int Month = 1;
    public static int Day = 2;
    public static int Hour = 3;
    public static int Minute = 4;
    public static int Second = 5;

    public static boolean compareTime(Date startTime, String endTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date endTimeDate = dateFormat.parse(endTime);
        return endTimeDate.compareTo(startTime) > 0;
    }

    public static boolean compareTime(String startTime, String endTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date startTimeDate = dateFormat.parse(startTime);
        Date endTimeDate = dateFormat.parse(endTime);
        return endTimeDate.compareTo(startTimeDate) > 0;
    }


    public static int[] parse(String time){
        String[] list = time.split("-");
        int[] res = new int[6];

        for(int i=0; i<6; i++){
            res[i] = Integer.parseInt(list[i]);
        }
        return res;
    }

    public static String getTimeFromPicker(DatePicker datePicker, TimePicker timePicker){
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        int second = timePicker.getSecond();

        return year + "-" + month + "-" + day + "-" + hour + "-" + minute +
                "-" + second;
    }

    public static long getRemainTime(String endTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        Date endTimeDate = dateFormat.parse(endTime);

        Date now = new Date();

        return (endTimeDate.getTime() - now.getTime());

    }

    public static long getTimeFromString(SimpleDateFormat dateFormat, String time) throws ParseException {
        Date date = dateFormat.parse(time);
        return date.getTime();
    }
}
