package strops.relics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.actions.FilmStarryCurtainAction;
import strops.cards.Aurora;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class FilmStarryCurtain extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(FilmStarryCurtain.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(FilmStarryCurtain.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(FilmStarryCurtain.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int TIER=4;

    public static final IntSliderSetting MH=new IntSliderSetting("FilmStarryCurtain_MH_v0.13.1","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("FilmStarryCurtain_G_v0.13.1","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("FilmStarryCurtain_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public FilmStarryCurtain() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;

        this.cardToPreview=new Aurora();
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
                ((Decanter)AbstractDungeon.player.getRelic(Decanter.ID)).relicToDisenchant.equals(FilmStarryCurtain.ID)){
            return;
        }

        flash();
        addToBot(new FilmStarryCurtainAction(AbstractDungeon.player, BaseMod.MAX_HAND_SIZE));
    }

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
