package eu.restio.pomodorio;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;

public class MainActivity extends Activity {

    private TextView motivation_message;
    private TextView stopwatch;
    private TextView antitimer;
    private ImageButton start_stop_btn;
    private CountDownTimer countDownTimer;
    private Boolean is_running = false;
    private long timer_val = (60000 * 25);
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        set_notification_complete();
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                motivation_message = (TextView) findViewById(R.id.motivation_message);
                stopwatch = (TextView) findViewById(R.id.stopwatch);
                antitimer = (TextView) findViewById(R.id.antitimer);
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/aldrich.ttf");
                Typeface ubuntumono = Typeface.createFromAsset(getAssets(), "fonts/ubuntumono.ttf");
                stopwatch.setTypeface(face);
                antitimer.setTypeface(face);
                motivation_message.setTypeface(ubuntumono);
                motivation_message.setText(Helper.get_random_motivation_message());
                start_stop_btn = (ImageButton) findViewById(R.id.play_stop_btn);

                // TODO: Rewrite me!
                countDownTimer = new CountDownTimer(timer_val, 1000) {
                    public void onTick(long millisUntilFinished) {
                        clock_tick(millisUntilFinished);
                    }

                    public void onFinish() {
                        set_notification_complete();
                    }
                };
            }
        });
    }

    private void clock_tick(long millisUntilFinished) {
        // UI Implementation of Timer.tick()
        long remain_time = timer_val - millisUntilFinished;
        long remain_minutes = remain_time / 60000;
        long minutes = millisUntilFinished / 60000;
        long minutes_in_ms = minutes * 60000;
        long calc1 = ((millisUntilFinished - minutes_in_ms) / 1000);
        stopwatch.setText((minutes) + ":" + calc1);
        antitimer.setText((remain_minutes)+":"+((remain_time - (remain_minutes * 60000))/ 1000));
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
        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.background_gradient))
                        .setSmallIcon(R.drawable.common_signin_btn_icon_dark)
                        .setContentTitle("Relax 5 minutes")
                        .setPriority(1)
                        .setVibrate(new long[300])
                        .setContentText(Helper.get_random_relax_message());

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


}
