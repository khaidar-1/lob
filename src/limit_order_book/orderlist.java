package limit_order_book;

 
/**
 * Write a description of class  here.
 *
 * @author Karim Haidar
 * @version 03/9/2022
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class orderlist implements Iterable<order>, Iterator<order>{
	/*
	 * This class create a sorted, iterable list or orders for each price  level
	 * in the order tree.
	 */
	private order head_order = null;
	private order tail_order = null;
	private int length = 0;
	private int volume = 0;    // Total volume at this price level
	private order last = null;
	
	// The next three methods implement Iterator.
	public boolean hasNext() {
		if (this.last==null){
			return false;
	    }  
	    return true;
	}
	
	public order next() {
	    if (this.last == null) {
	    	throw new NoSuchElementException();
	    }
	    order returnVal = this.last;
	    this.last = this.last.get_next_order();
	    return returnVal;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	  
	// This method implements Iterable.
	public Iterator<order> iterator() {
		this.last = head_order ;
		return this;
	}
	
	public void appendOrder(order  incoming_order) {
		if (length == 0) {
			incoming_order.set_next_order(null);
			incoming_order.set_prev_order(null);
			head_order = incoming_order;
			tail_order = incoming_order;
		} else{
			incoming_order.set_prev_order(tail_order);
			incoming_order.set_next_order(null);
			tail_order.set_next_order(incoming_order);
			tail_order = incoming_order;
		}
		length+=1;
		volume+=incoming_order.get_qtity();
	}
	
	public void remove_order(order  order) {
		this.volume -= order.get_qtity();
		this.length -= 1;
		if (this.length == 0) {
			return;
		}
		order temp_next_order = order.get_next_order();
		order temp_prev_order = order.get_prev_order();
		if ((temp_next_order != null) && (temp_prev_order != null)) {
			temp_next_order.set_prev_order(temp_prev_order);
			temp_prev_order.set_next_order(temp_next_order);
		} else if (temp_next_order != null){
			temp_next_order.set_prev_order(null);
			this.head_order = temp_next_order;
		} else if (temp_prev_order != null){
			temp_prev_order.set_next_order(null);
			this.tail_order = temp_prev_order;
		}
	}
	
	public void move_tail(order  order) {
		/*
		 * Move 'order' to the tail of the list (after modification for e.g.)
		 */
		if (order .get_prev_order() != null) {
			order.get_prev_order().set_next_order(order.get_next_order());
		} else {
			// Update head order
			this.head_order = order.get_next_order();
		}
		order.get_next_order().set_prev_order(order .get_prev_order());
		// Set the previous tail's next order to this order
		this.tail_order.set_next_order(order );
		order.set_prev_order(this.tail_order);
		this.tail_order = order;
		order.set_next_order(null);
	}
	
	public String toString() {
		String outString = "";
		for (order  o : this) {
			outString += ("| " + o.toString()+"\n");
		}
		return outString;
	}
	
	
	public Integer getLength() {
		return length;
	}

	public order getHeadOrder() {
		return head_order ;
	}

	public order get_tail_order() {
		return tail_order;
	}

	public void set_tail_order(order  tail_order) {
		this.tail_order = tail_order;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
}