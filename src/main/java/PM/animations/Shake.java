package PM.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
    private TranslateTransition tt;
    public Shake(Node node){ // Node-любой обьект на нашем окне (кнопка , txt Field и тд)
        tt=new TranslateTransition(Duration.millis(70),node);
        tt.setFromX((0f)); //отступ от x
        tt.setByX(10f); // передвинется относительно начальной позиции по x
        tt.setCycleCount(3); // Количество передвижений
        tt.setAutoReverse(true);//Возврат в исходную позицию

    }

    public void playAnimation(){
        tt.playFromStart();
    }

}
