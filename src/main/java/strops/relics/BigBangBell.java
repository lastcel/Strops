package strops.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import strops.helpers.ModHelper;
import strops.patch.PatchBigBangBell;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;
import java.util.function.Predicate;

public class BigBangBell extends StropsAbstractRelic implements
        CustomBottleRelic, CustomSavable<ArrayList<Integer>>, ClickableRelic {
    public static final String ID = ModHelper.makePath(BigBangBell.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(BigBangBell.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(BigBangBell.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    private static final Texture hitAreaImg = ImageMaster.loadImage("StropsResources/img/misc/BBBHitArea.png");

    private boolean cardSelected = true;
    public static AbstractCard card = null;
    //public static boolean canShowHitArea = false;
    public static float hitTimer = 0.0f;
    public static float xSc = Settings.xScale;
    public static float ySc = Settings.yScale;
    public static int wid=Settings.WIDTH;
    public static int hei=Settings.HEIGHT;
    public static float width=hitAreaImg.getWidth()*xSc;
    public static float height=hitAreaImg.getHeight()*ySc;
    public static float centerX = (wid-width)/2;
    public static float centerY = (hei-height*1.2f);

    public static boolean hasDrawnBellFirstTurn=false;

    public static final int NUM1=10,NUM2=16,NUM3=20,TIER=2;

    public static final IntSliderSetting PASSIVE=new IntSliderSetting("BBB_Passive", "N1", NUM1, 15);
    public static final IntSliderSetting EMBOMB=new IntSliderSetting("BBB_Embomb","N2",NUM2,13,33);
    public static final IntSliderSetting MH=new IntSliderSetting("BBB_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("BBB_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("BBB_R","R", TIER,0,5);
    public static final IntSliderSetting START_TIME=new IntSliderSetting("BBB_Start_Time", "10xT", NUM3, 5,50);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PASSIVE);
        settings.add(EMBOMB);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        settings.add(START_TIME);
        return settings;
    }

    public BigBangBell() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
        canSpawnInBattle=false;
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchBigBangBell.PatchTool1.inBigBangBell::get;
    }

    /*
    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }

     */

    @Override
    public ArrayList<Integer> onSave() {
        ArrayList<Integer> cardsIndex=new ArrayList<>();
        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            if(PatchBigBangBell.PatchTool1.inBigBangBell.get(c)){
                cardsIndex.add(AbstractDungeon.player.masterDeck.group.indexOf(c));
            }
        }
        return cardsIndex;
    }

    /*
    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                PatchBigBangBell.PatchTool1.inBigBangBell.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }

     */

    @Override
    public void onLoad(ArrayList<Integer> cardsIndex) {
        for(int i:cardsIndex){
            if(i>=0&&i<AbstractDungeon.player.masterDeck.group.size()){
                card=AbstractDungeon.player.masterDeck.group.get(i);
                if(card!=null){
                    PatchBigBangBell.PatchTool1.inBigBangBell.set(card,true);
                    setDescriptionAfterLoading();
                }
            }
        }
    }

    @Override
    public void onEquip(){
        //logger.info("判定区范围：centerX="+centerX+"width="+width+"centerY="+centerY+"height="+height);
        onEquipMods(MH,G);
        cardSelected = false;
        CardGroup cardGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        cardGroup.group.removeIf(c -> c.type != AbstractCard.CardType.POWER);
        /*
        for(AbstractCard c:cardGroup.group){
            if(c.type != AbstractCard.CardType.POWER){
                cardGroup.group.remove(c);
            }
        }
         */
        if (cardGroup.isEmpty()) {
            cardSelected = true;
        } else {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(cardGroup, 1, DESCRIPTIONS[5] + name + LocalizedStrings.PERIOD, false, false, false, false);
        }
    }

    /*
    @Override
    public void onUnequip() { // 1. On unequip
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                PatchBigBangBell.PatchTool1.inBigBangBell.set(cardInDeck, false);
            }
        }
    }

     */

    @Override
    public void onUnequip() { // 1. On unequip
        for(AbstractCard c:AbstractDungeon.player.masterDeck.group){
            PatchBigBangBell.PatchTool1.inBigBangBell.set(c,false);
        }
    }

    @Override
    public void onRightClick() {
        if(StaticHelpers.canClickRelic(this)){
            grayscale=!grayscale;
        }
    }

    @Override
    public void atBattleStartPreDraw(){
        hasDrawnBellFirstTurn=false;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(PatchBigBangBell.PatchTool1.inBigBangBell.get(drawnCard)){
            flash();
            hasDrawnBellFirstTurn=true;

            for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
                if(!m.isDeadOrEscaped()&&m.getIntentBaseDmg()>=0&&PASSIVE.value>0){
                    addToBot(new DamageAction(m,new DamageInfo(AbstractDungeon.player,PASSIVE.value, DamageInfo.DamageType.THORNS),AbstractGameAction.AttackEffect.FIRE));
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            PatchBigBangBell.PatchTool1.inBigBangBell.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }

    @Override
    public void onVictory(){
        grayscale=false;
    }

    @Override
    public void renderAndCheck(SpriteBatch sb){
        if(grayscale){
            return;
        }
        
        if(AbstractDungeon.player.isDraggingCard
                &&PatchBigBangBell.PatchTool1.inBigBangBell.get(AbstractDungeon.player.hoveredCard)) {
            renderHitArea(sb);
            renderProgressBar(sb);
            //logger.info("鼠标位置：mX="+InputHelper.mX+"，mY="+InputHelper.mY);
            if ((InputHelper.mX >= centerX) &&
                    (InputHelper.mX <= centerX + width) &&
                    (InputHelper.mY >= centerY) &&
                    (InputHelper.mY <= centerY + height)) {
                hitTimer += Gdx.graphics.getDeltaTime();
                if (hitTimer >= START_TIME.value/10.0f) {
                    AbstractPlayer p=AbstractDungeon.player;
                    AbstractCard c = p.hoveredCard;
                    p.releaseCard();
                    resetCardBeforeMoving(c);

                    flash();
                    addToBot(new VFXAction(p, new MindblastEffect(p.dialogX, p.dialogY, p.flipHorizontal), 0.1F));
                    addToBot(new DamageAllEnemiesAction(AbstractDungeon.player,

                            DamageInfo.createDamageMatrix(EMBOMB.value, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
                }
            } else {
                hitTimer = 0.0f;
            }
        } else {
            hitTimer = 0.0f;
        }
    }

    /*
    @Override
    public void renderInTopPanel(SpriteBatch sb){
        super.renderInTopPanel(sb);
    }

     */

    public static void renderHitArea(SpriteBatch sb){
        Color col=Color.valueOf("#aa8447");
        col.a=0.5f;
        sb.setColor(col);
        sb.draw(hitAreaImg, centerX, centerY, width, height,  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    public static void renderProgressBar(SpriteBatch sb){
        /*
        int r=myInterpolate(60,255);
        int g=255;
        int b=myInterpolate(255,60);

        Color col= Color.WHITE;
        col.r=r;
        col.g=g;
        col.b=b;
        col.a=0.5f;
        sb.setColor(col);

         */
        int r,g=255,b;
        if(hitTimer<=START_TIME.value/10.0f/2){
            r=myInterpolate(40,90);
            b=myInterpolate(255,205);
        } else {
            r=myInterpolate2(90,255);
            b=myInterpolate2(205,60);
        }
        Color col=Color.valueOf("#"+Integer.toHexString(r)+Integer.toHexString(g)+Integer.toHexString(b));
        col.a=0.5f;
        sb.setColor(col);
        sb.draw(hitAreaImg, centerX-width/5*xSc*2, centerY, width/5*xSc, height*hitTimer/(START_TIME.value/10.0f),  0, 0, hitAreaImg.getWidth(), hitAreaImg.getHeight(), false, false);
    }

    public static int myInterpolate(float startValue, float endValue){
        return MathUtils.round(startValue+(endValue-startValue)*hitTimer/(START_TIME.value/10.0f/2));
    }

    public static int myInterpolate2(float startValue, float endValue){
        return MathUtils.round(startValue+(endValue-startValue)*(hitTimer/(START_TIME.value/10.0f/2)-1));
    }

    @Override
    public boolean canSpawn() {
        CardGroup cardGroup = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        for (AbstractCard c : cardGroup.group) {
            if (c.type == AbstractCard.CardType.POWER){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], PASSIVE.value, EMBOMB.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], PASSIVE.value, EMBOMB.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    public void setDescriptionAfterLoading() {
        description = String.format(DESCRIPTIONS[6] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[7],PASSIVE.value,EMBOMB.value);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        initializeTips();
    }

    private void resetCardBeforeMoving(AbstractCard c) {
        AbstractDungeon.actionManager.removeFromQueue(c);
        c.unhover();
        c.untip();
        c.stopGlowing();
        AbstractDungeon.player.hand.group.remove(c);
    }
}
