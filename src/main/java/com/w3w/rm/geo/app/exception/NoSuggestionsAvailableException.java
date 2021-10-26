package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;
/*
 * NoSuggestionsAvailableException is thrown at RunTime when the given 3wa string does not have a fetch any suggestions.
 *
 * @author Rajagopal
 * */
public class NoSuggestionsAvailableException extends RuntimeException {
    /*
     * Constructor to Create the NoSuggestionsAvailableException instance for a given 3wa.
     *
     * */
    public NoSuggestionsAvailableException() {
        super(ApplicationUtil.ERROR_NO_SUGGESTION_AVAILABLE);
    };
}
