import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Logan Willett the Spaghetti Code King
 */
public class AStarSearch {
//Width and length of the grid
public static final int N = 15;
public static int[][] grid;

    public static void main(String[] args) {
        //Scanners for user input
        Scanner sc = new Scanner(System.in);
        int input = 0;
        //Generate and print the grid
        grid = generateGrid();
        printGrid();
        //Start and goal coordinates
        int sRow, sCol, gRow, gCol;
        //Repeat while loop for menu
        boolean repeat = true;
        //Menu
        while (repeat) {
            //In case of incorrect user input
            try{
                System.out.println("Would you like to run the program?");
                System.out.print("Enter 1 to run or any other number to end the program: ");
                input = sc.nextInt();
                //Break out and end menu if any other number
                if (input != 1) {
                    repeat = false;
                    break;
                }
                //Show user the grid
                printGrid();
                //Get coordinates for start from user
                System.out.print("Enter coordinate for starting row: ");
                sRow = sc.nextInt();
                System.out.print("Enter coordinate for starting column: ");
                sCol = sc.nextInt();
                //Get coordinates for goal from user
                System.out.print("Enter coordinate for ending row: ");
                gRow = sc.nextInt();
                System.out.print("Enter coordinate for starting column: ");
                gCol = sc.nextInt();
                if ((grid[sRow][sCol] == 1) || (grid[gRow][gCol] == 1))
                    System.out.println("You've chosen a blocked coordinate");
                else
                    search(sRow, sCol, gRow, gCol);
                //Resets the grid
                grid = generateGrid();
            }
            catch(Exception E){
                System.out.println("Incorrect input, please try again"); 
                sc.next();
            }
        }
    }
    
    //Generates the grid
    public static int[][] generateGrid(){
        Random r = new Random();
        int[][] temp = new int[N][N];
        //Traverses the grid
        for (int i = 0; i < N; i++)
            for (int k = 0; k < N; k++) {
                //Randomly places bloacks across 10% of the grid
                if ( r.nextInt(10) == 1)
                        temp[i][k] = 1;
                else
                    temp[i][k] = 0;
            }
        return temp;
    }
    
    //Prints the grid
    public static void printGrid(){
        System.out.println("Grid: ");
        System.out.print("   ");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(" " + i);
            if (i < 10)
                    System.out.print(" ");
        }
        System.out.println();
        System.out.println("    -------------------------------------------");
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i);
            if (i < 10)
                System.out.print(" |");
            else
                System.out.print("|");
            for (int k = 0; k < grid.length; k++) {
                
                if (grid[i][k] == 0)
                    System.out.print(" 0 "); //Open space
                else if (grid[i][k] == 1)
                    System.out.print(" X "); //Blocked space
                else if (grid[i][k] == 2)
                    System.out.print(" P "); //Path
                else if (grid[i][k] == 3)
                    System.out.print(" E "); //Explored space
            }
            System.out.println();
        }
    }

    //Runs the A* Search
    public static void search(int sRow, int sCol, int gRow, int gCol) {
        //If goal is found
        boolean goal = false;
        //Using current node as node that the search will focus on
        Node current = new Node(sRow, sCol);
        
        current.setG(0);
        //Store the nodes
        //My friend and I got too far into this before realizing we should have used an ArrayList... this was a nightmare
        Node[] openList = new Node[N * N];
        Node[] closedList = new Node[N * N];
        //Add current to the open list
        openList[0] = current;
        //Loops until goal found or no nodes left to explore
        while (!goal && (openList[0] != null)) {
            //Takes the lowest cost node
            current = openList[0];
            //Shifts the array contents down
            for (int i = 0; i < (N * N) - 1; i++)
                openList[i] = openList[i + 1];
            //Break out of loop if current is the goal
            if (current.isGoal(gRow, gCol)) {
                    goal = true;
                    break;
            }
            //Checks all adjacent nodes
            addNode(current, 1, 0, closedList, openList, gRow, gCol);
            addNode(current, -1, 0, closedList, openList, gRow, gCol);
            addNode(current, 0, 1, closedList, openList, gRow, gCol);
            addNode(current, 0, -1, closedList, openList, gRow, gCol);
            addNode(current, 1, -1, closedList, openList, gRow, gCol);
            addNode(current, -1, -1, closedList, openList, gRow, gCol);
            addNode(current, 1, 1, closedList, openList, gRow, gCol);
            addNode(current, -1, 1, closedList, openList, gRow, gCol);
            //Adds the current Node to the closed list
            addToList(current, closedList);
            printGrid();
        }
        //Runs when the goal node is reached
        if (goal) {
            boolean done = false;
            //Finds the path for the nodes
            while (!done) {
                //Sets the coordinate as the path
                grid[current.row][current.col] = 2;
                //Once it finds the parent is null it stops
                if (current.getParent() == null) {
                    done = true;
                }
                current = current.getParent();
            }
            System.out.println("The final path:");
            printGrid();
        }
        else
            System.out.println("There is no path to the node");
    }
    
    //Adds a new node to the open list if passes the nightmare of tests
    public static void addNode(Node current, int row, int col, Node[] closedList, Node[] openList, int gRow, int gCol) {
        //Used to tell if the node is in the array
        int inArray;
        //Check if it's in bounds
        if (!(((current.row + row) < 0) || ((current.col + col) < 0) || ((current.row + row) >= N) || ((current.col + col) >= N))) {
            //Sets up a new node based on new coordinates
            Node tempNode = createNode(new Node((current.row + row), (current.col + col)), current, gRow, gCol);
            if (tempNode != null) {
                //Check if in closed list
                if (checkList(tempNode, closedList) == -1) {
                    //Check if in open list
                    inArray = checkList(tempNode, openList);
                    if (inArray != -1){
                        //Replace if better distance found
                        if (openList[inArray].getF() > tempNode.getF()) {
                            openList[inArray] = null;
                            addToList(tempNode, openList);
                        }
                    }
                    //Add to open list
                    else
                        addToList(tempNode, openList);
                }
            }
        }
    }

    //This sets up the node with all necesary info
    public static Node createNode(Node newNode, Node parent, int gRow, int gCol) {
        //Checks if the space is blocked
        if (grid[newNode.row][newNode.col] == 1)
            return null;
        //Sets info for the node
        newNode.setH(gRow, gCol);
        newNode.setG(parent.g + 1);
        newNode.setParent(parent);
        //Sets the node as seen
        grid[newNode.row][newNode.col] = 3;
        //Returns the new node
        return newNode;
    }

    //Adds node to openlist or closedlist in terms of f value
    public static void addToList(Node current, Node[] list) {
        //Variables needed
        int count = 0;
        boolean repeat = true;
        Node temp;
        //Repeat until the node is added
        while (repeat) {
            //Check if last entry
            if (list[count] == null) {
                //Add the node
                list[count] = current;
                repeat = false;
            }
            else {
                //Check if f is better
                if (current.getF() < list[count].getF()) {
                    temp = list[count];
                    list[count] = current;
                    current = temp;
                }
            }
            count++;
        }
    }

    //Checks if the given node is in the array
    public static int checkList(Node current, Node[] list) {
        int counter = 0;
        while (list[counter] != null) {
            if ((current.row == list[counter].row) && (current.col == list[counter].col))
                return counter;
            counter++;
        }
        return -1;
    }
}