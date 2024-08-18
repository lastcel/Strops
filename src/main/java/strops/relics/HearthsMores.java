package strops.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class HearthsMores extends StropsAbstractRelic implements ClickableRelic, CustomSavable<Boolean> {
    public static final String ID = ModHelper.makePath(HearthsMores.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(HearthsMores.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(FTLEngines.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;
    private static final Texture hitAreaImg = ImageMaster.loadImage("StropsResources/img/misc/BBBHitArea.png");

    public static float width=200.0F * Settings.scale;
    public static float height=200.0F * Settings.scale;
    public static float centerX = 0;
    public static float centerY = 0;

    public static final int NUM1=2;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("HearthsMores_Threshold", "N1", NUM1, 1,6);
    public static final IntSliderSetting MH=new IntSliderSetting("HearthsMores_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("HearthsMores_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public HearthsMores() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atTurnStart(){
        counter=0;
    }

    @Override
    public void onVictory(){
        counter=-1;
    }

    @Override
    public void onRightClick() {
        if(!hasTriColor()){
            return;
        }

        if(StaticHelpers.canClickRelic2(this)){
            grayscale=!grayscale;
        }
    }

    @Override
    public Boolean onSave(){
        return grayscale;
    }

    @Override
    public void onLoad(Boolean savedGrayscale){
        grayscale=savedGrayscale;
    }

    @Override
    public void renderAndCheck(SpriteBatch sb){
        if (AbstractDungeon.player.isDraggingCard) {
            renderHitArea(sb);
        }
    }

    /*
    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        super.renderInTopPanel(sb);
        if (AbstractDungeon.player.isDraggingCard) {
            renderHitArea(sb);
            \*
            if ((InputHelper.mX >= centerX) &&
                    (InputHelper.mX <= centerX + width) &&
                    (InputHelper.mY >= centerY) &&
                    (InputHelper.mY <= centerY + height)) {
                logger.info("鼠标处在待判定区");
                AbstractPlayer p=AbstractDungeon.player;
                if ((InputHelper.justClickedLeft || InputActionSet.confirm.isJustPressed() || CInputActionSet.select
                        .isJustPressed())){
                    logger.info("检测到左键点击");
                    boolean isEasyDragging=false;
                    try {
                        Field f = AbstractPlayer.class.getDeclaredField("isUsingClickDragControl");
                        f.setAccessible(true);
                        isEasyDragging=(boolean) f.get(p);
                        logger.info("是否拖动="+isEasyDragging);
                    } catch (IllegalAccessException|NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                    if(isEasyDragging){
                        //InputHelper.justClickedLeft = false;
                        getAnotherCard(p);
                    }
                } else if(InputHelper.justReleasedClickLeft){
                    logger.info("检测到松开左键");
                    getAnotherCard(p);
                }
            }

             *\
        }
    }
    */

    public static void renderHitArea(SpriteBatch sb){
        Color col=Color.valueOf("#ff6d06");
        col.a=0.5f;
        sb.setColor(col);
        sb.draw(hitAreaImg, centerX, centerY, width, height,  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    /*
    private static void getAnotherCard(AbstractPlayer p){
        AbstractCard c = p.hoveredCard;
        logger.info("悬停卡为："+c);
        p.releaseCard();
        p.hand.moveToDeck(c,true);
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p,1));
    }

     */

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
