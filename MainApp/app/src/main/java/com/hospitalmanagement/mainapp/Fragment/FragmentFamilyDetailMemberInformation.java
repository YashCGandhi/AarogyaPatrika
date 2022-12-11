package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.pojo.Data;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.Member;
import com.hospitalmanagement.mainapp.pojo.MutationQueryFamilyDetail;
import com.hospitalmanagement.mainapp.pojo.UpdateResponse;
import com.hospitalmanagement.mainapp.pojo.Variables;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFamilyDetailMemberInformation extends Fragment {

    private List<Member> memberNames, members;
    private List<String> memberDropdownList;
    private int selectedMemberIndex;
    private HashMap<String, Object> changedInformation;
    private List<String> changedDisabilityAdded;
    private List<String> changedDiseaseAdded;
    private List<String> changedModalityAdded;
    private List<String> changedDisabilityRemoved;
    private List<String> changedDiseaseRemoved;
    private List<String> changedModalityRemoved;
    private String objectId;
    private ApiInterface apiInterface;
    private boolean addMember = false;

    AwesomeValidation basicValidation, diseaseOtherValidation, disabilityOtherValidation,
            treatmentOtherValidation;

    private TextWatcher textWatcher;
    private Bundle bundle;
    private ProgressDialog progressDialog;
    private Handler handler;
    private AlertDialog.Builder alertDialogBuilder;

    private TextInputEditText editTextMemberName;
    private TextInputEditText editTextAge;
    private TextInputEditText editTextMobileNumber;
    private RadioGroup radioGroupGender;
    private TextInputEditText editTextAadharNumber;
    private TextInputEditText editTextBankName;
    private TextInputEditText editTextBankAccountNumber;
    private CheckBox checkBoxDisabilityOrtho;
    private CheckBox checkBoxDisabilityEye;
    private CheckBox checkBoxDisabilityEar;
    private CheckBox checkBoxDisabilityOther;
    private TextInputEditText editTextDisabilityOther;
    private CheckBox checkBoxDiseaseHeart, checkBoxDiseaseBp;
    private CheckBox checkBoxDiseaseDiabetes;
    private CheckBox checkBoxDiseaseTuberculosis;
    private CheckBox checkBoxDiseaseCancer;
    private CheckBox checkBoxDiseaseLeprosy;
    private CheckBox checkBoxDiseaseOther;
    private TextInputEditText editTextDiseaseOther;
    private CheckBox checkBoxTreatmentHomeopathy;
    private CheckBox checkBoxTreatmentAloepathy;
    private CheckBox checkBoxTreatmentAyurvedic;
    private CheckBox checkBoxTreatmentOther;
    private TextInputEditText editTextTreatmentOther;
    private MaterialSpinner memberMaterialSpinner;
    private RelativeLayout relativeLayoutMemberDetail;
    private MaterialButton saveMemberInformation;
    private MaterialButton addMemberButton;
    private TextView textViewMember;
    private NestedScrollView nestedScrollViewMember;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bundle = this.getArguments();

        if(bundle != null) {
            objectId = bundle.getString("objectId");
        }

        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        return inflater.inflate(R.layout.familydetail_memberinformation, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        memberDropdownList = new ArrayList<>();
        changedInformation = new HashMap<>();
        changedDisabilityAdded = new ArrayList<>();
        changedDiseaseAdded = new ArrayList<>();
        changedModalityAdded = new ArrayList<>();
        changedDisabilityRemoved = new ArrayList<>();
        changedDiseaseRemoved = new ArrayList<>();
        changedModalityRemoved = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        handler = new Handler();
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        //Validation
        basicValidation = new AwesomeValidation(ValidationStyle.BASIC);
        diseaseOtherValidation = new AwesomeValidation(ValidationStyle.BASIC);
        disabilityOtherValidation = new AwesomeValidation(ValidationStyle.BASIC);
        treatmentOtherValidation = new AwesomeValidation(ValidationStyle.BASIC);


        editTextMemberName = view.findViewById(R.id.editMemberName);
        editTextAge = view.findViewById(R.id.editMemberAge);
        radioGroupGender = view.findViewById(R.id.radioGroupMemberGender);
        editTextAadharNumber = view.findViewById(R.id.editMemberAadhar);
        editTextBankName = view.findViewById(R.id.editMemberBankName);
        editTextBankAccountNumber = view.findViewById(R.id.editMemberAccno);
        checkBoxDisabilityOrtho = view.findViewById(R.id.checkBoxMemberDisabilityOrtho);
        checkBoxDisabilityEye = view.findViewById(R.id.checkBoxMemberDisabilityEye);
        checkBoxDisabilityEar = view.findViewById(R.id.checkBoxMemberDisabilityEar);
        checkBoxDisabilityOther = view.findViewById(R.id.checkBoxMemberDisabilityOther);
        editTextDisabilityOther = view.findViewById(R.id.editMemberDisabilityOther);
        checkBoxDiseaseHeart = view.findViewById(R.id.checkBoxMemberDiseaseHeart);
        checkBoxDiseaseDiabetes = view.findViewById(R.id.checkBoxMemberDiseaseDiabetes);
        checkBoxDiseaseTuberculosis = view.findViewById(R.id.checkBoxMemberDiseaseTuberclosis);
        checkBoxDiseaseCancer = view.findViewById(R.id.checkBoxMemberDiseaseCancer);
        checkBoxDiseaseLeprosy = view.findViewById(R.id.checkBoxMemberDiseaseLeprosy);
        checkBoxDiseaseOther = view.findViewById(R.id.checkBoxMemberDiseaseOther);
        editTextDiseaseOther = view.findViewById(R.id.editMemberDiseaseOther);
        checkBoxTreatmentHomeopathy = view.findViewById(R.id.checkBoxMemberTreatmentHomeopathy);
        checkBoxTreatmentAloepathy = view.findViewById(R.id.checkBoxMemberTreatmentAloepathy);
        checkBoxTreatmentAyurvedic = view.findViewById(R.id.checkBoxMemberTreatmentAyurvedic);
        checkBoxTreatmentOther = view.findViewById(R.id.checkBoxMemberTreatmentOther);
        editTextTreatmentOther = view.findViewById(R.id.editMemberTreatmentOther);
        memberMaterialSpinner = view.findViewById(R.id.memberNameMultiSpinner);
        relativeLayoutMemberDetail = view.findViewById(R.id.memberDetailViewLayout);
        saveMemberInformation = view.findViewById(R.id.btnSaveMemberInformation);
        addMemberButton = view.findViewById(R.id.btnAddMember);
        textViewMember = view.findViewById(R.id.textMember);
        nestedScrollViewMember = view.findViewById(R.id.memberInformationNestedScrollView);
        editTextMobileNumber = view.findViewById(R.id.editTextMobileNumber);
        checkBoxDiseaseBp = view.findViewById(R.id.checkBoxMemberDiseaseBp);



        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i("gaya", "onTextChenged");
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("gaya", "afterTextChenged");

                if(s != null) {

                    if(s.hashCode()  == editTextMemberName.getText().hashCode()) {

                        if(members.get(selectedMemberIndex).getMembername() != null) {
                            if (!s.toString().equals(members.get(selectedMemberIndex).getMembername())) {

                                changedInformation.put("membername", editTextMemberName.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("membername"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("membername");
                                if (changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextMemberName.getText().toString().trim().length() != 0) {
                                changedInformation.put("membername", editTextMemberName.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("membername"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if(s.hashCode() == editTextAge.getText().hashCode()) {

                        if(members.get(selectedMemberIndex).getAge() != null) {
                            if (!s.toString().equals(String.valueOf(members.get(selectedMemberIndex).getAge()))) {

                                changedInformation.put("age", editTextAge.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("age"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("age");
                                if (changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextAge.getText().toString().trim().length() != 0) {
                                changedInformation.put("age", editTextAge.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("age"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if(s.hashCode() == editTextAadharNumber.getText().hashCode()) {

                        if(members.get(selectedMemberIndex).getAddharNo() != null) {
                            if (!s.toString().equals(String.valueOf(members.get(selectedMemberIndex).getAddharNo()))) {

                                changedInformation.put("addhar_no", editTextAadharNumber.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("addhar_no"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("addhar_no");
                                if (changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextAadharNumber.getText().toString().trim().length() != 0) {
                                changedInformation.put("addhar_no", editTextAadharNumber.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("addhar_no"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if(s.hashCode() == editTextBankAccountNumber.getText().hashCode()) {

                        if(members.get(selectedMemberIndex).getBankAcc() != null) {
                            if (!s.toString().equals(String.valueOf(members.get(selectedMemberIndex).getBankAcc()))) {

                                changedInformation.put("bank_acc", editTextBankAccountNumber.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("bank_acc"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("bank_acc");
                                if (changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextBankAccountNumber.getText().toString().trim().length() != 0) {
                                changedInformation.put("bank_acc", editTextBankAccountNumber.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("bank_acc"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if(s.hashCode() == editTextBankName.getText().hashCode()) {

                        if(members.get(selectedMemberIndex).getBankName() != null) {
                            if (!s.toString().equals(String.valueOf(members.get(selectedMemberIndex).getBankName()))) {

                                changedInformation.put("bank_name", editTextBankName.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("bank_name"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("bank_name");
                                if (changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextBankName.getText().toString().trim().length() != 0) {
                                changedInformation.put("bank_name", editTextBankName.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("bank_name"));

                                if (saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }  else if(s.hashCode() == editTextDisabilityOther.getText().hashCode()) {

                        for(String disability : members.get(selectedMemberIndex).getDisabilityType()) {

                            if(!s.toString().equals(String.valueOf(disability))) {

                                changedInformation.put("disabilityOther", editTextDisabilityOther.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("disabilityOther"));

                                if(saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("disabilityOther");
                                if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }

                        }


                    } else if(s.hashCode() == editTextDiseaseOther.getText().hashCode()) {

                        for(String disease : members.get(selectedMemberIndex).getDisease()) {

                            if(!s.toString().equals(String.valueOf(disease))) {

                                changedInformation.put("diseaseOther", editTextDiseaseOther.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("diseaseOther"));

                                if(saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("diseaseOther");
                                if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }

                        }


                    } else if(s.hashCode() == editTextTreatmentOther.getText().hashCode()) {

                        for(String modality : members.get(selectedMemberIndex).getModality()) {

                            if(!s.toString().equals(String.valueOf(modality))) {

                                changedInformation.put("modalityOther", editTextTreatmentOther.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("modalityOther"));

                                if(saveMemberInformation.getVisibility() == View.GONE) {
                                    saveMemberInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("modalityOther");
                                if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                    saveMemberInformation.setVisibility(View.GONE);
                                }

                            }

                        }


                    }
                    else if(s.hashCode() == editTextMobileNumber.getText().hashCode()) {

                        if(!s.toString().equals(String.valueOf(members.get(selectedMemberIndex).getMobile_no()))) {

                            changedInformation.put("mobile_no", editTextMobileNumber.getText().toString());
//                            System.out.println("Changes mobile " + changedInformation.get("mobile_no"));

                            if(saveMemberInformation.getVisibility() == View.GONE) {
                                saveMemberInformation.setVisibility(View.VISIBLE);
                            }

                        } else {

                            changedInformation.remove("mobile_no");
                            if(changedInformation.isEmpty()) {
                                saveMemberInformation.setVisibility(View.GONE);
                            }

                        }

                    }

                }

            }
        };


        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMember = true;
                changedInformation.clear();
                changedModalityRemoved.clear();
                changedModalityAdded.clear();
                changedDiseaseRemoved.clear();
                changedDiseaseAdded.clear();
                changedDisabilityRemoved.clear();
                changedDisabilityAdded.clear();
                saveMemberInformation.setVisibility(View.VISIBLE);
                relativeLayoutMemberDetail.setVisibility(View.GONE);
                memberMaterialSpinner.setVisibility(View.GONE);
                textViewMember.setVisibility(View.GONE);

                clearDataFromLayout();
                removeListenersFromComponents();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relativeLayoutMemberDetail.setVisibility(View.VISIBLE);
                    }
                }, 500);

                nestedScrollViewMember.scrollTo(0, 0);
            }
        });


        saveMemberInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Variables variables = new Variables();

                MutationQueryFamilyDetail mutationQuery = new MutationQueryFamilyDetail();
                if(basicValidation.validate()) {
                    if (!addMember) {

                        List<String> updatedDisease = new ArrayList<>(members.get(selectedMemberIndex).getDisease());
                        List<String> updatedDisability = new ArrayList<>(members.get(selectedMemberIndex).getDisabilityType());
                        List<String> updatedModality = new ArrayList<>(members.get(selectedMemberIndex).getModality());

                        // disability
                        for (String disability : members.get(selectedMemberIndex).getDisabilityType()) {
                            if (!changedDisabilityRemoved.isEmpty() && changedDisabilityRemoved.contains(disability)) {
                                updatedDisability.remove(disability);
                            }
                        }

//                        System.out.println("Update after removed: " + updatedDisability);

                        for (String disability : changedDisabilityAdded) {
                            updatedDisability.add(disability);
                        }

//                        System.out.println("Update after added: " + updatedDisability);

                        if (changedInformation.containsKey("disabilityOther")) {
                            if (((String) changedInformation.get("disabilityOther")).trim().length() != 0) {
                                updatedDisability.add((String) changedInformation.get("disabilityOther"));
                            }
                            changedInformation.remove("disabilityOther");
                        }

//                        System.out.println("Update after other check: " + updatedDisability);

                        if (!updatedDisability.isEmpty() || !changedDisabilityAdded.isEmpty() || !changedDisabilityRemoved.isEmpty()) {
                            //                    inputQuery += "disability_input: " + updatedDisability + ",";
                            changedInformation.put("disability_type", updatedDisability);
                        }

//                        System.out.println("final: " + updatedDisability);

                        //disease
                        for (String disease : members.get(selectedMemberIndex).getDisease()) {
                            if (!changedDiseaseRemoved.isEmpty() && changedDiseaseRemoved.contains(disease)) {
                                updatedDisease.remove(disease);
                            }
                        }

                        for (String disease : changedDiseaseAdded) {
                            updatedDisease.add(disease);
                        }

                        if (changedInformation.containsKey("diseaseOther")) {
                            if (((String) changedInformation.get("diseaseOther")).trim().length() != 0) {
                                updatedDisease.add((String) changedInformation.get("diseaseOther"));
                            }
                            changedInformation.remove("diseaseOther");
                        }

                        if (!updatedDisease.isEmpty() || !changedDiseaseAdded.isEmpty() || !changedDiseaseRemoved.isEmpty()) {
                            //                    inputQuery += "disease: " + updatedDisease + ",";
                            changedInformation.put("disease", updatedDisease);
                        }

                        //modality
                        for (String modality : members.get(selectedMemberIndex).getModality()) {
                            if (!changedModalityRemoved.isEmpty() && changedModalityRemoved.contains(modality)) {
                                updatedModality.remove(modality);
                            }
                        }

                        for (String modality : changedModalityAdded) {
                            updatedModality.add(modality);
                        }

                        if (changedInformation.containsKey("modalityOther")) {
                            if (((String) changedInformation.get("modalityOther")).trim().length() != 0) {
                                updatedModality.add((String) changedInformation.get("modalityOther"));
                            }
                            changedInformation.remove("modalityOther");
                        }

                        if (!changedModalityAdded.isEmpty() || !changedModalityRemoved.isEmpty() || !updatedModality.isEmpty()) {
                            //                    inputQuery += "disease: " + updatedDisease + ",";
                            changedInformation.put("modality", updatedModality);
                        }

                        if (changedInformation.get("age") != null) {
                            changedInformation.put("age", Integer.parseInt(String.valueOf(changedInformation.get("age"))));
                        }
                        if (changedInformation.get("addhar_no") != null) {
                            changedInformation.put("addhar_no", changedInformation.get("addhar_no").toString());
                        }

                        variables.setInput(changedInformation);

                        mutationQuery.setVariables(variables);

////                    System.out.println(mutationQuery.getVariables().getInput());
                        apiCallToUpdateMember(mutationQuery);

                    } else {
                        // add new member

                        List<String> disease = new ArrayList<>();
                        List<String> disability = new ArrayList<>();
                        List<String> modality = new ArrayList<>();
                        List<HashMap> input = new ArrayList<>();

                        HashMap<String, Object> information = new HashMap<>();

                        if (editTextMemberName.getText().toString().trim().length() != 0) {
                            information.put("membername", editTextMemberName.getText().toString());
                        }
                        if (editTextAge.getText().toString().trim().length() != 0) {
                            information.put("age", Integer.parseInt(editTextAge.getText().toString()));
                        }
                        if (editTextAadharNumber.getText().toString().trim().length() != 0) {
                            information.put("addhar_no", editTextAadharNumber.getText().toString());
                        }
                        if (editTextBankAccountNumber.getText().toString().trim().length() != 0) {
                            information.put("bank_acc", editTextBankAccountNumber.getText().toString());
                        }
                        if (editTextBankName.getText().toString().trim().length() != 0) {
                            information.put("bank_name", editTextBankName.getText().toString());
                        }

                        // gender
                        if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioMemberGenderMale) {
                            information.put("sex", "पुरुष");
                        } else if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioMemberGenderFemale) {
                            information.put("sex", "स्त्री");
                        } else if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioMemberGenderOther) {
                            information.put("sex", "इतर");
                        }

                        //disability
                        if (checkBoxDisabilityOrtho.isChecked()) {
                            disability.add("ऑर्थो");
                        }
                        if (checkBoxDisabilityEye.isChecked()) {
                            disability.add("डोळा");
                        }
                        if (checkBoxDisabilityEar.isChecked()) {
                            disability.add("ear");
                        }
                        if (checkBoxDisabilityOther.isChecked() && editTextDisabilityOther.getText().toString().trim().length() != 0) {
                            disability.add(editTextDisabilityOther.getText().toString());
                        }
                        information.put("disability_type", disability);

                        // disease
                        if (checkBoxDiseaseHeart.isChecked()) {
                            disease.add("हृदय रोग");
                        }
                        if (checkBoxDiseaseDiabetes.isChecked()) {
                            disease.add("मधुमेह");
                        }
                        if (checkBoxDiseaseTuberculosis.isChecked()) {
                            disease.add("टीबी");
                        }
                        if (checkBoxDiseaseCancer.isChecked()) {
                            disease.add("कर्करोग");
                        }
                        if (checkBoxDiseaseLeprosy.isChecked()) {
                            disease.add("कुष्ठरोग");
                        }
                        if (checkBoxDiseaseOther.isChecked() && editTextDiseaseOther.getText().toString().trim().length() != 0) {
                            disease.add(editTextDiseaseOther.getText().toString());
                        }
                        information.put("disease", disease);

                        // modality
                        if (checkBoxTreatmentHomeopathy.isChecked()) {
                            modality.add("होमिओपॅथी");
                        }
                        if (checkBoxTreatmentAloepathy.isChecked()) {
                            modality.add("अ\u200Dॅलोपॅथी");
                        }
                        if (checkBoxTreatmentAyurvedic.isChecked()) {
                            modality.add("आयुर्वेदिक");
                        }
                        if (checkBoxTreatmentOther.isChecked() && editTextTreatmentOther.getText().toString().trim().length() != 0) {
                            modality.add(editTextTreatmentOther.getText().toString());
                        }
                        information.put("modality", modality);


                        information.put("memberid", new ObjectId().toString());
                        input.add(information);

//                        System.out.println(input);
                        variables.setInput(input);
                        mutationQuery.setVariables(variables);

                        apiCallToAddMember(mutationQuery);

                    }
                }

            }
        });



        addValidation();
        apiCallToGetMembers();
        addListenerToComponents();


    }



    private void addListenerToComponents() {


        memberMaterialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                relativeLayoutMemberDetail.setVisibility(View.GONE);

////                System.out.println("position " + (position - 1));
                if(position != 0) {

                    selectedMemberIndex = position - 1;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayoutMemberDetail.setVisibility(View.VISIBLE);
                            nestedScrollViewMember.scrollTo(0, 0);
                        }
                    }, 500);

                    setDataToLayout(members.get(position-1));

                    changedInformation.clear();
                    changedModalityRemoved.clear();
                    changedModalityAdded.clear();
                    changedDiseaseRemoved.clear();
                    changedDiseaseAdded.clear();
                    changedDisabilityRemoved.clear();
                    changedDisabilityAdded.clear();
                    saveMemberInformation.setVisibility(View.GONE);

                } else {
                    relativeLayoutMemberDetail.setVisibility(View.GONE);
                    saveMemberInformation.setVisibility(View.GONE);
                }

            }
        });



        editTextMemberName.addTextChangedListener(textWatcher);
        editTextAge.addTextChangedListener(textWatcher);
        editTextAadharNumber.addTextChangedListener(textWatcher);
        editTextBankAccountNumber.addTextChangedListener(textWatcher);
        editTextBankName.addTextChangedListener(textWatcher);
        editTextDisabilityOther.addTextChangedListener(textWatcher);
        editTextTreatmentOther.addTextChangedListener(textWatcher);
        editTextDiseaseOther.addTextChangedListener(textWatcher);
        editTextMobileNumber.addTextChangedListener(textWatcher);

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseGenderId = -1;


                if(members.get(selectedMemberIndex).getSex().equalsIgnoreCase("पुरुष")) {
                    responseGenderId = R.id.radioMemberGenderMale;
                } else if(members.get(selectedMemberIndex).getSex().equalsIgnoreCase("स्त्री")) {
                    responseGenderId = R.id.radioMemberGenderFemale;
                } else if(members.get(selectedMemberIndex).getSex().equalsIgnoreCase("इतर")) {
                    responseGenderId = R.id.radioMemberGenderOther;
                }



                switch(checkedId) {

                    case R.id.radioMemberGenderMale:
                        if(responseGenderId == R.id.radioMemberGenderMale) {

                            changedInformation.remove("sex");
//                            System.out.println(changedInformation.get("sex"));

                            if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                saveMemberInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveMemberInformation.getVisibility() == View.GONE) {
                                saveMemberInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("sex", "पुरुष");
                        }
                        break;

                    case R.id.radioMemberGenderFemale:
                        if(responseGenderId == R.id.radioMemberGenderFemale) {
                            changedInformation.remove("sex");

                            if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                saveMemberInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveMemberInformation.getVisibility() == View.GONE) {
                                saveMemberInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("sex", "स्त्री");
                        }
                        break;

                    case R.id.radioMemberGenderOther:
                        if(responseGenderId == R.id.radioMemberGenderOther) {
                            changedInformation.remove("sex");

                            if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                                saveMemberInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(saveMemberInformation.getVisibility() == View.GONE) {
                                saveMemberInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("sex", "इतर");
                        }
                        break;

                }

//                System.out.println("gender " + changedInformation.get("sex"));
            }
        });

        checkBoxDisabilityOrtho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisabilityType().contains("ऑर्थो")) {
                    
                    if(isChecked) {
                        changedDisabilityAdded.add("ऑर्थो");
                        changedDisabilityRemoved.remove("ऑर्थो");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDisabilityAdded.remove("ऑर्थो");
//                        changedDisabilityRemoved.add("ऑर्थो");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                    
                } else {
                    if(!isChecked) {
                        changedDisabilityAdded.remove("ऑर्थो");
                        changedDisabilityRemoved.add("ऑर्थो");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDisabilityAdded.add("ऑर्थो");
                        changedDisabilityRemoved.remove("ऑर्थो");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("ortho disability added " + changedDisabilityAdded);
//                System.out.println("ortho disability removed" + changedDisabilityRemoved);

            }
        });

        checkBoxDisabilityEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisabilityType().contains("डोळा")) {

                    if(isChecked) {
                        changedDisabilityAdded.add("डोळा");
                        changedDisabilityRemoved.remove("डोळा");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDisabilityAdded.remove("डोळा");
//                        changedDisabilityRemoved.add("डोळा");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDisabilityAdded.remove("डोळा");
                        changedDisabilityRemoved.add("डोळा");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDisabilityAdded.add("डोळा");
                        changedDisabilityRemoved.remove("डोळा");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("eye disability added " + changedDisabilityAdded);
//                System.out.println("eye disability removed" + changedDisabilityRemoved);

            }
        });

        checkBoxDisabilityEar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisabilityType().contains("कान")) {

                    if(isChecked) {
                        changedDisabilityAdded.add("कान");
                        changedDisabilityRemoved.remove("कान");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDisabilityAdded.remove("कान");
//                        changedDisabilityRemoved.add("कान");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDisabilityAdded.remove("कान");
                        changedDisabilityRemoved.add("कान");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDisabilityAdded.add("कान");
                        changedDisabilityRemoved.remove("कान");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("ear disability added " + changedDisabilityAdded);
//                System.out.println("ear disability removed" + changedDisabilityRemoved);

            }
        });

        checkBoxDiseaseHeart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisease().contains("हृदय रोग")) {

                    if(isChecked) {
                        changedDiseaseAdded.add("हृदय रोग");
                        changedDiseaseRemoved.remove("हृदय रोग");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDiseaseAdded.remove("हृदय रोग");
//                        changedDiseaseRemoved.add("हृदय रोग");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDiseaseAdded.remove("हृदय रोग");
                        changedDiseaseRemoved.add("हृदय रोग");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDiseaseAdded.add("हृदय रोग");
                        changedDiseaseRemoved.remove("हृदय रोग");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("heart disability added " + changedDiseaseAdded);
//                System.out.println("heart disability removed" + changedDiseaseRemoved);

            }
        });

        checkBoxDiseaseDiabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisease().contains("मधुमेह")) {

                    if(isChecked) {
                        changedDiseaseAdded.add("मधुमेह");
                        changedDiseaseRemoved.remove("मधुमेह");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDiseaseAdded.remove("मधुमेह");
//                        changedDiseaseRemoved.add("मधुमेह");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDiseaseAdded.remove("मधुमेह");
                        changedDiseaseRemoved.add("मधुमेह");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDiseaseAdded.add("मधुमेह");
                        changedDiseaseRemoved.remove("मधुमेह");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("diabetes disability added " + changedDiseaseAdded);
//                System.out.println("diabetes disability removed" + changedDiseaseRemoved);

            }
        });

        checkBoxDiseaseTuberculosis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisease().contains("टीबी")) {

                    if(isChecked) {
                        changedDiseaseAdded.add("टीबी");
                        changedDiseaseRemoved.remove("टीबी");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDiseaseAdded.remove("टीबी");
//                        changedDiseaseRemoved.add("टीबी");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDiseaseAdded.remove("टीबी");
                        changedDiseaseRemoved.add("टीबी");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDiseaseAdded.add("टीबी");
                        changedDiseaseRemoved.remove("टीबी");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("tb disability added " + changedDiseaseAdded);
//                System.out.println("tb disability removed" + changedDiseaseRemoved);

            }
        });

        checkBoxDiseaseCancer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisease().contains("कर्करोग")) {


                    if(isChecked) {
                        changedDiseaseAdded.add("कर्करोग");
                        changedDiseaseRemoved.remove("कर्करोग");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {

                        changedDiseaseAdded.remove("कर्करोग");
//                        changedDiseaseRemoved.add("कर्करोग");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {

                        changedDiseaseAdded.remove("कर्करोग");
                        changedDiseaseRemoved.add("कर्करोग");

                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {

//                        changedDiseaseAdded.add("कर्करोग");
                        changedDiseaseRemoved.remove("कर्करोग");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("cancer disability added " + changedDiseaseAdded);
//                System.out.println("cancer disability removed" + changedDiseaseRemoved);

            }
        });

        checkBoxDiseaseLeprosy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisease().contains("कुष्ठरोग")) {

                    if(isChecked) {
                        changedDiseaseAdded.add("कुष्ठरोग");
                        changedDiseaseRemoved.remove("कुष्ठरोग");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDiseaseAdded.remove("कुष्ठरोग");
//                        changedDiseaseRemoved.add("कुष्ठरोग");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDiseaseAdded.remove("कुष्ठरोग");
                        changedDiseaseRemoved.add("कुष्ठरोग");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDiseaseAdded.add("कुष्ठरोग");
                        changedDiseaseRemoved.remove("कुष्ठरोग");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("leprosy disability added " + changedDiseaseAdded);
//                System.out.println("leprosy disability removed" + changedDiseaseRemoved);

            }
        });

        checkBoxDiseaseBp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getDisease().contains("उच्च रक्तदाब")) {

                    if(isChecked) {
                        changedDiseaseAdded.add("उच्च रक्तदाब");
                        changedDiseaseRemoved.remove("उच्च रक्तदाब");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedDiseaseAdded.remove("उच्च रक्तदाब");
//                        changedDiseaseRemoved.add("उच्च रक्तदाब");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedDiseaseAdded.remove("उच्च रक्तदाब");
                        changedDiseaseRemoved.add("उच्च रक्तदाब");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedDiseaseAdded.add("उच्च रक्तदाब");
                        changedDiseaseRemoved.remove("उच्च रक्तदाब");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("leprosy disability added " + changedDiseaseAdded);
//                System.out.println("leprosy disability removed" + changedDiseaseRemoved);

            }
        });

        checkBoxTreatmentHomeopathy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getModality().contains("होमिओपॅथी")) {

                    if(isChecked) {
                        changedModalityAdded.add("होमिओपॅथी");
                        changedModalityRemoved.remove("होमिओपॅथी");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedModalityAdded.remove("होमिओपॅथी");
//                        changedModalityRemoved.add("होमिओपॅथी");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedModalityAdded.remove("होमिओपॅथी");
                        changedModalityRemoved.add("होमिओपॅथी");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedModalityAdded.add("होमिओपॅथी");
                        changedModalityRemoved.remove("होमिओपॅथी");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("homeopathy disability added " + changedModalityAdded);
//                System.out.println("homeopathy disability removed" + changedModalityRemoved);

            }
        });

        checkBoxTreatmentAyurvedic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getModality().contains("आयुर्वेदिक")) {

                    if(isChecked) {
                        changedModalityAdded.add("आयुर्वेदिक");
                        changedModalityRemoved.remove("आयुर्वेदिक");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedModalityAdded.remove("आयुर्वेदिक");
//                        changedModalityRemoved.add("आयुर्वेदिक");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedModalityAdded.remove("आयुर्वेदिक");
                        changedModalityRemoved.add("आयुर्वेदिक");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedModalityAdded.add("आयुर्वेदिक");
                        changedModalityRemoved.remove("आयुर्वेदिक");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("ayurvedic disability added " + changedModalityAdded);
//                System.out.println("ayurvedic disability removed" + changedModalityRemoved);

            }
        });

        checkBoxTreatmentAloepathy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!members.get(selectedMemberIndex).getModality().contains("अ\u200Dॅलोपॅथी")) {

                    if(isChecked) {
                        changedModalityAdded.add("अ\u200Dॅलोपॅथी");
                        changedModalityRemoved.remove("अ\u200Dॅलोपॅथी");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        changedModalityAdded.remove("अ\u200Dॅलोपॅथी");
//                        changedModalityRemoved.add("अ‍ॅलोपॅथी");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }

                } else {
                    if(!isChecked) {
                        changedModalityAdded.remove("अ\u200Dॅलोपॅथी");
                        changedModalityRemoved.add("अ\u200Dॅलोपॅथी");
                        if(saveMemberInformation.getVisibility() == View.GONE) {
                            saveMemberInformation.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        changedModalityAdded.add("अ‍ॅलोपॅथी");
                        changedModalityRemoved.remove("अ\u200Dॅलोपॅथी");
                        if(changedInformation.isEmpty() && changedDisabilityAdded.isEmpty() && changedDiseaseAdded.isEmpty() && changedModalityAdded.isEmpty() && changedDisabilityRemoved.isEmpty() && changedDiseaseRemoved.isEmpty() && changedModalityRemoved.isEmpty()) {
                            saveMemberInformation.setVisibility(View.GONE);
                        }
                    }
                }

//                System.out.println("aloepathy disability added " + changedModalityAdded);
//                System.out.println("aloepathy disability removed" + changedModalityRemoved);

            }
        });


    }

    private void apiCallToAddMember(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Adding New Member ...");

        ObjectId memberId = new ObjectId();
////        System.out.println(memberId.toString());

        String query = "mutation($input: [MembersInput]) { addMembers(id: \"" + objectId + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

        Call<Family> addMemberInformation = apiInterface.addInformation(mutationQuery);

        addMemberInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {


                    apiCallToGetMembers();

                    changedInformation.clear();
                    changedModalityRemoved.clear();
                    changedModalityAdded.clear();
                    changedDiseaseRemoved.clear();
                    changedDiseaseAdded.clear();
                    changedDisabilityRemoved.clear();
                    changedDisabilityAdded.clear();

                    saveMemberInformation.setVisibility(View.GONE);
                    relativeLayoutMemberDetail.setVisibility(View.GONE);
                    clearDataFromLayout();
                    addListenerToComponents();
                    addMember = false;

                    stopProgressBar();
                    showAlertDialog("Member Added Successfully.");

//                    System.out.println(response.body().getData().getAddMembers());

                } else {

//                    System.out.println(response.errorBody());
                    stopProgressBar();
                    showAlertDialog("Error Adding Member.");

                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });

    }

    private void apiCallToUpdateMember(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Updating Details ...");

        String id = members.get(selectedMemberIndex).getMemberId();
        String query = "mutation($input: MembersInput) { updateMembers(memberid: \"" + id + "\", input: $input) { n nModified ok } }";
        mutationQuery.setQuery(query);
//        System.out.println(mutationQuery.getVariables().getInput());
//        System.out.println(mutationQuery.getQuery());
        Call<Family> updateMember = apiInterface.updateInformation(mutationQuery);

        updateMember.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {
                if(response.isSuccessful() && response.body() != null) {
                    stopProgressBar();
                    apiCallToGetMembers();
                    saveMemberInformation.setVisibility(View.GONE);
                    changedInformation.clear();
                    changedModalityRemoved.clear();
                    changedModalityAdded.clear();
                    changedDiseaseRemoved.clear();
                    changedDiseaseAdded.clear();
                    changedDisabilityRemoved.clear();
                    changedDisabilityAdded.clear();
                    showAlertDialog("Data Updated Successfully.");

//                    System.out.println("getN: " + response.body().getData().getUpdateMembers().getN());
                } else {
                    stopProgressBar();
                    showAlertDialog("Error Updating Data");
                }
            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });

    }


    private void apiCallToGetMembers() {

        showProgressBar("Loading Please Wait ...");

        String query = "query { getFamily(_id:\"" + objectId + "\") { members { memberid membername age mobile_no sex addhar_no bank_acc bank_name disease disability_type modality }}}";

        Call<Family> getMembers = apiInterface.getInformation(query);

        getMembers.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    members = response.body().getData().getGetFamily().getMembers();

                    memberDropdownList.clear();
                    memberDropdownList.add("---Select Member---");

                    for(Member member : members) {
                        memberDropdownList.add(member.getMembername());
                    }

                    memberMaterialSpinner.setItems(memberDropdownList);
                    addMemberButton.setVisibility(View.VISIBLE);
                    textViewMember.setVisibility(View.VISIBLE);
                    memberMaterialSpinner.setVisibility(View.VISIBLE);

                    stopProgressBar();


                } else {

                    stopProgressBar();
                    showAlertDialog("No Members Found");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

//                System.out.println(t.getMessage());

            }
        });

    }

    private void setDataToLayout(Member member) {



        int ortho = 0, eye = 0, ear = 0, disabilityOther = 0, heart = 0, diabetes = 0, tuberculosis = 0, cancer = 0 ,leprosy = 0, bp = 0, diseaseOther = 0, homeopathy = 0, aloepathy = 0, ayurvedic = 0, modalityOther = 0;

        checkBoxDisabilityEye.setChecked(false);
        checkBoxDisabilityEar.setChecked(false);
        checkBoxDisabilityOrtho.setChecked(false);
        checkBoxDisabilityOther.setChecked(false);

        checkBoxDiseaseHeart.setChecked(false);
        checkBoxDiseaseDiabetes.setChecked(false);
        checkBoxDiseaseTuberculosis.setChecked(false);
        checkBoxDiseaseCancer.setChecked(false);
        checkBoxDiseaseLeprosy.setChecked(false);
        checkBoxDiseaseOther.setChecked(false);

        checkBoxTreatmentHomeopathy.setChecked(false);
        checkBoxTreatmentAyurvedic.setChecked(false);
        checkBoxTreatmentAloepathy.setChecked(false);
        checkBoxTreatmentOther.setChecked(false);

        editTextMemberName.setText(member.getMembername());
        editTextAge.setText(String.valueOf(member.getAge()));

        if(member.getSex().equalsIgnoreCase("पुरुष")) {
            radioGroupGender.check(R.id.radioMemberGenderMale);
        } else if(member.getSex().equalsIgnoreCase("स्त्री")) {
            radioGroupGender.check(R.id.radioMemberGenderFemale);
        } else if(member.getSex().equalsIgnoreCase("इतर")) {
            radioGroupGender.check(R.id.radioMemberGenderOther);
        }

        editTextAadharNumber.setText(String.valueOf(member.getAddharNo()));
        editTextBankAccountNumber.setText(member.getBankAcc());
        editTextBankName.setText(member.getBankName());
        editTextMobileNumber.setText(String.valueOf(member.getMobile_no()));

        if(!member.getDisabilityType().isEmpty()) {

            for(String disability : member.getDisabilityType()) {
                if(disability.equalsIgnoreCase("ऑर्थो")) {
                    ortho =1;
                    checkBoxDisabilityOrtho.setChecked(true);
                    if(eye != 1) {
                        checkBoxDisabilityEye.setChecked(false);
                    }
                    if(ear != 1) {
                        checkBoxDisabilityEar.setChecked(false);
                    }
                    if(disabilityOther != 1) {
                        checkBoxDisabilityOther.setChecked(false);
                        editTextDisabilityOther.setText("");
                    }



                } else if(disability.equalsIgnoreCase("डोळा")) {
                    eye = 1;
                    checkBoxDisabilityEye.setChecked(true);

                    if(ortho != 1) {
                        checkBoxDisabilityOrtho.setChecked(false);
                    }
                    if(ear != 1) {
                        checkBoxDisabilityEar.setChecked(false);
                    }
                    if(disabilityOther != 1) {
                        checkBoxDisabilityOther.setChecked(false);
                        editTextDisabilityOther.setText("");
                    }

                } else if(disability.equalsIgnoreCase("कान")) {
                    ear = 1;
                    checkBoxDisabilityEar.setChecked(true);

                    if(ortho != 1) {
                        checkBoxDisabilityOrtho.setChecked(false);
                    }
                    if(eye != 1) {
                        checkBoxDisabilityEye.setChecked(false);
                    }
                    if(disabilityOther != 1) {
                        checkBoxDisabilityOther.setChecked(false);
                        editTextDisabilityOther.setText("");
                    }

                } else  {
                    disabilityOther = 1;
                    checkBoxDisabilityOther.setChecked(true);
                    editTextDisabilityOther.setText(disability);
                    if(ortho != 1) {
                        checkBoxDisabilityOrtho.setChecked(false);
                    }
                    if(ear != 1) {
                        checkBoxDisabilityEar.setChecked(false);
                    }
                    if(eye != 1) {
                        checkBoxDisabilityEye.setChecked(false);
                    }
                }
            }
        } else {
            editTextDisabilityOther.setText("");
        }

        if(!member.getDisease().isEmpty()) {

            for(String disease : member.getDisease()) {

                if(disease.equalsIgnoreCase("हृदय रोग")) {

                    heart = 1;
                    checkBoxDiseaseHeart.setChecked(true);

                    if(diabetes != 1) {
                        checkBoxDiseaseDiabetes.setChecked(false);
                    }
                    if(tuberculosis != 1) {
                        checkBoxDiseaseTuberculosis.setChecked(false);
                    }
                    if(cancer != 1) {
                        checkBoxDiseaseCancer.setChecked(false);
                    }
                    if(leprosy != 1) {
                        checkBoxDiseaseLeprosy.setChecked(false);
                    }
                    if(bp != 1) {
                        checkBoxDiseaseBp.setChecked(false);
                    }
                    if(diseaseOther != 1) {
                        checkBoxDiseaseOther.setChecked(false);
                        editTextDiseaseOther.setText("");
                    }

                } else if(disease.equalsIgnoreCase("मधुमेह")) {

                    diabetes = 1;
                    checkBoxDiseaseDiabetes.setChecked(true);

                    if(heart != 1) {
                        checkBoxDiseaseHeart.setChecked(false);
                    }
                    if(tuberculosis != 1) {
                        checkBoxDiseaseTuberculosis.setChecked(false);
                    }
                    if(cancer != 1) {
                        checkBoxDiseaseCancer.setChecked(false);
                    }
                    if(leprosy != 1) {
                        checkBoxDiseaseLeprosy.setChecked(false);
                    }
                    if(bp != 1) {
                        checkBoxDiseaseBp.setChecked(false);
                    }
                    if(diseaseOther != 1) {
                        checkBoxDiseaseOther.setChecked(false);
                        editTextDiseaseOther.setText("");
                    }

                } else if(disease.equalsIgnoreCase("टीबी")) {

                    tuberculosis = 1;
                    checkBoxDiseaseTuberculosis.setChecked(true);

                    if(diabetes != 1) {
                        checkBoxDiseaseDiabetes.setChecked(false);
                    }
                    if(heart != 1) {
                        checkBoxDiseaseHeart.setChecked(false);
                    }
                    if(cancer != 1) {
                        checkBoxDiseaseCancer.setChecked(false);
                    }
                    if(leprosy != 1) {
                        checkBoxDiseaseLeprosy.setChecked(false);
                    }
                    if(bp != 1) {
                        checkBoxDiseaseBp.setChecked(false);
                    }
                    if(diseaseOther != 1) {
                        checkBoxDiseaseOther.setChecked(false);
                        editTextDiseaseOther.setText("");
                    }

                } else if(disease.equalsIgnoreCase("कर्करोग")) {

                    cancer = 1;
                    checkBoxDiseaseCancer.setChecked(true);

                    if(diabetes != 1) {
                        checkBoxDiseaseDiabetes.setChecked(false);
                    }
                    if(tuberculosis != 1) {
                        checkBoxDiseaseTuberculosis.setChecked(false);
                    }
                    if(heart != 1) {
                        checkBoxDiseaseHeart.setChecked(false);
                    }
                    if(leprosy != 1) {
                        checkBoxDiseaseLeprosy.setChecked(false);
                    }
                    if(bp != 1) {
                        checkBoxDiseaseBp.setChecked(false);
                    }
                    if(diseaseOther != 1) {
                        checkBoxDiseaseOther.setChecked(false);
                        editTextDiseaseOther.setText("");
                    }

                } else if(disease.equalsIgnoreCase("कुष्ठरोग")) {

                    leprosy = 1;
                    checkBoxDiseaseLeprosy.setChecked(true);

                    if(diabetes != 1) {
                        checkBoxDiseaseDiabetes.setChecked(false);
                    }
                    if(tuberculosis != 1) {
                        checkBoxDiseaseTuberculosis.setChecked(false);
                    }
                    if(cancer != 1) {
                        checkBoxDiseaseCancer.setChecked(false);
                    }
                    if(heart != 1) {
                        checkBoxDiseaseHeart.setChecked(false);
                    }
                    if(bp != 1) {
                        checkBoxDiseaseBp.setChecked(false);
                    }
                    if(diseaseOther != 1) {
                        checkBoxDiseaseOther.setChecked(false);
                        editTextDiseaseOther.setText("");
                    }

                } else if(disease.equalsIgnoreCase("उच्च रक्तदाब")) {

                    bp = 1;
                    checkBoxDiseaseLeprosy.setChecked(true);

                    if(diabetes != 1) {
                        checkBoxDiseaseDiabetes.setChecked(false);
                    }
                    if(tuberculosis != 1) {
                        checkBoxDiseaseTuberculosis.setChecked(false);
                    }
                    if(cancer != 1) {
                        checkBoxDiseaseCancer.setChecked(false);
                    }
                    if(heart != 1) {
                        checkBoxDiseaseHeart.setChecked(false);
                    }
                    if(leprosy != 1) {
                        checkBoxDiseaseLeprosy.setChecked(false);
                    }
                    if(diseaseOther != 1) {
                        checkBoxDiseaseOther.setChecked(false);
                        editTextDiseaseOther.setText("");
                    }

                } else {
                    diseaseOther = 1;
                    checkBoxDiseaseOther.setChecked(true);
                    editTextDiseaseOther.setText(disease);

                    if(diabetes != 1) {
                        checkBoxDiseaseDiabetes.setChecked(false);
                    }
                    if(tuberculosis != 1) {
                        checkBoxDiseaseTuberculosis.setChecked(false);
                    }
                    if(cancer != 1) {
                        checkBoxDiseaseCancer.setChecked(false);
                    }
                    if(leprosy != 1) {
                        checkBoxDiseaseLeprosy.setChecked(false);
                    }
                    if(bp != 1) {
                        checkBoxDiseaseBp.setChecked(false);
                    }
                    if(heart != 1) {
                        checkBoxDiseaseHeart.setChecked(false);
                    }

                }
            }
        } else {
            editTextDiseaseOther.setText("");
        }

        if(!member.getModality().isEmpty()) {

            for(String modality : member.getModality()) {

                if(modality.equalsIgnoreCase("होमिओपॅथी")) {

                    homeopathy = 1;
                    checkBoxTreatmentHomeopathy.setChecked(true);
                    if(aloepathy != 1) {
                        checkBoxTreatmentAloepathy.setChecked(false);
                    }
                    if(ayurvedic != 1) {
                        checkBoxTreatmentAyurvedic.setChecked(false);
                    }
                    if(modalityOther != 1) {
                        checkBoxTreatmentOther.setChecked(false);
                        editTextTreatmentOther.setText("");
                    }

                } else if(modality.equalsIgnoreCase("अ\u200Dॅलोपॅथी")) {

                    aloepathy = 1;
                    checkBoxTreatmentAloepathy.setChecked(true);
                    if(homeopathy != 1) {
                        checkBoxTreatmentHomeopathy.setChecked(false);
                    }
                    if(ayurvedic != 1) {
                        checkBoxTreatmentAyurvedic.setChecked(false);
                    }
                    if(modalityOther != 1) {
                        checkBoxTreatmentOther.setChecked(false);
                        editTextTreatmentOther.setText("");
                    }

                } else if(modality.equalsIgnoreCase("आयुर्वेदिक")) {

                    ayurvedic = 1;
                    checkBoxTreatmentAyurvedic.setChecked(true);
                    if(aloepathy != 1) {
                        checkBoxTreatmentAloepathy.setChecked(false);
                    }
                    if(homeopathy != 1) {
                        checkBoxTreatmentHomeopathy.setChecked(false);
                    }
                    if(modalityOther != 1) {
                        checkBoxTreatmentOther.setChecked(false);
                        editTextTreatmentOther.setText("");
                    }

                } else {

                    modalityOther = 1;
                    checkBoxTreatmentOther.setChecked(true);
                    editTextTreatmentOther.setText(modality);
                    if(aloepathy != 1) {
                        checkBoxTreatmentAloepathy.setChecked(false);
                    }
                    if(ayurvedic != 1) {
                        checkBoxTreatmentAyurvedic.setChecked(false);
                    }
                    if(homeopathy != 1) {
                        checkBoxTreatmentHomeopathy.setChecked(false);
                    }

                }
            }
        } else {
            editTextTreatmentOther.setText("");
        }

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

    private void showAlertDialog(String message) {
        alertDialogBuilder
                .setTitle("Message")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }

    private void removeListenersFromComponents() {

        memberMaterialSpinner.setOnItemSelectedListener(null);

        editTextMemberName.removeTextChangedListener(textWatcher);
        editTextAge.removeTextChangedListener(textWatcher);
        editTextAadharNumber.removeTextChangedListener(textWatcher);
        editTextBankAccountNumber.removeTextChangedListener(textWatcher);
        editTextBankName.removeTextChangedListener(textWatcher);
        editTextDisabilityOther.removeTextChangedListener(textWatcher);
        editTextTreatmentOther.removeTextChangedListener(textWatcher);
        editTextDiseaseOther.removeTextChangedListener(textWatcher);

        radioGroupGender.setOnCheckedChangeListener(null);

        checkBoxDisabilityOrtho.setOnCheckedChangeListener(null);

        checkBoxDisabilityEye.setOnCheckedChangeListener(null);

        checkBoxDisabilityEar.setOnCheckedChangeListener(null);

        checkBoxDiseaseHeart.setOnCheckedChangeListener(null);

        checkBoxDiseaseDiabetes.setOnCheckedChangeListener(null);

        checkBoxDiseaseTuberculosis.setOnCheckedChangeListener(null);

        checkBoxDiseaseCancer.setOnCheckedChangeListener(null);

        checkBoxDiseaseLeprosy.setOnCheckedChangeListener(null);

        checkBoxTreatmentHomeopathy.setOnCheckedChangeListener(null);

        checkBoxTreatmentAyurvedic.setOnCheckedChangeListener(null);

        checkBoxTreatmentAloepathy.setOnCheckedChangeListener(null);

    }

    private  void clearDataFromLayout() {

        editTextMemberName.getText().clear();
        editTextAge.getText().clear();
        editTextMobileNumber.getText().clear();
        editTextAadharNumber.getText().clear();
        editTextBankAccountNumber.getText().clear();
        editTextBankName.getText().clear();
        editTextDisabilityOther.getText().clear();
        editTextDiseaseOther.getText().clear();
        editTextTreatmentOther.getText().clear();
        radioGroupGender.clearCheck();
        checkBoxDisabilityOrtho.setChecked(false);
        checkBoxDisabilityEye.setChecked(false);
        checkBoxDisabilityEar.setChecked(false);
        checkBoxDisabilityOther.setChecked(false);
        checkBoxDiseaseHeart.setChecked(false);
        checkBoxDiseaseDiabetes.setChecked(false);
        checkBoxDiseaseTuberculosis.setChecked(false);
        checkBoxDiseaseCancer.setChecked(false);
        checkBoxDiseaseLeprosy.setChecked(false);
        checkBoxDiseaseOther.setChecked(false);
        checkBoxTreatmentHomeopathy.setChecked(false);
        checkBoxTreatmentAloepathy.setChecked(false);
        checkBoxTreatmentAyurvedic.setChecked(false);
        checkBoxTreatmentOther.setChecked(false);

    }

    private void addValidation() {
        basicValidation.addValidation(editTextMemberName, RegexTemplate.NOT_EMPTY, "नाव प्रविष्ट करा");
        basicValidation.addValidation(editTextAge, RegexTemplate.NOT_EMPTY, "वय प्रविष्ट करा");
//        disabilityOtherValidation.addValidation(editTextDisabilityOther,RegexTemplate.NOT_EMPTY, "इतर अपंगत्व प्रविष्ट करा");
//        diseaseOtherValidation.addValidation(editTextDiseaseOther, RegexTemplate.NOT_EMPTY, "इतर रोग प्रविष्ट करा");
//        treatmentOtherValidation.addValidation(editTextTreatmentOther, RegexTemplate.NOT_EMPTY, "इतर उपचार पद्धती प्रविष्ट करा");
    }

}
