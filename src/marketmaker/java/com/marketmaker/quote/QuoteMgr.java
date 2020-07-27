package com.marketmaker.quote;

import java.util.ArrayList;
import java.util.HashMap;

import com.marketmaker.exchange.ExchangeBase;
import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.types.Order;

public class QuoteMgr {
	
	public void Start()
	{		
		m_dexMarketMaker = DexMarketMaker.getInstance();
		m_quoteParameter = m_dexMarketMaker.getQuoteParameter();
		m_exchange = m_dexMarketMaker.getExchange();
		while(!m_dexMarketMaker.Stop())
		{			
			try 
			{
				synchronized(m_quoteEventObj)
				{
					m_quoteEventObj.wait();					
				}							
				MarketData marketData = m_dexMarketMaker.getMarketDataMgr().MyMarketData;
				if(marketData.IsNormal)
				{					
					double buyPrice = marketData.FairValue * (1 - m_quoteParameter.QuotePriceSpreadRatio);
					double sellPrice = marketData.FairValue* (1 + m_quoteParameter.QuotePriceSpreadRatio);	
					if(m_pendingOrders.size()<=0)
					{																								
					}
					else
					{
						m_exchange.CancelOrders(m_pendingOrders);	
						m_pendingOrders.clear();
					}
					//buy side
					{
						Order buyOrder = new Order();					
						buyOrder.Symbol = marketData.Symbol;
						buyOrder.Price = buyPrice;
						buyOrder.Volume = m_quoteParameter.QuoteVolume;					
						if(m_exchange.PlaceOrder(buyOrder))
						{
							m_buyPendingOrderMap.put(buyOrder.OrderId, buyOrder);						
							m_pendingOrders.add(buyOrder);																	
						}
					}
					//sell side
					{
						Order sellOrder = new Order();
						sellOrder.Symbol = marketData.Symbol;
						sellOrder.Price = sellPrice;
						sellOrder.Volume = m_quoteParameter.QuoteVolume;	
						if(m_exchange.PlaceOrder(sellOrder))
						{
							m_sellPendingOrderMap.put(sellOrder.OrderId, sellOrder);
							m_pendingOrders.add(sellOrder);					
						}
					}
				}
				else
				{				
					System.out.println("cancel all pending orders due to abnormal state ...");
					m_dexMarketMaker.getExchange().CancelOrders(m_pendingOrders);
				}
			} 
			catch (Exception e)
			{				
				e.printStackTrace();
			}												
		}
	}
	public Object getQuoteEventObject()
	{
		return m_quoteEventObj;
	}	
	
	private HashMap<String, Order> m_buyPendingOrderMap = new HashMap<String, Order>();
	private HashMap<String, Order> m_sellPendingOrderMap = new HashMap<String, Order>();
	private ArrayList<Order> m_pendingOrders = new ArrayList<Order>();
	
	private DexMarketMaker m_dexMarketMaker = null;	
	private ExchangeBase m_exchange = null;
	private QuoteParameter m_quoteParameter = null;
	private Object m_quoteEventObj = new Object();
}
