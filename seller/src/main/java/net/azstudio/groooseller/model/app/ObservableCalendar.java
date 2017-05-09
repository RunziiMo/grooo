package net.azstudio.groooseller.model.app;

import android.databinding.BaseObservable;



import java.util.Calendar;
import java.util.Date;

/**
 * Created by runzii on 16-5-31.
 */
public class ObservableCalendar extends BaseObservable {

    private Calendar calendar;

    public ObservableCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getDays() {
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public Date getDate(int dayOfMonth) {
        Date date = calendar.getTime();
        date.setDate(dayOfMonth);
        return date;
    }

    public void rollMonth(boolean increment) {
        calendar.add(Calendar.MONTH, increment ? 1 : -1);
    }

}
