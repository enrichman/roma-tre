package com.roma3.infovideo.utility.lessons;

import android.util.Log;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class LessonsDownloadingException extends Exception {

    public LessonsDownloadingException(String message, Exception e) {
        super(message, e);
        Log.e("LEZIONI ROMA TRE", message);
    }

}
