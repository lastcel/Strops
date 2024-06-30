package strops.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Key extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(Key.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Key.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Key.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final IntSliderSetting MH=new IntSliderSetting("Key_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Key_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Key() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
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
    public void onEnterRoom(AbstractRoom room){
        counter=0;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
