/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package limit_order_book;

/**
 *
 * @author KHAIDAR
 */
import java.math.BigDecimal;
import java.util.ArrayList;

// TODO for precision, change prices from double to java.math.BigDecimal

public class order_book {
	private order_price_hashmap bids = new order_price_hashmap();
	private order_price_hashmap asks = new order_price_hashmap();
	private double tick_size;
	private int time;
	private int nextQuoteID;
	private int lastOrderSign;
	
	public order_book(double tick_size) {
		this.tick_size= tick_size;
	}

	
	
	/**
	 * Clips price according to tick_size

	 * 
	 * @param price
	 * @return
	 */
	private double clipPrice(double price) {
		int log_price = (int)Math.log10(1 / this.tick_size);
		BigDecimal price_decimal = new BigDecimal(price);
		BigDecimal rounded = price_decimal.setScale(log_price, BigDecimal.ROUND_HALF_UP);
		return rounded.doubleValue();
	}
	
	
	public void processOrder(order quote) {
		boolean isLimit = quote.is_limit();
		// Update time
		this.time = quote.get_timestamp();
		if (quote.get_qtity() <= 0 ) {
			throw new IllegalArgumentException("Negative quantity");
		}
		if (isLimit) {
			double clippedPrice = clipPrice(quote.get_price());
			quote.set_price(clippedPrice);
			processLimitOrder(quote);
		} else {
			processMarketOrder(quote);
		}
	}
	
	
	private void processMarketOrder(order quote) {
		Boolean is_bid = quote.get_is_bid();
		int qtyRemaining = quote.get_qtity();
		if (is_bid) {
			this.lastOrderSign = 1;
			while ((qtyRemaining > 0) && (this.asks.getnumber_of_orders() > 0)) {
				order_list ordersAtBest = this.asks.min_price_list();
				qtyRemaining = processOrderList(ordersAtBest, qtyRemaining,quote);
			}
		}else {
			this.lastOrderSign = -1;
			while ((qtyRemaining > 0) && (this.bids.getnumber_of_orders() > 0)) {
				order_list ordersAtBest = this.bids.max_price_list();
				qtyRemaining = processOrderList(ordersAtBest, qtyRemaining,quote);
			}
		}
	}
	
	
	private void processLimitOrder(order quote) {
		boolean order_in_book = false;
                boolean is_bid = quote.get_is_bid();
		int qtyRemaining = quote.get_qtity();
		double price = quote.get_price();
		if (is_bid) {
			this.lastOrderSign = 1;
			while ((this.asks.getnumber_of_orders() > 0) && 
					(qtyRemaining > 0) && 
					(price >= asks.min_price())) {
                                order_list ordersAtBest = asks.min_price_list();
				qtyRemaining = processOrderList(ordersAtBest, qtyRemaining,quote);
			}
			
			if (qtyRemaining > 0) {
				quote.set_Id(this.nextQuoteID);
				quote.setQuantity(qtyRemaining);
				this.bids.insertorder(quote);
				this.nextQuoteID+=1;
			} else {
			}
		} else{
			this.lastOrderSign = -1;
			while ((this.bids.getnumber_of_orders() > 0) && 
					(qtyRemaining > 0) && 
					(price <= bids.max_price())) {
				order_list ordersAtBest = bids.max_price_list();
				qtyRemaining = processOrderList(ordersAtBest, qtyRemaining,quote);	
			}
			// If volume remains, add to book
			if (qtyRemaining > 0) {
				quote.set_Id(this.nextQuoteID);
				quote.setQuantity(qtyRemaining);
				this.asks.insertorder(quote);
				order_in_book = true;
				this.nextQuoteID+=1;
			} else {
				order_in_book = false;
			}
		} 

	}
	
	
	
	
	public int getVolumeAtPrice(boolean is_bid, double price) {
		price = clipPrice(price);
		int vol = 0;
		if(is_bid) {
			if (bids.priceExists(price)) {
				vol = bids.get_priceList(price).getVolume();
			}
		} else{
			if (asks.priceExists(price)) {
				vol = asks.get_priceList(price).getVolume();
			}
		}
		return vol;
		
	}
	
	public double getBestBid() {
		return bids.max_price();
	}
	
	public double getWorstBid() {
		return bids.min_price();
	}
	
	public double getBestOffer() {
		return asks.min_price();
	}
	
	public double getWorstOffer() {
		return asks.max_price();
	}
	
	public int getLastOrderSign() {
		return lastOrderSign;
	}
	
	public int volumeOnSide(boolean is_bid) {
		if (is_bid) {
			return this.bids.getVolume();
		} else {
			return this.asks.getVolume();
		}
	}
	
	public double get_tick_size() {
		return tick_size
;
	}
	
	public double get_bid_ask_spread() {
		return this.asks.min_price()-this.bids.max_price();
	}
	
	public double getMid() {
		return this.getBestBid()+(this.get_bid_ask_spread()/2.0);
	}
	

	
	public String toString() {
                return null;
	}


	
}
