package mil.army.milirang.schedule;

import android.graphics.Color;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.*;

import java.util.Date;


public class CalDeco implements CalendarCellDecorator {

    @Override
    public void decorate(CalendarCellView cellView, Date date) {
        if(date.equals(new Date())) {
            int purpleColor = Color.parseColor("#7a00ff");
            cellView.setBackgroundColor(purpleColor);
        }
    }
}
