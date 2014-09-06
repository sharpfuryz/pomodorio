package eu.restio.pomodorio;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
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
    private CountDownTimer countDownTimer;
    private Boolean is_running = false;

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
                Typeface face = Typeface.createFromAsset(getAssets(),"fonts/aldrich.ttf");
                Typeface ubuntumono = Typeface.createFromAsset(getAssets(),"fonts/ubuntumono.ttf");
                stopwatch.setTypeface(face);
                motivation_message.setTypeface(ubuntumono);
                motivation_message.setText(Helper.get_random_motivation_message());
                start_stop_btn = (ImageButton) findViewById(R.id.play_stop_btn);
                countDownTimer = new CountDownTimer(60000 * 25, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long minutes = millisUntilFinished / 60000;
                        long minutes_in_ms = minutes * 60000;
                        stopwatch.setText((minutes) + ":" + ((millisUntilFinished - minutes_in_ms) / 1000));
                    }

                    public void onFinish() {
                        set_notification_complete();
                    }
                };
            }
        });
    }

    public void start_stop_action(View view) {
        // Should toggle image on button, start or stop timer
        if( !is_running ){
            // Should start
            is_running = true;
            countDownTimer.start();
            start_stop_btn.setImageResource(R.drawable.btn_stop);
        }else{
            is_running = false;
            start_stop_btn.setImageResource(R.drawable.btn_play);
            // Should stop
        }

    }

    private void set_notification_complete() {
        // TODO: Implement notificaiton onComplete
    }


}
