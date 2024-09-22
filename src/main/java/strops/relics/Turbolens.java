//本遗物的部分patch写在了燕雀、百货公司和超光速引擎的patch里面

package strops.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.patch.PatchTurbolens;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class Turbolens extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH_S = ModHelper.makeIPath(Turbolens.class.getSimpleName()+"_Boosted");
    private static final String IMG_PATH_F = ModHelper.makeIPath(Turbolens.class.getSimpleName()+"_Arctic");
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public boolean isBoosted=false,isFrozen=false;
    public static boolean isUsingXXFAST=false;

    public static final int NUM1=1,NUM2=6,NUM3=10,TIER=2;

    public static final IntSliderSetting DISINFECT=new IntSliderSetting("Turbolens_Disinfect", "N1", NUM1, 3);
    public static final IntSliderSetting LOSS_MAX=new IntSliderSetting("Turbolens_Loss_Max", "N2", NUM2, 15);
    public static final IntSliderSetting GAIN_LINE=new IntSliderSetting("Turbolens_Gain_Line", "N3", NUM3, 15);
    public static final IntSliderSetting MH=new IntSliderSetting("Turbolens_MH_v0.16.3","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Turbolens_G_v0.16.3","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Turbolens_R_v0.16.3","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(DISINFECT);
        settings.add(LOSS_MAX);
        settings.add(GAIN_LINE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Turbolens() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atPreBattle(){
        counter=0;
        secondCounter=0;

        if(isBoosted){
            disinfectAll();
        }
    }

    @Override
    public void atBattleStartPreDraw(){
        if(isBoosted){
            disinfectAll();
        }
    }

    @Override
    public void atBattleStart(){
        if(isBoosted){
            disinfectAll();
        }
    }

    @Override
    public void atTurnStart(){
        if(isBoosted){
            disinfectAll();
        }
    }

    @Override
    public void atTurnStartPostDraw(){
        if(isBoosted){
            disinfectAll();
        }
    }

    @Override
    public void onTrigger(){
        if(isBoosted){
            disinfectAll();
        } else {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));

            ArrayList<AbstractCard> statusList=AbstractDungeon.player.discardPile.group.stream().filter(c->c.type==AbstractCard.CardType.STATUS||c.type==AbstractCard.CardType.CURSE).collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(statusList,new Random(AbstractDungeon.treasureRng.randomLong()));
            //AbstractGameAction exhAction;
            for(int i=0;i<DISINFECT.value&&i<statusList.size();i++){
                isUsingXXFAST=true;
                addToBot(new ExhaustSpecificCardAction(statusList.get(i),AbstractDungeon.player.discardPile));
                isUsingXXFAST=false;
                //PatchTurbolens.PatchTool1.isQuickCall.set(exhAction,true);
                PatchTurbolens.PatchTool1.isQuickCall.set(statusList.get(i),true);
            }
        }
    }

    @Override
    public void onVictory(){
        counter=-1;
        secondCounter=-1;
        reOld();
    }

    @Override
    public String getUpdatedDescription() {
        if(DESCRIPTIONS[7].equals("1")){
            return String.format(this.DESCRIPTIONS[0], DISINFECT.value, GAIN_LINE.value, LOSS_MAX.value);
        }
        return String.format(this.DESCRIPTIONS[0], DISINFECT.value, LOSS_MAX.value, GAIN_LINE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(isBoosted){
            str_out.add(DESCRIPTIONS[5]);
        } else if(isFrozen){
            str_out.add(String.format(DESCRIPTIONS[6], DISINFECT.value));
        } else if(DESCRIPTIONS[7].equals("1")){
            str_out.add(String.format(this.DESCRIPTIONS[0], DISINFECT.value, GAIN_LINE.value, LOSS_MAX.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[0], DISINFECT.value, LOSS_MAX.value, GAIN_LINE.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    public void boost(){
        flash();
        isBoosted=true;
        counter=-1;
        secondCounter=-1;
        img=ImageMaster.loadImage(IMG_PATH_S);
        updateDesc();
    }

    public void freeze(){
        isFrozen=true;
        counter=-1;
        secondCounter=-1;
        img=ImageMaster.loadImage(IMG_PATH_F);
        updateDesc();
    }

    public void reOld(){
        isBoosted=false;
        isFrozen=false;
        img=ImageMaster.loadImage(IMG_PATH);
        updateDesc();
    }

    private void disinfectAll(){
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));

        isUsingXXFAST=true;
        for(AbstractCard c:AbstractDungeon.player.drawPile.group){
            if(c.type==AbstractCard.CardType.STATUS||c.type==AbstractCard.CardType.CURSE){
                PatchTurbolens.PatchTool1.isQuickCall.set(c,true);
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.drawPile));
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.hand));
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.discardPile));
            }
        }
        for(AbstractCard c:AbstractDungeon.player.hand.group){
            if(c.type==AbstractCard.CardType.STATUS||c.type==AbstractCard.CardType.CURSE){
                PatchTurbolens.PatchTool1.isQuickCall.set(c,true);
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.drawPile));
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.hand));
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.discardPile));
            }
        }
        for(AbstractCard c:AbstractDungeon.player.discardPile.group){
            if(c.type==AbstractCard.CardType.STATUS||c.type==AbstractCard.CardType.CURSE){
                PatchTurbolens.PatchTool1.isQuickCall.set(c,true);
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.drawPile));
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.hand));
                addToBot(new ExhaustSpecificCardAction(c,AbstractDungeon.player.discardPile));
            }
        }
        isUsingXXFAST=false;
    }
}

/*
package strops.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Turbolens extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Turbolens.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Turbolens.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=0,NUM2=1,NUM3=1,TIER=3;

    public static final IntSliderSetting DIFFERENCE=new IntSliderSetting("Turbolens_Difference", "N1", NUM1, -3,3);
    public static final IntSliderSetting PLAYED=new IntSliderSetting("Turbolens_Played", "N2", NUM2, 1,3);
    public static final IntSliderSetting DRAW=new IntSliderSetting("Turbolens_Draw", "N3", NUM3, 1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("Turbolens_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Turbolens_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Turbolens_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(DIFFERENCE);
        settings.add(PLAYED);
        settings.add(DRAW);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Turbolens() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
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
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], PLAYED.value, DIFFERENCE.value, DRAW.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], PLAYED.value, DIFFERENCE.value, DRAW.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction){
        if(EnergyPanel.totalCount-AbstractDungeon.player.hand.size()>DIFFERENCE.value){
            counter++;
            if(counter==PLAYED.value){
                counter=0;
                flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

                addToBot(new DrawCardAction(AbstractDungeon.player, DRAW.value));
            }
        }
    }
}

 */
