package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SoulCraft extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(SoulCraft.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SoulCraft.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SoulCraft.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=3,TIER=5;

    public static final IntSliderSetting USABLE=new IntSliderSetting("SoulCraft_Usable","N1", NUM1,1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("SoulCraft_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SoulCraft_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SoulCraft_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(USABLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public SoulCraft() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],USABLE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],USABLE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
