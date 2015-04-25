package com.gokhanbarisaker.globallayoutwatchersample.adapter;

import android.graphics.Rect;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gokhanbarisaker.globallayoutwatchersample.Application;
import com.gokhanbarisaker.globallayoutwatchersample.R;
import com.gokhanbarisaker.globallayoutwatchersample.utility.RxGlobalLayoutWatcher;
import com.gokhanbarisaker.globallayoutwatchersample.utility.RxPicasso;
import com.gokhanbarisaker.globallayoutwatchersample.utility.TemplateFormatter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by gokhanbarisaker on 4/25/15.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>
{
    // == Variables ================================================================================

    private static final String PATTERN = "http://placehold.it/{{w}}x{{h}}";

    private SparseArrayCompat<Subscription> imageLoadSubscriptions = new SparseArrayCompat<>();

    // == Adapter lifecycle callbacks ==============================================================

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_photo, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.ViewHolder viewHolder, int i) {
        Observable observable = RxGlobalLayoutWatcher.instance().addGlobalLayoutWatch(viewHolder.imageView, new RxGlobalLayoutWatcher.Mission(RxGlobalLayoutWatcher.OBJECTIVE_NONZEROAREA)).flatMap(new Func1<View, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(View view) {
                Rect frame = new Rect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                String path = TemplateFormatter.getInstance().format(PATTERN, frame);
                RequestCreator rc = Application.picasso.load(path);
                return RxPicasso.getInstance().loadImage(rc, viewHolder.imageView);
            }
        });

        imageLoadSubscriptions.append(viewHolder.imageView.hashCode(), observable.subscribe());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        Subscription subscription = imageLoadSubscriptions.get(holder.imageView.hashCode());

        if (subscription != null)
        {
            subscription.unsubscribe();
            imageLoadSubscriptions.remove(holder.imageView.hashCode());
        }

        Application.picasso.cancelRequest(holder.imageView);
    }


    // == Accessors ================================================================================

    @Override
    public int getItemCount() {
        return 99;
    }


    // == Minions ==================================================================================

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.photo_imageview);
        }
    }
}
