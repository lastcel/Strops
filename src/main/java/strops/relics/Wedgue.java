package strops.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import strops.actions.*;
import strops.cards.*;
import strops.helpers.ModHelper;
import strops.patch.PatchStrongestPotion;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Wedgue extends StropsAbstractRelic implements CustomSavable<Wedgue.Condition> {
    public static final String ID = ModHelper.makePath(Wedgue.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Wedgue.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Wedgue.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public Condition condition;
    public AbstractCard drawnCard;
    public Category category=null;
    public boolean firstTurn=true;

    public enum Condition{
        ZERO_COST,ONE_COST,TWO_COST,THREE_PLUS_COST,ATTACK,SKILL,POWER,COMMON,UNCOMMON,RARE
    }

    public enum Category{
        COST,TYPE,RARITY
    }

    public static final int TIER=4;

    public static final IntSliderSetting MH=new IntSliderSetting("Wedgue_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Wedgue_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Wedgue_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Wedgue() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        cardToPreview=new AscendersBane();
        canSpawnInBattle=false;
    }

    @Override
    public Condition onSave(){
        return condition;
    }

    @Override
    public void onLoad(Condition savedCondition){
        condition=savedCondition;
        setDescriptionAfterLoading();
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new AscendersBane(),
                Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> wedgueChoices = new ArrayList<>();
        wedgueChoices.add(new ChooseCost());
        wedgueChoices.add(new ChooseType());
        wedgueChoices.add(new ChooseRarity());
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        PatchStrongestPotion.PatchTool1.whichCallThis.set(AbstractDungeon.cardRewardScreen,this);
        AbstractDungeon.cardRewardScreen.chooseOneOpen(wedgueChoices);
    }

    @Override
    public void update(){
        super.update();
        if(!isObtained){
            return;
        }

        if(AbstractDungeon.screen != AbstractDungeon.CurrentScreen.CARD_REWARD && category!=null){
            switch (category){
                case COST:
                    InputHelper.moveCursorToNeutralPosition();
                    ArrayList<AbstractCard> costChoices = new ArrayList<>();
                    costChoices.add(new Choose0Cost());
                    costChoices.add(new Choose1Cost());
                    costChoices.add(new Choose2Cost());
                    costChoices.add(new Choose3PlusCost());
                    if(AbstractDungeon.isScreenUp){
                        AbstractDungeon.dynamicBanner.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    AbstractDungeon.cardRewardScreen.chooseOneOpen(costChoices);
                    break;
                case TYPE:
                    InputHelper.moveCursorToNeutralPosition();
                    ArrayList<AbstractCard> typeChoices = new ArrayList<>();
                    typeChoices.add(new ChooseAttack());
                    typeChoices.add(new ChooseSkill());
                    typeChoices.add(new ChoosePower());
                    if(AbstractDungeon.isScreenUp){
                        AbstractDungeon.dynamicBanner.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    AbstractDungeon.cardRewardScreen.chooseOneOpen(typeChoices);
                    break;
                case RARITY:
                    InputHelper.moveCursorToNeutralPosition();
                    ArrayList<AbstractCard> rarityChoices = new ArrayList<>();
                    rarityChoices.add(new ChooseCommon());
                    rarityChoices.add(new ChooseUncommon());
                    rarityChoices.add(new ChooseRare());
                    if(AbstractDungeon.isScreenUp){
                        AbstractDungeon.dynamicBanner.hide();
                        AbstractDungeon.overlayMenu.cancelButton.hide();
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
                    AbstractDungeon.cardRewardScreen.chooseOneOpen(rarityChoices);
                    break;
                default:break;
            }
            category=null;
        }
    }

    @Override
    public void atBattleStartPreDraw(){
        firstTurn=true;

        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        drawnCard=null;

        if(condition==null){
            return;
        }

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (useCheck(c)){
                tmp.addToRandomSpot(c);
            }
        }

        if(!tmp.isEmpty()){
            addToBot(new GeneralDrawPileToHandAction(1, myNeeds()));
            addToBot(new WedgueRetrieveDRPTHAAction(this));
        } else {
            addToBot(new GeneralDiscardPileToHandAction(1, myNeeds()));
            addToBot(new WedgueRetrieveDSPTHAAction(this));
        }

        addToBot(new WedgueRetainAction(this));
    }

    @Override
    public void atTurnStart(){
        if(firstTurn){
            firstTurn=false;
            return;
        }

        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        drawnCard=null;

        if(condition==null){
            return;
        }

        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (useCheck(c)){
                tmp.addToRandomSpot(c);
            }
        }

        if(!tmp.isEmpty()){
            addToBot(new GeneralDrawPileToHandAction(1, myNeeds()));
            addToBot(new WedgueRetrieveDRPTHAAction(this));
        } else {
            addToBot(new GeneralDiscardPileToHandAction(1, myNeeds()));
            addToBot(new WedgueRetrieveDSPTHAAction(this));
        }

        addToBot(new WedgueRetainAction(this));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    public void setDescriptionAfterLoading(){
        if(condition!=null){
            description=String.format(DESCRIPTIONS[5],getConditionString(condition));
            tips.clear();
            tips.add(new PowerTip(name, description));
            showMHaG(MH,G);
            initializeTips();
        }
    }

    public Predicate<AbstractCard> myNeeds() {
        return this::useCheck;
    }

    public boolean useCheck(AbstractCard c){
        switch (condition){
            case ZERO_COST:return c.cost == 0||c.freeToPlayOnce;
            case ONE_COST:return c.cost == 1;
            case TWO_COST:return c.cost == 2;
            case THREE_PLUS_COST:return c.cost >= 3;
            case ATTACK:return c.type == AbstractCard.CardType.ATTACK;
            case SKILL:return c.type == AbstractCard.CardType.SKILL;
            case POWER:return c.type == AbstractCard.CardType.POWER;
            case COMMON:return c.rarity == AbstractCard.CardRarity.COMMON;
            case UNCOMMON:return c.rarity == AbstractCard.CardRarity.UNCOMMON;
            case RARE:return c.rarity == AbstractCard.CardRarity.RARE;
            default:return false;
        }
    }

    public String getConditionString(Condition condition){
        return DESCRIPTIONS[6+condition.ordinal()];
    }
}
