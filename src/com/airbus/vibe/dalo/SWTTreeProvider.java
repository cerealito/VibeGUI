package com.airbus.vibe.dalo;

import java.util.ArrayList;

import mx.bitch.util.Tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class SWTTreeProvider implements ITreeContentProvider {

	public void dispose() {
		//do nothing
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		//do nothing special
	}

	/**
	 * This returns the objects to be used as roots of the three
	 * when inputElement is given as input.
	 */
	public Object[] getElements(Object inputElement) {
 
		if (inputElement == null) {
			Object[] o = {"null"};
			return o;
		}
		
		if (inputElement instanceof String) {
			Object[] o = {inputElement + "!"};
			return o;
		}
		
		if ( inputElement instanceof Tree<?> ) {
			Tree<NodeWrapper> tree = (Tree<NodeWrapper>)inputElement;	
			
			/*
				The returned array must not be nor *contain* the
				given inputElement,
				since this leads to recursion issues. 
			*/
			ArrayList<Tree<NodeWrapper>> return_ar = new ArrayList<Tree<NodeWrapper>>();
			for (int i = 0; i < tree.getSubTrees().size(); i++) {
				Tree<NodeWrapper> c_subtree = (Tree<NodeWrapper>) tree.getSubTrees().toArray()[i];
				if ( ! ((NodeWrapper)c_subtree.getElement()).isEnvironment() ) {
					return_ar.add(c_subtree);	
				}
			}
			
			return return_ar.toArray();
		}
		
		else {
			Object[] o = {"Unreadable object of class " + 
		                       inputElement.getClass().toString()};
			return o;
		}
		
	}
	
	/**
	 * This returns the interesting nodes when we expand the items in the
	 * tree
	 */
	public Object[] getChildren(Object parentElement) {
		
		Object[] r = null;
		
		if (parentElement instanceof Tree<?>) {
			
			r = ((Tree<NodeWrapper>)parentElement).getSubTrees().toArray();
			//System.out.println("Tree has " + r.length + " child(ren)");
		}
		else {
			System.err.println("unknown object of class " + parentElement.getClass());
		}
		
		return r;
	}

	public Object getParent(Object element) {
		Tree<NodeWrapper> parentTree = null;
		
		if (element instanceof Tree<?>) {
			if (element != null)
				parentTree = ((Tree<NodeWrapper>)element).getParent();
			//System.out.println("Parent is " + parentTree.getElement());
		}

		return parentTree;
	}

	public boolean hasChildren(Object element) {
		boolean ret = false;
		if (element instanceof Tree<?>) {
			ret = ((Tree<NodeWrapper>)element).hasLeaves();
		}
		//System.out.println("\tleaves: " + ret + "\n");
		return ret;
	}
	
}