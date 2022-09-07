/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package limit_order_book;

/**
 *
 * @author KHAIDAR
 */
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

public class order_price_hashmap {
    
        TreeMap<Double, order_list> price_tree = new TreeMap<Double, order_list>();
	HashMap<Double, order_list> price_hashmap = new HashMap<Double, order_list>();
        int volume;
	int number_of_orders;
	int depth;
        HashMap<Integer, order> order_hashmap = new HashMap<Integer, order>();

	
	public Integer length() {
		return order_hashmap.size();
	}
	
	public order_list get_priceList(double price) {
		/*
		 * Returns the order_list object associated with 'price'
		 */
		return price_hashmap.get(price);
	}
	
	public order get_order(int id) {
		/*
		 * Returns the order given the order id
		 */
		return order_hashmap.get(id);
	}
	
	public void createPrice(double price) {
		depth += 1;
		order_list newList = new order_list();
		price_tree.put(price, newList);
		price_hashmap.put(price, newList);
	}
	
	public void remove_price(double price) {
		depth -= 1;
		price_tree.remove(price);
		price_hashmap.remove(price);
	}
	
        public void removeOrderByID(int id) {
        this.number_of_orders -=1;
        order order = order_hashmap.get(id);
        this.volume -= order.get_qtity();
        order.get_order_list().remove_order(order);
        if (order.get_order_list().get_length() == 0) {
                this.remove_price(order.get_price());
        }
        this.order_hashmap.remove(id);
	}
        
        
        
	public boolean priceExists(double price) {
		return price_hashmap.containsKey(price);
	}
	
	public boolean orderExists(int id) {
		return order_hashmap.containsKey(id);
	}
	
	public void insertorder(order quote) {
		int quoteID = quote.get_Id();
		double quotePrice = quote.get_price();

		number_of_orders += 1;
		if (!priceExists(quotePrice)) {
			createPrice(quotePrice);
		}
		quote.set_oL(price_hashmap.get(quotePrice));
		price_hashmap.get(quotePrice).append_order(quote);
		order_hashmap.put(quoteID, quote);
		volume += quote.get_qtity();
	}
	

	
	public void update_order(order orderUpdate) {

	}
	
	public Double max_price() {
		if (this.depth>0) {
			return this.price_tree.lastKey();
		} else {
			return null;
		}
	}
	
	public Double min_price() {
		if (this.depth>0) {
			return this.price_tree.firstKey();
		} else {
			return null;
		}
	}
	
	public order_list max_price_list() {
		if (this.depth>0) {
			return this.get_priceList(max_price());
		} else {
			return null;
		}
	}
	
	public order_list min_price_list() {
		if (this.depth>0) {
			return this.get_priceList(min_price());
		} else {
			return null;
		}
	}
	
	public String toString() {
		String outString = "| The Book:\n" + 
							"| Max price = " + max_price
() +
							"\n| Min price = " + min_price
() +
							"\n| Volume in book = " + getVolume() +
							"\n| Depth of book = " + getDepth() +
							"\n| orders in book = " + getnumber_of_orders() +
							"\n| Length of tree = " + length() + "\n";
		for (Map.Entry<Double, order_list> entry : this.price_tree.entrySet()) {
			outString += entry.getValue().toString();
			outString += ("|\n");
		}
		return outString;
	}
        
        public void updateOrderQty(int qty, int Id) {
            order order = this.order_hashmap.get(Id);
            int original_volume = order.get_qtity();
            order.update_target_qtity(qty, order.get_timestamp());
            this.volume += (order.get_qtity() - original_volume);
	}

	public Integer getVolume() {
		return volume;
	}

	public Integer getnumber_of_orders() {
		return number_of_orders;
	}

	public Integer getDepth() {
		return depth;
	}
    
}
