package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.relics.StrongestPotion;

public class EatMango extends CustomCard {
    public static final String ID = ModHelper.makePath(EatMango.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/EatMango.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public EatMango(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=StrongestPotion.BONUS.value;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        AbstractDungeon.player.increaseMaxHp(StrongestPotion.BONUS.value,true);
        if(AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.INCOMPLETE){
            AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new EatMango();
    }
}
