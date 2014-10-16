package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class STDDEV extends TAIndicator {
	private int periods = 0;
	private double nbDev = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public STDDEV(int periods, double nbDev){
		this.periods = periods;
		this.nbDev = nbDev;
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
		
		return tSTDDEV(periods, inRealVal, nbDev);
	}
}
