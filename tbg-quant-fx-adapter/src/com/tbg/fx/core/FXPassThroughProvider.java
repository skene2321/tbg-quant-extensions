package com.tbg.fx.core;

import com.tbg.core.model.cep.ICEPProvider;

public class FXPassThroughProvider extends FXCEPProviderBase implements ICEPProvider {
	
	public FXPassThroughProvider(FXStrategyBase strategyBase) {
		super(strategyBase);
	}

	public void sendEvent(Object event) {
		if (event != null) {
			this.caller.onPreEvent(event);
			this.caller.onEvent(event);
		}
	}

	public void setCEPProvider() {}

	public String getCEPProviderDescription() {
		return "Pass-Through CEP Provider";
	}
}