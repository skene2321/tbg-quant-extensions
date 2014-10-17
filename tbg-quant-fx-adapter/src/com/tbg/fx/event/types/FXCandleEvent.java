package com.tbg.fx.event.types;

import com.fxcore2.O2GMarketDataSnapshotResponseReader;

public class FXCandleEvent {
	private double ask;
	private double askLow;
	private double askHigh;
	private double askOpen;
	private double askClose;
	private double bid;
	private double bidLow;
	private double bidHigh;
	private double bidOpen;
	private double bidClose;
	private int volume;
	private String symbol;
	private long timeStamp;
	
	public FXCandleEvent(O2GMarketDataSnapshotResponseReader reader, int i, String symbol) {
		this.ask = reader.getAsk(i);
		this.askLow = reader.getAskLow(i);
		this.askHigh = reader.getAskHigh(i);
		this.askOpen = reader.getAskOpen(i);
		this.askClose = reader.getAskClose(i);
		this.bid = reader.getBid(i);
		this.bidLow = reader.getBidLow(i);
		this.bidHigh = reader.getBidHigh(i);
		this.bidOpen = reader.getBidOpen(i);
		this.bidClose = reader.getBidClose(i);
		this.volume = reader.getVolume(i);
		this.timeStamp = reader.getDate(i).getTimeInMillis();
		this.symbol = symbol;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}

	public double getAskLow() {
		return askLow;
	}

	public void setAskLow(double askLow) {
		this.askLow = askLow;
	}

	public double getAskHigh() {
		return askHigh;
	}

	public void setAskHigh(double askHigh) {
		this.askHigh = askHigh;
	}

	public double getAskOpen() {
		return askOpen;
	}

	public void setAskOpen(double askOpen) {
		this.askOpen = askOpen;
	}

	public double getAskClose() {
		return askClose;
	}

	public void setAskClose(double askClose) {
		this.askClose = askClose;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getBidLow() {
		return bidLow;
	}

	public void setBidLow(double bidLow) {
		this.bidLow = bidLow;
	}

	public double getBidHigh() {
		return bidHigh;
	}

	public void setBidHigh(double bidHigh) {
		this.bidHigh = bidHigh;
	}

	public double getBidOpen() {
		return bidOpen;
	}

	public void setBidOpen(double bidOpen) {
		this.bidOpen = bidOpen;
	}

	public double getBidClose() {
		return bidClose;
	}

	public void setBidClose(double bidClose) {
		this.bidClose = bidClose;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "CandleEvent [ask=" + ask + ", askLow=" + askLow + ", askHigh="
				+ askHigh + ", askOpen=" + askOpen + ", askClose=" + askClose
				+ ", bid=" + bid + ", bidLow=" + bidLow + ", bidHigh="
				+ bidHigh + ", bidOpen=" + bidOpen + ", bidClose=" + bidClose
				+ ", volume=" + volume + ", symbol=" + symbol + "]";
	}
}
