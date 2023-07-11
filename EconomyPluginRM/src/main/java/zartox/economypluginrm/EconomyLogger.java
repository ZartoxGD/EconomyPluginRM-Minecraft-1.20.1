package zartox.economypluginrm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EconomyLogger {

    private static ArrayList<Log> logsToSave = new ArrayList<>();

    private static void CreateTodaysLogFile() throws IOException {
        File directory = new File("EconomyLogs");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, GetTodaysDate() + ".txt");
        file.createNewFile();
    }

    private static String GetTodaysDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void AddLog(Log log) throws IOException {
        logsToSave.add(log);
        if(logsToSave.size() >= 50){
            SaveLogs();
        }
    }

    public static void SaveLogs() throws IOException {
        CreateTodaysLogFile();
        FileWriter w = new FileWriter("EconomyLogs/" + GetTodaysDate() + ".txt", true);

        for(Log log : logsToSave){
            w.append(log.GetMessage());
        }

        w.close();
        logsToSave.clear();
        System.out.println("Saved EconomyLogs !");
    }
}
