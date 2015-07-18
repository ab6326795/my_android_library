package com.pwdgame.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.manager.ImageLoaderManager;
import com.pwdgame.bean.ActionBarIcon;
import com.pwdgame.bean.ActionBarMenuItem;
import com.pwdgame.library.R;
import com.pwdgame.util.Utility;
/**
 * 弹出菜单 ，效果和ActionBar类似，参见好好说APP
 * @author xieyuan
 *
 */
public class XYPopMenu {

	private static final String TAG="XYPopMenu";

	private List<ActionBarMenuItem> menuItems=new ArrayList<ActionBarMenuItem>();
	private Activity mActivity;
	private LayoutInflater inflater;
		
	private View actionView;
	private ListView actionListView;
	
	private PopupWindow popupWindow;
	private float mScale;
	
	private WindowManager windowManager;
	private int displayWidth,displayHeight;
	private OnActionBarMenuOnClick onActionBarMenuOnClick;
	private android.widget.PopupWindow.OnDismissListener onDismissListener;
	
	private int background;
	private int textColor=Color.BLACK;
	private int popStyle = R.style.PopupAnimation;

	public XYPopMenu(Activity activity){
		mActivity=activity;
		inflater=(LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}
	
	public ActionBarMenuItem add(int id,String title, ActionBarIcon icon,String subTitle){
		ActionBarMenuItem item=new ActionBarMenuItem(id, title, icon, subTitle);
		menuItems.add(item);
		return item;
	}
	
	public ActionBarMenuItem add(int id,String title, Object icon,String subTitle){
		ActionBarMenuItem item=new ActionBarMenuItem(id, title, icon, subTitle);
		menuItems.add(item);
		return item;
	}
	public ActionBarMenuItem add(int id,String title, Object icon){
		return this.add(id, title, icon, null);
	}
	public ActionBarMenuItem add(int id,String title, ActionBarIcon icon){
		return this.add(id, title, icon, null);
	}
	/**
	 * 更新菜单项
	 * @param id
	 * @param title
	 * @param icon
	 * @param subTitle
	 */
	public void update(int id,String title, ActionBarIcon icon,String subTitle){
		for(int i=0,size=menuItems.size();i<size;i++){
			if(menuItems.get(i).id==id){
				menuItems.get(i).set(id, title, icon, subTitle);
				break;
			}
		}
	}
	
	

	private void init(){
		background=R.drawable.trangle_menu_background;//R.drawable.abs__menu_dropdown_panel_holo_dark;
		windowManager=(WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
		displayWidth=windowManager.getDefaultDisplay().getWidth();
		displayHeight=windowManager.getDefaultDisplay().getHeight() - Utility.getNavigationBarHeightEx(mActivity)
				- Utility.getStatusBarHeight(mActivity);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		mScale=metrics.scaledDensity;
		
		actionView=inflater.inflate(R.layout.actionbarview_content, null,false);
		actionListView=(ListView) actionView.findViewById(R.id.actionbarview_content_listview);

		actionView.setFocusable(true);
		actionView.setFocusableInTouchMode(true);
		
		actionView.setOnKeyListener(new View.OnKeyListener() {  
	            @Override  
	            public boolean onKey(View v, int keyCode, KeyEvent event) {  
	                if (event.getAction() == KeyEvent.ACTION_UP) {  
	                    switch(keyCode) {   
	                    case KeyEvent.KEYCODE_MENU:  
	                        dismiss(); 
	                        break;  
	                    }  
	                }  
	                return true;  
	            }  
	        });  

		popupWindow=new PopupWindow(mActivity);
/*	    popupWindow.setTouchInterceptor(new View.OnTouchListener() {    
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  
                 *  如果点击了popupwindow的外部，popupwindow也会消失. 
                 *  这是PopupWindow的重要特性 
                   
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {    
               	 popupWindow.dismiss();    
                    return true;     
                }    
                return false; 
			}    
         }); */
		
	    popupWindow.setTouchable(true);
	    popupWindow.setFocusable(true);
	    popupWindow.setOutsideTouchable(true);
	    popupWindow.setAnimationStyle(popStyle);	    
	    popupWindow.setContentView(actionView);
	    
	}
	
	public void setOnDismissListener(android.widget.PopupWindow.OnDismissListener onDismissListener){
		this.onDismissListener = onDismissListener;
		if(this.onDismissListener != null)
		    popupWindow.setOnDismissListener(this.onDismissListener);
	}
	
	public void setAnimationStyle(int style){
		this.popStyle = style;
		popupWindow.setAnimationStyle(popStyle);
	}
	
	/**
	 * 设置MENU背景
	 * @param resid
	 */
	public void setBackground(int resid){
		this.background=resid;
	}
	
	public void setTextColor(int textColor){
		this.textColor=textColor;
	}
	
	public float measureMaxWidth(){
		Paint paint=new Paint();
		paint.setTextSize(Utility.dipToPx(mActivity, 18));
		
		float maxWidth = 0;
		Rect titleRect=new Rect();
		Rect subRect=new Rect();
		float temp=0;
		for(int i=0,size=menuItems.size();i<size;i++){			
			CharSequence str=menuItems.get(i).titleStr;
			if(str!=null){
			   paint.getTextBounds(str.toString(), 0, str.length(), titleRect);
			   
			}
			str=menuItems.get(i).subTitleStr;
			if(str!=null){
				paint.getTextBounds(str.toString(), 0, str.length(), subRect);				
			}
			temp=titleRect.width()>subRect.width()? titleRect.width():subRect.width();
			maxWidth=temp>maxWidth? temp:maxWidth;
		}
		return maxWidth;
	}
	
	public void show(final View view){

		if(menuItems==null||menuItems.size()<=0)
			return;
/*		int firstLength=Math.max(1, menuItems.get(0).titleStr.length());		
		int maxLength=0;
		for(int i=0,size=menuItems.size();i<size;i++){
			int tmp=menuItems.get(i).titleStr.length();
			if(tmp>maxLength)
				maxLength=tmp;
		}
		if(maxLength>firstLength){
			int cha=maxLength-firstLength;
			StringBuilder builder=new StringBuilder();
			for(int i=0;i<cha;i++){
				builder.append("   ");
			}
			
			menuItems.get(0).titleStr=menuItems.get(0).titleStr.toString()+builder.toString();
		}*/
		
	      MenuItemAdapter adapter = new MenuItemAdapter(mActivity, menuItems,textColor);
	      actionListView.setAdapter(adapter);
	      actionListView.setOnItemClickListener(new ListView.OnItemClickListener() {

	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	            	
	            	ActionBarMenuItem item=menuItems.get(position);
	                if (onActionBarMenuOnClick!= null) {
	                	onActionBarMenuOnClick.onMenuItemOnClick(item);
	                }
	                popupWindow.dismiss();
	            }
	        });

       if (view == null) {
            View parent = ((Activity)mActivity).getWindow().getDecorView();
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            return;
        }

		//获取视图位置
		int[] location=new int[2];
		view.getLocationOnScreen(location);
		Rect viewRect=new Rect(location[0],location[1] ,location[0]+view.getWidth(),location[1]  + view.getHeight());
		
		//获取的宽高是第一列的宽高
		//actionView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		actionView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int actionHeight=actionView.getMeasuredHeight() * adapter.getCount() +Utility.dipToPx(mActivity, 5);
	
		//int actionWidth=(int) (actionView.getMeasuredWidth()*mScale);

		//int dp=Utility.dipToPx(mActivity, 200);
		//actionWidth=dp;

        int actionWidth=(int) (measureMaxWidth()+Utility.dipToPx(mActivity, 80));
		
		//设置POPWINDOW宽高
        popupWindow.setWidth(actionWidth);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);        
        popupWindow.setBackgroundDrawable(mActivity.getResources().getDrawable(background));

       /* actionLinear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

			@Override
			public void onGlobalLayout() {
				actionLinear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				int width=actionLinear.getWidth();
				actionLinear.getLayoutParams().width=width;
	
				actionLinear.requestLayout();
				
				//popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, xPos,yPos);
				if(BuildConfig.DEBUG){
				  Log.e(TAG, ""+width);
				}
			}
			
		});*/
        int xPos,yPos;
        //向父控件的右边对齐
        if(viewRect.right>actionWidth){
        	xPos=viewRect.right-actionWidth;
        }else{
        	//向左对齐
			xPos=viewRect.left+actionWidth;
        }
		
/*		 int dyTop = viewRect.top;
		 int dyBottom  = screenH - viewRect.bottom; 
*/
        //倒过来，按钮在下，菜单在上
		 if(viewRect.bottom+actionHeight>displayHeight){
			 yPos=viewRect.top-actionHeight;
		 }else {
			 //菜单在下
		    yPos=viewRect.bottom;
		 }

		//popupWindow.setAnimationStyle(R.style.PopupAnimation);
		// 加上下面两行可以用back键关闭popupwindow，否则必须调用dismiss();		
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, xPos,yPos);		
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
    
    public View getPopupView(){
    	
    	Field field = null;
    	View view = null;
		try {
			field = popupWindow.getClass().getDeclaredField("mPopupView");
			field.setAccessible(true);
			view = (View) field.get(popupWindow);
		} catch( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return view;
    	
    }
    
    private class MenuItemAdapter extends ArrayAdapter<ActionBarMenuItem> {

    	private int textColor;
        public MenuItemAdapter(Context context, List<ActionBarMenuItem> objects,int textColor) {
            super(context, 0, objects);
            this.textColor=textColor;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.actionbarview_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.actionbarview_item_icon);
                holder.iconShade=(ImageView)convertView.findViewById(R.id.actionbarview_item_icon_shade);
                holder.title = (TextView) convertView.findViewById(R.id.actionbarview_item_title);
                holder.subTitle=(TextView)convertView.findViewById(R.id.actionbarview_item_subtitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ActionBarMenuItem item = getItem(position);
            if (item.icon!=null&&item.icon.icon!=null) {
            	
            	if(item.icon.icon instanceof Drawable){
            		holder.icon.getLayoutParams().width=item.icon.width;
              	    holder.icon.getLayoutParams().height=item.icon.height;
              	    holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
                    holder.icon.setImageDrawable((Drawable) item.icon.icon);
            	}else if(item.icon.icon instanceof Integer){
            		holder.icon.getLayoutParams().width=item.icon.width;
              	    holder.icon.getLayoutParams().height=item.icon.height;
              	    holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
              	    holder.icon.setImageResource((Integer) item.icon.icon);                    
            	}else if(item.icon.icon instanceof String){
              	   holder.icon.getLayoutParams().width=item.icon.width;
              	   holder.icon.getLayoutParams().height=item.icon.height;
              	   holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);	
              	   
              	   ImageLoaderManager.displayImage((String)item.icon.icon, holder.icon);            	   
            	}
            	////////////////////////
            	if(item.icon.reserve!=null&& item.icon.reserve instanceof Integer&&(Integer)item.icon.reserve!=-1){
            		holder.iconShade.setImageResource((Integer)item.icon.reserve);            		
                }else{
                	holder.iconShade.setImageDrawable(null);             	   
                }
            	holder.icon.setVisibility(View.VISIBLE);
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            
            holder.title.setTextColor(textColor);
            if(item.titleStr!=null){
               holder.title.setText(item.titleStr);               
               holder.title.setVisibility(View.VISIBLE);
            }
            else{
            	holder.title.setVisibility(View.GONE);
            }

            if(item.subTitleStr!=null){
            	holder.subTitle.setText(item.subTitleStr);
            	holder.subTitle.setVisibility(View.VISIBLE);
  
            }else {            	
            	holder.subTitle.setVisibility(View.GONE);
			}
            
            return convertView;
        }
    }
    
    static class ViewHolder {
        ImageView icon;
        ImageView iconShade;
        TextView title;
        TextView subTitle;
    }

	
	public void setOnActionBarMenuOnClick(OnActionBarMenuOnClick listener){
		this.onActionBarMenuOnClick=listener;
	}
    
	
	public interface OnActionBarMenuOnClick{
		public void onMenuItemOnClick(ActionBarMenuItem item);
	}
}
