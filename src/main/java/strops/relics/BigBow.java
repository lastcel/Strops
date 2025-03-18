package strops.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
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
    private static final String IMG_PATH_O = ModHelper.makeOPath(BigBow.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static boolean isEnabled=false;

    public static final int NUM1=1,NUM2=1,NUM3=10,TIER=3;

    public static final IntSliderSetting LUCKY=new IntSliderSetting("BigBow_Lucky_v0.16.11", "N1", NUM1, 3);
    public static final IntSliderSetting DRAW=new IntSliderSetting("BigBow_Draw_v0.16.11", "N2", NUM2, 1,3);
    public static final IntSliderSetting LIMIT=new IntSliderSetting("BigBow_Limit_v0.16.11", "N3", NUM3, 1,100);
    public static final IntSliderSetting MH=new IntSliderSetting("BigBow_MH_v0.16.11","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("BigBow_G_v0.16.11","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("BigBow_R_v0.16.11","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(LUCKY);
        settings.add(DRAW);
        settings.add(LIMIT);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public BigBow() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        //this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
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
    public void atTurnStart(){
        counter=0;
        grayscale=false;
    }

    @Override
    public void onTrigger(){
        if(counter>=LIMIT.value){
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(BigBow.DRAW.value));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        if(++counter==LIMIT.value){
            grayscale=true;
        }
    }

    @Override
    public void onVictory(){
        counter=-1;
        grayscale=false;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], LUCKY.value, DRAW.value, LIMIT.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], LUCKY.value, DRAW.value, LIMIT.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        //str_out.add(this.DESCRIPTIONS[1]);
        //str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    /*
    @Override
    public boolean canSpawn() {
        return AbstractDungeon.floorNum>=1;
    }

     */
}
