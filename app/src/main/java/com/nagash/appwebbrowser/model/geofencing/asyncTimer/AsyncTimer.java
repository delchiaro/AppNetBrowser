package com.nagash.appwebbrowser.model.geofencing.asyncTimer;

import android.os.AsyncTask;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by nagash on 23/09/16.
 */
public class AsyncTimer extends AsyncTask<Void, Void, Void> {

    private boolean running = false;
    private boolean shallStop = false;

    private int frequency;
    private int hits = -1;

    List<AsyncTimerListener> listeners = new LinkedList<>();


    /**
     * Create an AsyncTimer with a given frequency and number of loops.
     * Each time a loop ends, all the listeners will be called in the main thread, while the next loop starts in the async thread.
     * Use stop() to stop the timer at the end of the next loop.
     * @param frequency milliseconds between two loops.
     * @param hits number of loops. If <= 0 will run forever (until stop() is called)
     */
    AsyncTimer(int frequency, int hits) {
        this.frequency = frequency;
    }

    public AsyncTimer(int frequency) {
        this(frequency, -1);
    }



    public AsyncTimer addListener(AsyncTimerListener listener) {
        listeners.add(listener);
        return this;
    }

    public AsyncTimer stop() {
        shallStop = true;
        return this;
    }

    public AsyncTimer setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        running = true;
    }


    @Override
    protected Void doInBackground(Void... voids)
    {

        int freq = this.frequency;
        int nHits = this.hits;
        if(hits > 0)
        {
            for(int counter = 0; counter < nHits && !shallStop; counter ++)
            {
                try { Thread.sleep(freq); }
                catch (InterruptedException e) {  continue; }
                publishProgress(null);
            }
        }

        else
        {
            do {
                try { Thread.sleep(freq); }
                catch (InterruptedException e) {  continue; }
                publishProgress(null);
            } while (!shallStop);
            return null;
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        for(AsyncTimerListener lst : listeners)
            lst.onTimerLoop();
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        shallStop = false;

    }
}
