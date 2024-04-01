package org.cftoolsuite.cfapp.domain;

import java.time.LocalDateTime;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Indexed;

import io.r2dbc.spi.Row;

@Indexed
@ReadingConverter
public class SpaceUsersReadConverter implements Converter<Row, SpaceUsers> {

    @Override
    public SpaceUsers convert(Row source) {
        return
                SpaceUsers
                .builder()
                .foundation(Defaults.getColumnValue(source, "foundation", String.class))
                .organization(Defaults.getColumnValue(source, "organization", String.class))
                .space(Defaults.getColumnValue(source, "space", String.class))
                .auditors(Defaults.getColumnListOfStringValue(source, "auditors"))
                .developers(Defaults.getColumnListOfStringValue(source, "developers"))
                .managers(Defaults.getColumnListOfStringValue(source, "managers"))
                .collectionDateTime(Defaults.getColumnValue(source, "collection_time", LocalDateTime.class))
                .build();
    }

}
