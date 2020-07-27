package com.marketmaker.exchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import com.marketmaker.account.StockAccount;
import com.marketmaker.fairvalue.MarketData;
import com.marketmaker.main.DexMarketMaker;
import com.marketmaker.types.Order;

public abstract class ExchangeBase {
	
	public abstract void updateMarketData(MarketData marketData);
	public abstract void updateStockAccountInfo(StockAccount account);
		
	public abstract boolean PlaceOrder(Order order);	
	public abstract void CancelOrder(Order order);	
	public abstract void UpdateOrder(Order order);	
	
	public static String httpGet(String httpUrl)
	{
		BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-type", "application/json"); 
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (SocketException e) {
            System.out.println("Connection timed out: connect");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;		
	}	
	
	protected DexMarketMaker m_dexMarketMaker = DexMarketMaker.getInstance();
}
