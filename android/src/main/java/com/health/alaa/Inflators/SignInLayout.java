package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.project.entry.R;

public class SignInLayout  {


    private ConstraintLayout signInLayout;
    private TextView signInPassHeader;
    private TextView signInIdHeader;

    public ConstraintLayout getSignInLayout() {
        return signInLayout;
    }

    public TextView getSignInPassHeader() {
        return signInPassHeader;
    }

    public TextView getSignInIdHeader() {
        return signInIdHeader;
    }

    public TextView getSignInTitle() {
        return signInTitle;
    }

    public ImageView getSignInImage() {
        return signInImage;
    }

    public TextView getSignInWrong() {
        return signInWrong;
    }

    public Button getSignInButton() {
        return signInButton;
    }

    private TextView signInTitle;
    private ImageView signInImage;
    private TextView signInWrong;
    private Button signInButton;

    public View getProgress() {
        return Progress;
    }

    private View Progress;

    private View view;

    public  SignInLayout(LayoutInflater inflater, ViewGroup container) {
        view= inflater.inflate(R.layout.sign_in_layout, container,false);
        view.setTag(this);
        bind();
    }
    public void bind() {

        signInLayout = (ConstraintLayout) view.findViewById(R.id.sign_in_layout);
        signInButton=(Button) view.findViewById(R.id.sign_in_button);
        signInPassHeader = (TextView) view.findViewById(R.id.sign_in_pass_header);
        signInIdHeader = (TextView) view.findViewById(R.id.sign_in_id_header);
        signInTitle = (TextView) view.findViewById(R.id.sign_in_title);
        signInImage = (ImageView) view.findViewById(R.id.sign_in_image);
        signInWrong = (TextView) view.findViewById(R.id.sign_in_wrong);
        Progress=view.findViewById(R.id.sign_in_progress);
    }

}
