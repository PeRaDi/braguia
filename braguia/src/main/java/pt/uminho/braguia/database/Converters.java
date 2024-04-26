package pt.uminho.braguia.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {

    private static final String LIST_SEPARATOR = ",";

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date value) {
        return value == null ? null : value.getTime();
    }

    @TypeConverter
    public static List<String> stringListFromString(String value) {
        return value == null ? null : Arrays.stream(value.split(LIST_SEPARATOR)).collect(Collectors.toList());
    }

    @TypeConverter
    public static String stringListToString(List<String> value) {
        return value == null ? null : value.stream().collect(Collectors.joining(LIST_SEPARATOR));
    }

    @TypeConverter
    public static List<Integer> integerListFromString(String value) {
        return value == null ? null : Arrays.stream(value.split(LIST_SEPARATOR))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @TypeConverter
    public static String integerListToString(List<Integer> value) {
        return value == null ? null : value.stream()
                .map(v -> v.toString())
                .collect(Collectors.joining(LIST_SEPARATOR));
    }

}
