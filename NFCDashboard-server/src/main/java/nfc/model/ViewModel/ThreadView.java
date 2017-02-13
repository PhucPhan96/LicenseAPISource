package nfc.model.ViewModel;
import nfc.model.Thread;

public class ThreadView {
	private Thread thread = new Thread();
	private Thread threadSmall;
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	public Thread getThreadSmall() {
		return threadSmall;
	}
	public void setThreadSmall(Thread threadSmall) {
		this.threadSmall = threadSmall;
	}
}
