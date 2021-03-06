package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class ADOSC extends TAIndicator {
	private int periods = 0;
	private int fastPeriods = 0;
	private int slowPeriods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inHigh = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inLow = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inClose = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inVolume = new HashMap<String, RollingWindow>();
	
	public ADOSC(int fastPeriods, int slowPeriods){
		this.periods = slowPeriods;
		this.fastPeriods = fastPeriods;
		this.slowPeriods = slowPeriods;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double hValue, double lValue, double cValue, double vValue) {
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
			
			if(vValue > 0) {
				RollingWindow<Double> inVolumeVal = new RollingWindow<Double>(periods);
				if(inVolume.get(symbol) != null)
					inVolumeVal = inVolume.get(symbol);
				inVolumeVal.add(vValue);
				inVolume.put(symbol, inVolumeVal);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public double getValue(String symbol) {
		RollingWindow<Double> inHighVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inLowVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inCloseVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inVolumeVal = new RollingWindow<Double>(periods);
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
		
		if (inVolume.get(symbol) != null)
			inVolumeVal = inVolume.get(symbol);
		else
			return errorCode;
		
		return tADOSC(inHighVal, inLowVal, inCloseVal, inVolumeVal, fastPeriods, slowPeriods);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
