package com.w3w.rm.geo.app.config;

import com.what3words.javawrapper.What3WordsV3;
import org.springframework.stereotype.Component;

/**
 * What3WordsWrapper helps split the What3WordsV3 instance creation from the application lifecycle. <br/>
 * This enables decoupling of the component creation, encourages single responsibility, robust testing and unwraps the benefits of DependencyInversion. <br/>
 * @author Rajagopal
 */
@Component()
public class What3WordsWrapper {
    // Eager initialisation of the singleton instance of What3WordsV3 API
    private static What3WordsV3 API_INSTANCE = new What3WordsV3("API-KEY-HERE");

    /**
     * Private constructor to block instantiation of the Wrapper class.
     */
    private What3WordsWrapper() {
        // singleton blocking
    }

    /**
     * Method getInstance is static and gives access to the underlying What3WordsV3 API instance created and managed under this wrapper.
     * @return What3WordsV3 instant that is created
     */
    public What3WordsV3 getInstance() {
        return API_INSTANCE;
    }
}
