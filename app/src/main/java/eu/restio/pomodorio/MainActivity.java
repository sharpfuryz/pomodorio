package eu.restio.pomodorio;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Time;

public class MainActivity extends Activity {

    private TextView motivation_message;
    private TextView stopwatch;
    private ImageButton start_stop_btn;

    private Boolean is_running = false;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    long startTime = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                motivation_message = (TextView) findViewById(R.id.motivation_message);
                stopwatch = (TextView) findViewById(R.id.stopwatch);
                start_stop_btn = (ImageButton) findViewById(R.id.play_stop_btn);
                Time t = Time.valueOf("25:00:00");
                startTime = t.getTime();
            }
        });
    }

    public void start_stop_action(View view) {
        // Should toggle image on button, start or stop timer
        if( !is_running ){
            // Should start
            is_running = true;
            startTime = 0;
            customHandler.postDelayed(updateTimerThread, 0);

            // tweak button
        }else{
            // Should stop
        }

    }

    // Timer code
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
//            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            timeInMilliseconds = 1500000 - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            stopwatch.setText(mins + ":" + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };

}
