package com.tbg.core.model.indicators;

import com.tbg.core.model.indicator.IndicatorBase;
import com.tbg.core.model.types.RollingWindow;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public abstract class TAIndicator implements IndicatorBase {
	private Core core = new Core();
	private MInteger begin = new MInteger();
    private MInteger length = new MInteger();
	protected final int errorCode = Integer.MIN_VALUE;
	
	public double tAD(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose, RollingWindow<Double> inVolume) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.ad(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose), 
	    		toDoubleArray(inVolume), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tADOSC(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose, RollingWindow<Double> inVolume, int fastPeriod, int slowPeriod) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.adOsc(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose), 
	    		toDoubleArray(inVolume), fastPeriod, slowPeriod, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tADX(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.adx(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tADXR(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.adxr(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tAPO(RollingWindow<Double> inReal, int fastPeriods, int slowPeriods, MAType maType) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.apo(0, inReal.size() - 1, toDoubleArray(inReal), fastPeriods, slowPeriods, maType, 
	    		begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double[] tAROON(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow) {
		double[] out = new double[2];
		double[] outUp = new double[inHigh.size()];
		double[] outDown = new double[inHigh.size()];
	    RetCode retCode = core.aroon(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), periods, 
	    		begin, length, outDown, outUp);
	    if (retCode == RetCode.Success) {
			out[0] = outUp[0];
			out[1] = outDown[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double tAROONOSC(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.aroonOsc(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), periods, 
	    		begin, length, out);
	    if (retCode == RetCode.Success) 
			return out[0];

		return errorCode;
	}
	
	public double tATR(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.atr(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tAVGPRICE(RollingWindow<Double> inOpen, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inOpen.size()];
	    RetCode retCode = core.avgPrice(0, inOpen.size() - 1, toDoubleArray(inOpen), toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double[] tBBANDS(int periods, RollingWindow<Double> inReal, double devUp, double devDown, 
			MAType maType) {
		double[] out = new double[3];
		double[] outUp = new double[inReal.size()];
		double[] outMid = new double[inReal.size()];
		double[] outDown = new double[inReal.size()];
	    RetCode retCode = core.bbands(0, inReal.size() - 1, toDoubleArray(inReal), periods, devUp, devDown, maType, begin, length, outUp, outMid, outDown);
		if (retCode == RetCode.Success) {
			out[0] = outUp[0];
			out[1] = outMid[0];
			out[2] = outDown[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double tBETA(int periods, RollingWindow<Double> inReal0, RollingWindow<Double> inReal1) {
		double[] out = new double[inReal0.size()];
	    RetCode retCode = core.beta(0, inReal0.size() - 1, toDoubleArray(inReal0), toDoubleArray(inReal1), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tBOP(RollingWindow<Double> inOpen, RollingWindow<Double> inHigh, 
			RollingWindow<Double> inLow, RollingWindow<Double> inClose) {
		double[] out = new double[inOpen.size()];
	    RetCode retCode = core.bop(0, inOpen.size() - 1, toDoubleArray(inOpen), toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tCCI(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode =  core.cci(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tCMO(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.cmo(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tCORREL(int periods, RollingWindow<Double> inReal0, RollingWindow<Double> inReal1) {
		double[] out = new double[inReal0.size()];
	    RetCode retCode = core.correl(0, inReal0.size() - 1, toDoubleArray(inReal0), toDoubleArray(inReal1), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tDEMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.dema(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tDX(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.dx(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tEMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.ema(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tHT_DCPERIOD(RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.htDcPeriod(0, inReal.size() - 1, toDoubleArray(inReal), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tHT_DCPHASE(RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.htDcPhase(0, inReal.size() - 1, toDoubleArray(inReal), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double[] tHT_PHASOR(RollingWindow<Double> inReal) {
		double[] out = new double[2];
		double[] outPhase = new double[inReal.size()];
		double[] outQuad = new double[inReal.size()];
	    RetCode retCode = core.htPhasor(0, inReal.size() - 1, toDoubleArray(inReal), begin, length, outPhase,
	    		outQuad);
		if (retCode == RetCode.Success) {
			out[0] = outPhase[0];
			out[1] = outQuad[0];
			return out;
		}	

		out[0] = errorCode;
		return out;
	}
	
	public double[] tHT_SINE(RollingWindow<Double> inReal) {
		double[] out = new double[2];
		double[] outSine = new double[inReal.size()];
		double[] outLeadSine = new double[inReal.size()];
	    RetCode retCode = core.htSine(0, inReal.size() - 1, toDoubleArray(inReal), begin, length, outSine,
	    		outLeadSine);
		if (retCode == RetCode.Success) {
			out[0] = outSine[0];
			out[1] = outLeadSine[0];
			return out;
		}	

		out[0] = errorCode;
		return out;
	}
	
	public double tHT_TRENDLINE(RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.htTrendline(0, inReal.size() - 1, toDoubleArray(inReal), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tKAMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.kama(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tLINEARREG(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.linearReg(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tLINEARREG_ANGLE(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.linearRegAngle(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tLINEARREG_INTERCEPT(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.linearRegIntercept(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tLINEARREG_SLOPE(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.linearRegSlope(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double[] tMACD(int periods, RollingWindow<Double> inReal, int fastPeriods, int slowPeriods) {
		double[] out = new double[3];
		double[] outMacd = new double[inReal.size()];
		double[] outMacdSignal = new double[inReal.size()];
		double[] outMacdHist = new double[inReal.size()];
	    RetCode retCode = core.macd(0, inReal.size() - 1, toDoubleArray(inReal), fastPeriods, slowPeriods,
	    		periods, begin, length, outMacd, outMacdSignal, outMacdHist);
		if (retCode == RetCode.Success) {
			out[0] = outMacd[0];
			out[1] = outMacdSignal[0];
			out[2] = outMacdHist[0];
			return out;
		}

		out[0] = errorCode;
		return out;	
	}
	
	public double[] tMACDEXT(int periods, RollingWindow<Double> inReal, int fastPeriods, int slowPeriods,
			MAType maType, MAType fastMaType, MAType slowMaType) {
		double[] out = new double[3];
		double[] outMacd = new double[inReal.size()];
		double[] outMacdSignal = new double[inReal.size()];
		double[] outMacdHist = new double[inReal.size()];
	    RetCode retCode = core.macdExt(0, inReal.size() - 1, toDoubleArray(inReal), fastPeriods, fastMaType, slowPeriods,
	    		slowMaType, periods, maType, begin, length, outMacd, outMacdSignal, outMacdHist);
		if (retCode == RetCode.Success) {
			out[0] = outMacd[0];
			out[1] = outMacdSignal[0];
			out[2] = outMacdHist[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double[] tMACDFIX(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[3];
		double[] outMacd = new double[inReal.size()];
		double[] outMacdSignal = new double[inReal.size()];
		double[] outMacdHist = new double[inReal.size()];
	    RetCode retCode = core.macdFix(0, inReal.size() - 1, toDoubleArray(inReal), periods,
	    		begin, length, outMacd, outMacdSignal, outMacdHist);
		if (retCode == RetCode.Success) {
			out[0] = outMacd[0];
			out[1] = outMacdSignal[0];
			out[2] = outMacdHist[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double[] tMAMA(RollingWindow<Double> inReal, double fastLimit, double slowLimit) {
		double[] out = new double[2];
		double[] outMama = new double[inReal.size()];
		double[] outFama = new double[inReal.size()];
	    RetCode retCode = core.mama(0, inReal.size() - 1, toDoubleArray(inReal), fastLimit, 
	    		slowLimit, begin, length, outMama, outFama);
		if (retCode == RetCode.Success) {
			out[0] = outMama[0];
			out[1] = outFama[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double tMFI(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose, RollingWindow<Double> inVolume) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.mfi(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), toDoubleArray(inVolume), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tMINUS_DI(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.minusDI(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tMINUS_DM(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.minusDM(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tMOM(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.mom(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tNATR(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.natr(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tOBV(RollingWindow<Double> inReal, RollingWindow<Double> inVolume) {
		double[] out = new double[inReal.size()];
	    RetCode retCode = core.obv(0, inReal.size() - 1, toDoubleArray(inReal), toDoubleArray(inVolume), 
	    		begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tPLUS_DI(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
		RetCode retCode = core.plusDI(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tPLUS_DM(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.plusDM(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tPPO(RollingWindow<Double> inReal, int fastPeriods, int slowPeriods, MAType maType) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.ppo(0, inReal.size() - 1, toDoubleArray(inReal), fastPeriods, slowPeriods,
	    		maType, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tROC(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.roc(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tROCP(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.rocP(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tROCR(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.rocR(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tROCR100(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.rocR100(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tRSI(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.rsi(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tSAR(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, double inAcc, 
			double inMax) {
		double[] out = new double[inHigh.size()];
	    RetCode retCode = core.sar(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		inAcc, inMax, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tSMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.sma(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tSTDDEV(int periods, RollingWindow<Double> inReal, double nbDev) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.stdDev(0, inReal.size() - 1, toDoubleArray(inReal), periods, nbDev,
	    		begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double[] tSTOCH(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, RollingWindow<Double> inClose, 
			int fastKPeriods, int slowKPeriods, MAType slowKMaType, int slowDPeriods, MAType slowDMaType) {
		double[] out = new double[2];
		double[] outSlowK = new double[inHigh.size()];
		double[] outSlowD = new double[inHigh.size()];
	    RetCode retCode = core.stoch(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose),
	    		fastKPeriods, slowKPeriods, slowKMaType, slowDPeriods, slowDMaType, begin, length, outSlowK, 
	    		outSlowD);
		if (retCode == RetCode.Success) {
			out[0] = outSlowK[0];
			out[1] = outSlowD[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double[] tSTOCHF(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, RollingWindow<Double> inClose, 
			int fastKPeriods, int fastDPeriods, MAType fastDMaType) {
		double[] out = new double[2];
		double[] outFastK = new double[inHigh.size()];
		double[] outFastD = new double[inHigh.size()];
		 RetCode retCode = core.stochF(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), toDoubleArray(inClose),
	    		fastKPeriods, fastDPeriods, fastDMaType, begin, length, outFastK, outFastD);
		if (retCode == RetCode.Success) {
			out[0] = outFastK[0];
			out[1] = outFastD[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double[] tSTOCHRSI(int periods, RollingWindow<Double> inReal, int fastKPeriods, int fastDPeriods, MAType fastDMaType) {
		double[] out = new double[2];
		double[] outFastK = new double[inReal.size()];
		double[] outFastD = new double[inReal.size()];
		 RetCode retCode = core.stochRsi(0, inReal.size() - 1, toDoubleArray(inReal), periods, fastKPeriods, fastDPeriods, 
				 fastDMaType, begin, length, outFastK, outFastD);
		if (retCode == RetCode.Success) {
			out[0] = outFastK[0];
			out[1] = outFastD[0];
			return out;
		}

		out[0] = errorCode;
		return out;
	}
	
	public double tT3(int periods, RollingWindow<Double> inReal, double vFactor) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.t3(0, inReal.size() - 1, toDoubleArray(inReal), periods, vFactor,
	    		begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tTEMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];    
	    RetCode retCode = core.tema(0, inReal.size() - 1, toDoubleArray(inReal), periods,
	    		begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tTRANGE(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
		RetCode retCode = core.trueRange(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tTRIMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.trima(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tTRIX(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.trix(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tTSF(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.tsf(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tULTOSC(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose, int periods0, int periods1, int periods2) {
		double[] out = new double[inHigh.size()];
		RetCode retCode = core.ultOsc(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods0, periods1, periods2, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tWCLPRICE(RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
		RetCode retCode = core.wclPrice(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tWILLR(int periods, RollingWindow<Double> inHigh, RollingWindow<Double> inLow, 
			RollingWindow<Double> inClose) {
		double[] out = new double[inHigh.size()];
		RetCode retCode = core.willR(0, inHigh.size() - 1, toDoubleArray(inHigh), toDoubleArray(inLow), 
	    		toDoubleArray(inClose), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}
	
	public double tWMA(int periods, RollingWindow<Double> inReal) {
		double[] out = new double[inReal.size()];
		RetCode retCode = core.wma(0, inReal.size() - 1, toDoubleArray(inReal), periods, begin, length, out);
		if (retCode == RetCode.Success)
			return out[0];

		return errorCode;
	}

	private static double[] toDoubleArray(RollingWindow<Double> rw) {
		double[] out = new double[rw.size()];
		for(int i = 0; i < rw.size(); i++)
			out[i] = (Double) rw.get(i);
		return out;
	}
}
