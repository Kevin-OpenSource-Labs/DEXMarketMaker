package com.marketmaker.fairvalue;

import java.util.Date;

import com.marketmaker.exchange.ExchangeBase;
import com.marketmaker.exchange.ExchangeBinance;
import com.marketmaker.exchange.ExchangeMock;
import com.marketmaker.exchange.ExchangeOKEX;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.quote.QuoteMgr;

public class MarketDataMgr {
	
	public MarketData MyMarketData = null;
	
	public void Start(final String symbol)
	{		
		m_dexMarketMaker = DexMarketMaker.getInstance();
		ExchangeOKEX okexExchange = new ExchangeOKEX();
		ExchangeBinance binanceExchange = new ExchangeBinance();
		MyMarketData = new MarketData(symbol);
		while(!m_dexMarketMaker.Stop())
		{
			try 			
			{
				ExchangeBase exchange = m_dexMarketMaker.getExchange();
				QuoteMgr quoteMgr = m_dexMarketMaker.getQuoteMgr();
				Object quoteEventObj = quoteMgr.getQuoteEventObject();
				//mock trade
				if(exchange instanceof ExchangeMock)
				{					
					exchange.updateMarketData(MyMarketData);			
					MyMarketData.FairValue = MyMarketData.DepthData.Mid + 10 * Math.random();
					MyMarketData.IsNormal = true;
					MyMarketData.DepthData.UpdateTime = new Date();					
					synchronized(quoteEventObj)
					{
						quoteEventObj.notify();		
					}
				}
				//real trade
				else
				{					
					MarketData okexMarketData = new MarketData(symbol);
					okexExchange.updateMarketData(okexMarketData);
					MarketData binanceMarketData = new MarketData(symbol);
					binanceExchange.updateMarketData(binanceMarketData);				
					if(okexMarketData.DepthData.Mid>0 && binanceMarketData.DepthData.Mid>0)
					{					
						//calculate fair value and market state
						double avgMidPrice = (okexMarketData.DepthData.Mid + binanceMarketData.DepthData.Mid) / 2;																
						MyMarketData.FairValue = avgMidPrice;	
						if(Math.abs(okexMarketData.DepthData.Mid - binanceMarketData.DepthData.Mid) / 2 >= 0.01)
						{
							MyMarketData.IsNormal = false;
						}
						else
						{
							MyMarketData.IsNormal = true;								
						}
						MyMarketData.DepthData.UpdateTime = new Date();
						synchronized(quoteEventObj)
						{							
							quoteEventObj.notify();	
						}						
					}
				}
				Thread.sleep(50);
			}
			catch (Exception e)
			{						
				e.printStackTrace();
		    }			
		}
	}	 
	
	private DexMarketMaker m_dexMarketMaker = null;
}
