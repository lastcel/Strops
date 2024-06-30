package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.actions.SteamEpicAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SteamEpic extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(SteamEpic.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SteamEpic.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(SteamEpic.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final IntSliderSetting MH=new IntSliderSetting("SteamEpic_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SteamEpic_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public SteamEpic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onPlayerEndTurn(){
        flash();
        addToBot(new SteamEpicAction());
    }



    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
