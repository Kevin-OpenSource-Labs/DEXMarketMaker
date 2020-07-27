package com.marketmaker.main;

import com.marketmaker.exchange.ExchangeBase;
import com.marketmaker.exchange.ExchangeMock;
import com.marketmaker.fairvalue.MarketDataMgr;
import com.marketmaker.quote.QuoteMgr;
import com.marketmaker.quote.QuoteParameter;
import com.marketmaker.riskcontrol.AutoHedgeMgr;
import com.marketmaker.types.StockAccount;

public class DexMarketMaker {
	
	public static void main(String args[]) {		
		
		m_inst = getInstance();		
		m_inst.Start(new ExchangeMock(), "btc-usdt");		
        //marketDataMgr.Stop();
    }
	
	public static DexMarketMaker getInstance()
	{
		if(m_inst == null)
		{
			synchronized(m_lockObj)
			{
				if(m_inst == null)
				{
					m_inst = new DexMarketMaker();				
				}								
			}
		}
		return m_inst;
	}	
	public MarketDataMgr getMarketDataMgr()
	{
		return m_marketDataMgr;
	}
	public QuoteMgr getQuoteMgr()
	{
		return m_quoteMgr;
	}
	public AutoHedgeMgr getAutoHedgeMgr()
	{
		return m_hedgeMgr;
	}
	public ExchangeBase getExchange()
	{
		return m_exchange;		
	}
	public StockAccount getAccount()
	{
		return m_account;
	}
	public QuoteParameter getQuoteParameter()
	{
		return m_quoteParameter;		
	}
    public Thread getQuoteThread()
	{
		return m_quoteThread;
	}
	public boolean Stop()
	{
		return m_stop;		
	}
	
	public void Start(ExchangeBase exchange, String symbol)
	{	
		m_exchange = exchange;
		m_account = new StockAccount();
		m_marketDataMgr = new MarketDataMgr();
		m_quoteMgr = new QuoteMgr();	
		m_hedgeMgr = new AutoHedgeMgr();
		
		m_quoteParameter = new QuoteParameter();
		m_quoteParameter.QuoteVolume = 1;
		m_quoteParameter.QuoteVolumeRatioThreshold = 0.01;
		m_quoteParameter.QuotePriceSpreadRatio = 0.01;
		m_quoteParameter.QuotePriceRatioThreshold = 0.005;
		m_quoteParameter.AutoHedgeVolumeThreshold = 1;		
		
		//quote logic
		m_quoteThread = new Thread(new Runnable() 
		{
			@Override
			public void run() {
			m_quoteMgr.Start();
		}});
		m_quoteThread.start();
				
		//Update fair value logic
		m_marketDataThread = new Thread(new Runnable() 
		{
			@Override
			public void run() {
			m_marketDataMgr.Start(symbol);
		}});
		m_marketDataThread.start();
		
				
		//Hedge logic
		m_hedgeThread = new Thread(new Runnable() 
		{
			@Override
			public void run() {
				m_hedgeMgr.Start();
		}});
		m_hedgeThread.start();
	}	
	private static DexMarketMaker m_inst = null;
	private static Object m_lockObj = new Object();	
	private ExchangeBase m_exchange = null;
	private MarketDataMgr m_marketDataMgr = null;	
	private QuoteMgr m_quoteMgr = null;
	private AutoHedgeMgr m_hedgeMgr = null;	
	private StockAccount m_account = null;
	private Thread m_marketDataThread = null;
	private Thread m_quoteThread = null;
	private Thread m_hedgeThread = null;
	private QuoteParameter m_quoteParameter = null;
	private boolean m_stop = false;
}
