/**
 *
 * @author Logan Willett the Spaghetti King
 */
public class Node {
	
	public int row, col, g, h, f;
	public Node parent = null;
	
	public Node(int row, int column) {
		this.row = row;
		this.col = column;
	}
        
	public void setG(int g) {
		this.g = g;
	}
	
        //Sam and I found online that using the pythagorean theorm is a more accurate way of calculating the distance
        public void setH(int goalRow, int goalColumn) {
		int a = Math.abs(goalRow - row) + Math.abs(goalColumn - col);
		int b = (int) Math.sqrt((goalRow - row) * (goalRow - row) + (goalColumn - col) * (goalColumn - col));
                h = a + b;
	}
        
	public int getF() {
		return h + g;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
        //Compares coordinates of node and goal to find if they match
	public boolean isGoal(int gRow, int gCol) {
		return ((row == gRow) && (col == gCol));
	}
}
