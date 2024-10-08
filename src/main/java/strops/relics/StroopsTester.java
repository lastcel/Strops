package strops.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.curses.Pride;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class StroopsTester extends StropsAbstractRelic implements CustomSavable<Integer> {
    public static final String ID = ModHelper.makePath(StroopsTester.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(StroopsTester.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(StroopsTester.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    private boolean activated = true;

    public static final int NUM1=2,NUM2=1,TIER=4;

    public static final IntSliderSetting DUTY=new IntSliderSetting("StroopsTester_Duty","N2", NUM1,10);
    public static final IntSliderSetting INTERVAL=new IntSliderSetting("StroopsTester_Interval","N1-N2", NUM2,10);
    public static final IntSliderSetting MH=new IntSliderSetting("StroopsTester_MH_v0.16.1","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("StroopsTester_G_v0.16.1","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("StroopsTester_R_v0.16.1","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(DUTY);
        settings.add(INTERVAL);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public StroopsTester() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        cardToPreview=new Pride();
        color=Color.SCARLET;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        /*AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Pride(),
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

         */
        if(DUTY.value==0){
            return;
        }
        if(INTERVAL.value==0){
            secondCounter=99;
        } else {
            counter=INTERVAL.value;
        }
    }

    @Override
    public Integer onSave(){
        return secondCounter;
    }

    @Override
    public void onLoad(Integer savedSecondCounter){
        secondCounter=savedSecondCounter;
    }

    @Override
    public void atBattleStartPreDraw(){
        if(DUTY.value==0){
            return;
        }

        if(counter>0){
            counter--;
            return;
        }

        if(INTERVAL.value>0){
            secondCounter--;
        }

        if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                        .relicToDisenchant.equals(StroopsTester.ID)){
            AbstractDungeon.player.getRelic(Decanter.ID).beginLongPulse();
            AbstractDungeon.player.getRelic(Decanter.ID).flash();
        } else {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new MakeTempCardInDrawPileAction(new Pride(), 1, false, true));
        }
    }

    @Override
    public void onVictory(){
        if(DUTY.value==0){
            return;
        }

        if(counter==0){
            counter=-1;
            secondCounter=DUTY.value;
        } else if(secondCounter==0){
            secondCounter=-1;
            counter=INTERVAL.value;
        }
    }

    @Override
    public String getUpdatedDescription() {
        if(DESCRIPTIONS[5].equals("1")){
            return String.format(this.DESCRIPTIONS[0],DUTY.value,DUTY.value+INTERVAL.value);
        }
        return String.format(this.DESCRIPTIONS[0],DUTY.value+INTERVAL.value,DUTY.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(DESCRIPTIONS[5].equals("1")){
            str_out.add(String.format(this.DESCRIPTIONS[0],DUTY.value,DUTY.value+INTERVAL.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[0],DUTY.value+INTERVAL.value,DUTY.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.upgraded && this.activated) {
            this.activated = false;
            flash();
            AbstractMonster m = null;
            if (action.target != null){
                m = (AbstractMonster)action.target;
            }
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0F;
            tmp.applyPowers();
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            this.pulse = false;
        }
    }

    public void atTurnStart() {
        this.activated = true;
    }

    public boolean checkTrigger() {
        return this.activated;
    }
}
