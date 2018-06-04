package eco_sim;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyCalendar {

    Calendar calendar;

    public MyCalendar() {
        calendar = new GregorianCalendar();
    }

    public void set(int year, int month, int day, int hour, int minute) {
        calendar.set(year, month - 1, day, hour, minute);
    }

    public int get(int choice) {
        if(choice == Calendar.MONTH) {
            return calendar.get(choice) + 1;
        } else {
            return calendar.get(choice);
        }
    }

    public int getActualMaximum(int choice) {
        return calendar.getActualMaximum(choice);
    }

    public void nextHour() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour == 23) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            nextDay();
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, ++hour);
        }
    }

    public void nextDay() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(day == calendar.getMaximum(Calendar.DAY_OF_MONTH)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            nextMonth();
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, ++day);
        }
    }

    public void nextMonth() {
        int month = calendar.get(Calendar.MONTH);
        if(month == 11) {
            calendar.set(Calendar.MONTH, 0);
            nextYear();
        } else {
            calendar.set(Calendar.MONTH, ++month);
        }
    }

    public void nextYear() {
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
    }

    @Override
    public String toString() {
        return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE);
    }
}
