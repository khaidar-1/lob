package limit_order_book;

  
/**
 * order class that defines the order format;
 * takes 2 shapes of orders
 * @author Karim Haidar
 * @version (a version number or a date)
 */

public class order
{
    // instance variables - replace the example below with your own
    private int time; // order time
    private boolean is_limit; // is limit order or not
    private int target_qtity; // order quantity
    private double price; // Price ; if Null , execute at market
    private boolean is_bid;    
    private int Id;
    private order next_order; // Next order in the list
    private order prev_order; // Previous order
    private orderlist order_list;

    /**
     * order object that stores time, type of order, qutity
     * @param time Time 
     * @param is_limit is a limit order if true, market order if not
     * @param target_qtity target quantity of order
     * @param is_bid    Is bid or ask - bid if True !bid=ask
     * @param Id        order OD
     */
    public order(int time, boolean is_limit, int target_qtity, int Id,boolean is_bid) 
    {
		this(time, is_limit, target_qtity, Id, is_bid,null);
    }
    
    public order(int time, boolean is_limit, int target_qtity, 
				int Id, boolean is_bid,Double price)
    {
        // initialise instance variables
        this.target_qtity=target_qtity;
        this.is_limit=is_limit;
        this.time=time;
        this.Id=Id;
        if (price!=null){
            this.price= price;
        }
        
    }

    /**
     * Update volume and priority for LoB
     *
     * @param  updated_qtity  a sample parameter for a method
     * @param  tstamp   timestamp of orders 
     * @param 
     */
    public void update_target_qtity(int updated_qtity, int tstamp) {
		if ((updated_qtity > this.target_qtity) && (this.order_list.get_tail_order() != this)) {
			// check position of order and move to tail
			this.order_list.move_tail(this);
			this.time = tstamp;
		}
		order_list.setVolume(order_list.getVolume()-(this.target_qtity-updated_qtity));
		this.target_qtity = updated_qtity;
	}
	
	public String toString() {
	String trade="";
        String limit_or_market="";
	    if (is_bid){
	     trade="bid";
        }else{
             trade="ask";
        }
        
        if (is_limit){
	     limit_or_market="limit order";
        }else{
             limit_or_market="market order";
        }
        
        return Integer.toString(target_qtity) + "\t@\t" + Double.toString(price) + " " + trade + " "+ limit_or_market +
        		"\tt=" + Integer.toString(time) + 
        		"\ttId=" + Integer.toString(Id);
    }

	
	// Getters and Setters
	public order get_next_order() {
		return next_order;
	}

	public void set_next_order(order next_order) {
		this.next_order = next_order;
	}

	public order get_prev_order() {
		return prev_order;
	}

	public void set_prev_order(order prev_order) {
		this.prev_order = prev_order;
	}

	public Integer get_timestamp() {
		return time;
	}

	public void setTimestamp(int timestamp) {
		this.time = timestamp;
	}

	public int get_qtity() {
		return this.target_qtity;
	}

	public void setQuantity(int target_qtity) {
		this.target_qtity = target_qtity;
	}

	public double get_price() {
		return this.price;
	}

	public void set_price(double price) {
		this.price = price;
	}

	public int get_Id() {
		return this.Id;
	}

	public void set_Id(int Id) {
		this.Id = Id;
	}
        
	public boolean get_is_bid(){
		return this.is_bid;
	}

	public void set_is_bid(boolean is_bid) {
		this.is_bid = is_bid;
	}


	public orderlist get_order_list() {
		return order_list;
	}

	public boolean is_limit() {
		return is_limit;
	}


	public void setoL(orderlist order_list) {
		this.order_list= order_list;
	}
}
