package strops.relics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import strops.helpers.ModHelper;
import strops.powers.RegisterPower;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class Register extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(Register.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Register.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Register.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    private static final Texture hitAreaImg = ImageMaster.loadImage("StropsResources/img/misc/BBBHitArea.png");

    public static float hitTimer = 0.0f;
    public static float xSc = Settings.xScale;
    public static float ySc = Settings.yScale;
    public static int wid=Settings.WIDTH;
    public static int hei=Settings.HEIGHT;
    public static float width=hitAreaImg.getWidth()*xSc;
    public static float height=hitAreaImg.getHeight()*ySc;
    public static float centerX = (wid-width)/2;
    public static float centerY = (hei-height*1.2f);

    public static final int NUM1=2,NUM2=1,NUM3=10,TIER=2;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Register_Threshold","N1", NUM1,1,10);
    public static final IntSliderSetting PENALTY=new IntSliderSetting("Register_Penalty","N2", NUM2,5);
    public static final IntSliderSetting MH=new IntSliderSetting("Register_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Register_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Register_R","R", TIER,0,5);
    public static final IntSliderSetting START_TIME=new IntSliderSetting("Register_Start_Time", "10xT", NUM3, 5,50);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(PENALTY);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(START_TIME);
        return settings;
    }

    public Register() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        grayscale=true;
    }

    @Override
    public void onRightClick() {
        if(StaticHelpers.canClickRelic(this)){
            grayscale=!grayscale;
        }
    }

    @Override
    public void renderAndCheck(SpriteBatch sb){
        if(grayscale){
            return;
        }

        AbstractPlayer p=AbstractDungeon.player;
        if(p.isDraggingCard) {

            renderHitArea(sb);
            renderProgressBar(sb);

            AbstractCard c = p.hoveredCard;
            c.shrink();

            for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
                if(m.hb.hovered&&!m.isDying && !m.isEscaping && m.currentHealth > 0){
                    hitTimer += Gdx.graphics.getDeltaTime();

                    if (hitTimer >= START_TIME.value/10.0f) {
                        flash();
                        p.releaseCard();
                        resetCardBeforeMoving(c);

                        if(!m.hasPower(RegisterPower.POWER_ID)){
                            m.addPower(new RegisterPower(m));
                        }
                        RegisterPower registerPower=(RegisterPower)m.getPower(RegisterPower.POWER_ID);
                        registerPower.cards.add(c);
                        registerPower.updateDescription();

                        if(registerPower.cards.size()%THRESHOLD.value==0){
                            if(PENALTY.value>0){
                                addToBot(new ApplyPowerAction(m,p,new BeatOfDeathPower(m, PENALTY.value)));
                                return;
                            }
                        }

                        AbstractDungeon.player.hand.refreshHandLayout();
                    }
                    return;
                }
            }
        }
        hitTimer = 0.0f;
    }

    /*
    @Override
    public void renderInTopPanel(SpriteBatch sb){
        super.renderInTopPanel(sb);

        if(grayscale){
            return;
        }

        AbstractPlayer p=AbstractDungeon.player;
        if(p.isDraggingCard) {

            renderHitArea(sb);
            renderProgressBar(sb);

            AbstractCard c = p.hoveredCard;
            c.shrink();

            for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
                if(m.hb.hovered&&!m.isDying && !m.isEscaping && m.currentHealth > 0){
                    hitTimer += Gdx.graphics.getDeltaTime();

                    if (hitTimer >= START_TIME.value/10.0f) {
                        flash();
                        p.releaseCard();
                        resetCardBeforeMoving(c);

                        if(!m.hasPower(RegisterPower.POWER_ID)){
                            m.addPower(new RegisterPower(m));
                        }
                        RegisterPower registerPower=(RegisterPower)m.getPower(RegisterPower.POWER_ID);
                        registerPower.cards.add(c);
                        registerPower.updateDescription();

                        if(registerPower.cards.size()%THRESHOLD.value==0){
                            if(PENALTY.value>0){
                                addToBot(new ApplyPowerAction(m,p,new BeatOfDeathPower(m, PENALTY.value)));
                                return;
                            }
                        }

                        AbstractDungeon.player.hand.refreshHandLayout();
                    }
                    return;
                }
            }
        }
        hitTimer = 0.0f;
    }

     */

    public static void renderHitArea(SpriteBatch sb){
        Color col=Color.valueOf("#ff6d06");
        col.a=0.5f;
        sb.setColor(col);
        for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
            if(!m.isDying && !m.isEscaping && m.currentHealth > 0){
                sb.draw(hitAreaImg, m.hb.x, m.hb.y, m.hb.width, m.hb.height,0,0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
            }
        }
    }

    public static void renderProgressBar(SpriteBatch sb){
        int r,g=255,b;
        if(hitTimer<=START_TIME.value/10.0f/2){
            r=myInterpolate(40,90);
            b=myInterpolate(255,205);
        } else {
            r=myInterpolate2(90,255);
            b=myInterpolate2(205,60);
        }
        Color col=Color.valueOf("#"+Integer.toHexString(r)+Integer.toHexString(g)+Integer.toHexString(b));
        col.a=0.5f;
        sb.setColor(col);
        sb.draw(hitAreaImg, centerX-width/5*xSc*2, centerY, width/5*xSc, height*hitTimer/(START_TIME.value/10.0f),  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    public static int myInterpolate(float startValue, float endValue){
        return MathUtils.round(startValue+(endValue-startValue)*hitTimer/(START_TIME.value/10.0f/2));
    }

    public static int myInterpolate2(float startValue, float endValue){
        return MathUtils.round(startValue+(endValue-startValue)*(hitTimer/(START_TIME.value/10.0f/2)-1));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],THRESHOLD.value,PENALTY.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],THRESHOLD.value,PENALTY.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    private void resetCardBeforeMoving(AbstractCard c) {
        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        AbstractDungeon.player.hand.group.remove(c);
    }

    @Override
    public void onVictory(){
        grayscale=true;
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        grayscale=true;
    }
}
