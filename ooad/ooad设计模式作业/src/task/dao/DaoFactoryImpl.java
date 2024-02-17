package src.dao;

import java.io.*;
import java.util.Properties;

public class DaoFactoryImpl implements DaoFactory{
    private static DaoFactoryImpl instance =null;

    private static Properties prop;
    private DaoFactoryImpl() {
        InputStream in= null;
        try {
            in = new BufferedInputStream(new
                    FileInputStream("resource/data.properties"));
            prop.load(in);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(prop.get("Staff"));
    }

    public static void loudprop(){

    }

    public static DaoFactory getInstance(){
        if (instance==null){
            instance =new DaoFactoryImpl();
        }
        return instance;
    }
    @Override
    public ComputerDao createComputerDao() {
        return null;
    }

    @Override
    public StaffDao createStaffDao() {
        return null;
    }
}
