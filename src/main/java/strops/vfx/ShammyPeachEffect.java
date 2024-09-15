package strops.vfx;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import strops.relics.ShammyPeach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShammyPeachEffect extends AbstractGameEffect {
    private static final float DUR = 3.0F;

    private final Color screenColor = Color.PINK.cpy();

    private final boolean isToDrawPile;

    public ShammyPeachEffect(boolean isToDrawPile) {
        this.duration = DUR;
        this.screenColor.a = 0.0F;
        this.isToDrawPile=isToDrawPile;
        //AbstractDungeon.overlayMenu.endTurnButton.hide();
    }

    @Override
    public void update() {
        if(this.duration==DUR){
            AbstractPlayer p=AbstractDungeon.player;
            ArrayList<AbstractCard> hand=p.hand.group;
            ArrayList<AbstractCard> draw=p.drawPile.group;
            ArrayList<AbstractCard> discard=p.discardPile.group;
            for(AbstractCard c:hand){
                c.darken(true);
            }
            for(AbstractCard c:draw){
                c.darken(true);
            }
            for(AbstractCard c:discard){
                c.darken(true);
            }
            Collections.sort(hand);
            Collections.sort(draw);
            Collections.sort(discard);
            Collections.shuffle(hand,new Random(AbstractDungeon.shuffleRng.randomLong()));
            Collections.shuffle(draw,new Random(AbstractDungeon.shuffleRng.randomLong()));
            Collections.shuffle(discard,new Random(AbstractDungeon.shuffleRng.randomLong()));

            ArrayList<AbstractCard> tempHand = new ArrayList<>(hand);
            ArrayList<AbstractCard> tempDraw = new ArrayList<>(draw);
            ArrayList<AbstractCard> tempDiscard = new ArrayList<>(discard);
            hand.clear();
            draw.clear();
            discard.clear();
            if(isToDrawPile){
                draw.addAll(tempDiscard);
                discard.addAll(tempHand);

                if(tempDraw.size()<= BaseMod.MAX_HAND_SIZE){
                    hand.addAll(tempDraw);
                } else {
                    for(int i=0;i<BaseMod.MAX_HAND_SIZE;i++){
                        hand.add(tempDraw.get(i));
                    }
                    for(int i=BaseMod.MAX_HAND_SIZE;i<tempDraw.size();i++){
                        discard.add(tempDraw.get(i));
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, ShammyPeach.BLOCK.value));
                    }
                }
            } else {
                draw.addAll(tempHand);
                discard.addAll(tempDraw);

                if(tempDiscard.size()<=BaseMod.MAX_HAND_SIZE){
                    hand.addAll(tempDiscard);
                } else {
                    for(int i=0;i<BaseMod.MAX_HAND_SIZE;i++){
                        hand.add(tempDiscard.get(i));
                    }
                    for(int i=BaseMod.MAX_HAND_SIZE;i<tempDiscard.size();i++){
                        discard.add(tempDiscard.get(i));
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, ShammyPeach.BLOCK.value));
                    }
                }
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        updatePinkScreenColor();

        if (this.duration < 0.0F) {
            for(AbstractCard c:AbstractDungeon.player.hand.group){
                c.lighten(true);
            }
            for(AbstractCard c:AbstractDungeon.player.drawPile.group){
                c.lighten(true);
                c.clearPowers();
                c.stopGlowing();
            }
            for(AbstractCard c:AbstractDungeon.player.discardPile.group){
                c.lighten(true);
                c.clearPowers();
                c.stopGlowing();
            }
            AbstractDungeon.player.onCardDrawOrDiscard();
            AbstractDungeon.player.hand.refreshHandLayout();
            this.isDone = true;
        }
    }

    private void updatePinkScreenColor() {
        if (this.duration > DUR*2/3) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
    }

    @Override
    public void dispose() {}
}
