//本遗物的一部分patch写在了醒咒器的patch里面
package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class GlowFeather extends StropsAbstractRelic{

    public static final String ID = ModHelper.makePath(GlowFeather.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(GlowFeather.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(GlowFeather.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=12;
    public static final int NUM2=7;

    public static final IntSliderSetting RATIO=new IntSliderSetting("GlowFeather_Ratio", "N1", NUM1, 5,20);
    public static final IntSliderSetting SCOPE=new IntSliderSetting("GlowFeather_Scope", "N2", NUM2, 3,14);
    public static final IntSliderSetting MH=new IntSliderSetting("GlowFeather_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("GlowFeather_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(RATIO);
        settings.add(SCOPE);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public GlowFeather() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        if(AbstractDungeon.getCurrRoom() instanceof ShopRoom){
            flash();
            pulse = true;
        }
        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if (room instanceof com.megacrit.cardcrawl.rooms.ShopRoom) {
            flash();
            pulse = true;

        } else {
            pulse = false;
        }
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], RATIO.value, SCOPE.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], RATIO.value, SCOPE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
