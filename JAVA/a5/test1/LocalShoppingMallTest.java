import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class LocalShoppingMallTest {

    @Test
    void testAddOrGetMember() {
        ShoppingMall shoppingMall = new ConcreteShoppingMall();
        shoppingMall.addMember("M01 F 29 S");
        shoppingMall.addMember("M02 M 40 G");
        shoppingMall.addMember("M03 F 39 S");
        List<String> members = new ArrayList<>();
        members.add("M04 M 50 S");
        members.add("M10 M 25 S");
        members.add("M22 F 55 G");
        shoppingMall.addMember(members);
        Assertions.assertEquals("SilverCardMember: M01 F 29 points=0.0", shoppingMall.getMember("M01").toString());
        Assertions.assertEquals("GoldCardMember: M02 M 40", shoppingMall.getMember("M02").toString());
        Assertions.assertEquals("SilverCardMember: M03 F 39 points=0.0", shoppingMall.getMember("M03").toString());
        Assertions.assertEquals("SilverCardMember: M04 M 50 points=0.0", shoppingMall.getMember("M04").toString());
        Assertions.assertEquals("SilverCardMember: M10 M 25 points=0.0", shoppingMall.getMember("M10").toString());
        Assertions.assertEquals("GoldCardMember: M22 F 55", shoppingMall.getMember("M22").toString());
    }



    @Test
    void testExample() {
        List<String> records;
        ShoppingMall shoppingMall = new ConcreteShoppingMall();
        shoppingMall.addMember("M01 F 29 S");
        shoppingMall.addMember("M02 M 40 G");
        shoppingMall.addMember("M03 F 39 S");
        List<String> members = new ArrayList<>();
        members.add("M04 M 50 S");
        members.add("M10 M 25 S");
        members.add("M22 F 55 G");
        shoppingMall.addMember(members);


        Assertions.assertEquals(8000, shoppingMall.placeOrder("M01", 8000, ProductCategory.WATCH));
        Assertions.assertEquals(2734, shoppingMall.placeOrder("M01", 3000, ProductCategory.DIGITAL_PRODUCT));
        Assertions.assertEquals(1900, shoppingMall.placeOrder("M01", 2000, ProductCategory.LUGGAGE));
        Assertions.assertEquals(2950, shoppingMall.placeOrder("M02", 3000, ProductCategory.PERFUME));
        Assertions.assertEquals(19450, shoppingMall.placeOrder("M02", 22000, ProductCategory.DIGITAL_PRODUCT));
        Assertions.assertEquals(2760, shoppingMall.placeOrder("M02", 2800, ProductCategory.DRINKS));
        Assertions.assertEquals(9000, shoppingMall.placeOrder("M04", 9000, ProductCategory.JEWELRY));
        Assertions.assertEquals(6300, shoppingMall.placeOrder("M10", 6300, ProductCategory.SKINCARE));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        records = shoppingMall.getMemberRecords("M01");
        Assertions.assertEquals(3, records.size());
        Collections.sort(records);
        Assertions.assertEquals("M01 WATCH 8000 8000", records.get(2).trim());
        Assertions.assertEquals("M01 DIGITAL_PRODUCT 3000 2734", records.get(0).trim());
        Assertions.assertEquals("M01 LUGGAGE 2000 1900", records.get(1).trim());

        records = shoppingMall.getMemberRecords("M02");
        Assertions.assertEquals(3, records.size());
        Collections.sort(records);
        Assertions.assertEquals("M02 PERFUME 3000 2950", records.get(2).trim());
        Assertions.assertEquals("M02 DIGITAL_PRODUCT 22000 19450", records.get(0).trim());
        Assertions.assertEquals("M02 DRINKS 2800 2760", records.get(1).trim());

        records = shoppingMall.getMemberRecords("M04");
        Assertions.assertEquals(1, records.size());
        Collections.sort(records);
        Assertions.assertEquals("M04 JEWELRY 9000 9000", records.get(0).trim());
        records = shoppingMall.getMemberRecords("M10");
        Assertions.assertEquals(1, records.size());
        Collections.sort(records);
        Assertions.assertEquals("M10 SKINCARE 6300 6300", records.get(0).trim());
        records = shoppingMall.getMemberRecords("M22");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());

        records = shoppingMall.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 1 6300", records.get(0).trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 2 22184", records.get(1).trim());
        Assertions.assertEquals("WATCH 1 8000", records.get(2).trim());
        Assertions.assertEquals("JEWELRY 1 9000", records.get(3).trim());
        Assertions.assertEquals("DRINKS 1 2760", records.get(4).trim());
        Assertions.assertEquals("LUGGAGE 1 1900", records.get(5).trim());
        Assertions.assertEquals("PERFUME 1 2950", records.get(6).trim());

        shoppingMall.addMember("MS1 M 22 S");
        shoppingMall.addMember("MS2 M 22 S");
        shoppingMall.placeOrder("MS1", 1000, ProductCategory.DRINKS);
        shoppingMall.placeOrder("MS1", 100, ProductCategory.DRINKS);
        shoppingMall.placeOrder("MS2", 1100, ProductCategory.DRINKS);

        records = shoppingMall.getMemberRecordByGenderAndAge('M', 20, 40);
        Assertions.assertEquals(4, records.size());
        Assertions.assertEquals("MS2 M 22 1100.0", records.get(0).trim());
        Assertions.assertEquals("MS1 M 22 1067.0", records.get(1).trim());
        Assertions.assertEquals("M10 M 25 6300.0", records.get(2).trim());
        Assertions.assertEquals("M02 M 40 25160.0", records.get(3).trim());

        Assertions.assertEquals(12634, (int) shoppingMall.getTotalCost("M01"));
        Assertions.assertEquals(25160, (int) shoppingMall.getTotalCost("M02"));
        Assertions.assertEquals(55261, (int) shoppingMall.getTotalIncome());
    }

    @Test
    void testSimple0() {
        double tmpDouble;
        List<String> records;
        ShoppingMall m = new ConcreteShoppingMall();
        m.addMember("M01 F 90 G");
        Assertions.assertEquals("GoldCardMember: M01 F 90", m.getMember("M01").toString().trim());
        Assertions.assertEquals(7255, (int) (m.placeOrder("M01", 7673, ProductCategory.WATCH)));
        Assertions.assertEquals(8335, (int) (m.placeOrder("M01", 8873, ProductCategory.LUGGAGE)));
        records = m.getMemberRecords("M01");
        Collections.sort(records);
        Assertions.assertEquals(2, records.size());
        Assertions.assertEquals("M01 LUGGAGE 8873 8336", records.get(0).toString().trim());
        Assertions.assertEquals("M01 WATCH 7673 7256", records.get(1).toString().trim());
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 0 0", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 1 7256", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 0 0", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 1 8336", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        records = m.getMemberRecordByGenderAndAge('F', 1, 100);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M01 F 90 15591.4", records.get(0).toString().trim());
        records = m.getMemberRecordByGenderAndAge('M', 1, 100);
        Assertions.assertEquals(0, records.size());
        tmpDouble = m.getTotalCost("M01");
        Assertions.assertEquals(String.format("%.2f", 15591.400000000001), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalIncome();
        Assertions.assertEquals(String.format("%.2f", 15591.4), String.format("%.2f", tmpDouble));
    }


    @Test
    void testSimple1() {
        double tmpDouble;
        List<String> records;
        ShoppingMall m1 = new ConcreteShoppingMall();
        m1.addMember("M01 M 57 S");
        m1.addMember(Arrays.asList("M02 F 62 S", "M03 F 57 G"));
        Assertions.assertEquals("SilverCardMember: M01 M 57 points=0.0", m1.getMember("M01").toString().trim());
        Assertions.assertEquals("SilverCardMember: M02 F 62 points=0.0", m1.getMember("M02").toString().trim());
        Assertions.assertEquals("GoldCardMember: M03 F 57", m1.getMember("M03").toString().trim());
        Assertions.assertNull(m1.getMember("M04"));
        Assertions.assertNull(m1.getMember("M05"));
        Assertions.assertNull(m1.getMember("M06"));
        Assertions.assertEquals(1933, (int) m1.placeOrder("M01", 1933, ProductCategory.WATCH));
        Assertions.assertEquals(5799, (int) m1.placeOrder("M01", 5863, ProductCategory.DIGITAL_PRODUCT));
        Assertions.assertEquals(2924, (int) m1.placeOrder("M01", 3119, ProductCategory.WATCH));
        Assertions.assertEquals(7000, (int) m1.placeOrder("M02", 7000, ProductCategory.SKINCARE));
        Assertions.assertEquals(9148, (int) m1.placeOrder("M02", 9381, ProductCategory.WATCH));
        Assertions.assertEquals(6802, (int) m1.placeOrder("M02", 7114, ProductCategory.PERFUME));
        records = m1.getMemberRecords("M01");
        Collections.sort(records);
        Assertions.assertEquals(3, records.size());
        Assertions.assertEquals("M01 DIGITAL_PRODUCT 5863 5799", records.get(0).toString().trim());
        Assertions.assertEquals("M01 WATCH 1933 1933", records.get(1).toString().trim());
        Assertions.assertEquals("M01 WATCH 3119 2924", records.get(2).toString().trim());
        records = m1.getMemberRecords("M02");
        Collections.sort(records);
        Assertions.assertEquals(3, records.size());
        Assertions.assertEquals("M02 PERFUME 7114 6802", records.get(0).toString().trim());
        Assertions.assertEquals("M02 SKINCARE 7000 7000", records.get(1).toString().trim());
        Assertions.assertEquals("M02 WATCH 9381 9148", records.get(2).toString().trim());
        records = m1.getMemberRecords("M03");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());
        records = m1.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 1 7000", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 1 5799", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 3 14005", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 0 0", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 0 0", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 1 6802", records.get(6).toString().trim());
        records = m1.getMemberRecordByGenderAndAge('F', 1, 100);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M02 F 62 22950.0", records.get(0).toString().trim());
        records = m1.getMemberRecordByGenderAndAge('M', 1, 100);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M01 M 57 10656.0", records.get(0).toString().trim());
        tmpDouble = m1.getTotalCost("M01");
        Assertions.assertEquals(String.format("%.2f", 10656.0), String.format("%.2f", tmpDouble));
        tmpDouble = m1.getTotalCost("M02");
        Assertions.assertEquals(String.format("%.2f", 22950.0), String.format("%.2f", tmpDouble));
        tmpDouble = m1.getTotalCost("M03");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        tmpDouble = m1.getTotalIncome();
        Assertions.assertEquals(String.format("%.2f", 33606.0), String.format("%.2f", tmpDouble));
    }

    @Test
    void testmedium0() {
        double tmpDouble;
        List<String> records;
        ShoppingMall m = new ConcreteShoppingMall();
        Assertions.assertNull(m.getMember("M76"));
        m.addMember("M73 M 24 S");
        m.addMember("M32 M 11 S");
        m.addMember("M10 M 55 G");
        m.addMember("M83 F 42 G");
        m.addMember("M36 M 75 G");
        m.addMember("M63 M 50 S");
        m.addMember("M59 M 90 S");
        m.addMember("M35 M 13 S");
        m.addMember(Arrays.asList("M43 M 49 G", "M75 M 7 S", "M40 F 37 S", "M28 M 58 G"));
        m.addMember(Arrays.asList("M81 F 20 S", "M16 F 47 S", "M12 F 79 G"));
        Assertions.assertEquals(9406, (int) m.placeOrder("M40", 9406, ProductCategory.PERFUME));
        Assertions.assertEquals(9510, (int) m.placeOrder("M73", 9510, ProductCategory.DIGITAL_PRODUCT));
        Assertions.assertEquals(5441, (int) m.placeOrder("M32", 5441, ProductCategory.LUGGAGE));
        Assertions.assertEquals(2857, (int) m.placeOrder("M73", 3174, ProductCategory.SKINCARE));
        Assertions.assertEquals(1767, (int) m.placeOrder("M10", 1767, ProductCategory.DRINKS));
        Assertions.assertEquals(4584, (int) m.placeOrder("M12", 4721, ProductCategory.SKINCARE));
        Assertions.assertEquals(5876, (int) m.placeOrder("M35", 5876, ProductCategory.JEWELRY));
        Assertions.assertEquals(157, (int) m.placeOrder("M75", 157, ProductCategory.DRINKS));
        Assertions.assertEquals(8769, (int) m.placeOrder("M10", 9355, ProductCategory.SKINCARE));
        Assertions.assertEquals(7253, (int) m.placeOrder("M81", 7253, ProductCategory.LUGGAGE));
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 3 16211", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 1 9510", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 0 0", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 1 5876", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 2 1924", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 2 12694", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 1 9406", records.get(6).toString().trim());
        tmpDouble = m.getTotalIncome();
        Assertions.assertEquals(String.format("%.2f", 55621.45), String.format("%.2f", tmpDouble));
        Assertions.assertEquals(6709, (int) m.placeOrder("M28", 7066, ProductCategory.DRINKS));
        Assertions.assertEquals(3703, (int) m.placeOrder("M75", 3708, ProductCategory.JEWELRY));
        Assertions.assertEquals(8819, (int) m.placeOrder("M40", 9132, ProductCategory.DIGITAL_PRODUCT));
        Assertions.assertEquals(3888, (int) m.placeOrder("M81", 4129, ProductCategory.PERFUME));
        Assertions.assertEquals(826, (int) m.placeOrder("M10", 826, ProductCategory.LUGGAGE));
        Assertions.assertEquals(8475, (int) m.placeOrder("M32", 8656, ProductCategory.WATCH));
        Assertions.assertEquals(7518, (int) m.placeOrder("M75", 7641, ProductCategory.LUGGAGE));
        Assertions.assertEquals(6752, (int) m.placeOrder("M81", 6889, ProductCategory.LUGGAGE));
        Assertions.assertEquals(1946, (int) m.placeOrder("M81", 2290, ProductCategory.LUGGAGE));
        Assertions.assertEquals(430, (int) m.placeOrder("M63", 430, ProductCategory.DIGITAL_PRODUCT));
        Assertions.assertEquals("GoldCardMember: M43 M 49", m.getMember("M43").toString().trim());
        Assertions.assertEquals("SilverCardMember: M35 M 13 points=195.0", m.getMember("M35").toString().trim());
        Assertions.assertEquals("GoldCardMember: M83 F 42", m.getMember("M83").toString().trim());
        Assertions.assertNull(m.getMember("M45"));
        Assertions.assertEquals("SilverCardMember: M63 M 50 points=14.0", m.getMember("M63").toString().trim());
        Assertions.assertEquals("SilverCardMember: M35 M 13 points=195.0", m.getMember("M35").toString().trim());
        Assertions.assertNull(m.getMember("M94"));
        Assertions.assertNull(m.getMember("M98"));
        Assertions.assertEquals("SilverCardMember: M75 M 7 points=254.0", m.getMember("M75").toString().trim());
        Assertions.assertNull(m.getMember("M93"));
        Assertions.assertEquals("GoldCardMember: M83 F 42", m.getMember("M83").toString().trim());
        Assertions.assertEquals("SilverCardMember: M32 M 11 points=288.0", m.getMember("M32").toString().trim());
        Assertions.assertEquals("GoldCardMember: M43 M 49", m.getMember("M43").toString().trim());
        Assertions.assertEquals("SilverCardMember: M59 M 90 points=0.0", m.getMember("M59").toString().trim());
        Assertions.assertEquals("SilverCardMember: M75 M 7 points=254.0", m.getMember("M75").toString().trim());
        Assertions.assertNull(m.getMember("M24"));
        Assertions.assertNull(m.getMember("M04"));
        Assertions.assertNull(m.getMember("M42"));
        Assertions.assertNull(m.getMember("M82"));
        Assertions.assertNull(m.getMember("M02"));
        Assertions.assertEquals(5748, (int) m.placeOrder("M10", 5998, ProductCategory.LUGGAGE));
        Assertions.assertEquals(3337, (int) m.placeOrder("M35", 3532, ProductCategory.DRINKS));
        Assertions.assertEquals(3764, (int) m.placeOrder("M43", 3857, ProductCategory.JEWELRY));
        Assertions.assertEquals(1298, (int) m.placeOrder("M59", 1298, ProductCategory.LUGGAGE));
        Assertions.assertEquals(1895, (int) m.placeOrder("M75", 2149, ProductCategory.JEWELRY));
        Assertions.assertEquals(277, (int) m.placeOrder("M40", 581, ProductCategory.PERFUME));
        Assertions.assertEquals(6489, (int) m.placeOrder("M35", 6606, ProductCategory.WATCH));
        Assertions.assertEquals(2381, (int) m.placeOrder("M75", 2488, ProductCategory.DRINKS));
        Assertions.assertEquals(7325, (int) m.placeOrder("M12", 7751, ProductCategory.LUGGAGE));
        Assertions.assertEquals(19, (int) m.placeOrder("M40", 48, ProductCategory.JEWELRY));
        Assertions.assertEquals(4705, (int) m.placeOrder("M43", 4848, ProductCategory.LUGGAGE));
        Assertions.assertEquals(4652, (int) m.placeOrder("M83", 4792, ProductCategory.PERFUME));
        Assertions.assertEquals(2251, (int) m.placeOrder("M40", 2253, ProductCategory.WATCH));
        Assertions.assertEquals(9233, (int) m.placeOrder("M36", 9870, ProductCategory.DRINKS));
        Assertions.assertEquals(5534, (int) m.placeOrder("M12", 5761, ProductCategory.WATCH));
        Assertions.assertEquals(2265, (int) m.placeOrder("M43", 2279, ProductCategory.PERFUME));
        Assertions.assertEquals(6655, (int) m.placeOrder("M35", 6875, ProductCategory.JEWELRY));
        Assertions.assertEquals(4075, (int) m.placeOrder("M32", 4363, ProductCategory.WATCH));
        Assertions.assertEquals(5978, (int) m.placeOrder("M10", 6254, ProductCategory.LUGGAGE));
        Assertions.assertEquals(4421, (int) m.placeOrder("M12", 4549, ProductCategory.WATCH));
        records = m.getMemberRecords("M36");
        Collections.sort(records);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M36 DRINKS 9870 9233", records.get(0).toString().trim());
        records = m.getMemberRecords("M73");
        Collections.sort(records);
        Assertions.assertEquals(2, records.size());
        Assertions.assertEquals("M73 DIGITAL_PRODUCT 9510 9510", records.get(0).toString().trim());
        Assertions.assertEquals("M73 SKINCARE 3174 2857", records.get(1).toString().trim());
        records = m.getMemberRecords("M12");
        Collections.sort(records);
        Assertions.assertEquals(4, records.size());
        Assertions.assertEquals("M12 LUGGAGE 7751 7326", records.get(0).toString().trim());
        Assertions.assertEquals("M12 SKINCARE 4721 4585", records.get(1).toString().trim());
        Assertions.assertEquals("M12 WATCH 4549 4422", records.get(2).toString().trim());
        Assertions.assertEquals("M12 WATCH 5761 5535", records.get(3).toString().trim());
        records = m.getMemberRecords("M59");
        Collections.sort(records);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M59 LUGGAGE 1298 1298", records.get(0).toString().trim());
        records = m.getMemberRecords("M10");
        Collections.sort(records);
        Assertions.assertEquals(5, records.size());
        Assertions.assertEquals("M10 DRINKS 1767 1767", records.get(0).toString().trim());
        Assertions.assertEquals("M10 LUGGAGE 5998 5748", records.get(1).toString().trim());
        Assertions.assertEquals("M10 LUGGAGE 6254 5979", records.get(2).toString().trim());
        Assertions.assertEquals("M10 LUGGAGE 826 826", records.get(3).toString().trim());
        Assertions.assertEquals("M10 SKINCARE 9355 8770", records.get(4).toString().trim());
        records = m.getMemberRecords("M16");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());
        records = m.getMemberRecords("M63");
        Collections.sort(records);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M63 DIGITAL_PRODUCT 430 430", records.get(0).toString().trim());
        records = m.getMemberRecords("M75");
        Collections.sort(records);
        Assertions.assertEquals(5, records.size());
        Assertions.assertEquals("M75 DRINKS 157 157", records.get(0).toString().trim());
        Assertions.assertEquals("M75 DRINKS 2488 2382", records.get(1).toString().trim());
        Assertions.assertEquals("M75 JEWELRY 2149 1895", records.get(2).toString().trim());
        Assertions.assertEquals("M75 JEWELRY 3708 3703", records.get(3).toString().trim());
        Assertions.assertEquals("M75 LUGGAGE 7641 7518", records.get(4).toString().trim());
        records = m.getMemberRecords("M12");
        Collections.sort(records);
        Assertions.assertEquals(4, records.size());
        Assertions.assertEquals("M12 LUGGAGE 7751 7326", records.get(0).toString().trim());
        Assertions.assertEquals("M12 SKINCARE 4721 4585", records.get(1).toString().trim());
        Assertions.assertEquals("M12 WATCH 4549 4422", records.get(2).toString().trim());
        Assertions.assertEquals("M12 WATCH 5761 5535", records.get(3).toString().trim());
        records = m.getMemberRecords("M81");
        Collections.sort(records);
        Assertions.assertEquals(4, records.size());
        Assertions.assertEquals("M81 LUGGAGE 2290 1947", records.get(0).toString().trim());
        Assertions.assertEquals("M81 LUGGAGE 6889 6752", records.get(1).toString().trim());
        Assertions.assertEquals("M81 LUGGAGE 7253 7253", records.get(2).toString().trim());
        Assertions.assertEquals("M81 PERFUME 4129 3888", records.get(3).toString().trim());
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 3 16211", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 3 18759", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 6 31247", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 6 21913", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 6 23585", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 11 54793", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 5 20488", records.get(6).toString().trim());
        records = m.getMemberRecordByGenderAndAge('F', 85, 87);
        Assertions.assertEquals(0, records.size());
        records = m.getMemberRecordByGenderAndAge('M', 31, 75);
        Assertions.assertEquals(5, records.size());
        Assertions.assertEquals("M43 M 49 10734.8", records.get(0).toString().trim());
        Assertions.assertEquals("M63 M 50 430.0", records.get(1).toString().trim());
        Assertions.assertEquals("M10 M 55 23089.3", records.get(2).toString().trim());
        Assertions.assertEquals("M28 M 58 6709.4", records.get(3).toString().trim());
        Assertions.assertEquals("M36 M 75 9233.0", records.get(4).toString().trim());
        records = m.getMemberRecordByGenderAndAge('M', 74, 98);
        Assertions.assertEquals(2, records.size());
        Assertions.assertEquals("M36 M 75 9233.0", records.get(0).toString().trim());
        Assertions.assertEquals("M59 M 90 1298.0", records.get(1).toString().trim());
        records = m.getMemberRecordByGenderAndAge('F', 35, 49);
        Assertions.assertEquals(2, records.size());
        Assertions.assertEquals("M40 F 37 20773.0", records.get(0).toString().trim());
        Assertions.assertEquals("M83 F 42 4652.4", records.get(1).toString().trim());
        records = m.getMemberRecordByGenderAndAge('F', 40, 42);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M83 F 42 4652.4", records.get(0).toString().trim());
        records = m.getMemberRecordByGenderAndAge('F', 1, 49);
        Assertions.assertEquals(3, records.size());
        Assertions.assertEquals("M81 F 20 19839.5", records.get(0).toString().trim());
        Assertions.assertEquals("M40 F 37 20773.0", records.get(1).toString().trim());
        Assertions.assertEquals("M83 F 42 4652.4", records.get(2).toString().trim());
        tmpDouble = m.getTotalCost("M12");
        Assertions.assertEquals(String.format("%.2f", 21867.3), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M10");
        Assertions.assertEquals(String.format("%.2f", 23089.300000000003), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M40");
        Assertions.assertEquals(String.format("%.2f", 20773.0), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M16");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M36");
        Assertions.assertEquals(String.format("%.2f", 9233.0), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M73");
        Assertions.assertEquals(String.format("%.2f", 12367.0), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M10");
        Assertions.assertEquals(String.format("%.2f", 23089.300000000003), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M73");
        Assertions.assertEquals(String.format("%.2f", 12367.0), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M10");
        Assertions.assertEquals(String.format("%.2f", 23089.300000000003), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalCost("M59");
        Assertions.assertEquals(String.format("%.2f", 1298.0), String.format("%.2f", tmpDouble));
        tmpDouble = m.getTotalIncome();
        Assertions.assertEquals(String.format("%.2f", 186996.20), String.format("%.2f", tmpDouble));
    }

    @Test
    void testRandom() {
        double tmpDouble;
        List<String> records;
        ShoppingMall m = new ConcreteShoppingMall();
        Assertions.assertNull(m.getMember("M42"));
        Assertions.assertNull(m.getMember("M73"));
        m.addMember("M47 F 89 G");
        m.addMember("M96 F 2 G");
        m.addMember("M45 F 1 S");
        m.addMember("M97 F 83 G");
        m.addMember("M33 M 45 S");
        m.addMember("M60 F 62 S");
        m.addMember("M24 M 94 G");
        m.addMember("M02 M 50 G");
        m.addMember("M09 M 1 S");
        m.addMember("M26 F 8 S");
        Assertions.assertEquals("SilverCardMember: M09 M 1 points=0.0", m.getMember("M09").toString().trim());
        tmpDouble = m.getTotalCost("M47");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        m.addMember("M52 M 43 S");
        m.addMember(Arrays.asList("M29 F 88 S", "M11 M 92 G", "M25 F 14 G", "M28 F 77 G"));
        Assertions.assertNull(m.getMember("M30"));
        m.addMember("M43 F 73 S");
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 0 0", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 0 0", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 0 0", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 0 0", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        records = m.getMemberRecordByGenderAndAge('M', 29, 34);
        Assertions.assertEquals(0, records.size());
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 0 0", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 0 0", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 0 0", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 0 0", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        Assertions.assertNull(m.getMember("M49"));
        m.addMember("M76 F 48 S");
        Assertions.assertEquals("GoldCardMember: M25 F 14", m.getMember("M25").toString().trim());
        records = m.getMemberRecords("M97");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());
        Assertions.assertEquals(7201, (int) m.placeOrder("M29", 7201, ProductCategory.DIGITAL_PRODUCT));
        m.addMember("M50 M 3 S");
        records = m.getMemberRecords("M96");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());
        records = m.getMemberRecordByGenderAndAge('M', 33, 34);
        Assertions.assertEquals(0, records.size());
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 1 7201", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 0 0", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 0 0", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 0 0", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        Assertions.assertNull(m.getMember("M86"));
        records = m.getMemberRecordByGenderAndAge('M', 18, 48);
        Assertions.assertEquals(0, records.size());
        m.addMember("M70 F 14 G");
        tmpDouble = m.getTotalCost("M47");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        Assertions.assertEquals("SilverCardMember: M45 F 1 points=0.0", m.getMember("M45").toString().trim());
        Assertions.assertNull(m.getMember("M21"));
        records = m.getMemberRecords("M45");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());
        Assertions.assertEquals(9188, (int) m.placeOrder("M45", 9188, ProductCategory.JEWELRY));
        tmpDouble = m.getTotalCost("M52");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        Assertions.assertNull(m.getMember("M66"));
        Assertions.assertEquals(6586, (int) (m.placeOrder("M97", 6929, ProductCategory.LUGGAGE)));
        Assertions.assertEquals("SilverCardMember: M45 F 1 points=306.0", m.getMember("M45").toString().trim());
        Assertions.assertEquals("GoldCardMember: M02 M 50", m.getMember("M02").toString().trim());
        Assertions.assertEquals(4968, (int) m.placeOrder("M28", 5132, ProductCategory.LUGGAGE));
        m.addMember("M12 F 56 S");
        m.addMember(Arrays.asList("M37 F 82 S", "M79 F 73 G"));
        records = m.getMemberRecordByGenderAndAge('M', 20, 56);
        Assertions.assertEquals(0, records.size());
        Assertions.assertEquals(2038, (int) m.placeOrder("M79", 2040, ProductCategory.LUGGAGE));
        Assertions.assertEquals(5872, (int) m.placeOrder("M96", 6136, ProductCategory.LUGGAGE));
        Assertions.assertEquals("GoldCardMember: M79 F 73", m.getMember("M79").toString().trim());
        m.addMember("M58 M 16 S");
        Assertions.assertEquals(7743, (int) m.placeOrder("M11", 8215, ProductCategory.WATCH));
        Assertions.assertNull(m.getMember("M64"));
        records = m.getMemberRecords("M28");
        Collections.sort(records);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M28 LUGGAGE 5132 4969", records.get(0).toString().trim());
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 1 7201", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 1 7744", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 1 9188", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 4 19465", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        Assertions.assertEquals(9209, (int) m.placeOrder("M28", 9844, ProductCategory.LUGGAGE));
        tmpDouble = m.getTotalCost("M37");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 1 7201", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 1 7744", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 1 9188", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 5 28675", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        tmpDouble = m.getTotalCost("M58");
        Assertions.assertEquals(String.format("%.2f", 0.0), String.format("%.2f", tmpDouble));
        records = m.getCostByCategory();
        Assertions.assertEquals(7, records.size());
        Assertions.assertEquals("SKINCARE 0 0", records.get(0).toString().trim());
        Assertions.assertEquals("DIGITAL_PRODUCT 1 7201", records.get(1).toString().trim());
        Assertions.assertEquals("WATCH 1 7744", records.get(2).toString().trim());
        Assertions.assertEquals("JEWELRY 1 9188", records.get(3).toString().trim());
        Assertions.assertEquals("DRINKS 0 0", records.get(4).toString().trim());
        Assertions.assertEquals("LUGGAGE 5 28675", records.get(5).toString().trim());
        Assertions.assertEquals("PERFUME 0 0", records.get(6).toString().trim());
        m.addMember("M14 F 64 G");
        m.addMember(Arrays.asList("M05 M 2 S", "M31 M 82 G", "M54 M 87 G"));
        records = m.getMemberRecordByGenderAndAge('M', 52, 94);
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals("M11 M 92 7743.5", records.get(0).toString().trim());
        records = m.getMemberRecords("M76");
        Collections.sort(records);
        Assertions.assertEquals(0, records.size());
        m.addMember("M27 F 82 G");
    }

    @Test
    void testDefinedFieldAndMethod() throws NoSuchFieldException, NoSuchMethodException {
        Field members = ConcreteShoppingMall.class.getDeclaredField("members");
        ParameterizedType t = (ParameterizedType) members.getGenericType();
        Assertions.assertEquals(List.class, t.getRawType());
        Assertions.assertEquals(Member.class, t.getActualTypeArguments()[0]);

        Method addMembers = ShoppingMall.class.getDeclaredMethod("addMember", List.class);
        Method addMember = ShoppingMall.class.getDeclaredMethod("addMember", String.class);
        Assertions.assertEquals(void.class, addMembers.getReturnType());
        Assertions.assertEquals(void.class, addMember.getReturnType());

        Method getMember = ShoppingMall.class.getDeclaredMethod("getMember", String.class);
        Method placeOrder = ShoppingMall.class.getDeclaredMethod("placeOrder", String.class, int.class, ProductCategory.class);
        Method getMemberRecords = ShoppingMall.class.getDeclaredMethod("getMemberRecords", String.class);
        Method getCostByCategory = ShoppingMall.class.getDeclaredMethod("getCostByCategory");
        Method getMemberRecordByGenderAndAge = ShoppingMall.class.getDeclaredMethod("getMemberRecordByGenderAndAge", char.class, int.class, int.class);
        Method getTotalCost = ShoppingMall.class.getDeclaredMethod("getTotalCost", String.class);
        Method getTotalIncome = ShoppingMall.class.getDeclaredMethod("getTotalIncome");

        Assertions.assertEquals(Member.class, getMember.getReturnType());
        Assertions.assertEquals(double.class, placeOrder.getReturnType());
        Assertions.assertEquals(List.class, getMemberRecords.getReturnType());
        Assertions.assertEquals(List.class, getCostByCategory.getReturnType());
        Assertions.assertEquals(List.class, getMemberRecordByGenderAndAge.getReturnType());
        Assertions.assertEquals(double.class, getTotalCost.getReturnType());
        Assertions.assertEquals(double.class, getTotalIncome.getReturnType());
    }

    @Test
    void testPolymorphism() throws NoSuchFieldException, IllegalAccessException {
        ShoppingMall shoppingMall = new ConcreteShoppingMall();
        Member member = new TestMember("M01 F 89 M");
        Field memberListField = ConcreteShoppingMall.class.getDeclaredField("members");
        memberListField.setAccessible(true);
        Object memberListObj = memberListField.get(shoppingMall);
        memberListField.setAccessible(false);
        List<Member> memberList = (List<Member>) memberListObj;
        memberList.add(member);
        Assertions.assertEquals("M01 F 89", shoppingMall.getMember("M01").toString().trim());
        Assertions.assertEquals(25, (int) shoppingMall.placeOrder("M01", 50, ProductCategory.DRINKS));
    }

    static class TestMember extends Member {

        public TestMember(String info) {
            super(info);
        }

        @Override
        public double consume(int amount) {
            return amount / 2.0;
        }
    }


}

