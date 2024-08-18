package strops.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Bolster extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(Bolster.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Bolster.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=11,NUM2=3,TIER=3;

    public static final IntSliderSetting THRESHOLD = new IntSliderSetting("Bolster_Threshold", "N1", NUM1, 5,15);
    public static final IntSliderSetting BONUS = new IntSliderSetting("Bolster_Bonus", "N2", NUM2, 1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("Bolster_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Bolster_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Bolster_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Bolster() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);

        if(DESCRIPTIONS[5].equals("1")){
            flavorText=String.format(flavorText,THRESHOLD.value);
        }
    }

    //public boolean isequipped=false;

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        //isequipped=true;
    }

    @Override
    public void update() {
        super.update();
        if(isObtained){
            counter=AbstractDungeon.player.relics.size();
            this.pulse=(counter >= THRESHOLD.value);
        }
    }

    @Override
    public void atBattleStart(){
        if(counter>=THRESHOLD.value){
            flash();
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, BONUS.value), BONUS.value));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],THRESHOLD.value,BONUS.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public void setCounter(int setCounter){
        counter=setCounter;

        if(counter>=THRESHOLD.value){
            flash();
        }
    }
}
