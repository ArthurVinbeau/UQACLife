package uqac.dim.uqaclife;

import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;


public class Parser2 {

    //Return the json corresponding to the html for lesson list
    public String toJson(String html) {
        //Array of <start int value,lesson> ordered by start int value in array of day of the week
        ArrayList<ArrayList<Pair<Integer, String>>> week = new ArrayList<ArrayList<Pair<Integer, String>>>();
        for (int i = 0; i < 7; i++) {
            week.add(i, new ArrayList<Pair<Integer, String>>());
            week.get(i).add(0, new Pair<>(-1, "day" + i));
        }

        //Remove unuseful tags end
        html = html.replaceAll("</span>","")
                .replaceAll("</li>","")
                .replaceAll("</div>","")
                .replaceAll("</ul>","");

        //Split for each lesson names
        //lessons[0] is garbage
        String[] lessons = html.split("<div class=\"card-header");
        for (int i = 1; i< lessons.length ; i++){
            String[] tmp =lessons[i].split("<div")[0].split("<span>");   //get  lesson name and id in tmp[1] and tmp[3]
            String cours = tmp[1] + " -" + tmp[3].split("\n")[0]; //cours = id -name
            String[] heures = lessons[i].split("<div class=\"card-body p-1 text-small\">")[1].split("<ul class=\"list-unstyled\">"); //split informations foreach lesson heures[0] is garbage
            for (int j = 1 ; j < heures.length; j++){

                String[] infos = heures[j].split("<li>");
                //Infos[0] contains garbage
                //Infos[1] contains period
                //Infos[2] contains hour
                //Infos[3] contains room
                //Infos[4] contains garbage


                int index = 0;//index to save in the array
                if (infos[1].contains("lundi")) //set array according to day in period
                    index = 0;
                else if (infos[1].contains("mardi"))
                    index = 1;
                else if (infos[1].contains("mercredi"))
                    index = 2;
                else if (infos[1].contains("jeudi"))
                    index = 3;
                else if (infos[1].contains("vendredi"))
                    index = 4;
                else if (infos[1].contains("samedi"))
                    index = 5;
                else if (infos[1].contains("dimanche"))
                    index = 6;

                tmp = infos[1].split("<span>");
                String period = tmp[2].split(" ")[1] + " " + tmp[4].split(" ")[1]; //Get start day and end day in period
                tmp = infos [2].split("<span>");
                String[] start = tmp[2].split(":"); //get start hour of lesson
                int timevalue = Integer.parseInt(start[0])*100 + Integer.parseInt(start[1].split("\n")[0]); //get starthour value as int to compare and order

                String duration = tmp[2].split("\n")[0] + " à "+ tmp[4].split("\n")[0]; //get start and end hour
                String local = null;
                try {               //Sometimes, local isn't in html
                    local = infos[3].split("<span>")[1].split("\n")[0] + (infos[3].contains("T.D") ? "T.D" : (infos[3].contains("LAB") ? "LAB" : "")); //Saves local and T.D or LAB
                } catch (Exception e){
                    local = "Non renseignée";
                    Log.i("HTML error", e.toString(),e);
                }

                String toAdd = cours + "#" + duration + "#" + local  + "#"+ period;
                //Save lesson in array as  id-grp - Nom du cours#hh:mm à hh:mm#local#period
                insertSorted(week.get(index), new Pair<>(timevalue, toAdd));
            }

        }

        //Week is filled for each day in hour ascending order
        //each lesson is saved as as  id-grp - Nom du cours#hh:mm à hh:mm#local#period
        //Creates JSON from the Week Array
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

    //Insert in list value in pair Integer ascending order
    private void insertSorted(ArrayList<Pair<Integer, String>> list, Pair<Integer, String> value) {
        int i = 0;
        int toAdd = value.first;
        while (i < list.size() && list.get(i).first < toAdd)
            i++;
        list.add(i, value);
    }
}
