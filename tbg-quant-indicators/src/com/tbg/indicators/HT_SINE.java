package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class HT_SINE extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public HT_SINE(){
		this.periods = 1;
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
	public double getValue(String symbol, int type) {
		RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);
		if (inReal.get(symbol) != null)
			inRealVal = inReal.get(symbol);
		else
			return errorCode;
		
		double[] out = tHT_SINE(inRealVal);
		// type = 0 -> sine
		// type = 1 -> lease sine
		return out[type];
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
