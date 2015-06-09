package com.pwdgame.tasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
/**
 * AsyncTask<Params, Progress, Result> 模型实现
 * 执行顺序为：
 * onPreExecute();
 * doInBackground((Params[])this.mParams);
 * onPostExecute(paramResult);
 * 
 * 因为15版本才开始有AsyncTask,这里就实现了它以兼容低版本
 * @author admin
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
//“启动任务执行的输入参数”、“后台任务执行的进度”、“后台计算结果的类型”。
public abstract class AsyncMockTask<Params, Progress, Result>
{
	private static final String LOG_TAG = "AsyncMockTask";
	
	  private static final int CORE_POOL_SIZE = 50;
	  private static final int MAXIMUM_POOL_SIZE = 100;
	  private static final long KEEP_ALIVE = 10;
	  
	  private static final int MESSAGE_POST_CANCEL = 3;
	  private static final int MESSAGE_POST_PROGRESS = 2;
	  private static final int MESSAGE_POST_RESULT = 1;
	  
	  private static final ThreadPoolExecutor sExecutor;
	  private static final InternalHandler sHandler;
	  private static final ThreadFactory sThreadFactory;
	  private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(50);
	  
	  /*
	   * 要执行的任务主体
	   */
	  private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>()
	  {
		  //计算结果，则抛出一个异常，如果无法做到这一点。
	    public Result call()
	      throws Exception
	    {
	      Process.setThreadPriority(10);
	      //执行任务，返回结果Result
	      return AsyncMockTask.this.doInBackground((Params[])this.mParams);
	    }
	  };
	  /**
	   * 一个FutureTask可用于包装可赎回或Runnable对象。
	   * 因为FutureTask实现了Runnable，一个FutureTask可以提交给一个Executor执行。
	   * 执行this.mWorker.call 里的doInBackground((Params[])this.mParams);
	   */
	  private final FutureTask<Result> mFuture = new FutureTask<Result>(this.mWorker)
	  {
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		protected void done()
	    {
	      Object localObject1 = null;
	      try
	      {
	    	  //如有必要，等待计算完成，然后获取其结果。
	        Object localObject2 = get();
	        localObject1 = localObject2;
	        AsyncMockTask.InternalHandler localInternalHandler = AsyncMockTask.sHandler;
			AsyncMockTask localRunAsyncTask = AsyncMockTask.this;
	        Object[] arrayOfObject = new Object[1];
	        arrayOfObject[0] = localObject1;
	        //发送handle消息 1，new 出的对象，get()返回的结果 []
	        localInternalHandler.obtainMessage(MESSAGE_POST_RESULT, new AsyncMockTask.AsyncTaskResult(localRunAsyncTask, arrayOfObject)).sendToTarget();
	        return;
	      }
	      catch (InterruptedException localInterruptedException)
	      {
	          Log.w(LOG_TAG, localInterruptedException);
	      }
	      catch (ExecutionException localExecutionException)
	      {
	        throw new RuntimeException("An error occured while executing doInBackground()", localExecutionException.getCause());
	      }
	      catch (CancellationException localCancellationException)
	      {
	    	  AsyncMockTask.sHandler.obtainMessage(MESSAGE_POST_CANCEL, new AsyncMockTask.AsyncTaskResult(AsyncMockTask.this, null)).sendToTarget();
	      }
	      catch (Throwable localThrowable)
	      {
	    	  throw new RuntimeException("An error occured while executing doInBackground()", localThrowable);
	      }
	     
	    }
	  };
	  private volatile Status mStatus = Status.PENDING;

	  static
	  {
		//可以用原子方式更新的int值。
	    sThreadFactory = new ThreadFactory()
	    {
	      private final AtomicInteger mCount = new AtomicInteger(1);

	      public Thread newThread(Runnable paramRunnable)
	      {
	    	 //每次调用都递增当前值
	        return new Thread(paramRunnable, "ThreadTask->c #" + this.mCount.getAndIncrement());
	      }
	    };
	    /**
	     * 创建新的ThreadPoolExecutor具有给定的初始参数和默认拒绝执行的处理程序。
	     * 线程数目、最大线程数、空闲时间、空闲时间的单位（秒）、工作队列、线程工程，创建线程使用
	     */
	    sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sWorkQueue, sThreadFactory,
	    		new ThreadPoolExecutor.DiscardOldestPolicy());
	    //创建Handler消息
	    sHandler = new InternalHandler();
	  }

	  private void finish(Result paramResult)
	  {
	    if (isCancelled())
	      paramResult = null;
	    onPostExecute(paramResult);
	    this.mStatus = Status.FINISHED;
	  }

	  public final boolean cancel(boolean paramBoolean)
	  {
	    return this.mFuture.cancel(paramBoolean);
	  }

	  protected abstract Result doInBackground(Params... paramArrayOfParams);

	  /**
	   * 执行任务
	   * @param paramArrayOfParams 参数[]
	   * @return
	   */
	  public final AsyncMockTask<Params, Progress, Result> execute(Params...paramArrayOfParams){
	    if (this.mStatus != Status.PENDING)
	    {
	        switch (this.mStatus)
	        {
	        case RUNNING:
	        	 throw new IllegalStateException("Cannot execute task: the task is already running.");
	        case FINISHED:
	        	 throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
	        }
	    }

        this.mStatus = Status.RUNNING;
        onPreExecute();
        this.mWorker.mParams = paramArrayOfParams;
        //执行任务
        sExecutor.execute(this.mFuture);
	    
	    return this;
	  }

	  /**
	   * 如有必要，等待计算完成，然后获取其结果。
	   * @return
	   * @throws InterruptedException
	   * @throws ExecutionException
	   */
	  public final Result get()
	    throws InterruptedException, ExecutionException
	  {
	    return this.mFuture.get();
	  }

	  /**
	   * 如果需要等待至多给定的时间计算完成，然后获取其结果（如果可用）。
	   * @param paramLong         时间
	   * @param paramTimeUnit    时间单位
	   * @return
	   * @throws InterruptedException
	   * @throws ExecutionException
	   * @throws TimeoutException
	   */
	  public final Result get(long paramLong, TimeUnit paramTimeUnit)
	    throws InterruptedException, ExecutionException, TimeoutException
	  {
	    return this.mFuture.get(paramLong, paramTimeUnit);
	  }

	  public final Status getStatus()
	  {
	    return this.mStatus;
	  }

	  public final boolean isCancelled()
	  {
	    return this.mFuture.isCancelled();
	  }
       
	  protected void onCancelled()
	  {
	  }

	  protected void onPostExecute(Result paramResult)
	  {
	  }

	  protected void onPreExecute()
	  {
	  }

	  protected void onProgressUpdate(Progress... paramArrayOfProgress)
	  {
	  }

	  @SuppressWarnings({ "rawtypes", "unchecked" })
	protected final void publishProgress(Progress[] paramArrayOfProgress)
	  {
	    sHandler.obtainMessage(MESSAGE_POST_PROGRESS, new AsyncTaskResult(this, paramArrayOfProgress)).sendToTarget();
	  }

	  private static class AsyncTaskResult<Data>
	  {
	    final Data[] mData;
	    @SuppressWarnings("rawtypes")
		final AsyncMockTask mTask;

	    @SuppressWarnings("rawtypes")
		AsyncTaskResult(AsyncMockTask paramRunAsyncTask, Data[] paramArrayOfData)
	    {
	      this.mTask = paramRunAsyncTask;
	      this.mData = paramArrayOfData;
	    }
	  }

	  private static class InternalHandler extends Handler
	  {
	    @SuppressWarnings("unchecked")
		public void handleMessage(Message paramMessage)
	    {
	    	@SuppressWarnings("rawtypes")
			AsyncMockTask.AsyncTaskResult localAsyncTaskResult = (AsyncMockTask.AsyncTaskResult)paramMessage.obj;
	      switch (paramMessage.what)
	      {
	      case MESSAGE_POST_RESULT:
	    	  localAsyncTaskResult.mTask.finish(localAsyncTaskResult.mData[0]);
	    	  break;
	      case MESSAGE_POST_PROGRESS:
	    	  localAsyncTaskResult.mTask.onProgressUpdate(localAsyncTaskResult.mData);
	    	  break;
	      case MESSAGE_POST_CANCEL:
	    	  localAsyncTaskResult.mTask.onCancelled();
	    	  break;
	      default:
	    	  break;
	      }
	    }
	  }
	  
	  public enum Status {
	      /**
	       * Indicates that the task has not been executed yet.
	       */
	      PENDING,
	      /**
	       * Indicates that the task is running.
	       */
	      RUNNING,
	      /**
	       * Indicates that {@link AsyncTask#onPostExecute} has finished.
	       */
	      FINISHED,
	  }

	  private static abstract class WorkerRunnable<Params, Result>
	    implements Callable<Result>
	  {
	    Params[] mParams;
	  }
}