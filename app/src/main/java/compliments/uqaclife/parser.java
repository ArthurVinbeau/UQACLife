public string toJson ( string html){
    string lessonPart = html.split("<FONT SIZE="-1"><B>Toute la journée</B></FONT>")[1].split("<!-- fin contenu centrale -->")[0];
    string[] lessons = lessonPart.split(<hr><b>)
    ArrayList<ArrayList<Pair<int, string>>> week;
    for(int i = 0; i<7 ; i++){
        week.add(i,Pair<-1,"day" + i>)
    }
    for(int i = 1 ; i < lessons.length ; i++)
    {
        string name = lessons[i].split("</b>")[0];
        string[] times = lessons[i].split("<LI><b>");
        for (int j = 1 ; j < times.length ; j ++ ){
            string hour = times[j].split(" de ")[1].split("</li>")[0];
            string[] from = hour.split(" à ")[0].split(":");
            int timevalue = Integer.parseInt(from[0])*100 + Integer.parseInt.(from[1]);
            string local = times.split("Local:</b> ")[1].split("</li>")[0];
            int index;
            if(java.lang.String.contains("Lundi"))
                index = 0;
            else if (java.lang.String.contains("Mardi"))
                index = 1;
            else if (java.lang.String.contains("Mercredi"))
                index = 2;
            else if (java.lang.String.contains("Jeudi"))
                index = 3;
            else if (java.lang.String.contains("Vendredi"))
                index = 4;
            else if (java.lang.String.contains("Samedi"))
                index = 5;
            else if (java.lang.String.contains("Dimanche"))
                index = 6;
            insertSorted(week[index],Pair<timevalue,name +"|" + hour + "|" + local>);
        }
    }
    //Normalement, ici on week avec chaque jour rempli par les cours dans l'ordre de la journee
    //Ils sont inscrits sous la forme id-grp - Nom du cours|hh:mm à hh:mm|local
    string json = "{\n";
    for(int i = 0 ; i < 7 ; i++)
    {
        json += "\""
        switch (i){
            default:
                break;
            case 0: json += "monday";
                break;
            case 0: json += "tuesday";
                break;
            case 0: json += "wednesday";
                break;
            case 0: json += "thursday";
                break;
            case 0: json += "friday";
                break;
            case 0: json += "saturday";
                break;
            case 0: json += "sunday";
                break;
        }
        json += "\": [\n";
        for(int j = 1 ; j < week[i].length ; j++){
            string[] actualLesson = week[i][j].split("|");
            json += "{\n\"id\": \""+ actualLesson[0].split("-")[0] + "\",\n\"name\": \"" + actualLesson[0].split(" - ")[1] 
            + "\",\n\"grp\": \"" + actualLesson[0].split(" - ")[0].split("-")[1] + "\",\n\"start\": \"" + +"\",\n\"end\": \""
            + + "\",\n\"room\": \"" + + "\"\n}" + ((j+1 <  week[i].length)?",":"") +"\n";
        /*{ 
            "id": "8GIF150",
            "name": "Conception de jeux vidéo",
            "grp": "01",
            "start": "08:00",
            "end": "10:45",
            "room": "P4-4020"
        }*/
        }
        json += "]" + ((i==6)?"":",");
    }
    return json + "}";
}

private void insertSorted(ArrayList<Pair<int,string>> list,Pair<int,string> value){
    int i = 0;
    int toAdd = value.getkey();
    while(i < list.length && list[i][0] < toAdd){
        i++;
    }
    list.add(i,value);
}
