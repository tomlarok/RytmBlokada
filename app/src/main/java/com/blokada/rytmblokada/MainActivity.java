package com.blokada.rytmblokada;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    // z Chronometer
    /**
     * Key for getting saved start time of Chronometer class
     * this is used for onResume/onPause/etc.
     */
    public static final String START_TIME = "START_TIME";
    /**
     * Same story, but to tell whether the Chronometer was running or not
     */
    public static final String CHRONO_WAS_RUNNING = "CHRONO_WAS_RUNNING";
    /**
     * Same story, but if chronometer was stopped, we dont want to lose the stop time shows in
     * the tv_timer
     */
    public static final String TV_TIMER_TEXT = "TV_TIMER_TEXT";
    /**
     * Same story, we don't want to lose recorded laps
     */
    public static final String ET_LAPST_TEXT = "ET_LAPST_TEXT";
    /**
     * Same story...keeps the value of the lap counter
     */
    public static final String LAP_COUNTER  = "LAP_COUNTER";
    // koniec z Chronometer
    int rhythmUser[] = new int [10];
    // kod - rytm
    int rhythmCode[] = {250, 500, 500, 250};


    private Button lock, disable, enable, rytm;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;

        // z Chronometer
    //Instance of Chronometer
    Rhythm mRhythm;

    //keep track of how many times btn_lap was clicked
    int mLapCounter = 1;

    //Thread for chronometer
    Thread mThreadChrono;

    //Reference to the MainActivity (this class!)
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating all member variables

        mContext = this; // z chronometer

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        lock = (Button) findViewById(R.id.lock);
        enable = (Button) findViewById(R.id.enableBtn);
        disable = (Button) findViewById(R.id.disableBtn);
        rytm = (Button) findViewById(R.id.rytmBtn); // button rytmu
        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        rytm.setOnClickListener(this);  // button rytmu

        // z Chronometru
        //mEtLaps.setEnabled(false); //prevent the et_laps to be editable
        //mSvLaps = (ScrollView) findViewById(R.id.sv_lap);


        //btn_start click handler
        rytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer has not been instantiated before...

                //boolean startRytmu = True;
                int startRytmu = 0;
                int liczbaPukniec = 0;
                if(mRhythm == null) {
                    //instantiate the chronometer
                    mRhythm = new Rhythm(mContext);
                    //run the chronometer on a separate thread
                    mThreadChrono = new Thread(mRhythm);
                    mThreadChrono.start();

                    //start the chronometer!
                    mRhythm.start();
                /*
                    //clear the perilously populated et_laps
                    mEtLaps.setText(""); //empty string!
                */
                    //reset the lap counter
                    mLapCounter = 1;

                    //boolean startRytmu = True;
                    startRytmu = 1; // poziom 1 włączenie spr kodu rytmu
                }
                //boolean startRytmu = True;
                //if(startRytmu == True){
                if(startRytmu == 1){
                    // lap = rytm
                    int lpTap = 1; //liczba puknieć przez użytkownika
                    if(lpTap < liczbaPukniec ){


                        //if chrono is not running we shouldn't capture the lap!
                        if(mRhythm == null) {
                  /*
                    Toast.makeText(mContext
                            , R.string.warning_lap_button, Toast.LENGTH_SHORT).show();
                    */
                            return; //do nothing!
                        }
                     /*
                        //we just simply copy the current text of tv_timer and append it to et_laps
                        mEtLaps.append("LAP " + String.valueOf(mLapCounter++)
                                + "   " + mTvTimer.getText() + "\n");
                     */
                        // kopiowanie do tabeli
                        int czas = Rhythm.milisekundy;
                        rhythmUser[liczbaPukniec] = czas;
                        //rhythmUser[liczbaPukniec] = Rhythm.milisekundy;


                        /*
                        //scroll to the bottom of et_laps
                        mSvLaps.post(new Runnable() {
                            @Override
                            public void run() {
                                mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                            }
                        });
                        */
                    }
                    if(lpTap == liczbaPukniec ){

                        if(mRhythm != null) {
                            //stop the chronometer
                            mRhythm.stop();
                            //stop the thread
                            mThreadChrono.interrupt();
                            mThreadChrono = null;
                            //kill the chrono class
                            mRhythm = null;

                            // Sprawdanie kodu rytmu
                            checkRhythm();

                        }

                    }

                }
            }
        });
/*
        //btn_stop click handler
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer had been instantiated before...
                if(mChronometer != null) {
                    //stop the chronometer
                    mChronometer.stop();
                    //stop the thread
                    mThreadChrono.interrupt();
                    mThreadChrono = null;
                    //kill the chrono class
                    mChronometer = null;
                }
            }
        });

        mBtnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if chrono is not running we shouldn't capture the lap!
                if(mChronometer == null) {
                  /*
                    Toast.makeText(mContext
                            , R.string.warning_lap_button, Toast.LENGTH_SHORT).show();
                    */
        /*
                    return; //do nothing!
                }

                //we just simply copy the current text of tv_timer and append it to et_laps
                mEtLaps.append("LAP " + String.valueOf(mLapCounter++)
                        + "   " + mTvTimer.getText() + "\n");

                //scroll to the bottom of et_laps
                mSvLaps.post(new Runnable() {
                    @Override
                    public void run() {
                        mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                    }
                });
            }
        });
        // koniec z Chronometru

                */
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
        rytm.setVisibility(isActive ? View.GONE : View.VISIBLE);    //Rytm button
    }

    @Override
    public void onClick(View view) {
        if (view == lock) {
            boolean active = devicePolicyManager.isAdminActive(compName);

            if (active) {
                devicePolicyManager.lockNow();
            } else {
                Toast.makeText(this, "Włącz funkcje administratora", Toast.LENGTH_SHORT).show();
            }
        } else if (view == enable) {

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Dlaczego potrzebne jest pozwolenie ...");
            startActivityForResult(intent, RESULT_ENABLE);

        } else if (view == disable) {
            devicePolicyManager.removeActiveAdmin(compName);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
            tapRhythm(); //uruchomienie metody rytmu
        }
        // Klikniecie przycisku Rytm
        /*
        } else if (view == rytm ){
            devicePolicyManager.removeActiveAdmin(compName);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
            tapRhythm(); //uruchomienie metody rytmu
        }
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Włączyłeś funkcje administratora", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Problem z włączeniem funkcji admnistratora", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkRhythm(){

            int spr = 0;
            for(int x=0; x<rhythmUser.length; x++ ){

                spr = rhythmCode[x] - rhythmUser[x];

        }
    }

    public void tapRhythm() {
        rytm.setVisibility(View.VISIBLE);
        // TODO if przycisk jest włączony uruchamia się moetoda ????
        /*
        rytm.setOnClickListener(new OnClickListener() {
            /*
                Rhythm rhythm = new Rhythm();
                rhythm.);

            Rhythm mRhythm;
        }
        */

    }
}
































