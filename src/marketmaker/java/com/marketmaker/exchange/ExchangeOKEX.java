package com.marketmaker.exchange;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.marketmaker.types.StockAccount;
import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.fairvalue.MarketDepthData;
import com.marketmaker.types.Order;

public class ExchangeOKEX extends ExchangeBase {

	@Override
	public void updateMarketData(MarketData marketData) {		
		if(marketData.Symbol != null && marketData.Symbol != "")
		{
			String okexHttpUrl = String.format("https://www.okex.com/api/spot/v3/instruments/%s/book?size=5", marketData.Symbol);
			String okexJson = httpGet(okexHttpUrl);
			if(okexJson != null && okexJson != "")
			{
				//parse okex market data
				JSONObject obj = (JSONObject)JSON.parse(okexJson);	
				JSONArray asks = (JSONArray)obj.get("asks");
				JSONArray bids = (JSONArray)obj.get("bids");
				MarketDepthData depthData = marketData.DepthData;
				for(int i=0; i<5; i++)
				{
					JSONArray askItem = asks.getJSONArray(i);
					if(null != askItem)
					{
						depthData.Ask[i] = askItem.getDouble(0);
						depthData.AskVol[i] = askItem.getDoubleValue(1);							
					}
					JSONArray bidItem = bids.getJSONArray(i);
					if(null != bidItem)
					{
						depthData.Bid[i] = bidItem.getDouble(0);
						depthData.BidVol[i] = bidItem.getDoubleValue(1);								
					}
				}	
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

	@Override
	public void CancelOrders(ArrayList<Order> orders) {
		// TODO Auto-generated method stub
		
	}

}
