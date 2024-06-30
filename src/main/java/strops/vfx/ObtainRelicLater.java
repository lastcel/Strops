//This file is mainly copied from the Aspiration mod, credits to erasels!
package strops.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ObtainRelicLater extends AbstractGameEffect {
    private AbstractRelic relic;

    public ObtainRelicLater(AbstractRelic relic) {
        this.relic = relic;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.relic.instantObtain();
        this.isDone = true;
    }

    public void render(SpriteBatch spriteBatch) {}

    public void dispose() {}
}
