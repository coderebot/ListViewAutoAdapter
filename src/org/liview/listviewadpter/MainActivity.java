package org.liview.listviewadpter;

import org.liview.listviewadpter.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ListView mListView;
	
	public static class Beauty {
		public String name;
		public int    headIcon;
		public String info;
		public int    constellation; //����
		
		public static final int Aquarius = 0; 
		public static final int Pisces = 1;
		public static final int Aries = 2;
		public static final int Taurus = 3;
		public static final int Gemini = 4;
		public static final int Cancer = 5;
		public static final int Leo = 6;
		public static final int Virgo = 7;
		public static final int Libra = 8;
		public static final int Scorpio = 9;
		public static final int Sagittarius = 10;
		public static final int Capricorn = 11;
		
		public Beauty(String name, int head, String info, int cons) {
			this.name = name;
			this.headIcon = head;
			this.info = info;
			this.constellation = cons;
		}
	}
	
	private List<Beauty> mBeauties;
	
	private List<Beauty> getData() {
		if(mBeauties != null)
			return mBeauties;
		
		mBeauties = new ArrayList<Beauty>();
		mBeauties.add(new Beauty("貂蝉", R.drawable.mv1, "东汉帝国小姐冠军", Beauty.Aquarius));
		mBeauties.add(new Beauty("王昭君", R.drawable.mv2, "和亲大使", Beauty.Capricorn));
		mBeauties.add(new Beauty("西施", R.drawable.mv3, "越国美女007", Beauty.Leo));
		mBeauties.add(new Beauty("杨贵妃", R.drawable.mv4, "最不担心体重的美丽女人", Beauty.Gemini));
		mBeauties.add(new Beauty("苍井空", R.drawable.mv5, "屌丝女神", Beauty.Aries));
		mBeauties.add(new Beauty("赫本", R.drawable.mv6, "最有公主气质的女人", Beauty.Sagittarius));
		
		return mBeauties;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mListView = (ListView)findViewById(R.id.listView1);
		
		try{
			mListView.setAdapter(
					new AutoBindAdapter<Beauty>(
							this,
							R.layout.listitem,
							getData(),
							new AutoBindAdapter.AutoBinder()
								.add(R.id.img, Beauty.class.getField("headIcon"))
								.add(R.id.title, Beauty.class.getField("name"))
								.add(R.id.info, Beauty.class.getField("info"))
								.add(R.id.img_constellation, Beauty.class.getField("constellation"),
										new AutoBindAdapter.Binder() {
											public void setView(View view, Object obj) {
												ImageView img = (ImageView)(view);
												int resId = 0;
												int constellation = (Integer)obj;
												switch(constellation){
												case Beauty.Aquarius:    resId = R.drawable.aquarius; break;
												case Beauty.Pisces:      resId = R.drawable.pisces; break;
												case Beauty.Aries:       resId = R.drawable.aries; break;
												case Beauty.Taurus:      resId = R.drawable.taurus; break;
												case Beauty.Gemini:      resId = R.drawable.gemini; break;
												case Beauty.Cancer:      resId = R.drawable.cancer; break;
												case Beauty.Leo:         resId = R.drawable.leo; break;
												case Beauty.Virgo:       resId = R.drawable.virgo; break;
												case Beauty.Libra:       resId = R.drawable.libra; break;
												case Beauty.Scorpio:     resId = R.drawable.scorpio; break;
												case Beauty.Sagittarius: resId = R.drawable.sagittarius; break;
												case Beauty.Capricorn:   resId = R.drawable.capricorn; break;
												default: return;
												}
												img.setImageResource(resId);
											}
										}
									)
								
					)
			);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
