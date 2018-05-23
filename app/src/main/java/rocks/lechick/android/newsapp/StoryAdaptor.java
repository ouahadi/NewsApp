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

public class StoryAdaptor extends ArrayAdapter<Story> {

    private static final String DATE_SEPARATOR = "T";

    public StoryAdaptor(Context context, List<Story> stories) {
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.story_list_view, parent, false);
        }

        Story currentStory = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.story_title);
        title.setText(currentStory.getmTitle());

        TextView section = (TextView) listItemView.findViewById(R.id.story_section);
        section.setText(currentStory.getmSection());

        TextView author = (TextView) listItemView.findViewById(R.id.story_contributor);
        author.setText(currentStory.getmAuthor());

        TextView date = (TextView) listItemView.findViewById(R.id.story_date);
        String fullDateTime = currentStory.getmDate();
        String[] parts = fullDateTime.split(DATE_SEPARATOR);
        String dateUsed = parts[0];
        date.setText(dateUsed);

        TextView summary = (TextView) listItemView.findViewById(R.id.story_summary);
        summary.setText(currentStory.getmSummary());

        return listItemView;

    }

}

