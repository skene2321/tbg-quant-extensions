package com.tbg.indicators;

import java.util.HashMap;

import com.tbg.core.model.indicators.TAIndicator;
import com.tbg.core.model.types.RollingWindow;

public class OBV extends TAIndicator {
	private int periods = 0;
	
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inReal = new HashMap<String, RollingWindow>();
	@SuppressWarnings("rawtypes")
	private HashMap<String, RollingWindow> inVolume = new HashMap<String, RollingWindow>();
	
	public OBV(){
		this.periods = 1;
	}
	
	@SuppressWarnings("unchecked")
	public void add(String symbol, double rValue, double vValue) {
		if(symbol != null) {
			if(rValue > 0) {
				RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);
				if(inReal.get(symbol) != null)
					inRealVal = inReal.get(symbol);
				inRealVal.add(rValue);
				inReal.put(symbol, inRealVal);
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
		RollingWindow<Double> inRealVal = new RollingWindow<Double>(periods);
		RollingWindow<Double> inVolumeVal = new RollingWindow<Double>(periods);

		if (inReal.get(symbol) != null)
			inRealVal = inReal.get(symbol);
		else
			return errorCode;
		
		if (inVolume.get(symbol) != null)
			inVolumeVal = inVolume.get(symbol);
		else
			return errorCode;
		
		return tOBV(inRealVal, inVolumeVal);
	}

	@Override
	public void add(String arg0, double arg1) {
		// TODO Auto-generated method stub
	}

}
