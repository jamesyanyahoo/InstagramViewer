package com.yahoo.shopping.instagramviewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class GifView extends ImageView {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long movieStart;

    public GifView(Context context) throws IOException {
        super(context);
        init(context);
    }


    public GifView(Context context, AttributeSet attrs) throws IOException {
        super(context, attrs);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) throws IOException {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) throws IOException {
        setFocusable(true);

        gifInputStream = context.getAssets().open("loading.gif");

        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();

        Log.i("xxxxxxx", String.valueOf(movieWidth));
        Log.i("xxxxxxx", String.valueOf(movieHeight));
        Log.i("xxxxxxx", String.valueOf(movieDuration));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }

    public int getMovieWidth() {
        return movieWidth;
    }

    public int getMovieHeight() {
        return movieHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();

        if (movieStart == 0) {
            movieStart = now;
        }

        if (gifMovie != null) {
            int dur = gifMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - movieStart) % dur);

            gifMovie.setTime(relTime);
            gifMovie.draw(canvas, (this.getWidth() - movieWidth) / 2, (this.getHeight() - movieHeight) / 2);
            invalidate();
        }
    }
}