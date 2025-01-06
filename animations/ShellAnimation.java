import java.util.Timer;
import java.util.TimerTask;

public class ShellAnimation implements Animation {
	private Universe current = new tankmaplayout();
	private boolean universeSwitched = false;
	private boolean animationComplete = false;
	private int universeCount = 0;
	
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
		
		if(universeCount == 1 && current.getKillCount() == 6 && !isTimerActive) {
			  isTimerActive = true;  // Mark timer as active
	            startSwitchTimer(); 
		}
		
	}
	
	

    private void startSwitchTimer() {
        timer.schedule(new TimerTask() {
            public void run() {
                // Switch universe after 2 seconds
                universeCount = 2; 
                universeSwitched = true;
                isTimerActive = false;  // Reset the timer status after switch
            }
        }, 2000);  
    }
	public Universe switchUniverse(Object event) {
		animationComplete = true;
		return current;
	}
	
	public int getUniverseCount() {
		return this.universeCount;
	}
	
}
