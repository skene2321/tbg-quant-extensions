package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class MACD extends TAIndicator {
	private int periods = 0;
	private int fastPeriods = 0;
	private int slowPeriods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public MACD(int periods, int fastPeriods, int slowPeriods){
		this.periods = periods;
		this.fastPeriods = fastPeriods;
		this.slowPeriods = slowPeriods;
	}
	
	@Override
	@SuppressWarnings("unchecked")
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
	public double getValue(String symbol, int type) {
		RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);

		if (inReal.get(symbol) != null)
			inRealVal = inReal.get(symbol);
		else
			return errorCode;
		
		double[] out = tMACD(periods, inRealVal, fastPeriods, slowPeriods);
		// type = 0 -> macd
		// type = 1 -> macd signal
		// type = 2 -> macd hist
		return out[type];
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
