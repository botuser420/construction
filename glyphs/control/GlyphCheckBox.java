package scripts.utilities.fonts.glyphs.control;

import scripts.utilities.fonts.glyphs.GlyphIcon;

import scripts.utilities.fonts.glyphs.control.skin.GlyphCheckBoxSkin;
import scripts.utilities.fonts.glyphs.fontawesome.FontAwesomeIcon;
import scripts.utilities.fonts.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;

/**
 * @author Jens Deters (jens.deters@codecentric.de)
 */
public class GlyphCheckBox extends CheckBox {

    private ObjectProperty<GlyphIcon> notSelectedIcon;
    private ObjectProperty<GlyphIcon> selectedIcon;

    public GlyphCheckBox() {
        super("");
    }

    public GlyphCheckBox(GlyphIcon notSelectedIcon, GlyphIcon selectedIcon, String text) {
        super(text);
        setNotSelectedIcon(notSelectedIcon);
        setSelectedIcon(selectedIcon);
    }

    @Override
    protected Skin createDefaultSkin() {
        return new GlyphCheckBoxSkin(this);
    }

    public ObjectProperty<GlyphIcon> notSelectedIconProperty() {
        if (notSelectedIcon == null) {
            notSelectedIcon = new SimpleObjectProperty<GlyphIcon>(new FontAwesomeIconView(FontAwesomeIcon.TOGGLE_OFF));
        }
        return notSelectedIcon;
    }

    public GlyphIcon getNotSelectedIcon() {
        return notSelectedIconProperty().get();
    }

    public void setNotSelectedIcon(GlyphIcon icon) {
        notSelectedIconProperty().set(icon);
    }

    public ObjectProperty<GlyphIcon> selectedIconProperty() {
        if (selectedIcon == null) {
            selectedIcon = new SimpleObjectProperty<GlyphIcon>(new FontAwesomeIconView(FontAwesomeIcon.TOGGLE_ON));
        }
        return selectedIcon;
    }

    public GlyphIcon getSelectedIcon() {
        return selectedIconProperty().get();
    }

    public void setSelectedIcon(GlyphIcon icon) {
        selectedIconProperty().set(icon);
    }
}
