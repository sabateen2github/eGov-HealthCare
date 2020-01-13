package com.health.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.health.alaa.Inflators.SignInLayout;
import com.health.objects.CheckAccountIdPass;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.R;
import com.health.project.entry.Util;


public class SignInFragment extends Fragment implements View.OnClickListener {


    SignInLayout sign_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        sign_layout = new SignInLayout(inflater, parent);
        sign_layout.getSignInButton().setOnClickListener(this);
        sign_layout.getSignInWrong().setVisibility(View.GONE);
        sign_layout.getProgress().setVisibility(View.GONE);
        sign_layout.getSignInButton().setVisibility(View.VISIBLE);


        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.rootlayout, parent, false);

        layout.addView(sign_layout.getSignInLayout());
        return layout;
    }


    @Override
    public void onViewCreated(View v, Bundle save) {
        super.onViewCreated(v, save);
        if (save == null) {
            String Pass;
            long id;
            Pass = Application.context.getSharedPreferences("Pass.id", Context.MODE_PRIVATE).getString(Strings.PASS, null);
            if (Pass != null) {
                id = Application.context.getSharedPreferences("Pass.id", Context.MODE_PRIVATE).getLong(Strings.ID, 0);
                ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_id)).setText(id + "");
                ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_pass)).setText(Pass);
                onClick(sign_layout.getSignInButton());
            }
        }

    }


    @Override
    public void onClick(View view) {


        try {
            final long ID = Long.parseLong(((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_id)).getText().toString());
            final String Pass = ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_pass)).getText().toString();


            Util.safeBeginDelayedTranstion(sign_layout.getSignInLayout());
            sign_layout.getSignInButton().setVisibility(View.GONE);
            sign_layout.getProgress().setVisibility(View.VISIBLE);
            ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_id)).setEnabled(false);
            ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_pass)).setEnabled(false);


            sign_layout.getSignInWrong().setVisibility(View.GONE);
            sign_layout.getProgress().setVisibility(View.VISIBLE);
            sign_layout.getSignInButton().setVisibility(View.GONE);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Application.let_pass = CheckAccountIdPass.CheckPassID(ID, Pass, Types.Acc_Type_Patient);


                    Application.main_handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Application.let_pass) {
                                Application.Patient_ID = ID;
                                Application.Pass = Pass;
                                Application.context.getSharedPreferences("Pass.id", Context.MODE_PRIVATE).edit().putString(Strings.PASS, Application.Pass).putLong(Strings.ID, Application.Patient_ID).commit();
                            }
                            onAcoountChecked();
                        }
                    });
                }
            });


        } catch (Exception e) {


        }
    }


    public void onAcoountChecked() {


        if (getActivity() == null) return;
        if (Application.let_pass) {
            //Fragment Transaction


            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root_layout, new MainFragment(), Strings.MAIN_FRAGMENT).commit();
        } else {
            Util.safeBeginDelayedTranstion(sign_layout.getSignInLayout());

            sign_layout.getSignInWrong().setVisibility(View.VISIBLE);
            sign_layout.getProgress().setVisibility(View.GONE);
            sign_layout.getSignInButton().setVisibility(View.VISIBLE);

            ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_id)).getText().clear();
            ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_pass)).getText().clear();
            ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_id)).setEnabled(true);
            ((EditText) sign_layout.getSignInLayout().findViewById(R.id.sign_in_pass)).setEnabled(true);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
    }

}
