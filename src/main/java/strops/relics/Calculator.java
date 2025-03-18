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
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=5,NUM2=7,TIER=5;

    public static final IntSliderSetting PENALTY=new IntSliderSetting("Calculator_Penalty", "N1", NUM1, 5);
    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Calculator_Threshold", "N2", NUM2, 10);
    public static final IntSliderSetting MH=new IntSliderSetting("Calculator_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Calculator_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Calculator_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PENALTY);
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Calculator() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.player.masterHandSize-=PENALTY.value;
    }

    @Override
    public void onUnequip(){
        AbstractDungeon.player.masterHandSize+=PENALTY.value;
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
        return String.format(this.DESCRIPTIONS[0], PENALTY.value, THRESHOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], PENALTY.value, THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
