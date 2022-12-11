package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hospitalmanagement.mainapp.GetterSetter.FamilyMemberList;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.Sessions.DatabaseHelper;
import com.hospitalmanagement.mainapp.Sessions.SessionManager;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FragmentMemberDetails extends Fragment {
    View v;

    //Manager
    SessionManager sessionManager;
    DatabaseHelper databaseHelper;
    ProgressDialog progressDialog;

    //UI Components
    TextView textMemberDetailNumber, textMember1, textMongoId;
    TextInputEditText editName, editAge, editAadhar, editAccno, editBankname, editDisabilityOther
            , editDiseaseOther, editTreatmentOther, editMobileNumber;
    MaterialButton btnAddMember, btnNext, btnDeleteMember;
    RadioGroup radioGender;
    CheckBox cbkDiseaseHeart, cbkDiseaseDiabetes, cbkDiseaseTb, cbkDiseaseCancer,
            cbkDieaseLeprosy, cbkDiseaseOther, cbkDiseaseDiabetes123;
    CheckBox cbkTreatmentHomepathy, cbkTreatmentAleopathy, cbkTreatmentAyurvedic,
            cbkTreatmentOthers;
    CheckBox cbkDisabilityHeart, cbkDisabilityEye, cbkDisabilityEar, cbkDisabilityOther;
    NestedScrollView nestedScrollView;
    RadioButton radioGenderMale, radioGenderFemale;
    RelativeLayout addMemberDetail, memberDetailLayout;
    MaterialSpinner memberSpinner;

    //Validation
    AwesomeValidation basicValidation, diseaseOtherValidation, disabilityOtherValidation,
            treatmentOtherValidation;

    //AcceptedInputs
    String name, gender="पुरुष", aadhar, bankNo, bankName, memberId, mobileNumber;
    ArrayList<String> disease, treatment, disability;
    int age;
    int memberCount = 1;
    int flag = 0;
    boolean flagPreviousMember = false;

    //Family Member Arraylist
    ArrayList<FamilyMemberList> familyMemberLists;
    ArrayList<String> memberList;
    String oid;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater =
                LayoutInflater.from(new ContextThemeWrapper(getContext(),
                        R.style.AppTheme));
        v = inflater.inflate(R.layout.fragment_create_member, container,false);
        initApp();

        addValidation();
        memberSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                textMemberDetailNumber.setText(String.valueOf(position+1));
                textMember1.setText("Member " + String.valueOf(position+1) + " Details");
                showProgressBar("Loading details. Please wait...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stopProgressBar();
                            checkForPreviousData();
                        } catch (Exception e) {
                            Log.i("FRAGMENTMEMBEr", "run: "+e.getLocalizedMessage());
                        }

                    }
                }, 1000);
            }
        });
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basicValidation.validate()) {
                    saveInformation(true);
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basicValidation.validate()) {
                    saveInformation(false);
                }
            }
        });
        btnDeleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMember(Integer.parseInt(textMemberDetailNumber.getText().toString().trim()));
            }
        });
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton genderRb = (RadioButton) v.findViewById(checkedId);
                gender = genderRb.getText().toString();
            }
        });

        v.setFocusableInTouchMode(true);
        v.requestFocus( );
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        if (Integer.parseInt(textMemberDetailNumber.getText().toString().trim()) == 1) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    FragmentTransaction fa = getFragmentManager().beginTransaction();
//                                    fa.replace(R.id.fragment_container, new FragmentGenInfo());
//                                    fa.commit();
//                                }
//                            }, 1000);
//                        } else if(Integer.parseInt(textMemberDetailNumber.getText().toString().trim()) > 1) {
//                            reloadPage(Integer.parseInt(textMemberDetailNumber.getText().toString().trim()) - 2);
//                            populateData(familyMemberLists);
//                        }
                        FragmentTransaction fa = getFragmentManager().beginTransaction();
                                    fa.replace(R.id.fragment_container, new FragmentGenInfo());
                                    fa.commit();
                        return true;
                    }
                }
                return false;
            }
        });

        return v;
    }

    private void deleteMember(final int memC) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Alert")
                .setMessage("Do you want to delete this member entry ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String json = "";
                        if (familyMemberLists.size() == 1) {
                            familyMemberLists.remove(memC-1);
                        } else {
                            familyMemberLists.remove(memC - 1);
                            Gson gson = new Gson();
                            json = gson.toJson(familyMemberLists);
                        }
                        final String js = json;
                        showProgressBar("Deleting member. Please wait...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                databaseHelper.saveMemberDetails(js);
                                stopProgressBar();
                                textMemberDetailNumber.setText("1");
                                textMember1.setText("Member " + "1" + " Details");
                                checkForPreviousData();
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

    private void initApp() {
        //Session Manager
        sessionManager = new SessionManager(v.getContext());
        databaseHelper = new DatabaseHelper(v.getContext());

        //FamilyMemberArrayList
        familyMemberLists = new ArrayList<FamilyMemberList>();
        memberList = new ArrayList<String>();

        //TextView
        textMemberDetailNumber = (TextView) v.findViewById(R.id.textMemDetailNumber);
        textMember1 = (TextView) v.findViewById(R.id.textMember1);
        textMongoId = (TextView) v.findViewById(R.id.textMongoId);
        memberCount = Integer.parseInt(textMemberDetailNumber.getText().toString().trim());

        //TextInputEditText
        editName = (TextInputEditText) v.findViewById(R.id.editMem1Name);
        editAge = (TextInputEditText) v.findViewById(R.id.editMem1Age);
        editAadhar = (TextInputEditText) v.findViewById(R.id.editMem1Aadhar);
        editAccno = (TextInputEditText) v.findViewById(R.id.editMem1Accno);
        editBankname = (TextInputEditText) v.findViewById(R.id.editMem1BankName);
        editDisabilityOther = (TextInputEditText) v.findViewById(R.id.editMem1DisabilityOther);
        editDiseaseOther = (TextInputEditText) v.findViewById(R.id.editMem1DiseaseOther);
        editTreatmentOther = (TextInputEditText) v.findViewById(R.id.editMem1TreatmentOther);
        editMobileNumber = (TextInputEditText) v.findViewById(R.id.editMobileNumber);

        //MaterialButton
        btnAddMember = (MaterialButton) v.findViewById(R.id.btnMem1Add);
        btnNext = (MaterialButton) v.findViewById(R.id.btnMem1Next);
        btnDeleteMember = (MaterialButton) v.findViewById(R.id.btnDeleteMember);

        //RadioGroup
        radioGender = (RadioGroup) v.findViewById(R.id.radioMem1Gender);

        //RelativeLayout
        addMemberDetail = (RelativeLayout) v.findViewById(R.id.addMemberLayout);
        memberDetailLayout = (RelativeLayout) v.findViewById(R.id.memberDetailLayout);

        //MaterialSpinner
        memberSpinner = (MaterialSpinner) v.findViewById(R.id.memberSpinner);

        //Disease Checkbox
        cbkDiseaseHeart = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseHeart);
        cbkDiseaseDiabetes = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseDiabetes);
        cbkDiseaseTb = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseTuberclosis);
        cbkDiseaseCancer = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseCancer);
        cbkDieaseLeprosy = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseLeprosy);
        cbkDiseaseOther = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseOther);
        cbkDiseaseDiabetes123 = (CheckBox) v.findViewById(R.id.cbkMem1DiseaseDiabetes123);

        //Treatment Checkbox
        cbkTreatmentHomepathy = (CheckBox) v.findViewById(R.id.cbkMem1TreatmentHomeopathy);
        cbkTreatmentAleopathy = (CheckBox) v.findViewById(R.id.cbkMem1TreatmentAloepathy);
        cbkTreatmentAyurvedic = (CheckBox) v.findViewById(R.id.cbkMem1TreatmentAyurvedic);
        cbkTreatmentOthers = (CheckBox) v.findViewById(R.id.cbkMem1TreatmentOther);

        //Disability Checkbox
        cbkDisabilityHeart = (CheckBox) v.findViewById(R.id.cbkMem1DisabilityHeart);
        cbkDisabilityEar = (CheckBox) v.findViewById(R.id.cbkMem1DisabilityEar);
        cbkDisabilityEye = (CheckBox) v.findViewById(R.id.cbkMem1DisabilityEye);
        cbkDisabilityOther = (CheckBox) v.findViewById(R.id.cbkMem1DisabilityOther);

        //Radiobutton
        radioGenderMale = (RadioButton) v.findViewById(R.id.radioMem1GenderMale);
        radioGenderFemale = (RadioButton) v.findViewById(R.id.radioMem1GenderFemale);

        //Validation
        basicValidation = new AwesomeValidation(ValidationStyle.BASIC);
        diseaseOtherValidation = new AwesomeValidation(ValidationStyle.BASIC);
        disabilityOtherValidation = new AwesomeValidation(ValidationStyle.BASIC);
        treatmentOtherValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //NestedScrollView
        nestedScrollView = (NestedScrollView) v.findViewById(R.id.memberScrollView);

        //ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        checkForPreviousData();
    }

    private void saveInformation(boolean type) {
        name = editName.getText().toString();
        age = Integer.parseInt(editAge.getText().toString());
        aadhar = editAadhar.getText().toString();
        bankNo = editAccno.getText().toString();
        bankName = editBankname.getText().toString();
        disease = diseaseCheckbox();
        treatment = treatmentCheckbox();
        disability = disabilityCheckbox();
//        memberCount = Integer.parseInt(textMemberDetailNumber.getText().toString().trim());
        memberId = textMongoId.getText().toString().trim();
        mobileNumber = editMobileNumber.getText().toString().trim();
        int diseasePos = -1, treatmentPos = -1, disabilityPos = -1;
        diseasePos = disease.indexOf("इतर");
        treatmentPos = treatment.indexOf("इतर");
        disabilityPos = disability.indexOf("इतर");
        if (diseasePos != -1) {
            disease.add(editDiseaseOther.getText().toString());
        }
        if (treatmentPos != -1) {
            treatment.add(editTreatmentOther.getText().toString());
        }
        if (disabilityPos != -1) {
            disability.add(editDisabilityOther.getText().toString());
        }
        String id = "";
//        try {
//            id = String.valueOf(ObjectId.get());
//        } catch (Exception e) {
//            Log.i("FRAGMENTMEMBEr", e.getLocalizedMessage());
//        }
        FamilyMemberList familyMemberList = new FamilyMemberList(name, age, aadhar, bankNo,
                bankName, gender, disability, disease, treatment, memberId, mobileNumber);
        storeInArrayList(familyMemberList, type);
    }

    private void storeInArrayList(FamilyMemberList familyMemberList, boolean type) {
        memberCount = Integer.parseInt(textMemberDetailNumber.getText().toString().trim());
        if (flag == 1) {
            familyMemberLists.remove(memberCount-1);
            familyMemberLists.add(memberCount-1, familyMemberList);
            flag = 0;
        } else {
            familyMemberLists.add(memberCount-1, familyMemberList);
            flag = 0;
        }
        changeFragment(familyMemberLists, "Saving member "+memberCount+" details. Please wait..."
                , type, memberCount);
    }

    private void changeFragment(final ArrayList<FamilyMemberList> familyMemberLists, String content,
                                boolean type, final int memCount) {
        Gson gson = new Gson();
        String json = gson.toJson(familyMemberLists);
        if (type) {
            databaseHelper.saveMemberDetails(json);
            showProgressBar(content);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    if (flagPreviousMember) {
                        int count = familyMemberLists.size();
                        reloadPage(count);
                    } else {
                        reloadPage(memCount);
                    }
                    checkForPreviousData();
                }
            }, 1000);
        } else {
            databaseHelper.saveMemberDetails(json);
            showProgressBar(content);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopProgressBar();
                    FragmentTransaction fa = getFragmentManager().beginTransaction();
                    fa.replace(R.id.fragment_container, new FragmentCoupleDetails());
                    fa.commit();
                }
            }, 1000);
        }
    }

    private void reloadPage(int memCount) {
        String memDet = "Member " + (memCount+1) + " Details";
        textMember1.setText(memDet);
        textMemberDetailNumber.setText(String.valueOf(memCount+1));
        editName.setText("");
        editAge.setText("");
        editAadhar.setText("");
        editAccno.setText("");
        editBankname.setText("");
        editDisabilityOther.setText("");
        editDiseaseOther.setText("");
        editTreatmentOther.setText("");
        editMobileNumber.setText("");
        editName.clearFocus();
        editAge.clearFocus();
        editAadhar.clearFocus();
        editAccno.clearFocus();
        editBankname.clearFocus();
        editDisabilityOther.clearFocus();
        editDiseaseOther.clearFocus();
        editTreatmentOther.clearFocus();
        editMobileNumber.clearFocus();
        radioGenderMale.setChecked(true);
        radioGenderFemale.setChecked(false);
        gender = "पुरुष";
        nestedScrollView.scrollTo(0,0);
        textMongoId.setText(getOid());
        //Treatment checkbox
        cbkTreatmentAleopathy.setChecked(false);
        cbkTreatmentAyurvedic.setChecked(false);
        cbkTreatmentHomepathy.setChecked(false);
        cbkTreatmentOthers.setChecked(false);
        //Disease checkbox
        cbkDiseaseHeart.setChecked(false);
        cbkDiseaseCancer.setChecked(false);
        cbkDiseaseDiabetes.setChecked(false);
        cbkDiseaseOther.setChecked(false);
        cbkDieaseLeprosy.setChecked(false);
        cbkDiseaseTb.setChecked(false);
        cbkDiseaseDiabetes123.setChecked(false);
        //Disability checkbox
        cbkDisabilityEar.setChecked(false);
        cbkDisabilityEye.setChecked(false);
        cbkDisabilityHeart.setChecked(false);
        cbkDisabilityOther.setChecked(false);
        btnDeleteMember.setVisibility(View.GONE);
    }

    private String getOid() {
        ObjectId memberId = ObjectId.get();
        return memberId.toString();
    }

    private void checkForPreviousData() {
        if (databaseHelper.getMemberCount() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FamilyMemberList>>(){}.getType();
            if (databaseHelper.getMemberDetails().equals("")) {
                reloadPage(0);
                flagPreviousMember = false;
                flag = 0;
                addMemberDetail.setVisibility(View.GONE);
            } else {
                ArrayList<FamilyMemberList> famLists = gson.fromJson(databaseHelper.getMemberDetails(), type);
                populateData(famLists);
                addMemberDetail.setVisibility(View.VISIBLE);
                flagPreviousMember = true;
            }
        } else {
            flagPreviousMember = false;
            addMemberDetail.setVisibility(View.GONE);
            reloadPage(0);
        }
    }

    private void populateData(ArrayList<FamilyMemberList> famList) {
        familyMemberLists = famList;
        memberList.clear();
        for (int i=0; i<familyMemberLists.size(); i++) {
            memberList.add(familyMemberLists.get(i).getName());
            Log.i("FRAGMENTMEMBER", "Objects id : "+familyMemberLists.get(i).getMemberid());
        }
        memberSpinner.setItems(memberList);
        int cnt = Integer.parseInt(textMemberDetailNumber.getText().toString().trim());
        if (memberList.size() <= cnt - 1) {

        } else {
            memberSpinner.setSelectedIndex(cnt-1);
        }
        if (familyMemberLists.size() >= cnt) {
            flag = 1;
            btnDeleteMember.setVisibility(View.VISIBLE);
            textMongoId.setText(familyMemberLists.get(cnt-1).getMemberid());
            Log.i("FRAGMENTMEMBER", "populateData: "+familyMemberLists.get(cnt-1).getMemberid().toString());
            name = familyMemberLists.get(cnt-1).getName();
            age = familyMemberLists.get(cnt-1).getAge();
            bankName = familyMemberLists.get(cnt-1).getBankName();
            bankNo = familyMemberLists.get(cnt-1).getBankNo();
            aadhar = familyMemberLists.get(cnt-1).getAadharNo();
            gender = familyMemberLists.get(cnt-1).getGender();
            mobileNumber = familyMemberLists.get(cnt-1).getMobile_no();
            editName.setText(name);
            editAge.setText(String.valueOf(age));
            editBankname.setText(bankName);
            editAccno.setText(bankNo);
            editAadhar.setText(aadhar);
            editMobileNumber.setText(mobileNumber);
            if (gender.equals("पुरुष")) {
                RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioMem1GenderMale);
                radioButton.setChecked(true);
            } else if (gender.equals("स्त्री")) {
                RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioMem1GenderFemale);
                radioButton.setChecked(true);
            } else if (gender.equals("इतर")) {
                RadioButton radioButton = (RadioButton) v.findViewById(R.id.radioMem1GenderOthers);
                radioButton.setChecked(true);
            }
            setDiseaseCheckbox(familyMemberLists.get(cnt-1).getDisease());
            setDisabilityCheckbox(familyMemberLists.get(cnt-1).getDisability());
            setTreatmentCheckbox(familyMemberLists.get(cnt-1).getTreatment());
        }
    }

    private ArrayList<String> diseaseCheckbox() {
        ArrayList<String> diseaseList = new ArrayList<String>();
        if (cbkDiseaseHeart.isChecked()) {
            diseaseList.add(cbkDiseaseHeart.getText().toString());
        }
        if (cbkDiseaseCancer.isChecked()) {
            diseaseList.add(cbkDiseaseCancer.getText().toString());
        }
        if (cbkDiseaseDiabetes.isChecked()) {
            diseaseList.add(cbkDiseaseDiabetes.getText().toString());
        }
        if (cbkDiseaseTb.isChecked()) {
            diseaseList.add(cbkDiseaseTb.getText().toString());
        }
        if (cbkDieaseLeprosy.isChecked()) {
            diseaseList.add(cbkDieaseLeprosy.getText().toString());
        }
        if (cbkDiseaseOther.isChecked()) {
            diseaseList.add(cbkDiseaseOther.getText().toString());
        }
        if (cbkDiseaseDiabetes123.isChecked()) {
            diseaseList.add(cbkDiseaseDiabetes123.getText().toString());
        }
        return diseaseList;
    }

    private ArrayList<String> disabilityCheckbox() {
        ArrayList<String> disabilityList = new ArrayList<String>();
        if (cbkDisabilityHeart.isChecked()) {
            disabilityList.add(cbkDisabilityHeart.getText().toString());
        }
        if (cbkDisabilityEye.isChecked()) {
            disabilityList.add(cbkDisabilityEye.getText().toString());
        }
        if (cbkDisabilityEar.isChecked()) {
            disabilityList.add(cbkDisabilityEar.getText().toString());
        }
        if (cbkDisabilityOther.isChecked()) {
            disabilityList.add(cbkDisabilityOther.getText().toString());
        }
        return disabilityList;
    }

    private ArrayList<String> treatmentCheckbox() {
        ArrayList<String> treatmentList = new ArrayList<String>();
        if (cbkTreatmentHomepathy.isChecked()) {
            treatmentList.add(cbkTreatmentHomepathy.getText().toString());
        }
        if (cbkTreatmentAleopathy.isChecked()) {
            treatmentList.add(cbkTreatmentAleopathy.getText().toString());
        }
        if (cbkTreatmentAyurvedic.isChecked()) {
            treatmentList.add(cbkTreatmentAyurvedic.getText().toString());
        }
        if (cbkTreatmentOthers.isChecked()) {
            treatmentList.add(cbkTreatmentOthers.getText().toString());
        }
        return treatmentList;
    }


    private void setDisabilityCheckbox(ArrayList<String> getDisability) {
        int disabilityPos = -1;
        disabilityPos = getDisability.indexOf("ऑर्थो");
        if (disabilityPos != -1) {
            cbkDisabilityHeart.setChecked(true);
        }
        disabilityPos = -1;
        disabilityPos = getDisability.indexOf("डोळा");
        if (disabilityPos != -1) {
            cbkDisabilityEye.setChecked(true);
        }
        disabilityPos = -1;
        disabilityPos = getDisability.indexOf("कान");
        if (disabilityPos != -1) {
            cbkDisabilityEar.setChecked(true);
        }
        disabilityPos = -1;
        disabilityPos = getDisability.indexOf("इतर");
        if (disabilityPos != -1) {
            cbkDisabilityOther.setChecked(true);
            editDisabilityOther.setText(getDisability.get(disabilityPos+1));
        }
    }

    private void setTreatmentCheckbox(ArrayList<String> getTreatment) {
        int treatmentPos = -1;
        treatmentPos = getTreatment.indexOf("होमिओपॅथी");
        if (treatmentPos != -1) {
            cbkTreatmentHomepathy.setChecked(true);
        }
        treatmentPos = -1;
        treatmentPos = getTreatment.indexOf("अ\u200Dॅलोपॅथी");
        if (treatmentPos != -1) {
            cbkTreatmentAleopathy.setChecked(true);
        }
        treatmentPos = -1;
        treatmentPos = getTreatment.indexOf("आयुर्वेदिक");
        if (treatmentPos != -1) {
            cbkTreatmentAyurvedic.setChecked(true);
        }
        treatmentPos = -1;
        treatmentPos = getTreatment.indexOf("इतर");
        if (treatmentPos != -1) {
            cbkTreatmentOthers.setChecked(true);
            editTreatmentOther.setText(getTreatment.get(treatmentPos+1));
        }
    }

    private void setDiseaseCheckbox(ArrayList<String> getDisease) {
        int diseasePos = -1;
        diseasePos = getDisease.indexOf("हृदय रोग");
        if (diseasePos != -1) {
            cbkDiseaseHeart.setChecked(true);
        }
        diseasePos = -1;
        diseasePos = getDisease.indexOf("उच्च रक्तदाब");
        if (diseasePos != -1) {
            cbkDiseaseDiabetes.setChecked(true);
        }
        diseasePos = -1;
        diseasePos = getDisease.indexOf("टीबी");
        if (diseasePos != -1) {
            cbkDiseaseTb.setChecked(true);
        }
        diseasePos = -1;
        diseasePos = getDisease.indexOf("कर्करोग");
        if (diseasePos != -1) {
            cbkDiseaseCancer.setChecked(true);
        }
        diseasePos = -1;
        diseasePos = getDisease.indexOf("कुष्ठरोग");
        if (diseasePos != -1) {
            cbkDieaseLeprosy.setChecked(true);
        }
        diseasePos = -1;
        diseasePos = getDisease.indexOf("इतर");
        if (diseasePos != -1) {
            cbkDiseaseOther.setChecked(true);
            editDiseaseOther.setText(getDisease.get(diseasePos+1));
        }
        diseasePos = -1;
        diseasePos = getDisease.indexOf("मधुमेह");
        if (diseasePos != -1) {
            cbkDiseaseDiabetes123.setChecked(true);
        }
    }

    private void addValidation() {
        basicValidation.addValidation(editName, RegexTemplate.NOT_EMPTY, "नाव प्रविष्ट करा");
        basicValidation.addValidation(editAge, RegexTemplate.NOT_EMPTY, "वय प्रविष्ट करा");
        disabilityOtherValidation.addValidation(editDisabilityOther,RegexTemplate.NOT_EMPTY, "इतर अपंगत्व प्रविष्ट करा");
        diseaseOtherValidation.addValidation(editDiseaseOther, RegexTemplate.NOT_EMPTY, "इतर रोग प्रविष्ट करा");
        treatmentOtherValidation.addValidation(editTreatmentOther, RegexTemplate.NOT_EMPTY, "इतर उपचार पद्धती प्रविष्ट करा");
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

    private void displayAlertDialog() {

    }
}
