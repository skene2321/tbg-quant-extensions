package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class ULTOSC extends TAIndicator {
	private int periods = 0;
	private int periods0 = 0;
	private int periods1 = 0;
	private int periods2 = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inHigh = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inLow = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inClose = new HashMap<String, RollingWindow>();
	
	public ULTOSC(int periods0, int periods1, int periods2){
		if(periods0 > periods1 && periods0 > periods2)
			this.periods = periods0;
		if(periods1 > periods0 && periods1 > periods2)
			this.periods = periods1;
		if(periods2 > periods1 && periods2 > periods0)
			this.periods = periods2;
		
		this.periods0 = periods0;
		this.periods1 = periods1;
		this.periods2 = periods2;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double hValue, double lValue, double cValue) {
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
		RollingWindow<Double> inHighVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inLowVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inCloseVal = new RollingWindow<Double>(periods);

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
		
		return tULTOSC(inHighVal, inLowVal, inCloseVal, periods0, periods1, periods2);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
