package dk.easv.belmanqcreport.BLL.Manager;
// Project Imports
import dk.easv.belmanqcreport.BE.Log;
import dk.easv.belmanqcreport.DAL.Database.LogRepository;
// Java Imports
import java.io.IOException;
import java.util.List;

public class LogManager {

    private final LogRepository logRepository;

    public LogManager() throws IOException {
        logRepository = new LogRepository();
    }

    public Log addLog(Log log) throws Exception {
        return logRepository.add(log);
    }

    public List<Log> getLogsForItem(int orderItemId) throws Exception {
        return logRepository.getByOrderItem(orderItemId);}

}
