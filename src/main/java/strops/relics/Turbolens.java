package strops.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Turbolens extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Turbolens.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=0,NUM2=1,NUM3=1,TIER=3;

    public static final IntSliderSetting DIFFERENCE=new IntSliderSetting("Turbolens_Difference", "N1", NUM1, -3,3);
    public static final IntSliderSetting PLAYED=new IntSliderSetting("Turbolens_Played", "N2", NUM2, 1,3);
    public static final IntSliderSetting DRAW=new IntSliderSetting("Turbolens_Draw", "N3", NUM3, 1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("Turbolens_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Turbolens_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Turbolens_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(DIFFERENCE);
        settings.add(PLAYED);
        settings.add(DRAW);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Turbolens() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStart(){
        counter=0;
    }

    @Override
    public void onVictory(){
        counter=-1;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], PLAYED.value, DIFFERENCE.value, DRAW.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], PLAYED.value, DIFFERENCE.value, DRAW.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction){
        if(EnergyPanel.totalCount-AbstractDungeon.player.hand.size()>DIFFERENCE.value){
            counter++;
            if(counter==PLAYED.value){
                counter=0;
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

                addToBot(new DrawCardAction(AbstractDungeon.player, DRAW.value));
            }
        }
    }
}
