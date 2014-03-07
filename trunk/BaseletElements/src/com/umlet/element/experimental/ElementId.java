package com.umlet.element.experimental;

import com.umlet.element.experimental.element.Text;
import com.umlet.element.experimental.element.plot.PlotGrid;
import com.umlet.element.experimental.element.uml.Action;
import com.umlet.element.experimental.element.uml.ActivityObject;
import com.umlet.element.experimental.element.uml.Actor;
import com.umlet.element.experimental.element.uml.Class;
import com.umlet.element.experimental.element.uml.Interface;
import com.umlet.element.experimental.element.uml.Note;
import com.umlet.element.experimental.element.uml.Package;
import com.umlet.element.experimental.element.uml.State;
import com.umlet.element.experimental.element.uml.SyncBarHorizontal;
import com.umlet.element.experimental.element.uml.SyncBarVertical;
import com.umlet.element.experimental.element.uml.Timer;
import com.umlet.element.experimental.element.uml.UseCase;
import com.umlet.element.experimental.element.uml.relation.Relation;

/**
 * these IDs should NEVER be changed, because they are stored in uxf files
 */
public enum ElementId {
	UMLClass, UMLUseCase, UMLInterface, UMLActor, UMLAction, UMLObject, UMLTimer, UMLState, UMLNote, UMLSyncBarHorizontal, UMLSyncBarVertical, UMLPackage, Relation, Text, 
	PlotGrid /*standalone only (at the moment), therefore instantiated in ElementFactory and not here*/;
	
	public NewGridElement createAssociatedGridElement() {
		final NewGridElement returnObj;
		if (this == UMLClass) returnObj = new Class();
		else if (this == UMLUseCase) returnObj = new UseCase();
		else if (this == UMLInterface) returnObj = new Interface();
		else if (this == UMLActor) returnObj = new Actor();
		else if (this == UMLAction) returnObj = new Action();
		else if (this == UMLObject) returnObj = new ActivityObject();
		else if (this == UMLTimer) returnObj = new Timer();
		else if (this == UMLState) returnObj = new State();
		else if (this == UMLNote) returnObj = new Note();
		else if (this == UMLSyncBarHorizontal) returnObj = new SyncBarHorizontal();
		else if (this == UMLSyncBarVertical) returnObj = new SyncBarVertical();
		else if (this == UMLPackage) returnObj = new Package();
		else if (this == Relation) returnObj = new Relation();
		else if (this == Text) returnObj = new Text();
		else if (this == PlotGrid) returnObj = new PlotGrid();
		else throw new RuntimeException("Unknown class id: " + this);
		return returnObj;
	}
}