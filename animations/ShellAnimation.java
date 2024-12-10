public class ShellAnimation implements Animation {
	private Universe current = new tankmaplayout();
	private boolean universeSwitched = false;
	private boolean animationComplete = false;
	private int universeCount = 0;
	
	public Universe getCurrentUniverse() {
		if(universeCount == 0) {
			return new tankmaplayout();
		} else{
			return new MainUniverse();
		}
		
		
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
		if ( KeyboardInput.getKeyboard().keyDownOnce(27)) {
			if(this.universeCount < 1 && animationComplete == false) {
				universeCount++;
				this.universeSwitched = true;
			} else {
				animationComplete = true;
			}
			
		}
		
	}
	public Universe switchUniverse(Object event) {
		animationComplete = true;
		return current;
	}
	
}
