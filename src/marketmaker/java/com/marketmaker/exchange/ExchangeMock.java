package com.marketmaker.exchange;

import com.marketmaker.account.StockAccount;
import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.types.Order;

public class ExchangeMock extends ExchangeBase {
	
	@Override
	public void updateMarketData(MarketData marketData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateStockAccountInfo(StockAccount account) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean PlaceOrder(Order order)
	{
	    return false;	
	}
	@Override
	public void CancelOrder(Order order)
	{		
	}
	@Override
	public void UpdateOrder(Order order)
	{	
		
	}	
}
