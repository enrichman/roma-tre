package com.roma3.infovideo.utility;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import com.roma3.infovideo.R;


/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class TitleColorManager {

    public static void white(String faculty, TextView textView, Activity activity) {
        String[] whiteFaculties = activity.getResources().getStringArray(R.array.whiteLogo);
        for (String s : whiteFaculties) {
            if (s.equals(faculty)) {
                textView.setTextColor(Color.WHITE);
//                ImageView image = (ImageView) activity.findViewById(R.id.titleLogo);
//                image.setImageResource(R.drawable.title_logo_white);
            }
        }
    }

}
