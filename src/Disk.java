import java.awt.*;

public class Disk {

  private int size = 0;
  Color diskcolor = new Color(145, 151, 174);


  public Disk (int size) {
    this.size = size;
  }
  public int getSize() {
    return size;
  }

  public void setColor(Color change){
    diskcolor = change;
  }

  public void draw(double x, double y,double x_scale, double y_scale){
    StdDraw.setPenColor(diskcolor);
    double halfWidth = (Math.sqrt(size)*x_scale)/2;
    double halfHeight = (Math.sqrt(size)*y_scale)/2;
    StdDraw.filledRectangle(x,y-halfHeight,halfWidth, halfHeight);
    StdDraw.setPenColor(StdDraw.WHITE);
    Font text = new Font("Arial",0,20);
    StdDraw.setFont(text);
    StdDraw.text(x,y-halfHeight,String.valueOf(this.getSize()));
    this.setColor(new Color(145, 151, 174));
  }
}
