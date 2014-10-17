package com.tbg.adapter.fx.marketdatafeed;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bushe.swing.event.EventBus;

import com.fxcore2.O2GMarketDataSnapshotResponseReader;
import com.fxcore2.O2GRequest;
import com.fxcore2.O2GRequestFactory;
import com.fxcore2.O2GResponse;
import com.fxcore2.O2GResponseReaderFactory;
import com.fxcore2.O2GSession;
import com.fxcore2.O2GTableType;
import com.fxcore2.O2GTimeConverter;
import com.fxcore2.O2GTimeConverterTimeZone;
import com.fxcore2.O2GTimeframe;
import com.fxcore2.O2GTimeframeCollection;
import com.fxcore2.O2GTransport;
import com.tbg.adapter.fx.utils.CandleEventResponseListener;
import com.tbg.adapter.fx.utils.SessionStatusListener;
import com.tbg.adapter.fx.utils.TickEventResponseListener;
import com.tbg.core.model.Security;
import com.tbg.core.model.alarm.IAlarmService;
import com.tbg.core.model.marketDataFeed.IMarketDataFeed;
import com.tbg.core.model.types.MarketDataEventType;
import com.tbg.fx.event.types.FXCandleEvent;
import com.tbg.fx.event.types.FXTickEvent;

public class FXMarketDataFeed implements IMarketDataFeed {
	private int marketDataFeedId;
	private MarketDataEventType marketDataEventType;
	public long SLEEP_TIME = 0L;
	protected boolean debug = false;
	private String mUserID = "";
	private String mPassword = "";
	private String mURL = "";
	private String mConnection = "";
	private String mDBName = "";
	private String mPin = "";
	
	private String mTimeFrame = "";
	private String mDateFrom = "";
	private String mDateTo = "";
	boolean mContinue = true;
    private Calendar calFrom = null;
    private Calendar calTo = null;
    private Calendar calFirst = null;
    private Calendar calDate = null;
    private int mReaderSize = 0;
    private int mCounter = 0;
    private int mBorder = 0;
    private static int mMaxBars = 300;
    
	private O2GSession mSession = null;
	private SessionStatusListener statusListener = null;
	private ArrayList<TickEventResponseListener> tickResponseListener = new ArrayList<TickEventResponseListener>();
	private CandleEventResponseListener candleResponseListener = new CandleEventResponseListener();
	private ArrayList<String> mInstrument = new ArrayList<String>();

	public boolean FORCE_SYNCHRONIZATION = false;
	protected Map<Security, ArrayList<FXCandleEvent>> candleEvents = new HashMap<Security, ArrayList<FXCandleEvent>>();
	protected Map<Security, ArrayList<FXTickEvent>> tickEvents = new HashMap<Security, ArrayList<FXTickEvent>>();
	
	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	protected static final Logger log = Logger.getLogger(FXMarketDataFeed.class);
	 
	public void setFXParameters(String mUserID, String mPassword, String mURL, String mConnection, String mDBName,
			String mPin) {
		this.mUserID = mUserID;
		this.mPassword = mPassword;
		this.mURL = mURL;
		this.mConnection = mConnection;
		this.mDBName = mDBName;
		this.mPin = mPin;
	}
	
	public void setFXParameters(String mUserID, String mPassword, String mURL, String mConnection, String mDBName,
			String mPin, String timeFrame, String dateFrom, String dateTo) {
		this.mUserID = mUserID;
		this.mPassword = mPassword;
		this.mURL = mURL;
		this.mConnection = mConnection;
		this.mDBName = mDBName;
		this.mPin = mPin;
		this.mTimeFrame = timeFrame;
		this.mDateFrom = dateFrom;
		this.mDateTo = dateTo;
		checkDateFormat();
	}
	
	public void setFXDemoParameters(String mUserID, String mPassword) {
		this.mUserID = mUserID;
		this.mPassword = mPassword;
		this.mURL = "http://www.fxcorporate.com/Hosts.jsp";
		this.mConnection = "Demo";
	}
	
	public void setFXDemoParameters(String mUserID, String mPassword, String timeFrame, String dateFrom, String dateTo) {
		this.mUserID = mUserID;
		this.mPassword = mPassword;
		this.mURL = "http://www.fxcorporate.com/Hosts.jsp";
		this.mConnection = "Demo";
		this.mTimeFrame = timeFrame;
		this.mDateFrom = dateFrom;
		this.mDateTo = dateTo;
		checkDateFormat();
	}

	@Override
	public void activeMarketDataSubscription() {
		// Retrieve data from the data provider (Tick and Candle)
		O2GRequestFactory requestFactory = mSession.getRequestFactory();
		if(this.marketDataEventType.equals(MarketDataEventType.TICK_EVENT)) {
	        if (!mInstrument.equals("{INSTRUMENT}") && !mInstrument.equals("")) {
	            if (requestFactory != null)
	                mSession.sendRequest(requestFactory.createRefreshTableRequest(O2GTableType.OFFERS));
	        } 
		} else if(this.marketDataEventType.equals(MarketDataEventType.CANDLE_EVENT)) {
			O2GTimeframeCollection timeFrames = requestFactory.getTimeFrameCollection();
            O2GTimeframe timeFrame = timeFrames.get(mTimeFrame);
            if(timeFrame == null) {
               log.error("You specified an invalid time frame.");
               mContinue = false;
            }
            
            if (mContinue) {
            	for(String instrument : mInstrument) {
            		O2GRequest request = requestFactory.createMarketDataSnapshotRequestInstrument(instrument, 
            				timeFrame, mMaxBars);
            		if(request == null) {
            			log.error("Cannot create request for market data snapshot.");
            			mContinue = false;
            		}
            	    
            		if (mContinue) {
            			do {
            				requestFactory.fillMarketDataSnapshotRequestTime(request, calFrom, calTo, false);
            				mSession.sendRequest(request);
            				try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
            				
            				O2GResponse response = candleResponseListener.getResponse();
            				if (response != null) {
            					O2GResponseReaderFactory responseFactory = mSession.getResponseReaderFactory();
            					O2GMarketDataSnapshotResponseReader reader = responseFactory.createMarketDataSnapshotReader(response);
            					mReaderSize = reader.size();
            					// check if we need additional request
            					if ((calFrom == null) || (calTo == null) || (mReaderSize < mMaxBars))
            						mBorder = 0;
            					else {
            						if (mCounter > 0) {
            							if (mTimeFrame.equals("m1"))
            								mBorder = 1;
            						}
            					}
            					
            					O2GTimeConverter converter = mSession.getTimeConverter();
            					for (int i = mReaderSize - 1; i >= 0; i--) {
            						calDate = reader.getDate(i);
            						calDate = converter.convert(calDate, O2GTimeConverterTimeZone.LOCAL);
            						if (i <= mReaderSize - 1 - mBorder)
            							EventBus.publish(new FXCandleEvent(reader, i, instrument));
    
            						calFirst = reader.getDate(0);
            					}
            					
            					mCounter++;
            				}

            				if (calFrom == null || mReaderSize < mMaxBars)
            					break;

            				if (calFrom.before(calFirst))
            					calTo = calFirst;
            				
            			} while (calFrom.before(calFirst));
            		}
            	}
            }
		} 
		log.info("activeMarketDataSubscrition");
	}

	@Override
	public void connectToMarketFeed() {
		if(statusListener == null || !statusListener.isConnected()) {
			try {
	            mSession = O2GTransport.createSession();
	            statusListener = new SessionStatusListener(mSession, mDBName, mPin);
	            mSession.subscribeSessionStatus(statusListener);
	            mSession.login(mUserID, mPassword, mURL, mConnection);
	            while (!statusListener.isConnected() && !statusListener.hasError()) {
	                    Thread.sleep(50);
	            }
	        } catch (Exception e) {
	            System.out.println ("Exception: " + e.getMessage());
	            System.exit(1);
	        }
		}
		log.info("Connected to FXMarketDataFeed.");
	}

	@Override
	public void disconnectFromMarketDataFeed() {
		try {
			if (!statusListener.hasError()) {
				mSession.logout();
				
				while (!statusListener.isDisconnected()) {
					Thread.sleep(50);
				}
			}
		} catch (Exception e) {
			System.out.println ("Exception: " + e.getMessage());
	        System.exit(1);
	    }
		
		mSession.unsubscribeSessionStatus(statusListener);
		try {
			for(TickEventResponseListener r : this.tickResponseListener)
				mSession.unsubscribeResponse(r);
			mSession.unsubscribeResponse(candleResponseListener);
		} catch (Exception e) {
			log.info("unable to unsubscribe reponses. Disposing session anyway");
		}
		mSession.dispose();
		log.info("Disconnect from FXMarketDataFeed.");
	}

	@Override
	public MarketDataEventType getMarketDataEvent() {
		return this.marketDataEventType;
	}

	@Override
	public int getMarketDataFeedId() {
		return this.marketDataFeedId;
	}

	@Override
	public boolean isConnected() {
		return statusListener != null && statusListener.isConnected();
	}

	@Override
	public void setAlarmService(IAlarmService arg0) {
		// TODO Auto-generated method stub
		log.info("setAlarmService");
	}

	@Override
	public void setDebug(boolean arg0) {
		// TODO Auto-generated method stub
		log.info("setDebug");
	}

	@Override
	public void setMarketDataEvent(MarketDataEventType marketDataEventType) {
		this.marketDataEventType = marketDataEventType;
	}

	@Override
	public void setMarketDataFeedId(int arg0) {
		// TODO Auto-generated method stub
		log.info("setMarketDataFeedId");
	}

	@Override
	public void subscribeMarketData(Security security) {
		mInstrument.add(security.getSymbol().toString());
		if(this.marketDataEventType.equals(MarketDataEventType.TICK_EVENT)) {
			tickResponseListener.add(new TickEventResponseListener(mSession, security.getSymbol().toString()));
	        mSession.subscribeResponse(tickResponseListener.get(tickResponseListener.size() - 1));
		} else if(this.marketDataEventType.equals(MarketDataEventType.CANDLE_EVENT)) {
			mSession.subscribeResponse(candleResponseListener);
		}
		log.info("subscribeMarketData");
	}
	
	private void checkDateFormat() {
		if ((!mDateFrom.equals("")) && (!mDateFrom.equals("{DATEFROM}"))) {
            try {
                Date dtFrom = df.parse(mDateFrom);
                calFrom = Calendar.getInstance();
                calFrom.setTime(dtFrom);
            } catch (Exception e) {
                System.out.println(" Date From format invalid.");
                System.exit(1);
            }
        }
		
        if ((!mDateTo.equals("")) && (!mDateTo.equals("{DATETO}"))) {
            try {
                Date dtTo = df.parse(mDateTo);
                calTo = Calendar.getInstance();
                calTo.setTime(dtTo);
            } catch (Exception e) {
                System.out.println(" Date To format invalid.");
                System.exit(1);
            }
        }
	}
}
