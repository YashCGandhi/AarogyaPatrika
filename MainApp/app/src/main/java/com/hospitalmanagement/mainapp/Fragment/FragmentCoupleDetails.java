package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hospitalmanagement.mainapp.GetterSetter.CoupleDetails;
import com.hospitalmanagement.mainapp.GetterSetter.FamilyMemberList;
import com.hospitalmanagement.mainapp.GetterSetter.IfNoOption;
import com.hospitalmanagement.mainapp.GetterSetter.TotalChildren;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.Sessions.DatabaseHelper;
import com.hospitalmanagement.mainapp.Sessions.SessionManager;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class FragmentCoupleDetails extends Fragment {
    View v;

    //Manager
    SessionManager sessionManager;
    DatabaseHelper databaseHelper;
    ProgressDialog progressDialog;

    //UI Components
    MaterialSpinner husbandSpinner, wifeSpinner, regHusbandSpinner, regWifeSpinner;
    RadioGroup radioMethod, radioWilling, radioChildren, radioCouple;
    TextView textWilling, textCoupleInfo, textCoupleNumber, textMaleFemale, textChildren,
            textviewWilling, textCoupleId, textPrevInfo;
    MaterialButton btnAddDate, btnAddCoup, btnNext, btnAddCouple, btnMaleFemaleNext,
            btnDeleteCouple, btnNextLayout;
    RelativeLayout relCut, relNoMethodLayout, relCoupleLayout, previousCoupleLayout,
    noMaleFemaleLayout, tpLayout;
    LinearLayout datesLayout;
    LayoutInflater layoutInflater;

    //Currently radiobutton
    RadioButton radioCurrentlyCut, radioCurrentlyNirodh, radioCurrentlyPills,
            radioCurrentlyAntara, radioCurrentlySterilization, radioCurrentlyNoMethod;
    //Willing Radio
    RadioButton radioWillingOp, radioWillingAntara, radioWillingNirodh, radioWillingCut,
            radioWillingEp, radioWillingOthers;

    LinearLayout linearLayout;
    TextInputLayout editLastAgeLayout;
    TextInputEditText editDateOfVisit, editGroupMeeting, editTotalMale, editTotalFemale,
            editLastAge, editId;
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    NestedScrollView nestedScrollView;
    EditText editText;

    //Accepted Inputs
    String husbandName, wifeName, husId, wifId, currentlyMethod="कॉपर टी", dateOfVisit="",
            groupMeeting="", willingMethod="गर्भनिरोधक गोळ्या", childGender="पुरुष";
    ArrayList<String> dates, childrenList, childList;
    int coupleId, totalMaleMember, totalFemaleMember, ageLastChild;
    boolean flag = false, prevFlag;
    int cnt = 0;
    boolean maleFlag = false, femaleFlag = false, finalFlag = false;
    boolean mFlag = false, fFlag = false;

    //Variables used in spinners
    ArrayList<String> husbandList, wifeList, husbandId, wifeId, prevHusbandId, prevHusbandName,
            prevWifeId, prevwifeName;
    //FamilyMemberArrayList
    ArrayList<FamilyMemberList> familyMemberLists;
    ArrayList<CoupleDetails> coupleDetailsArrayList;

    //Validation
    AwesomeValidation awesomeValidation;

    Calendar calendar;
    int year, month, day;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater =
                LayoutInflater.from(new ContextThemeWrapper(getContext(),
                        R.style.AppTheme));
        layoutInflater = LayoutInflater.from(new ContextThemeWrapper(getContext(),
                R.style.AppTheme));
        v = inflater.inflate(R.layout.fragment_create_couple, container,false);
        initApp();
        addValidation();
        checkForCouplelayout();
        checkForPreviousCouple();
        radioMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton1;
                int selectedId = radioMethod.getCheckedRadioButtonId();
                radioButton1 = (RadioButton) v.findViewById(selectedId);
                currentlyMethod = radioButton1.getText().toString().trim();
                if (currentlyMethod.equals("कॉपर टी") || currentlyMethod.equals("अंतरा") || currentlyMethod.equals("गर्भनिरोधक गोळ्या")) {
                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.antara);
                    tpLayout.setLayoutParams(layoutParams);
                    relCut.setVisibility(View.VISIBLE);
                    relNoMethodLayout.setVisibility(View.GONE);
                    radioWilling.setVisibility(View.GONE);
                    textviewWilling.setVisibility(View.GONE);
                } else if (currentlyMethod.equals("काहीही नाही")) {
                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.noMethod);
                    tpLayout.setLayoutParams(layoutParams);
                    relCut.setVisibility(View.GONE);
                    relNoMethodLayout.setVisibility(View.VISIBLE);
                    radioWilling.setVisibility(View.VISIBLE);
                    textviewWilling.setVisibility(View.VISIBLE);
                } else {
                    hideAllLayout();
                }
            }
        });
        editGroupMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        editGroupMeeting.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        editDateOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth+"/"+(month+1)+"/"+year;
                        editDateOfVisit.setText(date);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });
        radioChildren.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                Log.i("FRAGMENTCOUPLE", "onCheckedChanged: "+radioButton.getText().toString().trim());
                childGender = radioButton.getText().toString().trim();
            }
        });
        husbandSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                husbandName = String.valueOf(item);
                husId = husbandId.get(position);
            }
        });
        wifeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                wifeName = String.valueOf(item);
                wifId = wifeId.get(position);
            }
        });
        radioWilling.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                willingMethod = radioButton.getText().toString();
            }
        });
        btnAddCouple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    saveInformation(true);
                }
            }
        });
        btnAddCoup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relCoupleLayout.setVisibility(View.VISIBLE);
                reloadPage(0);
            }
        });
        regHusbandSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                textCoupleNumber.setText(String.valueOf(position+1));
                textCoupleInfo.setText("Couple "+String.valueOf(position+1)+" Details");
                regWifeSpinner.setSelectedIndex(position);
                showProgressBar("Loading Details. Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressBar();
                        checkForPreviousCouple();
                    }
                },1000);
            }
        });
        regWifeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                textCoupleNumber.setText(String.valueOf(position+1));
                textCoupleInfo.setText("Couple "+String.valueOf(position+1)+" Details");
                regHusbandSpinner.setSelectedIndex(position);
                showProgressBar("Loading Details. Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressBar();
                        checkForPreviousCouple();
                    }
                },1000);
            }
        });
        btnDeleteCouple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMember(Integer.parseInt(textCoupleNumber.getText().toString().trim()));
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    saveInformation(false);

                }
            }
        });
        btnMaleFemaleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maleFlag) {
                    changeFragmentWhenNoMember(new FragmentCreatePregnantDetails());
                }
                if (femaleFlag) {
                    changeFragmentWhenNoMember(new FragmentCreateChildDetails());
                }
            }
        });
        btnAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, datesLayout, true).findViewById(R.id.editVaccinationDate).setId(cnt);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) datesLayout.findViewById(cnt));

                ((TextInputEditText) datesLayout.findViewById(cnt   )).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) datesLayout.findViewById(cnt)).setHint("Enter date "+String.valueOf(cnt+1));
                cnt++;
            }
        });
        btnNextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fa = getFragmentManager().beginTransaction();
                fa.replace(R.id.fragment_container, new FragmentCreatePregnantDetails());
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
                        fa.replace(R.id.fragment_container, new FragmentMemberDetails());
                        fa.commit();
                        return true;
                    }
                }
                return false;
            }
        });
        return v;
    }

    private void changeFragmentWhenNoMember(Fragment fragment) {
        FragmentTransaction fa = getFragmentManager().beginTransaction();
        fa.replace(R.id.fragment_container, fragment);
        fa.commit();
    }

    private void deleteMember(final int number) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Alert")
                .setMessage("Do you want to delete this couple entry ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String json = "";
                        if (coupleDetailsArrayList.size() == 1) {
                            coupleDetailsArrayList.remove(number-1);
                        } else {
                            coupleDetailsArrayList.remove(number-1);
                            Gson gson = new Gson();
                            json = gson.toJson(coupleDetailsArrayList);
                        }
                        final String js = json;
                        showProgressBar("Deleting member. Please wait...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                databaseHelper.saveCoupleDetails(js);
                                stopProgressBar();
                                textCoupleNumber.setText("1");
                                textCoupleInfo.setText("Couple " + "1" + " Details");
                                checkForPreviousCouple();
                                nestedScrollView.scrollTo(0, 0);
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

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editTotalMale.getText().toString().trim().equals("") == false) {
                if (Integer.parseInt(editTotalMale.getText().toString().trim()) > 0) {
                    mFlag = true;
                    System.out.println(mFlag);
                    editLastAgeLayout.setVisibility(View.VISIBLE);
                    textChildren.setVisibility(View.VISIBLE);
                    radioChildren.setVisibility(View.VISIBLE);
                    awesomeValidation.clear();
                    awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "couple id प्रविष्ट करा");
                    awesomeValidation.addValidation(editTotalMale, RegexTemplate.NOT_EMPTY,
                            "कृपया एकूण पुरुष मुले प्रविष्ट करा");
                    awesomeValidation.addValidation(editTotalFemale, RegexTemplate.NOT_EMPTY,
                            "कृपया एकूण महिला मुले प्रविष्ट करा");
                    awesomeValidation.addValidation(editLastAge, RegexTemplate.NOT_EMPTY, "कृपया शेवटच्या मुलाचे वय प्रविष्ट करा");
                } else {
                    mFlag = false;
                    System.out.println(fFlag + "" + mFlag);
                    if (fFlag) {

                    } else {
                        editLastAgeLayout.setVisibility(View.GONE);
                        textChildren.setVisibility(View.GONE);
                        radioChildren.setVisibility(View.GONE);
                        awesomeValidation.clear();
                        awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                "couple id प्रविष्ट करा");
                    }
                }
            } else {
                mFlag = false;
                System.out.println(fFlag + "" + mFlag);
                if (fFlag) {

                } else {
                    editLastAgeLayout.setVisibility(View.GONE);
                    textChildren.setVisibility(View.GONE);
                    radioChildren.setVisibility(View.GONE);
                    awesomeValidation.clear();
                    awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "couple id प्रविष्ट करा");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editTotalFemale.getText().toString().trim().equals("") == false) {
                if (Integer.parseInt(editTotalFemale.getText().toString().trim()) > 0) {
                    System.out.println(fFlag);
                    fFlag = true;
                    editLastAgeLayout.setVisibility(View.VISIBLE);
                    textChildren.setVisibility(View.VISIBLE);
                    radioChildren.setVisibility(View.VISIBLE);
                    awesomeValidation.clear();
                    awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "couple id प्रविष्ट करा");
                    awesomeValidation.addValidation(editTotalMale, RegexTemplate.NOT_EMPTY,
                            "कृपया एकूण पुरुष मुले प्रविष्ट करा");
                    awesomeValidation.addValidation(editTotalFemale, RegexTemplate.NOT_EMPTY,
                            "कृपया एकूण महिला मुले प्रविष्ट करा");
                    awesomeValidation.addValidation(editLastAge, RegexTemplate.NOT_EMPTY, "कृपया शेवटच्या मुलाचे वय प्रविष्ट करा");
                } else {
                    fFlag = false;
                    System.out.println(fFlag + "" + mFlag);
                    if (mFlag) {

                    } else {
                        editLastAgeLayout.setVisibility(View.GONE);
                        textChildren.setVisibility(View.GONE);
                        radioChildren.setVisibility(View.GONE);
                        awesomeValidation.clear();
                        awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                                "couple id प्रविष्ट करा");
                    }
                }
            } else {
                fFlag = false;
                System.out.println(fFlag + "" + mFlag);
                if (mFlag) {

                } else {
                    editLastAgeLayout.setVisibility(View.GONE);
                    textChildren.setVisibility(View.GONE);
                    radioChildren.setVisibility(View.GONE);
                    awesomeValidation.clear();
                    awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                            "couple id प्रविष्ट करा");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void saveInformation(boolean type) {
        boolean cntFlag = false;
        int methodCnt = 0;
        int cpId;
        String cid = textCoupleId.getText().toString().trim();
        cpId = Integer.parseInt(editId.getText().toString().trim());
        if (!editTotalMale.getText().toString().trim().equals("") || !editTotalFemale.getText().toString().trim().equals("")) {
            cntFlag = true;
            if (editTotalMale.getText().toString().trim().equals("")) {
                totalMaleMember = 0;
            } else {
                totalMaleMember = Integer.parseInt(editTotalMale.getText().toString().trim());
            }
            if (editTotalFemale.getText().toString().trim().equals("")) {
                totalFemaleMember = 0;
            } else {
                totalFemaleMember = Integer.parseInt(editTotalFemale.getText().toString().trim());
            }
            ageLastChild = Integer.parseInt(editLastAge.getText().toString().trim());
            RadioButton rbss =
                    (RadioButton) v.findViewById(radioChildren.getCheckedRadioButtonId());
            childGender = rbss.getText().toString().trim();
        }
        if (currentlyMethod.equals("कॉपर टी") || currentlyMethod.equals("अंतरा") || currentlyMethod.equals("गर्भनिरोधक गोळ्या")) {
            dates = new ArrayList<String>();
            dates.clear();
            if (cnt != 0) {
                for (int i=0; i<cnt; i++) {
                    TextInputEditText editText = (TextInputEditText) datesLayout.findViewById(i);
                    if (editText.getText().toString().trim().length() != 0) {
                        dates.add(editText.getText().toString().trim());
                    }
                }
            }
        } else if (currentlyMethod.equals("काहीही नाही")) {
            methodCnt = 1;
            dateOfVisit = editDateOfVisit.getText().toString().trim();
            groupMeeting = editGroupMeeting.getText().toString().trim();
        } else {
            methodCnt = 2;
        }
        CoupleDetails coupleDetails;
        if (cntFlag) {
            if (methodCnt == 0) {
                coupleDetails = new CoupleDetails(cid, husbandName, husId, wifeName, wifId, cpId, new TotalChildren(totalMaleMember, totalFemaleMember,
                        ageLastChild, childGender), currentlyMethod, dates);
                storeInArrayList(coupleDetails, type);
            } else if (methodCnt == 1) {
                coupleDetails = new CoupleDetails(cid, husbandName, husId, wifeName, wifId,
                        cpId, new TotalChildren(totalMaleMember, totalFemaleMember,
                        ageLastChild, childGender), currentlyMethod, new IfNoOption(willingMethod
                        , dateOfVisit, groupMeeting));
                storeInArrayList(coupleDetails, type);
            } else if (methodCnt == 2) {
                coupleDetails = new CoupleDetails(cid, husbandName, husId, wifeName, wifId,
                        cpId, new TotalChildren(totalMaleMember, totalFemaleMember,
                        ageLastChild, childGender), currentlyMethod);
                storeInArrayList(coupleDetails, type);
            }
        } else {
            if (methodCnt == 0) {
                coupleDetails = new CoupleDetails(cid, husbandName, husId, wifeName, wifId,
                        cpId, currentlyMethod, dates);
                storeInArrayList(coupleDetails, type);
            } else if (methodCnt == 1) {
                coupleDetails = new CoupleDetails(cid, husbandName, husId, wifeName, wifId,
                        cpId, currentlyMethod, new IfNoOption(willingMethod
                        , dateOfVisit, groupMeeting));
                storeInArrayList(coupleDetails, type);
            } else if (methodCnt == 2) {
                coupleDetails = new CoupleDetails(cid, husbandName, husId, wifeName, wifId,
                        cpId, currentlyMethod);
                storeInArrayList(coupleDetails, type);
            }
        }
    }

    private void storeInArrayList(CoupleDetails coupleDetails, boolean type) {
        int memCnt = Integer.parseInt(textCoupleNumber.getText().toString().trim());
        if (finalFlag) {
            coupleDetailsArrayList.remove(memCnt-1);
            coupleDetailsArrayList.add(memCnt-1, coupleDetails);
            finalFlag = false;
        } else {
            coupleDetailsArrayList.add(memCnt-1, coupleDetails);
            finalFlag = false;
        }
        changeFragment(coupleDetailsArrayList, "Saving couple "+memCnt+" details. Please wait..."
                , memCnt, type);
    }

    private void changeFragment(final ArrayList<CoupleDetails> coupleDetails, String content,
                                final int memCnt, boolean type) {
        Gson gson = new Gson();
        String json = gson.toJson(coupleDetails);
        if (type) {
            showProgressBar(content);
            databaseHelper.saveCoupleDetails(json);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    if (prevFlag) {
                        int counts = coupleDetailsArrayList.size();
                        reloadPage(counts);
                    } else {
                        reloadPage(memCnt);
                    }
                    populateCoupleDetails(coupleDetailsArrayList);
                }
            }, 1000);
        } else {
            databaseHelper.saveCoupleDetails(json);
            showProgressBar(content);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    FragmentTransaction fa = getFragmentManager().beginTransaction();
                    fa.replace(R.id.fragment_container, new FragmentCreatePregnantDetails());
                    fa.commit();
                }
            }, 1000);
        }
    }

    private void reloadPage(int memCnt) {
        Log.i("FRAGMENTCOUPLE", "reloadPage: "+memCnt);
        String memDet = "Couple " + String.valueOf(memCnt+1) + " Details";
        textCoupleNumber.setText(String.valueOf(memCnt+1));
        textCoupleInfo.setText(memDet);
        nestedScrollView.scrollTo(0,0);
        textCoupleId.setText(getOid());
        datesLayout.removeAllViews();
//        editText.setText("");
//        editText.clearFocus();
        cnt = 0;
        btnDeleteCouple.setVisibility(View.GONE);
        editDateOfVisit.setText("");
        editDateOfVisit.clearFocus();
        editGroupMeeting.setText("");
        editGroupMeeting.clearFocus();
        editId.setText("");
        editId.clearFocus();
        editTotalMale.setText("");
        editTotalFemale.setText("");
        editTotalMale.clearFocus();
        editTotalFemale.clearFocus();
        editLastAge.setText("");
        editLastAge.clearFocus();
        //CURRENTLY
        radioCurrentlyCut.setChecked(true);
        radioCurrentlySterilization.setChecked(false);
        radioCurrentlyPills.setChecked(false);
        radioCurrentlyNoMethod.setChecked(false);
        radioCurrentlyNirodh.setChecked(false);
        radioCurrentlyAntara.setChecked(false);
        radioWillingOthers.setChecked(false);
        //WILLING
        radioWillingOp.setChecked(true);
        radioWillingNirodh.setChecked(false);
        radioWillingEp.setChecked(false);
        radioWillingCut.setChecked(false);
        radioWillingAntara.setChecked(false);
        RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioChildrenMale);
        radioButton.setChecked(true);
        currentlyMethod = "कॉपर टी";
        willingMethod = "गर्भनिरोधक गोळ्या";
        childGender = "पुरुष";
        finalFlag = false;
        populateData(familyMemberLists);

    }

    private void initApp() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        //Session Manager
        sessionManager = new SessionManager(v.getContext());
        databaseHelper = new DatabaseHelper(v.getContext());
        progressDialog = new ProgressDialog(v.getContext());

        //Arraylists
        familyMemberLists = new ArrayList<FamilyMemberList>();
        childrenList = new ArrayList<String>();
        coupleDetailsArrayList = new ArrayList<CoupleDetails>();
        husbandList = new ArrayList<String>();
        wifeList = new ArrayList<String>();
        husbandId = new ArrayList<String>();
        wifeId = new ArrayList<String>();
        prevHusbandName = new ArrayList<String>();
        prevHusbandId = new ArrayList<String>();
        prevwifeName = new ArrayList<String>();
        prevWifeId = new ArrayList<String>();
        datesLayout = (LinearLayout) v.findViewById(R.id.datesLayout);

        //MaterialSpinner husband/wife
        husbandSpinner = (MaterialSpinner) v.findViewById(R.id.husbandSpinner);
        wifeSpinner = (MaterialSpinner) v.findViewById(R.id.wifeSpinner);
        regHusbandSpinner = (MaterialSpinner) v.findViewById(R.id.regHusbandSpinner);
        regWifeSpinner = (MaterialSpinner) v.findViewById(R.id.regWifeSpinner);
        regHusbandSpinner.setVisibility(View.GONE);
        regWifeSpinner.setVisibility(View.GONE);
//        coupleSpinner = (MaterialSpinner) v.findViewById(R.id.coupleSpinner);
        //Radio Buttons
        radioMethod = (RadioGroup) v.findViewById(R.id.radioMethod);
        radioWilling = (RadioGroup) v.findViewById(R.id.radioWilling);
        radioChildren = (RadioGroup) v.findViewById(R.id.radioChildren);
//        radioCouple = (RadioGroup) v.findViewById(R.id.radioCouple);
        //Textview
        textWilling = (TextView) v.findViewById(R.id.textWilling);
        textCoupleInfo = (TextView) v.findViewById(R.id.textCoupleInfo);
        textCoupleNumber = (TextView) v.findViewById(R.id.textCoupleNumber);
        textMaleFemale = (TextView) v.findViewById(R.id.textMaleFemale);
        textChildren = (TextView) v.findViewById(R.id.textChildren);
        textviewWilling = (TextView) v.findViewById(R.id.textWilling);
        textCoupleId = (TextView) v.findViewById(R.id.textCoupleId);
        textCoupleId.setText(getOid());
        textPrevInfo = (TextView) v.findViewById(R.id.textPreviousCouple);
        textPrevInfo.setVisibility(View.GONE);
        //Material Button
        btnAddDate = (MaterialButton) v.findViewById(R.id.btnAddDate);
        btnAddCoup = (MaterialButton) v.findViewById(R.id.btnAddCoup);
        btnNext = (MaterialButton) v.findViewById(R.id.btnNext);
        btnAddCouple = (MaterialButton) v.findViewById(R.id.btnAddCouple);
        btnMaleFemaleNext = (MaterialButton) v.findViewById(R.id.btnMaleFemaleNext);
        btnDeleteCouple = (MaterialButton) v.findViewById(R.id.btnDeleteMember);
        btnDeleteCouple.setVisibility(View.GONE);
        btnNextLayout = (MaterialButton) v.findViewById(R.id.btnNextLayout);
        //RadioCurrently
        radioCurrentlyCut = (RadioButton) v.findViewById(R.id.radioMethodCut);
        radioCurrentlyAntara = (RadioButton) v.findViewById(R.id.radioMethodAntara);
        radioCurrentlyNirodh = (RadioButton) v.findViewById(R.id.radioMethodNirodh);
        radioCurrentlyNoMethod = (RadioButton) v.findViewById(R.id.radioMethodNoMethod);
        radioCurrentlyPills = (RadioButton) v.findViewById(R.id.radioMethodOralPills);
        radioCurrentlySterilization = (RadioButton) v.findViewById(R.id.radioMethodSterilization);
        //Radiowilling
        radioWillingAntara = (RadioButton) v.findViewById(R.id.radioWillingAntara);
        radioWillingCut = (RadioButton) v.findViewById(R.id.radioWillingCut);
        radioWillingEp = (RadioButton) v.findViewById(R.id.radioWillingEp);
        radioWillingNirodh = (RadioButton) v.findViewById(R.id.radioWillingNirodh);
        radioWillingOp = (RadioButton) v.findViewById(R.id.radioWillingOp);
        radioWillingOthers = (RadioButton) v.findViewById(R.id.radioWillingOthers);
        //Layouts
        relCut = (RelativeLayout) v.findViewById(R.id.antara);
        relNoMethodLayout = (RelativeLayout) v.findViewById(R.id.noMethod);
        linearLayout = (LinearLayout) v.findViewById(R.id.datesLayout);
        relCoupleLayout = (RelativeLayout) v.findViewById(R.id.relCoupleLayout);
        relCoupleLayout.setVisibility(View.GONE);
        noMaleFemaleLayout = (RelativeLayout) v.findViewById(R.id.noFemaleMaleLayout);
        previousCoupleLayout = (RelativeLayout) v.findViewById(R.id.previousCoupleLayout);
        tpLayout = (RelativeLayout) v.findViewById(R.id.tpLayout);
        //Edittext
        editDateOfVisit = (TextInputEditText) v.findViewById(R.id.editVisit);
        editGroupMeeting = (TextInputEditText) v.findViewById(R.id.editMeeting);

        editTotalMale = (TextInputEditText) v.findViewById(R.id.editMaleMember);
        editTotalMale.addTextChangedListener(textWatcher);
        editTotalFemale = (TextInputEditText) v.findViewById(R.id.editFemaleMember);
        editTotalFemale.addTextChangedListener(textWatcher1);
        editLastAge = (TextInputEditText) v.findViewById(R.id.editLastChild);
//        editText = (EditText) v.findViewById(R.id.editStart1);
//        editLastAge.addTextChangedListener(textWatcher);
        editLastAgeLayout = (TextInputLayout) v.findViewById(R.id.editLastChildLayout);
        editLastAgeLayout.setVisibility(View.GONE);
        editId = (TextInputEditText) v.findViewById(R.id.editCoupleId);
        //NestedScrollview
        nestedScrollView = (NestedScrollView) v.findViewById(R.id.coupleScrollview);
        //Validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        cnt = 0;
//        showNirodhLayout();
        checkForPreviousData();
        showCutLayout();
    }

//    private void storeInArrayList(CoupleDetails coupleDetails, boolean type) {
//        int memCount = Integer.parseInt(textCoupleNumber.getText().toString().trim());
//        if (finalFlag) {
//            coupleDetailsArrayList.remove(memCount-1);
//            coupleDetailsArrayList.add(memCount-1, coupleDetails);
//            finalFlag = false;
//        } else {
//            coupleDetailsArrayList.add(memCount-1, coupleDetails);
//            finalFlag = false;
//        }
//    }

    private void addValidation() {
        awesomeValidation.addValidation(editId, RegexTemplate.NOT_EMPTY, "कृपया " +
                "couple id प्रविष्ट करा");
    }

    private void checkForPreviousData() {
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
            maleFlag = false;
            femaleFlag = false;
            childList = new ArrayList<String>();
            husbandList.clear();
            husbandId.clear();
            wifeList.clear();
            wifeId.clear();
            for (int i=0; i<familyMemberLists.size(); i++) {
                childList.add(familyMemberLists.get(i).getName());
                if(familyMemberLists.get(i).getGender().equals("पुरुष")) {
                    husbandId.add(familyMemberLists.get(i).getMemberid());
                    husbandList.add(familyMemberLists.get(i).getName());
                } else if (familyMemberLists.get(i).getGender().equals("स्त्री")) {
                    wifeId.add(familyMemberLists.get(i).getMemberid());
                    wifeList.add(familyMemberLists.get(i).getName());
                }
            }
            for (int i=0; i<wifeList.size(); i++) {
                Log.i("FRAGMENTCOUPLE", "populateData: "+wifeList.get(i));
            }
            if (husbandList.size() == 0) {
                noMaleFemaleLayout.setVisibility(View.VISIBLE);
                textMaleFemale.setText("कुटुंबात पुरुष सदस्य नाहीत. म्हणून आपण जोडपे जोडू शकत नाही. कृपया गरोदरपणाचा तपशील जोडण्यासाठी पुढच्या बटणावर क्लिक करा");
                maleFlag = true;
                femaleFlag = false;
                husbandName = "";
            } else  {
                noMaleFemaleLayout.setVisibility(View.GONE);
                husbandSpinner.setItems(husbandList);
                husbandName = husbandList.get(0);
                husId = husbandId.get(0);
            }
            if (wifeList.size() == 0) {
                noMaleFemaleLayout.setVisibility(View.VISIBLE);
                textMaleFemale.setText("कुटुंबात कोणतीही महिला सदस्य नाहीत. म्हणून आपण जोडपे जोडू शकत नाही. कृपया मुलांचा तपशील जोडण्यासाठी पुढच्या बटणावर क्लिक करा");
                maleFlag = false;
                femaleFlag = true;
                wifeName = "";
            } else  {
                noMaleFemaleLayout.setVisibility(View.GONE);
                wifeSpinner.setItems(wifeList);
                wifeName = wifeList.get(0);
                wifId = wifeId.get(0);
            }
        } else {

        }
    }

    private void checkForPreviousCouple() {
        if (databaseHelper.getCoupleCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CoupleDetails>>(){}.getType();
            if (databaseHelper.getCoupleDetails() == null) {
                Log.i("FRAGMENTCOUPLE", "checkForPreviousCouple: no i am here...");
                reloadPage(0);
                prevFlag = false;
                btnAddCoup.setVisibility(View.VISIBLE);
                btnNextLayout.setVisibility(View.VISIBLE);
                relCoupleLayout.setVisibility(View.GONE);
            } else {
                if (databaseHelper.getCoupleDetails().equals("")) {
                    reloadPage(0);
                    prevFlag = false;
                    regHusbandSpinner.setVisibility(View.GONE);
                    regWifeSpinner.setVisibility(View.GONE);
                    btnAddCoup.setVisibility(View.VISIBLE);
                    btnNextLayout.setVisibility(View.VISIBLE);
                    relCoupleLayout.setVisibility(View.GONE);
                } else {
                    Log.i("FRAGMENTCOUPLE", "checkForPreviousCouple: "+databaseHelper.getCoupleDetails());
                    coupleDetailsArrayList =
                            gson.fromJson(databaseHelper.getCoupleDetails(), type);
                    prevFlag = true;
                    populateCoupleDetails(coupleDetailsArrayList);
                }
            }
        } else {
            prevFlag = false;
            reloadPage(0);
            finalFlag = false;
            btnAddCoup.setVisibility(View.VISIBLE);
            btnNextLayout.setVisibility(View.VISIBLE);
            relCoupleLayout.setVisibility(View.GONE);
        }
    }

    private void showCutLayout() {
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.antara);
        tpLayout.setLayoutParams(layoutParams);
        relCut.setVisibility(View.VISIBLE);
        relNoMethodLayout.setVisibility(View.GONE);
        radioWilling.setVisibility(View.GONE);
        textviewWilling.setVisibility(View.GONE);
    }
    private void showNoMethodLayout() {
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.noMethod);
        tpLayout.setLayoutParams(layoutParams);
        relCut.setVisibility(View.GONE);
        relNoMethodLayout.setVisibility(View.VISIBLE);
        radioWilling.setVisibility(View.VISIBLE);
        textviewWilling.setVisibility(View.VISIBLE);
    }
    private void hideAllLayout() {
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.antara);
        tpLayout.setLayoutParams(layoutParams);
        relCut.setVisibility(View.GONE);
        relNoMethodLayout.setVisibility(View.GONE);
        radioWilling.setVisibility(View.GONE);
        textviewWilling.setVisibility(View.GONE);
    }

    private void populateCoupleDetails (ArrayList<CoupleDetails> coupleDetailsArrayList) {
        if (coupleDetailsArrayList.size() > 0) {
            int count = Integer.parseInt(textCoupleNumber.getText().toString().trim());
            prevHusbandName.clear();
            prevHusbandId.clear();
            prevwifeName.clear();
            prevWifeId.clear();
            for (int i=0; i<coupleDetailsArrayList.size(); i++) {
                for (int j=0; j<familyMemberLists.size(); j++) {
                    if (coupleDetailsArrayList.get(i).getHusbandId().equals(familyMemberLists.get(j).getMemberid())) {
                        coupleDetailsArrayList.get(i).setHusband(familyMemberLists.get(j).getName());
                    }
                    if (coupleDetailsArrayList.get(i).getWifeId().equals(familyMemberLists.get(j).getMemberid())) {
                        coupleDetailsArrayList.get(i).setWife(familyMemberLists.get(j).getName());
                    }
                }
            }
            for (int i=0; i<coupleDetailsArrayList.size(); i++) {
                prevHusbandName.add(coupleDetailsArrayList.get(i).getHusband());
                prevHusbandId.add(coupleDetailsArrayList.get(i).getHusbandId());
                prevwifeName.add(coupleDetailsArrayList.get(i).getWife());
                prevWifeId.add(coupleDetailsArrayList.get(i).getWifeId());
            }
            regHusbandSpinner.setItems(prevHusbandName);
            regWifeSpinner.setItems(prevwifeName);
            regHusbandSpinner.setVisibility(View.VISIBLE);
            regWifeSpinner.setVisibility(View.VISIBLE);
            btnAddCoup.setVisibility(View.GONE);
            btnNextLayout.setVisibility(View.GONE);
            if (prevHusbandName.size() <= count-1) {

            } else {
                regHusbandSpinner.setSelectedIndex(count-1);
                regWifeSpinner.setSelectedIndex(count-1);
            }
            if (coupleDetailsArrayList.size() >= count) {
                textCoupleId.setText(coupleDetailsArrayList.get(count-1).getEligibleCoupleNameId());
                finalFlag = true;
                btnDeleteCouple.setVisibility(View.VISIBLE);
                int pos = -1;
                pos = husbandId.indexOf(prevHusbandId.get(count-1));
                if (pos == -1) {

                } else {
                    husbandSpinner.setSelectedIndex(pos);
                    husbandName = husbandList.get(pos);
                    husId = husbandId.get(pos);
                }
                pos = -1;
                pos = wifeId.indexOf(prevWifeId.get(count-1));
                if (pos == -1) {

                } else {
                    wifeSpinner.setSelectedIndex(pos);
                    wifeName = wifeList.get(pos);
                    wifId = wifeId.get(pos);
                }
                if (coupleDetailsArrayList.get(count-1).getFamilyPlanningMethod().equals("कॉपर टी")) {
//                cnt = 1;
                    radioCurrentlyCut.setChecked(true);
                    currentlyMethod = "कॉपर टी";
                    showCutLayout();
                    doThisToCut(count);
                } else if (coupleDetailsArrayList.get(count-1).getFamilyPlanningMethod().equals("अंतरा")) {
                    radioCurrentlyAntara.setChecked(true);
                    currentlyMethod = "अंतरा";
                    showCutLayout();
                    doThisToCut(count);
                } else if (coupleDetailsArrayList.get(count-1).getFamilyPlanningMethod().equals("गर्भनिरोधक गोळ्या")) {
                    radioCurrentlyPills.setChecked(true);
                    currentlyMethod = "गर्भनिरोधक गोळ्या";
                    showCutLayout();
                    doThisToCut(count);
                } else if (coupleDetailsArrayList.get(count-1).getFamilyPlanningMethod().equals("काहीही नाही")) {
                    radioCurrentlyNoMethod.setChecked(true);
                    currentlyMethod = "काहीही नाही";
                    editDateOfVisit.setText(coupleDetailsArrayList.get(count-1).getIfNoOption().getDateOfVisit());
                    editGroupMeeting.setText(coupleDetailsArrayList.get(count-1).getIfNoOption().getDateOfGroupMeeting());
                    if (coupleDetailsArrayList.get(count-1).getIfNoOption().getFutureMethod().equals(
                            "गर्भनिरोधक गोळ्या")) {
                        radioWillingOp.setChecked(true);
                        willingMethod = "गर्भनिरोधक गोळ्या";
                    } else if (coupleDetailsArrayList.get(count-1).getIfNoOption().getFutureMethod().equals("अंतरा")) {
//                        radioWilling.clearCheck();
                        radioWillingAntara.setChecked(true);
                        willingMethod = "अंतरा";
                    } else if (coupleDetailsArrayList.get(count-1).getIfNoOption().getFutureMethod().equals("निरोध")) {
//                        radioWilling.clearCheck();
                        radioWillingNirodh.setChecked(true);
                        willingMethod = "निरोध";
                    } else if (coupleDetailsArrayList.get(count-1).getIfNoOption().getFutureMethod().equals("कॉपर टी")) {
//                        radioWilling.clearCheck();
                        radioWillingCut.setChecked(true);
                        willingMethod = "कॉपर टी";
                    } else if (coupleDetailsArrayList.get(count-1).getIfNoOption().getFutureMethod().equals("नसबंदी")) {
//                        radioWilling.clearCheck();
                        radioWillingEp.setChecked(true);
                        willingMethod = "नसबंदी";
                    } else if (coupleDetailsArrayList.get(count-1).getIfNoOption().getFutureMethod().equals("काहीही नाही")) {
//                        radioWilling.clearCheck();
                        radioWillingOthers.setChecked(true);
                        willingMethod = "काहीही नाही";
                    }
                    showNoMethodLayout();
                } else if (coupleDetailsArrayList.get(count-1).getFamilyPlanningMethod().equals(
                        "नसबंदी")) {
                    radioCurrentlySterilization.setChecked(true);
                    currentlyMethod = "नसबंदी";
                    hideAllLayout();
                } else if (coupleDetailsArrayList.get(count-1).getFamilyPlanningMethod().equals("निरोध")) {
//                    radioMethod.clearCheck();
                    radioCurrentlyNirodh.setChecked(true);
                    currentlyMethod = "निरोध";
                    hideAllLayout();
                }

                if (coupleDetailsArrayList.get(count-1).getTotalChildren() == null) {

                } else {
                    RadioButton radioButton;
                    editTotalMale.setText(String.valueOf(coupleDetailsArrayList.get(count-1).getTotalChildren().getMale()));
                    editTotalFemale.setText(String.valueOf(coupleDetailsArrayList.get(count-1).getTotalChildren().getFemale()));
                    editLastAge.setText(String.valueOf(coupleDetailsArrayList.get(count-1).getTotalChildren().getAgeLastChild()));
                    if (coupleDetailsArrayList.get(count-1).getTotalChildren().getGenderLastChild().equals("पुरुष")) {
                        radioButton = (RadioButton) v.findViewById(R.id.radioChildrenMale);
                        childGender = "पुरुष";
                    } else if (coupleDetailsArrayList.get(count-1).getTotalChildren().getGenderLastChild().equals("स्त्री")) {
                        radioButton = (RadioButton) v.findViewById(R.id.radioChildrenFemale);
                        childGender = "स्त्री";
                    } else {
                        radioButton = (RadioButton) v.findViewById(R.id.radioChildrenOthers);
                        childGender = "इतर";
                    }
                    radioButton.setChecked(true);
                }
                editId.setText(String.valueOf(coupleDetailsArrayList.get(count-1).getCoupleid()));
            } else {
                finalFlag = false;
            }
        } else {
            regHusbandSpinner.setItems();
            regHusbandSpinner.setVisibility(View.GONE);
            regWifeSpinner.setVisibility(View.GONE);
            btnNextLayout.setVisibility(View.VISIBLE);
            finalFlag = false;
        }
    }

    private void doThisToCut(int count) {
        datesLayout.removeAllViews();
        if (coupleDetailsArrayList.get(count-1).getFpmdates().size() > 0) {
            ArrayList<String> date = coupleDetailsArrayList.get(count-1).getFpmdates();
            cnt = 0;
            for (int i=0; i<date.size(); i++) {
                layoutInflater.inflate(R.layout.familydetail_vaccinationdate, datesLayout, true).findViewById(R.id.editVaccinationDate).setId(cnt);
                final TextInputEditText textInputEditText =
                        ((TextInputEditText) datesLayout.findViewById(cnt));

                ((TextInputEditText) datesLayout.findViewById(cnt)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = dayOfMonth+"/"+(month+1)+"/"+year;
                                textInputEditText.setText(date);
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                                R.style.Theme_MaterialComponents_Light_Dialog_MinWidth,
                                onDateSetListener, year, month, day);
                        datePickerDialog.getWindow();
                        datePickerDialog.show();
                    }
                });
                ((TextInputEditText) datesLayout.findViewById(cnt)).setHint("Enter date "+String.valueOf(cnt-1));
                ((TextInputEditText) datesLayout.findViewById(cnt)).setText(coupleDetailsArrayList.get(count-1).getFpmdates().get(i));
                cnt++;
            }
        }
//        for (int i=0; i<allEds.size(); i++){
//            linearLayout.removeView(allEds.get(i));
//            allEds.remove(i);
//        }
//        cnt = 0;
//        if (coupleDetailsArrayList.get(count-1).getFpmdates().size() > 0) {
//            ArrayList<String> date = coupleDetailsArrayList.get(count-1).getFpmdates();
//            for (int i=0; i<date.size(); i++) {
//                Log.i("FRAGMENTCOUPLE", "doThisToCut: "+date.get(i));
//            }
//
//            for (int i=0; i<date.size(); i++) {
//                if (i == 0) {
//                    editText.setText(date.get(i));
//                } else {
//                    cnt++;
//                    addEditTextLayout(cnt);
//                    allEds.get(i-1).setText(date.get(i));
//                }
//            }
//        }
    }

    private String getOid() {
        ObjectId memberId = ObjectId.get();
        return memberId.toString();
    }
    private void checkForCouplelayout() {
        if (maleFlag || femaleFlag) {
            noMaleFemaleLayout.setVisibility(View.VISIBLE);
            relCoupleLayout.setVisibility(View.GONE);
        } else {
            previousCoupleLayout.setVisibility(View.VISIBLE);
            relCoupleLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showSnackBar(String content, boolean bol) {
        Snackbar.make(v.getRootView(), content, 5000).show();
        if (bol) {
//            removeCoupleLayout();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(v.getContext(), "Doneeee...", Toast.LENGTH_SHORT).show();
                }
            }, 5000);
            return;
        }

    }

    private void addEditTextLayout(int cnt) {
        int cnt2 = cnt;
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText editText = new EditText(v.getContext());
        layoutParams.setMargins(30, 9, 30, 0);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint(String.valueOf(cnt2+1)+" date of visit");
        editText.setId(cnt2+1);
        editText.setLayoutParams(layoutParams);
        allEds.add(editText);
        linearLayout.addView(editText);
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
}
