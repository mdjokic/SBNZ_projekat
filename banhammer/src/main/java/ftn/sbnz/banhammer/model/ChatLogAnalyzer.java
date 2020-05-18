package ftn.sbnz.banhammer.model;

import java.util.Random;

public class ChatLogAnalyzer {

    private boolean flame;
    private boolean hate;

    public ChatLogAnalyzer(){
        Random r = new Random();
        flame = r.nextBoolean();
        hate = r.nextBoolean();
    }

    public boolean isFlame() {
        return flame;
    }

    public void setFlame(boolean flame) {
        this.flame = flame;
    }

    public boolean isHate() {
        return hate;
    }

    public void setHate(boolean hate) {
        this.hate = hate;
    }
}
