package com.pwdgame.tasks;


import android.content.Context;

import com.pwdgame.util.Utility;

/**
 * 本类继承了AsyncMockTask，实现了类似于AsyncTask的任务机制
 * 通过传入AsyncTaskInterface<Result> 接口对象，实现更方便的任务的调度
 * 
 * 这里执行dobackground任务增加了网络检查
 * @author admin
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class AsyncTaskWrapper<Params, Progress, Result> extends AsyncMockTask<Params, Progress, Result>
{

	protected TException exception;//a
	protected Context context;//b
	private AsyncTaskInterface<Result> ac;

	public AsyncTaskWrapper(AsyncTaskInterface<Result> ac1, Context context)
	{
		this.exception = null;
		this.ac = ac1;
		this.context = context;
	}

	protected void onPreExecute()
	{
		if (this.ac != null)
			this.ac.onPreExecute();
	}
	
	protected void onPostExecute(Result obj)
	{
		if (this.ac != null)
			this.ac.onPostExecute(obj, this.exception);
		Runtime.getRuntime().gc();
	}

	protected void onProgressUpdate(Progress... values)
	{
	}

	protected Result doInBackground(Params... aobj)
	{
		//当网络不可用
		if (!Utility.checkNetWork(this.context))
			this.exception = new TException(4, "");
		if (this.ac != null)
			this.ac.doInBackground();
		return null;
	}

	protected void onCancelled()
	{
		if (this.ac != null)
			this.ac.onCancelled();
	}
}