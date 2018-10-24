package mil.army.milirang.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class ReceiverAutoComplete extends AutoCompleteTextView {

    private String previous = "";
    private String seperator = ",";
    boolean isState = false;

    public ReceiverAutoComplete(final Context context, final AttributeSet attrs,
                              final int defStyle) {
        super(context, attrs, defStyle);
        this.setThreshold(1);

    }

    /**
     * This method filters out the existing text till the separator and launched
     * the filtering process again
     */
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        String filterText = text.toString().trim();
        String lastchar = filterText.substring(filterText.length() - 1,
                filterText.length());
        if (filterText.length() == 1) {
            if (lastchar.equals(seperator)) {
                isState = true;
            } else {
                isState = false;
            }
        }
        previous = filterText.substring(0,
                filterText.lastIndexOf(getSeperator()) + 1);

        filterText = filterText.substring(filterText
                .lastIndexOf(getSeperator()) + 1);

        if ((lastchar.equals(seperator)) || isState) {
            super.performFiltering(filterText, keyCode);

            isState = true;

        }
    }
    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(final String seperator) {
        this.seperator = seperator;
    }
}
