package com.lonevox.renewableghasttears;

public interface GhastEntityAccessor {
	boolean isCrying();

	void setCrying(boolean crying);

	/**
	 * Makes the Ghast cry a Ghast Tear if the right conditions are met.
	 */
	void tryCryGhastTear();
}
