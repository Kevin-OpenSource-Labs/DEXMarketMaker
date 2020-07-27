package com.marketmaker.exchange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.marketmaker.account.StockAccount;
import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.fairvalue.MarketDepthData;
import com.marketmaker.types.Order;

public class ExchangeBinance extends ExchangeBase {

	@Override
	public void updateMarketData(MarketData marketData) {
		if(marketData.Symbol != null && marketData.Symbol != "")
		{
			String binanceHttpUrl = String.format("https://api.binance.com/api/v3/depth?symbol=%s&limit=5", 
					  marketData.Symbol.replaceAll("-", "").toUpperCase()); 
			String binanceJson = httpGet(binanceHttpUrl);
			if(binanceJson != null && binanceJson != "")
			{
				//parse binance market data
				JSONObject obj = (JSONObject)JSON.parse(binanceJson);	
				JSONArray asks = (JSONArray)obj.get("asks");
				JSONArray bids = (JSONArray)obj.get("bids");				
				MarketDepthData depthData = marketData.DepthData;				
				depthData.Ask[0] = asks.getJSONArray(0).getDoubleValue(0);							
				depthData.Bid[0] = bids.getJSONArray(0).getDoubleValue(0);				
				if(depthData.Ask[0]>0 && depthData.Bid[0]>0)
			    {
			    	depthData.Mid = (depthData.Ask[0] + depthData.Bid[0]) / 2;							    	
			    }
			}	
		}		
	}

	@Override
	public void updateStockAccountInfo(StockAccount account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean PlaceOrder(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void CancelOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void UpdateOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

}
