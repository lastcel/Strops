package strops.utilities;

import com.badlogic.gdx.InputProcessor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class MyTypeHelper implements InputProcessor {
    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        String charStr = String.valueOf(character);
        if (charStr.length() != 1)
            return false;
        if (FontHelper.getSmartWidth(FontHelper.cardTitleFont, NeuronSightPanel.textField, 1.0E7F, 0.0F, 0.82F) >= 240.0F * Settings.scale)
            return false;
        if (Character.isDigit(character))
            NeuronSightPanel.textField += charStr;
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}

