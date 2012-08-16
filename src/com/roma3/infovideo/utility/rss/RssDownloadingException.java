package com.roma3.infovideo.utility.rss;

/**
 * Version 1.2
 * Copyright (C) 2012 Enrico Candino ( enrico.candino@gmail.com )
 *
 * This file is part of "Roma Tre".
 * "Roma Tre" is released under the General Public Licence V.3 or later
 *
 * @author Enrico Candino
 */
public class RssDownloadingException extends Exception {

    public RssDownloadingException(String message, Exception e) {
        super(message, e);
    }

}
