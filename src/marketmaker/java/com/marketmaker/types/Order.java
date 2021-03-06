package com.marketmaker.types;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Order {
	
	public String StrategyId;
	public String OrderId;
	public String OrderTime;	
	public String Symbol;
	public TradeDirection Direction;
	public double Volume;
	public double Price;
	public double TradedVolume;
	public double TradedPrice;
	public OrderState State;
	public boolean IsFinished;
	public String Message;
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)            
            .append("OrderTime", OrderTime)
            .append("OrderId", OrderId)
            .append("Symbol", Symbol)
            .append("Direction", Direction)
            .append("Volume", Volume)
            .append("Price", Price)
            .append("TradedVolume", TradedVolume)
            .append("TradedPrice", TradedPrice)
            .append("OrderState", State)
            .append("IsFinished", IsFinished)
            .append("Message", Message)
            .toString();
    }
}
