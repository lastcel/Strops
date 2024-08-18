package strops.relics;

import com.megacrit.cardcrawl.cards.purple.JustLucky;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class BigBow extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(BigBow.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(BigBow.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(FTLEngines.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static boolean isEnabled=false;

    public static final int NUM1=1,NUM2=1,TIER=2;

    public static final IntSliderSetting LUCKY=new IntSliderSetting("BigBow_Lucky", "N1", NUM1, 3);
    public static final IntSliderSetting DRAW=new IntSliderSetting("BigBow_Draw", "N2", NUM2, 1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("BigBow_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("BigBow_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("BigBow_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(LUCKY);
        settings.add(DRAW);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public BigBow() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.cardToPreview=new JustLucky();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        for(int i=0;i<LUCKY.value;i++){
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(CardLibrary.getCard(JustLucky.ID).makeCopy(),
                    Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], LUCKY.value, DRAW.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], LUCKY.value, DRAW.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
