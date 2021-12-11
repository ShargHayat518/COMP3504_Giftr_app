package com.example.giftr_app;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramViewHolder {

    ImageView itemImage;
    TextView recommend;
    TextView cost;
    TextView url;



    ProgramViewHolder(View v){

        itemImage = (ImageView) v.findViewById(R.id.icon);
        url = v.findViewById(R.id.url);
        url.setMovementMethod(LinkMovementMethod.getInstance());
        recommend = v.findViewById(R.id.linkView);
        cost = v.findViewById(R.id.priceView);

    }
}

