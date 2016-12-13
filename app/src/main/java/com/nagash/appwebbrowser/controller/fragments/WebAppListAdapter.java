package com.nagash.appwebbrowser.controller.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.controller.MainActivity;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.util.List;

/**
 * Created by nagash on 06/10/16.
 */


public class WebAppListAdapter extends ArrayAdapter<WebApp>
{

    boolean showingAppDetails = false;

    MainActivity mainActivity;
    public WebAppListAdapter(@NonNull MainActivity mainActivity, List<WebApp> users) {
        super(mainActivity, 0, users);
        this.mainActivity = mainActivity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position]
        final WebApp app = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.app_list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.app_name);
        tvName.setText(app.getName());


        FloatingActionButton btnApp = (FloatingActionButton) convertView.findViewById(R.id.app_button);
        btnApp.setSize(FloatingActionButton.SIZE_MINI);
        btnApp.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        // Populate the data into the template view using the data object
        btnApp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { mainActivity.startAppFragment(app); }
        });
        btnApp.setFocusable(false);

        // Return the completed view to render on screen
        return convertView;
    }
}