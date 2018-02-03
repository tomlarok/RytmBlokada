package com.blokada.rytmblokada;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.ToDoubleBiFunction;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    // z Chronometer
    /**
     * Key for getting saved start time of tap rhythm class
     * this is used for onResume/onPause/etc.
     */
    public static final String START_TIME = "START_TIME";
    /**
     * Same story, but to tell whether the tap rhythm was running or not
     */
    public static final String CHRONO_WAS_RUNNING = "CHRONO_WAS_RUNNING";
    /**
     * Same story, but if tap rhythm was stopped, we dont want to lose the stop time shows in
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
    int rhythmCode[] = {500, 590, 590, 590};
    //int rhythmCode[] = {250, 500, 500, 250};
    int tolerRhythm = 200;  //300

    // zmienne do spr i zapisu kodorytmu
    int startRytmu = 0;
    int liczbaPukniec = 3;  //liczba udzerzeń w rytmie TODO ustalone na sztywno 4
    int lpTap = 0; //liczba puknieć przez użytkownika TODO =1

    int timeMillis;     //milisekundy

    // debug
    private static final String TAG = "MyActivity";


    private Button lock, disable, enable, rytm;
    private TextView txtblokada;   // tekst Blokada
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;

        // z Chronometer
    //Instance of Chronometer
    Rhythm mRhythm;

    //keep track of how many times btn_lap was clicked
    int mLapCounter = 1;

    //Thread for tap rhythm class
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
        txtblokada = (TextView) findViewById(R.id.textBlokada);
        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
        rytm.setOnClickListener(this);  // button rytmu

        // z Chronometru
        //mEtLaps.setEnabled(false); //prevent the et_laps to be editable
        //mSvLaps = (ScrollView) findViewById(R.id.sv_lap);

        // TODO Test
        Log.i(TAG, "Działa coś!!!!!!!!" );

        //btn_start click handler
        rytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the chronometer has not been instantiated before...
                // TODO Test
                Log.i(TAG, "MainActivity - uruchomiona metoda onClick" );
                //boolean startRytmu = True;
                /*
                int startRytmu = 0;
                int liczbaPukniec = 4;  //liczba udzerzeń w rytmie TODO ustalone na sztywno
                int lpTap = 1; //liczba puknieć przez użytkownika
                */
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
                    // TODO Test
                    Log.i(TAG, "OnClcik 1 pukniecie" );
                    // odpalenie wątku przechwytywania czasu milisekund
                 //   updateTimerText();
                }
                //boolean startRytmu = True;
                //if(startRytmu == True){
                if(startRytmu == 1){
                    // lap = rytm
                    //int lpTap = 1; //liczba puknieć przez użytkownika
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
                        // kopiowanie do tabeli TODO Nie działa!!!!!!
                        //int czas = Rhythm.milisekundy;
                        int czas = timeMillis;
                        //rhythmUser[liczbaPukniec] = czas;
                        rhythmUser[lpTap] = czas;
                        //rhythmUser[liczbaPukniec] = Rhythm.milisekundy;

                        Log.i(TAG, "->" + lpTap + ":" + rhythmUser[lpTap] + "\n");


                        /*
                        //scroll to the bottom of et_laps
                        mSvLaps.post(new Runnable() {
                            @Override
                            public void run() {
                                mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                            }
                        });
                        */
                        lpTap ++; //zwieksza sie liczba pukniec
                        // TODO Test
                        Log.i(TAG, "onClick kolejne pukniecie" );
                    }
                    if(lpTap == liczbaPukniec ){

                        // kopiowanie do tabeli TODO Nie działa!!!!!!
                        //int czas = Rhythm.milisekundy;
                        int czas = timeMillis;
                        //rhythmUser[liczbaPukniec] = czas;
                        rhythmUser[lpTap] = czas;

                        if(mRhythm != null) {
                            //stop the chronometer
                            mRhythm.stop();
                            //stop the thread
                            mThreadChrono.interrupt();
                            mThreadChrono = null;
                            //kill the chrono class
                            mRhythm = null;
                            // TODO Test
                            Log.i(TAG, "Ostatnie pukniceic !!! " );
                            // Sprawdanie kodu rytmu
                            checkRhythm();

                        }

                    }

                }
            }
        });
        // koniec z Chronometru

    }

    public void updateTimerText(final int millis) {
        Log.i(TAG, "updateTimer - odpala się! Timer!" );
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //   mTvTimer.setText(timeAsText);
                    //timeMillis = Rhythm.milisekundy;;
                    timeMillis = millis;
                    //mTvTime.setText(time);
                }
            });
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
                txtblokada.setText("Zablokowane");  //ustalenie tekstu stanu blokady
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
            Log.i(TAG, "MyActivity - Odpala się checkRhythm " );
            System.out.println("MyActivity - Odpala się checkBox " );
            int spr = 0; //przechowuuje wartość różnicy w milisekundach
            int correctSpr[] = new int [10]; //przechowuje info czy rytm dobrze wsytukano, w dobrmy intwerwale, 1 - zgadza się kodorytm
            //boolean correctSpr[] = new int [10]; //przechowuje info czy rytm dobrze wsytukano, w dobrmy intwerwale, 1 - zgadza się kodorytm
            boolean okRhythm = false;
            for(int x=0; x<rhythmUser.length; x++ ){    //TODO rythmuser długosśc - 10
                if(x < liczbaPukniec -1 ){
                    Log.i(TAG, "MyActivity - checkRhythm - Kodorytm: " + rhythmCode[x] + " Użytkownik: " + rhythmUser[x] );
                } else {
                    Log.i(TAG, "MyActivity - checkRhythm - Odwolanie do pustego indexu w tabeli = java.lang.ArrayIndexOutOfBoundsException: length=4; index=4 " );
                    break;
                }
             //   Log.i(TAG, "MyActivity - checkRhythm - Kodorytm: " + rhythmCode[x] + "Użytkownik: " + rhythmUser[x] );
                spr = Math.abs(rhythmCode[x] - rhythmUser[x]);
                Log.i(TAG, "MyActivity - spr: " + spr);
                if(spr < tolerRhythm){
                    correctSpr[x] = 1;
                    okRhythm = true;
                    Log.i(TAG, "MyActivity Puknięcie Ok — okRhythm: " + okRhythm);
                } else{
                    correctSpr[x] = 0;
                    okRhythm = false;
                    Log.i(TAG, "MyActivity  ZŁE Puknięcie !!! — okRhythm: " + okRhythm);
                    break; // przerwanie funkcji gdy jest zły kodorytm
                }
            }

            if(okRhythm == true){
                Toast.makeText(MainActivity.this, "Kodorytm poprawny!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "MyActivity Kodorytm Ok, Oblokowano! — okRhythm: " + okRhythm);
                txtblokada.setText("Odblokowane");  //ustalenie tekstu stanu blokady
            } else {
                Toast.makeText(MainActivity.this, "Zły kodorytm!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "MyActivity ZŁY Kodorytm! — okRhythm: " + okRhythm);
            }
            lpTap = 0;
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
/*
    public void updateTimerText(final String timeAsText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
             //   mTvTimer.setText(timeAsText);
                timeMillis = Rhythm.milisekundy;;
                //mTvTime.setText(time);
            }
        });
    }
    */
}

































