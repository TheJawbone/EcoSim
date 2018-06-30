package eco_sim;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * MyCalendar class acts as a modification for the Calendar util class. It hides the unnecessary fields and methods
 * while also adds some functionalities.
 */
public class MyCalendar {

    /**
     * Calendar util object.
     */
    private Calendar calendar;

    /**
     * Main constructor, instantiates the calendar.
     */
    public MyCalendar() {
        calendar = new GregorianCalendar();
    }

    /**
     * Sets the date.
     * @param year Year to be set.
     * @param month Month to be set.
     * @param day Day to be set.
     */
    public void set(int year, int month, int day) {
        calendar.set(year, month - 1, day, 0, 0);
    }

    /**
     * Sets the date based on the MyCalendar object passed as an argument.
     * @param calendar MyCalendar object based on which the state of this object will be set.
     */
    public void set(MyCalendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        this.set(year, month, day);
    }

    /**
     * Returns certain calendar attribute based on the choice.
     * @param choice
     * @return
     */
    public int get(int choice) {
        if(choice == Calendar.MONTH) {
            return calendar.get(choice) + 1;
        } else {
            return calendar.get(choice);
        }
    }

    /**
     * Returns maximum value for the specified attribute.
     * @param choice
     * @return
     */
    public int getActualMaximum(int choice) {
        return calendar.getActualMaximum(choice);
    }

    /**
     * Sets the calendar for the next hour.
     */
    public void nextHour() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour == 23) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            nextDay();
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, ++hour);
        }
    }

    /**
     * Sets the calendar for the next day.
     */
    public void nextDay() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(day == calendar.getMaximum(Calendar.DAY_OF_MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            nextMonth();
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, ++day);
        }
    }

    /**
     * Sets the calendar for the next month.
     */
    public void nextMonth() {
        int month = calendar.get(Calendar.MONTH);
        if(month == 11) {
            calendar.set(Calendar.MONTH, 0);
            nextYear();
        } else {
            calendar.set(Calendar.MONTH, ++month);
        }
    }

    /**
     * Sets the calendar for the next year.
     */
    public void nextYear() {
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
    }

    /**
     * Sets the calendar for the previous day.
     */
    public void previousDay() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(day == 1) {
            int month = calendar.get(Calendar.MONTH);
            if(month == 0) {
                calendar.set(calendar.get(Calendar.YEAR) - 1, 11, 31);
            } else {
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
        } else {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day - 1);
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String toString() {
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean equals(Object object) {
        MyCalendar date = (MyCalendar)object;
        if(date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && date.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }
}
