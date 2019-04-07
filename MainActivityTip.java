package com.example.shanylajara.spinner_tip;


import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.NumberFormat;

public class MainActivityTip extends AppCompatActivity  implements TextWatcher, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private EditText editTextBillAmount;
    private TextView textViewBillAmount;
    private TextView textViewPercent;
    private TextView tipTextView;
    private SeekBar Percentage;
    private TextView text_tip_amount;
    private TextView text_total;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private String spinner_label = "";
    public static final String TAG = "MainActivityTip";
    private TextView RoundText;
    private RadioButton RD_TIP;
    private RadioButton RD_TOTAL;
    private RadioButton RD_NO;
    private TextView total_split;
    private TextView split_total_textview;
    int bill_split_is = 0;


    
    private double billAmount = 0.0;
    private double percent = 0.0;

    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();
    private AdapterView adapterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tip);
     
        split_total_textview = (TextView) findViewById(R.id.split_total_textview);


        total_split = (TextView) findViewById(R.id.total_split);


        RD_TIP = (RadioButton) findViewById(R.id.RD_TIP);
        RD_TOTAL = (RadioButton) findViewById(R.id.RD_TOTAL);
        RD_NO = (RadioButton) findViewById(R.id.RD_NO);
        RoundText = (TextView) findViewById(R.id.RoundText);


        editTextBillAmount = (EditText) findViewById(R.id.editTextBillAmount);
        editTextBillAmount.addTextChangedListener((TextWatcher) this);
        
        textViewBillAmount = (TextView) findViewById(R.id.textViewBillAmount);

        textViewPercent = (TextView) findViewById(R.id.textViewPercent);

        tipTextView = (TextView) findViewById(R.id.tipTextView);

        text_tip_amount = (TextView) findViewById(R.id.text_tip_amount);

        text_total = (TextView) findViewById(R.id.text_total);

        ////// SEEKBAR /////////////

        Percentage = (SeekBar) findViewById(R.id.seekBar1);
        Percentage.setOnSeekBarChangeListener(this);

        ///// SPINNER ///////////

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.labels_array, android.R.layout.simple_spinner_item);

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(adapter);
        }
       


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                 String themsg = split_total_textview.getText().toString();
                 String message = "Hi there, Bill amount has been split into:" +themsg+" amount per person! ";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi there, Bill amount has been split into:" +themsg+" amount per person!");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                return true;

            case R.id.action_info:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityTip.this);
                alertDialog.setTitle("Split Info");
                alertDialog.setMessage("Spinner is used to split the total among friends.");
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

 
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("MainActivity", "inside onTextChanged method: charSequence= " + charSequence);
   


        try {
            billAmount = Double.parseDouble(charSequence.toString());
            Log.d("MainActivity", "Bill Amount = " + billAmount);
            //setText on the textView
        } catch (NumberFormatException w) {
            billAmount = 0;
        }
        textViewBillAmount.setText(currencyFormat.format(billAmount));


        calculate();


    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        percent = (double) progress;

        //calculate percent based on seeker value
        calculate();


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onRadioButtonClicked(View view) {
        
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.RD_TIP:
                if (checked) {
                    double tip = billAmount * percent / 100;
                    double total = billAmount + tip;

                    final Integer tipRounded = (int) Math.ceil(tip);
                    System.out.println("tip:" + tip);
                    String tipRoundd = String.valueOf(tipRounded);
                    tipTextView.setText(tipRoundd);

                    String totalplusTipRounded = String.valueOf(tipRounded+billAmount);
                    textViewBillAmount.setText(totalplusTipRounded);

                    long TOTALW = (long) (tipRounded+billAmount);

                    bill_split_is = (int) ((double)tipRounded+billAmount);


                }

               
                break;

            case R.id.RD_TOTAL:
                if (checked) {

                    double tip = billAmount * percent / 100;
                    double total = billAmount + tip;

                    final Integer totalRounded = (int) Math.ceil(total);

                    String total_Rounded = String.valueOf((double)totalRounded);
                    textViewBillAmount.setText(total_Rounded);
                    tipTextView.setText(String.valueOf(tip));

                    bill_split_is = (int) ((double)totalRounded);

                    System.out.println("total:" + total);

                }
            
                break;

            case R.id.RD_NO:
                if (checked) {
                    double tip = billAmount * percent / 100;
                    double total = (billAmount + tip);

                    tipTextView.setText(String.valueOf(tip));
                    split_total_textview.setText("0.00");
                    textViewBillAmount.setText(String.valueOf(total));

                }
               
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        Log.d(TAG, "inside on ItemSelected");
        spinner_label = adapterView.getItemAtPosition(pos).toString();
        Toast.makeText(this, "You chose " + spinner_label, Toast.LENGTH_LONG).show();
    

        switch (pos) {
            case 1:
                Toast.makeText(adapterView.getContext(), "Bill for 2", Toast.LENGTH_SHORT).show();
                calculate2(2);


                break;
            case 2:
                Toast.makeText(adapterView.getContext(), "Bill for 3!", Toast.LENGTH_SHORT).show();
                calculate2(3);

                break;
            case 3:
                Toast.makeText(adapterView.getContext(), "Bill for 4!", Toast.LENGTH_SHORT).show();
                calculate2(4);

                break;
            case 4:
                Toast.makeText(adapterView.getContext(), "Bill for 5!", Toast.LENGTH_SHORT).show();
                calculate2(5);

                break;

        }


    }


    // calculate and display tip and total amounts
    private void calculate() {
        Log.d("MainActivity", "inside calculate method");

        textViewPercent.setText("" + percent + "%");

        double tip = billAmount * percent / 100;
        double total = billAmount + tip;

        tipTextView.setText(currencyFormat.format(tip));
        textViewBillAmount.setText(currencyFormat.format(total));


    }

    private void calculate2(int value){

        double tip = billAmount * percent / 100;
        double total = billAmount + tip;

        int thisOne = bill_split_is;
        if (thisOne == 0){
            split_total_textview.setText(String.valueOf(total/value));
        }
        else {
            split_total_textview.setText(String.valueOf(thisOne / value));
        }

    }
}
