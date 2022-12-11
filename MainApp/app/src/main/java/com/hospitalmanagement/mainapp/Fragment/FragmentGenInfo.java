package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.Sessions.DatabaseHelper;
import com.hospitalmanagement.mainapp.Sessions.SessionManager;

public class FragmentGenInfo extends Fragment {
    View v;

    //UI Components
    TextInputEditText editAreaName, editFamilyNum, editMobile;
    RadioGroup radioToilet, radioBPL, radioHouse, radioWater, radioCaste;
    MaterialButton btnSaveGenInfo;
    ProgressDialog progressDialog;

    //Saving Information
    SessionManager sessionManager;
    DatabaseHelper databaseHelper;

    //Validation
    AwesomeValidation genInfoValidation;

    //Accepted Inputs;
    String areaName, water="सार्वजनिक", caste="SC", mobile="";
    int familyNum=0;
    boolean toilet=true, house=false, bpl=true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater =
                LayoutInflater.from(new ContextThemeWrapper(getContext(),
                        R.style.AppTheme));
        v = inflater.inflate(R.layout.fragment_geninfo, container,false);
        initApp();
        addValidation();
        //button listener
        btnSaveGenInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(genInfoValidation.validate()) {
                    showProgressBar("माहिती जतन करीत आहे. कृपया प्रतीक्षा करा...");
                    areaName = editAreaName.getText().toString();
                    sessionManager.setGeneralInfo(areaName, toilet, bpl, house, water, caste,
                            familyNum, mobile, true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            stopProgressBar();
                            FragmentTransaction fragmentTransaction =
                                    getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container,
                                    new FragmentMemberDetails());
                            fragmentTransaction.commit();
                        }
                    }, 1000);

                }
            }
        });

        radioToilet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton toiletRb = (RadioButton) v.findViewById(checkedId);
                if(toiletRb.getText().equals("आहे")) {
                    toilet = true;
                } else {
                    toilet = false;
                }
            }
        });

        radioHouse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton houseRb = (RadioButton) v.findViewById(checkedId);
                if(houseRb.getText().equals("कच्चे")) {
                    house = false;
                } else if(houseRb.getText().equals("पक्के")) {
                    house = true;
                }
            }
        });

        radioBPL.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton bplRb = (RadioButton) v.findViewById(checkedId);
                if(bplRb.getText().equals("आहे")) {
                    bpl = true;
                } else  {
                    bpl = false;
                }
            }
        });

        radioWater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton waterRb = (RadioButton) v.findViewById(checkedId);
                water = waterRb.getText().toString();
            }
        });

        radioCaste.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton casteRb = (RadioButton) v.findViewById(checkedId);
                caste = casteRb.getText().toString();
            }
        });

        return v;
    }

    private void initApp() {
        //Initializing components
        editAreaName = (TextInputEditText) v.findViewById(R.id.editAreaname);
        radioToilet = (RadioGroup) v.findViewById(R.id.radioToilet);
        radioHouse = (RadioGroup) v.findViewById(R.id.radioHouse);
        radioBPL = (RadioGroup) v.findViewById(R.id.radioBpl);
        radioWater = (RadioGroup) v.findViewById(R.id.radioWater);
        radioCaste = (RadioGroup) v.findViewById(R.id.radioCaste);
        btnSaveGenInfo = (MaterialButton) v.findViewById(R.id.btnSaveGenInfo);
        sessionManager = new SessionManager(getContext());
        databaseHelper = new DatabaseHelper(v.getContext());
        genInfoValidation = new AwesomeValidation(ValidationStyle.BASIC);
        progressDialog = new ProgressDialog(getContext());
        getGenInfo();
    }

    private void addValidation() {
        genInfoValidation.addValidation(editAreaName, RegexTemplate.NOT_EMPTY, "गाव नाव प्रविष्ट " +
                "करा");
    }

    private void showProgressBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void stopProgressBar() {
        progressDialog.dismiss();
    }

    private void getGenInfo() {
        RadioButton radioButton;
        if (sessionManager.isGenInfoStored()) {
            editAreaName.setText(sessionManager.getAreaName());
            if (sessionManager.getToilet()) {
                toilet = true;
                radioButton = (RadioButton) v.findViewById(R.id.radioToiletYes);
                radioButton.setChecked(true);
            } else {
                toilet = false;
                radioButton = (RadioButton) v.findViewById(R.id.radioToiletNo);
                radioButton.setChecked(true);
            }

            if (sessionManager.getBPL()) {
                bpl = true;
                radioButton = (RadioButton) v.findViewById(R.id.radioBplYes);
                radioButton.setChecked(true);
            } else {
                bpl = false;
                radioButton = (RadioButton) v.findViewById(R.id.radioBplNo);
                radioButton.setChecked(true);
            }

            if (sessionManager.getHouse()) {
                house = true;
                radioButton = (RadioButton) v.findViewById(R.id.radioHousePakka);
                radioButton.setChecked(true);
            } else {
                house = false;
                radioButton = (RadioButton) v.findViewById(R.id.radioHouseKaccha);
                radioButton.setChecked(true);
            }
            water = sessionManager.getWater();
            if (water.equals("सार्वजनिक")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioWaterPublic);
                radioButton.setChecked(true);
            } else if (water.equals("बोअर")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioWaterBore);
                radioButton.setChecked(true);
            } else if (water.equals("विहीर")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioWaterHandpump);
                radioButton.setChecked(true);
            } else if (water.equals("हातपंप")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioWaterHaatpump);
                radioButton.setChecked(true);
            }
            caste = sessionManager.getCaste();
            if (caste.equals("प्रवर्ग अनुसूचित जाती")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioCasteSc);
                radioButton.setChecked(true);
            } else if (caste.equals("अनु.जमाती")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioCasteSt);
                radioButton.setChecked(true);
            } else if (caste.equals("इतर")) {
                radioButton = (RadioButton) v.findViewById(R.id.radioCasteOthers);
                radioButton.setChecked(true);
            }
            displayAlertDialog();
        }
    }

    private void displayAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Message")
                .setMessage("आपण या नोंदणीसह सुरू ठेवू इच्छिता?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initDefault();
                        sessionManager.clearAllInformation();
                        databaseHelper.deleteAllData();
                        FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new FragmentGenInfo());
                        fragmentTransaction.commit();
                    }
                }).create();
        alertDialog.show();
    }
    private void initDefault() {
        water="सार्वजनिक";
        caste="प्रवर्ग अनुसूचित जाती";
        toilet=true;
        house=false;
        bpl=true;
    }
}
