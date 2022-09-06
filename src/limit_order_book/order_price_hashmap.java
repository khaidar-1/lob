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
    
    TreeMap<Double, order_list> priceTree = new TreeMap<Double, order_list>();
	HashMap<Double, order_list> priceMap = new HashMap<Double, order_list>();;
	HashMap<Integer, order> orderMap = new HashMap<Integer, order>();
	int volume;
	int norders;
	int depth;
	
	public order_price_hashmap() {
		reset();
	}
	
	public void reset() {
		priceTree.clear();
		priceMap.clear();
		orderMap.clear();
		volume = 0;
		norders = 0;
		depth = 0;
	}
	
	public Integer length() {
		return orderMap.size();
	}
	
	public order_list get_priceList(double price) {
		/*
		 * Returns the order_list object associated with 'price'
		 */
		return priceMap.get(price);
	}
	
	public order getorder(int id) {
		/*
		 * Returns the order given the order id
		 */
		return orderMap.get(id);
	}
	
	public void createPrice(double price) {
		depth += 1;
		order_list newList = new order_list();
		priceTree.put(price, newList);
		priceMap.put(price, newList);
	}
	
	public void removePrice(double price) {
		depth -= 1;
		priceTree.remove(price);
		priceMap.remove(price);
	}
	
	public boolean priceExists(double price) {
		return priceMap.containsKey(price);
	}
	
	public boolean orderExists(int id) {
		return orderMap.containsKey(id);
	}
	
	public void insertorder(order quote) {
		int quoteID = quote.get_Id();
		double quotePrice = quote.get_price();

		norders += 1;
		if (!priceExists(quotePrice)) {
			createPrice(quotePrice);
		}
		quote.setoL(priceMap.get(quotePrice));
		priceMap.get(quotePrice).append_order(quote);
		orderMap.put(quoteID, quote);
		volume += quote.get_qtity();
	}
	

	
	public void update_order(order orderUpdate) {

	}
	
	public Double maxPrice() {
		if (this.depth>0) {
			return this.priceTree.lastKey();
		} else {
			return null;
		}
	}
	
	public Double minPrice() {
		if (this.depth>0) {
			return this.priceTree.firstKey();
		} else {
			return null;
		}
	}
	
	public order_list maxPriceList() {
		if (this.depth>0) {
			return this.get_priceList(maxPrice());
		} else {
			return null;
		}
	}
	
	public order_list minPriceList() {
		if (this.depth>0) {
			return this.get_priceList(minPrice());
		} else {
			return null;
		}
	}
	
	public String toString() {
		String outString = "| The Book:\n" + 
							"| Max price = " + maxPrice() +
							"\n| Min price = " + minPrice() +
							"\n| Volume in book = " + getVolume() +
							"\n| Depth of book = " + getDepth() +
							"\n| orders in book = " + getnorders() +
							"\n| Length of tree = " + length() + "\n";
		for (Map.Entry<Double, order_list> entry : this.priceTree.entrySet()) {
			outString += entry.getValue().toString();
			outString += ("|\n");
		}
		return outString;
	}

	public Integer getVolume() {
		return volume;
	}

	public Integer getnorders() {
		return norders;
	}

	public Integer getDepth() {
		return depth;
	}
    
}
