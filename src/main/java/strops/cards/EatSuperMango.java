package strops.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.patch.PatchStrongestPotion;
import strops.relics.StrongestPotion;

public class EatSuperMango extends AbstractStropsCard{
    public static final String ID = ModHelper.makePath(EatSuperMango.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/EatSuperMango.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public EatSuperMango(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=StrongestPotion.SUPER_BONUS.value;
        this.keyNumber1=this.baseKeyNumber1=StrongestPotion.DURATION.value;
        this.keyNumber2=this.baseKeyNumber2=StrongestPotion.INTENSE.value;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        AbstractDungeon.player.increaseMaxHp(StrongestPotion.SUPER_BONUS.value,true);
        PatchStrongestPotion.PatchTool1.whichCallThis.get(AbstractDungeon.cardRewardScreen).counter=StrongestPotion.DURATION.value;
        PatchStrongestPotion.PatchTool1.whichCallThis.get(AbstractDungeon.cardRewardScreen).beginLongPulse();
        if(AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.INCOMPLETE){
            AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new EatMango();
    }
}
