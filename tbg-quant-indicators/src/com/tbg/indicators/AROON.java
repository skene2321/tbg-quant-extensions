package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class AROON extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inHigh = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inLow = new HashMap<String, RollingWindow>();
	
	public AROON(int periods){
		this.periods = periods;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double hValue, double lValue) {
		if(symbol != null) {
			if(hValue > 0) {
				RollingWindow<Double> inHighVal = new RollingWindow<Double>(periods);
				if(inHigh.get(symbol) != null)
					inHighVal = inHigh.get(symbol);
				inHighVal.add(hValue);
				inHigh.put(symbol, inHighVal);
			}
			if(lValue > 0) {
				RollingWindow<Double> inLowVal = new RollingWindow<Double>(periods);
				if(inLow.get(symbol) != null)
					inLowVal = inLow.get(symbol);
				inLowVal.add(lValue);
				inLow.put(symbol, inLowVal);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public double getValue(String symbol, int type) {
		RollingWindow<Double> inHighVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inLowVal = new RollingWindow<Double>(periods);

		if (inHigh.get(symbol) != null)
			inHighVal = inHigh.get(symbol);
		else
			return errorCode;
		if (inLow.get(symbol) != null)
			inLowVal = inLow.get(symbol);
		else
			return errorCode;
		
		double[] out = tAROON(periods, inHighVal, inLowVal);
		// type = 0 -> up
		// type = 1 -> down
		return out[type];
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}
}
