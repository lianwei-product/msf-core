package cn.com.connext.msf.framework.piidata;

public interface PIIDataEncoder {

    String encode(String plainText);

    String decode(String encodedText);

    String decodeMobile(String encodedText);

    String decodePhone(String encodedText);

    String decodeAddress(String encodedText);
}
