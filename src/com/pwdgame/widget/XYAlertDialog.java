package com.pwdgame.widget;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwdgame.library.R;

public class XYAlertDialog {

	// 点击返回记录，用于点击后取消或退出
	private static int backcount = 1;

	/*
	 * 退出提示对话框 参数：上下文实例、标题、内容（可以为NULL）
	 
	public static void OptionAlertDialog(Context context, String title,
			String content, DialogInterface.OnClickListener clickListener) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);

		builder.setTitle(title);
		if (content != null)
			builder.setMessage(content);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setPositiveButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.ok, clickListener);
		builder.setCancelable(false);
		builder.create().show();
	}

	
	 * 更新提示对话框 参数：上下文实例、标题、内容（可以为NULL）
	 
	public static void OptionAlertDialog(Context context, String title,
			String content, DialogInterface.OnClickListener okListener,
			boolean IsCancel, String okStr, String CancelStr) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);

		if(title!=null)
		  builder.setTitle(title);
		if (content != null)
			builder.setMessage(content);
		builder.setNegativeButton(okStr, okListener);
		if (IsCancel)
			builder.setPositiveButton(CancelStr,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		builder.setCancelable(IsCancel);

		builder.create().show();
	}
	
	 * 更新提示对话框 参数：上下文实例、标题、内容（可以为NULL）
	 
	public static void OptionAlertDialog(Context context, String title,
			String content, DialogInterface.OnClickListener okListener,DialogInterface.OnClickListener cancelListener,
			String okStr, String CancelStr) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);

		if(title!=null)
		  builder.setTitle(title);
		if (content != null)
			builder.setMessage(content);
		builder.setNegativeButton(okStr, okListener);
		builder.setPositiveButton(CancelStr,cancelListener);
		builder.setCancelable(false);

		builder.create().show();
	}
	
	 * 退出提示对话框,用于在dispatchKeyEvent里 参数：上下文实例、标题、内容（可以为NULL） 返回值：调用成功true，失败false
	 
	public static boolean ExitAlertDialogEx(Context context, String title,
			String content) {
		if (backcount == 1) {
			android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
					context);			
			builder.setTitle(title);
			if (content != null)
				builder.setMessage(content);
			builder.setPositiveButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            backcount = 1;
                        }
                    });
			builder.setNegativeButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            backcount = 1;
                            MyApplication.getInstance().exit();
                        }
                    });
			builder.setCancelable(false);
			builder.create().show();

			backcount = (1 + backcount);
			return true;
		}
		return false;
	}
*/
	/*
	 * 自定义对话框，重写了 Handler消息，点击按钮也不关闭
	 */
	public static void CustomAlertDialog(Context context, View view,
			String title, String okBtnText,
			DialogInterface.OnClickListener okClickListener) {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				context);
		builder.setTitle(title);
		builder.setView(view);
		//builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {						
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(okBtnText, okClickListener);
		builder.setCancelable(true);		
		AlertDialog alertDialog = builder.create();
		// 修改一个 mHandler 变量的值
		try {

			Field field = alertDialog.getClass().getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(alertDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, new ButtonHandler(alertDialog));
		} catch (Exception e) {
		}
		// 显示对话框
		alertDialog.show();
	}

	
	/**
	 * 显示消息的对话框
	 * @param context
	 * @param title
	 * @param content
	 */
	public static void MsgAlertDialog(Context context, String title,
			String content) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);

		builder.setTitle(title);
		if (content != null)
			builder.setMessage(content);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		builder.setCancelable(false);
		builder.create().show();
	}
	
	 //* 显示消息的对话框
	 
	public static void MsgAlertDialog(Context context, String title,
			String content,DialogInterface.OnClickListener clickListener) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);

		builder.setTitle(title);
		if (content != null)
			builder.setMessage(content);
		builder.setPositiveButton(R.string.ok,clickListener);
		builder.setCancelable(false);
		builder.create().show();
	}
	/*
	 * 显示消息的对话框
	 */
	public static void RadioAlertDialog(Context context, String[] items,
			String title, String okBtnText,
			DialogInterface.OnClickListener radioItemClickListener,
			DialogInterface.OnClickListener okButtonClickListener) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle(title);
		builder.setSingleChoiceItems(items, 0, radioItemClickListener);
		builder.setPositiveButton(okBtnText, okButtonClickListener);
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		builder.setCancelable(false);
		builder.create().show();
	}

	/*
	 * 显示消息的对话框
	 */
	public static void RadioAlertDialog(Context context, String[] items,
			int defaultChoose, String title, String okBtnText,
			DialogInterface.OnClickListener radioItemClickListener,
			DialogInterface.OnClickListener okButtonClickListener) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle(title);
		builder.setSingleChoiceItems(items, defaultChoose,
				radioItemClickListener);
		builder.setPositiveButton(okBtnText, okButtonClickListener);
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

		builder.setCancelable(false);
		builder.create().show();
	}
	/**
	 * 显示消息的对话框
	 */
	public static void OptionMenuAlertDialog(Context context, int items,
			int title, DialogInterface.OnClickListener itemClickListener) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		
		builder.setTitle(title);
		builder.setItems(items,itemClickListener);
		builder.setCancelable(true);
		builder.create().show();
	}
	/**
	 * 显示消息的对话框
	 */
	public static void OptionMenuAlertDialog(Context context, String[] items,
			String title, DialogInterface.OnClickListener itemClickListener) {
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		
		builder.setTitle(title);
		builder.setItems(items,itemClickListener);
		builder.setCancelable(true);
		builder.create().show();
	}
	// 重写 AlertDialog Handler消息
	private static final class ButtonHandler extends Handler {

		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mDialog.get(), msg.what);
				break;
			}
		}
	}
}
