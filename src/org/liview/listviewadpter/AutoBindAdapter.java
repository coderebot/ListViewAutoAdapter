package org.liview.listviewadpter;

import android.net.Uri;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;

public class AutoBindAdapter<E> extends BaseAdapter {

	private List<E> mList;
	private final Context mContext;
	private final LayoutInflater mInflater;
	
	public static abstract class Binder {
		private static Binder defaultBinder = new Binder() {
			public void setView(View view, Object obj){
				if(view == null || obj == null)
					return;
				if(view instanceof TextView)
				{
					TextView txt = (TextView)view;
					if(obj instanceof String)
						txt.setText((String)obj);
					else if(obj instanceof CharSequence)
						txt.setText((CharSequence)obj);
					else
						txt.setText(obj.toString());
				}
				else if(view instanceof ImageView)
				{
					ImageView img = (ImageView)view;
					if(obj instanceof String)
					{
						try {
							img.setImageResource(Integer.parseInt((String)obj));
						}catch(NumberFormatException nfe) {
							img.setImageURI(Uri.parse((String)obj));
						}
					}
					else if(obj instanceof Integer) {
						img.setImageResource((Integer)obj);
					}
					else if(obj instanceof Bitmap)
					{
						img.setImageBitmap((Bitmap)obj);
					}
					else if(obj instanceof Drawable)
					{
						img.setImageDrawable((Drawable)obj);
					}
				}
			}
		};
		
		public abstract void setView(View view, Object obj);
		public static Binder getDefault() {
			return defaultBinder;
		}
	}
	
	public static Binder getDefaultBinder() {
		return Binder.getDefault();
	}
	
	private Binder mBinder;
	private int mViewId;
	
	
	public static class AutoBinder extends Binder{
		private static class BindEntry {
			public int viewId;
			public Object access;
			public Binder bind;   
			public BindEntry(int id, Object access, Binder bind)
			{
				viewId = id;
				this.access = access;
				this.bind = bind;
			}
			public Object getValue(Object obj) {
				try{
					if(access instanceof Field)
					{
						return ((Field)access).get(obj);
					}
					else if (access instanceof Method){
						return ((Method)access).invoke(obj, (Object[])null);
					}
					else if (access instanceof Class) {
						return obj;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}
		private List<BindEntry> mBindList;
		
		public AutoBinder()
		{
			mBindList = new ArrayList<BindEntry>();
		}
		
		public AutoBinder add(int viewId, Object access, Binder bind){
			mBindList.add(new BindEntry(viewId, access, bind));
			return this;
		}
		public AutoBinder add(int viewId, Object access)
		{
			return add(viewId, access, Binder.getDefault());
		}
		
		public void setView(View view, Object obj)
		{
			int size = mBindList.size();
			if(view == null || obj == null || size <= 0)
				return ;
				
			View[] holders = (View[]) view.getTag();
			if(holders == null)
			{
				holders = new View[size];
				for(int i = 0; i < size; i ++)
				{
					BindEntry bind = mBindList.get(i);
					holders[i] = view.findViewById(bind.viewId);
					holders[i].setTag(bind); //set the bind
					bind.bind.setView(holders[i], bind.getValue(obj));
				}
			}
			else
			{
				for(int i = 0; i < size; i++)
				{
					BindEntry bind = (BindEntry)holders[i].getTag();
					bind.bind.setView(holders[i], bind.getValue(obj));
				}
			}
			
		}
	}
	
	public AutoBindAdapter(Context context, int viewId, List<E> list, Binder bind)
	{
		mContext = context;
		mList = list;
		mBinder = bind;
		mViewId = viewId;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convert, ViewGroup parent) {
		View view;
		if(convert == null)
			view = mInflater.inflate(mViewId, parent, false);
		else
			view = convert;
		
		bindView(view, (E)getItem(position));
		
		return view;
	}
	
	private void bindView(View view, Object obj)
	{
		mBinder.setView(view, obj);
	}

}
