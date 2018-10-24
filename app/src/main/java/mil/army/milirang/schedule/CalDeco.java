package mil.army.milirang.schedule;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.*;

import java.util.Date;
import java.util.List;

public class CalDeco implements CalendarCellDecorator {

    CalendarPickerView calview;
    List<Date> nodays;
    public CalDeco(CalendarPickerView calview, List<Date> nodays) {
        this.calview = calview;
        this.nodays = nodays;
    }

    @Override
    public void decorate(CalendarCellView cellView, Date date) {

        List<Date> selects = calview.getSelectedDates();

        if(date.before(new Date())){
            nodays.add(date);
            cellView.setSelectable(false);
        }

        if(!cellView.isSelectable())
        {
            cellView.getDayOfMonthTextView().setText("no!");
        }

        if(nodays.indexOf(date) != -1)
        {
            cellView.setBackgroundColor(Color.BLACK);
        }
        else {
            if (selects.indexOf(date) != -1 && nodays.indexOf(date) == -1) {
                int purpleColor = Color.parseColor("#7a00ff");
                cellView.setBackgroundColor(purpleColor);
                cellView.getDayOfMonthTextView().setTextColor(Color.GREEN);
            } else {
                cellView.setBackgroundColor(Color.RED);
            }
        }
    }
}
