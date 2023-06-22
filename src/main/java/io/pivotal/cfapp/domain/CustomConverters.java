package io.pivotal.cfapp.domain;

import java.util.List;

public class CustomConverters {

    public static List<Object> get() {
        return List.of(
            new QueryPolicyReadConverter(),
            new QueryPolicyWriteConverter()
        );
    }
}
