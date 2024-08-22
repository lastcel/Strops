//部分patch写在了玻璃棒的patch里面
package strops.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import strops.helpers.ModHelper;
import strops.modcore.Strops;
import strops.utilities.SoulCraftInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

public class SoulCraftedCard extends CustomCard implements CustomSavable<SoulCraftInfo> {
    public static final String ID = ModHelper.makePath(SoulCraftedCard.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/SoulCraftedCard.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public AbstractPotion sourcePotion=null;
    //public int usable;

    public SoulCraftedCard(AbstractPotion p, int usableTimes) {
        super(ID, NAME, IMG_PATH, 0, "", TYPE, COLOR, RARITY, TARGET);
        sourcePotion=p;
        cardID=ID+"_"+sourcePotion.ID;
        name=NAME+sourcePotion.name;
        magicNumber=baseMagicNumber=/*usable=*/usableTimes;
        rawDescription=String.format(DESCRIPTION,p.name);
        initializeDescription();
        //setImg();
        exhaust=true;
        if(sourcePotion.targetRequired){
            target=CardTarget.ENEMY;
        } else {
            target=CardTarget.SELF;
        }
    }

    public SoulCraftedCard(){
        super(ID, NAME, IMG_PATH, 0, "", TYPE, COLOR, RARITY, TARGET);
        //sourcePotion=new LiquidMemories();
        //cardID=ID+"_"+sourcePotion.ID;
        name=NAME+UPGRADE_DESCRIPTION;
        magicNumber=baseMagicNumber=/*usable=*/0;
        rawDescription=String.format(DESCRIPTION,UPGRADE_DESCRIPTION);
        initializeDescription();
        //setImg();
        exhaust=true;
    }

    @Override
    public void upgrade() {}

    @Override
    public boolean canUpgrade(){
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(sourcePotion==null){
            return;
        }

        sourcePotion.use(m);

        //usable--;
        //magicNumber=baseMagicNumber=usable;
        if(baseMagicNumber>0){
            upgradeMagicNumber(-1);
            rawDescription=String.format(DESCRIPTION,sourcePotion.name);
            initializeDescription();
        }

        Iterator<AbstractCard> iterator=AbstractDungeon.player.masterDeck.group.iterator();
        while (iterator.hasNext()){
            AbstractCard c=iterator.next();
            if(c.uuid.equals(uuid)){
                //((SoulCraftedCard)c).usable--;
                //c.magicNumber=c.baseMagicNumber=usable;
                try {
                    Method meth = AbstractCard.class.getDeclaredMethod("upgradeMagicNumber", int.class);
                    meth.setAccessible(true);
                    meth.invoke(c,-1);
                } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                    Strops.logger.info("An exception happened while Soul Craft's reflect on AbstractCard.#abstractCard#.upgradeMagicNumber()!");
                }
                if(/*((SoulCraftedCard)c).usable==0*/c.magicNumber<=0){
                    iterator.remove();
                } else {
                    c.rawDescription=String.format(DESCRIPTION,sourcePotion.name);
                    c.initializeDescription();
                }
            }
        }
    }

    @Override
    public void triggerWhenDrawn(){
        addToTop(new DrawCardAction(AbstractDungeon.player,1));
    }

    @Override
    public AbstractCard makeCopy(){
        if(sourcePotion!=null){
            return new SoulCraftedCard(sourcePotion,/*usable*/magicNumber);
        }
        return new SoulCraftedCard();
    }

    @Override
    public SoulCraftInfo onSave(){
        if(sourcePotion==null){
            return new SoulCraftInfo("",0);
        }
        return new SoulCraftInfo(sourcePotion.ID,/*usable*/magicNumber);
    }

    @Override
    public void onLoad(SoulCraftInfo savedSoulCraftInfo){
        if(savedSoulCraftInfo.sourcePotion.equals("")){
            return;
        }

        sourcePotion=PotionHelper.getPotion(savedSoulCraftInfo.sourcePotion);
        cardID=ID+"_"+sourcePotion.ID;
        name=NAME+sourcePotion.name;
        magicNumber=baseMagicNumber=/*usable=*/savedSoulCraftInfo.usable;
        rawDescription=String.format(DESCRIPTION,sourcePotion.name);
        initializeDescription();
        //setImg();
        //exhaust=true;
        if(sourcePotion.targetRequired){
            target=CardTarget.ENEMY;
        } else {
            target=CardTarget.SELF;
        }
    }

    @Override
    public void render(SpriteBatch sb){
        super.render(sb);
        if(sourcePotion!=null){
            sourcePotion.posX=current_x;
            sourcePotion.posY=current_y+hb.height*0.2f;
            sourcePotion.scale=Settings.scale*3*drawScale;
            sourcePotion.render(sb);
        }
    }

    @Override
    public void renderCardTip(SpriteBatch sb){
        super.renderCardTip(sb);
        if(sourcePotion==null){
            return;
        }

        if (Settings.hideCards) {
            return;
        }

        if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard && !Settings.isTouchScreen) {
            return;
        }

        try {
            Field f = AbstractCard.class.getDeclaredField("hovered");
            f.setAccessible(true);
            if(!(Boolean)f.get(this)){
                return;
            }
        } catch (IllegalAccessException|NoSuchFieldException e) {
            Strops.logger.info("An exception happened while Soul Craft's reflect on AbstractCard#abstractCard#.hovered!");
        }

        try {
            Method m = TipHelper.class.getDeclaredMethod("renderPowerTips", float.class, float.class, SpriteBatch.class, ArrayList.class);
            m.setAccessible(true);
            //ArrayList<PowerTip> sourcePotionTips=new ArrayList<>();
            //sourcePotionTips.add(new PowerTip(sourcePotion.name,sourcePotion.description));
            m.invoke(this,current_x-AbstractCard.IMG_WIDTH/2.0f-320.0f*Settings.scale,
                    current_y+AbstractCard.IMG_HEIGHT/2.0f,sb,sourcePotion.tips);
        } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
            Strops.logger.info("An exception happened while Soul Craft's reflect on AbstractCard.renderPowerTips()!");
        }
    }

    /*
    public void setImg(){
        try {
            Field f = AbstractPotion.class.getDeclaredField("liquidImg");
            f.setAccessible(true);
            if(f.get(sourcePotion)!=null){
                portraitImg=(Texture)f.get(sourcePotion);
            }
        } catch (IllegalAccessException|NoSuchFieldException e) {
            Strops.logger.info("An exception happened while Soul Craft's reflect on AbstractPotion#abstractPotion#.liquidImg!");
        }
    }

     */
}
