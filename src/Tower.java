import java.awt.*;
import java.util.Stack;


public class Tower {

  public Stack<Disk> diskStack = new Stack<Disk>();
  private int number;
  private double x,y;


  public Tower(int number, double x, double y){
    this.diskStack = new Stack<Disk>();
    this.number = number;
    this.x = x;
    this.y = y;

  }

  public int getNumber(){
    return this.number;
  }

  public String toString() {

    String temp = "";

    for (int i = 0; i < diskStack.size(); i++) {
      temp += String.valueOf(diskStack.get(i).getSize());
      temp += ",";
    }
    return temp;
  }

  public void push(Disk disk_test){
    this.diskStack.push(disk_test);
  }

  public Disk pop(){
    return this.diskStack.pop();
  }

  public Disk peek(){
    return this.diskStack.peek();
  }

  public void draw(double x_width, double y_length,double x_scale, double y_scale){
    Color rect_color = new Color(39, 48, 66);
    StdDraw.setPenColor(rect_color);
    StdDraw.filledRectangle(x,y,x_width,y_length);

    this.draw_discs(x_scale,y_scale);

    StdDraw.setPenColor(StdDraw.BLACK);
    Font text = new Font("Arial",Font.BOLD,24);
    StdDraw.setFont(text);
    StdDraw.text(x,y-y_length-1,"Tower "+ this.getNumber());

  }


  public void draw_discs(double x_scale, double y_scale){
    double point_y=2;
    for(int i=0; i< diskStack.size(); i++){
      point_y+= (Math.sqrt(diskStack.get(i).getSize())*y_scale);
      diskStack.get(i).draw(x,point_y, x_scale,y_scale);
    }
  }
}

