package com.bloomhealthco.guestbook

import org.joda.time.LocalDateTime;

public class Guest {
    Long id
    String name
    String company
    Employee visiting
    LocalDateTime checkinTime
    LocalDateTime checkoutTime

    static Guest createFromDb(values) {
        new Guest(id: values.id, name: values.name, company: values.company, checkinTime: new LocalDateTime(values.checkin_time), checkoutTime: new LocalDateTime(values.checkout_time))
    }
}
