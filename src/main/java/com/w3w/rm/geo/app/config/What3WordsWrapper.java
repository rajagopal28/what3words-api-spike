package com.w3w.rm.geo.app.config;

import com.what3words.javawrapper.What3WordsV3;
import org.springframework.stereotype.Component;


@Component()
public class What3WordsWrapper {
    private static What3WordsV3 API_INSTANCE = new What3WordsV3("92RKJSSS");
    private What3WordsWrapper() {
        // singleton blocking
    }

    public What3WordsV3 getInstance() {
        return API_INSTANCE;
    }
}
