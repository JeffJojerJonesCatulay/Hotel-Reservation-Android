
package com.example.exer6catulayjeffjojerjones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<String> room_capacity = new ArrayList<>();
    ArrayList<String> payment_type = new ArrayList<>();

    private TextView output_name, output_check_in, output_check_out, number_of_days,payment_total;
    private EditText input_name;
    private Button save_data, date_check_in, date_check_out, clear_data, clear_date_only, compute_payment, complete_transaction;
    private Spinner room_capacity_spinner, payment_type_spinner;
    private RadioButton suite, deluxe, regular;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    public static final String CHECKIN = "checkin";
    public static final String CHECKOUT = "checkout";
    private final static String TOTALDAYS = "days";
    private final static String ROOMCAPACITY = "capacity";
    private final static String ROOMTYPE1 = "suite";
    private final static String ROOMTYPE2 = "deluxe";
    private final static String ROOMTYPE3 = "regular";
    private final static String PAYMENT = "payment";
    private final static String PAYMENTVALUE = "php";

    String checkIn, checkOut;
    String nameVal, checkIn_Val, checkOut_Val, payment_Val;
    long difference;
    Date in, out;
    int year_checkIn;
    int year_checkOut;
    int month_checkIn;
    int month_checkOut;
    int day_CheckIn;
    int day_checkOut;
    int rate_per_day;
    int roomCapacitySelected_Value, paymentTypeSelected_Value, totalDays;
    double partial_payment, additional_charges, additional_payment;
    int final_payment;
    boolean suiteOption, deluxeOption, regularOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output_name = findViewById(R.id.output_name);
        input_name = findViewById(R.id.input_name);
        save_data = findViewById(R.id.saveBTM);
        date_check_in = findViewById(R.id.check_in_BTN);
        date_check_out = findViewById(R.id.check_out_BTN);
        clear_data = findViewById(R.id.clearBTM);
        output_check_in = findViewById(R.id.output_check_in);
        output_check_out = findViewById(R.id.output_check_out);
        number_of_days = findViewById(R.id.number_of_days);
        room_capacity_spinner = findViewById(R.id.room_capacity);
        payment_type_spinner = findViewById(R.id.payment_type);
        suite = findViewById(R.id.suite);
        deluxe = findViewById(R.id.de_luxe);
        regular = findViewById(R.id.regular);
        clear_date_only = findViewById(R.id.clear_date_btn);
        payment_total = findViewById(R.id.paymentTotal);
        compute_payment = findViewById(R.id.computeBTM);
        complete_transaction = findViewById(R.id.completeBTM);

        saveData();
        loadData();
        updateDate();
        clearData();
        dateSelector();
        roomCapacity();
        paymentType();
        clearDateOnly();
        paymentComputation();
        isDataLoaded();
        confirmTransactionCompletion();

        compute_payment.setEnabled(false);
        save_data.setEnabled(false);
        date_check_out.setEnabled(false);
        room_capacity_spinner.setSelection(roomCapacitySelected_Value);
        payment_type_spinner.setSelection(paymentTypeSelected_Value);

    }

    private void isDataLoaded(){
        if ((output_name.getText().toString().length() != 0) && (output_check_in.getText().toString().length() != 0) && (output_check_in.getText().toString().length() != 0) && (payment_total.getText().toString().length() != 0)){
            input_name.setEnabled(false);
            date_check_in.setEnabled(false);
            date_check_out.setEnabled(false);
            clear_date_only.setEnabled(false);
            room_capacity_spinner.setEnabled(false);
            suite.setEnabled(false);
            deluxe.setEnabled(false);
            regular.setEnabled(false);
            payment_type_spinner.setEnabled(false);
            compute_payment.setEnabled(false);
            save_data.setEnabled(false);
            clear_data.setEnabled(false);
            complete_transaction.setEnabled(true);
        }
        else {
            input_name.setEnabled(true);
            date_check_in.setEnabled(true);
            date_check_out.setEnabled(true);
            clear_date_only.setEnabled(true);
            room_capacity_spinner.setEnabled(true);
            suite.setEnabled(true);
            deluxe.setEnabled(true);
            regular.setEnabled(true);
            payment_type_spinner.setEnabled(true);
            compute_payment.setEnabled(true);
            save_data.setEnabled(true);
            clear_data.setEnabled(true);
            complete_transaction.setEnabled(false);
        }
    }

    private void confirmTransactionCompletion(){
        complete_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completion("Do you want to proceed?");
            }
        });
    }

    private void completeTransaction(){
        input_name.setEnabled(true);
        date_check_in.setEnabled(true);
        date_check_out.setEnabled(true);
        clear_date_only.setEnabled(true);
        room_capacity_spinner.setEnabled(true);
        suite.setEnabled(true);
        deluxe.setEnabled(true);
        regular.setEnabled(true);
        payment_type_spinner.setEnabled(true);
        compute_payment.setEnabled(true);
        save_data.setEnabled(true);
        clear_data.setEnabled(true);
        complete_transaction.setEnabled(false);

        try {
            sharedPreferences.edit().remove(NAME).apply();
            sharedPreferences.edit().remove(CHECKIN).apply();
            sharedPreferences.edit().remove(CHECKOUT).apply();
            sharedPreferences.edit().remove(ROOMCAPACITY).apply();
            sharedPreferences.edit().remove(ROOMTYPE1).apply();
            sharedPreferences.edit().remove(ROOMTYPE2).apply();
            sharedPreferences.edit().remove(ROOMTYPE3).apply();
            sharedPreferences.edit().remove(PAYMENT).apply();
            sharedPreferences.edit().remove(TOTALDAYS).apply();
            sharedPreferences.edit().remove(PAYMENTVALUE).apply();

            room_capacity_spinner.setSelection(0);
            suite.setChecked(false);
            deluxe.setChecked(false);
            regular.setChecked(false);
            payment_type_spinner.setSelection(0);
            payment_total.setText("");

            output_name.setText("");
            output_check_in.setText("");
            output_check_out.setText("");
            number_of_days.setText("");
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    private void paymentComputation(){
        if ((output_check_in.getText().toString().length() != 0) || (output_check_out.getText().toString().length() != 0) || (number_of_days.getText().toString().length() != 0)
        || (payment_total.getText().toString().length() != 0)
        ){
            compute_payment.setEnabled(false);
            save_data.setEnabled(false);
        }
        compute_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((suite.isChecked()) || (deluxe.isChecked()) || (regular.isChecked())){
                        int capacity_selected = room_capacity_spinner.getSelectedItemPosition();
                        if (capacity_selected == 0){
                            if (suite.isChecked()){
                                rate_per_day = 500;
                            }
                            else if (deluxe.isChecked()){
                                rate_per_day = 300;
                            }
                            else if (regular.isChecked()){
                                rate_per_day = 100;
                            }
                        }
                        else if (capacity_selected == 1){
                            if (suite.isChecked()){
                                rate_per_day = 800;
                            }
                            else if (deluxe.isChecked()){
                                rate_per_day = 500;
                            }
                            else if (regular.isChecked()){
                                rate_per_day = 200;
                            }
                        }
                        else if (capacity_selected == 2){
                            if (suite.isChecked()){
                                rate_per_day = 1000;
                            }
                            else if (deluxe.isChecked()){
                                rate_per_day = 750;
                            }
                            else if (regular.isChecked()){
                                rate_per_day = 500;
                            }
                        }

                        int payment_selected = payment_type_spinner.getSelectedItemPosition();
                        if (payment_selected == 0){
                            additional_charges = 0;
                        }
                        else if (payment_selected == 1){
                            additional_charges = 0.05;
                        }
                        else if (payment_selected == 2){
                            additional_charges = 0.1;
                        }

                        partial_payment = totalDays * rate_per_day;
                        additional_payment = partial_payment * additional_charges;
                        final_payment = (int) (partial_payment + additional_payment);
                        String formatted_value = NumberFormat.getNumberInstance(Locale.US).format(final_payment) + " PHP";
                        payment_total.setText(formatted_value);

                        input_name.setEnabled(false);
                        save_data.setEnabled(true);
                        compute_payment.setEnabled(false);
                        room_capacity_spinner.setEnabled(false);
                        suite.setEnabled(false);
                        deluxe.setEnabled(false);
                        regular.setEnabled(false);
                        payment_type_spinner.setEnabled(false);
                        clear_date_only.setEnabled(false);
                    }
                    else {
                        showMessage("Error", "Please Complete Details");
                    }

                }catch (Exception e){
                    showMessage("Error", "Something is Wrong!");
                }
            }
        });
    }

    private void clearDateOnly(){
        clear_date_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sharedPreferences.edit().remove(CHECKIN).apply();
                    sharedPreferences.edit().remove(CHECKOUT).apply();
                    sharedPreferences.edit().remove(TOTALDAYS).apply();

                    output_check_in.setText("");
                    output_check_out.setText("");
                    number_of_days.setText("");

                    date_check_in.setEnabled(true);
                    date_check_out.setEnabled(true);
                    compute_payment.setEnabled(false);
                }catch (Exception e){
                    showMessage("Error", "Something is Wrong!");
                }
            }
        });
    }

    private void dateSelector(){
        date_check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment checkIn = new DatePickerFragment();
                checkIn.show(getSupportFragmentManager(), "date picker");
                date_check_in.setEnabled(false);
                date_check_out.setEnabled(true);
            }
        });

        date_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment checkOut = new DatePickerFragment();
                checkOut.show(getSupportFragmentManager(), "date picker");
                date_check_out.setEnabled(false);

            }
        });
    }

    private void roomCapacity(){
        room_capacity.add("Single");
        room_capacity.add("Double");
        room_capacity.add("Family");

        ArrayAdapter<String> capacity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, room_capacity );
        capacity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_capacity_spinner.setAdapter(capacity);
    }

    private void paymentType(){
        payment_type.add("Cash");
        payment_type.add("Check");
        payment_type.add("Credit Card");

        ArrayAdapter<String> type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, payment_type );
        type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment_type_spinner.setAdapter(type);
    }

    public void dayInterval(Date one, Date two){
        try {
            difference = ((one.getTime()-two.getTime())/86400000);
            if (difference == 0){
                if ((month_checkIn == month_checkOut) && (day_CheckIn == day_checkOut)){
                    totalDays = (int) (difference + 1);
                    number_of_days.setText(String.valueOf(totalDays));
                }
                else {
                    showMessage("Error", "Invalid Date!");
                    totalDays = (int) difference;
                    number_of_days.setText(String.valueOf(totalDays));
                }
            }
            else if (difference < 0 ){
                showMessage("Error", "Invalid Date!");
            }
            else {
                totalDays = (int) (difference + 1);
                number_of_days.setText(String.valueOf(totalDays));
            }
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    private void clearData(){
        clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sharedPreferences.edit().remove(NAME).apply();
                    sharedPreferences.edit().remove(CHECKIN).apply();
                    sharedPreferences.edit().remove(CHECKOUT).apply();
                    sharedPreferences.edit().remove(ROOMCAPACITY).apply();
                    sharedPreferences.edit().remove(ROOMTYPE1).apply();
                    sharedPreferences.edit().remove(ROOMTYPE2).apply();
                    sharedPreferences.edit().remove(ROOMTYPE3).apply();
                    sharedPreferences.edit().remove(PAYMENT).apply();
                    sharedPreferences.edit().remove(TOTALDAYS).apply();
                    sharedPreferences.edit().remove(PAYMENTVALUE).apply();

                    room_capacity_spinner.setSelection(0);
                    suite.setChecked(false);
                    deluxe.setChecked(false);
                    regular.setChecked(false);
                    payment_type_spinner.setSelection(0);
                    payment_total.setText("");

                    output_name.setText("");
                    output_check_in.setText("");
                    output_check_out.setText("");
                    number_of_days.setText("");

                    date_check_in.setEnabled(true);
                    date_check_out.setEnabled(false);
                    compute_payment.setEnabled(false);
                    save_data.setEnabled(false);
                    input_name.setText("");

                    suite.setEnabled(true);
                    deluxe.setEnabled(true);
                    regular.setEnabled(true);
                    room_capacity_spinner.setEnabled(true);
                    payment_type_spinner.setEnabled(true);
                    clear_date_only.setEnabled(true);
                    input_name.setEnabled(true);

                }catch (Exception e){
                    showMessage("Error", "Something is Wrong!");
                }
            }
        });
    }

    private void confirmData(){
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(NAME, input_name.getText().toString());
        editor.putString(CHECKIN, output_check_in.getText().toString());
        editor.putString(CHECKOUT, output_check_out.getText().toString());
        editor.putInt(TOTALDAYS, Integer.parseInt(number_of_days.getText().toString()));
        editor.putInt(ROOMCAPACITY, room_capacity_spinner.getSelectedItemPosition());
        editor.putBoolean(ROOMTYPE1, suite.isChecked());
        editor.putBoolean(ROOMTYPE2, deluxe.isChecked());
        editor.putBoolean(ROOMTYPE3, regular.isChecked());
        editor.putInt(PAYMENT, payment_type_spinner.getSelectedItemPosition());
        editor.putString(PAYMENTVALUE, payment_total.getText().toString());
        editor.apply();
        showMessage("Success", "Booking Added");
    }

    public void saveData(){
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if ((input_name.getText().length() == 0) || (output_check_in.getText().equals("")) || (output_check_out.getText().equals(""))){
                        showMessage("Error", "Please Complete Details");
                    }
                    else {
                        if ((suite.isChecked()) || (deluxe.isChecked()) || (regular.isChecked())) {
                            confirmation("Book Now?");
                            input_name.setEnabled(false);
                            date_check_in.setEnabled(false);
                            date_check_out.setEnabled(false);
                            clear_date_only.setEnabled(false);
                            room_capacity_spinner.setEnabled(false);
                            suite.setEnabled(false);
                            deluxe.setEnabled(false);
                            regular.setEnabled(false);
                            payment_type_spinner.setEnabled(false);
                            compute_payment.setEnabled(false);
                            save_data.setEnabled(false);
                            clear_data.setEnabled(false);
                            complete_transaction.setEnabled(true);
                        }
                        else {
                            showMessage("Error", "Please Complete Details");
                        }
                    }
                }catch (Exception e){
                    showMessage("Error", "Something is Wrong!");
                }
            }
        });
    }

    public void loadData(){
        try {
            sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            nameVal = sharedPreferences.getString(NAME, "");
            checkIn_Val  = sharedPreferences.getString(CHECKIN, "");
            checkOut_Val  = sharedPreferences.getString(CHECKOUT, "");
            totalDays = sharedPreferences.getInt(TOTALDAYS, 0);
            roomCapacitySelected_Value = sharedPreferences.getInt(ROOMCAPACITY, 0);
            suiteOption = sharedPreferences.getBoolean(ROOMTYPE1, false);
            deluxeOption = sharedPreferences.getBoolean(ROOMTYPE2, false);
            regularOption = sharedPreferences.getBoolean(ROOMTYPE3, false);
            paymentTypeSelected_Value = sharedPreferences.getInt(PAYMENT, 0);
            payment_Val = sharedPreferences.getString(PAYMENTVALUE, "");
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    public void updateDate(){
        try {
            output_name.setText(nameVal);
            output_check_in.setText(checkIn_Val);
            output_check_out.setText(checkOut_Val);
            suite.setChecked(suiteOption);
            deluxe.setChecked(deluxeOption);
            regular.setChecked(regularOption);
            number_of_days.setText(String.valueOf(totalDays));
            payment_total.setText(payment_Val);
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            checkIn = currentDateString;
            checkOut = currentDateString;
            if (output_check_in.getText().toString().isEmpty()){
                output_check_in.setText(checkIn);
                in = c.getTime();
                year_checkIn = year;
                month_checkIn = month;
                day_CheckIn = dayOfMonth;
            }
            else {
                year_checkOut = year;
                month_checkOut = month;
                day_checkOut = dayOfMonth;
                if (year_checkIn != year_checkOut){
                    showMessage("Error!", "Invalid Date");
                }
                else {
                    output_check_out.setText(checkOut);
                    out = c.getTime();
                    dayInterval(out, in);
                    compute_payment.setEnabled(true);
                }
            }
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void confirmation(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            confirmData();
                        }
                    })
                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            save_data.setEnabled(true);
                            clear_data.setEnabled(true);
                            complete_transaction.setEnabled(false);
                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }

    public void completion(String title){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(title)
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            completeTransaction();
                        }
                    })
                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            save_data.setEnabled(true);
                            clear_data.setEnabled(true);
                            complete_transaction.setEnabled(false);
                        }
                    });
            builder.show();
        }catch (Exception e){
            showMessage("Error", "Something is Wrong!");
        }
    }
}