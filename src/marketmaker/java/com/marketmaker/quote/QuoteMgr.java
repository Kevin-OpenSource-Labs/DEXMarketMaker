package com.marketmaker.quote;

import java.util.HashMap;

import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.types.Order;

public class QuoteMgr {
	
	public void Start()
	{		
		m_dexMarketMaker = DexMarketMaker.getInstance();	
		MarketData marketData = m_dexMarketMaker.getMarketDataMgr().MyMarketData;
		while(!m_dexMarketMaker.Stop())
		{			
			if(marketData.IsNormal)
			{
				double buyPrice = marketData.FairValue;
				double sellPrice = marketData.FairValue;
												
			}
			else
			{				
				//m_dexMarketMaker.getExchange().CancelOrder(order);
			}								
		}
	}
	
	private HashMap<String, Order> m_buyPendingOrderMap = new HashMap<String, Order>();
	private HashMap<String, Order> m_sellPendingOrderMap = new HashMap<String, Order>();	
	private DexMarketMaker m_dexMarketMaker = null;	
}
