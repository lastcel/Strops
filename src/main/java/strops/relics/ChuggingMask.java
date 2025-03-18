package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ChuggingMask extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ChuggingMask.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ChuggingMask.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(ChuggingMask.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int MAXHP=5;
    //public static final int TIMES_LIMIT_PER_FLOOR=5;

    //public static final IntSliderSetting BONUS=new IntSliderSetting("ChuggingMask_Bonus_v0.12.4", "N1", NUM1, 6);
    public static final IntSliderSetting MH=new IntSliderSetting("ChuggingMask_MH_v0.13.4","MH", MAXHP,-20,25);
    public static final IntSliderSetting G=new IntSliderSetting("ChuggingMask_G_v0.13.4","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public ChuggingMask() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        //counter=0;
    }

    /*
    @Override
    public void onEnterRoom(AbstractRoom room) {
        counter=0;
    }

     */

    /*
    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

     */

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
