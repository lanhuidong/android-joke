package com.nexusy.gemini.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nexusy.gemini.R;
import com.nexusy.gemini.model.Joke;

import java.util.List;

/**
 * @author lanhuidong
 * @since 2015-08-16
 */
public class JokeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Joke> jokes;

    public JokeAdapter(Context context, List<Joke> jokes) {
        this.mInflater = LayoutInflater.from(context);
        this.jokes = jokes;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return jokes.size();
    }

    @Override
    public Object getItem(int position) {
        return jokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return jokes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.joke, parent, false);
            holder.content = (TextView) convertView.findViewById(R.id.joke_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.content.setText(jokes.get(position).getContent());
        return convertView;
    }

    private class ViewHolder {
        public TextView content;
    }
}
