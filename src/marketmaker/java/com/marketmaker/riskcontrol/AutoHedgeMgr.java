package com.marketmaker.riskcontrol;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import com.marketmaker.account.StockAccount;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.types.StockPosition;

public class AutoHedgeMgr {
	
	public StockAccount Account = new StockAccount();
	
	public void Start()
	{
		m_dexMarketMaker = DexMarketMaker.getInstance();
		long lastUpdateAccountTime = new Date().getTime() - 1000 * 1000;
		while(!m_dexMarketMaker.Stop())
		{			
			try {
				
				long now = new Date().getTime();				
				if(now - lastUpdateAccountTime>=1000 * 10)	
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
		m_dexMarketMaker.getExchange().updateStockAccountInfo(Account);
	}
	
	public void AutoHedgeIfNeed()
	{
		for(String key : Account.StockPositionMap.keySet()) 
		{
			StockPosition position = Account.StockPositionMap.get(key);
			if(Math.abs(position.TotalVolume - position.BaseVolume)>=1)
			{
				
			}
		}
	}
	
	private DexMarketMaker m_dexMarketMaker = null;		
}
