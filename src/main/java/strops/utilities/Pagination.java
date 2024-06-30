package strops.utilities;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

import static basemod.BaseMod.logger;

public class Pagination implements IUIElement {
    private ImageButton next;

    private ImageButton prior;

    private int page;

    private int elementsPerPage;

    private List<RelicSettingsButton> elements;

    private int width;

    private int height;

    private int rows;

    private int columns;

    public Pagination(ImageButton next, ImageButton prior, int rows, int columns, int width, int height, List<RelicSettingsButton> elements) {
        this.page = 0;
        next.click = (b -> this.page++);
        prior.click = (b -> this.page--);
        this.next = next;
        this.prior = prior;
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.elements = new ArrayList<>();
        this.elementsPerPage = rows * columns;
        for (int i = 0; i < elements.size(); i++) {
            RelicSettingsButton element = elements.get(i);
            if (element != null) {
                RelicSettingsButton newElement;
                if (element.relic != null) {
                    newElement = new RelicSettingsButton(element.relic, element.x + (width * (i % columns)), element.y - (height * (i % this.elementsPerPage - i % columns) / columns), width, height, element.elements);
                } else {
                    newElement = new RelicSettingsButton(element.image, element.outline, element.x + (width * (i % columns)), element.y - (height * (i % this.elementsPerPage - i % columns) / columns), width, height, element.elements);
                }
                newElement.settings = element.settings;
                this.elements.add(newElement);
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
        if (this.page != 0)
            this.prior.render(spriteBatch);
        if ((this.page + 1) * this.elementsPerPage < this.elements.size())
            this.next.render(spriteBatch);
        for (RelicSettingsButton element : this.elements.subList(this.page * this.elementsPerPage, Math.min((this.page + 1) * this.elementsPerPage, this.elements.size())))
            element.render(spriteBatch);
    }

    public void update() {
        if (this.page != 0)
            this.prior.update();
        if ((this.page + 1) * this.elementsPerPage < this.elements.size())
            this.next.update();
        for (RelicSettingsButton element : this.elements.subList(this.page * this.elementsPerPage, Math.min((this.page + 1) * this.elementsPerPage, this.elements.size())))
            element.update();
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

    public void addElement(RelicSettingsButton element) {
        RelicSettingsButton newElement;
        int i = this.elements.size();
        if (element.relic != null) {
            newElement = new RelicSettingsButton(element.relic, element.x + (this.width * i % this.columns), element.y - (this.height * (i % this.elementsPerPage - i % this.columns) / this.columns), this.width, this.height, element.elements);
        } else {
            newElement = new RelicSettingsButton(element.image, element.outline, element.x + (this.width * i % this.columns), element.y - (this.height * (i % this.elementsPerPage - i % this.columns) / this.columns), this.width, this.height, element.elements);
        }
        newElement.settings = element.settings;
        this.elements.add(newElement);
    }
}

