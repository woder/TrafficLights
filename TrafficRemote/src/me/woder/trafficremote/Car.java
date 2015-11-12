package me.woder.trafficremote;

public class Car {
	public int x = 0;
	public int y = 0;
	public double vy = 0;
	public double vx = 0;
	public int direction = 0;
	public boolean isMoving = false;
	
	public Car(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void tick(){
	  if(isMoving){
		if(direction == 0){ //si il va ver le nord
		   vy -= 1;
		   y += vy;
		}else if(direction == 1){ //si il va ver le sud
		   vy += 1;
		   y += vy;
		}else if(direction == 2){ //si il va ver l'est
		   vx -= 1;
		   x += vx;
		}else if(direction == 3){ //si il va ver l'ouest
		   vx += 1;
		   x += vx;
		}
	  }
	}

}
