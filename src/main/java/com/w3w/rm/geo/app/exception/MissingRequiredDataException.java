package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;

public class MissingRequiredDataException extends RuntimeException {
    public MissingRequiredDataException() {
        this(ApplicationUtil.ERROR_INVALID_REQUEST);
    }

    public MissingRequiredDataException(String message) {
        super(message);
    }
}
