package com.getEventsFromWEEIA;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
public class WeeiaEventsCalendar {

    private URL URL;
    private Map<String, String> eventsMap = new TreeMap<String, String>();
    private final String year;
    private final String month;

    public WeeiaEventsCalendar(String year, String month) {
        this.year = year;
        this.month = month;
        try {
            URL = new URL("http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php?rok=" + this.year + "&miesiac=" + this.month + "&lang=1");
            getEvents();
            System.out.println(eventsMap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get events from weeia url and save it in TreeMap
     */
    private void getEvents() {
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) URL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                if (inputLine.contains("<a class='active'")) {
                    inputLine = inputLine.replace("</tr><tr class='dzien'>", "");

                    while (inputLine.length() != 0) {
                        String dzien = inputLine.substring(inputLine.indexOf("<td"), inputLine.indexOf("</td>") + 5);

                        if (dzien.contains("InnerBox")) {
                            String day = dzien.substring(dzien.indexOf("void();\">") + 9, dzien.indexOf("</a>"));
                            String event = dzien.substring(dzien.indexOf("<p>") + 3, dzien.indexOf("</p>"));
                            eventsMap.put(day, event);
                        }
                        inputLine = inputLine.replace(dzien, "");
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getEventsMap() {
        return eventsMap;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
