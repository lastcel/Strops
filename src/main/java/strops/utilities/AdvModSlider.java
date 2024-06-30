package strops.utilities;

import basemod.ModPanel;
import basemod.ModSlider;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.function.Consumer;

public class AdvModSlider extends ModSlider {
    public float min;

    public AdvModSlider(String lbl, float posX, float posY, float min, float max, String suf, ModPanel p, Consumer<ModSlider> changeAction) {
        super(lbl, posX, posY, max - min, suf, p, changeAction);
        this.min = min;
    }

    public void render(SpriteBatch sb) {
        float realVal = this.value;
        this.value = (this.value * this.multiplier + this.min) / this.multiplier;
        super.render(sb);
        this.value = realVal;
    }

    public void setValue(int val) {
        super.setValue((val - this.min) / this.multiplier);
    }

    public void setValue(float val) {
        super.setValue(val - this.min / this.multiplier);
    }
}

