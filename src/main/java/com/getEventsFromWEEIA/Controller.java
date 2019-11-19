package com.getEventsFromWEEIA;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Http request handler class
 */
@RestController
public class Controller {

    @RequestMapping("/getCalendar/{year}/{month}")
    public String getDetailsAboutString(@PathVariable String year, @PathVariable String month) {
        WeeiaEventsCalendar events = new WeeiaEventsCalendar(year, month);
        new CreateCallendarICS(events);
        return "Plik z wydarzeniami zostal pomyslnie utworzony!";
    }
}
