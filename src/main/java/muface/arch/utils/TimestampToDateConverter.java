package muface.arch.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.Timestamp;
import java.util.Date;

@WritingConverter
public class TimestampToDateConverter implements Converter<Timestamp, Date> {
    @Override
    public Date convert(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }
}
