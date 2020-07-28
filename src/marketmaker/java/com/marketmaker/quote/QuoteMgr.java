package com.marketmaker.quote;

import java.util.ArrayList;
import java.util.HashMap;

import com.marketmaker.exchange.ExchangeBase;
import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.types.Order;
import com.marketmaker.types.OrderState;
import com.marketmaker.types.StockAccount;

public class QuoteMgr {
	
	public void Start()
	{		
		m_dexMarketMaker = DexMarketMaker.getInstance();
		m_quoteParameter = m_dexMarketMaker.getQuoteParameter();
		m_exchange = m_dexMarketMaker.getExchange();
		m_account = m_dexMarketMaker.getAccount();
		while(!m_dexMarketMaker.Stop())
		{			
			try 
			{
				synchronized(m_quoteEventObj)
				{
					m_quoteEventObj.wait();					
				}		
				//update pending order containers
				for(Order order : m_pendingOrders)
				{
					if(order.IsFinished || order.State == OrderState.Cancelling)
					{
						m_buyPendingOrderMap.remove(order.OrderId);
						m_sellPendingOrderMap.remove(order.OrderId);
						m_pendingOrders.remove(order);
					}
				}
				
				//Quote strategy
				MarketData marketData = m_dexMarketMaker.getMarketDataMgr().MyMarketData;
				if(marketData.IsNormal)
				{					
					double buyPrice = marketData.FairValue * (1 - m_quoteParameter.QuotePriceSpreadRatio);
					double sellPrice = marketData.FairValue* (1 + m_quoteParameter.QuotePriceSpreadRatio);	
					
					//Anti arbitrate adjust
					if(buyPrice >= marketData.DepthData.Ask[0])
					{
						buyPrice = marketData.DepthData.Ask[0];
					}
					if(sellPrice <= marketData.DepthData.Bid[0])
					{
						sellPrice = marketData.DepthData.Bid[0];
					}
					//To do... Other checks...
					
					//quote					
					double exposeVolume = m_account.getExpose(marketData.Symbol);
					double buyVolume = m_quoteParameter.QuoteVolume - exposeVolume;
					double sellVolume = m_quoteParameter.QuoteVolume + exposeVolume;
					
					//Cancel order too far from target price
					if(m_buyPendingOrderMap.size()>0)
					{						
						for(Order order : m_buyPendingOrderMap.values())
						{
							if(Math.abs(order.Price-buyPrice)/buyPrice>m_quoteParameter.QuotePriceRatioThreshold)
							{									
								m_exchange.CancelOrder(order);
							}
						}							
					}	
					if(m_sellPendingOrderMap.size()>0)
					{
						for(Order order : m_sellPendingOrderMap.values())
						{
							if(Math.abs(order.Price-sellPrice)/sellPrice>m_quoteParameter.QuotePriceRatioThreshold)
							{									
								m_exchange.CancelOrder(order);
							}								
						}																					
					}
					double minVolume = m_quoteParameter.QuoteVolume * m_quoteParameter.QuoteVolumeRatioThreshold;
					//buy order
					if(buyVolume > minVolume)
					{
						Order buyOrder = new Order();					
						buyOrder.Symbol = marketData.Symbol;
						buyOrder.Price = buyPrice;
						buyOrder.Volume = buyVolume;					
						if(m_exchange.PlaceOrder(buyOrder))
						{
							m_buyPendingOrderMap.put(buyOrder.OrderId, buyOrder);						
							m_pendingOrders.add(buyOrder);																	
						}
					}
					//sell order
					if(sellVolume>minVolume)
					{
						Order sellOrder = new Order();
						sellOrder.Symbol = marketData.Symbol;
						sellOrder.Price = sellPrice;
						sellOrder.Volume = sellVolume;	
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
	private StockAccount m_account = null;
	private QuoteParameter m_quoteParameter = null;
	private Object m_quoteEventObj = new Object();
}
