package dao;

import bean.Computer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class DaoFactoryImpl implements DaoFactory{
    private static DaoFactoryImpl instance =null;

    private static Properties prop=new Properties();
    private DaoFactoryImpl() {
        InputStream in= null;
        try {
            in = new BufferedInputStream(new
                    FileInputStream("resource/data.properties"));
            prop.load(in);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Object createobject(String classname){
        Class<?> clz= null;
        try {
            clz = Class.forName(classname);
            return clz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public static DaoFactory getInstance(){
        if (instance==null){
            instance =new DaoFactoryImpl();
        }
        return instance;
    }
    @Override
    public ComputerDao createComputerDao() {
        return (ComputerDao) createobject(prop.getProperty("computer"));
    }

    @Override
    public StaffDao createStaffDao() {
        return (StaffDao) createobject(prop.getProperty("staff"));
    }
}
