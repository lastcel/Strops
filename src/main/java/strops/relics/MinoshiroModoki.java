package strops.relics;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cardmods.CardModDecreaseCostUntilPlayed;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.Iterator;

public class MinoshiroModoki extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(MinoshiroModoki.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(MinoshiroModoki.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(MinoshiroModoki.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1,TIER=5;

    public static final IntSliderSetting BONUS= new IntSliderSetting("MinoshiroModoki_Bonus", "N1", NUM1, 5);
    public static final IntSliderSetting MH=new IntSliderSetting("MinoshiroModoki_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("MinoshiroModoki_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("MinoshiroModoki_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public MinoshiroModoki() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        removeStrikeTip();
    }

    private void removeStrikeTip() {
        ArrayList<String> strikes = new ArrayList<>();
        for (String s : GameDictionary.STRIKE.NAMES)
            strikes.add(s.toLowerCase());
        for (Iterator<PowerTip> t = this.tips.iterator(); t.hasNext(); ) {
            PowerTip derp = t.next();
            String s = derp.header.toLowerCase();
            if (strikes.contains(s)) {
                t.remove();
                break;
            }
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onRightClick() {
        AbstractRoom currRoom= AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            return;
        }

        if(grayscale){
            counter=-1;
            grayscale=false;
        } else {
            counter=-2;
            grayscale=true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    public void onThisTriggered(CardGroup premier, CardGroup adder){
        flash();

        for(AbstractCard c:adder.group){
            AbstractCard cNew=c.makeStatEquivalentCopy();
            CardModifierManager.addModifier(cNew, new CardModDecreaseCostUntilPlayed(BONUS.value));
            premier.addToTop(cNew);
        }
    }

    @Override
    public void setCounter(int counter){
        this.counter=counter;
        grayscale= (this.counter != -1);
    }
}
