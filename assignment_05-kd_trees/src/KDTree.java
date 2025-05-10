import java.util.Vector;

public class KDTree {
	int depth;
	double[] point;
	KDTree left;
	KDTree right;

	KDTree(double[] point, int depth) {
		this.point = point;
		this.depth = depth;
	}

	boolean compare(double[] a) {
		int dim = this.depth % this.point.length;

		return a[dim] >= this.point[dim];
	}

	static KDTree insert(KDTree tree, double[] p) {
		if (tree == null){
			return new KDTree(p, 0);
		}

		if(tree.compare(p)){
			tree.right = insert(tree.right, p);
			if(tree.right != null){
				tree.right.depth = tree.depth + 1;
			}
		}else{
			tree.left = insert(tree.left, p);
			if(tree.left != null){
				tree.left.depth = tree.depth + 1;
			}
		}

		return tree;
	}										

	static double sqDist(double[] a, double[] b) {
		if(a.length != b.length){
			throw new IllegalArgumentException("Points must have the same dimension");
		}

		double sum = 0;
		for(int i=0; i < a.length; i++){
			double diff = a[i] - b[i];
			sum += diff*diff;
		}

		return sum;
	}

	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		if(tree == null){
			return champion;
		}

		if(champion == null || sqDist(tree.point, a) < sqDist(champion, a)){
			champion = tree.point;
		}

		champion = closestNaive(tree.left, a, champion);
		champion = closestNaive(tree.right, a, champion);

		return champion;
	}


	static double[] closestNaive(KDTree tree, double[] a) {
		return closestNaive(tree, a, null);
	}

	static double[] closest(KDTree tree, double[] a, double[] champion) {
		if (tree == null)
			return champion;

		// sert pour InteractiveClosest.
		InteractiveClosest.trace(tree.point, champion);
		
		// Compare current point with champion
		double distToChampion = champion == null ? Double.POSITIVE_INFINITY : sqDist(a, champion);
		double distToCurrentPoint = sqDist(a, tree.point);
		
		if (distToCurrentPoint < distToChampion) {
			champion = tree.point;
			distToChampion = distToCurrentPoint;
		}

		int dim = tree.depth % tree.point.length;
		KDTree nearSubtree, farSubtree;
		
		if (a[dim] < tree.point[dim]) {
			nearSubtree = tree.left;
			farSubtree = tree.right;
		} else {
			nearSubtree = tree.right;
			farSubtree = tree.left;
		}

		champion = closest(nearSubtree, a, champion);

		distToChampion = sqDist(a, champion);
		
		double distToPlane = a[dim] - tree.point[dim];
		distToPlane = distToPlane * distToPlane;
		
		if (distToPlane < distToChampion) {
			champion = closest(farSubtree, a, champion);
		}
		
		return champion;
	}

	static double[] closest(KDTree tree, double[] a) {
		if (tree == null) {
			return null;
		}
		return closest(tree, a, null);
	}

	static int size(KDTree tree) {
		if(tree == null){
			return 0;
		}
		return 1+size(tree.left)+size(tree.right);
	}

	static void sum(KDTree tree, double[] acc) {
		if(tree==null || acc==null){
			return;
		}

		for(int i=0; i<tree.point.length; i++){
			acc[i] += tree.point[i];
		}

		sum(tree.left, acc);
		sum(tree.right, acc);
	}

	static double[] average(KDTree tree) {
		if(tree==null){
			return null;
		}

		int treeSize = size(tree);
		if(treeSize == 0){
			return null;
		}

		double[] acc =new double[tree.point.length];

		sum(tree, acc);

		for(int i=0; i<acc.length; i++){
			acc[i] /= treeSize;
		}

		return acc;
	}


	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		if(tree==null || maxpoints<=0){
			return new Vector<double[]>();
		}

		Vector<double[]> result = new Vector<double[]>();
		Vector<KDTree> subtrees = new Vector<KDTree>();
		subtrees.add(tree);

		while(subtrees.size()<maxpoints && !allLeaves(subtrees)){
			int maxIndex = findLargestNonLeafIndex(subtrees);
			if(maxIndex == -1){
				break;
			}

			KDTree largest = subtrees.remove(maxIndex);

			if(largest.left != null){
				subtrees.add(largest.left);
			}
			if(largest.right != null){
				subtrees.add(largest.right);
			}

			while(subtrees.size() > maxpoints){
				int minIndex = findSmallestSubtreeIndex(subtrees); 
				subtrees.remove(minIndex);
			}
		}

		for(KDTree subtree: subtrees){
			double[] avg = average(subtree);
			if(avg != null){
				result.add(avg);
			}
		}

		return result;
	}

	private static boolean allLeaves(Vector<KDTree> trees){
		for(KDTree tree: trees){
			if(tree.left != null || tree.right != null){
				return false;
			}
		}

		return true;
	}

	private static int findLargestNonLeafIndex(Vector<KDTree> trees){
		int maxSize = -1;
		int maxIndex = -1;

		for(int i=0; i<trees.size(); i++){
			KDTree tree = trees.get(i);
			if(tree.left != null || tree.right != null){
				int s = size(tree);
				if(s > maxSize){
					maxSize = s;
					maxIndex = i;
				}
			}
		}

		return maxIndex;
	} 

	private static int findSmallestSubtreeIndex(Vector<KDTree> trees){
		int minSize = Integer.MAX_VALUE;
		int minIndex = 0;

		for(int i=0; i<trees.size(); i++){
			int s = size(trees.get(i));
			if(s<minSize){
				minSize = s;
				minIndex = i;
			}
		}

		return minIndex;
	} 

	public String pointToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (this.point.length > 0)
			sb.append(this.point[0]);
		for (int i = 1; i < this.point.length; i++)
			sb.append("," + this.point[i]);
		sb.append("]");
		return sb.toString();
	}

}
