package strops.relics;

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
import strops.actions.LoseMaxHpAction;
import strops.cards.*;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;
import strops.patch.PatchSoulCannon;

import java.util.ArrayList;

import static strops.relics.SoulCannon.WARMUP;

public class SoulCannonThreeHuge extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(SoulCannonThreeHuge.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SoulCannonThreeHuge.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SoulCannon.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=3,NUM2=45,NUM3=54,NUM4=63;

    public static final IntSliderSetting COST=new IntSliderSetting("ThreeHuge_Cost", "N1", NUM1, 2,6);
    public static final IntSliderSetting ACT1=new IntSliderSetting("ThreeHuge_Act1", "N2", NUM2, 20,60);
    public static final IntSliderSetting ACT2=new IntSliderSetting("ThreeHuge_Act2", "N3", NUM3, 24,72);
    public static final IntSliderSetting ACT3=new IntSliderSetting("ThreeHuge_Act3", "N4", NUM4, 28,84);
    //public static final IntSliderSetting ACT4=new IntSliderSetting("ThreeTiny_Act4", "N5", NUM5, 18,90);


    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(ACT1);
        settings.add(ACT2);
        settings.add(ACT3);
        //settings.add(ACT4);
        return settings;
    }

    public SoulCannonThreeHuge() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));

        for (int i = 0; i < this.points.length; i++){
            this.points[i] = new Vector2();
        }
    }

    @Override
    public void onEquip(){
        counter=PatchSoulCannon.PatchTool1.notUsedCannonTurn.get();
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
    public void onTrigger() {
        if(!usedThisTurn){
            counter++;
            if(counter>=WARMUP.value){
                needWarmUp(String.format(DESCRIPTIONS[3],WARMUP.value,COST.value));
                pulse=false;
            }
        }
    }

    @Override
    public void onVictory() {
        PatchSoulCannon.PatchTool1.notUsedCannonTurn.set(counter);
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
            if(canSpawnCannonFour()){
                typeChoices.add(new Four());
            }

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

        grayscale=(counter>=WARMUP.value);

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
            //case 4:damage=ACT4.value;break;
            default:damage=1;break;
        }

        AbstractPlayer p=AbstractDungeon.player;
        addToTop(new LoseMaxHpAction(COST.value));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        if(grayscale) {
            decideFormat();
            rise(String.format(this.DESCRIPTIONS[0],WARMUP.value,COST.value,format1,ACT1.value,
                    format2,ACT2.value, format3,ACT3.value));
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY,
                    3.0F, DESCRIPTIONS[6], true));
            upThisTurn=true;
        } else {
            DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS);
            AbstractDungeon.actionManager.addToBottom(new DamageAction(enemy, info, AbstractGameAction.AttackEffect.FIRE));
            upThisTurn=false;
        }

        counter=0;
        usedThisTurn = true;
        pulse=false;
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

        if (usedThisTurn&&!upThisTurn) {
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY,
                    3.0F, DESCRIPTIONS[4], true));
            return false;
        }

        if(p.maxHealth<COST.value){
            AbstractDungeon.effectList.add(new ThoughtBubble(p.dialogX, p.dialogY,
                    3.0F, DESCRIPTIONS[5], true));
            return false;
        }

        if(AbstractDungeon.actNum==4){
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
            if(PatchSoulCannon.PatchTool1.notUsedCannonTurn.get()>=WARMUP.value){
                return String.format(DESCRIPTIONS[3],WARMUP.value,COST.value);
            }
            return String.format(this.DESCRIPTIONS[0], WARMUP.value, COST.value, format1, ACT1.value,
                    format2, ACT2.value, format3, ACT3.value);
        }
        return String.format(this.DESCRIPTIONS[0],WARMUP.value,COST.value,"#b",ACT1.value,"#b",
                ACT2.value, "#b",ACT3.value);
    }

    @Override
    public void onEnterRoom(AbstractRoom room){
        if(AbstractDungeon.floorNum==18||AbstractDungeon.floorNum==35||AbstractDungeon.floorNum==53){
            updateFormat();
        }
    }

    @Override
    public void updateFormat(){
        if(counter>=WARMUP.value){
            return;
        }
        decideFormat();
        this.description=String.format(this.DESCRIPTIONS[0],WARMUP.value,
                COST.value,format1,ACT1.value, format2,ACT2.value,
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
            if(PatchSoulCannon.PatchTool1.notUsedCannonTurn.get()>=WARMUP.value){
                str_out.add(String.format(DESCRIPTIONS[3],WARMUP.value,COST.value));
            } else {
                str_out.add(String.format(this.DESCRIPTIONS[0], WARMUP.value, COST.value,
                        format1, ACT1.value, format2, ACT2.value, format3, ACT3.value,
                        format4));
            }

        } else {
            str_out.add(String.format(this.DESCRIPTIONS[0],WARMUP.value,COST.value,"#b",
                    ACT1.value,"#b",ACT2.value, "#b",ACT3.value));
        }
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public void setCounter(int counter){
        this.counter=counter;
        if(this.counter>=WARMUP.value){
            needWarmUp(String.format(DESCRIPTIONS[3],WARMUP.value,COST.value));
        }
    }

    @Override
    public AbstractRelic makeCopy(){
        return cannonMakeCopy();
    }
}
