package strops.textkeynumbers;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import strops.interfaces.IKeyNumber1;

public class TextKeyNumber1 extends DynamicVariable {
    @Override
    public String key()
    {
        return "Strops:KN1";
    }

    @Override
    public boolean isModified(AbstractCard card)
    {
        if (card instanceof IKeyNumber1) {
            return ((IKeyNumber1)card).isKeyNumber1Modified();
        }
        return false;
    }

    @Override
    public int value(AbstractCard card)
    {
        if (card instanceof IKeyNumber1) {
            return ((IKeyNumber1) card).keyNumber1();
        }
        return -1;
    }

    @Override
    public int baseValue(AbstractCard card)
    {
        if (card instanceof IKeyNumber1) {
            return ((IKeyNumber1) card).baseKeyNumber1();
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card)
    {
        if (card instanceof IKeyNumber1) {
            return ((IKeyNumber1) card).upgradedKeyNumber1();
        }
        return false;
    }
}
