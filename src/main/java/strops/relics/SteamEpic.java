package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=8,TIER=3;

    public static final IntSliderSetting CAPACITY=new IntSliderSetting("SteamEpic_Capacity","N1", NUM1,1,20);
    public static final IntSliderSetting MH=new IntSliderSetting("SteamEpic_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SteamEpic_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SteamEpic_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(CAPACITY);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public SteamEpic() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStart(){
        counter=0;
    }

    @Override
    public void onVictory(){
        counter=-1;
        grayscale=false;
    }

    public void onThisTriggered(){
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new SteamEpicAction(this));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],CAPACITY.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],CAPACITY.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
