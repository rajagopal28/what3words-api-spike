package com.w3w.rm.geo.app.exception;

import com.w3w.rm.geo.app.util.ApplicationUtil;

public class Invalid3WaWordException extends RuntimeException{

    public Invalid3WaWordException(String word) {
        super(ApplicationUtil.NOT_RECOGNIZED_3WA_ERROR_FN.apply(word));
    }
}
