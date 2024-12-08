package strops.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import strops.helpers.ModHelper;
import strops.patch.*;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlackRabbit extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(BlackRabbit.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(BlackRabbit.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(BlackRabbit.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public boolean onCardsSelected=true,offCardsSelected=true;
    //private AbstractDungeon.CurrentScreen prevScreen;
    private AbstractRoom.RoomPhase prevPhase;
    private final CardGroup offGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private final CardGroup onGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private final CardGroup offResult = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private final CardGroup onResult = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    int numCards;

    public static final int NUM1=0,TIER=1;

    public static final IntSliderSetting UPGRADABLE=new IntSliderSetting("BlackRabbit_Upgradable","S1", NUM1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("BlackRabbit_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("BlackRabbit_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("BlackRabbit_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(UPGRADABLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public BlackRabbit() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        if(UPGRADABLE.value>0) {
            ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
            List<AbstractCard> upgradeReadyCards;
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade()){
                    upgradableCards.add(c);
                }
            }
            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
            upgradeReadyCards=upgradableCards.subList(0,UPGRADABLE.value);

            for (AbstractCard c : upgradeReadyCards) {
                if (c.canUpgrade()) {
                    float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c
                            .makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                    c.upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(c);
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        if(UPGRADABLE.value==0){
            return this.DESCRIPTIONS[0];
        }
        return String.format(DESCRIPTIONS[7], UPGRADABLE.value)+DESCRIPTIONS[0];
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(UPGRADABLE.value==0){
            str_out.add(this.DESCRIPTIONS[0]);
        } else {
            str_out.add(String.format(DESCRIPTIONS[7], UPGRADABLE.value)+DESCRIPTIONS[0]);
        }

        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public void onRightClick(){
        if(grayscale){
            return;
        }

        AbstractRoom currRoom= AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            return;
        }

        if(AbstractDungeon.screen==AbstractDungeon.CurrentScreen.GRID||AbstractDungeon.screen==AbstractDungeon.CurrentScreen.CARD_REWARD){
            return;
        }

        offGroup.clear();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if(c.upgraded&&!c.makeCopy().upgraded){
                offGroup.addToTop(c);
            }
        }
        if (offGroup.group.isEmpty()) {
            this.offCardsSelected = true;
            return;
        }
        this.offCardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        prevPhase=AbstractDungeon.getCurrRoom().phase;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        //prevScreen = AbstractDungeon.screen;
        AbstractDungeon.gridSelectScreen.open(offGroup, offGroup.size(), true, this.DESCRIPTIONS[5]);
    }

    @Override
    public void update() {
        super.update();
        if(!offCardsSelected && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            //logger.info("------降级屏幕已关闭------");
            //logger.info("当前屏幕="+AbstractDungeon.screen);
            offCardsSelected = true;
            numCards = AbstractDungeon.gridSelectScreen.selectedCards.size();

            if(numCards==0){
                onCardsSelected=true;
                AbstractDungeon.getCurrRoom().phase = prevPhase;
                return;
            }

            offResult.clear();
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){
                offResult.addToTop(c);
                c.stopGlowing();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            onGroup.clear();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if(c.canUpgrade()&&!offResult.group.contains(c)){
                    onGroup.addToTop(c);
                }
            }
            if (onGroup.group.isEmpty()) {
                onCardsSelected = true;
                AbstractDungeon.getCurrRoom().phase = prevPhase;
                return;
            }
            onCardsSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            //logger.info("------预备打开升级屏幕------");
            AbstractDungeon.gridSelectScreen.open(onGroup, Math.min(onGroup.size(), numCards), true, String.format(this.DESCRIPTIONS[6], numCards));
        } else if(!onCardsSelected && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            //logger.info("------升级屏幕已关闭------");
            //logger.info("当前屏幕="+AbstractDungeon.screen);
            onCardsSelected = true;

            if(AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()){
                AbstractDungeon.getCurrRoom().phase = prevPhase;
                return;
            }

            onResult.clear();
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){
                onResult.addToTop(c);
                c.stopGlowing();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            for(AbstractCard c:offResult.group){
                //logger.info("降级卡牌："+c.name+"，升级次数="+c.timesUpgraded);
                //logger.info(c.name+"在牌组中？="+AbstractDungeon.player.masterDeck.group.contains(c));
                AbstractCard cNew=c.makeCopy();
                cNew.upgrade();
                boolean isAcquiredSelfRetain=(c.selfRetain&&!cNew.selfRetain)||c.makeCopy().selfRetain;
                AbstractCard c2 = PatchGlassRod.myGetCopy(c.cardID,c.timesUpgraded-1,c.misc,isAcquiredSelfRetain);
                c2.uuid=c.uuid;
                c2.inBottleFlame=c.inBottleFlame;
                c2.inBottleLightning=c.inBottleLightning;
                c2.inBottleTornado=c.inBottleTornado;
                PatchBigBangBell.PatchTool1.inBigBangBell.set(c2,PatchBigBangBell.PatchTool1.inBigBangBell.get(c));
                PatchTranscendence.PatchTool1.inTranscendence.set(c2,PatchTranscendence.PatchTool1.inTranscendence.get(c));
                PatchGrabbyHands.PatchTool6.isGrabbed.set(c2,PatchGrabbyHands.PatchTool6.isGrabbed.get(c));
                PatchGrabbyHands.PatchTool7.ages.set(c2,PatchGrabbyHands.PatchTool7.ages.get(c));
                PatchCatalyst.PatchTool1.inCatalyst.set(c2,PatchCatalyst.PatchTool1.inCatalyst.get(c));

                AbstractDungeon.player.masterDeck.group.set(AbstractDungeon.player.masterDeck.group.indexOf(c),c2);
                //logger.info("完成降级卡牌："+c2.name+"，升级次数="+c2.timesUpgraded);
                //logger.info(c.name+"在牌组中="+AbstractDungeon.player.masterDeck.group.contains(c2));
            }

            for(AbstractCard c:onResult.group){
                //logger.info("升级卡牌："+c.name);
                c.upgrade();
            }

            if(AbstractDungeon.player.hasRelic(GlassRod.ID)){
                counter=-2;
                usedUp();
            }

            AbstractDungeon.getCurrRoom().phase = prevPhase;
        }
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        }
    }
}
