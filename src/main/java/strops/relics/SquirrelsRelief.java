package strops.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.OddlySmoothStone;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.vfx.ObtainRelicLater;

import java.util.ArrayList;

public class SquirrelsRelief extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(SquirrelsRelief.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SquirrelsRelief.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(SquirrelsRelief.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=1;

    public static final IntSliderSetting BONUS=new IntSliderSetting("SquirrelsRelief_Bonus", "N1", NUM1, 3);
    public static final IntSliderSetting MH=new IntSliderSetting("SquirrelsRelief_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SquirrelsRelief_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public SquirrelsRelief() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        for(int i=0;i<BONUS.value;i++){
            AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(RelicLibrary.getRelic(OddlySmoothStone.ID).makeCopy()));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
