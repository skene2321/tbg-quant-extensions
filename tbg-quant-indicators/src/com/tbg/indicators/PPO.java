package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;
import com.tictactec.ta.lib.MAType;

public class PPO extends TAIndicator {
	private int periods = 0;
	private int fastPeriods = 0;
	private int slowPeriods = 0;
	private MAType maType;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public PPO(int fastPeriods, int slowPeriods, MAType maType){
		this.periods = slowPeriods;
		this.fastPeriods = fastPeriods;
		this.slowPeriods = slowPeriods;
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
	@Override
	public double getValue(String symbol) {
		RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);

		if (inReal.get(symbol) != null)
			inRealVal = inReal.get(symbol);
		else
			return errorCode;
		
		return tPPO(inRealVal, fastPeriods, slowPeriods, maType);
	}
}
