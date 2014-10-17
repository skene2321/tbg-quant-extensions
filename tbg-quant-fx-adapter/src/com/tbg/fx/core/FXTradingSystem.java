package com.tbg.fx.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tbg.core.model.Order;
import com.tbg.core.model.Position;
import com.tbg.core.model.Security;
import com.tbg.core.model.Symbol;
import com.tbg.core.model.persistent.RunID;
import com.tbg.core.model.types.AccountKey;
import com.tbg.core.model.types.Messages;
import com.tbg.core.model.types.OrderSide;
import com.tbg.core.model.types.OrderState;
import com.tbg.core.model.types.OrderTIF;
import com.tbg.core.model.types.OrderType;
import com.tbg.core.model.types.PositionSide;
import com.tbg.core.model.types.RunIDStatus;
import com.tbg.core.model.types.ValueType;
import com.tbg.core.model.types.event.CandleEvent;
import com.tbg.core.util.Parameter;
import com.tbg.fx.event.types.FXCandleEvent;
import com.tbg.fx.event.types.FXTickEvent;
import com.tbg.strategy.ITradingSystem;
import com.tbg.strategy.utils.PositionTracker;
import com.tbg.strategy.utils.SignalTracker;
import com.tbg.strategy.utils.TrendTracker;

public class FXTradingSystem extends FXStrategyBase implements ITradingSystem {
	protected static final Logger log = Logger.getLogger(FXTradingSystem.class);

	private String runID = null;
	private String tradingSystemID = null;
	private String tradingSystemName = "noname";
	private String tradingSystemDescription = "n/a";
	Parameter parameter = new Parameter();
	private Map<String, Integer> events = new HashMap();
	private int SYSTEM_ACTIVATION_PERIODS = 0;
	private Map<String, Boolean> systemActive = new HashMap();

	protected TrendTracker trendTracker = new TrendTracker();
	protected PositionTracker positionTracker = new PositionTracker();
	protected SignalTracker signalTracker = new SignalTracker();
	
	public FXTradingSystem() {
		this.tradingSystemID = getClass().getSimpleName();
	    try {
	    	this.parameter.loadConfigFile("config.proprieties");
	    	log.info("Loaded parameters from config.proprieties file.");
	    } catch (Exception e) {
	    	log.warn("Cannot find config.proprieties file, proceeding without it.");
	    }
	}
	
	public void init() {
		log.info("Strategy ID : " + getTradingSystemID());
	    log.info("Strategy " + this.tradingSystemName + " started [" + new Date() + "]");
	    log.info("Strategy Description: " + this.tradingSystemDescription);

	    if (this.runID != null)
	    	setRunID(new RunID(this.runID, getTradingSystemDescription(), getTradingSystemName(), this.tradingSystemID));
	    else
	    	setRunID(new RunID("", getTradingSystemDescription(), getTradingSystemName(), this.tradingSystemID));
	}
	
	public void onPreEvent(Object event) {
		this.positionTracker.setOrderTracker(this.broker.getOrderTracker());
	    this.positionTracker.setPortfolio(this.portfolio);

	    int es = 0;
	    String symbol = "n/a";
	    if ((event instanceof FXCandleEvent)) {
	    	symbol = ((FXCandleEvent) event).getSymbol();
	    } else if ((event instanceof FXTickEvent)) {
	    	symbol = ((FXTickEvent) event).getSymbol();
	    } else if ((event instanceof Map)) {
	    	symbol = (String)((Map)event).get("symbol");
	    }

	    if (this.events.get(symbol) != null)
	      es = ((Integer)this.events.get(symbol)).intValue();
	    es++;
	    
	    this.events.put(symbol, Integer.valueOf(es));
	    if (es > this.SYSTEM_ACTIVATION_PERIODS)
	    	this.systemActive.put(symbol, Boolean.valueOf(true));
	}
	
	public int getMinimunSystemPeriods() {
		return this.SYSTEM_ACTIVATION_PERIODS;
	}

	public void setMinimunSystemPeriods(int minimunPeriods) {
		this.SYSTEM_ACTIVATION_PERIODS = minimunPeriods;
	}

	public boolean getSystemActivation(String symbol) {
	    return this.systemActive.get(symbol) != null;
	}

	private String getTradingSystemID() {
		return this.tradingSystemID;
	}

	public void setTradingSystemName(String tradeSystemName) {
		this.tradingSystemName = tradeSystemName;
	}

	public String getTradingSystemName() {
		return this.tradingSystemName;
	}

	public void setTradingSystemDescription(String tradeSystemDescription) {
		this.tradingSystemDescription = tradeSystemDescription;
	}

	public String getTradingSystemDescription() {
		return this.tradingSystemDescription;
	}

	public void setRunID(String runID) {
		this.runID = runID;
	}

	public void onCustomCandleEvent(FXCandleEvent event) {}

	public Security getSecurityBySymbol(String symbol) {
		for (int i = 0; i < this.securities.size(); i++) {
			Security sec = (Security)this.securities.get(i);
			if (sec.getSymbol().toString().equalsIgnoreCase(symbol))
				return sec;
		}
		return null;
	}

	public void scheduleReportGeneration(int seconds) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				FXTradingSystem.log.info("Time for Report!");
				FXTradingSystem.this.accountReport();
			}
		}, seconds * 1000, seconds * 1000);

		log.info("Account Report generation scheduled every " + seconds + " seconds.");
	}

	public String getTradingSystemParameter(String key) {
		if (this.parameter != null) {
			return this.parameter.getConfigParameter(key);
		}
		
		log.error("Fatal Error, unable to get parameter from config.proprieties file!");
	    log.error("Please check your configruation or create the config.proprieties file in according with your strategy rules.");

	    return null;
	}

	public void closeAllOpenPositions() {
	    log.info("Sending orders to close open position...");
	    for (int i = 0; i < this.portfolio.size(); i++) {
	      Security _security = ((Position)this.portfolio.get(i)).getSecurity();
	      PositionSide _side = ((Position)this.portfolio.get(i)).getSide();
	      
	      OrderSide _orderSide;
	      
	      if (_side.equals(PositionSide.LONG))
	        _orderSide = OrderSide.SELL;
	      else
	        _orderSide = OrderSide.BUY;
	      double _qty = ((Position)this.portfolio.get(i)).getQuantity();

	      Order order = new Order();
	      order.setSecurity(_security);
	      order.setOrderSide(_orderSide);
	      order.setOrderType(OrderType.MARKET);
	      order.setQuantity(_qty);
	      order.setTimeInForce(OrderTIF.DAY);

	      this.broker.sendOrder(order);
	    }

	    while (this.portfolio.size() > 1) {
	    	try { Thread.sleep(2000L); } catch (InterruptedException e) {	}
	    	this.broker.updateAccount();
	    	this.portfolio = this.broker.getAccount().getPortfolio();
	    	log.info("Waiting positions to be closed...");
	    }
	    
	    this.broker.updateAccount();
	    log.info("All positions have been closed.");
	}

	public void closePositionFor(String symbol) {
		for (int i = 0; i < this.portfolio.size(); i++) {
			Security _security = ((Position)this.portfolio.get(i)).getSecurity();
			if (_security.getSymbol().toString().equalsIgnoreCase(symbol)) {
				PositionSide _side = ((Position)this.portfolio.get(i)).getSide();
				OrderSide _orderSide;

				if (_side.equals(PositionSide.LONG))
					_orderSide = OrderSide.SELL;
				else
					_orderSide = OrderSide.BUY;
				double _qty = ((Position)this.portfolio.get(i)).getQuantity();

				Order order = new Order();
				order.setSecurity(_security);
				order.setOrderSide(_orderSide);
				order.setOrderType(OrderType.MARKET);
				order.setQuantity(_qty);
				order.setTimeInForce(OrderTIF.DAY);

				this.broker.sendOrder(order);
				break;
			}
		}
	}

	public int getSharesFor(double cashAmount, double actualPrice) {
	    int shares = (int)(cashAmount / actualPrice);
	    return shares;
	}

	public double getCashBalance() {
	    String balance = null;
	    try {
	    	balance = this.account.getAccountKey(AccountKey.CASH_BALANCE.getKey()).getValue(); 
	    } catch (Exception e) {}
	    
	    if (balance == null) {
	    	return 0.0D;
	    }
	    return Double.parseDouble(balance);
	}

	public double getInitialCapital() {
		String capital = null;
	    try {
	      capital = this.account.getAccountKey(AccountKey.INITIAL_CAPITAL.getKey()).getValue(); 
	    } catch (Exception e) {}
	    
	    if (capital == null) {
	      return 0.0D;
	    }
	    return Double.parseDouble(capital);
	}

	public void cancelOrderForSymbol(Symbol symbol) {
		ArrayList nids = this.broker.getOrderTracker().getOrderId(symbol.toString(), OrderState.NEW);
	    for (int i = 0; i < nids.size(); i++) {
	    	this.broker.getOrderTracker().updateOrder(((Long)nids.get(i)).intValue(), OrderState.CANCELED, "Cancel request.");
	    }

	    ArrayList oids = this.broker.getOrderTracker().getOrderId(symbol.toString(), OrderState.PROCESSING);
	    for (int i = 0; i < oids.size(); i++)
	    	this.broker.cancelOrder(((Long)oids.get(i)).intValue());
	}

	public void cancelAllOrders() {
		for (int i = 0; i < this.securities.size(); i++) {
			log.info("Sending cancel request for ");
			cancelOrderForSymbol(((Security)this.securities.get(i)).getSymbol());
		}
	}

	public Map getEventMap(Object event) {
		if ((event instanceof FXTickEvent)) {
			log.warn(Messages.EVENT_ISTANCEOF_TICK.getDesc());
			HashMap map = new HashMap();
			map.put("symbol", ((FXTickEvent)event).getSymbol());
			map.put("ask", Double.valueOf(((FXTickEvent)event).getAsk()));
			map.put("bid", Double.valueOf(((FXTickEvent)event).getBid()));
			map.put("high", Double.valueOf(((FXTickEvent)event).getHigh()));
			map.put("low", Double.valueOf(((FXTickEvent)event).getLow()));
			map.put("volume", Integer.valueOf(((FXTickEvent)event).getVolume()));
			map.put("timestamp", ((FXTickEvent)event).getTimeStamp());
			return map;
		}

	    if ((event instanceof FXCandleEvent)) {
	    	log.warn(Messages.EVENT_INSTANCEOF_CANDLE.getDesc());
	    	HashMap map = new HashMap();
	    	map.put("symbol", ((FXCandleEvent)event).getSymbol());
	    	map.put("ask", Double.valueOf(((FXCandleEvent)event).getAsk()));
	    	map.put("askLow", Double.valueOf(((FXCandleEvent)event).getAskLow()));
	    	map.put("askHigh", Double.valueOf(((FXCandleEvent)event).getAskHigh()));
	    	map.put("askOpen", Double.valueOf(((FXCandleEvent)event).getAskOpen()));
	    	map.put("askClose", Double.valueOf(((FXCandleEvent)event).getAskClose()));
	    	map.put("bid", Double.valueOf(((FXCandleEvent)event).getBid()));
	    	map.put("bidLow", Double.valueOf(((FXCandleEvent)event).getBidLow()));
	    	map.put("bidHigh", Double.valueOf(((FXCandleEvent)event).getBidHigh()));
	    	map.put("bidOpen", Double.valueOf(((FXCandleEvent)event).getBidOpen()));
	    	map.put("bidClose", Double.valueOf(((FXCandleEvent)event).getBidClose()));
	    	map.put("volume", Integer.valueOf(((FXCandleEvent)event).getVolume()));
	    	map.put("timestamp", ((FXCandleEvent)event).getTimeStamp());
	    	return map;
	    }

	    Map map = (Map)event;
	    return map;
	}
	
	public void setStopLoss(double stop_value, ValueType valueType) {
		for (int i = 0; i < this.portfolio.size(); i++) {
			String symbol = ((Position)this.portfolio.get(i)).getSecurity().getSymbol().toString();
			setStopLossForSymbol(symbol, stop_value, valueType);
		}
	}

	public void setStopLossForSymbol(String symbol, double stop_value, ValueType valueType) {
		if (stop_value == 0.0D) {
			log.warn("Wrong value, Stop Order Value cannot be zero!");
			return;
		}

		for (int i = 0; i < this.portfolio.size(); i++) {
			Position p = (Position)this.portfolio.get(i);
			if (p.getSecurity().getSymbol().toString().equalsIgnoreCase(symbol)) {
				if (p.getUnrealizedPnL() >= 0.0D) break;
				double pnl = p.getUnrealizedPnL();
				if (valueType.equals(ValueType.PERCENTAGE))
					pnl = p.getUnrealizedPnL() * 100.0D / (p.getAveragePrice() * p.getQuantity());
				if (Math.abs(pnl) >= Math.abs(stop_value)) {
					log.info(Messages.STOP_LOSS_ORDER.getDesc());
					closePositionFor(symbol);
				}
				break;
			}
		}
	}

	public void setStopProfit(double stop_value, ValueType valueType) {
		for (int i = 0; i < this.portfolio.size(); i++) {
			String symbol = ((Position)this.portfolio.get(i)).getSecurity().getSymbol().toString();
			setStopProfitForSymbol(symbol, stop_value, valueType);
		}
	}

	public void setStopProfitForSymbol(String symbol, double stop_value, ValueType valueType) {
		if (stop_value == 0.0D) {
			log.warn("Wrong value, Stop Order Value cannot be zero!");
			return;
		}

		for (int i = 0; i < this.portfolio.size(); i++) {
			Position p = (Position)this.portfolio.get(i);
			if (p.getSecurity().getSymbol().toString().equalsIgnoreCase(symbol)) {
				if (p.getUnrealizedPnL() <= 0.0D) break;
				double pnl = p.getUnrealizedPnL();
				if (valueType.equals(ValueType.PERCENTAGE))
					pnl = p.getUnrealizedPnL() * 100.0D / (p.getAveragePrice() * p.getQuantity());
				if (Math.abs(pnl) >= Math.abs(stop_value)) {
					log.info(Messages.STOP_PROFIT_ORDER.getDesc());
					closePositionFor(symbol);
				}
				break;
			}
		}
	}

	public void invokeStrategyStop() {
		log.info(" => Invoking Strategy Stop.. .");
	    this.storeService.setRunningStatus(RunIDStatus.INVOKE_STOP);
	}

	@Override
	public void onCustomCandleEvent(CandleEvent arg0) {}
}
