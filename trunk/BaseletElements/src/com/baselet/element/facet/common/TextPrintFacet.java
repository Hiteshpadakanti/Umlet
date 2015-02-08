package com.baselet.element.facet.common;

import java.util.Collections;
import java.util.List;

import com.baselet.control.basics.XValues;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.ElementStyle;
import com.baselet.control.enums.Priority;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.TextSplitter;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.gui.AutocompletionText;

public class TextPrintFacet extends Facet {

	public static final TextPrintFacet INSTANCE = new TextPrintFacet();

	private TextPrintFacet() {}

	@Override
	public boolean checkStart(String line, PropertiesParserState state) {
		return !line.startsWith("//"); // comments start with // and are not printed
	}

	@Override
	public void handleLine(String line, DrawHandler drawer, PropertiesParserState state) {
		setupAtFirstLine(line, drawer, state);

		XValues xLimitsForText = state.getXLimitsForArea(state.getyPos(), drawer.textHeightMax(), false);
		Double spaceNotUsedForText = state.getGridElementSize().width - xLimitsForText.getSpace();
		if (!spaceNotUsedForText.equals(Double.NaN)) { // NaN is possible if xlimits calculation contains e.g. a division by zero
			state.updateCalculatedElementWidth(spaceNotUsedForText + drawer.textWidth(line));
		}
		AlignHorizontal hAlign = state.gethAlign();
		drawer.print(line, calcHorizontalTextBoundaries(xLimitsForText, drawer.getDistanceBorderToText(), hAlign), state.getyPos(), hAlign);
		state.addToYPos(drawer.textHeightMaxWithSpace());
	}

	/**
	 * before the first line is printed, some space-setup is necessary to make sure the text position is correct
	 */
	private static void setupAtFirstLine(String line, DrawHandler drawer, PropertiesParserState state) {
		boolean isFirstPrintedLine = state.getFacetResponse(TextPrintFacet.class, true);
		if (isFirstPrintedLine) {
			calcTopDisplacementToFitLine(line, state, drawer);
			state.setFacetResponse(TextPrintFacet.class, false);
		}
	}

	/**
	 * Calculates the necessary y-pos space to make the first line fit the xLimits of the element
	 * Currently only used by UseCase element to make sure the first line is moved down as long as it doesn't fit into the available space
	 */
	private static void calcTopDisplacementToFitLine(String firstLine, PropertiesParserState state, DrawHandler drawer) {
		boolean wordwrap = state.getElementStyle() == ElementStyle.WORDWRAP;
		if (!wordwrap) { // in case of wordwrap or no text, there is no top displacement
			int BUFFER = 2; // a small buffer between text and outer border
			double textHeight = drawer.textHeightMax();
			double addedSpacePerIteration = textHeight / 2;
			double yPosToCheck = state.getyPos();
			double availableWidthSpace = state.getXLimitsForArea(yPosToCheck, textHeight, true).getSpace() - BUFFER;
			int maxLoops = 1000;
			while (yPosToCheck < state.getGridElementSize().height && !TextSplitter.checkifStringFits(firstLine, availableWidthSpace, drawer)) {
				if (maxLoops-- < 0) {
					throw new RuntimeException("Endless loop during calculation of top displacement");
				}
				yPosToCheck += addedSpacePerIteration;
				double previousWidthSpace = availableWidthSpace;
				availableWidthSpace = state.getXLimitsForArea(yPosToCheck, textHeight, true).getSpace() - BUFFER;
				// only set displacement if the last iteration resulted in a space gain (eg: for UseCase until the middle, for Class: stays on top because on a rectangle there is never a width-space gain)
				if (availableWidthSpace > previousWidthSpace) {
					state.addToYPos(addedSpacePerIteration);
				}
			}
		}
	}

	private static double calcHorizontalTextBoundaries(XValues xLimitsForText, double distanceBorderToText, AlignHorizontal hAlign) {
		double x;
		if (hAlign == AlignHorizontal.LEFT) {
			x = xLimitsForText.getLeft() + distanceBorderToText;
		}
		else if (hAlign == AlignHorizontal.CENTER) {
			x = xLimitsForText.getSpace() / 2.0 + xLimitsForText.getLeft();
		}
		else /* if (state.gethAlign() == AlignHorizontal.RIGHT) */{
			x = xLimitsForText.getRight() - distanceBorderToText;
		}
		return x;
	}

	@Override
	public List<AutocompletionText> getAutocompletionStrings() {
		return Collections.emptyList(); // no autocompletion text for this facet
	}

	@Override
	public Priority getPriority() {
		return Priority.LOWEST;
	}

}
