package org.cftoolsuite.cfapp.domain;

import java.util.List;

public class CustomConverters {

    public static List<Object> get() {
        return List.of(
            new AppDetailReadConverter(),
            new AppDetailWriteConverter(),
            new QueryPolicyReadConverter(),
            new QueryPolicyWriteConverter(),
            new ServiceInstanceDetailReadConverter(),
            new ServiceInstanceDetailWriteConverter(),
            new SpaceUsersReadConverter(),
            new SpaceUsersWriteConverter()
        );
    }
}
