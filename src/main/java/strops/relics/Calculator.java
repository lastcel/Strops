package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.actions.CalculatorAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class Calculator extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(Calculator.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Calculator.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Calculator.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=7;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Calculator_Threshold", "N1", NUM1, 4,10);
    public static final IntSliderSetting MH=new IntSliderSetting("Calculator_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Calculator_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Calculator() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.player.masterHandSize-=5;
    }

    @Override
    public void onUnequip(){
        AbstractDungeon.player.masterHandSize+=5;
    }

    @Override
    public void atTurnStart() {
        counter=0;
        grayscale=false;
    }

    @Override
    public void onRightClick(){
        if (!StaticHelpers.canClickRelic(this)) {
            return;
        }

        if(grayscale){
            return;
        }

        addToBot(new DrawCardAction(1,new CalculatorAction()));
    }

    @Override
    public void onVictory(){
        counter=-1;
        grayscale=false;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
