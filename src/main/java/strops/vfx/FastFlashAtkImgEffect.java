/*
package strops.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FastFlashAtkImgEffect extends AbstractGameEffect {
    private static int blockSound = 0;

    public TextureAtlas.AtlasRegion img;

    private float x;

    private float y;

    private float sY;

    private float tY;

    private static final float DURATION = 0.6F;

    private AbstractGameAction.AttackEffect effect;

    private boolean triggered = false;

    public FastFlashAtkImgEffect(float x, float y, AbstractGameAction.AttackEffect effect, boolean mute, float controlledDuration) {
        this.duration = controlledDuration;
        this.startingDuration = controlledDuration;
        this.effect = effect;
        this.img = loadImage();
        if (this.img != null) {
            this.x = x - this.img.packedWidth / 2.0F;
            y -= this.img.packedHeight / 2.0F;
        }
        this.color = Color.WHITE.cpy();
        this.scale = Settings.scale;
        if (!mute)
            playSound(effect);
        this.y = y;
        switch (effect) {
            case SHIELD:
                this.y = y + 80.0F * Settings.scale;
                this.sY = this.y;
                this.tY = y;
                return;
        }
        this.y = y;
        this.sY = y;
        this.tY = y;
    }

    public FastFlashAtkImgEffect(float x, float y, AbstractGameAction.AttackEffect effect) {
        this(x, y, effect, false, DURATION);
    }

    public FastFlashAtkImgEffect(float x, float y, AbstractGameAction.AttackEffect effect, boolean mute) {
        this(x, y, effect, mute, DURATION);
    }

    private TextureAtlas.AtlasRegion loadImage() {
        switch (this.effect) {
            case SLASH_DIAGONAL:
                return ImageMaster.ATK_SLASH_D;
            case SLASH_HEAVY:
                return ImageMaster.ATK_SLASH_HEAVY;
            case SLASH_HORIZONTAL:
                return ImageMaster.ATK_SLASH_H;
            case SLASH_VERTICAL:
                return ImageMaster.ATK_SLASH_V;
            case BLUNT_LIGHT:
                return ImageMaster.ATK_BLUNT_LIGHT;
            case BLUNT_HEAVY:
                this.rotation = MathUtils.random(360.0F);
                return ImageMaster.ATK_BLUNT_HEAVY;
            case FIRE:
                return ImageMaster.ATK_FIRE;
            case POISON:
                return ImageMaster.ATK_POISON;
            case SHIELD:
                return ImageMaster.ATK_SHIELD;
            case NONE:
                return null;
        }
        return ImageMaster.ATK_SLASH_D;
    }

    private void playSound(AbstractGameAction.AttackEffect effect) {
        switch (effect) {
            case SLASH_HEAVY:
                CardCrawlGame.sound.play("ATTACK_HEAVY");
            case BLUNT_LIGHT:
                CardCrawlGame.sound.play("BLUNT_FAST");
            case BLUNT_HEAVY:
                CardCrawlGame.sound.play("BLUNT_HEAVY");
            case FIRE:
                CardCrawlGame.sound.play("ATTACK_FIRE");
            case POISON:
                CardCrawlGame.sound.play("ATTACK_POISON");
            case SHIELD:
                playBlockSound();
            case NONE:
                return;
        }
        CardCrawlGame.sound.play("ATTACK_FAST");
    }

    private void playBlockSound() {
        if (blockSound == 0) {
            CardCrawlGame.sound.play("BLOCK_GAIN_1");
        } else if (blockSound == 1) {
            CardCrawlGame.sound.play("BLOCK_GAIN_2");
        } else {
            CardCrawlGame.sound.play("BLOCK_GAIN_3");
        }
        blockSound++;
        if (blockSound > 2)
            blockSound = 0;
    }

    public void update() {
        switch (this.effect) {
            case SHIELD:
                this.duration -= Gdx.graphics.getDeltaTime();
                if (this.duration < 0.0F) {
                    this.isDone = true;
                    this.color.a = 0.0F;
                } else if (this.duration < startingDuration/3) {
                    this.color.a = this.duration * 5.0F;
                } else {
                    this.color.a = Interpolation.fade.apply(1.0F, 0.0F, this.duration * 0.75F / startingDuration);
                }
                this.y = Interpolation.exp10In.apply(this.tY, this.sY, this.duration / startingDuration);
                if (this.duration < startingDuration*2/3 && !this.triggered)
                    this.triggered = true;
                return;
        }
        super.update();
    }

    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.color);
            sb.draw((TextureRegion)this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, this.rotation);
        }
    }

    public void dispose() {}
}

 */

