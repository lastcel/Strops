package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.patch.PatchStrongestPotion;
import strops.relics.StropsAbstractRelic;
import strops.relics.Wedgue;

public class ChooseSkill extends CustomCard {
    public static final String ID = ModHelper.makePath(ChooseSkill.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/ChoiceRequired.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChooseSkill(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        ((Wedgue) PatchStrongestPotion.PatchTool1.whichCallThis.get(AbstractDungeon.cardRewardScreen)).condition = Wedgue.Condition.SKILL;
        ((Wedgue) PatchStrongestPotion.PatchTool1.whichCallThis.get(AbstractDungeon.cardRewardScreen)).setDescriptionAfterLoading();

        AbstractDungeon.getCurrRoom().phase= StropsAbstractRelic.savedRoomPhase;
    }

    public void upgrade() {}
}
