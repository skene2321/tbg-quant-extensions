/**
 *  tbg-quant-strategy-development project
 *  
 *  Alberto Sfolcini <a.sfolcini@gmail.com>
 */
package com.tbg.test;

import com.tbg.adapter.paper.account.PaperAccount;
import com.tbg.adapter.paper.broker.PaperBroker;
import com.tbg.adapter.yahoo.marketdatafeed.YahooMarketDataFeed;
import com.tbg.core.model.broker.IBroker;
import com.tbg.core.model.strategy.IStrategy;
import com.tbg.core.model.types.Currency;
import com.tbg.core.model.types.MarketDataEventType;
import com.tbg.core.model.types.Messages;
import com.tbg.core.model.types.SecurityType;
import com.tbg.core.model.types.event.CandleEvent;
import com.tbg.indicators.AD;
import com.tbg.indicators.ADX;
import com.tbg.indicators.AVGPRICE;
import com.tbg.indicators.BBANDS;
import com.tbg.indicators.EMA;
import com.tbg.strategy.TradingSystem;
import com.tbg.strategy.utils.LoadSecurities;
import com.tictactec.ta.lib.MAType;

/**
 * Skeleton Strategy code.
 * <br>
 * Use this code like a starting point to create something more unique :-)
 * <br>
 */
public class SkeletonStrategy extends TradingSystem implements IStrategy{
	
	private final PaperAccount account = new PaperAccount();
	private final IBroker broker = new PaperBroker(account);
	private final YahooMarketDataFeed marketDataFeed = new YahooMarketDataFeed();	
	{
		marketDataFeed.setYahooParameters("1", "1", "2014", "1", "6", "2014", "Week");
		marketDataFeed.setMarketDataEvent(MarketDataEventType.CANDLE_EVENT);
	}
	
	public SkeletonStrategy() {
		setTradingSystemName("Skeleton Strategy");
		setTradingSystemDescription("Use this code to create something more unique.");
		setBroker(broker);
		setMarketDataFeed(marketDataFeed);
		subscribeSecurities(new LoadSecurities(SecurityType.STK,"SMART",Currency.USD,"FB").getSecurities());
		setReportService(reportService);
	}
	
	
	@Override
	public void onStart() {
		log.info("START");
	}

	@Override
	public void onStop() {
		closeAllOpenPositions();
		accountReport();
		log.info("STOP");
	}

	BBANDS bbands = new BBANDS(20, 2.5, 2.5, MAType.Ema);
	EMA ema = new EMA(20);
	AD ad = new AD();
	AVGPRICE avg = new AVGPRICE();
	ADX adx = new ADX(14);
	@Override
	public void onEvent(Object event) {
		CandleEvent ce = (CandleEvent) event;
		String symbol  = ce.getSymbol();
		
		bbands.add(symbol, ce.getOpenPrice());
		ema.add(symbol, ce.getOpenPrice());
		adx.add(symbol, ce.getHighPrice(), ce.getLowPrice(), ce.getClosePrice());
		ad.add(symbol, ce.getHighPrice(), ce.getLowPrice(), ce.getClosePrice(), ce.getVolume());
		avg.add(symbol, ce.getOpenPrice(), ce.getHighPrice(), ce.getLowPrice(), ce.getClosePrice());
		if(getSystemActivation(symbol)) {  
			log.info(bbands.getValue(symbol, 0) + " " + bbands.getValue(symbol, 1) + " " + bbands.getValue(symbol, 2)); 
			log.info(ema.getValue(symbol));
			log.info(ad.getValue(symbol));
			log.info(avg.getValue(symbol));
			log.info(adx.getValue(symbol));
		}
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
		new SkeletonStrategy().start();
	}

}
