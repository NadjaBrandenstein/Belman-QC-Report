package dk.easv.belmanqcreport.GUI.Model;
// Project Import
import dk.easv.belmanqcreport.BE.Log;
import dk.easv.belmanqcreport.BLL.Manager.LogManager;
// Java Imports
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class LogModel {

    private LogManager manager;

    public LogModel() throws IOException {
        manager = new LogManager();
    }

    public Log addLog(int orderItemId, String imagePosition, String action, String user) throws Exception {
        Log log = new Log();
        log.setOrderItemID(orderItemId);
        log.setImagePosition(imagePosition);
        log.setAction(action);
        log.setUsername(user);
        log.setTimestamp(LocalDateTime.now());      // or however you track time
        return manager.addLog(log);
    }

    public List<Log> getLogsForItem(int orderItemId) throws Exception {
        return manager.getLogsForItem(orderItemId);
    }

}
