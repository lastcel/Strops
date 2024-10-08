package strops.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import strops.actions.ReduceCardRewardSizeAction;
import strops.cards.*;
import strops.helpers.ModHelper;
import strops.patch.PatchSoulCannon;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class SoulCannonFour extends StropsAbstractRelic implements ClickableRelic, CustomSavable<Integer> {
    public static final String ID = ModHelper.makePath(SoulCannonFour.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SoulCannonFour.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SoulCannon.class.getSimpleName());
    private static final AbstractRelic.RelicTier RELIC_TIER = AbstractRelic.RelicTier.SPECIAL;
    private static final AbstractRelic.LandingSound LANDING_SOUND = AbstractRelic.LandingSound.HEAVY;

    public int numReduced=0;

    public static final int NUM1=40,NUM2=48,NUM3=56,NUM4=0,NUM5=5;

    public static final IntSliderSetting ACT1=new IntSliderSetting("Four_Act1", "N1", NUM1, 10,50);
    public static final IntSliderSetting ACT2=new IntSliderSetting("Four_Act2", "N2", NUM2, 12,60);
    public static final IntSliderSetting ACT3=new IntSliderSetting("Four_Act3", "N3", NUM3, 14,70);
    public static final IntSliderSetting LOWER=new IntSliderSetting("Four_Lower", "S1", NUM4, -1,10);
    public static final IntSliderSetting UPPER=new IntSliderSetting("Four_Upper", "S2", NUM5, -1,10);

    @Override
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(ACT1);
        settings.add(ACT2);
        settings.add(ACT3);
        settings.add(LOWER);
        settings.add(UPPER);
        return settings;
    }

    public SoulCannonFour() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));

        for (int i = 0; i < this.points.length; i++){
            this.points[i] = new Vector2();
        }
    }

    @Override
    public Integer onSave(){
        return numReduced;
    }

    @Override
    public void onLoad(Integer savedNumReduced){
        numReduced=savedNumReduced;
    }

    @Override
    public void atBattleStart(){
        numReduced=0;
    }

    @Override
    public void atTurnStart() {
        usedThisTurn=false;
        if(!grayscale){
            flash();
            pulse=true;
        }
    }

    @Override
    public void onVictory() {
        pulse=false;
    }

    @Override
    public void onRightClick() {
        if(AbstractDungeon.screen==AbstractDungeon.CurrentScreen.GRID||AbstractDungeon.screen==AbstractDungeon.CurrentScreen.CARD_REWARD){
            return;
        }

        AbstractRoom currRoom = AbstractDungeon.getCurrRoom();
        if ((currRoom != null) && (currRoom.phase != AbstractRoom.RoomPhase.COMBAT)) {
            InputHelper.moveCursorToNeutralPosition();
            ArrayList<AbstractCard> typeChoices = new ArrayList<>();
            typeChoices.add(new Zero());
            typeChoices.add(new OneTiny());
            typeChoices.add(new OneHuge());
            typeChoices.add(new TwoTiny());
            typeChoices.add(new TwoHuge());
            typeChoices.add(new ThreeTiny());
            typeChoices.add(new ThreeHuge());

            if(AbstractDungeon.isScreenUp){
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            AbstractDungeon.cardRewardScreen.chooseOneOpen(typeChoices);
            return;
        }

        if(!StaticHelpers.canClickRelic(this)){
            return;
        }

        if(!canUse()){
            return;
        }

        targetMode=true;
        GameCursor.hidden = true;
    }

    @Override
    public void update(){
        super.update();
        if (!isObtained) {
            return;
        }

        counter=(AbstractDungeon.getCurrRoom()!=null&&
                AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.COMBAT)?
                Math.max(getNumberOfCardsInReward(),0):-1;

        if(!targetMode){
            return;
        }
        updateTargetMode();
    }

    @Override
    public void cannonShoot(AbstractCreature enemy){
        int damage;
        switch (AbstractDungeon.actNum){
            case 1:damage=ACT1.value;break;
            case 2:damage=ACT2.value;break;
            case 3:damage=ACT3.value;break;
            default:damage=1;break;
        }

        addToTop(new ReduceCardRewardSizeAction(this));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS);
        addToBot(new DamageAction(enemy, info, AbstractGameAction.AttackEffect.FIRE));

        usedThisTurn=true;
        pulse=false;
        PatchSoulCannon.PatchTool1.notUsedCannonTurn.set(0);
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {

        super.renderInTopPanel(sb);
        if (this.targetMode) {
            if (this.hoveredMonster != null){
                this.hoveredMonster.renderReticle(sb);
            }

            renderTargetingUi(sb);
        }
    }

    private boolean canUse(){
        AbstractPlayer p=AbstractDungeon.player;

        if (usedThisTurn) {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY,
                    3.0F, DESCRIPTIONS[4], true));
            return false;
        }

        if(getNumberOfCardsInReward()<=0){
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY,
                    3.0F, DESCRIPTIONS[5], true));
            return false;
        }

        if(AbstractDungeon.floorNum>=50){
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY,
                    3.0F, DESCRIPTIONS[7], true));
            return false;
        }

        return true;
    }

    @Override
    public String getUpdatedDescription() {
        if(AbstractDungeon.player!=null){
            decideFormat();
            return String.format(this.DESCRIPTIONS[0], format1, ACT1.value,
                    format2, ACT2.value, format3, ACT3.value);
        }
        return String.format(this.DESCRIPTIONS[0],"#b",ACT1.value,"#b",
                ACT2.value, "#b",ACT3.value);
    }

    @Override
    public void onEnterRoom(AbstractRoom room){
        numReduced=0;

        if(AbstractDungeon.floorNum==18||AbstractDungeon.floorNum==35||AbstractDungeon.floorNum==53){
            updateFormat();
        }
    }

    @Override
    public void updateFormat(){
        decideFormat();
        this.description=String.format(this.DESCRIPTIONS[0],
                format1,ACT1.value,format2,ACT2.value,
                format3,ACT3.value);
        this.tips.clear();
        this.tips.add(new PowerTip(DESCRIPTIONS[1],DESCRIPTIONS[2]));
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(AbstractDungeon.player!=null) {
            decideFormat();
            str_out.add(String.format(this.DESCRIPTIONS[0],
                    format1, ACT1.value, format2, ACT2.value, format3, ACT3.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[0],"#b",
                    ACT1.value,"#b",ACT2.value,"#b",ACT3.value));
        }
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public AbstractRelic makeCopy(){
        return cannonMakeCopy();
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards-numReduced;
    }

    private static int getNumberOfCardsInReward(){
        int numCards = 3;
        for (AbstractRelic r : AbstractDungeon.player.relics){
            numCards = r.changeNumberOfCardsInReward(numCards);
        }
        if (com.megacrit.cardcrawl.helpers.ModHelper.isModEnabled("Binary")){
            numCards--;
        }
        return numCards;
    }
}
