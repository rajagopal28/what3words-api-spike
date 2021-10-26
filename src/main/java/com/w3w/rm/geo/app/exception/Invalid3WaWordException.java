package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;

/*
* Invalid3WaWordException is thrown at RunTime when the given 3wa string does not have a valid 3wa signature
*
* @author Rajagopal
* */
public class Invalid3WaWordException extends RuntimeException{

    /*
    * Constructor to Create the Invalid3WaWordException instance for the wrong word.
    *
    * @param word which was validated and found to be invalid.
    * */
    public Invalid3WaWordException(String word) {
        super(ApplicationUtil.NOT_RECOGNIZED_3WA_ERROR_FN.apply(word));
    }
}
