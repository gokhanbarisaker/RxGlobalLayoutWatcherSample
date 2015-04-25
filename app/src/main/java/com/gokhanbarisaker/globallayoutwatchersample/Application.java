package com.gokhanbarisaker.globallayoutwatchersample;

import com.squareup.picasso.Picasso;

/**
 * Created by gokhanbarisaker on 4/25/15.
 */
public class Application extends android.app.Application
{
    public static Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();

        picasso = Picasso.with(this);
    }
}
