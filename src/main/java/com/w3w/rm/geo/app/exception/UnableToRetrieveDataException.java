package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;

public class UnableToRetrieveDataException extends RuntimeException {

    public UnableToRetrieveDataException(String word, String language) {
        super(ApplicationUtil.UNABLE_TO_FETCH_AUTO_SUGGEST_FOR.apply(word, language));
    }
}
