package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class AVGPRICE extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inOpen = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inHigh = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inLow = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inClose = new HashMap<String, RollingWindow>();
	
	public AVGPRICE(){
		this.periods = 1;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double oValue, double hValue, double lValue, double cValue) {
		if(symbol != null) {
			if(oValue > 0) {
				RollingWindow<Double> inOpenVal = new RollingWindow<Double>(periods);
				if(inOpen.get(symbol) != null)
					inOpenVal = inOpen.get(symbol);
				inOpenVal.add(oValue);
				inOpen.put(symbol, inOpenVal);
			}
			
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
			
			if(cValue > 0) {
				RollingWindow<Double> inCloseVal = new RollingWindow<Double>(periods);
				if(inClose.get(symbol) != null)
					inCloseVal = inClose.get(symbol);
				inCloseVal.add(cValue);
				inClose.put(symbol, inCloseVal);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public double getValue(String symbol) {
		RollingWindow<Double> inOpenVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inHighVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inLowVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inCloseVal = new RollingWindow<Double>(periods);
		
		if (inOpen.get(symbol) != null)
			inOpenVal = inOpen.get(symbol);
		else
			return errorCode;

		if (inHigh.get(symbol) != null)
			inHighVal = inHigh.get(symbol);
		else
			return errorCode;
		
		if (inLow.get(symbol) != null)
			inLowVal = inLow.get(symbol);
		else
			return errorCode;
		
		if (inClose.get(symbol) != null)
			inCloseVal = inClose.get(symbol);
		else
			return errorCode;
		
		return tAVGPRICE(inOpenVal, inHighVal, inLowVal, inCloseVal);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
