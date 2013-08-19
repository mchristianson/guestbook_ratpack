package com.bloomhealthco.guestbook

/**
 * Created by mchristianson on 8/15/13.
 */
class Employee {
    Long id
    String firstName
    String lastName
    String email
    Integer sortOrder

    static Employee createFromDb(values) {
        new Employee(id: values.id?:null,
                firstName: values.first_name?:null,
                lastName: values.last_name?:null,
                email: values.email?:null,
                sortOrder: values.sort_order?:null)
    }

}
