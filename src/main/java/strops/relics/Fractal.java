package strops.relics;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.cards.BodySlamPlusPlus;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Fractal extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(Fractal.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Fractal.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Fractal.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    AbstractCard BodySlamAdded;

    int stepsToGrayScale =0;

    public static final int NUM1=1,NUM2=2,TIER=5;

    public static final IntSliderSetting MULTIPLE=new IntSliderSetting("Fractal_Multiple", "N1", NUM1, 1, 10);
    public static final IntSliderSetting SINGLE=new IntSliderSetting("Fractal_Single", "N2", NUM2,1,  10);
    public static final IntSliderSetting MH=new IntSliderSetting("Fractal_MH_v0.12.8","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Fractal_G_v0.12.8","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Fractal_R","R", TIER,0,5);
    public static final IntSliderSetting B1=new IntSliderSetting("Fractal_B1","B1", 0,1);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MULTIPLE);
        settings.add(SINGLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(B1);
        return settings;
    }

    public Fractal() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);

        AbstractCard c=new BodySlam();
        c.upgrade();
        this.cardToPreview=c;
    }

    @Override
    public void atBattleStartPreDraw(){
        counter=0;
        stepsToGrayScale=0;

        if(B1.value==1){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            grayscale=true;

            BodySlamAdded=new BodySlam();
            BodySlamAdded.upgrade();
            addToBot(new MakeTempCardInHandAction(BodySlamAdded, 1, false));
            return;
        }

        if(MULTIPLE.value==1){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            stepsToGrayScale++;

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

        if(SINGLE.value==1){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            stepsToGrayScale++;
            if(stepsToGrayScale==2){
                grayscale=true;
            }

            BodySlamAdded=new BodySlam();
            BodySlamAdded.upgrade();
            addToBot(new MakeTempCardInHandAction(BodySlamAdded, 1, false));
        }
    }

    @Override
    public void atTurnStart()
    {
        counter++;

        if(B1.value==1){
            if(counter!=1&&counter<=SINGLE.value){
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                flash();
                if(counter==SINGLE.value){
                    grayscale=true;
                }

                BodySlamAdded=new BodySlam();
                BodySlamAdded.upgrade();
                addToBot(new MakeTempCardInHandAction(BodySlamAdded, 1, false));
            }
            return;
        }

        if(counter!=1&&counter==MULTIPLE.value){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            stepsToGrayScale++;
            if(stepsToGrayScale==2){
                grayscale=true;
            }

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

        if(counter!=1&&counter==SINGLE.value){
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            flash();
            stepsToGrayScale++;
            if(stepsToGrayScale==2){
                grayscale=true;
            }

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
        counter=-1;
        grayscale=false;
    }

    @Override
    public void onRefreshHand(){
        ArrayList<AbstractCard> bodySlamPieces=new ArrayList<>();
        for(AbstractCard c:AbstractDungeon.player.hand.group){
            if(c.cardID.equals(BodySlam.ID)&&c.upgraded){
                bodySlamPieces.add(c);
                //Strops.logger.info("检测到全身撞击："+c.uuid);
            }
            if(bodySlamPieces.size()==3){
                break;
            }
        }

        if(bodySlamPieces.size()==3){
            for(AbstractCard c:bodySlamPieces){
                AbstractDungeon.player.hand.group.remove(c);
            }

            for(AbstractCard c:bodySlamPieces){
                resetCardBeforeMoving(c);
            }

            addToBot(new MakeTempCardInHandAction(new BodySlamPlusPlus(), 1));
            addToBot(new DrawCardAction(AbstractDungeon.player,1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        if(B1.value!=1){
            return String.format(this.DESCRIPTIONS[0], MULTIPLE.value, SINGLE.value);
        }
        return String.format(this.DESCRIPTIONS[5], SINGLE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(B1.value!=1){
            str_out.add(String.format(this.DESCRIPTIONS[0], MULTIPLE.value, SINGLE.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[5], SINGLE.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    private void resetCardBeforeMoving(AbstractCard c) {
        if (AbstractDungeon.player.hoveredCard == c){
            AbstractDungeon.player.releaseCard();
        }
        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        //AbstractDungeon.player.hand.group.remove(c);
    }
}
