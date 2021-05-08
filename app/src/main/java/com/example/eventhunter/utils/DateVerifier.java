package com.example.eventhunter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateVerifier {

    public static boolean dateInTheFuture(String eventDate) {
        return !dateInThePast(eventDate);
    }

    public static boolean dateInThePast(String eventDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        String[] partsEventDate = eventDate.split("/");
        String[] partsCurrentDate = currentDate.split("/");

        int eventYear = Integer.parseInt(partsEventDate[2]);
        int currentYear = Integer.parseInt(partsCurrentDate[2]);
        int eventMonth = Integer.parseInt(partsEventDate[1]);
        int currentMonth = Integer.parseInt(partsCurrentDate[1]);
        int eventDay = Integer.parseInt(partsEventDate[0]);
        int currentDay = Integer.parseInt(partsCurrentDate[0]);

        if (eventYear < currentYear)
            return true;
        if ((eventYear == currentYear) && (eventMonth < currentMonth))
            return true;
        return (eventYear == currentYear) && (eventMonth == currentMonth) && (eventDay < currentDay);
    }

    public static Date getDateFromString(String stringDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.parse(stringDate);
    }

    public static String getDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public static boolean compareStringDates(String stringDate1, String stringDate2) {
        try {
            Date date1 = getDateFromString(stringDate1);
            Date date2 = getDateFromString(stringDate2);

            return date1.equals(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean compareStringDateAndDate(String stringDate1, Date date2) {
        try {
            Date date1 = getDateFromString(stringDate1);
            return date1.equals(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
