import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

public class Main {
  private static final int CANVAS_WIDTH = 1280;
  private static final int CANVAS_HEIGHT = 720;

  public static void updateVisualization(AtomicInteger steps, Tower... towers) {
    Color bground = new Color(239, 246, 238);
    StdDraw.clear(bground);
    double x_scale = 5.0 / towers.length;
    double y_scale = 7.0 / (towers.length * 4);

    for (Tower tower : towers) {
      tower.draw(0.25, 3, x_scale, y_scale);
    }
    StdDraw.setPenColor(StdDraw.BLACK);
    Font text = new Font("Arial",Font.BOLD,32);
    StdDraw.setFont(text);
    StdDraw.text(28,15,("Steps: " +steps.get()));
    StdDraw.show();
    StdDraw.pause(1000);  // Adjust the pause time as needed
  }

  public static void swap(Tower from, Tower to, AtomicInteger numberOfSteps) {
    Disk hold = from.pop();
    hold.setColor(new Color(163, 76, 100));
    to.push(hold);
    numberOfSteps.set(numberOfSteps.get() + 1);
  }

  public static void solveHanoi(int numDiscs, Tower source, Tower auxiliary, Tower destination ,AtomicInteger numberOfSteps) {
    if (numDiscs == 1) {
      swap(source, destination, numberOfSteps);
      updateVisualization(numberOfSteps,source, auxiliary, destination);
    }
    else {
      solveHanoi(numDiscs - 1, source, destination, auxiliary , numberOfSteps);
      swap(source, destination  , numberOfSteps);
      updateVisualization(numberOfSteps,source,auxiliary,destination);
      solveHanoi(numDiscs - 1, auxiliary, source, destination,  numberOfSteps);
      }
  }

  public static void bitwise_iterative(int numDiscs, Tower t1, Tower t2, Tower t3, AtomicInteger steps){
    Tower[] towers = new Tower[3];
    int parity = numDiscs & 1;

    towers[0] = t1;
    towers[1] = (parity == 0) ? t3 : t2;
    towers[2] = (parity == 0) ? t2 : t3;

    int numofsteps = (1 << numDiscs) - 1; // Using bit manipulation for power of 2

    for (int i = 0; i < numofsteps; i++) {
      int from = (i & i + 1) % 3;
      int to = ((i | i + 1) + 1) % 3;

      swap(towers[from], towers[to], steps);
      updateVisualization(steps,t1, t2, t3);
    }
  }

  public static void treeIterative(int numDiscs, Tower t1, Tower t2, Tower t3, AtomicInteger steps) {
    HashMap<Character,Tower> towers = new HashMap<Character,Tower>();
    int numofsteps = ((int) Math.pow(2, numDiscs)) - 1;
    towers.put('0',t1);
    towers.put('1',t2);
    towers.put('2',t3);

    char[][] moves = new char[numofsteps][3];
    moves[numofsteps / 2][0] = '0';
    moves[numofsteps / 2][1] = '1';
    moves[numofsteps / 2][2] = '2';


    for (int level = numDiscs; level > 1; level--) {
      int first = (int) Math.pow(2, level - 1);
      int level_gap = (int) Math.pow(2, level);
      int under_gap = first / 2;

      for (int i = first; i < numofsteps; i += level_gap) {
        moves[i - under_gap - 1][0] = moves[i - 1][0];
        moves[i - under_gap - 1][1] = moves[i - 1][2];
        moves[i - under_gap - 1][2] = moves[i - 1][1];

        moves[i + under_gap - 1][0] = moves[i - 1][1];
        moves[i + under_gap - 1][1] = moves[i - 1][0];
        moves[i + under_gap - 1][2] = moves[i - 1][2];
      }
    }


    for (int i = 0; i < numofsteps; i++) {
      swap(towers.get(moves[i][0]), towers.get(moves[i][2]), steps);
    }
  }

  public static void stack_iterative(int numDiscs, Tower t1, Tower t2, Tower t3, AtomicInteger steps) {
    Tower[] towers = {t1, t2, t3};
    Stack<int[]> stack = new Stack<>();
    int totalMoves = (1 << numDiscs) - 1; // Total number of moves

    // Push initial move to the stack
    stack.push(new int[] { numDiscs, 0, 1, 2 });

    while (!stack.isEmpty()) {
      int[] move = stack.pop();
      int disc = move[0];
      int source = move[1];
      int auxiliary = move[2];
      int destination = move[3];

      if (disc == 1) {
        // Move the smallest disc directly
        swap(towers[source], towers[destination], steps);
      } else {
        // Push the next moves onto the stack
        stack.push(new int[] { disc - 1, auxiliary, source, destination });
        stack.push(new int[] { 1, source, auxiliary, destination });
        stack.push(new int[] { disc - 1, source, destination, auxiliary });
      }
    }


  }


  public static void main(String[] args) {
	  boolean way = false;
	  JFrame frame = new JFrame();

      SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 12, 1);
      JSpinner spinner = new JSpinner(model);

      Object[] options = {"Recursive", "Iterative"};
      int n = JOptionPane.showOptionDialog(frame,spinner, "Please Enter a Number", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

      int numberOfDisks = (Integer) spinner.getValue();

      if (n == JOptionPane.YES_OPTION) {
          way = true;
      } else if (n == JOptionPane.NO_OPTION) {
          way = false;
      }
      else {
    	System.exit(0);
      }

    Color bground = new Color(239, 246, 238);
    StdDraw.setCanvasSize(CANVAS_WIDTH,CANVAS_HEIGHT);
    StdDraw.clear(bground);
    StdDraw.setXscale(0,32);
    StdDraw.setYscale(0,18);
    StdDraw.enableDoubleBuffering();

    Tower tower1 = new Tower(1,8,5);
    Tower tower2 = new Tower(2,16,5);
    Tower tower3 = new Tower(3,24,5);
    AtomicInteger numberOfSteps = new AtomicInteger(0);

    for (int i = numberOfDisks;  i >= 1; i--) {
      tower1.push(new Disk(i));
    }
    updateVisualization(numberOfSteps,tower1, tower2, tower3);

    double start = System.currentTimeMillis();
    
    if(way) {
      solveHanoi(numberOfDisks, tower1, tower2, tower3, numberOfSteps);
    }
    else {
      bitwise_iterative(numberOfDisks, tower1, tower2, tower3, numberOfSteps);
    }
    double end = System.currentTimeMillis();

    System.out.println("Tower1:" +tower1);
    System.out.println("Tower2:" +tower2);
    System.out.println("Tower3:" +tower3);
    System.out.println("Number of steps: " + numberOfSteps.get());

    System.out.println("=====================================");

    System.out.println("Execution time in milliseconds: " + (end - start));

    StdDraw.show();
  }
}