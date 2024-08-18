/*
package strops.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class ShowCardEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.5F;

    private AbstractCard card;

    private static final float PADDING = 30.0F * Settings.scale;

    public ShowCardEffect(AbstractCard srcCard, float x, float y) {
        this.card = srcCard.makeStatEquivalentCopy();
        this.duration = 1.5F;
        this.card.target_x = x;
        this.card.target_y = y;
        AbstractDungeon.effectsQueue.add(new CardPoofEffect(this.card.target_x, this.card.target_y));
        this.card.drawScale = 0.75F;
        this.card.targetDrawScale = 0.75F;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.card.shrink();
            (AbstractDungeon.getCurrRoom()).souls.discard(this.card, true);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDone){
            this.card.render(sb);
        }
    }

    @Override
    public void dispose() {}
}

 */
