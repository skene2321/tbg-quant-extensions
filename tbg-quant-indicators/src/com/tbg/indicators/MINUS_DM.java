package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class MINUS_DM extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inHigh = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inLow = new HashMap<String, RollingWindow>();
	
	public MINUS_DM(int periods){
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
	@Override
	public double getValue(String symbol) {
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
		
		return tMINUS_DM(periods, inHighVal, inLowVal);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
