package com.example.positivelines;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FinalActivity extends Activity implements OnClickListener
{
	TextView tv_thank;
	Button bt_cham;
	Button bt_exit;
	
	Typeface font;
	AnimatorSet setButton;
	AnimatorSet setLayout;
	
	float initPosition=0;
	float finalPosition=0;
	
	int stringA;
	int answer;
	
	String buttonText=null;
	
	String KEY_SA="STRINGA";
	String KEY_AN="ANSWER";
	String KEY_CP="CURRENTP";
	String KEY_FP="FINALP";
	String KEY_BT="BUTTONT";
	String KEY_ST="STARTTIME";
	String KEY_TTT="TIMETOTHINK";
	
	Bundle gotBasket;
	
	final Context context = this;
	
	List<Integer> stringSentance = new ArrayList<Integer>();
	List<Integer> feelingOption = new ArrayList<Integer>();
	List<Long> durationPerVisit = new ArrayList<Long>();
	List<Long> timeToThinkArray = new ArrayList<Long>();
	
	/*
	int stringSentance[];
	int feelingOption[];
	long durationPerVisit[];
	long timeToThinkArray[];
	*/
	
	int timesToday=0;
	
	long startTime=0;
	long endTime=0;
	long timeSpent=0;
	long timeToThink=0;
	
	String filename="Stats";
	SharedPreferences stats;
	
	long globalTimes=0;
	long gtReal=0;
	
	final String TAG="COOKIE";
	
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main2, menu);
		return true;
	}*/
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main3, menu);
        
        
        getLayoutInflater().setFactory(new Factory() 
        {
            public View onCreateView(String name, Context context,AttributeSet attrs) 
            {

                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) 
                {
                    try 
                    {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        new Handler().post(new Runnable() 
                        {
                            public void run() {
                                // set the background drawable if you want that
                                //or keep it default -- either an image, border
                                //gradient, drawable, etc.
                                //view.setBackgroundResource(R.drawable.myimage);
                                ((TextView) view).setTextSize(20); 

                                // set the text color
                                Typeface face = Typeface.createFromAsset(getAssets(),"Roboto-Light.ttf");     
                                ((TextView) view).setTypeface(face);
                                ((TextView) view).setTextColor(Color.RED);
                            }
                        });
                        return view;
                    } catch (InflateException e) 
                    {
                        //Handle any inflation exception here
                    } catch (ClassNotFoundException e) 
                    {
                        //Handle any ClassNotFoundException here
                    }
                }
                return null;
            }
        });
        
        
        return super.onCreateOptionsMenu(menu);
    }
	
	private void ActionBarFontstuff()
	{
		int titleId = getResources().getIdentifier("action_bar_title", "id","android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	  //  yourTextView.setTextColor(getResources().getColor(R.color.black));
	    yourTextView.setTypeface(font);
	}
	/*
	private void overflowStuff()
	{
		 SpannableString s = new SpannableString("My Title");
		 s.setSpan(new TypefaceSpan("Roboto-Light.ttf"), 0, s.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		 PopUpMenu 
		 // Update the action bar title with the TypefaceSpan instance
		 ActionBar actionBar = getActionBar();
		 actionBar.
	}
	*/
	
	private void overflowStuff()
	{
	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		
		//Init
		//unpackbundle
		//Set button text
		//SetFont
		//Set Animation
		//Plat Animation
		init();
		
		ActionBarFontstuff();
		overflowStuff();
		
		unPackBundle();
		setButtonText();
		setFont();
		setAnimation();
		playAnimation();
		//MainActivity.fa.finish();
		retreveData();
		addData();
		saveData();
		MainActivity.fa.finish();
	}
	
	private void init()
	{
		tv_thank = (TextView) findViewById(R.id.tvThank);
		
		bt_cham = (Button) findViewById(R.id.btChameleon);
		bt_cham.setOnClickListener(this);
		
		bt_exit = (Button) findViewById(R.id.btExit);
		bt_exit.setOnClickListener(this);
		
		font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
	}
	
	private void unPackBundle()
	{
		gotBasket = getIntent().getExtras();
		stringA= gotBasket.getInt(KEY_SA);
		answer= gotBasket.getInt(KEY_AN);
		initPosition= gotBasket.getFloat(KEY_CP);
		finalPosition=  gotBasket.getFloat(KEY_FP)+30;
		buttonText= gotBasket.getString(KEY_BT);
		startTime= gotBasket.getLong(KEY_ST);
		timeToThink= gotBasket.getLong(KEY_TTT);
	}
	
	private void setButtonText()
	{
		bt_cham.setText(buttonText);
	}
	
	private void setFont()
	{
		tv_thank.setTypeface(font);
		tv_thank.setAlpha(0);
		
		bt_cham.setTypeface(font);
		
		bt_exit.setTypeface(font);
		bt_exit.setAlpha(0);
	}

	private void setAnimation()
	{
		long duration = 1000; //was 2000
		
		 ObjectAnimator moveupAnimation = ObjectAnimator.ofFloat(bt_cham, View.TRANSLATION_Y,initPosition,finalPosition);
		 moveupAnimation.setDuration(duration);
		 
		 ObjectAnimator fadeinAnimation = ObjectAnimator.ofFloat(tv_thank, View.ALPHA,0,1);
		 fadeinAnimation.setDuration(duration);
		 
		 ObjectAnimator fadeinAnimation2 = ObjectAnimator.ofFloat(bt_exit, View.ALPHA,0,1);
		 fadeinAnimation2.setDuration(duration);
	     
		 setButton = new AnimatorSet();
		 setButton.playSequentially(moveupAnimation,fadeinAnimation,fadeinAnimation2);
	}
	
	private void playAnimation()
	{
		setButton.start();
	}
	
	/**
	 * STUFF TO BE SAVED
	 * -WHICH SENTANCE WAS SHOWN
	 * -WHICH OPTION WAS CLICKED
	 * -DATE
	 * -HOW MANY TIMES THE APP WAS USED IN THE DAY
	 * -FOR HOW LONG WAS THE USER IN THE APP
	 */

	private void retreveData()
	{
		stats	 = getSharedPreferences(filename, 0);
		globalTimes = stats.getInt("GlobalTimes", 0);
		gtReal=globalTimes+1;
		
		/*
		int stringSentance[] = new int[(int) gtReal+1];
		int feelingOption[]= new int[(int) gtReal+1];
		long durationPerVisit[]= new long[(int) gtReal+1];
		long timeToThinkArray[] = new long[(int) gtReal+1];
		*/
		
		for(int i=0;i<gtReal;i++)
		{
			String iString = Integer.toString(i);
			
			stringSentance.add(stats.getInt(iString+"SS", 0));
			feelingOption.add(stats.getInt(iString+"FO", 0));
			durationPerVisit.add(stats.getLong(iString+"DPV", 0));
			timeToThinkArray.add(stats.getLong(iString+"TTT", 0));
			
		}
		
		//Change date 
	}
	
	private void addData()
	{
		stringSentance.add(stringA);
		feelingOption.add(answer);
		//durationPerVisit[(int) gtReal]=timeSpent;
		timeToThinkArray.add(timeToThink);
		
		durationPerVisit.add((long) 0);
	}
	
	private void saveData()
	{
		//Save in SharedPreferences
		SharedPreferences.Editor editor = stats.edit(); 						//same as  Editor ed; >>import ediot
		
		String gtReal_string = Long.toString(gtReal);

		editor.putInt(gtReal_string+"SS", stringA);
		editor.putInt(gtReal_string+"FO", answer);
		//editor.putLong(gtReal_string+"DPV", timeSpent);
		editor.putLong(gtReal_string+"TTT", timeToThink);
		
		editor.commit();
	}
	
	private void finalRites()
	{
		timeSpent=endTime-startTime;
		//durationPerVisit[(int) gtReal]=timeSpent;
		
		String gtReal_string = Long.toString(gtReal);
		SharedPreferences.Editor editor = stats.edit(); 
		editor.putLong(gtReal_string+"DPV", timeSpent);
		editor.commit();
	}
	
	@Override
	public void onBackPressed() 
	{
		finalRites();
		finish();
	}
	
	private static final String shareDialogTitle = "Share";
	private static final String shareSubject     = "Shared via Cookie";
	
	private static final String link             = "https://drive.google.com/folderview?id=0B0yBs8S3b4bdN3NmMWwyRko3eUk&usp=sharing";
	private static final String shareText        = "Download Cookie, to get motivated! " + link;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{ 
		switch (item.getItemId()) 
		{
		
			case R.id.action_share:
				shareDialog();
				return true;
		
			case R.id.action_update:
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
				startActivity(browserIntent);
				return true;
				
	        case R.id.menu_feedback:
	            // OpenDialog 
	        	askFeedback();
	            return true;
	    
	        case R.id.menu_more:
	        	signUp();
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}
	
	public void shareDialog()
	{

		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);

		startActivity(Intent.createChooser(shareIntent, shareDialogTitle));
	
	}
	
	private void askFeedback()
	{
		

		// Get activity_feedback.xml view
		LayoutInflater li = LayoutInflater.from(context);
		View feedbackView = li.inflate(R.layout.activity_feedback, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// Set activity_feedback.xml to alertdialog builder
		alertDialogBuilder.setView(feedbackView);

		final EditText userInput = (EditText) feedbackView.findViewById(R.id.etFeedback);
		userInput.setTypeface(font);

		final TextView tvDialog = (TextView) feedbackView.findViewById(R.id.tvFeedback);
		tvDialog.setTypeface(font);
		
		// Set dialog message
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id) 
			{
				// Get input and send it thru Parse
				sendFeedback(userInput.getText().toString());
			}
		});
		
		alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id) 
			{
				dialog.cancel();
			}
		});

		// Create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		
		
		// Show it
		alertDialog.show();

	}
	
	public void sendFeedback(String feedback)
	{
		//Upload to Parse
		final ParseObject pObject = new ParseObject("Feedback");
		pObject.put("message", feedback);
		pObject.put("user", ParseUser.getCurrentUser());
		
		pObject.saveInBackground(new SaveCallback() 
		{
			@Override
			public void done(ParseException e) 
			{
				if(e==null)
				{
					Log.i(TAG, "Message:DONE");
					Toast.makeText(FinalActivity.this, "Sent", Toast.LENGTH_LONG).show();
					
					//Send Daksh a PushMessage
					//sPush.sendPush(1, "daksh");
				}else
				{
					Log.i(TAG, "Error:Message" + e.getMessage());
					Log.i(TAG, "Error:Code"    + Integer.toString(e.getCode()));
					
					if(e.getCode() == 100)
					{
						Log.i(TAG, "Toast");
						Toast.makeText(FinalActivity.this, "No Internet Access", Toast.LENGTH_LONG).show();
					}
					
				}
			}
		});
		
		//Send Push
		//Update status & Notify
	
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		
		case R.id.btChameleon:
			contextualizeActionBar();
			break;
			
		case R.id.btExit:
			finalRites();
			finish();
			break;
		
		}
	}
	

	private void signUp()
	{
		// Get activity_feedback.xml view
		LayoutInflater li = LayoutInflater.from(context);
		View moreView = li.inflate(R.layout.activity_more, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// Set activity_feedback.xml to alertdialog builder
		alertDialogBuilder.setView(moreView);

		final EditText etSignUp = (EditText) moreView.findViewById(R.id.etMore);
		etSignUp.setTypeface(font);

		final TextView tvMore = (TextView) moreView.findViewById(R.id.tvMore);
		tvMore.setTypeface(font);
		
		final TextView tvMore2 = (TextView) moreView.findViewById(R.id.tvMore2);
		tvMore2.setTypeface(font);
		
		// Set dialog message
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id) 
			{
				// Get input and send it thru Parse
				sendFeedback(etSignUp.getText().toString());
			}
		});
		
		alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog,int id) 
			{
				dialog.cancel();
			}
		});

		// Create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		
		// Show it
		alertDialog.show();

	
	}
	
	
	private void contextualizeActionBar()
	{
		
	}
	
	
}
