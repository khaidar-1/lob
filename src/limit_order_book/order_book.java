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

public class order_book {
	private order_price_hashmap bids = new order_price_hashmap();
	private order_price_hashmap asks = new order_price_hashmap();
	private double tick_size;
	private int time;
	private int next_quote_Id;
	private int lastOrderSign;
	
	public order_book(double tick_size) {
		this.tick_size= tick_size;
	}

	
	
	public void execute_order(order quote) {
		boolean isLimit = quote.is_limit();
		// Update time
		this.time = quote.get_timestamp();
		if (quote.get_qtity() <= 0 ) {
			throw new IllegalArgumentException("Negative quantity");
		}
		if (isLimit) {
			double clippedPrice = price_to_tick(quote.get_price());
			quote.set_price(clippedPrice);
			execute_limit_order(quote);
		} else {
			execute_market_order(quote);
		}
	}
	
	
	private void execute_market_order(order quote) {
		Boolean is_bid = quote.get_is_bid();
		int qtyRemaining = quote.get_qtity();
		if (is_bid) {
			this.lastOrderSign = 1;
			while ((qtyRemaining > 0) && (this.asks.getnumber_of_orders() > 0)) {
				order_list order_at_best_price = this.asks.min_price_list();
				qtyRemaining = processOrderList(order_at_best_price, qtyRemaining,quote);
			}
		}else {
			this.lastOrderSign = -1;
			while ((qtyRemaining > 0) && (this.bids.getnumber_of_orders() > 0)) {
				order_list order_at_best_price = this.bids.max_price_list();
				qtyRemaining = processOrderList(order_at_best_price, qtyRemaining,quote);
			}
		}
	}
	
	
	private void execute_limit_order(order quote) {
		boolean order_in_book = false;
                boolean is_bid = quote.get_is_bid();
		int qtyRemaining = quote.get_qtity();
		double price = quote.get_price();
		if (is_bid) {
			this.lastOrderSign = 1;
			while ((this.asks.getnumber_of_orders() > 0) && 
					(qtyRemaining > 0) && 
					(price >= asks.min_price())) {
                                order_list order_at_best_price = asks.min_price_list();
				qtyRemaining = processOrderList(order_at_best_price, qtyRemaining,quote);
			}
			
			if (qtyRemaining > 0) {
				quote.set_Id(this.next_quote_Id);
				quote.setQuantity(qtyRemaining);
				this.bids.insertorder(quote);
				this.next_quote_Id+=1;
			} else {
			}
		} else{
			this.lastOrderSign = -1;
			while ((this.bids.getnumber_of_orders() > 0) && 
					(qtyRemaining > 0) && 
					(price <= bids.max_price())) {
				order_list order_at_best_price = bids.max_price_list();
				qtyRemaining = processOrderList(order_at_best_price, qtyRemaining,quote);	
			}
			// If volume remains, add to book
			if (qtyRemaining > 0) {
				quote.set_Id(this.next_quote_Id);
				quote.setQuantity(qtyRemaining);
				this.asks.insertorder(quote);
				order_in_book = true;
				this.next_quote_Id+=1;
			} else {
				order_in_book = false;
			}
		} 

	}
	
	
	
	
	public int get_volume_at_price(boolean is_bid, double price) {
		price = price_to_tick(price);
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
        
        private double price_to_tick(double price) {
        int log_price = (int)Math.log10(1 / this.tick_size);
        BigDecimal price_decimal = new BigDecimal(price);
        BigDecimal rounded = price_decimal.setScale(log_price, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
	}
        
        private int processOrderList( order_list orders,int qtyRemaining, order quote) {
		boolean is_bid = quote.get_is_bid();
		int buyer, seller;
		int takerId = quote.get_Id();
		int time = quote.get_timestamp();
		while ((orders.get_length()>0) && (qtyRemaining>0)) {
			int qtyTraded = 0;
			order headOrder = orders.getHeadorder();
			if (qtyRemaining < headOrder.get_qtity()) {
				qtyTraded = qtyRemaining;
				if (is_bid) {
					this.bids.updateOrderQty(headOrder.get_qtity()-qtyRemaining, 
											 headOrder.get_Id());
				} else {
					this.asks.updateOrderQty(headOrder.get_qtity()-qtyRemaining, 
											 headOrder.get_Id());
				}
				qtyRemaining = 0;
			} else {
				qtyTraded = headOrder.get_qtity();
				if (is_bid) {
					this.bids.removeOrderByID(headOrder.get_Id());
				} else {
					this.asks.removeOrderByID(headOrder.get_Id());
				}
				qtyRemaining -= qtyTraded;
			}
			if (is_bid) {
				buyer = headOrder.get_Id();
				seller = takerId;
			} else {
				buyer = takerId;
				seller = headOrder.get_Id();
			}
		}
		return qtyRemaining;
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
		return tick_size;
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
