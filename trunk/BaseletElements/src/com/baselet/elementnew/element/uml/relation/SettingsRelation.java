package com.baselet.elementnew.element.uml.relation;

import java.util.Arrays;
import java.util.List;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.AlignVertical;
import com.baselet.control.geom.XValues;
import com.baselet.elementnew.facet.ElementStyleEnum;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.relation.DescriptionPositionFacet;
import com.baselet.elementnew.facet.relation.LineDescriptionFacet;
import com.baselet.elementnew.facet.relation.RelationLineTypeFacet;
import com.baselet.elementnew.settings.SettingsAbstract;

public abstract class SettingsRelation extends SettingsAbstract {

	@Override
	public XValues getXValues(double y, int height, int width) {
		return new XValues(0, width);
	}

	@Override
	public AlignVertical getVAlign() {
		return AlignVertical.TOP;
	}

	@Override
	public AlignHorizontal getHAlign() {
		return AlignHorizontal.CENTER;
	}

	@Override
	public ElementStyleEnum getElementStyle() {
		return ElementStyleEnum.NORESIZE;
	}

	@Override
	public List<? extends Facet> createFacets() {
		return Arrays.asList(RelationLineTypeFacet.INSTANCE, LineDescriptionFacet.INSTANCE, DescriptionPositionFacet.INSTANCE_MESSAGE_START, DescriptionPositionFacet.INSTANCE_MESSAGE_END, DescriptionPositionFacet.INSTANCE_ROLE_START, DescriptionPositionFacet.INSTANCE_ROLE_END);
	}

	@Override
	protected List<? extends Facet> createDefaultFacets() {
		return SettingsAbstract.RELATION;
	}

	@Override
	public boolean printText() {
		return false;
	}

	public abstract RelationPointHandler getRelationPoints();

}
