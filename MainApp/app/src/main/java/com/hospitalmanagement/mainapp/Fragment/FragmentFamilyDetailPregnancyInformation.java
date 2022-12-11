package com.hospitalmanagement.mainapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.hospitalmanagement.mainapp.R;
import com.hospitalmanagement.mainapp.pojo.Family;
import com.hospitalmanagement.mainapp.pojo.Member;
import com.hospitalmanagement.mainapp.pojo.MutationQueryFamilyDetail;
import com.hospitalmanagement.mainapp.pojo.Pregnancy;
import com.hospitalmanagement.mainapp.pojo.UpdateResponse;
import com.hospitalmanagement.mainapp.pojo.Variables;
import com.hospitalmanagement.mainapp.retrofitService.apiClient.ApiClient;
import com.hospitalmanagement.mainapp.retrofitService.apiInterface.ApiInterface;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFamilyDetailPregnancyInformation extends Fragment {

    private List<Pregnancy> pregnancies;
    private List<String> pregnancyDropDownList;
    private List<Member>  members;
    private List<String> memberNamesList;
    private HashMap<String, Object> changedInformation;
    private String objectId;
    private boolean addPregnancy = false;

    private TextWatcher textWatcher;
    private Bundle bundle;
    private Handler handler;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private ApiInterface apiInterface;
    private Calendar calendar;
    private int day, month, year, dateFlag = -1;
    private  DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private AwesomeValidation pregnantValidation;
    private String date;

    private int selectedPregnantWomanIndex, selectedMemberPregnantIndex = -1;

    private MaterialSpinner pregnantWomenSpinner;
    private RelativeLayout relativeLayoutPregnancyDetail;
    private TextInputEditText editTextPara;
    private TextInputEditText editTextPregnantWomanName;
    private TextInputEditText editTextComplicationsPreviousPregnancy;
    private TextInputEditText editTextLastMenstrualDate;
    private TextInputEditText editTextExpectedDateDelivery;
    private TextInputEditText editTextExpectedPlaceDelivery;
    private RadioGroup radioGroupPregnancyType;
    private TextInputEditText editTextPregnancyTypeOther;
    private RadioGroup radioGroupOutcomeOfPregnancy;
    private TextInputEditText editTextPmmvyRegistrationDate;
    private TextInputEditText editTextPmmvySixMonthVisitDate;
    private TextInputEditText editTextPmmvyPenta3Date;
    private TextInputEditText editTextJsyPaidAmount;
    private TextInputEditText editTextJsyBenefitDate;
    private MaterialButton savePregnancyInformation;
    private MaterialButton addPregnancyButton;
    private TextView textViewPregnantWoman, newTextViewPregnantWoman;
    private NestedScrollView nestedScrollViewPregnancy;
    private LinearLayout newPregnancySpinnerLinearLayout;
    private MaterialSpinner newPregnantWomanSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bundle = this.getArguments();

        if(bundle != null) {
            objectId = bundle.getString("objectId");
        }

        inflater = LayoutInflater.from(new ContextThemeWrapper(getContext(), R.style.AppTheme));

        return inflater.inflate(R.layout.familydetail_pregnancyinformation, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        pregnantWomenSpinner = view.findViewById(R.id.pregnantNameMultiSpinner);
        relativeLayoutPregnancyDetail = view.findViewById(R.id.pregnantDetailViewLayout);
        editTextPara = view.findViewById(R.id.editParaValue);
//        editTextPregnantWomanName = view.findViewById(R.id.editPregnantName);
        editTextComplicationsPreviousPregnancy = view.findViewById(R.id.editComplicationsPreviousPregnancy);
        editTextLastMenstrualDate = view.findViewById(R.id.editLastMenstrualDate);
        editTextExpectedDateDelivery = view.findViewById(R.id.editExpectedDateDelivery);
        editTextExpectedPlaceDelivery = view.findViewById(R.id.editExpectedPlaceDelivery);
        radioGroupPregnancyType = view.findViewById(R.id.radioPregnancyTypeRadioGroup);
        editTextPregnancyTypeOther = view.findViewById(R.id.editPregnancyTypeOther);
        radioGroupOutcomeOfPregnancy = view.findViewById(R.id.radioGroupOutcomeOfPregnancy);
        editTextPmmvyRegistrationDate = view.findViewById(R.id.editPmmvyRegistrationDate);
        editTextPmmvySixMonthVisitDate = view.findViewById(R.id.editPmmvySixMonthVisitDate);
        editTextPmmvyPenta3Date = view.findViewById(R.id.editPmmvyPenta3Date);
        editTextJsyPaidAmount = view.findViewById(R.id.editJsyPaidAmt);
        editTextJsyBenefitDate = view.findViewById(R.id.editJsyBenefitDate);
        savePregnancyInformation = view.findViewById(R.id.btnSavePregnancyInformation);
        addPregnancyButton = view.findViewById(R.id.btnAddPregnancy);
        textViewPregnantWoman = view.findViewById(R.id.textPregnantWoman);
        nestedScrollViewPregnancy = view.findViewById(R.id.pregnancyInformationNestedScrollView);
        newPregnancySpinnerLinearLayout = view.findViewById(R.id.newPregnantSpinnerLinearLayout);
        newPregnantWomanSpinner = view.findViewById(R.id.newPregnantNameMultiSpinner);
        newTextViewPregnantWoman = view.findViewById(R.id.newTextPregnantWoman);


        //Addvalidation
        pregnantValidation = new AwesomeValidation(ValidationStyle.BASIC);

        pregnancyDropDownList = new ArrayList<>();
        changedInformation = new HashMap<>();
        progressDialog = new ProgressDialog(getContext());
        handler = new Handler();
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        memberNamesList = new ArrayList<>();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                date = dayOfMonth + "/" + (month + 1) + "/" + year;

                if(dateFlag == 0) {
                    editTextLastMenstrualDate.setText(date);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar1.set(Calendar.MONTH, month);
                    calendar1.set(Calendar.YEAR, year);
                    calendar1.add(Calendar.MONTH, 9);
                    calendar1.add(Calendar.DAY_OF_MONTH, 7);
                    String date1 =
                            calendar1.get(Calendar.DAY_OF_MONTH)+"/"+calendar1.get(Calendar.MONTH)+"/"+calendar1.get(Calendar.YEAR);
                    editTextExpectedDateDelivery.setText(date1);
                } else if(dateFlag == 1) {
                    editTextExpectedDateDelivery.setText(date);
                } else if(dateFlag == 2) {
                    editTextPmmvyRegistrationDate.setText(date);
                } else if(dateFlag == 3) {
                    editTextPmmvySixMonthVisitDate.setText(date);
                } else if(dateFlag == 4) {
                    editTextPmmvyPenta3Date.setText(date);
                } else if(dateFlag == 5) {
                    editTextJsyBenefitDate.setText(date);
                }

            }
        };

        datePickerDialog = new DatePickerDialog(getContext(), R.style.Theme_MaterialComponents_Light_Dialog_MinWidth, onDateSetListener, year, month, day);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s != null) {


                    if (s.hashCode() == editTextPara.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getPara() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getPara().toString())) {

                                changedInformation.put("para", editTextPara.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("para"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("para");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextPara.getText().toString().trim().length() != 0) {
                                changedInformation.put("para", editTextPara.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("para"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }
//                    else if (s.hashCode() == editTextPregnantWomanName.getText().hashCode()) {
//
//                        if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getName())) {
//
//                            changedInformation.put("name", editTextPregnantWomanName.getText().toString());
////                            System.out.println("Changes " + changedInformation.get("name"));
//
//                            if (savePregnancyInformation.getVisibility() == View.GONE) {
//                                savePregnancyInformation.setVisibility(View.VISIBLE);
//                            }
//
//                        } else {
//
//                            changedInformation.remove("name");
//                            if (changedInformation.isEmpty()) {
//                                savePregnancyInformation.setVisibility(View.GONE);
//                            }
//
//                        }
//
//                    }
                    else if (s.hashCode() == editTextComplicationsPreviousPregnancy.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getComplicationPreviousPregnancy() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getComplicationPreviousPregnancy())) {

                                changedInformation.put("complicationPreviousPregnancy", editTextComplicationsPreviousPregnancy.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("complicationPreviousPregnancy"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("complicationPreviousPregnancy");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextComplicationsPreviousPregnancy.getText().toString().trim().length() != 0) {
                                changedInformation.put("complicationPreviousPregnancy", editTextComplicationsPreviousPregnancy.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("complicationPreviousPregnancy"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextLastMenstrualDate.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getLastMenstrualDate() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getLastMenstrualDate().toString())) {

                                changedInformation.put("lastMenstrualDate", editTextLastMenstrualDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("lastMenstrualDate"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("lastMenstrualDate");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextLastMenstrualDate.getText().toString().trim().length() != 0) {
                                changedInformation.put("lastMenstrualDate", editTextLastMenstrualDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("lastMenstrualDate"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextExpectedDateDelivery.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getExpectedDateDelivery() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getExpectedDateDelivery().toString())) {

                                changedInformation.put("expectedDateDelivery", editTextExpectedDateDelivery.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("expectedDateDelivery"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("expectedDateDelivery");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextExpectedDateDelivery.getText().toString().trim().length() != 0) {
                                changedInformation.put("expectedDateDelivery", editTextExpectedDateDelivery.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("expectedDateDelivery"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextExpectedPlaceDelivery.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getExpectedPlaceDelivery() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getExpectedPlaceDelivery())) {

                                changedInformation.put("expectedPlaceDelivery", editTextExpectedPlaceDelivery.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("expectedPlaceDelivery"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("expectedPlaceDelivery");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextExpectedPlaceDelivery.getText().toString().trim().length() != 0) {
                                changedInformation.put("expectedPlaceDelivery", editTextExpectedPlaceDelivery.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("expectedPlaceDelivery"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextPregnancyTypeOther.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getDelivery() != null && pregnancies.get(selectedPregnantWomanIndex).getDelivery().getTypeOfPregnancy() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getDelivery().getTypeOfPregnancy())) {

                                changedInformation.put("pregnancyTypeOther", editTextPregnancyTypeOther.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("pregnancyTypeOther"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("pregnancyTypeOther");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextPregnancyTypeOther.getText().toString().trim().length() != 0) {
                                changedInformation.put("pregnancyTypeOther", editTextPregnancyTypeOther.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("pregnancyTypeOther"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextPmmvyRegistrationDate.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getPMMVY() != null && pregnancies.get(selectedPregnantWomanIndex).getPMMVY().getRegistrationDate() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getPMMVY().getRegistrationDate().toString())) {

                                changedInformation.put("registrationDate", editTextPmmvyRegistrationDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("registrationDate"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("registrationDate");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextPmmvyRegistrationDate.getText().toString().trim().length() != 0) {
                                changedInformation.put("registrationDate", editTextPmmvyRegistrationDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("registrationDate"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextPmmvySixMonthVisitDate.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getPMMVY() != null && pregnancies.get(selectedPregnantWomanIndex).getPMMVY().getSixmonthVisit() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getPMMVY().getSixmonthVisit().toString())) {

                                changedInformation.put("sixmonthVisit", editTextPmmvySixMonthVisitDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("sixmonthVisit"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("sixmonthVisit");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextPmmvySixMonthVisitDate.getText().toString().trim().length() != 0) {
                                changedInformation.put("sixmonthVisit", editTextPmmvySixMonthVisitDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("sixmonthVisit"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextPmmvyPenta3Date.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getPMMVY() != null && pregnancies.get(selectedPregnantWomanIndex).getPMMVY().getPenta3date() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getPMMVY().getPenta3date().toString())) {

                                changedInformation.put("Penta3date", editTextPmmvyPenta3Date.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("Penta3date"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("Penta3date");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextPmmvyPenta3Date.getText().toString().trim().length() != 0) {
                                changedInformation.put("Penta3date", editTextPmmvyPenta3Date.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("Penta3date"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextJsyPaidAmount.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getJSY() != null && pregnancies.get(selectedPregnantWomanIndex).getJSY().getPaidAmount() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getJSY().getPaidAmount().toString())) {

                                changedInformation.put("paidAmount", editTextJsyPaidAmount.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("paidAmount"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("paidAmount");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextJsyPaidAmount.getText().toString().trim().length() != 0) {
                                changedInformation.put("paidAmount", editTextJsyPaidAmount.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("paidAmount"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    } else if (s.hashCode() == editTextJsyBenefitDate.getText().hashCode()) {

                        if(pregnancies.get(selectedPregnantWomanIndex).getJSY() != null && pregnancies.get(selectedPregnantWomanIndex).getJSY().getBenefitDate() != null) {

                            if (!s.toString().equals(pregnancies.get(selectedPregnantWomanIndex).getJSY().getBenefitDate().toString())) {

                                changedInformation.put("benefitDate", editTextJsyBenefitDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("benefitDate"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }

                            } else {

                                changedInformation.remove("benefitDate");
                                if (changedInformation.isEmpty()) {
                                    savePregnancyInformation.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            if(editTextJsyBenefitDate.getText().toString().trim().length() != 0) {
                                changedInformation.put("benefitDate", editTextJsyBenefitDate.getText().toString());
//                                System.out.println("Changes " + changedInformation.get("benefitDate"));

                                if (savePregnancyInformation.getVisibility() == View.GONE) {
                                    savePregnancyInformation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                    }

                }

            }
        };


        editTextLastMenstrualDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 0;

                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        editTextExpectedDateDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 1;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        editTextPmmvyRegistrationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 2;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        editTextPmmvySixMonthVisitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 3;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        editTextPmmvyPenta3Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 4;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });

        editTextJsyBenefitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateFlag = 5;
                datePickerDialog.getWindow();
                datePickerDialog.show();

            }
        });


        savePregnancyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Variables variables = new Variables();
                MutationQueryFamilyDetail mutationQuery = new MutationQueryFamilyDetail();

                if(pregnantValidation.validate()) {
                    if (!addPregnancy) {

                        HashMap<String, Object> JSY = new HashMap<>();
                        HashMap<String, Object> PMMVY = new HashMap<>();
                        HashMap<String, Object> delivery = new HashMap<>();

                        if (changedInformation.get("para") != null) {
                            changedInformation.put("para", Integer.parseInt(changedInformation.get("para").toString()));
                        }

                        if (changedInformation.get("lastMenstrualDate") != null) {
                            changedInformation.put("lastMenstrualDate", changedInformation.get("lastMenstrualDate").toString());
                        }

                        if (changedInformation.get("expectedDateDelivery") != null) {
                            changedInformation.put("expectedDateDelivery", changedInformation.get("expectedDateDelivery").toString());
                        }

                        if (changedInformation.get("registrationDate") != null) {
                            changedInformation.put("registrationDate", changedInformation.get("registrationDate").toString());
                        }

                        if (changedInformation.get("sixmonthVisit") != null) {
                            changedInformation.put("sixmonthVisit", changedInformation.get("sixmonthVisit").toString());
                        }

                        if (changedInformation.get("Penta3date") != null) {
                            changedInformation.put("Penta3date", changedInformation.get("Penta3date").toString());
                        }

                        if (changedInformation.get("paidAmount") != null) {
                            changedInformation.put("paidAmount", changedInformation.get("paidAmount").toString());
                        }

                        if (changedInformation.get("benefitDate") != null) {
                            changedInformation.put("benefitDate", changedInformation.get("benefitDate").toString());
                        }

                        if (changedInformation.get("pregnancyTypeOther") != null) {
                            changedInformation.put("typeOfPregnancy", changedInformation.get("pregnancyTypeOther"));
                        }


                        if (changedInformation.get("paidAmount") != null || changedInformation.get("benefitDate") != null) {

                            if (changedInformation.get("paidAmount") != null) {
                                JSY.put("paidAmount", changedInformation.get("paidAmount"));
                                changedInformation.remove("paidAmount");
                            }

                            if (changedInformation.get("benefitDate") != null) {
                                JSY.put("benefitDate", changedInformation.get("benefitDate"));
                                changedInformation.remove("benefitDate");
                            }

                            changedInformation.put("JSY", JSY);
                        }

                        if (changedInformation.get("registrationDate") != null || changedInformation.get("sixmonthVisit") != null || changedInformation.get("Penta3date") != null) {

                            if (changedInformation.get("registrationDate") != null) {
                                PMMVY.put("registrationDate", changedInformation.get("registrationDate"));
                                changedInformation.remove("registrationDate");
                            }

                            if (changedInformation.get("sixmonthVisit") != null) {
                                PMMVY.put("sixmonthVisit", changedInformation.get("sixmonthVisit"));
                                changedInformation.remove("sixmonthVisit");
                            }

                            if (changedInformation.get("Penta3date") != null) {
                                PMMVY.put("Penta3date", changedInformation.get("Penta3date"));
                                changedInformation.remove("Penta3date");
                            }

                            changedInformation.put("PMMVY", PMMVY);
                        }

                        if (changedInformation.get("typeOfPregnancy") != null || changedInformation.get("outcomeOfPregnancy") != null) {

                            if (changedInformation.get("typeOfPregnancy") != null) {
                                delivery.put("typeOfPregnancy", changedInformation.get("typeOfPregnancy"));
                                changedInformation.remove("typeOfPregnancy");
                            }

                            if (changedInformation.get("outcomeOfPregnancy") != null) {
                                delivery.put("outcomeOfPregnancy", changedInformation.get("outcomeOfPregnancy"));
                                changedInformation.remove("outcomeOfPregnancy");
                            }

                            changedInformation.put("delivery", delivery);

                        }

                        variables.setInput(changedInformation);

                        mutationQuery.setVariables(variables);

                        apiCallToUpdatePregnancyInformation(mutationQuery);


                    } else {

                        //add pregnancy

                        HashMap<String, Object> information = new HashMap<>();
                        HashMap<String, Object> JSY = new HashMap<>();
                        HashMap<String, Object> PMMVY = new HashMap<>();
                        HashMap<String, Object> delivery = new HashMap<>();

                        if (memberNamesList.get(selectedMemberPregnantIndex) != null) {
                            information.put("name", memberNamesList.get(selectedMemberPregnantIndex));
                        }

                        if (editTextPara.getText().toString().trim().length() != 0) {
                            information.put("para", Integer.parseInt(editTextPara.getText().toString()));
                        }

                        if (editTextComplicationsPreviousPregnancy.getText().toString().trim().length() != 0) {
                            information.put("complicationPreviousPregnancy", editTextComplicationsPreviousPregnancy.getText().toString());
                        }

                        if (editTextLastMenstrualDate.getText().toString().trim().length() != 0) {
                            information.put("lastMenstrualDate", editTextLastMenstrualDate.getText().toString());
                        }

                        if (editTextExpectedDateDelivery.getText().toString().trim().length() != 0) {
                            information.put("expectedDateDelivery", editTextExpectedDateDelivery.getText().toString());
                        }

                        if (editTextExpectedPlaceDelivery.getText().toString().trim().length() != 0) {
                            information.put("expectedPlaceDelivery", editTextExpectedPlaceDelivery.getText().toString());
                        }


                        if (editTextJsyPaidAmount.getText().toString().trim().length() != 0 || editTextJsyBenefitDate.getText().toString().trim().length() != 0) {

                            if (editTextJsyPaidAmount.getText().toString().trim().length() != 0) {
                                JSY.put("paidAmount", Integer.parseInt(editTextJsyPaidAmount.getText().toString()));

                            }

                            if (editTextJsyBenefitDate.getText().toString().trim().length() != 0) {
                                JSY.put("benefitDate", editTextJsyBenefitDate.getText().toString());

                            }

                            information.put("JSY", JSY);
                        }

                        if (editTextPmmvyRegistrationDate.getText().toString().trim().length() != 0 || editTextPmmvySixMonthVisitDate.getText().toString().trim().length() != 0 || editTextPmmvyPenta3Date.getText().toString().trim().length() != 0) {

                            if (editTextPmmvyRegistrationDate.getText().toString().trim().length() != 0) {
                                PMMVY.put("registrationDate", editTextPmmvyRegistrationDate.getText().toString());

                            }

                            if (editTextPmmvySixMonthVisitDate.getText().toString().trim().length() != 0) {
                                PMMVY.put("sixmonthVisit", editTextPmmvySixMonthVisitDate.getText().toString());

                            }

                            if (editTextPmmvyPenta3Date.getText().toString().trim().length() != 0) {
                                PMMVY.put("Penta3date", editTextPmmvyPenta3Date.getText().toString());

                            }

                            information.put("PMMVY", PMMVY);
                        }


                        if (radioGroupPregnancyType.getCheckedRadioButtonId() != -1 || radioGroupOutcomeOfPregnancy.getCheckedRadioButtonId() != -1) {

                            if (radioGroupPregnancyType.getCheckedRadioButtonId() != -1) {
                                if (radioGroupPregnancyType.getCheckedRadioButtonId() == R.id.radioButtonPregnancyTypeNormal) {
                                    delivery.put("typeOfPregnancy", "सामान्य प्रसूती");
                                } else if (radioGroupPregnancyType.getCheckedRadioButtonId() == R.id.radioButtonPregnancyTypeLSCS) {
                                    delivery.put("typeOfPregnancy", "सिझरियन प्रसूती");
                                } else if (radioGroupPregnancyType.getCheckedRadioButtonId() == R.id.radioButtonPregnancyTypeOther) {
                                    delivery.put("typeOfPregnancy", editTextPregnancyTypeOther.getText().toString());
                                }

                            }

                            if (radioGroupOutcomeOfPregnancy.getCheckedRadioButtonId() != -1) {
                                if (radioGroupOutcomeOfPregnancy.getCheckedRadioButtonId() == R.id.radioButtonOutcomeMale) {
                                    delivery.put("outcomeOfPregnancy", "पुरुष");
                                } else if (radioGroupOutcomeOfPregnancy.getCheckedRadioButtonId() == R.id.radioButtonOutcomeFemale) {
                                    delivery.put("outcomeOfPregnancy", "स्त्री");
                                } else if (radioGroupOutcomeOfPregnancy.getCheckedRadioButtonId() == R.id.radioButtonOutcomeOther) {
                                    delivery.put("outcomeOfPregnancy", "इतर");
                                }
                            }

                            information.put("delivery", delivery);

                        }


                        List<HashMap<String, Object>> input = new ArrayList<>();

                        information.put("pregnancyId", new ObjectId().toString());

                        input.add(information);

                        variables.setInput(input);
                        mutationQuery.setVariables(variables);

                        apiCallToAddPregnancyInformation(mutationQuery);

                    }
                }

            }
        });


        addPregnancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPregnancy = true;
                relativeLayoutPregnancyDetail.setVisibility(View.GONE);
                savePregnancyInformation.setVisibility(View.VISIBLE);
                textViewPregnantWoman.setVisibility(View.GONE);
                pregnantWomenSpinner.setVisibility(View.GONE);
                newTextViewPregnantWoman.setVisibility(View.VISIBLE);
                newPregnancySpinnerLinearLayout.setVisibility(View.VISIBLE);

                changedInformation.clear();
                clearDataFromLayout();
                removeListenersFromComponents();

                apiCallToGetMemberNames();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relativeLayoutPregnancyDetail.setVisibility(View.VISIBLE);
                    }
                }, 500);

                nestedScrollViewPregnancy.scrollTo(0, 0);

            }
        });



        addValidation();
        addListenerToComponents();
        apiCallToGetPregnancyInformation();


    }

    private void addListenerToComponents() {



        addPregnancyButton.setVisibility(View.VISIBLE);
        textViewPregnantWoman.setVisibility(View.VISIBLE);
        pregnantWomenSpinner.setVisibility(View.VISIBLE);



        editTextPara.addTextChangedListener(textWatcher);
//        editTextPregnantWomanName.addTextChangedListener(textWatcher);
        editTextComplicationsPreviousPregnancy.addTextChangedListener(textWatcher);
        editTextLastMenstrualDate.addTextChangedListener(textWatcher);
        editTextExpectedDateDelivery.addTextChangedListener(textWatcher);
        editTextExpectedPlaceDelivery.addTextChangedListener(textWatcher);
        editTextPregnancyTypeOther.addTextChangedListener(textWatcher);
        editTextPmmvyRegistrationDate.addTextChangedListener(textWatcher);
        editTextPmmvySixMonthVisitDate.addTextChangedListener(textWatcher);
        editTextPmmvyPenta3Date.addTextChangedListener(textWatcher);
        editTextJsyPaidAmount.addTextChangedListener(textWatcher);
        editTextJsyBenefitDate.addTextChangedListener(textWatcher);

        newPregnantWomanSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position != 0) {

                    selectedMemberPregnantIndex = position;

                } else {
                    selectedMemberPregnantIndex = -1;
                }

            }
        });

        pregnantWomenSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                relativeLayoutPregnancyDetail.setVisibility(View.GONE);


                if(position != 0) {

                    selectedPregnantWomanIndex = position - 1;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            relativeLayoutPregnancyDetail.setVisibility(View.VISIBLE);
                            nestedScrollViewPregnancy.scrollTo(0, 0);
                        }
                    }, 500);


                    setDataToLayout(pregnancies.get(position - 1));

                    changedInformation.clear();
                    savePregnancyInformation.setVisibility(View.GONE);
                }

            }
        });


        radioGroupOutcomeOfPregnancy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseOutcomeId = -1;


                if(pregnancies.get(selectedPregnantWomanIndex).getDelivery() != null && pregnancies.get(selectedPregnantWomanIndex).getDelivery().getOutcomeOfPregnancy() != null) {

                    if (pregnancies.get(selectedPregnantWomanIndex).getDelivery().getOutcomeOfPregnancy().equalsIgnoreCase("पुरुष")) {
                        responseOutcomeId = R.id.radioButtonOutcomeMale;
                    } else if (pregnancies.get(selectedPregnantWomanIndex).getDelivery().getOutcomeOfPregnancy().equalsIgnoreCase("स्त्री")) {
                        responseOutcomeId = R.id.radioButtonOutcomeFemale;
                    } else if (pregnancies.get(selectedPregnantWomanIndex).getDelivery().getOutcomeOfPregnancy().equalsIgnoreCase("इतर")) {
                        responseOutcomeId = R.id.radioButtonOutcomeOther;
                    }
                }


                switch(checkedId) {

                    case R.id.radioButtonOutcomeMale:
                        if(responseOutcomeId == R.id.radioButtonOutcomeMale) {

                            changedInformation.remove("outcomeOfPregnancy");
//                            System.out.println(changedInformation.get("outcomeOfPregnancy"));

                            if(changedInformation.isEmpty()) {
                                savePregnancyInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(savePregnancyInformation.getVisibility() == View.GONE) {
                                savePregnancyInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("outcomeOfPregnancy", "पुरुष");
                        }
                        break;

                    case R.id.radioButtonOutcomeFemale:
                        if(responseOutcomeId == R.id.radioButtonOutcomeFemale) {
                            changedInformation.remove("outcomeOfPregnancy");

                            if(changedInformation.isEmpty()) {
                                savePregnancyInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(savePregnancyInformation.getVisibility() == View.GONE) {
                                savePregnancyInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("outcomeOfPregnancy", "स्त्री");
                        }
                        break;

                    case R.id.radioButtonOutcomeOther:
                        if(responseOutcomeId == R.id.radioButtonOutcomeOther) {
                            changedInformation.remove("outcomeOfPregnancy");

                            if(changedInformation.isEmpty()) {
                                savePregnancyInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(savePregnancyInformation.getVisibility() == View.GONE) {
                                savePregnancyInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("outcomeOfPregnancy", "इतर");
                        }
                        break;

                }

//                System.out.println("gender " + changedInformation.get("outcomeOfPregnancy"));
            }
        });


        radioGroupPregnancyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int responseTypeId = -1;

                if(pregnancies.get(selectedPregnantWomanIndex).getDelivery() != null && pregnancies.get(selectedPregnantWomanIndex).getDelivery().getTypeOfPregnancy() != null) {
                    if (pregnancies.get(selectedPregnantWomanIndex).getDelivery().getTypeOfPregnancy().equalsIgnoreCase("सिझरियन प्रसूती")) {
                        responseTypeId = R.id.radioButtonPregnancyTypeLSCS;
                    } else if (pregnancies.get(selectedPregnantWomanIndex).getDelivery().getTypeOfPregnancy().equalsIgnoreCase("सामान्य प्रसूती")) {
                        responseTypeId = R.id.radioButtonPregnancyTypeNormal;
                    } else {
                        responseTypeId = R.id.radioButtonPregnancyTypeOther;
                    }
                }



                switch(checkedId) {

                    case R.id.radioButtonPregnancyTypeLSCS:
                        if(responseTypeId == R.id.radioButtonPregnancyTypeLSCS) {

                            changedInformation.remove("typeOfPregnancy");
//                            System.out.println(changedInformation.get("typeOfPregnancy"));

                            if(changedInformation.isEmpty()) {
                                savePregnancyInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(savePregnancyInformation.getVisibility() == View.GONE) {
                                savePregnancyInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("typeOfPregnancy", "सिझरियन प्रसूती");
                        }
                        break;

                    case R.id.radioButtonPregnancyTypeNormal:
                        if(responseTypeId == R.id.radioButtonPregnancyTypeNormal) {
                            changedInformation.remove("typeOfPregnancy");

                            if(changedInformation.isEmpty()) {
                                savePregnancyInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(savePregnancyInformation.getVisibility() == View.GONE) {
                                savePregnancyInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("typeOfPregnancy", "सामान्य प्रसूती");
                        }
                        break;

                    case R.id.radioButtonPregnancyTypeOther:
                        if(responseTypeId == R.id.radioButtonPregnancyTypeOther) {
                            changedInformation.remove("typeOfPregnancy");

                            if(changedInformation.isEmpty()) {
                                savePregnancyInformation.setVisibility(View.GONE);
                            }

                        } else {

                            if(savePregnancyInformation.getVisibility() == View.GONE) {
                                savePregnancyInformation.setVisibility(View.VISIBLE);
                            }

                            changedInformation.put("typeOfPregnancy", "इतर");
                        }
                        break;

                }

//                System.out.println("typeOfPregnancy " + changedInformation.get("typeOfPregnancy"));
            }
        });




    }

    private void apiCallToAddPregnancyInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Adding New Pregnancy ...");

        String query = "mutation($input: [PregnancyInput]) { addPregnancy(id: \"" + objectId + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

        Call<Family> addPregnancyInformation = apiInterface.addInformation(mutationQuery);

        addPregnancyInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    apiCallToGetPregnancyInformation();

                    changedInformation.clear();

                    savePregnancyInformation.setVisibility(View.GONE);
                    relativeLayoutPregnancyDetail.setVisibility(View.GONE);

                    textViewPregnantWoman.setVisibility(View.VISIBLE);
                    pregnantWomenSpinner.setVisibility(View.VISIBLE);

                    newTextViewPregnantWoman.setVisibility(View.GONE);
                    newPregnantWomanSpinner.setVisibility(View.GONE);


                    clearDataFromLayout();
                    addListenerToComponents();
                    addPregnancy = false;

                    stopProgressBar();
                    showAlertDialog("Pregnancy Added Successfully.");

                } else {

                    stopProgressBar();
                    showAlertDialog("Error Adding Pregnancy.");

                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

            }
        });

    }

    private void apiCallToUpdatePregnancyInformation(MutationQueryFamilyDetail mutationQuery) {

        showProgressBar("Updating Pregnancy Data ...");

//        System.out.println(pregnancies.get(selectedPregnantWomanIndex).getPregnancyId());
        String query = "mutation($input: PregnancyInput) { updatePregnancy(pregnancyId: \"" + pregnancies.get(selectedPregnantWomanIndex).getPregnancyId() + "\", input: $input) { n nModified ok } }";

        mutationQuery.setQuery(query);

        Call<Family> updatePregnancyInformation = apiInterface.updateInformation(mutationQuery);

        updatePregnancyInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    stopProgressBar();
                    apiCallToGetPregnancyInformation();
                    savePregnancyInformation.setVisibility(View.GONE);
                    changedInformation.clear();
                    showAlertDialog("Data Updated Successfully");

                } else {
                    stopProgressBar();
                    showAlertDialog("Error Updating Data.");
                }

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {

//                System.out.println(t.getMessage());

            }
        });

    }

    private void apiCallToGetPregnancyInformation() {

        showProgressBar("Loading Please Wait ...");

        String query = "query { getFamily(_id: \"" + objectId + "\") { pregnancy { pregnancyId para name complicationPreviousPregnancy lastMenstrualDate expectedDateDelivery expectedPlaceDelivery delivery { typeOfPregnancy outcomeOfPregnancy } PMMVY { registrationDate sixmonthVisit Penta3date } JSY { paidAmount benefitDate }}}}";

//        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<Family> getPregancyInformation = apiInterface.getInformation(query);

        getPregancyInformation.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {


                    pregnancies = response.body().getData().getGetFamily().getPregnancy();

                    pregnancyDropDownList.clear();
                    pregnancyDropDownList.add("---Select Pregnant Woman---");
                    for(Pregnancy pregnancy : pregnancies) {
                        if(pregnancy.getName() != null) {
                            pregnancyDropDownList.add(pregnancy.getName());
                        }
                    }

//                    System.out.println(pregnancyDropDownList);
                    pregnantWomenSpinner.setItems(pregnancyDropDownList);
                    stopProgressBar();
                }
                else {
                    stopProgressBar();
                    showAlertDialog("Not Found");
                }

//                System.out.println(response.message());

            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
//                System.out.println(t.getMessage());
            }
        });

    }

    private void apiCallToGetMemberNames() {

        showProgressBar("Loading Please Wait ...");

        String query = "query { getFamily(_id:\"" + objectId + "\") { members { memberid membername sex }}}";

        Call<Family> getMembers = apiInterface.getInformation(query);

        getMembers.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {

                if(response.isSuccessful() && response.body() != null) {

                    members = response.body().getData().getGetFamily().getMembers();


                    memberNamesList.clear();
                    memberNamesList.add("---Select Member---");

                    for(Member member : members) {
                        if(member.getMembername() != null && member.getSex() != null && member.getSex().equalsIgnoreCase("स्त्री")) {
                            memberNamesList.add(member.getMembername());
                        }
                    }


                    newPregnantWomanSpinner.setItems(memberNamesList);


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

    private void setDataToLayout(Pregnancy pregnancy) {

        editTextPara.setText(String.valueOf(pregnancy.getPara()));
//        editTextPregnantWomanName.setText(pregnancy.getName());
        editTextComplicationsPreviousPregnancy.setText(pregnancy.getComplicationPreviousPregnancy());
        editTextLastMenstrualDate.setText(String.valueOf(pregnancy.getLastMenstrualDate()));
        editTextExpectedDateDelivery.setText(String.valueOf(pregnancy.getExpectedDateDelivery()));
        editTextExpectedPlaceDelivery.setText(pregnancy.getExpectedPlaceDelivery());

        if(pregnancy.getDelivery() != null) {
            if (pregnancy.getDelivery().getTypeOfPregnancy() != null) {

                if (pregnancy.getDelivery().getTypeOfPregnancy().equalsIgnoreCase("सामान्य प्रसूती")) {
                    radioGroupPregnancyType.check(R.id.radioButtonPregnancyTypeNormal);
                } else if (pregnancy.getDelivery().getTypeOfPregnancy().equalsIgnoreCase("सिझरियन प्रसूती")) {
                    radioGroupPregnancyType.check(R.id.radioButtonPregnancyTypeLSCS);
                } else {
                    radioGroupPregnancyType.check(R.id.radioButtonPregnancyTypeOther);
                    editTextPregnancyTypeOther.setText(pregnancy.getDelivery().getTypeOfPregnancy());
                }
            }

            if (pregnancy.getDelivery().getOutcomeOfPregnancy() != null) {

                if (pregnancy.getDelivery().getOutcomeOfPregnancy().equalsIgnoreCase("पुरुष")) {
                    radioGroupOutcomeOfPregnancy.check(R.id.radioButtonOutcomeMale);
                } else if (pregnancy.getDelivery().getOutcomeOfPregnancy().equalsIgnoreCase("स्त्री")) {
                    radioGroupOutcomeOfPregnancy.check(R.id.radioButtonOutcomeFemale);
                } else if (pregnancy.getDelivery().getOutcomeOfPregnancy().equalsIgnoreCase("इतर")) {
                    radioGroupOutcomeOfPregnancy.check(R.id.radioButtonOutcomeOther);
                }
            }
        }

        if(pregnancy.getPMMVY() != null) {
            if(pregnancy.getPMMVY().getRegistrationDate() != null) {
                editTextPmmvyRegistrationDate.setText(String.valueOf(pregnancy.getPMMVY().getRegistrationDate()));
            }
            if(pregnancy.getPMMVY().getSixmonthVisit() != null) {
                editTextPmmvySixMonthVisitDate.setText(String.valueOf(pregnancy.getPMMVY().getSixmonthVisit()));
            }
            if(pregnancy.getPMMVY().getPenta3date() != null) {
                editTextPmmvyPenta3Date.setText(String.valueOf(pregnancy.getPMMVY().getPenta3date()));
            }
        }

        if(pregnancy.getJSY() != null) {
            if(pregnancy.getJSY().getPaidAmount() != null) {
                editTextJsyPaidAmount.setText(String.valueOf(pregnancy.getJSY().getPaidAmount()));
            }
            if(pregnancy.getJSY().getBenefitDate() != null) {
                editTextJsyBenefitDate.setText(String.valueOf(pregnancy.getJSY().getBenefitDate()));
            }
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

        editTextPara.removeTextChangedListener(textWatcher);
//        editTextPregnantWomanName.addTextChangedListener(null);
        editTextComplicationsPreviousPregnancy.removeTextChangedListener(textWatcher);
        editTextLastMenstrualDate.removeTextChangedListener(textWatcher);
        editTextExpectedDateDelivery.removeTextChangedListener(textWatcher);
        editTextExpectedPlaceDelivery.removeTextChangedListener(textWatcher);
        editTextPregnancyTypeOther.removeTextChangedListener(textWatcher);
        editTextPmmvyRegistrationDate.removeTextChangedListener(textWatcher);
        editTextPmmvySixMonthVisitDate.removeTextChangedListener(textWatcher);
        editTextPmmvyPenta3Date.removeTextChangedListener(textWatcher);
        editTextJsyPaidAmount.removeTextChangedListener(textWatcher);
        editTextJsyBenefitDate.removeTextChangedListener(textWatcher);

//        newPregnantWomanSpinner.setOnItemSelectedListener(null);

        pregnantWomenSpinner.setOnItemSelectedListener(null);


        radioGroupOutcomeOfPregnancy.setOnCheckedChangeListener(null);


        radioGroupPregnancyType.setOnCheckedChangeListener(null);

    }

    private void clearDataFromLayout() {

        editTextPara.getText().clear();
//        editTextPregnantWomanName.getText().clear();
        editTextComplicationsPreviousPregnancy.getText().clear();
        editTextLastMenstrualDate.getText().clear();
        editTextExpectedDateDelivery.getText().clear();
        editTextExpectedPlaceDelivery.getText().clear();
        radioGroupPregnancyType.clearCheck();
        editTextPregnancyTypeOther.getText().clear();
        radioGroupOutcomeOfPregnancy.clearCheck();
        editTextPmmvyRegistrationDate.getText().clear();
        editTextPmmvySixMonthVisitDate.getText().clear();
        editTextPmmvyPenta3Date.getText().clear();
        editTextJsyPaidAmount.getText().clear();
        editTextJsyBenefitDate.getText().clear();


        radioGroupOutcomeOfPregnancy.setOnCheckedChangeListener(null);

    }

    private void addValidation() {
        pregnantValidation.addValidation(editTextPara, RegexTemplate.NOT_EMPTY, "कृपया para प्रविष्ट " +
                "करा");
        pregnantValidation.addValidation(editTextExpectedPlaceDelivery, RegexTemplate.NOT_EMPTY, "कृपया अपेक्षित वितरण स्थान प्रविष्ट करा");
    }

}
