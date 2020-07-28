package com.marketmaker.riskcontrol;

import java.util.ArrayList;
import java.util.Date;

import com.marketmaker.types.Order;
import com.marketmaker.types.StockAccount;
import com.marketmaker.exchange.ExchangeBase;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.quote.QuoteParameter;
import com.marketmaker.types.StockPosition;

public class AutoHedgeMgr {
	
	public void Start()
	{
		m_dexMarketMaker = DexMarketMaker.getInstance();
		m_exchange = m_dexMarketMaker.getExchange();
		m_account = m_dexMarketMaker.getAccount();
		m_quoteParameter = m_dexMarketMaker.getQuoteParameter();
		
		long lastUpdateAccountTime = new Date().getTime() - 1000 * 1000;
		while(!m_dexMarketMaker.Stop())
		{			
			try {
				
				long now = new Date().getTime();				
				if(now - lastUpdateAccountTime>= 1000 * 10)	
				{
					SyncAccountInfo();
					lastUpdateAccountTime = now; 
				}	
				
				AutoHedgeIfNeed();
				
				Thread.sleep(1000);	
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
	}
	
	public void SyncAccountInfo()
	{
		m_exchange.updateStockAccountInfo(m_account);
	}
	
	public void AutoHedgeIfNeed()
	{
		String symbol = m_dexMarketMaker.getMarketDataMgr().MyMarketData.Symbol;
		if(m_account.StockPositionMap.containsKey(symbol))		
		{
			//cancel hedge orders and remove finished orders
			for(Order order : m_hedgeOrders)
			{
				m_exchange.UpdateOrder(order);
				if(order.IsFinished)
				{
					m_hedgeOrders.remove(order);						
				}
				else						
				{
					m_exchange.CancelOrder(order);
				}					
			}
			
			//to do hedge
			double exposeVolume = m_account.getExpose(symbol);
			if(Math.abs(exposeVolume)>m_quoteParameter.AutoHedgeVolumeThreshold)
			{				
				if(exposeVolume>0)
				{					
					Order sellOrder = new Order();
					sellOrder.Symbol = symbol;
					sellOrder.Price = m_dexMarketMaker.getMarketDataMgr().MyMarketData.DepthData.Bid[0]*0.99;
					sellOrder.Volume = exposeVolume;	
					if(m_exchange.PlaceOrder(sellOrder))
					{
						m_hedgeOrders.add(sellOrder);			
					}
				}
				else
				{
					Order buyOrder = new Order();
					buyOrder.Symbol = symbol;
					buyOrder.Price = m_dexMarketMaker.getMarketDataMgr().MyMarketData.DepthData.Ask[0]*1.01;
					buyOrder.Volume = exposeVolume;	
					if(m_exchange.PlaceOrder(buyOrder))
					{
						m_hedgeOrders.add(buyOrder);			
					}										
				}
			}
		}
	}
	
	private DexMarketMaker m_dexMarketMaker = null;	
	private ExchangeBase m_exchange = null;
	private StockAccount m_account = null;
	private QuoteParameter m_quoteParameter = null;
	private ArrayList<Order> m_hedgeOrders = new ArrayList<Order>();
}
