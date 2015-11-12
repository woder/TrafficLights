package me.woder.trafficremote;

import java.awt.Image;

public class Car {
	public int x = 0;
	public int y = 0;
	public double vy = 0;
	public double vx = 0;
	public int direction = 0;
	public double acceleration = 0.025;
	public boolean yStopped = true;
	public boolean xStopped = true;
	public boolean isMoving = false;
	private TrafficContentPane trc;
	
	public Car(TrafficContentPane trc, int x, int y, int direction){
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.trc = trc;		
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public Image getImage(){
		switch(direction){
		case 0:
			return trc.carNorth;
		case 1:
			return trc.carSouth;
		case 2:
			return trc.carEast;
		case 3:
			return trc.carWest;
		}
		return null;
	}
	
	public void tick(){
	  if(isMoving){
		checkLine();
		if(-200 < x && x > 900){
		   this.trc.cars.remove(this); //enlevon nous de ceci car nous ne somme plus sur l'ecran
		}
		if(-200 < y && y > 600){
		   this.trc.cars.remove(this); 
		}
	  }
	}

	private void checkLine() {
		switch(direction){			
		    case 0: //north
		    	if((y+vy) < (303)){		    	   
				   if(yStopped && (y+vy) > 147){
					y=303;
				   }else{
				    y+=vy;
				    vy-=acceleration;
				   }
				}else{
				   y+=vy;
				   vy-=acceleration;
				}
		    break;
		    
		    case 1: //souths  //NOTE SUBSTRACT ONE CAR LENGTH WHEN GOING SOUTH
		    	if((y+vy) > (147-128)){		    	
			       if(yStopped && (y+vy) < 303){
			    	  y=(147-128);
			       }else{
			    	  y+=vy;
			    	  vy+=acceleration;
			       }
			     }else{
			         y+=vy;
			         vy+=acceleration;
			     }
			break;
			
		    case 2:
		    	if((x+vx) > (225-128)){		    	
				     if(xStopped && (x+vx < 420)){
				        x=(225-128);
				     }else{
				    	x+=vx;
				    	vx+=acceleration;
				     }
				 }else{
				     x+=vx;
				     vx+=acceleration;
				 }
		    break;
		    	
		    case 3:
		    	if((x+vx) < (420)){		    	
				    if(xStopped && (x+vx) > 225){
				       x=(420);
				    }else{
				       x+=vx;
				       vx-=acceleration;
				    }
				}else{
				    x+=vx;
				    vx-=acceleration;
				}
		    break;
			
		}
		
	}

}
