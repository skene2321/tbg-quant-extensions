package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;
import com.tictactec.ta.lib.MAType;

public class BBANDS extends TAIndicator {
	private int periods = 0;
	private double devUp = 0;
	private double devDown = 0;
	private MAType maType = null;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public BBANDS(int periods, double devUp, double devDown, MAType maType){
		this.periods = periods;
		this.devUp = devUp;
		this.devDown = devDown;
		this.maType = maType;
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
		
		double[] out = tBBANDS(periods, inRealVal, devUp, devDown, maType);
		// type = 0 -> upper band
		// type = 1 -> middle band
		// type = 2 -> lower band
		return out[type];
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
