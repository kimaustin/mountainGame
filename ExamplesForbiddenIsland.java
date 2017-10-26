import java.awt.Color;
import java.util.*;
import javalib.worldimages.*;
import javalib.impworld.*;
import tester.Tester;

//examples and tests
class ExamplesForbiddenIsland   {
  int iSize  =  ForbiddenIslandWorld.ISLAND_SIZE;
  ArrayList<Double> a  =  new ArrayList<Double>();
  int width  =  ForbiddenIslandWorld.ISLAND_SIZE + 1;
  int length  =  this.iSize + 1;
  Utils<ArrayList<Double>> util  =  new Utils<ArrayList<Double>>();
  ArrayList<ArrayList<Double>> l1  =  new ArrayList<ArrayList<Double>>(Arrays.asList(
      new ArrayList<Double>(Arrays.asList(0.0, 400.0)), 
      new ArrayList<Double>(Arrays.asList(0.0, 400.0)), 
      new ArrayList<Double>(Arrays.asList(0.0, 400.0))));
  ArrayList<Double> l2  =  new ArrayList<Double>(Arrays.asList(0.0, 400.0));
  ArrayList<Double> l3  =  new ArrayList<Double>(Arrays.asList(0.0, 3.0));
  IList<Cell> mt  =  new MtList<Cell>();
  Cell cell1  =  new Cell(1, 10, 10, false);
  Cell cell2  =  new OceanCell( - 5, 10, 10);
  Cell cell3  =  new Cell(2, 10, 10, false);
  WorldImage oneCell1  =  new RectangleImage(10, 10, OutlineMode.SOLID, new Color(35, 155, 35));
  WorldImage oneCell2  =  new RectangleImage(10, 10, OutlineMode.SOLID, new Color(0, 0, 255));
  WorldImage iCell1  =  new OverlayImage(this.oneCell1,
          new PhantomImage(new EmptyImage(), this.width, this.width)
            .movePinholeTo(new Posn(10  -  this.width / 2, 10  -  this.width / 2)))
                .movePinholeTo(new Posn(this.iSize / 2, this.iSize / 2));  
  WorldImage iCell2  =  new OverlayImage(this.oneCell2,
          new PhantomImage(new EmptyImage(), this.width, this.width)
            .movePinholeTo(new Posn(10  -  this.width / 2, 10  -  this.width / 2)))
                .movePinholeTo(new Posn(this.iSize / 2, this.iSize / 2)); 
  IList<Cell> cellList  =  new ConsList<Cell>(this.cell1, 
      new ConsList<Cell>(this.cell2, this.mt));
  ArrayList<ArrayList<Cell>> cl1  =  new ArrayList<ArrayList<Cell>>(Arrays.asList(
      new ArrayList<Cell>(Arrays.asList(this.cell1, this.cell2)), 
      new ArrayList<Cell>(Arrays.asList(this.cell2, this.cell1))));
  IList<Cell> lc1  =  new ConsList<Cell>(this.cell1, 
      new ConsList<Cell>(this.cell2,
          new ConsList<Cell>(this.cell2,
              new ConsList<Cell>(this.cell1, this.mt))));
  ArrayList<ArrayList<Cell>> mCells;
  ArrayList<ArrayList<Cell>> rCells;
  ArrayList<ArrayList<Cell>> tCells;
  
  //tests the game
  void testGame(Tester t)   {
    new ForbiddenIslandWorld().bigBang((this.iSize + 1)* 10,(this.iSize + 1)* 10, 1.0);
  }
  
  
  // test Rebuild
  // test removeEngs
  // test generateEngs
  //
  
  //EFFECT: initializes values for mountain island
  void initMountain()   {
    ForbiddenIslandWorld w  =  new ForbiddenIslandWorld();
    w.onKeyEvent("m");
    this.mCells = w.cellList;
  }
  
  //EFFECT: initializes values for random island
  void initRandom()   {
    ForbiddenIslandWorld w  =  new ForbiddenIslandWorld();
    w.onKeyEvent("m");
    this.rCells = w.cellList;
  }
  
  //EFFECT: initializes values for random terrain island
  void initTerrain()   {
    ForbiddenIslandWorld w  =  new ForbiddenIslandWorld();
    w.onKeyEvent("t");
    this.tCells = w.cellList;
  }
  
  //to test initMountain() and initRandom() 
  public boolean testInitMountainRandom(Tester t)  {
    this.initMountain();
    boolean next = t.checkExpect(this.mCells == null, false)
        && new Utils<ArrayList<Cell>>()
        .andMap2(this.mCells,  new CorrectCHeight());
    this.initRandom();
    boolean third = new Utils<ArrayList<Cell>>()
        .andMap2(this.mCells,  new CorrectCHeight());
    return next && !third;
    
  }
  
  //to test initTerrain
  public boolean testInitTerrain(Tester t) {
    this.mCells = null;
    this.tCells = null;
    this.initMountain();
    ArrayList<ArrayList<Cell>> mCells2 = this.mCells;
    boolean start = this.tCells == null && mCells2 != null;
    this.initTerrain();
    boolean finish = this.tCells != null && !this.mCells.equals(this.tCells);
    return t.checkExpect(start && finish, true);
  }
  
 
  //tests oneCell() method for cell
  public boolean testOneCell(Tester t)   {
    return t.checkExpect(this.cell1.oneCell(0, 2), this.oneCell1)
        && t.checkExpect(this.cell2.oneCell(1, 7), this.oneCell2)
        && t.checkExpect(this.cell1.oneCell(0, 7), 
            new RectangleImage(10, 10, OutlineMode.SOLID, new Color(112, 64, 64)));
  }

  
  //tests displayCell() method for cell
  public boolean testDetermineColor(Tester t)   {
    return t.checkExpect(this.cell3.determineColor(0, 7), 
        new Color(40, 160, 40))
        && t.checkExpect(this.cell1.determineColor(1, 7), 
            new Color(110, 60, 60))
        && t.checkExpect(this.cell1.determineColor(0, 2), 
                new Color(35, 155, 35));
  }
  
  
  //to test makeDoublesRow
  public boolean testMakeDoublesRow(Tester t)   {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    double x1  =  this.iSize / 2;
    double y1  =  this.iSize / 2;
    for (int j  =  0; j <=  this.iSize; j++)   {
      double x0  =  this.iSize  -  j;
      double y0  =  this.iSize  -  2;
      double ManDist  =  (Math.abs(y1 - y0) + Math.abs(x1 - x0));
      a.add(this.iSize  -  ManDist  -  ForbiddenIslandWorld.ISLAND_SIZE / 2);
    } 
    return t.checkExpect(world.makeDoublesRow(2), a)
        && t.checkExpect(world.makeDoublesRow(2).size(), this.iSize + 1);
  }
  
  //to test makeDoublesList
  public boolean testMakeDoublesList(Tester t)   {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    return t.checkExpect(world.makeDoublesList().size(), this.iSize + 1)
        && t.checkExpect(this.util.andMap(world.makeDoublesList(), new AllManDistRow()), true)
        && t.checkExpect(world.makeDoublesList().get(0).size(),  this.iSize + 1);
  }
  
  //to test andMap for utils
  public boolean testAndMap(Tester t)   {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    return t.checkExpect(this.util.andMap(world.makeDoublesList(), new AllManDistRow()), true)
        && t.checkExpect(this.util.andMap(this.l1, new AllManDistRow()), false);
  }
  
  //to test apply
  public boolean testApply(Tester t)   {
    return t.checkExpect(new AllManDistRow().apply(this.l3), true)
        && t.checkExpect(new AllManDistRow().apply(this.l2), false)
        && t.checkExpect(new AllManDist().apply(4.0), true)
        && t.checkExpect(new AllManDist().apply(500.0), false);
  }
  
  //to test makeCellList, MakeCellRow, and MakeCellRowRand
  //for MountainIsland using an andMap for cell height
  public boolean testMakeCellList(Tester t)   {
  ForbiddenIslandWorld world = new ForbiddenIslandWorld();
  return t.checkExpect(new Utils<ArrayList<Cell>>()
        .andMap2(world.makeCellList(false),  new CorrectCHeight()), true)
        && t.checkExpect(new Utils<ArrayList<Cell>>()
                .andMap2(world.makeCellList(false),  new CorrectCHeight()), true)
        && t.checkExpect(new Utils<ArrayList<Cell>>()
            .andMap2(this.cl1,  new CorrectCHeight()), false);
  }
  
  //tests updating the cell links for a mountain
  public boolean testUpdateCellsMountain(Tester t)   {
  ForbiddenIslandWorld world = new ForbiddenIslandWorld();  
    this.initMountain();
    return t.checkExpect(mCells.get(this.iSize).get(this.iSize).left.sameCell(
        mCells.get(world.iSize).get(world.iSize  -  1)), true)
        && t.checkExpect(mCells.get(this.iSize  -  1).get(this.iSize  -  1).left.sameCell(
            mCells.get(world.iSize  -  1).get(world.iSize  -  2)), true)
        && t.checkExpect(mCells.get(this.iSize  -  1).get(this.iSize  -  1).right.sameCell(
            mCells.get(world.iSize  -  1).get(this.iSize)), true)
        && t.checkExpect(mCells.get(this.iSize  -  1).get(this.iSize  -  1).top.sameCell(
            mCells.get(this.iSize  -  2).get(this.iSize  -  1)), true)
        && t.checkExpect(mCells.get(this.iSize  -  1).get(this.iSize  -  1).bottom.sameCell(
            mCells.get(this.iSize).get(this.iSize  -  1)), true)
        && t.checkExpect(mCells.get(this.iSize).get(this.iSize).right.sameCell(
            mCells.get(this.iSize).get(this.iSize)), true)
        && t.checkExpect(mCells.get(this.iSize).get(this.iSize).bottom.sameCell(
            mCells.get(this.iSize).get(this.iSize)), true)
        && t.checkExpect(mCells.get(this.iSize).get(2).bottom.sameCell(
                mCells.get(this.iSize).get(2)), true)
        && t.checkExpect(mCells.get(0).get(0).bottom.sameCell(
                mCells.get(1).get(0)), true)
        && t.checkExpect(mCells.get(0).get(0).left.sameCell(
                mCells.get(0).get(0)), true)
        && t.checkExpect(mCells.get(1).get(0).left.sameCell(
                mCells.get(1).get(0)), true)
        && t.checkExpect(mCells.get(1).get(this.iSize).right.sameCell(
                mCells.get(1).get(this.iSize)), true);      
        
  }
  
  
  //tests updating the cell links for a RandomIsland
  public boolean testUpdateCellsRandom(Tester t)   {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    this.initRandom();
    return t.checkExpect(rCells.get(this.iSize).get(this.iSize).left.sameCell(
        rCells.get(world.iSize).get(world.iSize  -  1)), true)
        && t.checkExpect(rCells.get(this.iSize  -  1).get(this.iSize  -  1).left.sameCell(
            rCells.get(world.iSize  -  1).get(world.iSize  -  2)), true)
        && t.checkExpect(rCells.get(this.iSize  -  1).get(this.iSize  -  1).right.sameCell(
            rCells.get(world.iSize  -  1).get(this.iSize)), true)
        && t.checkExpect(rCells.get(this.iSize  -  1).get(this.iSize  -  1).top.sameCell(
            rCells.get(this.iSize  -  2).get(this.iSize  -  1)), true)
        && t.checkExpect(rCells.get(this.iSize  -  1).get(this.iSize  -  1).bottom.sameCell(
            rCells.get(this.iSize).get(this.iSize  -  1)), true)
        && t.checkExpect(rCells.get(this.iSize).get(this.iSize).right.sameCell(
            rCells.get(this.iSize).get(this.iSize)), true)
        && t.checkExpect(rCells.get(this.iSize).get(this.iSize).bottom.sameCell(
            rCells.get(this.iSize).get(this.iSize)), true)
        && t.checkExpect(rCells.get(this.iSize).get(2).bottom.sameCell(
                rCells.get(this.iSize).get(2)), true)
        && t.checkExpect(rCells.get(0).get(0).bottom.sameCell(
                rCells.get(1).get(0)), true)
        && t.checkExpect(rCells.get(0).get(0).left.sameCell(
                rCells.get(0).get(0)), true)
        && t.checkExpect(rCells.get(1).get(0).left.sameCell(
                rCells.get(1).get(0)), true)
        && t.checkExpect(rCells.get(1).get(this.iSize).right.sameCell(
                rCells.get(1).get(this.iSize)), true);      
  }
  
  
  //tests updating the cell links for a Terrain Island
  public boolean testUpdateCellsTerrain(Tester t)   {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    this.initTerrain();
    return t.checkExpect(tCells.get(this.iSize).get(this.iSize).left.sameCell(
        tCells.get(world.iSize).get(world.iSize  -  1)), true)
        && t.checkExpect(tCells.get(this.iSize  -  1).get(this.iSize  -  1).left.sameCell(
            tCells.get(world.iSize  -  1).get(world.iSize  -  2)), true)
        && t.checkExpect(tCells.get(this.iSize  -  1).get(this.iSize  -  1).right.sameCell(
            tCells.get(world.iSize  -  1).get(this.iSize)), true)
        && t.checkExpect(tCells.get(this.iSize  -  1).get(this.iSize  -  1).top.sameCell(
            tCells.get(this.iSize  -  2).get(this.iSize  -  1)), true)
        && t.checkExpect(tCells.get(this.iSize  -  1).get(this.iSize  -  1).bottom.sameCell(
            tCells.get(this.iSize).get(this.iSize  -  1)), true)
        && t.checkExpect(tCells.get(this.iSize).get(this.iSize).right.sameCell(
            tCells.get(this.iSize).get(this.iSize)), true)
        && t.checkExpect(tCells.get(this.iSize).get(this.iSize).bottom.sameCell(
            tCells.get(this.iSize).get(this.iSize)), true)
        && t.checkExpect(tCells.get(this.iSize).get(2).bottom.sameCell(
                tCells.get(this.iSize).get(2)), true)
        && t.checkExpect(tCells.get(0).get(0).bottom.sameCell(
                tCells.get(1).get(0)), true)
        && t.checkExpect(tCells.get(0).get(0).left.sameCell(
                tCells.get(0).get(0)), true)
        && t.checkExpect(tCells.get(1).get(0).left.sameCell(
                tCells.get(1).get(0)), true)
        && t.checkExpect(tCells.get(1).get(this.iSize).right.sameCell(
                tCells.get(1).get(this.iSize)), true);      
  }
  
  
  //tests the makeDoubleTerrainStart method
  public boolean testDoubleTerrainStart(Tester t) {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    IList<Double> l = new MtList<Double>().addToList(world.makeDoubleTerrainStart());
    return t.checkExpect(new Utils<Double>().andMapIList(l, new EqualZero()), true);
  }
  
  
  //test makeDoubleTerrain method
  public boolean testDoubleTerrain(Tester t) {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    world.onKeyEvent("t");
    return t.checkExpect(new Utils<ArrayList<Double>>().andMap(world.doubleList, new UnderMaxHeight()), true);
  }
  
  
  
  
  //tests sameCell method for cells
  public boolean testSameCell(Tester t)  {
    return t.checkExpect(new Cell(10, 10, 10, false).sameCell(new Cell(10, 10, 10, false)), true)
        && t.checkExpect(new Cell(10, 10, 10, true).sameCell(new Cell(10, 10, 10, false)), false)
        && t.checkExpect(new Cell(5, 10, 10, true).sameCell(new Cell(10, 10, 10, false)), false)
        && t.checkExpect(new Cell(5, 10, 10, true).sameCell(new Cell(5, 5, 10, false)), false)
        && t.checkExpect(new Cell(5, 10, 10, true).sameCell(new Cell(5, 5, 5, false)), false)
        && t.checkExpect(new Cell(5, 10, 10, true).sameCell(new OceanCell(5, 10, 10)), false);
  }
 
  
  //to test addToList
  public boolean testAddToList(Tester t)   {
  ForbiddenIslandWorld world = new ForbiddenIslandWorld();  
    ArrayList<ArrayList<Cell>> mCells  =  world.makeCellList(false);
    return t.checkExpect(this.mt.addToList(mCells).getFirst(), mCells.get(0).get(0))
        && t.checkExpect(this.mt.addToList(this.cl1), this.lc1);
  }
    
  
  
  //to test isCons
  public boolean testIsCons(Tester t)   {
    return t.checkExpect(this.mt.isCons(), false)
        && t.checkExpect(this.cellList.isCons(), true);
  }
  
  
  //to test AsCons
  public boolean testAsCons(Tester t)   {
    return t.checkExpect(this.cellList.asCons(), this.cellList)
        && t.checkException(new ClassCastException("Cannot make empty a cons"), 
            this.mt, "asCons");
  }
  
  
  //to test addToList
  public boolean testAddToListH(Tester t)   {
  ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    ArrayList<ArrayList<Cell>> mCells  =  world.makeCellList(false);
    return t.checkExpect(this.mt.addToListH(mCells.get(0)).getFirst(), mCells.get(0).get(0))
        && t.checkExpect(this.mt.addToListH(this.cl1.get(0)), 
            new ConsList<Cell>(this.cell1, 
                new ConsList<Cell>(this.cell2, this.mt)));
  }
  
  //to test append
  public boolean testAppend(Tester t)   {
    return t.checkExpect(this.mt.append(this.lc1), this.lc1)
        && t.checkExpect(this.lc1.append(this.mt), this.lc1)
        && t.checkExpect(new ConsList<Cell>(this.cell1, 
                new ConsList<Cell>(this.cell2, this.mt)).append(
                    new ConsList<Cell>(this.cell2, 
                        new ConsList<Cell>(this.cell1, this.mt))), this.lc1);
  }
  
  //to test getFirst
  public boolean testGetFirst(Tester t)   {
    return t.checkExpect(this.mt.getFirst(), null)
        && t.checkExpect(this.lc1.getFirst(), this.cell1);
  }
  
  //to test Iterator
  public boolean testIterator(Tester t)   {
    return t.checkExpect(this.mt.iterator(), new IListIterator<Cell>(this.mt))
        && t.checkExpect(this.lc1.iterator(), new IListIterator<Cell>(this.lc1));
  }
  
  //to test getFirst
  public boolean testIteratorMethods(Tester t)   {
    java.util.Iterator<Cell> i = new IListIterator<Cell>(this.lc1);
    java.util.Iterator<Cell> i2 = new IListIterator<Cell>(this.mt);
    return t.checkExpect(i2.hasNext(), false)
        && t.checkExpect(i.hasNext(), true)
        && t.checkExpect(i.next(), this.cell1)
        && t.checkException(new ClassCastException("Cannot make empty a cons"), 
        i2, "next");
  } 
  
  
  //to test getNonOceanCell
  public boolean testNonOceanCell(Tester t) {
    ForbiddenIslandWorld w  =  new ForbiddenIslandWorld();
    w.onKeyEvent("r");
    Target targets = new Target(w.cellList);
    return t.checkExpect(targets.getNonOceanCell(w.makeCellList(false)).isOcean(), false);
  }
  
  //to test getRandLocation
  public boolean testGetRandLocation(Tester t) {
    ForbiddenIslandWorld w  =  new ForbiddenIslandWorld();
    w.onKeyEvent("r");
    return t.checkExpect(w.user.getNonOceanCell(w.cellList).isOcean(), false)
        && t.checkExpect(w.heli.getNonOceanCell(w.cellList).isOcean(), false);
  }
  
  //to test find location
  public boolean testFindLocation(Tester t) {
    ForbiddenIslandWorld w  =  new ForbiddenIslandWorld();
    w.onKeyEvent("r");
    HelicopterPiece h = new HelicopterPiece(w.board);
    return t.checkExpect(h.findLocation(w.board).isOcean(), false);
  }
  

  
  
  //to test onKeyEvent
  public boolean testOnKeyMountain(Tester t)   {
  ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    ForbiddenIslandWorld w2  =  new ForbiddenIslandWorld();
    ForbiddenIslandWorld w3  =  new ForbiddenIslandWorld();
    w2.onKeyEvent("m");
    this.initMountain();
    w3.onKeyEvent("r");
    return t.checkExpect(w2.cellList.size(), this.iSize + 1)
        && t.checkExpect(w2.cellList.equals(world.makeCellList(false)), false)
        && t.checkExpect(w3.cellList.equals(world.makeCellList(true)), false);
  }
  
  //to test onKeyEvent
  public boolean testOnKeyRandom(Tester t)   {
  ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    ForbiddenIslandWorld w3  =  new ForbiddenIslandWorld();
    ForbiddenIslandWorld w2  =  new ForbiddenIslandWorld();
    w3.cellList  =  w3.makeCellList(true);
    w2.onKeyEvent("r");
    w3.onKeyEvent("m");
    this.initRandom();
    return t.checkExpect(w2.cellList.size(), this.iSize + 1)
        && t.checkExpect(w3.cellList.equals(world.cellList), false);
  }
  
  //to test onKeyEvent
  public boolean testOnKeyTerrain(Tester t)   {
    ForbiddenIslandWorld world = new ForbiddenIslandWorld();
    ForbiddenIslandWorld w3  =  new ForbiddenIslandWorld();
    ForbiddenIslandWorld w2  =  new ForbiddenIslandWorld();
    w3.onKeyEvent("t");
    w2.onKeyEvent("m");
    this.initRandom();
    return t.checkExpect(w2.cellList.size(), this.iSize + 1)
        && t.checkExpect(w3.cellList.equals(world.cellList), false)
        && t.checkExpect(new Utils<ArrayList<Cell>>()
            .andMap2(w3.makeCellList(false),  new CorrectCHeight()), false);
  }
  
  
  //to test makeScene()
  public boolean testMakeScene(Tester t)   {
    ForbiddenIslandWorld island = new ForbiddenIslandWorld();
    island.onKeyEvent("m");
    WorldScene scene = island.getEmptyScene();
    island.board = this.lc1;
    island.count2 = 0;
    scene.placeImageXY(this.cell1.oneCell(0, 0), 105, 105);
    scene.placeImageXY(this.cell2.oneCell(0,0), 105, 105);
    scene.placeImageXY(this.cell2.oneCell(0,0), 105, 105);
    scene.placeImageXY(this.cell1.oneCell(0,0), 105, 105);
    return t.checkExpect(island.makeScene(), scene);
  }
  
  
  //to test updateWater()
  public boolean testUpdateWater(Tester t)   {
    ForbiddenIslandWorld island = new ForbiddenIslandWorld();
    island.onKeyEvent("m");
    boolean init = island.count == 0 && island.user.isDead == false;
    island.count = 10;
    island.user.x = 2;
    island.user.y = 33;
    island.updateWater();
    return t.checkExpect(island.count, 0)
        && t.checkExpect(island.user.isDead, true) 
        && t.checkExpect(init, true);
    
  }
  
  
  //to test onKeyEvent for moving the Player
  public boolean testOnKeyEventUser(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
   island.onKeyEvent("m"); 
   island.user.x = 20;
   island.user.y = 20;
   island.onKeyEvent("right");
   boolean right = island.user.x == 21;
   island.onKeyEvent("left");
   boolean left = island.user.x == 20;
   island.onKeyEvent("down");
   boolean down = island.user.y == 21;
   island.onKeyEvent("up");
   boolean up = island.user.y == 20;
   return t.checkExpect(right, true)
       && t.checkExpect(left, true)
       && t.checkExpect(up, true)
       && t.checkExpect(down, true);
   
  }
  
  //to test onKeyEvent for moving the Player and preventing moving into the ocean
  public boolean testOnKeyEventUser2(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
   island.onKeyEvent("m"); 
   island.user.x = 0;
   island.user.y = 0;
   island.onKeyEvent("right");
   boolean right = island.user.x == 0;
   island.onKeyEvent("left");
   boolean left = island.user.x == 0;
   island.onKeyEvent("down");
   boolean down = island.user.y == 0;
   island.onKeyEvent("up");
   boolean up = island.user.y == 0;
   return t.checkExpect(right, true)
       && t.checkExpect(left, true)
       && t.checkExpect(up, true)
       && t.checkExpect(down, true);
   
  }
  
  //to test movePlayer
  public boolean testMovePlayer(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
     island.onKeyEvent("m"); 
   Player p = new Player(island.cellList);
   p.x = 20;
   p.y = 20;
   p.movePlayer("right", island.board);
   boolean right = p.x == 21;
   p.movePlayer("left", island.board);
   boolean left = p.x == 20;
   p.movePlayer("down", island.board);
   boolean down = p.y == 21;
   p.movePlayer("up", island.board);
   boolean up = p.y == 20;
   return t.checkExpect(right, true)
       && t.checkExpect(left, true)
       && t.checkExpect(up, true)
       && t.checkExpect(down, true);
   
  }
  
  //to test movePlayer if the player cannot move onto the ocean
  public boolean testMovePlayer2(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
     island.onKeyEvent("m"); 
   Player p = new Player(island.cellList);
   p.x = 0;
   p.y = 0;
   p.movePlayer("right", island.board);
   boolean right = p.x == 0;
   p.movePlayer("left", island.board);
   boolean left = p.x == 0;
   p.movePlayer("down", island.board);
   boolean down = p.y == 0;
   p.movePlayer("up", island.board);
   boolean up = p.y == 0;
   return t.checkExpect(right && left && down && up, true);
   
  }
  
  //to test onKeyReleased
  public boolean testOnKeyReleased(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
   island.targets = new ArrayList<Target>();
   island.user.x = 10;
   island.user.y = 10;
   island.heli.x = 10;
   island.heli.y = 10;
   island.onKeyReleased("right");
   boolean heliGone = island.heli.canGet;
   boolean won = island.user.won;
   return t.checkExpect(heliGone && won, true);
   
  }
  
  //to test removeTargets
  public boolean testRemoveTargets(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
   int howMany = island.targets.size();
   island.targets.get(0).x = 10;
   island.targets.get(0).y = 10;
   island.user.x = 10;
   island.user.y = 10;
   island.removeTargets();
   return t.checkExpect(island.targets.size() < howMany, true);
   
  }
  
  //to test worldEnds
  public boolean testWorldEnds(Tester t) {
   ForbiddenIslandWorld island = new ForbiddenIslandWorld();
   ForbiddenIslandWorld island2 = new ForbiddenIslandWorld();
   boolean init = t.checkExpect(island.worldEnds(), new WorldEnd(false, island.makeScene()))
       && t.checkExpect(island2.worldEnds(), new WorldEnd(false, island2.makeScene()));
   island.user.won = true;
   island2.user.isDead = true;
   boolean fin = t.checkExpect(island.wonOrLost(), island.displayText("You escaped!"))
       && t.checkExpect(island2.wonOrLost(), island2.displayText("You lost!"));
   return t.checkExpect(init && fin, true);
   
  }
  
  //to test finalScene
  public boolean testFinalScene(Tester t) {
    ForbiddenIslandWorld island = new ForbiddenIslandWorld();
    WorldScene scene = island.getEmptyScene();
    scene.placeImageXY(new TextImage("hello", 
        20, FontStyle.BOLD, Color.RED), this.iSize * 10 / 2, 27);
    return t.checkExpect(island.displayText("hello"), scene);
   
  }
  
  
  //to test collision
  public boolean testCollision(Tester t) {
    ForbiddenIslandWorld island = new ForbiddenIslandWorld();
    island.user.x = 10;
    island.user.y = 10;
    Target tar = new Target(island.cellList);
    tar.x = 10;
    tar.y = 10;
    HelicopterPiece heli = new HelicopterPiece(island.board);
    heli.x = 10;
    heli.y = 10;
    boolean collide1 = t.checkExpect(island.user.collision(tar), true) 
        && t.checkExpect(island.user.collision(heli), true) 
        && t.checkExpect(heli.collision(island.user), true)
        && t.checkExpect(tar.collision(island.user), true)
        && t.checkExpect(tar.collision(heli), true)
        && t.checkExpect(heli.collision(tar), true);
    tar.x = 22;
    tar.y = 22;
    heli.x = 1;
    heli.y = 1;
    boolean collide2 = t.checkExpect(island.user.collision(tar), false) 
        && t.checkExpect(island.user.collision(heli), false) 
        && t.checkExpect(heli.collision(island.user), false)
        && t.checkExpect(tar.collision(island.user), false)
        && t.checkExpect(tar.collision(heli), false)
        && t.checkExpect(heli.collision(tar), false);
    return t.checkExpect(collide1 && collide2, true);
   
  }
  
//  //tests displayPiece
//  public boolean testDisplayPiece(Tester t) {
//    ForbiddenIslandWorld island = new ForbiddenIslandWorld();
//    Target tar = new Target(island.cellList);
//    return t.checkExpect(island.user.displayPiece(), 
//        new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW))
//        && t.checkExpect(island.heli.displayPiece(), 
//            new EllipseImage(10, 10, OutlineMode.SOLID, Color.black))
//        && t.checkExpect(tar.displayPiece(), 
//            new EllipseImage(5, 5, OutlineMode.SOLID, Color.MAGENTA));
//  }
  
  
  
}