package com.bdstar.taxi.events;

public class OrderCreateEvent {
    private int index;
    private String orderId;

    public OrderCreateEvent(){}

    public OrderCreateEvent(int index, String orderId){
        this.index = index;
        this.orderId = orderId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
