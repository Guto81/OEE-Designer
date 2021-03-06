/*
 * Copyright (c) 2017 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.point85.tilesfx.skins;

import org.point85.tilesfx.Tile;
import org.point85.tilesfx.chart.SunburstChart;
import org.point85.tilesfx.fonts.Fonts;
import org.point85.tilesfx.tools.Helper;

import javafx.scene.text.Text;


public class SunburstChartTileSkin extends TileSkin {
    private Text          titleText;
    private Text          text;
    private SunburstChart sunburstChart;


    // ******************** Constructors **************************************
    public SunburstChartTileSkin(final Tile TILE) {
        super(TILE);
    }


    // ******************** Initialization ************************************
    @Override protected void initGraphics() {
        super.initGraphics();

        sunburstChart = tile.getSunburstChart();

        titleText = new Text();
        titleText.setFill(tile.getTitleColor());
        Helper.enableNode(titleText, !tile.getTitle().isEmpty());

        text = new Text(tile.getText());
        text.setFill(tile.getTextColor());
        Helper.enableNode(text, tile.isTextVisible());

        getPane().getChildren().addAll(titleText, sunburstChart, text);
    }

    @Override protected void registerListeners() {
        super.registerListeners();
    }


    // ******************** Methods *******************************************
    @Override protected void handleEvents(final String EVENT_TYPE) {
        super.handleEvents(EVENT_TYPE);

        if ("VISIBILITY".equals(EVENT_TYPE)) {
            Helper.enableNode(titleText, !tile.getTitle().isEmpty());
            Helper.enableNode(text, tile.isTextVisible());
        } else if ("RECALC".equals(EVENT_TYPE)) {
            sunburstChart.redraw();
        }
    }

    @Override public void dispose() {
        sunburstChart.dispose();
        super.dispose();
    }


    // ******************** Resizing ******************************************
    @Override protected void resizeStaticText() {
        double maxWidth = width - size * 0.1;
        double fontSize = size * textSize.factor;

        titleText.setFont(Fonts.latoRegular(fontSize));
        if (titleText.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(titleText, maxWidth, fontSize); }
        switch(tile.getTitleAlignment()) {
            default    :
            case LEFT  : titleText.relocate(size * 0.05, size * 0.05); break;
            case CENTER: titleText.relocate((width - titleText.getLayoutBounds().getWidth()) * 0.5, size * 0.05); break;
            case RIGHT : titleText.relocate(width - (size * 0.05) - titleText.getLayoutBounds().getWidth(), size * 0.05); break;
        }

        text.setFont(Fonts.latoRegular(fontSize));
        if (text.getLayoutBounds().getWidth() > maxWidth) { Helper.adjustTextSize(text, maxWidth, fontSize); }
        switch(tile.getTextAlignment()) {
            default    :
            case LEFT  : text.setX(size * 0.05); break;
            case CENTER: text.setX((width - text.getLayoutBounds().getWidth()) * 0.5); break;
            case RIGHT : text.setX(width - (size * 0.05) - text.getLayoutBounds().getWidth()); break;
        }
        text.setY(height - size * 0.05);
    }

    @Override protected void resize() {
        super.resize();
        width  = tile.getWidth() - tile.getInsets().getLeft() - tile.getInsets().getRight();
        height = tile.getHeight() - tile.getInsets().getTop() - tile.getInsets().getBottom();
        size   = width < height ? width : height;

        double chartWidth   = contentBounds.getWidth();
        double chartHeight  = contentBounds.getHeight();
        double chartSize    = chartWidth < chartHeight ? chartWidth : chartHeight;

        if (width > 0 && height > 0) {
            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);

            sunburstChart.setPrefSize(chartSize, chartSize);
            sunburstChart.relocate((width - chartSize) * 0.5, contentBounds.getY() + (contentBounds.getHeight() - chartSize) * 0.5);


            resizeStaticText();
        }
    }

    @Override protected void redraw() {
        super.redraw();

        titleText.setText(tile.getTitle());
        text.setText(tile.getText());

        resizeStaticText();
        sunburstChart.redraw();

        titleText.setFill(tile.getTitleColor());
        text.setFill(tile.getTextColor());
    }
}
