package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 这个类主要完成由窗体上组件触发的动作。
 * 例如点击button等
 * ChessGameFrame中组件调用本类的对象，在本类中的方法里完成逻辑运算，将运算的结果传递至chessboard中绘制
 */
public class GameController {

    // keep all info 
    // 0: empty 
    // 1: soldier 
    // 2: canon 
    // 3: horse 
    // 4: chariot 
    // 5: minister 
    // 6: advisor 
    // 7: general 
    // mask: +- 10 
    // only chess board to visit it! 
    public int[] chesses = new int [32]; 

    public final List<Rule> rules = new ArrayList<>(); 

    private static void checkChessInfo(int[] chesses) throws RuntimeException {
        if (chesses.length != 32) {
            throw new RuntimeException("Invalid size");
        }
        for (var v : chesses) {
            if (v >= -7 || v <= 7) {
                continue; 
            }
            if (v < 0) {
                v += 10; 
                if (v >= -7) {
                    continue; 
                }
            } else {
                v -= 10; 
                if (v <= 7) {
                    continue; 
                }
            }
            throw new RuntimeException("Unregonized piece in chessboard"); 
        }
    }

    public static int getDis(int f, int t) {
        int fx = f % 4; 
        int fy = f / 4; 

        int tx = t % 4; 
        int ty = t / 4; 

        return Math.abs(fx - tx) + Math.abs(fy - ty); 
    }

    public void directlyExecute(Operator op) {
        // assume the operator can execute correctly! 
        if (chesses[op.index1] == 0) {
            throw new AssertionError("Select the empty position");
        }
        // handle the special situation: flip 
        if (op.index1 == op.index2) {
            // clears it! 
            if (chesses[op.index1] < -10) {
                chesses[op.index1] += 10; 
            } else if (chesses[op.index1] > 10) {
                chesses[op.index1] -= 10; 
            } else {
                throw new AssertionError("Invalid flip position");
            }
            return ; 
        }
        // handle the normal move situation (including kill) 
        for (var l : listenerBefore) {
            l.accept(chesses, op);
        }
        chesses[op.index2] = chesses[op.index1]; 
        chesses[op.index1] = 0; 
        for (var l : listenerAfter) {
            l.accept(chesses, op);
        }
        ops.add(op);
    }

    public void execute(Operator op) {
        boolean b = false; 
        for (var i : rules ) {
            b |= i.testOperator(chesses, op); 
        }
        if (!b) {
            // fails operator 
            throw new RuntimeException("No rule accept"); 
        }
        directlyExecute(op);
    }

    // commands! 
    public List<Operator> ops = new ArrayList<>(); 

    public List<BiConsumer<int[], Operator>> listenerBefore = new ArrayList<>();
    public List<BiConsumer<int[], Operator>> listenerAfter = new ArrayList<>();

    // use the path to load the game info, 
    // null when fails. 
    public int[] loadGameFromFile(String path) {
        try (ObjectInputStream is
            = new ObjectInputStream(new FileInputStream(Paths.get(".", "saveFile", path).toFile()))) {
            var o = (int []) is.readObject();
            checkChessInfo(o);
            return o; 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    // use the path to save the game info 
    public void writeFileByFileWriter(String path){
        try (ObjectOutputStream os = 
            new ObjectOutputStream(new FileOutputStream(Paths.get(".", "saveFile", path).toFile()))) {
                os.writeObject(chesses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRulePrefab() {
        rules.add(Utils.moveRule);
        rules.add(Utils.flipRule); 
    }

    public int[] init() {
        int[] ans = new int[32]; 
        int index = 0; 
        for (int j = 0; j < 5; ++j) {
            ans[index] = 1; 
            ans[index+1] = -1; 
            index += 2; 
        }
        for (int j = 2; j < 7; ++j) {
            ans[index] = j; 
            ans[index+1] = j; 
            ans[index+2] = -j; 
            ans[index+3] = -j; 
            index += 4; 
        }
        ans[index] = 7; 
        ans[index+1] = -7; 
        List<Integer> result = Arrays.stream(ans).map(GameController::mask).collect(ArrayList::new, ArrayList::add, (a, b) -> {
            a.addAll(b);
        }); 
        Collections.shuffle(result);
        for (int i = 0; i < 32; ++i) {
            ans[i] = result.get(i);
        }
        return ans; 
    }

    public static int mask(int origin) {
        if (origin > 0 && origin < 8) {
            return origin + 10; 
        } else if (origin < 0 && origin > -8) {
            return origin - 10; 
        } else {
            return origin; 
        }
    }

}
