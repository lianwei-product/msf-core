package cn.com.connext.msf.data.mongo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 时间类型转换器，数据库的时间类型和实体类中定义的类型不同，需要通过转换器进行映射
 */
public final class JSR310DateConverter {

    private JSR310DateConverter() {
    }

    @WritingConverter
    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        public static final JSR310DateConverter.LocalDateToDateConverter INSTANCE = new JSR310DateConverter.LocalDateToDateConverter();

        private LocalDateToDateConverter() {
        }

        @Override
        public Date convert(LocalDate source) {
            return source == null ? null : Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    @ReadingConverter
    public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {

        public static final JSR310DateConverter.DateToLocalDateConverter INSTANCE = new JSR310DateConverter.DateToLocalDateConverter();

        private DateToLocalDateConverter() {
        }

        @Override
        public LocalDate convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault())
                    .toLocalDate();
        }
    }

    @WritingConverter
    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        public static final JSR310DateConverter.ZonedDateTimeToDateConverter INSTANCE = new JSR310DateConverter.ZonedDateTimeToDateConverter();

        private ZonedDateTimeToDateConverter() {
        }

        @Override
        public Date convert(ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    @ReadingConverter
    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        public static final JSR310DateConverter.DateToZonedDateTimeConverter INSTANCE = new JSR310DateConverter.DateToZonedDateTimeConverter();

        private DateToZonedDateTimeConverter() {
        }

        @Override
        public ZonedDateTime convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    @WritingConverter
    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

        public static final JSR310DateConverter.LocalDateTimeToDateConverter INSTANCE = new JSR310DateConverter.LocalDateTimeToDateConverter();

        private LocalDateTimeToDateConverter() {
        }

        @Override
        public Date convert(LocalDateTime source) {
            return source == null ? null : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    @ReadingConverter
    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        public static final JSR310DateConverter.DateToLocalDateTimeConverter INSTANCE = new JSR310DateConverter.DateToLocalDateTimeConverter();

        private DateToLocalDateTimeConverter() {
        }

        @Override
        public LocalDateTime convert(Date source) {
            return source == null ? null : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }
}
