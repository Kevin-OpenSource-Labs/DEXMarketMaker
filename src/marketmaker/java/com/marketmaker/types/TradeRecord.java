package com.marketmaker.types;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TradeRecord {
	public String TradeTime;
	public String OrderId;
	public String Symbol;
	public TradeDirection Direction;
	public double TradedVolume;
	public double TradedPrice;
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)            
            .append("OrderTime", TradeTime)
            .append("OrderId", OrderId)
            .append("Symbol", Symbol)            
            .append("Direction", Direction)
            .append("TradedVolume", TradedVolume)
            .append("TradedPrice", TradedPrice)
            .toString();
    }
	
}
