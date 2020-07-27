package com.marketmaker.types;

import java.util.HashMap;

public class StockAccount {
	
    public HashMap<String, StockPosition> StockPositionMap = new HashMap<String, StockPosition>();
	
	public void AdjustStockVolume(String symbol, double volume)
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
