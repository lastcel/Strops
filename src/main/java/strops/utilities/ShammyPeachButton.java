package strops.utilities;

import basemod.ClickableUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import strops.relics.ShammyPeach;

public class ShammyPeachButton extends ClickableUIElement {
    public static final Texture IMAGE=ImageMaster.POPUP_ARROW;
    public static final float X_L=50.0f*Settings.scale;
    public static final float X_R=1620.0f*Settings.scale;
    public static final float X_L_TIP=260.0f*Settings.scale;
    public static final float X_R_TIP=1340.0f*Settings.scale;
    public static final float Y=Settings.HEIGHT/2.0f;
    public boolean isToDrawPile;
    //public boolean isFlippedX;
    public boolean isHovered=false;
    public static final String[] TIPS = CardCrawlGame.languagePack.getRelicStrings(ShammyPeach.ID).DESCRIPTIONS;

    public ShammyPeachButton(boolean isToDrawPile){
        super(IMAGE);
        this.isToDrawPile=isToDrawPile;
        if(this.isToDrawPile){
            setX(X_L);
        } else {
            setX(X_R);
        }
        setY(Y);
    }

    @Override
    public void onHover(){
        isHovered=true;
    }

    @Override
    public void onUnhover(){
        isHovered=false;
    }

    @Override
    public void onClick(){
        ShammyPeach shammyPeach=(ShammyPeach)AbstractDungeon.player.getRelic(ShammyPeach.ID);
        if(shammyPeach!=null){
            shammyPeach.onThisTriggered(isToDrawPile);
        }
    }

    @Override
    public void render(SpriteBatch sb, Color color) {
        sb.setColor(color);
        if (image != null) {
            float halfWidth = image.getWidth() / 2.0f;
            float halfHeight = image.getHeight() / 2.0f;
            sb.draw(image,
                    x - halfWidth + halfWidth * Settings.scale, y - halfHeight + halfHeight * Settings.scale,
                    halfWidth, halfHeight,
                    image.getWidth(), image.getHeight(),
                    Settings.scale, Settings.scale,
                    angle,
                    0, 0,
                    image.getWidth(), image.getHeight(),
                    !isToDrawPile, false);
            if (tint.a > 0) {
                sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
                sb.setColor(tint);
                sb.draw(image,
                        x - halfWidth + halfWidth * Settings.scale, y - halfHeight + halfHeight * Settings.scale,
                        halfWidth, halfHeight,
                        image.getWidth(), image.getHeight(),
                        Settings.scale, Settings.scale,
                        angle,
                        0, 0,
                        image.getWidth(), image.getHeight(),
                        !isToDrawPile, false);
                sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            }
        }
        renderHitbox(sb);
        if(isHovered){
            if(isToDrawPile){
                TipHelper.renderGenericTip(X_L_TIP,Y,TIPS[5],TIPS[6]);
            } else {
                TipHelper.renderGenericTip(X_R_TIP,Y,TIPS[7],TIPS[8]);
            }
        }
    }
}
