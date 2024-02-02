package com.example.bodyboost;

import com.example.bodyboost.Database.CalorieDAO;
import com.example.bodyboost.Database.CalorieDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilities {

    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("d.M EEE", Locale.ENGLISH);
    private final SimpleDateFormat longDateFormat = new SimpleDateFormat("y-MM-d", Locale.ENGLISH);

    /**Get date going backwards or forwards of given date and return the requested date.
     * Example: startDate = "2022-04-18", numOfDays = -6. Returned date is: "2022-04-12"
     * @param startDate The starting date where we will be counting from in the YYYY-MM-DD format.
     * @param numberOfDays Number of days to go forwards or backwards.
     * @return Returns the wanted date in YYYY-MM-DD format as string.
     */
    public String getDate(String startDate, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        Date date = convertStringToDate(startDate, longDateFormat);

        assert date != null;
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
        Date newDate = calendar.getTime();
        return longDateFormat.format(newDate);
    }

    /**Converts long date to short version. Example: "2022-04-18" -> "18.4 Mon"
     * @param longDate The long date in the YYYY-MM-DD format.
     * @return Returns the given date in the d.M EEE format as string.
     */
    public String convertFromLongToShort(String longDate) {
        Calendar calendar = Calendar.getInstance();
        Date date = convertStringToDate(longDate, longDateFormat);

        assert date != null;
        calendar.setTime(date);

        Date newDate = calendar.getTime();
        return shortDateFormat.format(newDate);
    }

    /**Convert string to date with given SimpleDateFormat.
     * @param stringDate The given date as String.
     * @param sdf The SimpleDateFormat what we want to use.
     * @return Returns the given string date in as Date.
     */
    public Date convertStringToDate(String stringDate, SimpleDateFormat sdf) {
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // TODO: Create error msg
        }
        return null;
    }

    /**Get the current date with wanted SimpleDateFormat.
     * @param sdf The wanted SimpleDateFormat we want today's date.
     * @return Returns today's date as a string, formatted by the given SimpleDateFormat.
     */
    public String getCurrentDate(SimpleDateFormat sdf) {
        Date currentTime = Calendar.getInstance().getTime();

        return sdf.format(currentTime);
    }

    /**Get the highest integer from given list
     * @param integers The list we want to search the max.
     * @return Returns the max value from the list as int.
     */
    public int getMax(List<Integer> integers) {
        int max = 0;
        for (int comparable : integers) {
            if (max < comparable) {
                max = comparable;
            }
        }
        return max;
    }
    /**Check if today's date exists in the calorie database
     * @param calorieDAO The calorie day database.
     * @return Returns true if today's date exists. Returns false if it doesn't.
     */
    public boolean todayCalorieDayExists(CalorieDAO calorieDAO) {
        String today = getCurrentDate(
                new SimpleDateFormat("y-MM-d", Locale.ENGLISH)
        );

        List<CalorieDay> calorieDays = calorieDAO.getLastRecords(1);
        if (calorieDays.size() != 0) { // Check if size is zero & if data contains today's date
            for (int i = 0; i < calorieDays.size(); i++) {
                CalorieDay day = calorieDays.get(i);
                if (day.getCalorieDate().equals(today)) {
                    return true;
                }
            }
        }
        return false;
    }
}
