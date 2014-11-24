package ru.ryer.droid.PM5;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


class PmCustomCursorAdapter extends android.support.v4.widget.CursorAdapter {

    private LayoutInflater mInflater;

    public PmCustomCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        //get an inflater
        //And we make use of it in newView.
        //LayoutInflater creates a view based on xml structure in resource file passed to inflate method as first parameter.
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    //newView method is called to create a View object representing on item in the list, here You just create an object don't set any values.
    //inflate the view
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.custom, parent, false);
    }

    //View returned from newView is passed as first parameter to bindView, it is here where You will set values to display.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView content = (TextView) view.findViewById(R.id.lvtext1);
        content.setText(cursor.getString(cursor.getColumnIndex(PmSQLiteHelper.COL_ID_1)));

        content = (TextView) view.findViewById(R.id.lvtext2);
        content.setText(cursor.getString(cursor.getColumnIndex(PmSQLiteHelper.COL_ID_2)));

        content = (TextView) view.findViewById(R.id.lvtext3);
        content.setText(cursor.getString(cursor.getColumnIndex(PmSQLiteHelper.COL_ID_3)));

        content = (TextView) view.findViewById(R.id.counter);
        content.setText(cursor.getString(cursor.getColumnIndex(PmSQLiteHelper.COL_ID_4)));

    }
}