package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;

public class NoSuggestionsAvailableException extends RuntimeException {
    public NoSuggestionsAvailableException() {
        super(ApplicationUtil.ERROR_NO_SUGGESTION_AVAILABLE);
    };
}
