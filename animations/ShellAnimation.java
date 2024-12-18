public class ShellAnimation implements Animation {
	private Universe current = new tankmaplayout();
	private boolean universeSwitched = false;
	private boolean animationComplete = false;
	private int universeCount = 0;
	
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
		if ( KeyboardInput.getKeyboard().keyDownOnce(114)) {
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
		
		System.out.println(current);
		if(universeCount == 1 && current.getKillCount() == 6) {
			universeCount = 2;
			this.universeSwitched = true;
		}
		
	}
	public Universe switchUniverse(Object event) {
		animationComplete = true;
		return current;
	}
	
	public int getUniverseCount() {
		return this.universeCount;
	}
	
}
