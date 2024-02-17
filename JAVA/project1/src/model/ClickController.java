package model;

import java.util.function.BiConsumer;

// controller is used to optimize the ui display, for example, highlight the background of chess, and emphasize something, display 
// the range of something and remove the effects. 
public class ClickController {

    public BiConsumer<Integer, Boolean> hightlight;
    public BiConsumer<Integer, Boolean> backgroundize; 

    int originHightlight = -1; 

    public void click(int x, int y) {
        // receive the events. 
    }   
}
