package com.revenco.daemon.natives;

import android.content.Context;

/**
 * natives base class
 * 
 * @author Mars
 *
 */
public class NativeDaemonBase {
	/**
	 * used for natives
	 */
	protected 	Context			mContext;
	
    public NativeDaemonBase(Context context){
        this.mContext = context;
    }

    /**
     * natives call back
     */
	protected void onDaemonDead(){
		IDaemonStrategy.Fetcher.fetchStrategy().onDaemonDead();
    }
    
}
