package com.marketmaker.exchange;

import com.marketmaker.types.StockAccount;
import com.marketmaker.types.StockPosition;
import com.marketmaker.types.TradeRecord;

import java.util.ArrayList;
import java.util.Date;

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
		//start mock trade thread
		if(m_mockTradeThread == null)
		{		
			startMockTradeThread();			
		}
		
		order.OrderId = order.OrderTime = (new Date()).toString();
		order.State = OrderState.Submitted;
		order.IsFinished = false;
		m_pendingOrders.add(order);
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
	
	private void startMockTradeThread()
	{
		m_mockTradeThread = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while(!m_dexMarketMaker.Stop())
				{						
					try 
					{
						for(Order order : m_pendingOrders)
						{								
							if(!order.IsFinished)	
							{
								//Mock two phase trades
								TradeRecord record = new TradeRecord();
								record.TradeTime = (new Date()).toString();
								record.OrderId = order.OrderId;
								record.Symbol = order.Symbol;
								record.Direction = order.Direction;
								record.TradedPrice = order.Price;										
								record.TradedVolume = order.Volume / 2;
								NotifyTrade(record);
								
								if(order.TradedVolume<=0)	
								{										
									order.TradedPrice = order.Price;
									order.TradedVolume = order.Volume / 2;
									order.State = OrderState.Partially_Filled;
									order.IsFinished = false;
								}
								else
								{
									order.TradedPrice = order.Price;
									order.TradedVolume = order.Volume;
									order.State = OrderState.Filled;
									order.IsFinished = true;
								}
							}
							if(order.IsFinished)
							{
								m_pendingOrders.remove(order);									
							}
						}
						Thread.sleep(2000);
					} 
					catch (InterruptedException e)
					{						
						e.printStackTrace();
					}						
				}
			}
		});
		m_mockTradeThread.start();
	}
	
	private Thread m_mockTradeThread = null;
	private ArrayList<Order> m_pendingOrders = new 	ArrayList<Order>();
}
