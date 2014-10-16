package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;
import com.tictactec.ta.lib.MAType;

public class STOCHRSI extends TAIndicator {
	private int periods = 0;
	private int fastKPeriods= 0;
	private int fastDPeriods = 0;
	private MAType fastDMaType;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	
	public STOCHRSI(int periods, int fastKPeriods, int fastDPeriods, MAType fastDMaType) {
		this.periods = periods;
		this.fastKPeriods = fastKPeriods;
		this.fastDPeriods = fastDPeriods;
		this.fastDMaType = fastDMaType;
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

		double[] out = tSTOCHRSI(periods, inRealVal, fastKPeriods, fastDPeriods, fastDMaType);
		// type = 0 -> fast k
		// type = 1 -> fast d
		return out[type];
	}

	@Override
	public double getValue(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
