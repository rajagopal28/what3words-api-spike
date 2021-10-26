package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;
/*
 * UnableToRetrieveDataException is thrown at RunTime when there is no response for any What3WordsV3 API call failed.
 *
 * @author Rajagopal
 * */
public class UnableToRetrieveDataException extends RuntimeException {
    /*
     * Constructor to Create the UnableToRetrieveDataException instance for the 3wa and language for which any What3WordsV3 API call failed.
     *
     * @param word which was validated and found to be invalid.
     * @param language in which the What3WordsV3 API call is invoked
     * */
    public UnableToRetrieveDataException(String word, String language) {
        super(ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply(word, language));
    }
}
