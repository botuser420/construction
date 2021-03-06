/**
 * Copyright (c) 2013-2016 Jens Deters http://www.jensd.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */
package scripts.utilities.fonts.glyphs;

import scripts.utilities.fonts.glyphs.fontawesome.FontAwesomeIconView;
import scripts.utilities.fonts.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import scripts.utilities.fonts.glyphs.materialicons.MaterialIconView;
import scripts.utilities.fonts.glyphs.octicons.OctIconView;
import scripts.utilities.fonts.glyphs.weathericons.WeatherIconView;

/**
 *
 * @author Jens Deters
 */
public class GlyphsDude {

    static {
        try {
            Font.loadFont(GlyphsDude.class.getResource(FontAwesomeIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(GlyphsDude.class.getResource(WeatherIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(GlyphsDude.class.getResource(MaterialDesignIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(GlyphsDude.class.getResource(MaterialIconView.TTF_PATH).openStream(), 10.0);
            Font.loadFont(GlyphsDude.class.getResource(OctIconView.TTF_PATH).openStream(), 10.0);
        } catch (IOException ex) {
            Logger.getLogger(MaterialDesignIconView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Text createIcon(GlyphIcons icon) {
        return GlyphsDude.createIcon(icon, GlyphIcon.DEFAULT_FONT_SIZE);
    }

    public static Text createIcon(GlyphIcons icon, String iconSize) {
        Text text = new Text(icon.characterToString());
        text.getStyleClass().add("glyph-icon");
        text.setStyle(String.format("-fx-font-family: %s; -fx-font-size: %s;", icon.getFontFamily(), iconSize));
        return text;
    }

    public static Label createIconLabel(GlyphIcons icon, String text, String iconSize, String fontSize, ContentDisplay contentDisplay) {
        Text iconLabel = GlyphsDude.createIcon(icon, iconSize);
        Label label = new Label(text);
        label.setStyle("-fx-font-size: " + fontSize);
        label.setGraphic(iconLabel);
        label.setContentDisplay(contentDisplay);
        return label;
    }

    public static Button createIconButton(GlyphIcons icon) {
        return createIconButton(icon, "");
    }

    public static Button createIconButton(GlyphIcons icon, String text) {
        Text label = GlyphsDude.createIcon(icon, GlyphIcon.DEFAULT_FONT_SIZE);
        Button button = new Button(text);
        button.setGraphic(label);
        return button;
    }

    public static Button createIconButton(GlyphIcons icon, String text, String iconSize, String fontSize, ContentDisplay contentDisplay) {
        Text label = GlyphsDude.createIcon(icon, iconSize);
        Button button = new Button(text);
        button.setStyle("-fx-font-size: " + fontSize);
        button.setGraphic(label);
        button.setContentDisplay(contentDisplay);
        return button;
    }

    public static ToggleButton createIconToggleButton(GlyphIcons icon, String text, String iconSize, ContentDisplay contentDisplay) {
        return createIconToggleButton(icon, text, iconSize, GlyphIcon.DEFAULT_FONT_SIZE, contentDisplay);
    }

    public static ToggleButton createIconToggleButton(GlyphIcons icon, String text, String iconSize, String fontSize, ContentDisplay contentDisplay) {
        Text label = GlyphsDude.createIcon(icon, iconSize);
        ToggleButton button = new ToggleButton(text);
        button.setStyle("-fx-font-size: " + fontSize);
        button.setGraphic(label);
        button.setContentDisplay(contentDisplay);
        return button;
    }

    public static void setIcon(Tab tab, GlyphIcons icon) {
        setIcon(tab, icon, GlyphIcon.DEFAULT_FONT_SIZE);
    }

    public static void setIcon(Tab tab, GlyphIcons icon, String iconSize) {
        tab.setGraphic(GlyphsDude.createIcon(icon, iconSize));
    }

    public static void setIcon(Labeled labeled, GlyphIcons icon) {
        setIcon(labeled, icon, GlyphIcon.DEFAULT_FONT_SIZE);
    }

    public static void setIcon(Labeled labeled, GlyphIcons icon, ContentDisplay contentDisplay) {
        setIcon(labeled, icon, GlyphIcon.DEFAULT_FONT_SIZE, contentDisplay);
    }

    public static void setIcon(Labeled labeled, GlyphIcons icon, String iconSize) {
        setIcon(labeled, icon, iconSize, ContentDisplay.LEFT);
    }

    public static void setIcon(Labeled labeled, GlyphIcons icon, String iconSize, ContentDisplay contentDisplay) {
        if (labeled == null) {
            throw new IllegalArgumentException("The component must not be 'null'!");
        }
        labeled.setGraphic(GlyphsDude.createIcon(icon, iconSize));
        labeled.setContentDisplay(contentDisplay);
    }

    public static void setIcon(MenuItem menuItem, GlyphIcons icon) {
        setIcon(menuItem, icon, GlyphIcon.DEFAULT_FONT_SIZE, GlyphIcon.DEFAULT_FONT_SIZE);
    }

    public static void setIcon(MenuItem menuItem, GlyphIcons icon, String iconSize) {
        setIcon(menuItem, icon, GlyphIcon.DEFAULT_FONT_SIZE, iconSize);
    }

    public static void setIcon(MenuItem menuItem, GlyphIcons icon, String fontSize, String iconSize) {
        if (menuItem == null) {
            throw new IllegalArgumentException("The menu item must not be 'null'!");
        }
        Text label = GlyphsDude.createIcon(icon, iconSize);
        menuItem.setStyle("-fx-font-size: " + fontSize);
        menuItem.setGraphic(label);
    }

    public static void setIcon(TreeItem treeItem, GlyphIcons icon) {
        setIcon(treeItem, icon, GlyphIcon.DEFAULT_FONT_SIZE);
    }

    public static void setIcon(TreeItem treeItem, GlyphIcons icon, String iconSize) {
        if (treeItem == null) {
            throw new IllegalArgumentException("The tree item must not be 'null'!");
        }
        Text label = GlyphsDude.createIcon(icon, iconSize);
        treeItem.setGraphic(label);
    }

}
