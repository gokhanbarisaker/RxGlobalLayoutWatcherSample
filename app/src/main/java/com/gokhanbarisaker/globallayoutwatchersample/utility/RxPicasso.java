package com.gokhanbarisaker.globallayoutwatchersample.utility;

import android.view.View;
import android.widget.ImageView;

import com.gokhanbarisaker.globallayoutwatchersample.Application;
import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by gokhanbarisaker on 4/25/15.
 */
public class RxPicasso
{
    // == Variables ================================================================================

    private static RxPicasso sharedInstance;


    // == Constructors & Instance providers ========================================================

    public static RxPicasso getInstance()
    {
        synchronized (RxPicasso.class)
        {
            if (sharedInstance == null)
            {
                sharedInstance = new RxPicasso();
            }
        }

        return sharedInstance;
    }


    // == Tools ====================================================================================

    public Observable<Boolean> loadImage(final RequestCreator creator, final ImageView view)
    {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                creator.into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(Boolean.TRUE);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onError() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(Boolean.FALSE);
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                Application.picasso.cancelRequest(view);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
