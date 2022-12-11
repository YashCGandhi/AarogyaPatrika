package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hospitalmanagement.mainapp.GetterSetter.Delivery;
import com.hospitalmanagement.mainapp.GetterSetter.FamilyMemberList;
import com.hospitalmanagement.mainapp.GetterSetter.JSY;
import com.hospitalmanagement.mainapp.GetterSetter.PMMVY;
import com.hospitalmanagement.mainapp.GetterSetter.PregnantDetails;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.Sessions.DatabaseHelper;
import com.hospitalmanagement.mainapp.Sessions.SessionManager;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentCreatePregnantDetails extends Fragment {
    View v;
    //Managers
    SessionManager sessionManager;
    DatabaseHelper databaseHelper;
    ProgressDialog progressDialog;

    //Arraylists
    ArrayList<FamilyMemberList> familyMemberLists;
    ArrayList<String> motherNameList, motherIdList, prevMotherNameList, prevMotherIdList;
    ArrayList<PregnantDetails> pregnantDetailsArrayList;

    //Components
    RelativeLayout mainJSYLayouts, fieldsOfJsy, relPregnantLayout, mainPMMVYLayout,
            mainChildrenLayout, fieldsOfPMMVY, pregnancyTypeLayout, prevPregnantLayout;
    TextView textSelectedLastDate, textFieldExpectedDate, textSelectedBenifitDate,
            textSelectedDateOfRegistration, textSelectedSecondInstallment,
            textSelectedThirdInstallment, textPreviousPregnant, textPregnantNumber, textPregnantId, textPregnantInfo;
    MaterialButton btnAddPregDetail, btnNextChildLayout, btnAddPreg, btnNextChild,
            btnDeletePregnant;
    MaterialSpinner motherSpinner, prevMotherSpinner;
    TextInputEditText editPara, editCompilation, editPlaceOfDelivery, editOther, editPaidAmount;
    RadioGroup radioJSY, radioPMMVY, radioDelivered, radioOutcome, radioPregnancyType;
    ImageView imageLastCalendar, imageBenifitCalendar, imageRegisterCalendar, imageSecondCalendar, imageThirdCalendar;
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    AwesomeValidation pregnantValidation;
    PregnantDetails pregnantDetails;
    NestedScrollView pregnantScrollView;
    //jsy radio
    RadioButton radioJsyYes, radioJsyNo;
    //pmmvy radio
    RadioButton radioPmmvyYes, radioPmmvyNo;
    //delivered radio
    RadioButton radioDeliveredYes, radioDeliveredNo;
    //pregnancy type radio
    RadioButton radioPregnancyTypeNormal, radioPregnancyTypeLscs, radioPregnancyTypeOther,
            radioPregnancyTypeScheduledSchezariun, radioPregnancyTypeUnScheduledSchezariun,
            radioPregnancyTypeVbac;

    //Acceptedinputs
    int year, month, day;
    int flag=0;
    int para;
    boolean prevFlag = false, finalFlag = false;
    boolean jsy=false, pmmvy=false, delivered=false;
    String outcome="पुरुष", pregnancyType="सामान्य प्रसूती", complication, place, lastDate, expectedDate,
            paidAmount, benifitDate, dor, second, third, other, motherName, motherId;
    Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater =
                LayoutInflater.from(new ContextThemeWrapper(getContext(),
                        R.style.AppTheme));
        v = inflater.inflate(R.layout.fragment_create_pregnant, container,false);
//        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//                month = month + 1;
//                String date = day+"/"+(month)+"/"+year;
//                switch (flag) {
//                    case 0:
//                        textSelectedLastDate.setText(date);
//                        Calendar calendar1 = Calendar.getInstance();
//                        calendar1.set(Calendar.DAY_OF_MONTH, day);
//                        calendar1.set(Calendar.MONTH, month);
//                        calendar1.set(Calendar.YEAR, year);
//                        calendar1.add(Calendar.MONTH, 9);
//                        calendar1.add(Calendar.DAY_OF_MONTH, 7);
//                        String date1 =
//                                calendar1.get(Calendar.DAY_OF_MONTH)+"/"+calendar1.get(Calendar.MONTH)+"/"+calendar1.get(Calendar.YEAR);
//                        textFieldExpectedDate.setText(date1);
//                        break;
//                    case 1:
//                        textSelectedBenifitDate.setText(date);
//                        break;
//                    case 2:
//                        textSelectedDateOfRegistration.setText(date);
//                        break;
//                    case 3:
//                        textSelectedSecondInstallment.setText(date);
//                        break;
//                    case 4:
//                        textSelectedThirdInstallment.setText(date);
//                        break;
//                }
//            }
//        };
        initApp();
        addValidation();
        checkForPreviousPregnants();
        textPregnantId.setText(getOid());
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        radioDelivered.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                if (radioButton.getId() == R.id.radioDeliveredYes) {
                    delivered = true;
                    pregnancyTypeLayout.setVisibility(View.VISIBLE);
                } else if (radioButton.getId() == R.id.radioDeliveredNo) {
                    delivered = false;
                    pregnancyTypeLayout.setVisibility(View.GONE);
                }
            }
        });
        radioPMMVY.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Yes")) {
                    pmmvy = true;
                    fieldsOfPMMVY.setVisibility(View.VISIBLE);
                } else if(radioButton.getText().toString().equals("No")) {
                    pmmvy = false;
                    fieldsOfPMMVY.setVisibility(View.GONE);
                }
            }
        });
        radioJSY.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Yes")) {
                    jsy = true;
                    fieldsOfJsy.setVisibility(View.VISIBLE);
                } else if (radioButton.getText().toString().equals("No")) {
                    jsy = false;
                    fieldsOfJsy.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioOutcome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                if (radioButton.getId() == R.id.radioOutcomeMale) {
                    outcome = "पुरुष";
                } else if (radioButton.getId() == R.id.radioOutcomeFemale) {
                    outcome = "स्त्री";
                } else if (radioButton.getId() == R.id.radioOutcomeOthers) {
                    outcome = "इतर";
                }
            }
        });
        radioPregnancyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                if (radioButton.getId() == R.id.radioPregnancyTypeNormal) {
                    pregnancyType = "सामान्य प्रसूती";
                    pregnantValidation.clear();
                    pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "para प्रविष्ट करा");
                    pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
                } else if (radioButton.getId() == R.id.radioPregnancyTypeLSCS) {
                    pregnancyType = "सिझरियन प्रसूती";
                    pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "para प्रविष्ट करा");
                    pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
                } else if (radioButton.getId() == R.id.radioPregnancyTypeOther) {
                    pregnancyType = "इतर";
                    pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "para प्रविष्ट करा");
                    pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
                    pregnantValidation.addValidation(editOther, RegexTemplate.NOT_EMPTY, "कृपया इतर गर्भधारणेचा प्रकार प्रविष्ट करा");
                } else if (radioButton.getId() == R.id.radioPregnancyTypeScheduleSchezaruin) {
                    pregnancyType = "वेळापत्रक योजना";
                    pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "para प्रविष्ट करा");
                    pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
                } else if (radioButton.getId() == R.id.radioPregnancyTypeUnScheduleSchezaruin) {
                    pregnancyType = "योजनाबद्ध नसलेले";
                    pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "para प्रविष्ट करा");
                    pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
                } else if (radioButton.getId() == R.id.radioPregnancyTypeVbac) {
                    pregnancyType = "वि बी ए सी";
                    pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "para प्रविष्ट करा");
                    pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
                }
            }
        });
        motherSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                motherName = motherNameList.get(position);
                motherId = motherIdList.get(position);
            }
        });
        imageLastCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        textSelectedLastDate.setText(date);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.add(Calendar.MONTH, 9);
                        calendar1.add(Calendar.DAY_OF_MONTH, 7);
                        String date1 =
                                calendar1.get(Calendar.DAY_OF_MONTH)+"/"+calendar1.get(Calendar.MONTH)+"/"+calendar1.get(Calendar.YEAR);
                        textFieldExpectedDate.setText(date1);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        imageBenifitCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        textSelectedBenifitDate.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        imageRegisterCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        textSelectedDateOfRegistration.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        imageSecondCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        textSelectedSecondInstallment.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        imageThirdCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        textSelectedThirdInstallment.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        prevMotherSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                textPregnantNumber.setText(String.valueOf(position+1));
                textPregnantInfo.setText("Pregnant "+String.valueOf(position+1+" Details"));
                showProgressBar("Loading Details. Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressBar();
                        checkForPreviousPregnants();
                    }
                },1000);
            }
        });
        btnAddPreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pregnantValidation.validate()) {
                    if (saveInformation()) {
                        storeInArrayList(pregnantDetails, true);
                    }
                }
            }
        });
        btnAddPregDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage(0);
                relPregnantLayout.setVisibility(View.VISIBLE);
            }
        });
        btnDeletePregnant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMember(Integer.parseInt(textPregnantNumber.getText().toString().trim()));
            }
        });
        btnNextChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pregnantValidation.validate()) {
                    if (saveInformation()) {
                        storeInArrayList(pregnantDetails, false);
                    }
                }
            }
        });
        btnNextChildLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fa = getFragmentManager().beginTransaction();
                fa.replace(R.id.fragment_container, new FragmentCreateChildDetails());
                fa.commit();
            }
        });
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentTransaction fa = getFragmentManager().beginTransaction();
                        fa.replace(R.id.fragment_container, new FragmentCoupleDetails());
                        fa.commit();
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }
    private void deleteMember(final int number) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Alert")
                .setMessage("Do you want to delete this pregnancy entry ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String json = "";
                        if (pregnantDetailsArrayList.size() == 1) {
                            pregnantDetailsArrayList.remove(number-1);
                        } else {
                            pregnantDetailsArrayList.remove(number-1);
                            Gson gson = new Gson();
                            json = gson.toJson(pregnantDetailsArrayList);
                        }
                        final String js = json;
                        showProgressBar("Deleting pregnancy detail. Please wait...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                databaseHelper.savePregnantDetails(js);
                                stopProgressBar();
                                textPregnantNumber.setText("1");
                                textPregnantInfo.setText("Pregnant " + "1" + " Details");
                                checkForPreviousPregnants();
                                pregnantScrollView.scrollTo(0, 0);
                            }
                        }, 500);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void storeInArrayList(PregnantDetails pregnantDetails, boolean type) {
        int cnt = Integer.parseInt(textPregnantNumber.getText().toString().trim());
        if (finalFlag) {
            pregnantDetailsArrayList.remove(cnt-1);
            pregnantDetailsArrayList.add(cnt-1, pregnantDetails);
            finalFlag = false;
        } else {
            pregnantDetailsArrayList.add(cnt-1, pregnantDetails);
            finalFlag = false;
        }
        changeFragment(pregnantDetailsArrayList, "Saving pregnant "+cnt+" details. Please wait.." +
                ".", cnt, type);
    }
    private void changeFragment(final ArrayList<PregnantDetails> pregnantDetailsArrayList,
                                String content, final int cnt, boolean type) {
        Gson gson = new Gson();
        String json = gson.toJson(pregnantDetailsArrayList);
        if (type) {
            showProgressBar(content);
            databaseHelper.savePregnantDetails(json);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    if (prevFlag) {
                        int cnt = pregnantDetailsArrayList.size();
                        reloadPage(cnt);
                    } else {
                        reloadPage(cnt);
                    }
                    checkForPreviousPregnants();
                }
            }, 1000);
        } else {
            databaseHelper.savePregnantDetails(json);
            showProgressBar(content);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    FragmentTransaction fa = getFragmentManager().beginTransaction();
                    fa.replace(R.id.fragment_container, new FragmentCreateChildDetails());
                    fa.commit();
                }
            }, 1000);
        }
    }

    private void checkForPreviousPregnants() {
        if (databaseHelper.getPregnantCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PregnantDetails>>(){}.getType();
            if (databaseHelper.getPregnantDetails() == null) {
                reloadPage(0);
                prevFlag = false;
                btnAddPregDetail.setVisibility(View.VISIBLE);
                relPregnantLayout.setVisibility(View.GONE);
                prevMotherSpinner.setVisibility(View.GONE);
                textPreviousPregnant.setVisibility(View.GONE);
                btnNextChildLayout.setVisibility(View.VISIBLE);
            } else {
                if (databaseHelper.getPregnantDetails().equals("")) {
                    reloadPage(0);
                    prevFlag = false;
                    btnAddPregDetail.setVisibility(View.VISIBLE);
                    relPregnantLayout.setVisibility(View.GONE);
                    prevMotherSpinner.setVisibility(View.GONE);
                    textPreviousPregnant.setVisibility(View.GONE);
                    btnNextChildLayout.setVisibility(View.VISIBLE);
                } else {
                    Log.i("FRAGMENTPREGNANT",
                            "checkForPreviousPregnants: "+ databaseHelper.getPregnantDetails());
                    pregnantDetailsArrayList = gson.fromJson(databaseHelper.getPregnantDetails(), type);
                    prevFlag = true;
                    btnAddPregDetail.setVisibility(View.GONE);
                    relPregnantLayout.setVisibility(View.VISIBLE);
                    prevMotherSpinner.setVisibility(View.VISIBLE);
                    textPreviousPregnant.setVisibility(View.VISIBLE);
                    btnNextChildLayout.setVisibility(View.GONE);
                    populatePregnancyDetails(pregnantDetailsArrayList);
                }
            }
        } else {
            prevFlag = false;
            reloadPage(0);
            btnAddPregDetail.setVisibility(View.VISIBLE);
            relPregnantLayout.setVisibility(View.GONE);
            prevMotherSpinner.setVisibility(View.GONE);
            textPreviousPregnant.setVisibility(View.GONE);
            btnNextChildLayout.setVisibility(View.VISIBLE);
        }
    }

    private void populatePregnancyDetails(ArrayList<PregnantDetails> pregnantDetailsArrayList) {
        if (pregnantDetailsArrayList.size() > 0) {
            int cnt = Integer.parseInt(textPregnantNumber.getText().toString().trim());
            prevMotherNameList.clear();
            prevMotherIdList.clear();
            for (int i=0; i<pregnantDetailsArrayList.size(); i++) {
                for (int j=0; j<familyMemberLists.size(); j++) {
                    if (pregnantDetailsArrayList.get(i).getMotherId().equals(familyMemberLists.get(j).getMemberid())) {
                        pregnantDetailsArrayList.get(i).setName(familyMemberLists.get(j).getName());
                    }
                }
            }
            for (int i=0; i<pregnantDetailsArrayList.size(); i++) {
                prevMotherNameList.add(pregnantDetailsArrayList.get(i).getName());
                prevMotherIdList.add(pregnantDetailsArrayList.get(i).getMotherId());
            }
            prevMotherSpinner.setItems(prevMotherNameList);
            if (prevMotherNameList.size() <= cnt-1) {

            } else {
                prevMotherSpinner.setSelectedIndex(cnt-1);
            }
            if (pregnantDetailsArrayList.size() >= cnt) {
                textPregnantId.setText(pregnantDetailsArrayList.get(cnt-1).getPregnancyId());
                finalFlag = true;
                int pos = -1;
                pos = motherIdList.indexOf(prevMotherIdList.get(cnt-1));
                btnDeletePregnant.setVisibility(View.VISIBLE);
                if (pos == -1) {

                } else {
                    motherSpinner.setSelectedIndex(pos);
                    motherName = motherNameList.get(pos);
                    motherId = motherIdList.get(pos);
                }
                editPara.setText(String.valueOf(pregnantDetailsArrayList.get(cnt-1).getPara()));
                editCompilation.setText(pregnantDetailsArrayList.get(cnt-1).getComplicationPreviousPregnancy());
                textSelectedLastDate.setText(pregnantDetailsArrayList.get(cnt-1).getLastMenstrualDate());
                textFieldExpectedDate.setText(pregnantDetailsArrayList.get(cnt-1).getExpectedDateDelivery());
                editPlaceOfDelivery.setText(pregnantDetailsArrayList.get(cnt-1).getExpectedPlaceDelivery());
                if ((sessionManager.getCaste().equals("प्रवर्ग अनुसूचित जाती") || sessionManager.getCaste().equals("अनु.जमाती")) && sessionManager.getBPL()) {
                    mainJSYLayouts.setVisibility(View.VISIBLE);
                } else {
                    mainJSYLayouts.setVisibility(View.GONE);
                }
                if (pregnantDetailsArrayList.get(cnt-1).getJSY().getBenefitDate() == null) {
                    radioJsyNo.setChecked(true);
                    jsy = false;
                    fieldsOfJsy.setVisibility(View.GONE);
                } else {
                    radioJsyYes.setChecked(true);
                    jsy = true;
                    fieldsOfJsy.setVisibility(View.VISIBLE);
                    editPaidAmount.setText(pregnantDetailsArrayList.get(cnt-1).getJSY().getPaidAmount() + "");
                    textSelectedBenifitDate.setText(pregnantDetailsArrayList.get(cnt-1).getJSY().getBenefitDate());
                }
                if (para == 1) {
                    if (pregnantDetailsArrayList.get(cnt-1).getPMMVY().getRegistrationDate() == null) {
                        radioPmmvyNo.setChecked(true);
                        fieldsOfPMMVY.setVisibility(View.GONE);
                        pmmvy = false;
                    } else {
                        pmmvy = true;
                        radioPmmvyYes.setChecked(true);
                        fieldsOfPMMVY.setVisibility(View.VISIBLE);
                        textSelectedDateOfRegistration.setText(pregnantDetailsArrayList.get(cnt-1).getPMMVY().getRegistrationDate());
                        textSelectedSecondInstallment.setText(pregnantDetailsArrayList.get(cnt-1).getPMMVY().getSixmonthVisit());
                        textSelectedThirdInstallment.setText(pregnantDetailsArrayList.get(cnt-1).getPMMVY().getPenta3date());
                    }
                } else {
                    pmmvy = false;
                    mainPMMVYLayout.setVisibility(View.GONE);
                    fieldsOfPMMVY.setVisibility(View.GONE);
                }
                if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy() == null) {
                    radioDeliveredNo.setChecked(true);
                    pregnancyTypeLayout.setVisibility(View.GONE);
                    delivered = false;
                    radioPregnancyTypeNormal.setChecked(true);
                    pregnancyType = "सामान्य प्रसूती";
                    outcome = "पुरुष";
                    RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioOutcomeMale);
                    radioButton.setChecked(true);
                } else {
                    delivered = true;
                    radioDeliveredYes.setChecked(true);
                    pregnancyTypeLayout.setVisibility(View.VISIBLE);
                    if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy().equals("सामान्य प्रसूती")) {
                        radioPregnancyTypeNormal.setChecked(true);
                        pregnancyType = "सामान्य प्रसूती";
                    } else if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy().equals("सिझरियन प्रसूती")) {
                        radioPregnancyTypeLscs.setChecked(true);
                        pregnancyType = "सिझरियन प्रसूती";
                    } else if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy().equals("वेळापत्रक योजना")) {
                        radioPregnancyTypeScheduledSchezariun.setChecked(true);
                        pregnancyType = "वेळापत्रक योजना";
                    } else if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy().equals("योजनाबद्ध नसलेले")) {
                        radioPregnancyTypeUnScheduledSchezariun.setChecked(true);
                        pregnancyType = "योजनाबद्ध नसलेले";
                    } else if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy().equals("वि बी ए सी")) {
                        radioPregnancyTypeVbac.setChecked(true);
                        pregnancyType = "वि बी ए सी";
                    } else {
                        radioPregnancyTypeOther.setChecked(true);
                        pregnancyType =
                                pregnantDetailsArrayList.get(cnt-1).getDelivery().getTypeOfPregnancy();
                        editOther.setText(pregnancyType);
                    }
                    if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getOutcomeOfPregnancy().equals("पुरुष")) {
                        RadioButton radioButton =
                                (RadioButton) v.findViewById(R.id.radioOutcomeMale);
                        radioButton.setChecked(true);
                        outcome = "पुरुष";
                    } else if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getOutcomeOfPregnancy().equals("स्त्री")) {
                        RadioButton radioButton =
                                (RadioButton) v.findViewById(R.id.radioOutcomeFemale);
                        radioButton.setChecked(true);
                        outcome = "स्त्री";
                    } else if (pregnantDetailsArrayList.get(cnt-1).getDelivery().getOutcomeOfPregnancy().equals("इतर")) {
                        RadioButton radioButton =
                                (RadioButton) v.findViewById(R.id.radioOutcomeOthers);
                        radioButton.setChecked(true);
                        outcome = "इतर";
                    }
                }
            }
        }
    }

    private void reloadPage(int cnt) {
        String memDet = "Pregnant "+String.valueOf(cnt+1)+" Details";
        btnNextChildLayout.setVisibility(View.GONE);
        textPregnantNumber.setText(String.valueOf(cnt+1));
        textPregnantInfo.setText(memDet);
        btnDeletePregnant.setVisibility(View.GONE);
        pregnantScrollView.scrollTo(0,0);
        textPregnantId.setText(getOid());
        editPara.setText("");
        editPara.clearFocus();
        editCompilation.setText("");
        editCompilation.clearFocus();
        textSelectedLastDate.setText("");
        lastDate = "";
        textFieldExpectedDate.setText("");
        expectedDate = "";
        editPlaceOfDelivery.setText("");
        editPlaceOfDelivery.clearFocus();
        editPaidAmount.setText("");
        editPaidAmount.clearFocus();
        textSelectedBenifitDate.setText("");
        benifitDate = "";
        textSelectedDateOfRegistration.setText("");
        textSelectedSecondInstallment.setText("");
        textSelectedThirdInstallment.setText("");
        dor = "";
        second = "";
        third = "";
        editOther.setText("");
        editOther.clearFocus();
        radioDeliveredNo.setChecked(true);
        delivered = false;
        radioPmmvyNo.setChecked(true);
        pmmvy = false;
        radioJsyNo.setChecked(true);
        jsy = false;
        mainPMMVYLayout.setVisibility(View.GONE);
        fieldsOfPMMVY.setVisibility(View.GONE);
        fieldsOfJsy.setVisibility(View.GONE);
        if ((sessionManager.getCaste().equals("प्रवर्ग अनुसूचित जाती") || sessionManager.getCaste().equals("अनु.जमाती")) && sessionManager.getBPL()) {
            mainJSYLayouts.setVisibility(View.VISIBLE);
        } else {
            mainJSYLayouts.setVisibility(View.GONE);
        }
        motherSpinner.setSelectedIndex(0);
        motherName = motherNameList.get(0);
        motherId = motherIdList.get(0);
        populateData(familyMemberLists);
    }

    private void makeSnakbar(String content) {
        Snackbar.make(v.getRootView(), content,
                BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private boolean saveInformation() {
        int pregNumber = Integer.parseInt(textPregnantNumber.getText().toString().trim());
        String pregId = textPregnantId.getText().toString().trim();
        para = Integer.parseInt(editPara.getText().toString().trim());
        complication = editCompilation.getText().toString().trim();
        place = editPlaceOfDelivery.getText().toString().trim();
        lastDate = textSelectedLastDate.getText().toString().trim();
        pregnantDetails = new PregnantDetails(pregId, para, motherName, motherId, complication,
                lastDate, "", place, new JSY(), new PMMVY(), new Delivery());
        if (lastDate.isEmpty()) {
            makeSnakbar("कृपया शेवटची मासिक तारीख निवडा.");
            return false;
        } else {
            expectedDate = textFieldExpectedDate.getText().toString().trim();
            pregnantDetails.setLastMenstrualDate(lastDate);
            pregnantDetails.setExpectedDateDelivery(expectedDate);
        }
        if (jsy) {
            paidAmount = editPaidAmount.getText().toString().trim();
            benifitDate = textSelectedBenifitDate.getText().toString().trim();
            if (benifitDate.isEmpty()) {
                makeSnakbar("Please select benifit date.");
                return false;
            }
            pregnantDetails.setJSY(new JSY(Integer.parseInt(paidAmount), benifitDate));
        }
        if (pmmvy) {
            dor = textSelectedDateOfRegistration.getText().toString().trim();
            second = textSelectedSecondInstallment.getText().toString().trim();
            third = textSelectedThirdInstallment.getText().toString().trim();
            pregnantDetails.setPMMVY(new PMMVY(dor, second, third));
        }
        if (delivered) {
            if (pregnancyType.equals("इतर")) {
                pregnancyType = editOther.getText().toString().trim();
            }
            pregnantDetails.setDelivery(new Delivery(pregnancyType, outcome));
        }
        return true;
    }

    private void initApp() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        //Datepicker
//        datePickerDialog = new DatePickerDialog(getContext(),
//                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener,
//                year, month, day);
        //Arraylist
        familyMemberLists = new ArrayList<FamilyMemberList>();
        motherNameList = new ArrayList<String>();
        motherIdList = new ArrayList<String>();
        prevMotherNameList = new ArrayList<String>();
        prevMotherIdList = new ArrayList<String>();
        pregnantDetailsArrayList = new ArrayList<PregnantDetails>();
        //Managers
        sessionManager = new SessionManager(v.getContext());
        databaseHelper = new DatabaseHelper(v.getContext());
        progressDialog = new ProgressDialog(v.getContext());
        //nestedscrollview
        pregnantScrollView = (NestedScrollView) v.findViewById(R.id.pregnantScrollView);
        //Textview
        textSelectedLastDate = (TextView) v.findViewById(R.id.textSelectedLastDate);
        textFieldExpectedDate = (TextView) v.findViewById(R.id.textFilledExpectedDate);
        textSelectedBenifitDate = (TextView) v.findViewById(R.id.textSelectedBenifitDate);
        textSelectedDateOfRegistration = (TextView) v.findViewById(R.id.textSelectedDateofRegistration);
        textSelectedSecondInstallment = (TextView) v.findViewById(R.id.textSelectedSecondInstallment);
        textSelectedThirdInstallment = (TextView) v.findViewById(R.id.textSelectedThirdInstallment);
        textPreviousPregnant = (TextView) v.findViewById(R.id.textPreviousPregnant);
        textPregnantNumber = (TextView) v.findViewById(R.id.textPregnantNumber);
        textPregnantId = (TextView) v.findViewById(R.id.textPregnantId);
        textPregnantInfo = (TextView) v.findViewById(R.id.textPregnantInfo);
        //MaterialButton
        btnAddPreg = (MaterialButton) v.findViewById(R.id.btnAddPreg);
        btnNextChild = (MaterialButton) v.findViewById(R.id.btnNextChild);
        btnAddPregDetail = (MaterialButton) v.findViewById(R.id.btnAddPregDetail);
        btnNextChildLayout = (MaterialButton) v.findViewById(R.id.btnNextChildLayout);
        btnDeletePregnant = (MaterialButton) v.findViewById(R.id.btnDeleteMember);
        //MaterialSpinner
        motherSpinner = (MaterialSpinner) v.findViewById(R.id.motherSpinner);
        prevMotherSpinner = (MaterialSpinner) v.findViewById(R.id.prevPregnantSpinner);
        //TextInputEditText
        editPlaceOfDelivery = (TextInputEditText) v.findViewById(R.id.editDelivery);
        editCompilation = (TextInputEditText) v.findViewById(R.id.editComplication);
        editPara = (TextInputEditText) v.findViewById(R.id.editPara);
        editOther = (TextInputEditText) v.findViewById(R.id.editOther);
        editPaidAmount = (TextInputEditText) v.findViewById(R.id.editPaidAmount);
        editPara.addTextChangedListener(textWatcher);
        //RadioGroup
        radioDelivered = (RadioGroup) v.findViewById(R.id.radioDelivered);
        radioJSY = (RadioGroup) v.findViewById(R.id.radioJSY);
        radioOutcome = (RadioGroup) v.findViewById(R.id.radioOutcome);
        radioPMMVY = (RadioGroup) v.findViewById(R.id.radioPMMVY);
        radioPregnancyType = (RadioGroup) v.findViewById(R.id.radioPregnancyType);
        //Imageview
        imageLastCalendar = (ImageView) v.findViewById(R.id.imageLastCalendar);
        imageBenifitCalendar = (ImageView) v.findViewById(R.id.imageBenifitCalendar);
        imageRegisterCalendar = (ImageView) v.findViewById(R.id.imageRegisterCalendar);
        imageSecondCalendar = (ImageView) v.findViewById(R.id.imageSecondCalendar);
        imageThirdCalendar = (ImageView) v.findViewById(R.id.imageThirdCalendar);
        //jsy radio
        radioJsyYes = (RadioButton) v.findViewById(R.id.radioJSYYes);
        radioJsyNo = (RadioButton) v.findViewById(R.id.radioJSYNo);
        //pmmvy radio
        radioPmmvyYes = (RadioButton) v.findViewById(R.id.radioPMMVYYes);
        radioPmmvyNo = (RadioButton) v.findViewById(R.id.radioPMMVYNo);
        //delivery radio
        radioDeliveredYes = (RadioButton) v.findViewById(R.id.radioDeliveredYes);
        radioDeliveredNo = (RadioButton) v.findViewById(R.id.radioDeliveredNo);
        //pregnancy type radio
        radioPregnancyTypeNormal = (RadioButton) v.findViewById(R.id.radioPregnancyTypeNormal);
        radioPregnancyTypeLscs = (RadioButton) v.findViewById(R.id.radioPregnancyTypeLSCS);
        radioPregnancyTypeOther = (RadioButton) v.findViewById(R.id.radioPregnancyTypeOther);
        radioPregnancyTypeScheduledSchezariun = (RadioButton) v.findViewById(R.id.radioPregnancyTypeScheduleSchezaruin);
        radioPregnancyTypeUnScheduledSchezariun = (RadioButton) v.findViewById(R.id.radioPregnancyTypeUnScheduleSchezaruin);
        radioPregnancyTypeVbac = (RadioButton) v.findViewById(R.id.radioPregnancyTypeVbac);

        //Layouts
        mainJSYLayouts = (RelativeLayout) v.findViewById(R.id.mainJSTLayout);
        fieldsOfJsy = (RelativeLayout) v.findViewById(R.id.fieldsOfJSY);
        mainPMMVYLayout = (RelativeLayout) v.findViewById(R.id.mainPMMVYLayout);
        fieldsOfPMMVY = (RelativeLayout) v.findViewById(R.id.fieldsOfPmmvy);
        relPregnantLayout = (RelativeLayout) v.findViewById(R.id.relPregnantLayout);
        mainChildrenLayout = (RelativeLayout) v.findViewById(R.id.mainChildrenLayout);
        pregnancyTypeLayout = (RelativeLayout) v.findViewById(R.id.pregnancyTypeLayout);
        prevPregnantLayout = (RelativeLayout) v.findViewById(R.id.prevPregnancyDetailLayout);
        //Addvalidation
        pregnantValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Checking for jsy layout
        if ((sessionManager.getCaste().equals("प्रवर्ग अनुसूचित जाती") || sessionManager.getCaste().equals("अनु.जमाती")) && sessionManager.getBPL()) {
            mainJSYLayouts.setVisibility(View.VISIBLE);
        } else {
            mainJSYLayouts.setVisibility(View.GONE);
        }
        checkMembers();
    }

    private void checkMembers() {
        if (databaseHelper.getMemberCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FamilyMemberList>>(){}.getType();
            familyMemberLists = gson.fromJson(databaseHelper.getMemberDetails(), type);
            Log.i("FRAGMENTCOUPLE", "checkForPreviousData: "+familyMemberLists.size());
            populateData(familyMemberLists);
        }
    }
    private void populateData(ArrayList<FamilyMemberList> familyMemberLists) {
        if (familyMemberLists.size() > 0) {
            motherNameList.clear();
            motherIdList.clear();
            for (int i=0; i<familyMemberLists.size(); i++) {
                if (familyMemberLists.get(i).getGender().equals("स्त्री")) {
                    motherNameList.add(familyMemberLists.get(i).getName());
                    motherIdList.add(familyMemberLists.get(i).getMemberid());
                }
            }
            motherSpinner.setItems(motherNameList);
            motherName = motherNameList.get(0);
            motherId = motherIdList.get(0);
        }
    }
    private void addValidation() {
        pregnantValidation.addValidation(editPara, RegexTemplate.NOT_EMPTY, "कृपया para प्रविष्ट " +
                "करा");
        pregnantValidation.addValidation(editPlaceOfDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
    }
    private String getOid() {
        ObjectId memberId = ObjectId.get();
        return memberId.toString();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editPara.getText().toString().isEmpty()) {
                mainPMMVYLayout.setVisibility(View.GONE);
            } else {
                if (Integer.parseInt(editPara.getText().toString()) == 1) {
                    mainPMMVYLayout.setVisibility(View.VISIBLE);
                } else {
                    mainPMMVYLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void showProgressBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void stopProgressBar() {
        progressDialog.dismiss();
    }
}
