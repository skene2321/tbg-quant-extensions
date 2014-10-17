package com.tbg.fx.core;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.tbg.core.model.Position;
import com.tbg.core.model.Security;
import com.tbg.core.model.account.IAccount;
import com.tbg.core.model.alarm.AlarmServiceBase;
import com.tbg.core.model.alarm.IAlarmService;
import com.tbg.core.model.broker.IBroker;
import com.tbg.core.model.cep.ICEPProvider;
import com.tbg.core.model.marketDataFeed.IMarketDataFeed;
import com.tbg.core.model.persistent.RunID;
import com.tbg.core.model.report.IReportService;
import com.tbg.core.model.store.IStoreService;
import com.tbg.core.model.strategy.IStrategy;
import com.tbg.core.model.types.AlarmLevel;
import com.tbg.core.model.types.MarketDataEventType;
import com.tbg.core.model.types.Messages;
import com.tbg.core.model.types.RunIDStatus;
import com.tbg.core.model.types.StoreServiceType;
import com.tbg.core.model.types.event.CandleEvent;
import com.tbg.core.model.types.event.TickEvent;
import com.tbg.core.util.Config;
import com.tbg.core.util.Utils;
import com.tbg.fx.event.types.FXCandleEvent;
import com.tbg.fx.event.types.FXTickEvent;
import com.tbg.service.store.H2DBStoreService;

public abstract class FXStrategyBase extends Thread implements IStrategy {
	protected static final Logger log = Logger.getLogger(FXStrategyBase.class);
	private double start_time = 0.0D;
	protected int CONNECTION_RETRY_TIME = 10000;
	protected int EVENT_CACHE_SIZE = 100000;

	protected int SLEEP_TIME = 1000;

	private RunIDStatus STRATEGY_STATUS = RunIDStatus.RUNNING;
	protected IBroker broker;
	protected IAccount account;
	private IMarketDataFeed marketDataFeed;
	protected IAlarmService alarmService = new AlarmServiceBase();
	protected IStoreService storeService;
	protected IReportService reportService;
	private ICEPProvider CEP = new FXPassThroughProvider(this);

	protected ArrayList<Position> portfolio = new ArrayList();
	protected ArrayList<Security> securities = new ArrayList();

	private RunID runID = new RunID();

	public FXStrategyBase() {
	    log.info("-------------------------------------------------------------------------");
	    log.info(Messages.VERSION.getDesc() + "      -      " + Messages.WWWTBG.getDesc());
	    log.info("-------------------------------------------------------------------------");
	    Config.loadConfigFile(Config.DEFAULT_CONFIG_FILE);
	}

	protected void setBroker(IBroker broker) {
		this.broker = broker;
	}

	public void disconnect() {
		this.broker.disconnectFromBroker();
	}

	protected void setMarketDataFeed(IMarketDataFeed marketDataFeed) {
		this.marketDataFeed = marketDataFeed;
	}

	public MarketDataEventType getMarketDataEvent() {
		return this.marketDataFeed.getMarketDataEvent();
	}

	protected void setAlarmService(IAlarmService alarmService) {
		this.alarmService = alarmService;
	}

	protected void subscribeMarketData() {
		for (int i = 0; i < this.securities.size(); i++)
			this.marketDataFeed.subscribeMarketData((Security)this.securities.get(i));
	}

	protected void setCEPProvider(ICEPProvider myCEP) {
		this.CEP = myCEP;
	}

	protected void subscribeSecurity(Security security) {
		this.securities.add(security);
	}

	protected void subscribeSecurities(ArrayList<Security> secs) {
		for (int i = 0; i < secs.size(); i++)
			this.securities.add(secs.get(i));
	}

	protected ArrayList<Security> getSubscribedSecurity() {
		return this.securities;
	}

	private void checkServices() {
		if (this.broker != null) {
			this.storeService = this.broker.getStoreService();
		}
		
		if (this.storeService == null) {
			log.info("Using default storeService..." + StoreServiceType.DEFAULT_STORESERVICE);

			this.storeService = new H2DBStoreService();
			this.storeService.setRunID(this.runID);
		} else {
			this.storeService.setRunID(this.runID);
			log.info("Using storeService..." + this.storeService.getName());
		}
		
		if (this.alarmService == null) {
			log.info("No alarmService setted.");
		}
		log.info("RunID: " + getRunID().toString());
	}

	private void connect() {
		this.broker.setMarketDataFeed(this.marketDataFeed);
		this.broker.setStoreService(this.storeService);
		this.broker.connectToBroker();
		this.marketDataFeed.connectToMarketFeed();
		subscribeMarketData();
	}

	public void run() {
		try {
			this.start_time = System.currentTimeMillis();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						FXStrategyBase.this.storeService.setRunningStatus(RunIDStatus.STOPPED);
					} catch (Exception e) {}
				}
			});
			
			init();
			onStart();
			checkServices();

			if (this.broker == null) {
				onError(Messages.NO_BROKER_SPECIFIED);
			}
			
			if (this.marketDataFeed == null) {
				onError(Messages.NO_MARKETDATAFEED_SPECIFIED);
			}

			if (this.marketDataFeed.getMarketDataEvent() == null) {
				log.warn(Messages.NO_MARKET_DATA_EVENT_SPECIFIED_USING_DEFAULT.getDesc());
				this.marketDataFeed.setMarketDataEvent(MarketDataEventType.TICK_EVENT);
			} else {
				log.info("MarketDataEventType setted to " + this.marketDataFeed.getMarketDataEvent().toString());
			}

			log.info(this.CEP.getCEPProviderDescription());
			this.CEP.setCEPProvider();

			if (this.securities.size() < 1) {
				onError(Messages.NO_SECURITY);
				if (this.alarmService.isAuth(AlarmLevel.ERRORS)) {
					this.alarmService.sendEvent(Messages.NO_SECURITY.getDesc());
				}
			}

			init_connect();

			while (this.STRATEGY_STATUS.equals(RunIDStatus.RUNNING)) {
				if ((this.broker.isConnected()) && (this.marketDataFeed.isConnected())) {
					this.STRATEGY_STATUS = this.storeService.getRunningStatus(this.STRATEGY_STATUS);

					sleep(this.SLEEP_TIME);
				} else {
					onError(this.marketDataFeed.isConnected() ? Messages.NO_BROKER_CONNECTION : Messages.NO_MARKETDATAFEED_CONNECTION);
					if (this.alarmService.isAuth(AlarmLevel.ERRORS))
						this.alarmService.sendEvent(this.broker.isConnected() ? Messages.NO_BROKER_CONNECTION.name() : Messages.NO_MARKETDATAFEED_CONNECTION.name(), this.broker.isConnected() ? Messages.NO_BROKER_CONNECTION.getDesc() : Messages.NO_MARKETDATAFEED_CONNECTION.getDesc());
					sleep(this.CONNECTION_RETRY_TIME);
					init_connect();
				}
			}
		} catch (Throwable t) {
			long millis;
			String duration;
			if (this.alarmService.isAuth(AlarmLevel.ERRORS))
				this.alarmService.sendEvent(Messages.PROCESS_ERROR.name(), Messages.PROCESS_ERROR.getDesc() + "\n\n" + t.fillInStackTrace());
			onError(Messages.PROCESS_ERROR);
			log.error(Messages.PROCESS_ERROR.getDesc() + t.fillInStackTrace());
		} finally {
			long millis;
			String duration;
			
			onStop();
			
			try {
				EventBus.clearCache();
				EventBus.clearAllSubscribers();
				this.marketDataFeed.disconnectFromMarketDataFeed();
				this.broker.disconnectFromBroker();
			} catch (Exception e) {
			} finally {
				long millis1 = (long) (System.currentTimeMillis() - this.start_time);
				String duration1 = Utils.getDurationFromMillis(millis1);
				log.info("Elaborated in " + duration1 + " (" + millis1 + " millis).");
			}
		}

		if (this.STRATEGY_STATUS.equals(RunIDStatus.INVOKE_STOP)) {
			this.storeService.close();
			log.info(Messages.STRATEGY_STOPPED.getDesc());
			System.exit(0);
		}
	}

	private void init_connect() {
		this.broker.getOrderTracker().clear();
		EventBus.clearCache();
	    EventBus.clearAllSubscribers();

	    connect();

	    MarketDataSubscriber subscriber = new MarketDataSubscriber();
	    if (this.marketDataFeed.getMarketDataEvent() == MarketDataEventType.TICK_EVENT) {
	    	EventBus.subscribeStrongly(FXTickEvent.class, subscriber);
	    	EventBus.setCacheSizeForEventClass(FXTickEvent.class, this.EVENT_CACHE_SIZE);
	    } else if (this.marketDataFeed.getMarketDataEvent() == MarketDataEventType.CANDLE_EVENT) {
	    	EventBus.subscribeStrongly(FXCandleEvent.class, subscriber);
	    	EventBus.setCacheSizeForEventClass(FXCandleEvent.class, this.EVENT_CACHE_SIZE);
	    } else {
	    	onError(Messages.UNSUPPORTED_MARKET_DATA_EVENT);
	    }

	    EventBus.subscribeStrongly(String.class, new EventSubscriber() {
	    	@Override
	    	public void onEvent(Object event) {
	    		String ev = (String) event;
	    		if (ev.equals(RunIDStatus.INVOKE_STOP.name())) {
	    			storeService.setRunningStatus(RunIDStatus.INVOKE_STOP);
	    			try { Thread.sleep(1000L); } catch (InterruptedException e) {}
	    		}
	    	}
	    });
	    log.info("Active MarketDataFeed...");
	    this.marketDataFeed.activeMarketDataSubscription();
	  }

	private void updateAccount() {
		this.account = this.broker.updateAccount();
		this.portfolio = this.account.getPortfolio();
	}

	public void init() {}

	public void onStart() {}

	public void onStop() {}

	public void onPreEvent(Object event) {}

	public void onEvent(Object event) {
		log.info("Event received: " + event.toString());
	}

	public void onError(Messages msg) {
		log.error("onError(): " + msg.getDesc());
	}

	protected void accountReport() {
		if ((this.reportService != null) && (this.account != null)) {
			this.reportService.setAccount(this.account);
			this.reportService.setStoreService(this.storeService);
			this.reportService.executeReport();
		}
	}

	protected void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

	protected void setStoreService(IStoreService storeService) {
		this.storeService = storeService;
		this.broker.setStoreService(storeService);
	}

	protected void setRunID(RunID runID) {
		this.runID = runID;
	}

	protected RunID getRunID() {
		return this.runID;
	}

	public class MarketDataSubscriber implements EventSubscriber<Object> {
		public MarketDataSubscriber() {}

		public void onEvent(Object event) {
			if(marketDataFeed.getMarketDataEvent() == MarketDataEventType.TICK_EVENT) {
				FXTickEvent tickEvent = (FXTickEvent)event;
				if(tickEvent != null) {
					CEP.sendEvent(tickEvent);
					updateAccount();
				}
			} else if(marketDataFeed.getMarketDataEvent() == MarketDataEventType.CANDLE_EVENT) {
				FXCandleEvent candleEvent = (FXCandleEvent)event;
				if(candleEvent != null) {
					CEP.sendEvent(candleEvent);
					updateAccount();
				}
			}
		}
	}
}