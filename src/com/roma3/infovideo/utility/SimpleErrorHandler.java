package com.roma3.infovideo.utility;

import android.util.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class SimpleErrorHandler implements ErrorHandler {
    @Override
    public void warning(SAXParseException e) throws SAXException {
        Log.e("LEZIONI ROMA TRE", e.getMessage());
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        Log.e("LEZIONI ROMA TRE", e.getMessage());
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        Log.e("LEZIONI ROMA TRE", e.getMessage());
    }
}
