import com.bloomhealthco.guestbook.DAOModule
import com.bloomhealthco.guestbook.Employee
import com.bloomhealthco.guestbook.Guest
import com.bloomhealthco.guestbook.GuestbookService
import org.joda.time.LocalDateTime

import ratpack.groovy.templating.Template
import ratpack.groovy.templating.TemplatingModule
import static ratpack.groovy.Groovy.*


ratpack {

    modules {
        register new DAOModule(new File('./config', 'Config.groovy'))
    }

    handlers { GuestbookService service ->

        get {
            List employees = service.listEmployees()
            List guests = service.listGuests()
            render groovyTemplate("skin.html", employees: employees, guests: guests)
        }

        get("checkin") {
            println request.queryParams
            Long employeeId = request.queryParams.visiting_id as Long
            Employee employee = service.getEmployee(employeeId)
            println employee
            Guest guest = service.addGuest(new Guest(name: request.queryParams.name, company: request.queryParams.company, visiting: employee))
            println guest

            render groovyTemplate("checkin.html", guest: guest)
        }

        // make more restful
        get("checkout") {
            Guest guest = service.getGuest(request.queryParams.id as Long)
            guest.checkoutTime = new LocalDateTime()
            service.updateGuest(guest)
            response.send guest.checkoutTime.toString("MM/dd/yyyy h:mm aaa")
        }

        // make more restful
        get("delete") {
            Guest guest = service.getGuest(request.queryParams.id as Long)
            service.deleteGuest(guest)
            response.send 'OK'
        }

        get("club") {

            response.send 'TEST'
        }

        assets "public"
    }
}


