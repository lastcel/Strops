package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.potions.PhantasmalShootingStar;
import strops.relics.Decanter;
import strops.relics.SunflowerInASummer;

public class ToFight extends CustomCard {
    public static final String ID = ModHelper.makePath(ToFight.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/ToFight.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ToFight(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        boolean canGet=true;
        if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
            } else {
                AbstractDungeon.player.getRelic(Sozu.ID).flash();
                canGet=false;
            }
        }
        if(canGet){
            for(int i=0;i< SunflowerInASummer.BOTTLE.value;i++){
                AbstractDungeon.player.obtainPotion(PotionHelper.getPotion(PhantasmalShootingStar.POTION_ID));
            }
        }

        if(AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.INCOMPLETE){
            AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new ToEscape();
    }
}
