package strops.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.cards.RoamingStrike;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class BanishingMace extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(BanishingMace.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(BanishingMace.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(BanishingMace.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int TIER=4;

    public static final IntSliderSetting MH=new IntSliderSetting("BanishingMace_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("BanishingMace_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("BanishingMace_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public BanishingMace() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        cardToPreview=new RoamingStrike();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void atTurnStartPostDraw(){
        if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID))
                        .relicToDisenchant.equals(BanishingMace.ID)){
            AbstractDungeon.player.getRelic(Decanter.ID).flash();
        } else {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new MakeTempCardInHandAction(new RoamingStrike(), 1, false));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
