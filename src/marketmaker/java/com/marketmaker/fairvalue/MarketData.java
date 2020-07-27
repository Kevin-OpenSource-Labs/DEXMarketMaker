package com.marketmaker.fairvalue;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MarketData {
	
	public boolean IsNormal = false;
	public String Symbol = null;
	public MarketDepthData DepthData = null;
	public double FairValue = 0;		
	
    public MarketData(String symbol)
    {
    	Symbol = symbol; 	   
    	DepthData = new MarketDepthData();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("Symbol", Symbol)
            .append("IsNormal", IsNormal)
            .append("FairValue", FairValue)
            .append("DepthData", DepthData)
            .toString();
    }
}
