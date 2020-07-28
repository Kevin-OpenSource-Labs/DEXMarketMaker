package com.marketmaker.types;

import java.util.HashMap;

public class StockAccount {
	
    public HashMap<String, StockPosition> StockPositionMap = new HashMap<String, StockPosition>();
	
    public double getExpose(String symbol)
    {
    	double exposeVolume = 0;
    	if(StockPositionMap.containsKey(symbol))
    	{
    		StockPosition position = StockPositionMap.get(symbol);
    		return position.TotalVolume - position.BaseVolume;	
    	}
    	return exposeVolume;
    }
	public void adjustStockVolume(String symbol, double volume)
	{
		if(!StockPositionMap.containsKey(symbol))
		{
			StockPosition position = new StockPosition();
			StockPositionMap.put(symbol, position);
		}
		StockPosition position = StockPositionMap.get(symbol);
		position.TotalVolume += volume;
		position.AvailVolume += volume;
	}
}
