package com.mfc.infra.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.sql.Timestamp;
import java.util.Date;

@ReadingConverter
public class DateToTimestampConverter implements Converter<Date, Timestamp> {
    @Override
    public Timestamp convert(Date date) {
        return new Timestamp(date.getTime());
    }
}
