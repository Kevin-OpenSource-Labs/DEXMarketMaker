package com.marketmaker.riskcontrol;

import java.util.Date;
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
			StockPosition position = m_account.StockPositionMap.get(symbol);
			if(Math.abs(position.TotalVolume - position.BaseVolume)>m_quoteParameter.AutoHedgeVolumeThreshold)
			{
				//to do hedge
				if(position.TotalVolume > position.BaseVolume)
				{
										
				}
				else
				{
					
				}
			}
		}
	}
	
	private DexMarketMaker m_dexMarketMaker = null;	
	private ExchangeBase m_exchange = null;
	private StockAccount m_account = null;
	private QuoteParameter m_quoteParameter = null;
}
