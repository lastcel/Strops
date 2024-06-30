package strops.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.relics.SoulCannonOneTiny;

public class OneTiny extends AbstractStropsCard {
    public static final String ID = ModHelper.makePath(OneTiny.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/OneTiny.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public OneTiny(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if (CANNON_RELICS.contains(AbstractDungeon.player.relics.get(i).relicId)){
                AbstractRelic r= new SoulCannonOneTiny();
                r.instantObtain(AbstractDungeon.player, i, true);
                break;
            }
        }
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new OneTiny();
    }
}
