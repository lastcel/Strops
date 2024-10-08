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
    private static final String IMG_PATH_O = ModHelper.makeOPath(SquirrelsRelief.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=1,TIER=5;

    public static final IntSliderSetting BONUS=new IntSliderSetting("SquirrelsRelief_Bonus_v0.16.5", "N1", NUM1, 4);
    public static final IntSliderSetting MH=new IntSliderSetting("SquirrelsRelief_MH_v0.16.5","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SquirrelsRelief_G_v0.16.5","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SquirrelsRelief_R_v0.16.5","R", TIER,0,5);
    public static final IntSliderSetting P=new IntSliderSetting("SquirrelsRelief_P","P", 100,300);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(P);
        return settings;
    }

    public SquirrelsRelief() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
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

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public int getPrice(){
        return P.value;
    }
}
