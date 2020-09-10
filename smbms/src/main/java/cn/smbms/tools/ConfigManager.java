package cn.smbms.tools;

import cn.smbms.dao.BaseDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-08-31 10:09:38
 **/
public class ConfigManager {
    private static ConfigManager configManager = new ConfigManager();
    private Properties properties;
    private ConfigManager(){
        //加载属性文件
        properties=new Properties();
        String configFile = "database.properties";
        InputStream is= BaseDao.class.getClassLoader().getResourceAsStream(configFile);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class InitInstance{
        private static ConfigManager instance = new ConfigManager();
    }
    //饿汉模式
    public static  ConfigManager getInstance(){
        configManager=InitInstance.instance;
        return configManager;
    }
    //懒汉模式
//    public static synchronized ConfigManager getInstance(){//加上synchronized线程安全
//        if(configManager==null){
//            configManager=new ConfigManager();
//        }
//        return configManager;
//    }
    public String getValue(String key){
        return properties.getProperty(key);
    }

}
