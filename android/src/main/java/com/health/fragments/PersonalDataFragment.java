package com.health.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.health.alaa.Inflators.PersonalDataItem;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;

public class PersonalDataFragment extends Fragment implements DataChangedListener {


    private static final int Header_Type=-199925;


    private FrameLayout f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        Application.registerListener(this);


        f = new FrameLayout(inflater.getContext());

        if (Application.is_initialized) {
            onPersonalDataInit();
        } else {

            ProgressBar bar = new ProgressBar(inflater.getContext());
            bar.setIndeterminate(true);
            f.addView(bar, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }

        return f;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        Application.unRegisterListener(this);
    }


    @Override
    public void onNotificationsChanged() {

    }

    @Override
    public void onAttachedProcedureChanged() {

    }

    @Override
    public void onCondtionsChanged() {

    }

    @Override
    public void onProceduresChanged() {

    }

    @Override
    public void onBillsChanged() {

    }

    @Override
    public void onPersonalDataChanged() {
        onPersonalDataInit();
    }

    @Override
    public void onNotificationInit() {

    }

    @Override
    public void onAttachedProcedureInit() {

    }

    @Override
    public void onCondtionsInit() {

    }

    @Override
    public void onProceduresInit() {

    }

    @Override
    public void onBillsInit() {

    }

    @Override
    public void onPersonalDataInit() {

        Util.safeBeginDelayedTranstion(f);
        if (f == null) return;
        f.removeAllViews();
        View container = getLayoutInflater().inflate(R.layout.personal_data_layout, f, false);
        PersonalDataItem name = new PersonalDataItem(container.findViewById(R.id.personal_name));
        PersonalDataItem gender = new PersonalDataItem(container.findViewById(R.id.personal_gender));
        PersonalDataItem phone = new PersonalDataItem(container.findViewById(R.id.personal_phone));
        PersonalDataItem height = new PersonalDataItem(container.findViewById(R.id.personal_height));
        PersonalDataItem weight = new PersonalDataItem(container.findViewById(R.id.personal_weight));
        PersonalDataItem birth = new PersonalDataItem(container.findViewById(R.id.personal_birth));
        PersonalDataItem address = new PersonalDataItem(container.findViewById(R.id.perosnl_address));
        PersonalDataItem insurance = new PersonalDataItem(container.findViewById(R.id.perosnl_insurance));
        PersonalDataItem lang = new PersonalDataItem(container.findViewById(R.id.perosnl_lang));


        name.getPersonal_btn().setVisibility(View.GONE);
        gender.getPersonal_btn().setVisibility(View.GONE);
        height.getPersonal_btn().setVisibility(View.GONE);
        weight.getPersonal_btn().setVisibility(View.GONE);
        birth.getPersonal_btn().setVisibility(View.GONE);
        address.getPersonal_btn().setVisibility(View.GONE);
        insurance.getPersonal_btn().setVisibility(View.GONE);
        phone.getPersonal_btn().setVisibility(View.GONE);
        lang.getPersonal_btn().setVisibility(View.GONE);

        name.getPersonalItemHeader().setText(Strings.NAME);
        gender.getPersonalItemHeader().setText(Strings.GENDER);
        height.getPersonalItemHeader().setText(Strings.HEIGHT);
        weight.getPersonalItemHeader().setText(Strings.WEIGHT);
        birth.getPersonalItemHeader().setText(Strings.BIRTH);
        address.getPersonalItemHeader().setText(Strings.ADDRESS);
        insurance.getPersonalItemHeader().setText(Strings.INSUARNCE);
        lang.getPersonalItemHeader().setText(Strings.LANGUAGE);
        phone.getPersonalItemHeader().setText(Strings.PHONE);

        name.getPersonalItemData().setText(Application.personalData.name);
        gender.getPersonalItemData().setText(Application.personalData.gender);
        height.getPersonalItemData().setText(Util.convertStringToAr(Application.personalData.height + Strings.CM));
        weight.getPersonalItemData().setText(Util.convertStringToAr(Application.personalData.weight + Strings.KG));
        birth.getPersonalItemData().setText(Application.personalData.date_of_birth);
        address.getPersonalItemData().setText(Application.personalData.address);
        insurance.getPersonalItemData().setText(Application.personalData.insurance_path);
        lang.getPersonalItemData().setText(Application.lang == Types.Lang_English ? Strings.ENGLISH : Strings.Arabic);
        phone.getPersonalItemData().setText(Application.personalData.phone);
        f.addView(container);
    }
}
