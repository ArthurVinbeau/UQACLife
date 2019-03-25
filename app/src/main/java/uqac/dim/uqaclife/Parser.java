package uqac.dim.uqaclife;

import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

public class Parser  {
    public String toJson(String html) {
        String lessonPart = html.split("<FONT SIZE=\"-1\"><B>Toute la journée</B></FONT>")[1].split("<!-- fin contenu centrale -->")[0];
        String[] lessons = lessonPart.split("<hr><b>");
        ArrayList<ArrayList<Pair<Integer, String>>> week = new ArrayList<ArrayList<Pair<Integer, String>>>();
        for (int i = 0; i < 7; i++) {
            week.add(i, new ArrayList<Pair<Integer, String>>());
            week.get(i).add(0, new Pair<>(-1, "day" + i));
        }
        for (int i = 1; i < lessons.length; i++) {
            String name = lessons[i].split("</b>")[0];

            String[] times = lessons[i].split("<LI><b>");


            for (int j = 1; j < times.length; j++) {
                String dates = times[j].split("<li><b>")[0].split("</b> ")[1];
                String parsedDates = "#";
                if(dates.contains("Le")) {
                    parsedDates +=  dates.split(" ")[2] + " " + dates.split(" ")[2];
                }
                else
                {
                    String[] splited = dates.split(" ");
                    parsedDates += splited[2] + " " + splited[5];
                }


                String hour = times[j].split(" de ")[1].split("</li>")[0];
                String[] from = hour.split(" à ")[0].split(":");
                int timevalue = Integer.parseInt(from[0]) * 100 + Integer.parseInt(from[1]);


                String local = times[j].split("Local:</b> ")[1].split("</li>")[0];
                int index = 0;
                if (times[j].contains("Lundi"))
                    index = 0;
                else if (times[j].contains("Mardi"))
                    index = 1;
                else if (times[j].contains("Mercredi"))
                    index = 2;
                else if (times[j].contains("Jeudi"))
                    index = 3;
                else if (times[j].contains("Vendredi"))
                    index = 4;
                else if (times[j].contains("Samedi"))
                    index = 5;
                else if (times[j].contains("Dimanche"))
                    index = 6;


                String toAdd = name + "#" + hour + "#" + local+parsedDates;

                insertSorted(week.get(index), new Pair<>(timevalue, toAdd));
            }
        }
        //Normalement, ici on week avec chaque jour rempli par les cours dans l'ordre de la journee
        //Ils sont inscrits sous la forme id-grp - Nom du cours#hh:mm à hh:mm#local#period#TD
        String json = "{\n";
        for (int i = 0; i < 7; i++) {
            json += "\"";
            switch (i) {
                default:
                    break;
                case 0:
                    json += "Monday";
                    break;
                case 1:
                    json += "Tuesday";
                    break;
                case 2:
                    json += "Wednesday";
                    break;
                case 3:
                    json += "Thursday";
                    break;
                case 4:
                    json += "Friday";
                    break;
                case 5:
                    json += "Saturday";
                    break;
                case 6:
                    json += "Sunday";
                    break;
            }


            json += "\": [\n";
            for (int j = 1; j < week.get(i).size(); j++) {
                String a = week.get(i).get(j).second;

                String[] actualLesson = week.get(i).get(j).second.split("#");
                json += "{\n\"id\": \"" + actualLesson[0].split("-")[0]
                        + "\",\n\"name\": \"" + actualLesson[0].split(" - ")[1]
                        + "\",\n\"grp\": \"" + actualLesson[0].split(" - ")[0].split("-")[1]
                        + "\",\n\"start\": \"" + actualLesson[1].split(" à ")[0]
                        + "\",\n\"end\": \"" + actualLesson[1].split(" à ")[1]
                        + "\",\n\"dates\": \"" + actualLesson[3]
                        + "\",\n\"room\": \"" + actualLesson[2].replaceAll("&nbsp", "")
                        + "\"\n}" + ((j + 1 < week.get(i).size()) ? "," : "") + "\n";




                //{
                //      "id": "8GIF150",
                //      "name": "Conception de jeux vidéo",
                //      "grp": "01",
                //      "start": "08:00",
                //      "end": "10:45",
                //      "date" : "07-01-2019 22-04-2019",       //"19-02-2019 19-02-2019"
                //      "room": "P4-4020"
                //}
            }
            json += "]\n" + ((i == 6) ? "" : ",");
        }
        return json + "}";
    }

    private void insertSorted(ArrayList<Pair<Integer, String>> list, Pair<Integer, String> value) {
        int i = 0;
        int toAdd = value.first;
        while (i < list.size() && list.get(i).first < toAdd) {
            i++;
        }
        list.add(i, value);
    }
}