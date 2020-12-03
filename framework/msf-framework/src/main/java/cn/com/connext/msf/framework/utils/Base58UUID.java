package cn.com.connext.msf.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Base58UUID {

    public final static String EMPTY = "0000000000000000";
    public final static String EMPTY_UUID = "00000000-0000-0000-0000-000000000000";

    public static String newBase58UUID() {
        UUID uuid = UUID.randomUUID();
        return convertFromUUID(uuid);
    }

    public static String newRawUUID() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    public static String convertFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base58Encoder.encode(bb.array());
    }

    public static String convertFromUUIDString(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return convertFromUUID(uuid);
    }

    public static String convertToUUID(String base58uuid) {
        if (EMPTY.equals(base58uuid)) return EMPTY_UUID;
        byte[] byUuid = Base58Encoder.decode(base58uuid);
        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }
}
