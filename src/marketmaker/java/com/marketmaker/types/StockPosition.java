package com.marketmaker.types;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StockPosition {
	
	public String CoinName;
	public double AvailVolume;
	public double TotalVolume;
	public double BaseVolume;
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)            
            .append("CoinName", CoinName)
            .append("BaseVolume", BaseVolume)
            .append("TotalVolume", TotalVolume)
            .append("AvailVolume", AvailVolume)
            .toString();        
    }
}