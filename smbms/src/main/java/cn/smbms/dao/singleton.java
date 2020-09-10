package cn.smbms.dao;

/**
 * @program: smbms_maven
 * @description:
 * @author: 擎天柱
 * @create: 2020-08-31 09:50:26
 **/
public class singleton {
    private static singleton s;
    public singleton(){}
    public static singleton getInstance(){
        if(s ==null){
            s=new singleton();
        }
        return s;
    }
}
