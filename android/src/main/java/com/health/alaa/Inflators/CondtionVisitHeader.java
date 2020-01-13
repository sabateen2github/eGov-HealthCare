package com.health.alaa.Inflators;

import android.view.View;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionVisitHeader {

    private View view;

    private TextView ordinaryHeaderTitle;
    private TextView ordinaryHeaderActivationHeader;
    private TextView ordinaryHeaderActivationFrom;
    private TextView ordinaryHeaderActivationTo;
    private TextView ordinaryHeaderPurposeHeader;
    private TextView ordinaryHeaderPurpose;

    public TextView getOrdinaryHeaderTitle() {
        return ordinaryHeaderTitle;
    }

    public TextView getOrdinaryHeaderActivationHeader() {
        return ordinaryHeaderActivationHeader;
    }

    public TextView getOrdinaryHeaderActivationFrom() {
        return ordinaryHeaderActivationFrom;
    }

    public TextView getOrdinaryHeaderActivationTo() {
        return ordinaryHeaderActivationTo;
    }

    public TextView getOrdinaryHeaderPurposeHeader() {
        return ordinaryHeaderPurposeHeader;
    }

    public TextView getOrdinaryHeaderPurpose() {
        return ordinaryHeaderPurpose;
    }

    public void bind()
    {

        ordinaryHeaderTitle = (TextView) view.findViewById(R.id.ordinary_header_title);
        ordinaryHeaderActivationHeader = (TextView) view.findViewById(R.id.ordinary_header_activation_header);
        ordinaryHeaderActivationFrom = (TextView) view.findViewById(R.id.ordinary_header_activation_from);
        ordinaryHeaderActivationTo = (TextView) view.findViewById(R.id.ordinary_header_activation_to);
        ordinaryHeaderPurposeHeader = (TextView) view.findViewById(R.id.ordinary_header_purpose_header);
        ordinaryHeaderPurpose = (TextView) view.findViewById(R.id.ordinary_header_purpose);


    }
}
