package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class T3 extends TAIndicator {
	private int periods = 0;
	private double vFactor = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public T3(int periods, double vFactor){
		this.periods = periods;
		this.vFactor = vFactor;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void add(String symbol, double rValue) {
		if(symbol != null) {
			if(rValue > 0) {			
				RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);
				if(inReal.get(symbol) != null)
					inRealVal = inReal.get(symbol);
				inRealVal.add(rValue);
				inReal.put(symbol, inRealVal);			
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public double getValue(String symbol) {
		RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);
		if (inReal.get(symbol) != null)
			inRealVal = inReal.get(symbol);
		else
			return errorCode;
		
		return tT3(periods, inRealVal, vFactor);
	}
}
