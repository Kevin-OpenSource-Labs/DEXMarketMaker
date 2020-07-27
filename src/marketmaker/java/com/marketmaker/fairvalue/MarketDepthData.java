package com.marketmaker.fairvalue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MarketDepthData {
	
	public double Mid;
	public double[] Bid = null;
	public double[] BidVol = null;
	public double[] Ask = null;
	public double[] AskVol = null;	
	
	public Date UpdateTime = null;
		
	public MarketDepthData()
	{
		Bid = new double[5];
		BidVol = new double[5];
		Ask = new double[5];
		AskVol = new double[5];	
		UpdateTime  = new Date();
	}
	
	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)            
            .append("UpdateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(UpdateTime))
            .append("Mid", Mid)
            .toString();        
    }
}
