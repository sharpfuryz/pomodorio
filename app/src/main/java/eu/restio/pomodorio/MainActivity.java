package eu.restio.pomodorio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends Activity {

    // UI
    private TextView motivation_message;
    private TextView stopwatch;
    private TextView antitimer;
    private ImageButton start_stop_btn;
    private static String default_time_active = "25:00";
    // Timer
    private CountDownTimer countDownTimer;
    private Boolean is_running = false;
    private long timer_val = 0;
    private static long timer_val_default = (60000*25);
    private static long timer_val_relax = (60000*5);
    private long timer_started_with = 0;
    private long millis_from_timer = 0;
    // Stuff
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // UI
                motivation_message = (TextView) findViewById(R.id.motivation_message);
                stopwatch = (TextView) findViewById(R.id.stopwatch);
                antitimer = (TextView) findViewById(R.id.antitimer);
                // Fonts
                Typeface face = Typeface.createFromAsset(getAssets(), "fonts/aldrich.ttf");
                Typeface ubuntumono = Typeface.createFromAsset(getAssets(), "fonts/ubuntumono.ttf");
                stopwatch.setTypeface(face);
                stopwatch.setText(default_time_active);
                antitimer.setTypeface(face);
                motivation_message.setTypeface(ubuntumono);
                motivation_message.setText(Helper.get_random_motivation_message());
                // Buttons
                start_stop_btn = (ImageButton) findViewById(R.id.play_stop_btn);
                // Actual clock
                Thread myThread = null;
                Runnable myRunnableThread = new CountDownRunner();
                myThread= new Thread(myRunnableThread);
                myThread.start();
                //
                initialize_timer(timer_val_default);
            }
        });
    }

    private void initialize_timer(long defined_val) {
    // Actualy it recreates timer, but not starts it
        timer_started_with = defined_val;
        countDownTimer = new CountDownTimer(defined_val, 1000) {
            public void onTick(long millisUntilFinished) {
                clock_tick(millisUntilFinished);
            }
            public void onFinish() {
                set_notification_complete();
            }
        };
    }

    private void clock_tick(long millisUntilFinished) {
        // UI Implementation of Timer.tick()
        millis_from_timer = millisUntilFinished;
        long remain_time = timer_started_with - millisUntilFinished;
        long remain_minutes = remain_time / 60000;
        long minutes = millisUntilFinished / 60000;
        long minutes_in_ms = minutes * 60000;
        long calc1 = ((millisUntilFinished - minutes_in_ms) / 1000);
//        long remain_seconds = ((remain_time - (remain_minutes * 60000))/ 1000 + 1);
        stopwatch.setText((minutes) + ":" + calc1);
//        antitimer.setText((remain_minutes)+":"+remain_seconds);
    }

    public void start_stop_action(View view) {
        motivation_message.setText(Helper.get_random_motivation_message());

        if( !is_running ){
            // Should start
            is_running = true;
            initialize_timer(timer_val_default);
            countDownTimer.start();
            start_stop_btn.setImageResource(R.drawable.btn_stop);
        }else{
            is_running = false;
            start_stop_btn.setImageResource(R.drawable.btn_play);
            stopwatch.setText(default_time_active);
            countDownTimer.cancel();
            // Should stop
        }

    }

    private void set_notification_complete() {
        vibrate();
//        int notificationId = 001;
//        // Build intent for notification content
//        Intent viewIntent = new Intent(this, MainActivity.class);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentTitle("Relax 5 minutes")
//                        .setPriority(1)
//                        .setVibrate(new long[300])
//                        .setContentText(Helper.get_random_relax_message());
//
//        // Get an instance of the NotificationManager service
//        NotificationManagerCompat notificationManager =
//                NotificationManagerCompat.from(this);
//
//        // Build the notification and issues it with notification manager.
//        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void vibrate(){
        long pattern[]={0,200,100,300,400};
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);
    }

    //
    // Anti-pattern
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    String curTime = hours + ":" + minutes;
                    antitimer.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
}
