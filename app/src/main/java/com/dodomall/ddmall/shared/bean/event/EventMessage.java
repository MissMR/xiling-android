package com.dodomall.ddmall.shared.bean.event;

import com.dodomall.ddmall.shared.constant.Event;

public class EventMessage {

    protected Event event;
    protected Object data;

    public EventMessage(Event event) {
        this.event = event;
    }

    public EventMessage(Event event, Object data) {
        this.event = event;
        this.data = data;
    }

    public Event getEvent() {
        return event;
    }

    public Object getData() {
        return data;
    }
}
