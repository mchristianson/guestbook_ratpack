package com.bloomhealthco.guestbook

import groovy.sql.Sql
import org.joda.time.LocalDateTime

class GuestbookService {

    private final Sql sql

    GuestbookService(Sql sql) {
       this.sql = sql
    }

    List<Employee> listEmployees() {
        List<Employee> employeeList = sql.rows('SELECT * FROM employee ORDER BY sort_order, last_name desc').collect { dbRow ->
            Employee.createFromDb(dbRow)
        }
        employeeList
    }

    Employee getEmployee(Long id) {
        Employee.createFromDb(sql.firstRow('SELECT * FROM employee WHERE id = ?', [id]))
    }

    List<Guest> listGuests() {
        List<Guest> guestList = sql.rows('SELECT * FROM guest ORDER BY checkin_time desc').collect { dbRow ->
            Guest guest = Guest.createFromDb(dbRow)
            guest.visiting = getEmployee(dbRow.visiting_id as Long)
            guest
        }
        guestList
    }

    Guest getGuest(Long id) {
        Guest.createFromDb(sql.firstRow('SELECT * FROM guest WHERE id = ?', [id]))
    }

    Guest addGuest(Guest guest) {
        Employee employee = getEmployee(guest.visiting.id)
        guest.visiting = employee
        String sqlInsert = 'INSERT INTO guest (name, company, visiting_id, checkin_time)VALUES(?, ?, ? ,now())'
        def keys = sql.executeInsert(sqlInsert, [guest.name,guest.company, guest.visiting.id])
        guest.id = keys.flatten()[0] as Long
        guest
    }

    Guest updateGuest(Guest guest) {
        sql.executeUpdate('''
                UPDATE guest set
                    name = ?,
                    company = ?,
                    visiting_id = ?,
                    checkin_time = ?,
                    checkout_time = ?
                WHERE id = ?
                ''', [
                guest.name,
                guest.company,
                guest.visiting.id,
                guest.checkinTime,
                guest.checkoutTime,
                guest.id
            ])
        guest
    }

    boolean deleteGuest(Guest guest) {
        sql.execute('delete from guest where id = ?', [guest.id])
    }
}
