package strops.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.ShammyPeachButton;
import strops.utilities.StaticHelpers;
import strops.vfx.ShammyPeachEffect;

import java.util.ArrayList;

public class ShammyPeach extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(ShammyPeach.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ShammyPeach.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(ShammyPeach.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    ShammyPeachButton buttonDraw;
    ShammyPeachButton buttonDiscard;

    public static final int NUM1=4,NUM2=2,TIER=3;

    public static final IntSliderSetting USABLE=new IntSliderSetting("ShammyPeach_Usable", "N1", NUM1, 1,10);
    public static final IntSliderSetting BLOCK=new IntSliderSetting("ShammyPeach_Block", "N2", NUM2, 1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("ShammyPeach_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("ShammyPeach_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("ShammyPeach_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(USABLE);
        settings.add(BLOCK);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public ShammyPeach() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
        buttonDraw=new ShammyPeachButton(true);
        buttonDiscard=new ShammyPeachButton(false);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=USABLE.value;
        beginLongPulse();
    }

    public void onThisTriggered(boolean isToDrawPile){
        AbstractPlayer p=AbstractDungeon.player;
        ArrayList<AbstractCard> hand=p.hand.group;
        ArrayList<AbstractCard> draw=p.drawPile.group;
        ArrayList<AbstractCard> discard=p.discardPile.group;
        if(hand.isEmpty()&&draw.isEmpty()&&discard.isEmpty()){
            return;
        }

        AbstractDungeon.effectList.add(new ShammyPeachEffect(isToDrawPile));

        counter--;
        if (counter == 0) {
            counter = -2;
            pulse = false;
            usedUp();
        }
    }

    @Override
    public void update(){
        super.update();
        if(!isObtained){
            return;
        }
        buttonDraw.update();
        buttonDiscard.update();
    }

    @Override
    public void renderAndCheck(SpriteBatch sb){
        if(/*AbstractDungeon.getCurrRoom()==null||
                AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT||*/
                !StaticHelpers.canClickRelic(this)||counter<=0){
            buttonDraw.setClickable(false);
            buttonDiscard.setClickable(false);
            return;
        }

        buttonDraw.setClickable(true);
        buttonDiscard.setClickable(true);
        buttonDraw.render(sb);
        buttonDiscard.render(sb);
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        } else {
            beginLongPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0],USABLE.value,BLOCK.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(DESCRIPTIONS[0],USABLE.value,BLOCK.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
