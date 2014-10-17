package com.tbg.fx.core;

import org.apache.log4j.Logger;

import com.tbg.core.model.cep.ICEPProvider;
import com.tbg.core.model.types.MarketDataEventType;
import com.tbg.service.cep.EsperCEPProvider;

public abstract class FXCEPProviderBase implements ICEPProvider {
	protected static final Logger log = Logger.getLogger(EsperCEPProvider.class);
	protected FXStrategyBase caller;
	private MarketDataEventType marketDataEventType = MarketDataEventType.TICK_EVENT;
	protected String[] CEP_QUERY = null;

	public FXCEPProviderBase(FXStrategyBase caller) {
		this.caller = caller;
	}

	public MarketDataEventType getMarketDataEvent() {
		return this.caller.getMarketDataEvent();
	}

	public void setCepQuery(String[] CEP_QUERY) {
		this.CEP_QUERY = CEP_QUERY;
	}

	public String[] getCepQuery() {
		return this.CEP_QUERY;
	}
}
