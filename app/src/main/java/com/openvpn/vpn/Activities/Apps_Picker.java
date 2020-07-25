package com.openvpn.vpn.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.openvpn.vpn.R;

public class Apps_Picker extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    ImageView addcontact,addplaystore,addcalculator,addcamera,addclock,addmap;
    public final static String PREFERENCES_RES = "akash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps__picker);
        sharedpreferences = getSharedPreferences(PREFERENCES_RES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        addcontact=(ImageView) findViewById(R.id.addcontacts);
        addcamera=(ImageView) findViewById(R.id.addcamera);
        addplaystore=(ImageView) findViewById(R.id.addplaystore);
        addcalculator=(ImageView) findViewById(R.id.addcalculator);
        addclock=(ImageView) findViewById(R.id.addclock);
        addmap=(ImageView) findViewById(R.id.addmap);


        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(sharedpreferences.getString("button1","l").equals("4")
                        ||sharedpreferences.getString("button2","l").equals("4")
                        ||sharedpreferences.getString("button3","l").equals("4")
                        ||sharedpreferences.getString("button4","l").equals("4"))) {
                    if (sharedpreferences.getString("button", "1").equals("1")) {
                        editor.putString("button1", "4");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("2")) {
                        editor.putString("button2", "4");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("3")) {
                        editor.putString("button3", "4");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("4")) {
                        editor.putString("button4", "4");
                        editor.commit();
                    }

                    finish();
                }
                else
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("This App Is Already Added");

                    Toast toast = new Toast(Apps_Picker.this);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();


                }

            }
        });

        addplaystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(sharedpreferences.getString("button1","l").equals("1")
                        ||sharedpreferences.getString("button2","l").equals("1")
                        ||sharedpreferences.getString("button3","l").equals("1")
                        ||sharedpreferences.getString("button4","l").equals("1"))) {
                    if (sharedpreferences.getString("button", "1").equals("1")) {
                        editor.putString("button1", "1");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("2")) {
                        editor.putString("button2", "1");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("3")) {
                        editor.putString("button3", "1");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("4")) {
                        editor.putString("button4", "1");
                        editor.commit();
                    }

                    finish();
                }
                else
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("This App Is Already Added");

                    Toast toast = new Toast(Apps_Picker.this);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();


                }
            }
        });

        addcalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(sharedpreferences.getString("button1","l").equals("2")
                        ||sharedpreferences.getString("button2","l").equals("2")
                        ||sharedpreferences.getString("button3","l").equals("2")
                        ||sharedpreferences.getString("button4","l").equals("2"))) {
                    if (sharedpreferences.getString("button", "1").equals("1")) {
                        editor.putString("button1", "2");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("2")) {
                        editor.putString("button2", "2");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("3")) {
                        editor.putString("button3", "2");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("4")) {
                        editor.putString("button4", "2");
                        editor.commit();
                    }

                    finish();
                }
                else
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("This App Is Already Added");

                    Toast toast = new Toast(Apps_Picker.this);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();


                }

            }
        });

        addclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(sharedpreferences.getString("button1","l").equals("3")
                        ||sharedpreferences.getString("button2","l").equals("3")
                        ||sharedpreferences.getString("button3","l").equals("3")
                        ||sharedpreferences.getString("button4","l").equals("3"))) {
                    if (sharedpreferences.getString("button", "1").equals("1")) {
                        editor.putString("button1", "3");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("2")) {
                        editor.putString("button2", "3");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("3")) {
                        editor.putString("button3", "3");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("4")) {
                        editor.putString("button4", "3");
                        editor.commit();
                    }

                    finish();
                }
                else
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("This App Is Already Added");

                    Toast toast = new Toast(Apps_Picker.this);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();


                }

            }
        });

        addmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!(sharedpreferences.getString("button1","l").equals("5")
                        ||sharedpreferences.getString("button2","l").equals("5")
                        ||sharedpreferences.getString("button3","l").equals("5")
                        ||sharedpreferences.getString("button4","l").equals("5"))) {
                    if (sharedpreferences.getString("button", "1").equals("1")) {
                        editor.putString("button1", "5");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("2")) {
                        editor.putString("button2", "5");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("3")) {
                        editor.putString("button3", "5");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("4")) {
                        editor.putString("button4", "5");
                        editor.commit();
                    }

                    finish();
                }
                else
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("This App Is Already Added");

                    Toast toast = new Toast(Apps_Picker.this);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();


                }

            }
        });

        addcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(sharedpreferences.getString("button1","l").equals("6")
                        ||sharedpreferences.getString("button2","l").equals("6")
                        ||sharedpreferences.getString("button3","l").equals("6")
                        ||sharedpreferences.getString("button4","l").equals("6"))) {

                    if (sharedpreferences.getString("button", "1").equals("1")) {
                        editor.putString("button1", "6");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("2")) {
                        editor.putString("button2", "6");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("3")) {
                        editor.putString("button3", "6");
                        editor.commit();
                    } else if (sharedpreferences.getString("button", "1").equals("4")) {
                        editor.putString("button4", "6");
                        editor.commit();
                    }

                    finish();
                }
                else
                {


                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("This App Is Already Added");

                    Toast toast = new Toast(Apps_Picker.this);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }

            }
        });



    }
}

