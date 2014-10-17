package com.tbg.adapter.fx.utils;

import org.apache.log4j.Logger;

import com.fxcore2.IO2GResponseListener;
import com.fxcore2.O2GResponse;

public class CandleEventResponseListener implements IO2GResponseListener {
    //Response , log and error variables
    private boolean mError = false;
    private O2GResponse mResponse = null;
    
    protected static final Logger log = Logger.getLogger(CandleEventResponseListener.class);
    
    // Gets Response
    public O2GResponse getResponse() {
        return mResponse;
    }
    
    // Shows if there was an error during the request processing
    public final boolean hasError() {
        return mError;
    }
    
    // Implementation of IO2GResponseListener interface public method onRequestCompleted
    public void onRequestCompleted(String requestID, O2GResponse response) {
        mResponse = response;
        mError = false;
    }

    // Implementation of IO2GResponseListener interface public method onRequestFailed
    public void onRequestFailed(String requestID, String error) {
        mError = true;
    }

    // Implementation of IO2GResponseListener interface public method onTablesUpdates
    public void onTablesUpdates(O2GResponse response) {
    }   
    
}