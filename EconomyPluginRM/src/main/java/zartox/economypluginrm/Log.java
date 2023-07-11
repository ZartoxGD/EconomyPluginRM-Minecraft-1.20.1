package zartox.economypluginrm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private String time;
    private String message;
    private String playerName;

    public Log(String _playerName, String _message){
        playerName = _playerName;
        message = _message;
        time = GetTime();

    }

    public String GetMessage(){
        return time + " -> [" + playerName + "] " + message + "\n";
    }

    private String GetTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
