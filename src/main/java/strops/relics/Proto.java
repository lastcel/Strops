/*
package strops.relics;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class Proto extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(Proto.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Proto.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=20;
    public static final int NUM2=10;

    public boolean targetMode=false;
    public AbstractMonster hoveredMonster = null;

    public Proto() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        for (int i = 0; i < this.points.length; i++){
            this.points[i] = new Vector2();
        }
    }


    @Override
    public void onRightClick(){
        if(!StaticHelpers.canClickRelic(this)){
            return;
        }
        targetMode=true;
        GameCursor.hidden = true;
    }

    @Override
    public void update(){
        super.update();
        if (!isObtained) {
            return;
        }
        if(!targetMode){
            return;
        }
        updateTargetMode();
    }

    @Override
    public void updateTargetMode() {
        if (InputHelper.justClickedRight || AbstractDungeon.isScreenUp || InputHelper.mY > Settings.HEIGHT - 80.0F * Settings.scale || AbstractDungeon.player.hoveredCard != null || InputHelper.mY < 140.0F * Settings.scale || CInputActionSet.cancel

                .isJustPressed()) {
            CInputActionSet.cancel.unpress();
            this.targetMode = false;
            GameCursor.hidden = false;
        }
        this.hoveredMonster = null;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m.hb.hovered && !m.isDying) {
                this.hoveredMonster = m;
                break;
            }
        }
        if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
            InputHelper.justClickedLeft = false;
            CInputActionSet.select.unpress();
            if (this.hoveredMonster != null) {
                if (AbstractDungeon.player.hasPower("Surrounded")){
                    AbstractDungeon.player.flipHorizontal = (this.hoveredMonster.drawX < AbstractDungeon.player.drawX);
                }
                cannonShoot(this.hoveredMonster);
                if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT){
                    AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                }
                this.targetMode = false;
                GameCursor.hidden = false;
            }
        }
    }

    public void cannonShoot(AbstractCreature enemy){
        if(AbstractDungeon.player.gold<NUM1){
            return;
        }
        AbstractDungeon.player.loseGold(NUM1);
        DamageInfo info = new DamageInfo(AbstractDungeon.player, NUM2, DamageInfo.DamageType.THORNS);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(enemy, info, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {

        super.renderInTopPanel(sb);
        if (this.targetMode) {
            if (this.hoveredMonster != null){
                this.hoveredMonster.renderReticle(sb);
            }

            renderTargetingUi(sb);
        }
    }

    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],NUM1,NUM2);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],NUM1,NUM2));
        return str_out;
    }

    public AbstractRelic makeCopy() {
        return new Proto();
    }
}

 */
