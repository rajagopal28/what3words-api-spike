package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;
/*
 * MissingRequiredDataException is thrown at RunTime when the given data request has one more more required fields missing.
 *
 * @author Rajagopal
 * */
public class MissingRequiredDataException extends RuntimeException {
    /*
     * Constructor to Create a Generic MissingRequiredDataException instance for an invalid request object.
     *
     * */
    public MissingRequiredDataException() {
        this(ApplicationUtil.ERROR_INVALID_REQUEST);
    }

    /*
     * Constructor to Create the MissingRequiredDataException when the given data request has one more more required fields missing.
     *
     * @param message related to the validation which creates the data validation to fail.
     * */
    public MissingRequiredDataException(String message) {
        super(message);
    }
}
