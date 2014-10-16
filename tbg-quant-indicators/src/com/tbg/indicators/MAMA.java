package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class MAMA extends TAIndicator {
	private int periods = 0;
	private double fastLimit = 0;
	private double slowLimit = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public MAMA(double fastLimit, double slowLimit){
		this.periods = 1;
		this.fastLimit = fastLimit;
		this.slowLimit = slowLimit;
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
		
		double[] out = tMAMA(inRealVal, fastLimit, slowLimit);
		// type = 0 -> mama
		// type = 1 -> fama
		return out[type];
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
