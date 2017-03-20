package lab0;

import java.util.Arrays;

public interface IAddForm<T> {
    T getRecord();

    Boolean[] isFieldValid();

    default boolean isRecordValid() {
        return Arrays.stream(isFieldValid()).reduce(true, (a, b) -> a && b);
    }
}
