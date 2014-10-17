/**
 *  tbg-quant-fx-adapter project
 *  
 *  Riccardo Alberti <riccardo.alberti@gmail.com>
 */
package com.tbg.adapter.fx.main;

import com.tbg.adapter.fx.marketdatafeed.FXMarketDataFeed;
import com.tbg.adapter.paper.account.PaperAccount;
import com.tbg.adapter.paper.broker.PaperBroker;
import com.tbg.core.model.broker.IBroker;
import com.tbg.core.model.strategy.IStrategy;
import com.tbg.core.model.types.Currency;
import com.tbg.core.model.types.MarketDataEventType;
import com.tbg.core.model.types.Messages;
import com.tbg.core.model.types.SecurityType;
import com.tbg.fx.core.FXTradingSystem;
import com.tbg.strategy.utils.LoadSecurities;

/**
 * FXSkeleton Strategy code.
 * <br>
 * Use this code like a starting point to create something more unique :-)
 * <br>
 */
public class FXSkeletonStrategy extends FXTradingSystem implements IStrategy {
	private final PaperAccount account = new PaperAccount();
	private final IBroker broker = new PaperBroker(account);
	private final FXMarketDataFeed marketDataFeed = new FXMarketDataFeed();
	{
		marketDataFeed.setMarketDataEvent(MarketDataEventType.CANDLE_EVENT);
		marketDataFeed.setFXDemoParameters("<user>", "<password>", "m1", "2012-01-01 10:10:10", "2012-01-02 10:10:10");
	}
	
	public FXSkeletonStrategy() {
		setTradingSystemName("FXSkeleton Strategy");
		setTradingSystemDescription("Use this code to create something more unique.");
		setBroker(broker);
		setMarketDataFeed(marketDataFeed);
		subscribeSecurities(new LoadSecurities(SecurityType.STK,"SMART",Currency.USD,"EUR/USD, GBP/USD").getSecurities());
	}
	
	@Override
	public void onStart() {
		log.info("START");
	}

	@Override
	public void onStop() {
		log.info("STOP");
	}

	@Override
	public void onEvent(Object event) {
		log.info(event);	
	}

	@Override
	public void onError(Messages msg) {
		log.error(msg.toString());	
	}

	/**
	 * Start it up!
	 */
	public static void main(String[] args) {
		new FXSkeletonStrategy().start();
	}
}
