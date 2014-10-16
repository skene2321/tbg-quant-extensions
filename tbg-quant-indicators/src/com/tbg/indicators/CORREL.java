package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class CORREL extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal0 = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal1 = new HashMap<String, RollingWindow>();
	
	public CORREL(){
		this.periods = 1;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double r0Value, double r1Value) {
		if(symbol != null) {
			if(r0Value > 0) {
				RollingWindow<Double> inReal0Val = new RollingWindow<Double>(periods);
				if(inReal0.get(symbol) != null)
					inReal0Val = inReal0.get(symbol);
				inReal0Val.add(r0Value);
				inReal0.put(symbol, inReal0Val);
			}
			
			if(r1Value > 0) {
				RollingWindow<Double> inReal1Val = new RollingWindow<Double>(periods);
				if(inReal1.get(symbol) != null)
					inReal1Val = inReal1.get(symbol);
				inReal1Val.add(r1Value);
				inReal1.put(symbol, inReal1Val);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public double getValue(String symbol) {
		RollingWindow<Double> inReal0Val = new RollingWindow<Double>(periods);
		RollingWindow<Double> inReal1Val = new RollingWindow<Double>(periods);

		if (inReal0.get(symbol) != null)
			inReal0Val = inReal0.get(symbol);
		else
			return errorCode;
		
		if (inReal1.get(symbol) != null)
			inReal1Val = inReal1.get(symbol);
		else
			return errorCode;
		
		return tCORREL(periods, inReal0Val, inReal1Val);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
