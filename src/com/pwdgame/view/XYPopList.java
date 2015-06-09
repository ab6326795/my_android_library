package com.pwdgame.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.pwdgame.bean.PopListItem;
import com.pwdgame.library.R;

/**
 * 一个弹出列表，默认从底部弹出
 * @author xieyuan
 *
 */
public class XYPopList {

	private List<PopListItem> menuItems=new ArrayList<PopListItem>();
	private Context mContext;
	private LayoutInflater inflater;
	
	private View actionView;
	private ListView actionListView;
	private Button cancelButton;
	
	private PopupWindow popupWindow;
	private float mScale;
	
	private WindowManager windowManager;
	private int screenW,screenH;
	private OnActionBarMenuOnClick onActionBarMenuOnClick;

	private int textColor=Color.WHITE;
	
	public XYPopList(Context context){
		mContext=context;
		inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}
	
	public PopListItem add(int id, int textResId, int background) {
		PopListItem menuItem=new PopListItem(id,mContext.getString(textResId),mContext.getResources().getDrawable(background));						
		menuItems.add(menuItem);		
		return menuItem;
	}
	public PopListItem add(int id, CharSequence text,Drawable background) {
		PopListItem menuItem=new PopListItem(id,text,background);						
		menuItems.add(menuItem);		
		return menuItem;
	}
	/**
	 * 更新菜单项
	 * @param id
	 * @param title
	 * @param icon
	 * @param subTitle
	 */
	public void update(int id, int textResId, int background) {
		for(int i=0,size=menuItems.size();i<size;i++){
			if(menuItems.get(i).id==id){
				menuItems.get(i).set(id,mContext.getString(textResId),mContext.getResources().getDrawable(background));
				break;
			}
		}
	}
	
	private void init(){

		windowManager=(WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		screenW=windowManager.getDefaultDisplay().getWidth();
		screenH=windowManager.getDefaultDisplay().getHeight();
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		mScale=metrics.scaledDensity;
		
		actionView=inflater.inflate(R.layout.xypoplist_layout, null);
		actionListView=(ListView) actionView.findViewById(R.id.xypoplist_listview);
		cancelButton=(Button)actionView.findViewById(R.id.xypoplist_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		actionView.setFocusable(true);
		actionView.setFocusableInTouchMode(true);
		
/*//		actionView.setOnKeyListener(new View.OnKeyListener() {  
//	            @Override  
//	            public boolean onKey(View v, int keyCode, KeyEvent event) {  
//	                if (event.getAction() == KeyEvent.ACTION_UP) {  
//	                    switch(keyCode) {   
//	                    case KeyEvent.KEYCODE_MENU:  
//	                        dismiss(); 
//	                        break;  
//	                    }  
//	                }  
//	                return true;  
//	            }  
//	        });  
*/		popupWindow=new PopupWindow(mContext);
	    popupWindow.setTouchable(true);
	    popupWindow.setFocusable(true);
	    popupWindow.setOutsideTouchable(true);
	    popupWindow.setAnimationStyle(R.style.XYPopListAnimation);	    
	    popupWindow.setContentView(actionView);
	 
	}

	
	public void show(View view){
		if(menuItems==null||menuItems.size()<=0)
			return;
	
		
	      MenuItemAdapter adapter = new MenuItemAdapter(mContext, menuItems,textColor);
	      actionListView.setAdapter(adapter);


       if (view == null) {
            View parent = ((Activity)mContext).getWindow().getDecorView();
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            return;
        }
		int xPos,yPos,arrowPos;
		//获取视图位置
		int[] location=new int[2];
		view.getLocationOnScreen(location);
		Rect viewRect=new Rect(location[0],location[1],location[0]+view.getWidth(),location[1]+view.getHeight());
		
		//获取的宽高是第一列的宽高
		actionView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		actionView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		//设置POPWINDOW宽高
        popupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.FILL_PARENT);
        
        ColorDrawable dw = new ColorDrawable(0x66000000);
        popupWindow.setBackgroundDrawable(dw);
        
        //向父控件的右边对齐
        

		//popupWindow.setAnimationStyle(R.style.PopupAnimation);
		// 加上下面两行可以用back键关闭popupwindow，否则必须调用dismiss();
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0,0);
		
	}

    /**
     * Dismiss the popup menu.
     */
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
        	popupWindow.dismiss();
        }
    }

    public boolean isShowing(){
    	if(popupWindow != null && popupWindow.isShowing())
    		return true;
    	return false;
    }
    
    private class MenuItemAdapter extends ArrayAdapter<PopListItem> {

        public MenuItemAdapter(Context context, List<PopListItem> objects,int textColor) {
            super(context, 0, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        	
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.xypoplist_item, null);
                holder = new ViewHolder();
                holder.button = (Button) convertView.findViewById(R.id.xypoplist_item_button);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PopListItem item = getItem(position);
            if (item.text!=null) {
            	holder.button.setText(item.text);
            }
            if(item.background!=null){
            	holder.button.setBackgroundDrawable(item.background);
            }    
            
            holder.button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PopListItem item=menuItems.get(position);
	                if (onActionBarMenuOnClick!= null) {
	                	onActionBarMenuOnClick.onMenuItemOnClick(item);
	                }
	                popupWindow.dismiss();
				}
			});  	   
            return convertView;
        }
    }
    
    static class ViewHolder {
        Button button;
    }

	
	public void setOnActionBarMenuOnClick(OnActionBarMenuOnClick listener){
		this.onActionBarMenuOnClick=listener;
	}
    
	
	public interface OnActionBarMenuOnClick{
		public void onMenuItemOnClick(PopListItem item);
	}
}
