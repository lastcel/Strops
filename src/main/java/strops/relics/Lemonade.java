package strops.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.patch.PatchGrassNowAndFlowersThen;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Lemonade extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Lemonade.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Lemonade.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Lemonade.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=7,NUM2=1,NUM3=4,NUM4=1;

    public boolean isUsed=false;

    public static final IntSliderSetting ENERGY=new IntSliderSetting("Lemonade_Energy", "N1", NUM1, 4,8);
    public static final IntSliderSetting DRAW=new IntSliderSetting("Lemonade_Draw", "N2", NUM2, 1,3);
    public static final IntSliderSetting COMBAT=new IntSliderSetting("Lemonade_Combat", "N3", NUM3, 1,10);
    public static final IntSliderSetting POTION=new IntSliderSetting("Lemonade_Potion", "N4", NUM4, 1,4);
    public static final IntSliderSetting MH=new IntSliderSetting("Lemonade_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Lemonade_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(ENERGY);
        settings.add(DRAW);
        settings.add(COMBAT);
        settings.add(POTION);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Lemonade() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void atTurnStart(){
        if(PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=2){
            isUsed=false;
        }
    }

    public void onVictory() {
        counter=(++counter)%COMBAT.value;
        isUsed=false;
        if(counter==COMBAT.value-1){
            beginLongPulse();
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=COMBAT.value-1;
        isUsed=false;
        pulse=true;
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if (counter == COMBAT.value-1){
            flash();
            pulse=true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], ENERGY.value, DRAW.value,
                COMBAT.value, POTION.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], ENERGY.value, DRAW.value,
                COMBAT.value, POTION.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return (AbstractDungeon.floorNum<=43);
    }

    /*
    @Override
    public boolean canSpawn(){
        return (!AbstractDungeon.player.hasRelic(ModHelper.makePath(Atri.class.getSimpleName())))&&
                (AbstractDungeon.floorNum<=43);
    }

     */
}
