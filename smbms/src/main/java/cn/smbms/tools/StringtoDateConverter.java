package cn.smbms.tools;

import org.springframework.core.convert.converter.Converter;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-09-04 09:22:32
 **/
public class StringtoDateConverter implements Converter<String, Date> {
    private String pattern;

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Date convert(String s) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        try {
            date= dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
