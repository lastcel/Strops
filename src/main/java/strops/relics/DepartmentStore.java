package strops.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class DepartmentStore extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(DepartmentStore.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(DepartmentStore.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(DepartmentStore.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int TIER=4;

    public static final IntSliderSetting MH=new IntSliderSetting("DepartmentStore_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("DepartmentStore_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("DepartmentStore_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public DepartmentStore() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip() {
        onEquipMods(MH,G);
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

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
