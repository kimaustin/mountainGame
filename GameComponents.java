// Assignment 9  -  part 2
// Kim Austin
// KimA
// Pine Michelle
// pinemichelle63

import java.util.*;
import java.awt.Color;
import javalib.worldimages.*;

//to represent a game piece
abstract class APiece { 
	WorldImage img;
	int x; 
	int y;
	
	APiece(WorldImage img, int x, int y) {
		this.img = img;
		this.x = x;
		this.y = y;
	}
	
	//displays the image
	WorldImage displayPiece() {
	   return this.img; 
	}
	
	//determines if two pieces collide
	boolean collision(APiece p) {
		return this.x == p.x
				&& this.y == p.y;
	}
	
	//finds a random cell that isn't an ocean cell
	Cell getNonOceanCell(ArrayList<ArrayList<Cell>> a) {
		int max = ForbiddenIslandWorld.ISLAND_SIZE;
		Cell c = a.get(new Random().nextInt(max - 1)).get(new Random().nextInt(max - 1));
		while (c.isFlooded) {
			c = a.get(new Random().nextInt(max - 1)).get(new Random().nextInt(max - 1));
		}
		return c;
	}

	
	
}

//to represent a Player 
class Player extends APiece {
	//represents whether the player has lost
	boolean isDead = false;
	boolean isEngineer = false;
	boolean won = false;
	
	Player(ArrayList<ArrayList<Cell>> arr){
		super(new ScaleImage(new FromFileImage("pilot-icon.png"), .8), 0, 0);
		Cell c = this.getNonOceanCell(arr);
		this.x = c.x;
		this.y = c.y;
	}

	//moves the player
	void movePlayer(String ke, IList<Cell> l) {
		IListIterator<Cell> it = new IListIterator<Cell>(l);
		Cell c2 = null;
	    while (it.hasNext()) {
	    	Cell c = it.next();
	        if (c.y == this.y && c.x == this.x) {
	        	c2 = c;
	        }
	    }
	    if (c2 != null){
			int max = ForbiddenIslandWorld.ISLAND_SIZE;
			if (ke.equals("right") && this.x + 1 <= max) {
				if (!c2.right.isOcean() && !c2.right.isFlooded) {
					this.x = this.x + 1;
				}
			} 
			else if (ke.equals("left") && this.x - 1 >= 0) {
				if (!c2.left.isOcean() && !c2.left.isFlooded) {
					this.x = this.x - 1;
				}
			} 
			else if (ke.equals("up") && this.y - 1 >= 0) {
				if (!c2.top.isOcean() && !c2.top.isFlooded) {
					this.y = this.y - 1;
				}
			} 
			else if (ke.equals("down") && this.y + 1 <= max) {
				if (!c2.bottom.isOcean() && !c2.bottom.isFlooded) {
					this.y = this.y + 1;
				}
			}
	    }
	}
	
}

//to represent a target
class Target extends APiece {
	
	Target(ArrayList<ArrayList<Cell>> a) {
		super(new EllipseImage(5, 5, OutlineMode.SOLID, Color.magenta), 0, 0);
		Cell c = this.getNonOceanCell(a);
		this.x = c.x;
		this.y = c.y;
	}

		
}

class EngineerPower extends APiece {
  
  EngineerPower(ArrayList<ArrayList<Cell>> a) {
    super(new ScaleImage(new FromFileImage("gear.png"), .05), 0, 0);
    Cell c = this.getNonOceanCell(a);
    this.x = c.x;
    this.y = c.y;
  }  
}

//to represent the helicopter piece
class HelicopterPiece extends APiece {
	
	//determines whether or not the player can get this helicopter
	boolean canGet = false;
	
	HelicopterPiece(IList<Cell> board) {
		super(new ScaleImage(new FromFileImage("helicopter.png"), .8), 0, 0);
		Cell c = this.findLocation(board);
		this.x = c.x;
		this.y = c.y;
	}
	
	//finds correct location for the helicopter piece
	Cell findLocation(IList<Cell> board) {
		Iterator<Cell> it = new IListIterator<Cell>(board);
		Cell maxC = new Cell(0.0, 0, 0, false);
		while (it.hasNext()) {
			Cell c = it.next();
			if (c.height > maxC.height) {
				maxC = c;
			}
			else if (c.height == maxC.height) {
				int choose = new Random().nextInt(2);
				if (choose == 1) {
					maxC = c;
				}
			}
		}
		return maxC;
	}
	
}


// Represents a single square of the game area
class Cell   {
  // represents absolute height of this cell, in feet
  double height;
  // In logical coordinates, with the origin at the top - left corner of the screen
  int x;
  int y;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  // reports whether this cell is flooded or not
  boolean isFlooded;
  
  Cell(double height, int x, int y, boolean isFlooded)   {
    this.height  =  height;
    this.x  =  x;
    this.y  =  y;
    this.isFlooded  =  isFlooded;
    if (x  ==  0)   {
      this.left  =  this;
    }
    if (x  ==  ForbiddenIslandWorld.ISLAND_SIZE)   {
      this.right  =  this;
    }
    if (y  ==  0)   {
      this.top  =  this;
    }
    if (y  ==  ForbiddenIslandWorld.ISLAND_SIZE)   {
      this.bottom  =  this;
    }
  }
  
  //to make the rectangle shape of the cell
  WorldImage oneCell(int waterHeight, int count)   {
    return new RectangleImage(10, 10, OutlineMode.SOLID, this.determineColor(waterHeight, count));
  }
  
  //to determine the color of the cell
  Color determineColor(int waterHeight, int count)   {
	  boolean closeToFlood = this.height - waterHeight <= 1;
	  if (!isFlooded && (!closeToFlood || count < 5)) {
		int red = 30;
		int green = 150;
		int blue = red;
		for (int i = 0; i < this.height - waterHeight; i = i + 1) {
			red = red + 5;
			green = green + 5;
			blue = red;
			if (green > 255) {
				green = 255;
			}
		}
		if (red > 255) {
			red = 255;
			blue = 255;
		}
		return new Color(red, green, blue);
	}
	  else if (closeToFlood && !isFlooded && count >= 5){
		  int red = 110;
	      int green = 60;
	      int blue = green;
	      for (int i = 0; i < this.height - waterHeight; i = i + 1) {
				red = red + 2;
				green = blue + 4;
				blue = green;
				if (red > 255) {
					red = 255;
				}
			}
			if (green > 255) {
				green = 255;
				blue = 255;
			}
			return new Color(red, green, blue);
		
	  }
	  else {
		  int red = 40;
	      int green = red;
	      int blue = 225;
	      for (int i = 0; i < waterHeight - this.height; i = i + 1) {
				red = red - 4;
				green = red;
				blue = blue - 4;
				if (blue < 102) {
					blue = 102;
				}
			}
			if (red < 24) {
				red = 25;
				green = 0;
			}
			return new Color(red, green, blue);
	  }
  }

	// is this Cell an OceanCell
	boolean isOcean() {
		return false;
	}
	
	
	//tests if two cells are the same
	boolean sameCell(Cell that) {
		return this.x == that.x 
				&& this.y == that.y
				&& this.height == that.height
				&& this.isFlooded == that.isFlooded
				&& this.isOcean() == that.isOcean();
	}

}

//represents a cell of the ocean
class OceanCell extends Cell   {
  
  OceanCell(double height, int x, int y)   {
    super(height, x, y, true);
  }
  

  /* FIELDS
   * ... height ...                    -  -  double
   * ... x, y ...                      -  -  int
   * ... left, top, right, bottom      -  -  Cell
   * ... isFlooded ...                 -  -  boolean
   * 
   * METHODS
   * ... this.displayCell() ...             -  -  WorldImage
   * ... this.oneCell() ...                 -  -  WorldImage
   * ... this.determineColor() ...          -  -  Color
   */
  
  //to make the rectangle shape of the cell
  WorldImage oneCell(int waterHeight, int count)   {
    return new RectangleImage(10, 10, OutlineMode.SOLID, new Color(0, 0, 255));
  }
  
  //is this Cell an OceanCell
  boolean isOcean() {
	  return true;
  }
}


//represents an iterator for ILists<T>
class IListIterator<T> implements java.util.Iterator<T> {
  
  IList<T> items;
    
  IListIterator(IList<T> items)  {
    this.items = items;
  }
    
  //determines if the IList has a next item 
  public boolean hasNext()  {
    return this.items.isCons();
  }
    
  //gets the next item in the IList
  public T next()  {
    ConsList<T> itemsAsCons = this.items.asCons();
    T answer = itemsAsCons.first;
    this.items = itemsAsCons.rest;
    return answer;
  }
  
}



//to represent a list of items
interface IList<T> extends Iterable<T> {
  //adds the items of the given array list to this list
  IList<T> addToList(ArrayList<ArrayList<T>> a);
  
  //determines if this IList<T> is a Cons
  boolean isCons();

  ConsList<T> asCons();

  //helps add the items of the given array to this list
  IList<T> addToListH(ArrayList<T> a);
  
  //appends one list to another
  IList<T> append(IList<T> l1);
  
  //gets the first item of a list
  T getFirst();
  
}

//to represent an empty list
class MtList<T> implements IList<T>   {

  //adds the items of the given array list to this list
  public IList<T> addToList(ArrayList<ArrayList<T>> a)   {
    IList<T> l  =  new MtList<T>();
    for (ArrayList<T> row : a)   {
      l  =  l.append(this.addToListH(row));
    }
    return l;
  }
  
  //helps add the items of the given array list to this list
  public IList<T> addToListH(ArrayList<T> a)   {
    IList<T> l  =  new MtList<T>();
    for (T t: a)   {
      l  =  l.append(new ConsList<T>(t, this));
    }
    return l;
  }
  
  //appends the given list to this list
  public IList<T> append(IList<T> l1)   {
    return l1;
  }
  
  //gets the first of this list
  public T getFirst()   {
    return null;
  }

  //iterates over the empty list
  public java.util.Iterator<T> iterator()  {
    return new IListIterator<T>(this);
  }
  
  //determines if this MtList is a cons
  public boolean isCons()  {
    return false;
  }
  
  //turns this empty list into a cons
  public ConsList<T> asCons()  {
    throw new ClassCastException("Cannot make empty a cons");
  }
 
}

//to represent a non - empty list of items
class ConsList<T> implements IList<T>   {
  T first;
  IList<T> rest;
  
  ConsList(T first, IList<T> rest)   {
    this.first  =  first;
    this.rest  =  rest; 
  }
  

  //adds the items of the given array list to this list
  public IList<T> addToList(ArrayList<ArrayList<T>> a)   {
    IList<T> l  =  new MtList<T>();
    for (ArrayList<T> row : a)   {
      l  =  l.append(this.addToListH(row));
    }
    return l;
  }
    
  //helps add the items of the given array list to this list
  public IList<T> addToListH(ArrayList<T> a)   {
    IList<T> l  =  new MtList<T>();
    for (T t: a)   {
      l  =  l.append(new ConsList<T>(t, this));
    }
    return l;
  }
  
  public T getFirst()   {
    return this.first;
  }
  
  //append the given list to this list;
  public IList<T> append(IList<T> l1)   {
    return new ConsList<T>(this.first, this.rest.append(l1));
  }
  
  //iterates over the non-empty list
  public java.util.Iterator<T> iterator()  {
    return new IListIterator<T>(this);
  }
  
  //determines if this MtList is a cons
  public boolean isCons()  {
    return true;
  }
  
  //turns this cons list into a cons
  public ConsList<T> asCons()  {
    return this;
  }
}

//a class of utilities for testing
class Utils<T>   {
  
  //checks if something is true for every item in the array list
  boolean andMap(ArrayList<T> a, IFunc<T, Boolean> f)   {
    boolean noFalse  =  true;
    for (int i  =  0; i <=  a.size()  -  1 && noFalse; i  =  i + 1)   {
      noFalse  =  f.apply(a.get(i));
    }
    return noFalse;
  }
  
  //checks if something is true for every item in the array list, 
  //against another arraylist
  boolean andMap2(ArrayList<T> a, IFunc<T, Boolean> f)   {
    boolean noFalse  =  true;
    for (int i  =  0; i <=  a.size()  -  1 && noFalse; i  =  i + 1)   {
      noFalse  =  f.apply2(a.get(i), i);
    }
    return noFalse;
  }
  
  //an andMap for IList
  boolean andMapIList(IList<T> list, IFunc<T, Integer> f) {
	  int count = 0;
	  IListIterator<T> iter = new IListIterator<T>(list);
	  while (iter.hasNext()) {
		  T arg = iter.next(); 
		  count = count + f.apply(arg);
	  }
	  return (count == 5);
  }
  
}

//to represent a function object
interface IFunc<T, U>    {
  
  //applies a function object
  U apply(T arg);
  
  //applies a function object with an integer parameter
  U apply2(T arg, int num);
}


//for determining if each row in the array contains only doubles
class AllManDistRow implements IFunc<ArrayList<Double>, Boolean>   {

  //applies the function object
  public Boolean apply(ArrayList<Double> arg)   {
    boolean noFalse  =  true;
    for (int i  =  0; i <=  arg.size()  -  1 && noFalse; i  =  i + 1)   {
      noFalse  =  new AllManDist().apply(arg.get(i));
    }
    return noFalse;
  }

  //alternate apply
  public Boolean apply2(ArrayList<Double> arg, int num)   {
    return this.apply(arg);
  }
  
}

//for determining if item in the array contains only doubles
class AllManDist implements IFunc<Double, Boolean>   {

  //applies the function object
  public Boolean apply(Double arg)   {
    return arg <=  ForbiddenIslandWorld.ISLAND_SIZE / 2;
  }

  //alternate apply
  public Boolean apply2(Double arg, int num)   {
    return this.apply(arg);
  }
  
}


class UnderMaxHeight implements IFunc<ArrayList<Double>, Boolean> {
	  //applies the function object
	  public Boolean apply(ArrayList<Double> arg)   {
		boolean notFalse = true;
		for (Double d:  arg) {
			notFalse = d <=  ForbiddenIslandWorld.ISLAND_SIZE / 2;
		}
		return notFalse;
	  }

	  //applies the function object
	  public Boolean apply2(ArrayList<Double> arg, int num)   {
		boolean notFalse = true;
		for (Double d:  arg) {
			notFalse = d <=  ForbiddenIslandWorld.ISLAND_SIZE / 2;
		}
		return notFalse;
	  }

}

//to determine if all cells in the list are the right height
class CorrectCHeight implements IFunc<ArrayList<Cell>, Boolean>   {

  //this method cannot use apply, so its return type is null
  public Boolean apply(ArrayList<Cell> arg)   {
    return null;
  }

  //apply2 takes an index, so it will be the predicate in andMap2
  public Boolean apply2(ArrayList<Cell> arg, int num)   {
    boolean noFalse  =  true;
    for (int i  =  0; i <=  arg.size()  -  1 && noFalse; i  =  i + 1)   {
      noFalse  =  (arg.get(i).height
          ==  new ForbiddenIslandWorld().doubleList.get(num).get(i));
    }
    return noFalse;
  }
  
}

//to determine if a value is equal to zero
class EqualZero implements IFunc<Double, Integer> {

	//checks if a double value is equal to 0
	public Integer apply(Double arg) {
		if (arg == 0.0) {
			return 0;
		}
		else {
			return 1;
		}
	}

	//checks if a double value is equal to 0
	public Integer apply2(Double arg, int num) {
		if (arg == 0.0) {
			return 0;
		}
		else {
			return 1;
		}
	}
}

