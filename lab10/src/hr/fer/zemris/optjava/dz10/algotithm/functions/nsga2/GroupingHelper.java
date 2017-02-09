package hr.fer.zemris.optjava.dz10.algotithm.functions.nsga2;

public class GroupingHelper implements Comparable<GroupingHelper>{
	public Double value;
	public int index;
	
	public GroupingHelper(int index, double value){
		this.value = value;
		this.index = index;
	}

	@Override
	public int compareTo(GroupingHelper o) {
		return -1 * this.value.compareTo(o.value);
	}
}
