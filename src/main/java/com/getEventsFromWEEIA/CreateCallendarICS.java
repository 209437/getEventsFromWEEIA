package com.getEventsFromWEEIA;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class CreateCallendarICS {

    public CreateCallendarICS(WeeiaEventsCalendar events) {
        Calendar calendar = createCalendar();
        addEventsFrom(events.getEventsMap(), events.getMonth(), events.getYear(), calendar);
        createICSFile(events.getYear(), events.getMonth(), calendar);
    }

    /**
     * Tworzenie pliku z rozszerzeniem ICS z eventami
     * @param year
     * @param month
     * @param calendar
     */
    private void createICSFile(String year, String month, Calendar calendar) {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(year+"-"+month+".ics");
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dodawanie eventow do kalendarza
     *
     * @param eventsMap mapa z datami oraz nazwami eventow
     * @param month miesiac eventu
     * @param year rok eventu
     * @param calendar kalendarz ICS do ktorego dodajemy event
     */
    private void addEventsFrom(Map<String, String> eventsMap, String month, String year, Calendar calendar) {
        for (Map.Entry<String, String> entry : eventsMap.entrySet()) {

            try {
                java.util.Date date = new SimpleDateFormat("yyyy-mm-dd").parse(year+"-"+month+"-"+entry.getKey());
                java.util.Calendar eventCalendar = java.util.Calendar.getInstance();
                eventCalendar.setTime(date);

                // initialise as an all-day event..
                VEvent vEvent = new VEvent(new Date(eventCalendar.getTime()),entry.getValue());

                // Generate a UID for the event..
                RandomUidGenerator uid = new RandomUidGenerator();
                vEvent.getProperties().add(uid.generateUid());
                calendar.getComponents().add(vEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tworzenie kalendarza ICS
     *
     * @return cbiekt kalendarza ICS
     */
    private Calendar createCalendar() {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Weeia Calendar"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        return calendar;
    }
}
