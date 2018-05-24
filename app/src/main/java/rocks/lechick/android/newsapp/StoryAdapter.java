package rocks.lechick.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by alek on 21/05/2018.
 */

public class StoryAdapter extends ArrayAdapter<Story> {

    private static final String DATE_SEPARATOR = "T";

    public StoryAdapter(Context context, List<Story> stories) {
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.story_list_item, parent, false);
        }

        Story currentStory = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.story_title);
        titleTextView.setText(currentStory.getmTitle());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.story_section);
        sectionTextView.setText(currentStory.getmSection());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.story_contributor);
        authorTextView.setText(currentStory.getmAuthor());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.story_date);
        String fullDateTime = currentStory.getmDate();
        String[] parts = fullDateTime.split(DATE_SEPARATOR);
        String dateUsed = parts[0];
        dateTextView.setText(dateUsed);

        TextView summaryTextView = (TextView) listItemView.findViewById(R.id.story_summary);
        summaryTextView.setText(currentStory.getmSummary());

        return listItemView;

    }

}

