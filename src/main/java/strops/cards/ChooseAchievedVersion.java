package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.relics.ProfiteeringMerchant;
import strops.relics.ProfiteeringMerchantAchieved;

public class ChooseAchievedVersion extends CustomCard {
    public static final String ID = ModHelper.makePath(ChooseAchievedVersion.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/ChooseAttemptedVersion.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChooseAchievedVersion(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if ((AbstractDungeon.player.relics.get(i)).relicId.equals(
                    ModHelper.makePath(ProfiteeringMerchant.class.getSimpleName()))) {
                AbstractRelic r= new ProfiteeringMerchantAchieved();
                r.instantObtain(AbstractDungeon.player, i, false);
                break;
            }
        }
        if(AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.INCOMPLETE){
            AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new ChooseAchievedVersion();
    }
}
