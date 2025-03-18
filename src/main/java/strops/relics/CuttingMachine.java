package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import strops.cards.PartScrapper;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class CuttingMachine extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(CuttingMachine.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(CuttingMachine.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(CuttingMachine.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=1,TIER=5;

    public static final IntSliderSetting COPIES=new IntSliderSetting("CuttingMachine_Copies","N1", NUM1,1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("CuttingMachine_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("CuttingMachine_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("CuttingMachine_R","R", TIER,0,5);
    public static final IntSliderSetting P=new IntSliderSetting("CuttingMachine_P","P", 150,50,300);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COPIES);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(P);
        return settings;
    }

    public CuttingMachine() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.cardToPreview=new PartScrapper();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        for(int i=0;i<COPIES.value;i++){
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new PartScrapper(),
                    Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],COPIES.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],COPIES.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public int getPrice(){
        return P.value;
    }
}
