//部分patch写在了玻璃棒的patch里面
package strops.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SeaOfMoon extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(SeaOfMoon.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SeaOfMoon.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(FTLEngines.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    private boolean cardSelected = true;

    public static final int NUM1=2;
    public static final int NUM2=2;
    public static final int NUM3=2;

    public static final IntSliderSetting RECEPTOR=new IntSliderSetting("SeaOfMoon_Receptor", "N1", NUM1, 1,5);
    public static final IntSliderSetting DAMAGE=new IntSliderSetting("SeaOfMoon_Damage", "N2", NUM2, 5);
    public static final IntSliderSetting BLOCK=new IntSliderSetting("SeaOfMoon_Block", "N3", NUM3, 5);
    public static final IntSliderSetting MH=new IntSliderSetting("SeaOfMoon_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SeaOfMoon_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(RECEPTOR);
        settings.add(DAMAGE);
        settings.add(BLOCK);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public SeaOfMoon() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup group = AbstractDungeon.player.masterDeck;
        if (group.isEmpty()) {
            cardSelected = true;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        } else if (group.size()<=RECEPTOR.value) {
            cardSelected = true;
            for(AbstractCard c:group.group){
                c.selfRetain=true;
                boolean hasRetainAlready=false;
                //logger.info("拾起，卡牌生描述："+c.rawDescription.toLowerCase());
                for(String s:GameDictionary.RETAIN.NAMES){
                    //logger.info("拾起，保留关键词："+s);
                    if(c.rawDescription.toLowerCase().startsWith(s)||c.rawDescription.toLowerCase().startsWith(" "+s)){
                        hasRetainAlready=true;
                        break;
                    }
                }
                if(!hasRetainAlready){
                    c.rawDescription=DESCRIPTIONS[5]+c.rawDescription;
                    c.initializeDescription();
                }
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        } else {
            AbstractDungeon.gridSelectScreen.open(group, RECEPTOR.value, String.format(DESCRIPTIONS[6],RECEPTOR.value), false, false,false,false);
        }
    }

    @Override
    public void update() {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size()==RECEPTOR.value) {
            cardSelected = true;
            for(AbstractCard c:AbstractDungeon.gridSelectScreen.selectedCards){
                c.selfRetain=true;
                boolean hasRetainAlready=false;
                //logger.info("拾起，卡牌生描述："+c.rawDescription.toLowerCase());
                for(String s:GameDictionary.RETAIN.NAMES){
                    //logger.info("拾起，保留关键词："+s);
                    if(c.rawDescription.toLowerCase().startsWith(s)||c.rawDescription.toLowerCase().startsWith(" "+s)){
                        hasRetainAlready=true;
                        break;
                    }
                }
                if(!hasRetainAlready){
                    c.rawDescription=DESCRIPTIONS[5]+c.rawDescription;
                    c.initializeDescription();
                }
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public void onTrigger(){
        flash();
        if(BLOCK.value>0){
            addToTop(new GainBlockAction(AbstractDungeon.player,AbstractDungeon.player,BLOCK.value));
        }
        if(DAMAGE.value>0){
            addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(DAMAGE.value, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }
    
    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], RECEPTOR.value, DAMAGE.value, BLOCK.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], RECEPTOR.value, DAMAGE.value, BLOCK.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
