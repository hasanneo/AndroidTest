package com.example.androidtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.squareup.picasso.Picasso;


import java.util.List;

public class DataScreen extends AppCompatActivity {
    int id;
    Context context;
    String rankName;
    String imageURL;
    String textColor;
    String backgroundColor;
    ImageView icon;
    TextView idRankTxt;
    TextView textColorTxt;
    TextView bgColorTxt;
    EditText urlTxt;
    EditText nameRankTxt;
    Button saveBtn;
    Button delBtn;
    int callingActivity;
    public final int RECYCLERVIEW_CLICK = 1;
    public final int FAB_CLICK = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_b);
        setIDs();
        context = this;
        callingActivity = getIntent().getIntExtra("activity", -1);
        //Check who called this activity
        if (callingActivity == RECYCLERVIEW_CLICK) {
            //if the calling activity is from the adapter
            setValuesFromIntent();
            delBtn.setOnClickListener(new ClickListner(this));
        } else if (callingActivity == FAB_CLICK) {
            //if calling activity is from the fab (insert)
            setDefaultValues();
            delBtn.setEnabled(false);//disable delete button
        }
        setClickListeners();
    }

    /**
     * Setting click listeners
     */
    private void setClickListeners() {
        textColorTxt.setOnClickListener(new ClickListner(this));
        saveBtn.setOnClickListener(new ClickListner(this));
        icon.setOnClickListener(new ClickListner(this));
        bgColorTxt.setOnClickListener(new ClickListner(this));
        urlTxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!urlTxt.getText().toString().isEmpty()) {
                        imageURL = urlTxt.getText().toString();
                        // Perform action on key press
                        Picasso.with(context).load(imageURL).fit().centerInside().into(icon);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Set ids for the views in the layout
     */
    private void setIDs() {
        idRankTxt = findViewById(R.id.id_rank_text);
        nameRankTxt = findViewById(R.id.rank_name_txt);
        textColorTxt = findViewById(R.id.text_color);
        bgColorTxt = findViewById(R.id.background_color);
        icon = findViewById(R.id.icon_image);
        urlTxt = findViewById(R.id.url_txt);
        saveBtn = findViewById(R.id.save_btn);
        delBtn = findViewById(R.id.del_btn);
    }

    /**
     * A method that sets values to the ImageView and TextViews
     */
    private void setViewsToValues() {
        int color = Color.parseColor(textColor);
        int bgColor = Color.parseColor(backgroundColor);
        idRankTxt.setText("ID:" + id);
        nameRankTxt.setText(rankName);
        idRankTxt.setTextColor(color);
        nameRankTxt.setTextColor(color);
        nameRankTxt.setBackgroundColor(bgColor);
        urlTxt.setText(imageURL);
        textColorTxt.setText("TEXT:" + textColor);
        textColorTxt.setTextColor(color);
        bgColorTxt.setText("BG" + backgroundColor);
        Picasso.with(this).load(imageURL).fit().centerInside().into(icon);
    }

    /**
     * ClickListener class to implement each listener for the clicks
     */
    private class ClickListner implements View.OnClickListener {
        Context context;

        ClickListner(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            List<FeedItem> list = MainActivity.viewAdapter.feedList;//pointer to the list
            switch (v.getId()) {
                case R.id.save_btn://save button click
                    if (callingActivity == FAB_CLICK) {
                        //insert
                        //check empty later...
                        //add to the last of the list
                        imageURL = urlTxt.getText().toString();
                        //textColor=textColorTxt.getText().toString();
                        int id = list.size();
                        list.add(new FeedItem(id, rankName, urlTxt.getText().toString(), textColor, backgroundColor));
                        MainActivity.viewAdapter.notifyItemInserted(id);
                    } else if (callingActivity == RECYCLERVIEW_CLICK) {
                        //Update selected item
                        list.get(id).setNameRank(nameRankTxt.getText().toString());
                        list.get(id).setIconUrL(imageURL);
                        list.get(id).setColorText(textColor);
                        list.get(id).setColorBackground(backgroundColor);
                        MainActivity.viewAdapter.notifyItemChanged(id);
                    }
                    finish();//end activity
                    break;
                case R.id.del_btn://delete button click
                    list.remove(id);
                    MainActivity.viewAdapter.notifyItemRemoved(id);
                    finish();
                    break;
                case R.id.text_color://text color click
                    //Opening color picker
                    new ColorPickerDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("ColorPicker Dialog")
                            .setPreferenceName("MyColorPickerDialog")
                            .setPositiveButton(getString(R.string.confirm),
                                    new ColorEnvelopeListener() {
                                        @Override
                                        public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                            setTextColor(envelope);
                                        }
                                    })
                            .setNegativeButton(getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                            .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                            .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                            .show();
                    break;
                case R.id.background_color://text color click
                    new ColorPickerDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("ColorPicker Dialog")
                            .setPreferenceName("MyColorPickerDialog")
                            .setPositiveButton(getString(R.string.confirm),
                                    new ColorEnvelopeListener() {
                                        @Override
                                        public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                            setTextBgColor(envelope);
                                        }
                                    })
                            .setNegativeButton(getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                            .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                            .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                            .show();
                    break;
            }
        }
    }

    private void setTextColor(ColorEnvelope envelope) {
        textColor = "#" + envelope.getHexCode();
        textColorTxt.setText("TEXT COLOR:" + textColor);
        textColorTxt.setTextColor(Color.parseColor(textColor));
    }

    private void setTextBgColor(ColorEnvelope envelope) {
        backgroundColor = "#" + envelope.getHexCode();
        bgColorTxt.setText("BG:" + backgroundColor);
    }

    /**
     * Getting the values that were passed from the calling activity
     */
    private void setValuesFromIntent() {
        //Get values sent from the previous activity
        id = getIntent().getIntExtra("idRank", 0);
        rankName = getIntent().getStringExtra("nameRank");
        imageURL = getIntent().getStringExtra("imageURL");
        textColor = getIntent().getStringExtra("textColor");
        backgroundColor = getIntent().getStringExtra("bgColor");
        setViewsToValues();
    }

    /**
     * Setting default values for the data
     */
    private void setDefaultValues() {
        id = getIntent().getIntExtra("idRank", 0);
        rankName = "RANK NAME";
        imageURL = "https://img.icons8.com/doodle/48/000000/link--v2.png";
        textColor = "#000000";
        backgroundColor = "#ffffff";
        idRankTxt.setText("ID:" + id);
        nameRankTxt.setText(rankName);
        setViewsToValues();
    }
}

