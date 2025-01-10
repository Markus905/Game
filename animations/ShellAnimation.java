import java.util.Timer;
import java.util.TimerTask;

public class ShellAnimation implements Animation {
	private Universe current = new tankmaplayout();
	private boolean universeSwitched = false;
	private boolean animationComplete = false;
	private int universeCount = 0;
	
	private int[] winCount = new int[4]; //tracks wins [red, yellow, green, purple]
	
	private AudioPlayer backgroundMusic = new AudioPlayer();
	
	Timer timer = new Timer();
    private boolean isTimerActive = false;  // To ensure only one timer is active at a time

	public Universe getCurrentUniverse() {
		if(universeCount == 0) {
			this.current = new tankmaplayout();
		} else if(universeCount == 1){
			this.current = new MainUniverse();
		} else {
			this.current = new EmptyUniverse();
		}
		
		return current;
		
		
	}
	
	
	@Override
	public boolean getUniverseSwitched() {
		return universeSwitched;
	}
	@Override
	public void acknowledgeUniverseSwitched() {
		this.universeSwitched = false;		
	}
	@Override
	public boolean isComplete() {
		return animationComplete;
	}
	@Override
	public void setComplete(boolean complete) {
		this.animationComplete = true;		
	}
	@Override
	public void update(AnimationFrame frame, long actual_delta_time) {	
		
		
		
		if(universeCount == 1) {
			int[] killTracker = ((MainUniverse)current).getKillTracker();
			if(checkGameOver(killTracker) && !isTimerActive) {
				  isTimerActive = true;// Mark timer as active
				  ((MainUniverse)current).gameOver();
				 
				  if (killTracker[0] == 0) {
					  this.winCount[0]++;
				  }else if(killTracker[1] == 0) {
					  this.winCount[1]++;
				  } else if(killTracker[2] == 0) {
					  this.winCount[2]++;
				  } else {
					  this.winCount[3]++;
				  }
				  
		            startSwitchTimer(); 
			}
		}
		
		
		if ( KeyboardInput.getKeyboard().keyDownOnce(16)) {
			if(this.universeCount < 1 && animationComplete == false) {
				this.universeCount++;
				this.universeSwitched = true;
			} else if(this.universeCount > 1 && animationComplete == false) {
				this.universeCount--;
				this.universeSwitched = true;
			}	
		} else if(KeyboardInput.getKeyboard().keyDownOnce(27)) {
			this.animationComplete = true;
		}
		

	}
	
	static boolean checkGameOver(int[] killTracker) {
		int count = 0;
		for(int num : killTracker) {
			if (num == 0) {
				count++;
			}
			if(count > 1) {
				return false;
				//checks if more than one player is alive and returns false if true
				//by doing it this way it makes it so it doesn't have to go through the whole array each time
			}
		}
		return true;
	}
	
	

    private void startSwitchTimer() {
        timer.schedule(new TimerTask() {
            public void run() {
                // Switch universe after 2 seconds
                universeCount = 2; 
                universeSwitched = true;
                isTimerActive = false;  // Reset the timer status after switch
            }
        }, 5000);  
    }
	public Universe switchUniverse(Object event) {
		animationComplete = true;
		return current;
	}

	public void backgroundMusic() {
		if(backgroundMusic.isPlayCompleted()) {
			backgroundMusic.playAsynchronous("res/audio/backgroundMusic.wav");
		}
		
	}
	
	public int[] getWinCount() {
		return this.winCount;
	}

	
	
	
}
