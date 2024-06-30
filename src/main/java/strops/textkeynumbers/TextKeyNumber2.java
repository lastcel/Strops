package strops.textkeynumbers;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import strops.interfaces.IKeyNumber2;

public class TextKeyNumber2 extends DynamicVariable {
    @Override
    public String key()
    {
        return "Strops:KN2";
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        if (card instanceof IKeyNumber2) {
            return ((IKeyNumber2)card).isKeyNumber2Modified();
        }
        return false;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (card instanceof IKeyNumber2) {
            return ((IKeyNumber2) card).keyNumber2();
        }
        return -1;
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (card instanceof IKeyNumber2) {
            return ((IKeyNumber2) card).baseKeyNumber2();
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof IKeyNumber2) {
            return ((IKeyNumber2) card).upgradedKeyNumber2();
        }
        return false;
    }
}
