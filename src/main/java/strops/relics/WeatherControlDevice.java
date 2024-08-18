package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import strops.cards.LightningStorm;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

import static strops.cards.LightningStorm.LightningCountLastTurn;

public class WeatherControlDevice extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(WeatherControlDevice.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(WeatherControlDevice.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(WeatherControlDevice.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final IntSliderSetting MH=new IntSliderSetting("WCD_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("WCD_G","G",0,-100,100);
    public static final IntSliderSetting P=new IntSliderSetting("WCD_P","P", 150,50,300);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(P);
        return settings;
    }

    public WeatherControlDevice() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.cardToPreview=new LightningStorm();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new LightningStorm(),
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    @Override
    public void update(){
        super.update();
        if (!isObtained) {
            return;
        }
        counter=(AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.COMBAT?LightningCountLastTurn:-1);
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

    @Override
    public int getPrice(){
        return P.value;
    }
}
