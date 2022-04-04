// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package graphics;

import worlds.*;
import utils.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import applications.simpleworld.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;

import objects.CommonObject;
import objects.Monolith;

import landscapegenerator.LoadFromFileLandscape;
import landscapegenerator.PerlinNoiseLandscapeGenerator;

/*
 * TODO
 * - clean code
 * - add objects + CA_objects
 * - add agents
 * - add my-agent + move + modify heights
 * - water flowing ; snow (same, with evaporation)
 *
 * technical issues:
 * - antialias
 * - go fast -- cf. "GO FAST" COMMENTS TO CHECK DOUBLE BUFFERING TRICK
 * 
 * 
*/





/**
 * Self-contained code 
 * displaying a landscape generated with Perlin noise
 */
public class Landscape implements GLEventListener, KeyListener, MouseListener{
	
		private WorldOfTrees _myWorld; 
		private Weather weather;

		private static final double INITIAL_PREDATOR_DENSITY = 0.001;
		private static final double INITIAL_PREY_DENSITY = 0.005;
		private static final double INITIAL_PLANT_DENSITY = 0.001;
	
		private static GLCapabilities caps;  // GO FAST ???
	
		static boolean MY_LIGHT_RENDERING = false; // true: nicer but slower
		
		final static boolean SMOOTH_AT_BORDER = true; // nicer (but wrong) rendering at border (smooth altitudes)
		
		//final static double landscapeAltitudeRatio = 0.6; // 0.5: half mountain, half water ; 0.3: fewer water
		
		public static boolean VIEW_FROM_ABOVE = false; // also deactivate altitudes
		
		static boolean DISPLAY_OBJECTS = true; // useful to deactivate if view_from_above
		
		static boolean DISPLAY_FPS = true; // on-screen display

		static boolean DISPLAY_HELP = true; // on-screen display

		
		/*
		 * benchmarking 
		 * 		Airbook (w/wo visible display) : 
		 * 			true:  frame per second  : frames per second  : 59 ; polygons per second: 966656 --- frames per second  : 82 ; polygons per second: 1343488
		 * 			false: frames per second  : 61 ; polygons per second: 999424 --- frames per second  : 254 ; polygons per second: 4161536 (!!!)
		 * 
		 * 
		 * Bonnes pratiques:
		 * - gl.Begin() ... gl.glEnd(); : faire un minimum d'appel, idealement un par iteration. (gain de 50% a 100% ici)
		 * - gl.glCullFace(GL.GL_FRONT); ... gl.glEnable(GL.GL_CULL_FACE); : si la scene le permet, reduit le nb de polyg a afficher.
		 * - TRIANGLE SPLIT permet de reduire le nombre d'appels a OpenGL (gl.begin et end)
		 * - each call to gl.glColor3f costs a lot (speed is down by two if no call!)
		 */
	
		static Animator animator; 
		//  https://sites.google.com/site/justinscsstuff/jogl-tutorial-3
		//  if you use a regular Animator object instead of the FPSAnimator, your program will render as fast as possible. You can, however, limit the framerate of a regular Animator by asking the graphics driver to synchronize with the refresh rate of the display (v-sync). Because the target framerate is often the same as the refresh rate, which is often 60-75 or so, this method is a great choice as it lets the driver do the work of limiting frame changes. 
		//  However, some Intel GPUs may ignore this setting. In the init method, active v-sync as follows:
		//  add : drawable.getGL().setSwapInterval(1); in the init method
		//  then: in the main method, replace the FPSAnimator with a regular Animator.
		
		private float rotateX = 0.0f;
		
		private float rotationVelocity = 0.2f; // 0.2f

        int it = 0;
        int movingIt = 0;
        int dxView;
        int dyView;

        public double[][] landscape;

        int lastFpsValue = 0;
        
        public static int lastItStamp = 0;
        public static long lastTimeStamp = 0;
        
        // visualization parameters
        
    	float heightFactor; //64.0f; // was: 32.0f;
        double heightBooster; // applied to landscape values. increase heights.
        // -- NOTE that this could also be achieved using heighFactor but is decomposed to enable further pre-calc of height values
        // heightFactor deals with visualization
        // heigBooster will impact landscape array content 
       
		float offset;
		float stepX;
		float stepY;
		float lenX;
		float lenY;
		
        float smoothFactor[];
        int smoothingDistanceThreshold;
        
        int movingX = 0; 
        int movingY = 0; 
        int movingZ = 0;
        
        /**
         * 
         */
        public Landscape (WorldOfTrees __myWorld, int __dx, int __dy, double scaling, double landscapeAltitudeRatio)
        {
    		_myWorld = __myWorld;

            //change
    		_myWorld.setLandscape(this);

    		landscape = PerlinNoiseLandscapeGenerator.generatePerlinNoiseLandscape(__dx,__dy,scaling,landscapeAltitudeRatio, 11); //last argument : how many noise functions we want to sum up in the perlin noise result 11 seems to be decent for details and smooth map
    		
    		initLandscape();

			initAgents();

        }

        /**
         * 
         */
        public Landscape (WorldOfTrees __myWorld, String __filename, double scaling, double landscapeAltitudeRatio)
        {
    		_myWorld = __myWorld;

    		_myWorld.setLandscape(this);

    		landscape = LoadFromFileLandscape.load(__filename,scaling,landscapeAltitudeRatio);

    		initLandscape();

			initAgents();
        }
        public int x, y;
        /**
         * 
         */
        private void initLandscape()
        {
       		dxView = landscape.length;
    		dyView = landscape[0].length;

    		System.out.println("Landscape contains " + dxView*dyView + " tiles. (" + dxView + "x" + dyView +")");

    		weather = new Weather(_myWorld);
    		landscape = weather.initVolcano(landscape);
        	
    		_myWorld.init(dxView,dyView,landscape);


    		float color[] = {1f, 0f, 0f};
    		_myWorld.setCellState(x, y, color);
    		_myWorld.setCellState((x-1 + landscape.length-1) % (landscape.length-1), y, color);
    		color = new float[]{0f, 0f, 1f};
    		_myWorld.setCellState((x+1) % (landscape.length-1), y, color);
    		_myWorld.setCellState(x, (y-1 + landscape.length-1) % (landscape.length-1), color);
    		_myWorld.setCellState(x, (y+1 + landscape.length-1) % (landscape.length-1), color);
    		
    		heightFactor = 32.0f; //64.0f; // was: 32.0f;
            heightBooster = 6.0; // default: 2.0 // 6.0 makes nice high mountains.
           
    		offset = -200.0f; // was: -40.
    		stepX = (-offset*2.0f) / dxView;
    		stepY = (-offset*2.0f) / dxView;
    		lenX = stepX / 2f;
    		lenY = stepY / 2f;
    		
            smoothFactor = new float[4];
            for ( int i = 0 ; i < 4 ; i++ )
            	smoothFactor[i] = 1.0f;
            
            smoothingDistanceThreshold = 30; //30;
            
        }
        
        /**
         * 
         */
        public static Landscape run(Landscape __landscape)
        {
    		caps = new GLCapabilities(null); //!n
    		//20210202-caps.setDoubleBuffered(true);  //!n
    		
    		final GLCanvas canvas = new GLCanvas(caps); // original
    		
            final Frame frame = new Frame("WE SHOULD PUT A COOL TITLE RIGHT? - World Of Cells");
            animator = new Animator(canvas);
            //Landscape myLandscape = new Landscape(dx,dy,myWorld);
            canvas.addGLEventListener(__landscape);
            canvas.addMouseListener(__landscape);// register mouse callback functions
            canvas.addKeyListener(__landscape);// register keyboard callback functions
            frame.add(canvas);
            frame.setSize(1024, 768);
            //frame.setSize(1280, 960);
            frame.setResizable(false);
            frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                            animator.stop();
                            frame.dispose();
                            System.exit(0);
                    }
            });
            frame.setVisible(true);
            //animator.setRunAsFastAsPossible(true); // GO FAST!  --- DOES It WORK? 
            animator.start();
            canvas.requestFocus();
            
            return __landscape;
        }
        
        

        /**
         * OpenGL Init method
         */
        //@Override
        public void init(GLAutoDrawable glDrawable) {
                GL2 gl = glDrawable.getGL().getGL2();

                // Enable front face culling (can speed up code, but is not always 
                // GO FAST ???
                
                // double buffer
                gl.glEnable(GL2.GL_DOUBLEBUFFER);
                glDrawable.setAutoSwapBufferMode(true);
                
                // Enable VSync
                // ? gl.setSwapInterval(1);
                // END of GO FAST ???


                gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClearDepth(1.0f);
                gl.glEnable(GL.GL_DEPTH_TEST);
                gl.glDepthFunc(GL.GL_LEQUAL);
                gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

                // Enables alpha color value
                gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
                gl.glEnable(gl.GL_BLEND);
                
                // Culling - display only triangles facing the screen
                //gl.glCullFace(GL.GL_FRONT);
                //gl.glEnable(GL.GL_CULL_FACE);

                // trucs d'alex
                gl.glEnable(GL.GL_DITHER);

        		weather.drawVolcano(gl);
                
        }
        
        
        /**
         * 
         */
        //@Override
        public void display(GLAutoDrawable gLDrawable) {
           
        		// ** compute FPS

        		if ( System.currentTimeMillis() - lastTimeStamp >= 1000 )
        		{
    				int fps = ( it - lastItStamp ) / 1;
       			
        			if ( Math.random() < 0.10 ) // display in console every ~10 updates
        			{
	        			System.out.print("frames per second  : "+fps+" ; ");
	        			System.out.println();
        			}
        			
        			lastItStamp = it;
        			lastTimeStamp = System.currentTimeMillis();
        			
        			lastFpsValue = fps;
        		}
        		
        		// ** clean screen
        		
        		final GL2 gl = gLDrawable.getGL().getGL2();
                gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();
                // ** display FPS on screen
                
                if ( DISPLAY_FPS )
                {
	                gl.glPushMatrix();

	                int xoff = 10;
	                int yoff = 20;
	                int y = 720;
	                gl.glColor3f(1f, 1f, 1f); // do this before calling glWindowsPos2d
	                gl.glWindowPos2d(xoff, y);
	                GLUT glut = new GLUT();
	                //gl.glTranslatef(0, 0, 0);
	                glut.glutBitmapString(GLUT.BITMAP_9_BY_15, "fps: " + lastFpsValue);
	                gl.glWindowPos2d(xoff, y - yoff);
	                glut.glutBitmapString(GLUT.BITMAP_9_BY_15, "x: " + movingX +" y:" + movingY + " z:" + movingZ);
	                gl.glWindowPos2d(xoff, y - 2*yoff);
	                glut.glutBitmapString(GLUT.BITMAP_9_BY_15, "time speed/condition : " + String.format("%.1f", getWeather().getTimeSpeed()*1000f) + "/" + getWeather().getCondition().toString());
	                if (DISPLAY_HELP) {
                        gl.glWindowPos2d(xoff, 30);
                        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10,
                            "           [b] toggle display info\t" +
                            "           [h] toggle help\t" +
                            "           [v] change view\t" +
                            "           [o] objects display on/off\t" +
                            "           [z] decrease altitude booster\t" +
                            "           [a] increase altitude booster\t");
                        gl.glWindowPos2d(xoff, 10);
                        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10,
                            "           [l/m] increase/decrease z-axis\t" +
                            "           [u/i] increase/decrase speed time\n" +
                            " [cursor keys] navigate in the landscape\n" +
                            "         [q/d] rotation with landscape\n" +
                            " [cursor keys] navigate\n");
                    }
	                gl.glPopMatrix();
	            }
				



        	
                // ** render all
               
                // *** ADD LIGHT
               
                if ( MY_LIGHT_RENDERING )
                {
	            	// Prepare light parameters.
	                float SHINE_ALL_DIRECTIONS = 1;
	                //float[] lightPos = {120.f, 120.f, -200.f, SHINE_ALL_DIRECTIONS};
	                //float[] lightPos = {40.f, 0.f, -300.f, SHINE_ALL_DIRECTIONS};
	                float[] lightPos = {0.f, 40.f, -100.f, SHINE_ALL_DIRECTIONS};
	                //float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
	                float[] lightColorAmbient = {0.5f, 0.5f, 0.5f, 1f};
	                float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};
	
	                // Set light parameters.
	                
	                gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
	                gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
	                gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
	                
	                // Enable lighting in GL.
	                gl.glEnable(GL2.GL_LIGHT1);
	                gl.glEnable(GL2.GL_LIGHTING);
                }

                
                // ***
                
                ////gl.glTranslatef(0.0f, 0.0f, -100.0f); // 0,0,-5
 
                // rotate on the three axis
                ////gl.glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
                ////gl.glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
                //gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
                
                // gl.glPushMatrix(); gl.glPopMatrix();
                
                /* DEBUG: as seen from above *
                gl.glTranslatef(0.0f, 0.0f, -500.0f); // 0,0,-5
                gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
                // DEBUG
                /**/

                /*
                // DEBUG
                gl.glTranslatef(0.0f, 0.0f, -500.0f); // 0,0,-5
                //gl.glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
                //gl.glRotatef(-90.f, 1.0f, 0.0f, 0.0f);
                gl.glRotatef(-90.f, 0.0f, 0.0f, 1.0f);
                // DEBUG
                /**/

                if ( VIEW_FROM_ABOVE == true )
                {
                	// as seen from above, no rotation (debug mode)
                	gl.glTranslatef(0.0f, 0.0f, -500.0f); // 0,0,-5
                }
                else
                {
                    // continuous rotation (default view) 
                    gl.glTranslatef(0.0f, -44.0f, -130.0f); // 0,0,-5
                    gl.glRotatef(rotateX, 0.0f, 1.0f, 0.0f);
                    gl.glRotatef(-90.f, 1.0f, 0.0f, 0.0f);
                }
                
                //System.out.println("rotateT = " + rotateT );
                
                it++;
                //if ( it % 30 == 0 )//&& it != 0)
                //	movingIt++;
                //movingIt=0;
                
                //movingIt=dxView+1;
                
        		// ** update Cellular Automata
            	
            	_myWorld.step();
            	weather.step();

        		// ** draw everything

        		weather.drawSky(gl);

            	gl.glBegin(GL2.GL_QUADS);                
                
                //movingX = movingIt;// it; // was: it
                //movingY = 0; // was: it
                
                for ( int x = 0 ; x < dxView-1 ; x++ )
                	for ( int y = 0 ; y < dyView-1 ; y++ )
                	{
           			 	
		                double height = _myWorld.getCellHeight(x+movingX,y+movingY);
           			 	int cellState = _myWorld.getCellValue(x+movingX,y+movingY);	
           			 	float[] color = _myWorld.getCellColorValue(x+movingX,y+movingY);

	                	// compute CA-based coloring

                        gl.glColor3f(color[0],color[1],color[2]);
                        
                        // * if light is on
	                	if ( MY_LIGHT_RENDERING )
                        {
	                        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, color, 0 );
	                        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, color, 0 );
	                        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, color, 0 );
	                        gl.glMateriali( GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 4 );
	                        float[] colorBlack  = {0.0f,0.0f,0.0f,1.0f};
	                        gl.glMaterialfv( GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION, colorBlack, 0 );
                        }
                        
                        // Border visual smoothing : smooth altitudes near border (i.e. nicer rendering)
                        if ( SMOOTH_AT_BORDER == true && VIEW_FROM_ABOVE != true)
                        {
	                        if ( Math.min(Math.min(x, dxView-x-1),Math.min(y, dyView-y-1)) < smoothingDistanceThreshold )
	                        {

	                            for ( int i = 0 ; i < 4 ; i++ )
	                            {
	                            	int xIt = i==1||i==2?1:0;
	                            	int yIt = i==0||i==1?1:0;
	                            	float xSign = i==1||i==2?1.f:-1.f;
	                            	float ySign = i==0||i==1?1.f:-1.f;
	                            	
		                        	smoothFactor[i] = (float)
		                        			Math.min(
		                        					Math.min( 1.0 , (double)Math.min(x+xIt,dxView-x+xIt)/(double)smoothingDistanceThreshold ) ,  // check x-axis
		                        					Math.min( 1.0 , (double)Math.min(y+yIt,dyView-y+yIt)/(double)smoothingDistanceThreshold )    // check y-axis
		                        					);
	                            }	                            	
	                        }
	                        else
	                        {
	                        	for ( int i = 0 ; i < 4 ; i++ )
	                        		smoothFactor[i] = 1.0f;
	                        }
                        }

                        // use dxCA instead of dxView to keep synchronization with CA states
                        
                        for ( int i = 0 ; i < 4 ; i++ )
                        {
                        	int xIt = i==1||i==2?1:0;
                        	int yIt = i==0||i==1?1:0;
                        	float xSign = i==1||i==2?1.f:-1.f;
                        	float ySign = i==0||i==1?1.f:-1.f;
                        	
                            float zValue = 0.f;

                        	if ( VIEW_FROM_ABOVE == false )
	                        {
	                        	//double altitude = landscape[(x+xIt+movingX)%(dxCA][(y+yIt+movingY)%dyCA] * heightBooster;
	                        	double altitude = landscape[(x+xIt+movingX)%(dxView-1)][(y+yIt+movingY)%(dyView-1)] * heightBooster;
	                        	if ( altitude < 0 ) 
	                        		zValue = 0;
	                        	else
	                        		zValue = heightFactor*(float)altitude * smoothFactor[i];
	                        }
	                        
	                        gl.glVertex3f( offset+x*stepX+xSign*lenX, offset+y*stepY+ySign*lenY, zValue + movingZ);
                        }

                        /**/
                        
                        // * display objects

                        // TODO+: diplayObjectAt(x,y,...) ==> on y gagne quoi? les smoothFactors. C'est tout. Donc on externalise?
                        
                        if ( DISPLAY_OBJECTS == true) // calls my world with the enough info to display anything at this location.
                        {
                        	float normalizeHeight = ( smoothFactor[0] + smoothFactor[1] + smoothFactor[2] + smoothFactor[3] ) / 4.f * (float)heightBooster * heightFactor;
                        	_myWorld.displayObjectAt(_myWorld,gl,cellState, x, y, height, offset, stepX, stepY, lenX, lenY, normalizeHeight);
                        }
                	}
	            
	            /**/
	            
	            // TODO+: displayObjects()
	            
	            if ( DISPLAY_OBJECTS == true) // calls my world with enough info to display anything anywhere
                { 
	            	float normalizeHeight = (float)heightBooster * heightFactor;
	            	_myWorld.displayUniqueObjects(_myWorld,gl,movingX,movingY,offset,stepX,stepY,lenX,lenY,normalizeHeight);
	            }

	            gl.glEnd();

                // increasing rotation for the next iteration                   
                rotateX += rotationVelocity;
                //gl.glFlush(); // GO FAST ???
            	//gLDrawable.swapBuffers(); // GO FAST ???  // should be done at the end (http://stackoverflow.com/questions/1540928/jogl-double-buffering)
            	

        }


        public int getZOffset() { return movingZ; }

        public Weather getWeather() { return weather; }

		public void initAgents()	{

			//_myWorld.displayHeightValues();		//Just testing

			PoolPredator predators = _myWorld.getPredators();
			PoolPrey prey = _myWorld.getPrey();
			//_myWorld.test();
			ArrayList<Plant> plants = _myWorld.getPlants();  //???

			for (int x=0; x<_myWorld.getWidth(); x++)	{
				for (int y=0; y<_myWorld.getHeight(); y++)	{
					if (_myWorld.getCellHeight(x,y) > WorldOfTrees.WATER_LEVEL)	{
						double dice = Math.random();
						if (dice < INITIAL_PLANT_DENSITY && _myWorld.getCellHeight(x,y) > 0 && _myWorld.getCellHeight(x,y) < WorldOfTrees.TREE_LINE )	{
							//plants.add(new Pineapple(x,y,_myWorld));
							plants.add(new Mushroom(x,y,_myWorld));
						}
						else if (dice < INITIAL_PLANT_DENSITY + INITIAL_PREDATOR_DENSITY && _myWorld.getCellHeight(x,y) < WorldOfTrees.TREE_LINE)	{
							predators.add(x,y,_myWorld);
						}
						else if (dice < INITIAL_PLANT_DENSITY + INITIAL_PREDATOR_DENSITY + INITIAL_PREY_DENSITY && _myWorld.getCellHeight(x,y) < WorldOfTrees.TREE_LINE)	{
							//System.out.println("myWorld"+_myWorld.getClass().getName());
							prey.add(x,y,_myWorld);
						}						
					}
				}
			}
		}
        
        /**
         * 
         */
        //@Override
        public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        		if ( this.it == 0 )
		        	System.out.println( "World Of Cells by Jarski Paul and Sok Chanattan 2022 - based on Nicolas Bredeche's version of 2013");
        		GL2 gl = gLDrawable.getGL().getGL2();
                final float aspect = (float) width / (float) height;
                gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                gl.glLoadIdentity();
                final float fh = 0.5f;
                final float fw = fh * aspect;
                gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
                gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                gl.glLoadIdentity();
        }
 
        
        /**
         * 
         */
        //@Override
        public void dispose(GLAutoDrawable gLDrawable) {
        }
 

      
        
        /**
         * 
         * @param args
         */
        /*
		public static void main(String[] args) {

        	initLandscape(200,200, new WorldOfTrees());

        }
        */


		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		  public void mousePressed(MouseEvent mouse)
		  {
			/* example from doublebuf.java
		    switch (mouse.getButton()) {
		      case MouseEvent.BUTTON1:
		        spinDelta = 2f;
		        break;
		      case MouseEvent.BUTTON2:
		      case MouseEvent.BUTTON3:
		        spinDelta = 0f;
		        break;
		    }
		    /**/
		  }

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void keyPressed(KeyEvent key) {
			switch (key.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				new Thread()
				{
					public void run() { animator.stop();}
				}.start();
				System.exit(0);
				break;
			case KeyEvent.VK_V:
				VIEW_FROM_ABOVE = !VIEW_FROM_ABOVE ;
				break;
			case KeyEvent.VK_R:
				MY_LIGHT_RENDERING = !MY_LIGHT_RENDERING;
				break;
			case KeyEvent.VK_O:
				DISPLAY_OBJECTS = !DISPLAY_OBJECTS;
				break;
			case KeyEvent.VK_A:
				heightBooster++;
				break;
			case KeyEvent.VK_Z:
				if ( heightBooster > 0 )
					heightBooster--;
				break;
            case KeyEvent.VK_L:
                movingZ += 1;
                break;
            case KeyEvent.VK_M:
                movingZ -= 1;
                break;
			case KeyEvent.VK_UP:
				movingX = ( movingX + 1 ) % (dxView-1);
				break;
			case KeyEvent.VK_DOWN:
				movingX = ( movingX - 1 + dxView-1 ) % (dxView-1);
				break;
			case KeyEvent.VK_RIGHT:
				movingY = ( movingY - 1 + dyView-1 ) % (dyView-1);
				break;
			case KeyEvent.VK_LEFT:
				movingY = ( movingY + 1 ) % (dyView-1);
				break; 
			case KeyEvent.VK_Q:
				rotationVelocity-=0.1;
				break;
			case KeyEvent.VK_D:
				rotationVelocity+=0.1;
				break;
            case KeyEvent.VK_U:
                weather.setTimeSpeed(weather.getTimeSpeed() + 0.0001f);
                break;
            case KeyEvent.VK_I:
                weather.setTimeSpeed(weather.getTimeSpeed() - 0.0001f);
                break;
            case KeyEvent.VK_B:
                DISPLAY_FPS = !DISPLAY_FPS;
                break;
            case KeyEvent.VK_X:
                System.out.println("On volcano ? " + weather.onVolcano(movingX, movingY));
                break;
            case KeyEvent.VK_H:
                DISPLAY_HELP = !DISPLAY_HELP;
			/*case KeyEvent.VK_H:
				System.out.println(
						"Help:\n" +
						"           [v] change view\n" +
						"           [o] objects display on/off\n" +
						"           [z] decrease altitude booster\n" +
						"           [a] increase altitude booster\n" +
						"           [l/m] increase/decrease z-axis\n" +
						"           [u/i] increase/decrase speed time\n" +
						" [cursor keys] navigate in the landscape\n" +
						"         [q/d] rotation wrt landscape\n" +
						" [cursor keys] navigate\n"
						);
				break;*/
			default:
				break;
			}
		}


		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
}
