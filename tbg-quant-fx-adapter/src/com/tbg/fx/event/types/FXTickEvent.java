package com.tbg.fx.event.types;

import com.fxcore2.O2GOfferRow;

public class FXTickEvent {
	private double ask;
	private double bid;
	private String symbol;
	private double high;
	private double low;
	private int volume;
	private long timeStamp;
	
	public FXTickEvent(O2GOfferRow row) {
		this.ask = row.getAsk();
		this.bid = row.getBid();
		this.symbol = row.getInstrument();
		this.high = row.getHigh();
		this.low = row.getLow();
		this.volume = row.getVolume();
		this.timeStamp = row.getTime().getTimeInMillis();
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}
	
	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "TickEvent [ask=" + ask + ", bid=" + bid + ", symbol="
				+ symbol + ", high=" + high + ", low=" + low + ", volume="
				+ volume + "]";
	}
}
