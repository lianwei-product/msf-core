package cn.com.connext.msf.data.mongo.converter;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;

public final class BigDecimalConverter {

    @WritingConverter
    public static class BigDecimalToDecimal128Converter implements Converter<BigDecimal, Decimal128> {

        public final static BigDecimalToDecimal128Converter INSTANCE = new BigDecimalToDecimal128Converter();

        @Override
        public Decimal128 convert(BigDecimal bigDecimal) {
            return new Decimal128(bigDecimal);
        }

    }

    @ReadingConverter
    public static class Decimal128ToBigDecimalConverter implements Converter<Decimal128, BigDecimal> {

        public final static Decimal128ToBigDecimalConverter INSTANCE = new Decimal128ToBigDecimalConverter();

        @Override
        public BigDecimal convert(Decimal128 decimal128) {
            return decimal128.bigDecimalValue();
        }

    }
}
