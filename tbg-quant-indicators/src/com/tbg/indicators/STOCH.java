package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;
import com.tictactec.ta.lib.MAType;

public class STOCH extends TAIndicator {
	private int periods = 0;
	private int fastKPeriods= 0;
	private int slowKPeriods = 0;
	private MAType slowKMaType;
	private int slowDPeriods = 0;
	private MAType slowDMaType;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inHigh = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inLow = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inClose = new HashMap<String, RollingWindow>();
	
	public STOCH(int fastKPeriods, int slowKPeriods, MAType slowKMaType, int slowDPeriods, MAType slowDMaType) {
		if(slowKPeriods > slowDPeriods)
			this.periods = slowKPeriods;
		else
			this.periods = slowDPeriods;
		
		this.fastKPeriods = fastKPeriods;
		this.slowKPeriods = slowKPeriods;
		this.slowKMaType = slowKMaType;
		this.slowDPeriods = slowDPeriods;
		this.slowDMaType = slowDMaType;
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
	public double getValue(String symbol, int type) {
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
		
		double[] out = tSTOCH(inHighVal, inLowVal, inCloseVal, fastKPeriods, slowKPeriods, slowKMaType, 
				slowDPeriods, slowDMaType);
		// type = 0 -> slow k
		// type = 1 -> slow d
		return out[type];
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
