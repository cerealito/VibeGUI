package mx.bitch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * 
 * @param <T>
 *          Object's type in the tree.
 */
public class Tree<T> {

	private T element;

	private ArrayList<Tree<T>> leaves = new ArrayList<Tree<T>>();

	private Tree<T> parent = null;

	private HashMap<T, Tree<T>> locate = new HashMap<T, Tree<T>>();

	/**
	 * Creates the tree from a data object of type T 
	 * @param data
	 */
	public Tree(T data) {
		this.element = data;
		locate.put(data, this);
	}

	public void addLeaf(T root, T leaf) {
		if (locate.containsKey(root)) {
			locate.get(root).addLeaf(leaf);
		} else {
			addLeaf(root).addLeaf(leaf);
		}
	}

	public Tree<T> addLeaf(T leaf) {
		Tree<T> t = new Tree<T>(leaf);
		leaves.add(t);
		t.parent = this;
		t.locate = this.locate;
		locate.put(leaf, t);
		return t;
	}

	public Tree<T> setAsParent(T parentRoot) {
		Tree<T> t = new Tree<T>(parentRoot);
		t.leaves.add(this);
		this.parent = t;
		t.locate = this.locate;
		t.locate.put(element, this);
		t.locate.put(parentRoot, t);
		return t;
	}

	public T getElement() {
		return element;
	}

	public Tree<T> getTree(T element) {
		return locate.get(element);
	}

	public Tree<T> getParent() {
		return parent;
	}

	public Collection<T> getSuccessors(T root) {
		Collection<T> successors = new ArrayList<T>();
		Tree<T> tree = getTree(root);
		if (null != tree) {
			for (Tree<T> leaf : tree.leaves) {
				successors.add(leaf.element);
			}
		}
		return successors;
	}
	
	public boolean hasLeaves() {
		if ( leaves.isEmpty() )
			return false;
		else
			return true;
	}

	public Collection<Tree<T>> getSubTrees() {
		return leaves;
	}

	public static <T> Collection<T> getSuccessors(T of, Collection<Tree<T>> input) {
		for (Tree<T> tree : input) {
			if (tree.locate.containsKey(of)) {
				return tree.getSuccessors(of);
			}
		}
		return new ArrayList<T>();
	}

	@Override
	public String toString() {
		return printTree(0);
	}

	private static final int indent = 2;

	private String printTree(int increment) {
		String s = "";
		String inc = "";
		for (int i = 0; i < increment; ++i) {
			inc = inc + " ";
		}
		s = inc + element;
		for (Tree<T> child : leaves) {
			s += "\n" + child.printTree(increment + indent);
		}
		return s;
	}
}