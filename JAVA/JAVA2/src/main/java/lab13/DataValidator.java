package lab13;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Scanner;

public class DataValidator {

    public static boolean validate(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean flag=true;
        for (Field field : fields) {
            MinLength minLength = field.getAnnotation(MinLength.class);

            CustomValidation[] customValidations = field.getAnnotationsByType(CustomValidation.class);

            if (minLength != null) {
                try {
                    field.setAccessible(true);
                    String value = (String) field.get(obj);
                    if (value == null || value.length() < minLength.min()) {
                        System.out.println("Validation failed for field *" + field.getName() +
                                "*: should have a minimum length of " + minLength.min());
                        flag=false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (customValidations != null) {
                for (CustomValidation customValidation : customValidations) {
                    try {
                        field.setAccessible(true);
                        String value = (String) field.get(obj);
                        Rule rule = customValidation.rule();
                        switch (rule) {
                            case ALL_LOWERCASE:
                                if (!value.equals(value.toLowerCase())) {
                                    System.out.println("Validation failed for field *" + field.getName() +
                                            "*: should be all lowercase");
                                    flag=false;
                                }
                                break;
                            case NO_USERNAME:
                                Field usernameField = obj.getClass().getDeclaredField("username");
                                usernameField.setAccessible(true);
                                String username = (String) usernameField.get(obj);
                                if (value.contains(username)) {
                                    System.out.println("Validation failed for field *" + field.getName() +
                                            "*: should not contain username");
                                    flag=false;
                                }
                                break;
                            case HAS_BOTH_DIGITS_AND_LETTERS:
                                if (!value.matches(".*\\d.*") || !value.matches(".*[a-zA-Z].*")) {
                                    System.out.println("Validation failed for field *" + field.getName() +
                                            "*: should have both letters and digits");
                                    flag=false;
                                }
                                break;
                        }
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return flag;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("Username: ");
            String username = sc.next();
            System.out.print("Password: ");
            String pwd = sc.next();
            User user = new User(username, pwd);
            if(validate(user)){
                System.out.println("Success!");
                break;
            }
        }
    }
}