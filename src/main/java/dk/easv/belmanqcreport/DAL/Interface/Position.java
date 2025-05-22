package dk.easv.belmanqcreport.DAL.Interface;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public enum Position {

    TOP(1), FRONT(2), BACK(3), LEFT(4), RIGHT(5), EXTRA(6);

    private final int dbId;

    Position(int dbId) {
        this.dbId = dbId; }

    public int getDbId() { return dbId; }

    private static final Map<Integer, Position> byId = Arrays.stream(values()).collect(toMap(Position::getDbId, p->p));

    public static Position fromDbId(int dbId) {
        return byId.getOrDefault(dbId, EXTRA);
    }
}
