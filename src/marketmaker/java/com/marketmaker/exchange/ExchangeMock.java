package com.marketmaker.exchange;

import com.marketmaker.types.StockAccount;
import com.marketmaker.types.StockPosition;

import java.util.ArrayList;

import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.types.Order;
import com.marketmaker.types.OrderState;

public class ExchangeMock extends ExchangeBase {
	
	@Override
	public void updateMarketData(MarketData marketData) {
		
		marketData.DepthData.Ask[0] = 1000 + 5 * Math.random();
		marketData.DepthData.AskVol[0] = 1;
		marketData.DepthData.Bid[0] = 1000 - 5 * Math.random();
		marketData.DepthData.BidVol[0] = 1;
		marketData.DepthData.Mid = 1000;
	}

	@Override
	public void updateStockAccountInfo(StockAccount account) {
		
		//first time to initialized
		if(account.StockPositionMap.size()<=0)
		{
			String symbol = DexMarketMaker.getInstance().getMarketDataMgr().MyMarketData.Symbol;
			StockPosition position = new StockPosition();			
			position.BaseVolume = position.TotalVolume = position.AvailVolume = 100;
			account.StockPositionMap.put(symbol, position);		
		}
	}
	
	@Override
	public boolean PlaceOrder(Order order)
	{		
		order.State = OrderState.Submitted;
		order.IsFinished = false;
	    return true;	
	}
	@Override
	public void CancelOrder(Order order)
	{	
		order.IsFinished = true;
		if(order.TradedVolume>=order.Volume)
		{
			order.State = OrderState.Filled;
		}
		else if(order.TradedVolume<=0)
		{
			order.State = OrderState.Cancelled;
		}
		else			
		{
			order.State = OrderState.Partially_Cancelled;	
		}
	}
	@Override
	public void UpdateOrder(Order order)
	{		
	}

	@Override
	public void CancelOrders(ArrayList<Order> orders) {
		for(Order order : orders)
		{
			CancelOrder(order);		
		}
	}	
}
