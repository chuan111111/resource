package original;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Adapter implements FileOperateInterfaceV2{
    FileOperateInterfaceV1 adapee;
    ManageStaffInterface adapee2;

    public Adapter(FileOperateInterfaceV1 adapee, ManageStaffInterface adapee2) {
        this.adapee = adapee;
        this.adapee2 = adapee2;
    }

    @Override
    public List<StaffModel> readAllStaff() {
        return adapee.readStaffFile();
    }

    @Override
    public void listAllStaff(List<StaffModel> list) {
        adapee.printStaffFile(list);
    }

    @Override
    public void writeByName(List<StaffModel> list) {
            adapee.writeStaffFile(list);
    }

    @Override
    public void writeByRoom(List<StaffModel> list) {
        try {
            if (list.isEmpty()) {
                System.out.println("No information to be written");
                return;
            }
            //todo: change your file path
            FileWriter f = new FileWriter("staff.txt");
            BufferedWriter bufw = new BufferedWriter(f);
            String str = "";
            Collections.sort(list, new Comparator<StaffModel>() {
                @Override
                public int compare(StaffModel o1, StaffModel o2) {
                    return o1.getRoom().compareTo(o2.getRoom());
                }
            });
            for (StaffModel s : list) {
                bufw.write(s.toString());
            }
            bufw.flush();
            bufw.close();
            System.out.println("finish writing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNewStaff(List<StaffModel> list) {
        adapee2.addingStaff(list);
    }

    @Override
    public void removeStaffByName(List<StaffModel> list) {
        adapee2.removeStaff(list);
    }
}
