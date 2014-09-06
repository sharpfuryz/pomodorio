package eu.restio.pomodorio;

import android.text.format.Time;

public interface OnClockTickListner {
    public void OnSecondTick(Time currentTime);
    public void OnMinuteTick(Time currentTime);
}