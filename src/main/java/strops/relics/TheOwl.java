package strops.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class TheOwl extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(TheOwl.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(TheOwl.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(TheOwl.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=4,NUM2=1,NUM3=12,TIER=4;

    public static final IntSliderSetting PLAYED=new IntSliderSetting("TheOwl_Played", "N1", NUM1, 2,10);
    public static final IntSliderSetting DRAW=new IntSliderSetting("TheOwl_Draw", "N2", NUM2, 1,4);
    public static final IntSliderSetting DIVIDER=new IntSliderSetting("TheOwl_Divider", "N3", NUM3, 5,20);
    public static final IntSliderSetting MH=new IntSliderSetting("TheOwl_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("TheOwl_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("TheOwl_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PLAYED);
        settings.add(DRAW);
        settings.add(DIVIDER);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public TheOwl() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atPreBattle(){
        secondCounter=0;
    }

    @Override
    public void update(){
        super.update();
        if(!isObtained){
            return;
        }

        counter=AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.COMBAT?getCount():-1;
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction){
        if(targetCard.type!=AbstractCard.CardType.ATTACK){
            secondCounter++;
        }

        while(secondCounter>=counter){
            secondCounter-=counter;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DrawCardAction(AbstractDungeon.player, DRAW.value));
        }
    }

    @Override
    public void onVictory(){
        secondCounter=-1;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0],PLAYED.value,DRAW.value,DIVIDER.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(DESCRIPTIONS[0],PLAYED.value,DRAW.value,DIVIDER.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    private static int getCount(){
        AbstractPlayer p=AbstractDungeon.player;
        return Math.max(PLAYED.value-(p.hand.size()+p.drawPile.size()+p.discardPile.size())/DIVIDER.value,1);
    }
}
