import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseMotionAdapter;

/*
 * This class represents the 'graphical user interface' or 'presentation' layer or 'frame'. Its job is to continuously 
 * read input from the user (i.e. keyboard, mouse) and to render (draw) a universe or 'logical' layer. Also, it
 * continuously prompts the logical layer to update itself based on the number of milliseconds that have elapsed.
 * 
 * The presentation layer generally does not try to affect the logical layer; most information
 * passes "upwards" from the logical layer to the presentation layer.
 */

public class AnimationFrame extends JFrame {

	final public static int FRAMES_PER_SECOND = 60;
	protected long REFRESH_TIME = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS

	protected static int SCREEN_HEIGHT = 1000;
	protected static int SCREEN_WIDTH = 1000;

	//These variables control where the screen is centered in relation to the logical center of universe.
	//Generally it makes sense to have these start at half screen width and height, so that the logical
	//center is rendered in the center of the screen. Changing them will 'pan' the screen.
	protected int screenOffsetX = SCREEN_WIDTH / 2;
	protected int screenOffsetY = SCREEN_HEIGHT / 2;

	
	//scale at which to render the universe. When 1, each logical unit represents 1 pixel in both x and y dimension
	protected double scale = 1;
	//point in universe on which the screen will center
	protected double logicalCenterX = 0;		
	protected double logicalCenterY = 0;

	//basic controls on interface... these are protected so that subclasses can access
	protected JPanel panel = null;
	protected JButton btnPauseRun;
	protected JLabel redScoreLabel;
	protected JLabel greenScoreLabel;
	protected JLabel yellowScoreLabel;
	protected JLabel purpleScoreLabel;

	protected static boolean stop = false;

	protected long total_elapsed_time = 0;
	protected long lastRefreshTime = 0;
	protected long deltaTime = 0;
	protected boolean isPaused = false;

	protected KeyboardInput keyboard = KeyboardInput.getKeyboard();
	protected Universe universe = null;

	//local (and direct references to various objects in universe ... should reduce lag by avoiding dynamic lookup
	protected Animation animation = null;
	protected ArrayList<DisplayableSprite> sprites = null;
	protected ArrayList<Background> backgrounds = null;
	protected Background background = null;
	
	/*
	 * Much of the following constructor uses a library called Swing to create various graphical controls. You do not need
	 * to modify this code to create an animation, but certainly many custom controls could be added.
	 */
	public AnimationFrame(Animation animation)
	{
		super("");
		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				thisContentPane_mousePressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				thisContentPane_mouseReleased(e);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				contentPane_mouseExited(e);
			}
		});
		
		this.animation = animation;
		this.setVisible(true);		
		this.setFocusable(true);
		this.setSize(SCREEN_WIDTH + 20, SCREEN_HEIGHT + 36);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				contentPane_mouseMoved(e);
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				contentPane_mouseMoved(e);
			}
		});

		Container cp = getContentPane();
		cp.setBackground(Color.BLACK);
		cp.setLayout(null);

		panel = new AnimationPanel();
		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		getContentPane().add(panel, BorderLayout.CENTER);

		redScoreLabel = new JLabel("Red: ");
		redScoreLabel.setForeground(Color.RED);
		redScoreLabel.setFont(new Font("Consolas", Font.BOLD, 50));
		redScoreLabel.setBounds(16, SCREEN_HEIGHT - 100, SCREEN_WIDTH - 5, 50);
		getContentPane().add(redScoreLabel);
		getContentPane().setComponentZOrder(redScoreLabel, 0);
		
		greenScoreLabel = new JLabel("Green: ");
		greenScoreLabel.setForeground(Color.GREEN);
		greenScoreLabel.setFont(new Font("Consolas", Font.BOLD, 50));
		greenScoreLabel.setBounds(220, SCREEN_HEIGHT - 100, SCREEN_WIDTH - 5, 50);
		getContentPane().add(greenScoreLabel);
		getContentPane().setComponentZOrder(greenScoreLabel, 0);
		
		yellowScoreLabel = new JLabel("Yellow: ");
		yellowScoreLabel.setForeground(Color.YELLOW);
		yellowScoreLabel.setFont(new Font("Consolas", Font.BOLD, 50));
		yellowScoreLabel.setBounds(470, SCREEN_HEIGHT - 100, SCREEN_WIDTH - 5, 50);
		getContentPane().add(yellowScoreLabel);
		getContentPane().setComponentZOrder(yellowScoreLabel, 0);
		
		purpleScoreLabel = new JLabel("Purple: ");
		purpleScoreLabel.setForeground(Color.MAGENTA);
		purpleScoreLabel.setFont(new Font("Consolas", Font.BOLD, 50));
		purpleScoreLabel.setBounds(742, SCREEN_HEIGHT - 100, SCREEN_WIDTH - 5, 50);
		getContentPane().add(purpleScoreLabel);
		getContentPane().setComponentZOrder(purpleScoreLabel, 0);
		
		
		


	}

	/* 
	 * The entry point into an Animation. The presentation (gui) and the logical layers should run on separate
	 * threads. This allows the presentation layer to remain responsive to user input while the logical is updating
	 * its state. The universe (a.k.a. logical) thread is created below.
	 */	
	public void start()
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				animationLoop();
				System.out.println("run() complete");
			}
		};

		thread.start();
		animationInitialize();
				
		System.out.println("main() complete");

	}

	
	
	/*
	 * You can add code for displaying gui elements when the animation initializes using a JDialog or similar
	 * 
	 * Note that this method runs runs asynchronous with the instantiation of the animation loop, which may
	 * take significant time. Thus, this is an ideal place for a title screen
	 */
	protected void animationInitialize() {
		
	}
	
	/*
	 * You can add code for displaying gui elements when the animation starts using a JDialog or similar
	 * 
	 * Note that this method runs runs synchronous within the animation thread. Thus, if the a modal dialogue
	 * is shown, the animation will not start, but a non-modal dialogue will appear alongside the animation
	 */
	protected void animationStart() {		
	}
	
	/*
	 * You can add code for displaying gui elements when there is a switch in universe (e.g. a next level dialogue)
	 */
	
	
	protected void universeSwitched() {
	}
	
	/*
	 * You can add code for displaying gui elements when the animation is fully complete (e.g. display a high score screen)
	 */
	protected void animationEnd() {
		
	}

	private void setLocalObjectVariables() {
		/*
		 * the following code is added to improve performance...
		 * by creating local object variables to the current universe / backgrounds / sprites, there is less redirection
		 * 
		 */
		universe = animation.getCurrentUniverse();
		sprites = universe.getSprites();
		backgrounds = universe.getBackgrounds();
		this.scale = universe.getScale();
	}
	
	
	/*
	 * You can overload this method to add additional rendering logic 
	 */
	protected void paintAnimationPanel(Graphics g) {
		
	}
	
	/*
	 * The animationLoop runs on the logical thread, and is only active when the universe needs to be
	 * updated. There are actually two loops here. The outer loop cycles through all universes as provided
	 * by the animation. Whenever a universe is 'complete', the animation is asked for the next universe;
	 * if there is none, then the loop exits and this method terminates
	 * 
	 * The inner loop attempts to update the universe regularly, whenever enough milliseconds have
	 * elapsed to move to the next 'frame' (i.e. the refresh rate). Once the universe has updated itself,
	 * the code then moves to a rendering phase where the universe is rendered to the gui and the
	 * controls updated. These two steps may take several milliseconds, but hopefully no more than the refresh rate.
	 * When the refresh has finished, the loop (and thus the thread) goes to sleep until the next
	 * refresh time. 
	 */
	private void animationLoop() {

		lastRefreshTime = System.currentTimeMillis();		
		universe = animation.getCurrentUniverse();

		animationStart();
		
		/* 
		 * outer game loop, which will run until stop is signaled or the animation is complete
		 */
		while (stop == false && animation.isComplete() == false) {
			
			//before the next universe is animated, allow other actions to take place
			universeSwitched();
			// the gui needs to acknowledge that the universe switch was detected
			// so that the gui can reset the switched flag
			animation.acknowledgeUniverseSwitched();			
			setLocalObjectVariables();
			//(re)set the keyboard to current state
			keyboard.reset();
			keyboard.poll();
			
			// inner game loop which will animate the current universe until stop is signaled or until the universe is complete / switched
			while (stop == false && animation.isComplete() == false && universe.isComplete() == false && animation.getUniverseSwitched() == false) {
				

				//adapted from http://www.java-gaming.org/index.php?topic=24220.0
				long target_wake_time = System.currentTimeMillis() + REFRESH_TIME;
				//sleep until the next refresh time
				while (System.currentTimeMillis() < target_wake_time)
				{
					//allow other threads (i.e. the Swing thread) to do its work
					Thread.yield();

					try {
						Thread.sleep(1);
					}
					catch(Exception e) {    					
					} 

				}


				//track time that has elapsed since the last update, and note the refresh time
				deltaTime = (isPaused ? 0 : System.currentTimeMillis() - lastRefreshTime);
				lastRefreshTime = System.currentTimeMillis();
				total_elapsed_time += deltaTime;
				
				//read input
				keyboard.poll();

				//update logical
				animation.update(this, deltaTime);
				universe.update(animation, deltaTime);

				
				//update interface
				this.logicalCenterX = universe.getXCenter();
				this.logicalCenterY = universe.getYCenter();
				
				this.updateScoreText();
				

				this.repaint();
				
				((ShellAnimation)animation).backgroundMusic();

			}
			
		}

		System.out.println("animation complete");
		AudioPlayer.setStopAll(true);
		dispose();	

	}



	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
			this.btnPauseRun.setText("||");
		}
		else {
			isPaused = true;
			this.btnPauseRun.setText(">");
		}
	}



	/*
	 * This method will run whenever the universe needs to be rendered. The animation loop calls it
	 * by invoking the repaint() method.
	 * 
	 * The work is reasonably simple. First, all backgrounds are rendered from "furthest" to "closest"
	 * Then, all sprites are rendered in order. Observe that the logical coordinates are continuously
	 * being translated to screen coordinates. Thus, how the universe is rendered is determined by
	 * the gui, but what is being rendered is determined by the universe. In other words, a sprite may
	 * be in a given logical location, but where it is rendered also depends on scale and camera placement
	 */
	class AnimationPanel extends JPanel {

		public void paintComponent(Graphics g)
		{	
						
			if (universe == null) {
				return;
			}

			if (backgrounds != null) {
				for (Background background: backgrounds) {
					paintBackground(g, background);
				}
			}

			if (sprites != null) {
				for (DisplayableSprite activeSprite : sprites) {
					DisplayableSprite sprite = activeSprite;
					if (sprite.getVisible()) {
						if (sprite.getImage() != null) {
							g.drawImage(sprite.getImage(), translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()), null);
						}
						else {
							g.setColor(Color.BLUE);
							g.fillRect(translateToScreenX(sprite.getMinX()), translateToScreenY(sprite.getMinY()), scaleLogicalX(sprite.getWidth()), scaleLogicalY(sprite.getHeight()));
						}
					}
				}				
			}
			
			

			paintAnimationPanel(g);
			
		}
		
		/*
		 * The algorithm for rendering a background may appear complex, but you can think of it as
		 * 'tiling' the screen from top left to bottom right. Each time, the gui determines a screen coordinate
		 * that has not yet been covered. It then asks the background (which is part of the universe) for the tile
		 * that would cover the equivalent logical coordinate. This tile has height and width, which allows
		 * the gui to draw the tile and to then move to the screen coordinate at the same minY and to the right of this tile.
		 * Again, the background is asked for the tile that would cover this coordinate.
		 * When eventually this coordinate is off the right hand edge of the screen, then move to the left of the screen
		 * but below the previously drawn tile. Repeat until the entire panel is covered.
		 */
		private void paintBackground(Graphics g, Background background) {
			
			if ((g == null) || (background == null)) {
				return;
			}
			
			//what tile covers the top-left corner?
			double logicalLeft = (logicalCenterX  - (screenOffsetX / scale) - background.getShiftX());
			double logicalTop =  (logicalCenterY - (screenOffsetY / scale) - background.getShiftY()) ;
						
			int row = background.getRow((int)(logicalTop - background.getShiftY() ));
			int col = background.getCol((int)(logicalLeft - background.getShiftX()  ));
			Tile tile = background.getTile(col, row);
			
			boolean rowDrawn = false;
			boolean screenDrawn = false;
			while (screenDrawn == false) {
				while (rowDrawn == false) {
					tile = background.getTile(col, row);
					if (tile.getWidth() <= 0 || tile.getHeight() <= 0) {
						//no increase in width; will cause an infinite loop, so consider this screen to be done
						g.setColor(Color.GRAY);
						g.fillRect(0,0, SCREEN_WIDTH, SCREEN_HEIGHT);					
						rowDrawn = true;
						screenDrawn = true;						
					}
					else {
						Tile nextTile = background.getTile(col+1, row+1);
						int width = translateToScreenX(nextTile.getMinX()) - translateToScreenX(tile.getMinX());
						int height = translateToScreenY(nextTile.getMinY()) - translateToScreenY(tile.getMinY());
						g.drawImage(tile.getImage(), translateToScreenX(tile.getMinX() + background.getShiftX()), translateToScreenY(tile.getMinY() + background.getShiftY()), width, height, null);
					}					
					//does the RHE of this tile extend past the RHE of the visible area?
					if (translateToScreenX(tile.getMinX() + background.getShiftX() + tile.getWidth()) > SCREEN_WIDTH || tile.isOutOfBounds()) {
						rowDrawn = true;
					}
					else {
						col++;
					}
				}
				//does the bottom edge of this tile extend past the bottom edge of the visible area?
				if (translateToScreenY(tile.getMinY() + background.getShiftY() + tile.getHeight()) > SCREEN_HEIGHT || tile.isOutOfBounds()) {
					screenDrawn = true;
				}
				else {
					col = background.getCol(logicalLeft);
					row++;
					rowDrawn = false;
				}
			}
		}				
	}

	protected int translateToScreenX(double logicalX) {
		return screenOffsetX + scaleLogicalX(logicalX - logicalCenterX);
	}		
	protected int scaleLogicalX(double logicalX) {
		return (int) Math.round(scale * logicalX);
	}
	protected int translateToScreenY(double logicalY) {
		return screenOffsetY + scaleLogicalY(logicalY - logicalCenterY);
	}		
	protected int scaleLogicalY(double logicalY) {
		return (int) Math.round(scale * logicalY);
	}

	protected double translateToLogicalX(int screenX) {
		double offset = screenX - screenOffsetX;
		return (offset / scale) + (universe != null ? universe.getXCenter() : 0);
	}
	protected double translateToLogicalY(int screenY) {
		double offset = screenY - screenOffsetY ;
		return (offset / scale) + (universe != null ? universe.getYCenter() : 0);		
	}
	
	protected void contentPane_mouseMoved(MouseEvent e) {
		Point point = this.getContentPane().getMousePosition();
		if (point != null) {
			MouseInput.screenX = point.x;		
			MouseInput.screenY = point.y;
			MouseInput.logicalX = translateToLogicalX(MouseInput.screenX);
			MouseInput.logicalY = translateToLogicalY(MouseInput.screenY);
		}
		else {
			MouseInput.screenX = -1;		
			MouseInput.screenY = -1;
			MouseInput.logicalX = Double.NaN;
			MouseInput.logicalY = Double.NaN;
		}
	}
	
	protected void thisContentPane_mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			MouseInput.leftButtonDown = true;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			MouseInput.rightButtonDown = true;
		} else {
			//DO NOTHING
		}
	}
	protected void thisContentPane_mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			MouseInput.leftButtonDown = false;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			MouseInput.rightButtonDown = false;
		} else {
			//DO NOTHING
		}
	}

	protected void this_windowClosing(WindowEvent e) {
		System.out.println("windowClosing()");
		stop = true;
		dispose();	
	}
	protected void contentPane_mouseExited(MouseEvent e) {
		contentPane_mouseMoved(e);
	}
	
	
	protected void updateScoreText() {
		if(this.universe instanceof MainUniverse) {
			int[] winCount = ((ShellAnimation)this.animation).getWinCount();
			
			this.redScoreLabel.setVisible(true);
			this.redScoreLabel.setText("RED:" + winCount[0]);
			
			this.greenScoreLabel.setVisible(true);
			this.greenScoreLabel.setText("GREEN:" + winCount[2]);
			
			this.yellowScoreLabel.setVisible(true);
			this.yellowScoreLabel.setText("YELLOW:" + winCount[1]);
			
			this.purpleScoreLabel.setVisible(true);
			this.purpleScoreLabel.setText("PURPLE:" + winCount[3]);
		} else {
			this.redScoreLabel.setVisible(false);
			this.greenScoreLabel.setVisible(false);
			this.yellowScoreLabel.setVisible(false);
			this.purpleScoreLabel.setVisible(false);;
		}
	}
}