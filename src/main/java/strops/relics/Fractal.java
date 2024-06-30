package strops.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Fractal extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(Fractal.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Fractal.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Fractal.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    AbstractCard BodySlamAdded;

    //public static int NUM1=2;

    //public static final IntSliderSetting DURATION=new IntSliderSetting("Fractal_Duration", "N1", NUM1, 1, 10);
    //public static final IntSliderSetting OLD=new IntSliderSetting("Fractal_Old", "OLD", 0,  1);
    public static final IntSliderSetting MH=new IntSliderSetting("Fractal_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Fractal_G_v0.12.0","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Fractal() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);

        AbstractCard c=new BodySlam();
        c.upgrade();
        this.cardToPreview=c;
    }

    @Override
    public void atBattleStartPreDraw(){
        counter=0;
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        flash();

        BodySlamAdded=new BodySlam();
        BodySlamAdded.upgrade();
        addToBot(new MakeTempCardInHandAction(BodySlamAdded, 1, false));
        BodySlamAdded=new BodySlam();
        BodySlamAdded.upgrade();
        addToBot(new MakeTempCardInDrawPileAction(BodySlamAdded, 1, true,true));
        BodySlamAdded=new BodySlam();
        BodySlamAdded.upgrade();
        addToBot(new MakeTempCardInDiscardAction(BodySlamAdded, 1));
    }

    @Override
    public void atTurnStart()
    {
        counter++;
        if(counter==2){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            grayscale=true;

            BodySlamAdded=new BodySlam();
            BodySlamAdded.upgrade();
            addToBot(new MakeTempCardInHandAction(BodySlamAdded, 1, false));
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onVictory() {
        counter = -1;
        grayscale=false;
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
