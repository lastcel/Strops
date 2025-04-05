package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.patch.PatchSunflowerInASummer;
import strops.relics.StropsAbstractRelic;
import strops.relics.SunflowerInASummer;

public class ToEscape extends CustomCard {
    public static final String ID = ModHelper.makePath(ToEscape.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/ToEscape.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ToEscape(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        PatchSunflowerInASummer.PatchTool2.everMetSunflower.set(true);
        AbstractDungeon.player.getRelic(SunflowerInASummer.ID).beginLongPulse();

        AbstractDungeon.getCurrRoom().phase= StropsAbstractRelic.savedRoomPhase;
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new ToEscape();
    }
}
