package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import java.util.ArrayList;

public class Leviboard extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Leviboard.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Leviboard.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(SquirrelsRelief.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=20,NUM2=350;

    public static final IntSliderSetting HP_THRESHOLD=new IntSliderSetting("Leviboard_Threshold_Hp", "N1", NUM1, 1,30);
    public static final IntSliderSetting GOLD_THRESHOLD=new IntSliderSetting("Leviboard_Threshold_Gold", "N2", NUM2, 100,400);
    public static final IntSliderSetting MH=new IntSliderSetting("Leviboard_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Leviboard_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(HP_THRESHOLD);
        settings.add(GOLD_THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Leviboard() {
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
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], HP_THRESHOLD.value,GOLD_THRESHOLD.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], HP_THRESHOLD.value,GOLD_THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 40));
    }

    public static boolean canFly(){
        return (AbstractDungeon.player.currentHealth<=HP_THRESHOLD.value)||
                (AbstractDungeon.player.gold>=GOLD_THRESHOLD.value);
    }
}
