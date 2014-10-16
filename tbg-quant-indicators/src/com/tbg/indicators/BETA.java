package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class BETA extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal0 = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal1 = new HashMap<String, RollingWindow>();
	
	public BETA(int periods){
		this.periods = periods;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double rValue0, double rValue1) {
		if(symbol != null) {
			if(rValue0 > 0) {
				RollingWindow<Double> inReal0Val = new RollingWindow<Double>(periods);
				if(inReal0.get(symbol) != null)
					inReal0Val = inReal0.get(symbol);
				inReal0Val.add(rValue0);
				inReal0.put(symbol, inReal0Val);
			}
			
			if(rValue1 > 0) {
				RollingWindow<Double> inReal1Val = new RollingWindow<Double>(periods);
				if(inReal1.get(symbol) != null)
					inReal1Val = inReal1.get(symbol);
				inReal1Val.add(rValue1);
				inReal1.put(symbol, inReal1Val);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public double getValue(String symbol) {
		RollingWindow<Double> inReal0Val = new RollingWindow<Double>(periods);
		RollingWindow<Double> inReal1Val = new RollingWindow<Double>(periods);

		if (inReal0.get(symbol) != null)
			inReal0Val = inReal0.get(symbol);
		else
			return errorCode;
		
		if (inReal1.get(symbol) != null)
			inReal1Val = inReal1.get(symbol);
		else
			return errorCode;
		
		return tBETA(periods, inReal0Val, inReal1Val);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
