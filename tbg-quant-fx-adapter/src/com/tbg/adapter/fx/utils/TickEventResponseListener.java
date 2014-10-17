package com.tbg.adapter.fx.utils;

import org.apache.log4j.Logger;
import org.bushe.swing.event.EventBus;

import com.fxcore2.IO2GResponseListener;
import com.fxcore2.O2GOfferRow;
import com.fxcore2.O2GOffersTableResponseReader;
import com.fxcore2.O2GResponse;
import com.fxcore2.O2GResponseReaderFactory;
import com.fxcore2.O2GResponseType;
import com.fxcore2.O2GSession;
import com.tbg.fx.event.types.FXTickEvent;

public class TickEventResponseListener implements IO2GResponseListener {
    // Session, response and request variables
    private O2GSession mSession = null;
    private String mInstrument = "";
    private O2GResponse mResponse = null; 
    private String mRequest = "";
    
    protected static final Logger log = Logger.getLogger(TickEventResponseListener.class);

    // Constructor
    public TickEventResponseListener(O2GSession session, String instrument) {
        mSession = session;
        mInstrument = instrument;   
    }
    
    public O2GSession getmSession() {
		return mSession;
	}

	public void setmSession(O2GSession mSession) {
		this.mSession = mSession;
	}

	public String getmInstrument() {
		return mInstrument;
	}

	public void setmInstrument(String mInstrument) {
		this.mInstrument = mInstrument;
	}

	public O2GResponse getmResponse() {
		return mResponse;
	}

	public void setmResponse(O2GResponse mResponse) {
		this.mResponse = mResponse;
	}

	public String getmRequest() {
		return mRequest;
	}

	public void setmRequest(String mRequest) {
		this.mRequest = mRequest;
	}

	// Implementation of IO2GResponseListener interface public method onRequestCompleted
    public void onRequestCompleted(String requestID, O2GResponse response) {
        if (response.getType() == O2GResponseType.GET_OFFERS) {
            mResponse = response;
            mRequest = "getoffers"; 
            publishOffers(mSession, mResponse, mInstrument);    
        }
    }

    // Implementation of IO2GResponseListener interface public method onRequestFailed
    public void onRequestFailed(String requestID, String error) {
        log.error("Request failed. requestID= " + requestID + "; error= " + error);
    }

    // Implementation of IO2GResponseListener interface public method onTablesUpdates
    public void onTablesUpdates(O2GResponse response) {
        if (response.getType() == O2GResponseType.TABLES_UPDATES) {
            mResponse = response;
            mRequest = "tablesupdates";         
            publishOffers(mSession, mResponse, mInstrument); 
        }
    }
    
    // Prints response to a user request and live offer updates
    public void publishOffers(O2GSession session, O2GResponse response, String instrument) {
        O2GResponseReaderFactory readerFactory = session.getResponseReaderFactory();
        if (readerFactory != null) {
            O2GOffersTableResponseReader reader = readerFactory.createOffersTableReader(response);
            for (int i = 0; i < reader.size(); i++) {       
                O2GOfferRow row = reader.getRow(i);
                if (row.isBidValid() && row.isAskValid()) {
                    if (row.getInstrument().equals(instrument)) {
                    	EventBus.publish(new FXTickEvent(row));
                        mRequest = "";                 
                        break;                      
                    }
                }                          
            }
        }   
    }
}
