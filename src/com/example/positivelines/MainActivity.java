package com.example.positivelines;

import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends Activity implements OnClickListener
{
	/**
	 * !!CREATE AN ANALYTICS CLASS LATER ON 
	 * --LIST ETC
	 * --ANALYTICS
	 * 
	 * --ACTION BAR CUSTOM-text color(normal+pressed));typeface;icon
	 * --DIALOG' FEEDBACK;FAQ  ETC
	 * 
	 * !!OVERFLOW TEXT COLOR(PRESSED)
	 * --DIALOG EDITTEXT COLOR CHANGE
	 * 
	 * UPLOADING ALL DATA TO PARSE
	 * SHARING WITH FRIENDS; RETREVE FRIENDLIST ETC
	 * 
	 * FRIENDS CAN SEND OTHER FRIENDS HAPPY MESSAGES
	 * DISPLAY ON THEIR HOME THE NEXT MORENING
	 * 
	 * CONTEXTUAL ACTIONBAR (LIKE GOOGLE+)
	 * 'MORE'- OVERFLOWMENU (SIGN UP FOR WOOFERALPHA)
	 */

	public static Activity fa;
	final Context context = this;
	Typeface font;

	TextView tv1, tv2, tv3;
	TextView tvQ;
	
	Random r = new Random();

	int a, b;

    AnimatorSet setAnimation1;
    AnimatorSet setAnimation2;
    AnimatorSet setAnimation3;
    
    AnimatorSet setQuestion;
    
    AnimatorSet setExit1;
    AnimatorSet setExit2;
    AnimatorSet setExit3;
    AnimatorSet setExit4;
    AnimatorSet setExit5;
    
	private static final AccelerateInterpolator sAccelerator = new AccelerateInterpolator();
	
	Button bt1,bt2,bt3,bt4,bt5;
	
	final String TAG="COOKIE";

	String[][] lines= 
		{
				{"YOU'RE", "AWESOME!"},
				{"YOU","LOOK","PRETTY :)"},
				{"HAVE","A","ROCKING DAY!"},
				{"GREAT!","GO GET EM'","TIGER!"},
				{"YOU'RE","PHENOMENAL", "IN BED ;)"},
				{"YOU","GET AN","A+"},
				{"I","LIKE YOUR","STYLE"},
				{"I LIKE","THE WAY","YOU MOVE ;)"},
				{"WELL","DONE"},
				{"YOU'RE","REALLY FUN!"},
				{"YOU'RE","BEAUTIFUL"},
				
				{"YOU'RE","BEAUTIFUL"},
				{"YOU'RE","STRONG"},
				{"NO ONE","CAN STOP","YOU NOW"},
				{"YOU'RE","LOVED"},
				{"YOU'RE","INTELLIGENT"},
				{"DONT","FORGET TO","SMILE :)"} 
		};

	int stringA;
	int answer;
	
	float currentButtonPosition;
	float finalButtonPosition; //This will be the initY position of Question TV
	
	String buttonText=null;
	
	String KEY_SA="STRINGA";
	String KEY_AN="ANSWER";
	String KEY_CP="CURRENTP";
	String KEY_FP="FINALP";
	String KEY_BT="BUTTONT";
	String KEY_ST="STARTTIME";
	String KEY_TTT="TIMETOTHINK";
	
	Bundle basket;
	
	boolean animOver=false;
	
	long startTime=0;
	
	long showOption;
	long clickOption;
	long timeToThink;
	
	Intent aIntent;
	
	private void ActionBarFontstuff()
	{
		int titleId = getResources().getIdentifier("action_bar_title", "id","android");
	    TextView yourTextView = (TextView) findViewById(titleId);
	  //  yourTextView.setTextColor(getResources().getColor(R.color.black));
	    yourTextView.setTypeface(font);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startTime = System.currentTimeMillis();
		 fa = this;
		init();
		ActionBarFontstuff();
		setText();
		setFont();
		
		if(ParseUser.getCurrentUser()==null)
		{
			createUser();
			assignInstallation();
		}
		
		assignInstallation();
		
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		
		/*
		Map<String, String> dimensions = new HashMap<String, String>();
		
		// Define ranges to bucket data points into meaningful segments
		dimensions.put("priceRange", "1000-1500");
		// Did the user filter the query?
		dimensions.put("source", "craigslist");
		// Do searches happen more often on weekdays or weekends?
		dimensions.put("dayType", "weekday");
		
		// Send the dimensions to Parse along with the 'search' event
		ParseAnalytics.trackEventInBackground("time", dimensions);
		*/
		
		setTextAnimation();
		playTextAnimation();
		
		//now we need the feedback thing to happen
		//fade in with a slight top to bottom movement
		//upset;bad;neutral;good;awesome
		 //setButtonAnimation();
		 //playButtonAnimation();
		
		//checkAnimation();
		
		//Prepare all exit animations
		prepareExitAnimation();
		//Play them when the button is pressed
	}
	
	public void createUser()
	{
		ParseAnonymousUtils.logIn(new LogInCallback() 
		{
			@Override
			public void done(ParseUser user, ParseException e) 
			{
				if(e==null)
				{
					Log.i(TAG, "Login:DONE");
					//Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_LONG).show();
					
					//Send Daksh a PushMessage
					//sPush.sendPush(1, "daksh");
				}else
				{
					Log.i(TAG, "Error:Login" + e.getMessage());
					Log.i(TAG, "Error:Code"    + Integer.toString(e.getCode()));
					
				}
			}
		});
	}
	
	public void assignInstallation()
	{
		// Save the current Installation to Parse.
		//ParseInstallation.getCurrentInstallation().saveInBackground();
		
		// When users indicate they are Giants fans, we subscribe them to that channel.
		ParsePush.subscribeInBackground("Cookie");
		
		// Associate the device with a user
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("user", ParseUser.getCurrentUser());
		
		installation.saveInBackground(new SaveCallback() 
		{
			
			@Override
			public void done(ParseException e) 
			{
				if(e==null)
				{
					Log.i(TAG, "Installation Done");

				}else
				{
					Log.i(TAG, "InstallationError:Message" + e.getMessage());
					Log.i(TAG, "InstallationError:Code"    + Integer.toString(e.getCode()));
				}
			}
		});
	}
	
	private void init()
	{
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		
		tvQ = (TextView) findViewById(R.id.tvQuestion);
		finalButtonPosition = tvQ.getY();
		
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt5 = (Button) findViewById(R.id.bt5);
		
		nullListners();
		
		font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
	}
	
	private void setText()
	{
		a = r.nextInt(lines.length);
		b = lines[a].length;

		tv1.setText(lines[a][0]);
		tv2.setText(lines[a][1]);
		
		if (b == 2) 
		{
			tv3.setText("");
		} else 
		{
			tv3.setText(lines[a][2]);
		}
		
		stringA=a;
	}
	
	private void setFont()
	{
		tv1.setTypeface(font);
		tv2.setTypeface(font);
		tv3.setTypeface(font);
		
		tvQ.setTypeface(font);
		tvQ.setAlpha(0);
		
		bt1.setTypeface(font);
		bt2.setTypeface(font);
		bt3.setTypeface(font);
		bt4.setTypeface(font);
		bt5.setTypeface(font);
		
		bt1.setAlpha(0);
		bt2.setAlpha(0);
		bt3.setAlpha(0);
		bt4.setAlpha(0);
		bt5.setAlpha(0);
	}

	private void setTextAnimation()
	{
		//Fly in
		//Stay
		//Fly out & Fade out
		
		long duration=900;
	      
        ObjectAnimator flyinAnimation1 = ObjectAnimator.ofFloat(tv1, View.TRANSLATION_X,-300, 10);
        //flyinAnimation1.setInterpolator(sAccelerator);
        flyinAnimation1.setDuration(duration*1);
        PropertyValuesHolder pvhX1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,1200);
        PropertyValuesHolder pvhY1 = PropertyValuesHolder.ofFloat(View.ALPHA, -2);
        ObjectAnimator flyoutAnimation1 = ObjectAnimator.ofPropertyValuesHolder(tv1, pvhX1, pvhY1);
        //flyoutAnimation1.setInterpolator(sAccelerator);
        flyoutAnimation1.setDuration(duration);
        flyoutAnimation1.setStartDelay(2000);
        
        ObjectAnimator flyinAnimation2 = ObjectAnimator.ofFloat(tv2, View.TRANSLATION_X,-600, 10);
        //flyinAnimation2.setInterpolator(sAccelerator);
        flyinAnimation2.setDuration(duration*2);
        //flyinAnimation2.setStartDelay(1000);
        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,1200);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat(View.ALPHA, -2);
        ObjectAnimator flyoutAnimation2 = ObjectAnimator.ofPropertyValuesHolder(tv2, pvhX2, pvhY2);
        //flyoutAnimation2.setInterpolator(sAccelerator);
        flyoutAnimation2.setDuration(duration);
        flyoutAnimation2.setStartDelay(2000);
        
        ObjectAnimator flyinAnimation3 = ObjectAnimator.ofFloat(tv3, View.TRANSLATION_X,-900, 10);
        //flyinAnimation3.setInterpolator(sAccelerator);
        flyinAnimation3.setDuration(duration*3);
        //flyinAnimation3.setStartDelay(2000);
        PropertyValuesHolder pvhX3 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,1200);
        PropertyValuesHolder pvhY3 = PropertyValuesHolder.ofFloat(View.ALPHA, -2);
        ObjectAnimator flyoutAnimation3 = ObjectAnimator.ofPropertyValuesHolder(tv3, pvhX3, pvhY3);
        //flyoutAnimation3.setInterpolator(sAccelerator);
        flyoutAnimation3.setDuration(duration);
        flyoutAnimation3.setStartDelay(2000);
        
        setAnimation1 = new AnimatorSet();
        setAnimation1.playSequentially(flyinAnimation1, flyoutAnimation1);
        
        setAnimation2 = new AnimatorSet();
        setAnimation2.playSequentially(flyinAnimation2, flyoutAnimation2);
        
        setAnimation3 = new AnimatorSet();
        setAnimation3.playSequentially(flyinAnimation3, flyoutAnimation3);
        
        flyoutAnimation3.addListener(new AnimatorListener() 
		{
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				setButtonAnimation();
				playButtonAnimation();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
    }
	
	private void playTextAnimation()
	{	
    	setAnimation1.start();
		setAnimation2.start();
		setAnimation3.start();
	}

	private void setButtonAnimation()
	{
		long duration =900;
		
		//Animate question
		ObjectAnimator fadeinAnimationQ = ObjectAnimator.ofFloat(tvQ, View.ALPHA,0,1);
		fadeinAnimationQ.setDuration(duration+300);
		//fadeinAnimationQ.setStartDelay(7000);
		
		//fade in
		//move downwards while fading in

        PropertyValuesHolder pvhXB1 = PropertyValuesHolder.ofFloat(View.ALPHA,0,1);
        PropertyValuesHolder pvhYB1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,95,100);
		ObjectAnimator fadeinAnimation1 = ObjectAnimator.ofPropertyValuesHolder(bt1, pvhXB1,pvhYB1);
		fadeinAnimation1.setDuration(duration);
		//fadeinAnimation1.setStartDelay(8000);

        PropertyValuesHolder pvhXB2 = PropertyValuesHolder.ofFloat(View.ALPHA,0,1);
        PropertyValuesHolder pvhYB2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,125,130);
		ObjectAnimator fadeinAnimation2 = ObjectAnimator.ofPropertyValuesHolder(bt2, pvhXB2,pvhYB2);
		fadeinAnimation2.setDuration(duration);

        PropertyValuesHolder pvhXB3 = PropertyValuesHolder.ofFloat(View.ALPHA,0,1);
        PropertyValuesHolder pvhYB3 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,155,160);
		ObjectAnimator fadeinAnimation3 = ObjectAnimator.ofPropertyValuesHolder(bt3, pvhXB3,pvhYB3);
		fadeinAnimation3.setDuration(duration);

        PropertyValuesHolder pvhXB4 = PropertyValuesHolder.ofFloat(View.ALPHA,0,1);
        PropertyValuesHolder pvhYB4 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,185,190);
		ObjectAnimator fadeinAnimation4 = ObjectAnimator.ofPropertyValuesHolder(bt4, pvhXB4,pvhYB4);
		fadeinAnimation4.setDuration(duration);

        PropertyValuesHolder pvhXB5 = PropertyValuesHolder.ofFloat(View.ALPHA,0,1);
        PropertyValuesHolder pvhYB5 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,215,220);
		ObjectAnimator fadeinAnimation5 = ObjectAnimator.ofPropertyValuesHolder(bt5, pvhXB5,pvhYB5);
		fadeinAnimation5.setDuration(duration);
		
		fadeinAnimation5.addListener(new AnimatorListener() 
		{
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				assignListners();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});

		setQuestion = new AnimatorSet();
		setQuestion.playSequentially(fadeinAnimationQ,fadeinAnimation1,fadeinAnimation2,fadeinAnimation3,fadeinAnimation4,fadeinAnimation5);
        //setButton1.playTogether(fadeinAnimationQ,fadeinAnimation1,fadeinAnimation2,fadeinAnimation3,fadeinAnimation4,fadeinAnimation5);
	}
	
	private void playButtonAnimation()
	{
		setQuestion.start();
	}
	
	private void checkAnimation()
	{
		//When the animation is not running
		while(true)
		{
			//Check if animation has ended
			if(setQuestion.isRunning() == false)
			{
				assignListners();
				break;
			}
		}
	}

	private void prepareExitAnimation()
	{
		//Question fades out
		ObjectAnimator fadeoutAnimationQ = ObjectAnimator.ofFloat(tvQ, View.ALPHA,1,0);
		fadeoutAnimationQ.setDuration(1000);
		//fadeinAnimationQ.setStartDelay(8000);
		
		//Buttons
		//Fade out
		//Move down slitely
		PropertyValuesHolder pvhXC1 = PropertyValuesHolder.ofFloat(View.ALPHA,1,0);
	    PropertyValuesHolder pvhYC1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,100,105);
		ObjectAnimator fadeoutAnimation1 = ObjectAnimator.ofPropertyValuesHolder(bt1, pvhXC1,pvhYC1);
		fadeoutAnimation1.setDuration(1000);

		PropertyValuesHolder pvhXC2 = PropertyValuesHolder.ofFloat(View.ALPHA,1,0);
	    PropertyValuesHolder pvhYC2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,130,135);
		ObjectAnimator fadeoutAnimation2 = ObjectAnimator.ofPropertyValuesHolder(bt2, pvhXC2,pvhYC2);
		fadeoutAnimation2.setDuration(1000);

		PropertyValuesHolder pvhXC3 = PropertyValuesHolder.ofFloat(View.ALPHA,1,0);
	    PropertyValuesHolder pvhYC3 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,160,165);
		ObjectAnimator fadeoutAnimation3 = ObjectAnimator.ofPropertyValuesHolder(bt3, pvhXC3,pvhYC3);
		fadeoutAnimation3.setDuration(1000);
		
		PropertyValuesHolder pvhXC4 = PropertyValuesHolder.ofFloat(View.ALPHA,1,0);
	    PropertyValuesHolder pvhYC4 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,190,195);
		ObjectAnimator fadeoutAnimation4 = ObjectAnimator.ofPropertyValuesHolder(bt4, pvhXC4,pvhYC4);
		fadeoutAnimation4.setDuration(1000);
		
		PropertyValuesHolder pvhXC5 = PropertyValuesHolder.ofFloat(View.ALPHA,1,0);
	    PropertyValuesHolder pvhYC5 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,220,225);
		ObjectAnimator fadeoutAnimation5 = ObjectAnimator.ofPropertyValuesHolder(bt5, pvhXC5,pvhYC5);
		fadeoutAnimation5.setDuration(1000);
		
		
		//Animator set
		setExit1 = new AnimatorSet();
		setExit1.playTogether(fadeoutAnimationQ, fadeoutAnimation2, fadeoutAnimation3, fadeoutAnimation4, fadeoutAnimation5);
        
		setExit2 = new AnimatorSet();
		//setExit2.playSequentially(fadeoutAnimationQ, fadeoutAnimation1, fadeoutAnimation3);
		setExit2.playTogether(fadeoutAnimationQ, fadeoutAnimation1, fadeoutAnimation3, fadeoutAnimation4, fadeoutAnimation5);
		
		setExit3 = new AnimatorSet();
		setExit3.playTogether(fadeoutAnimationQ, fadeoutAnimation1, fadeoutAnimation2, fadeoutAnimation4, fadeoutAnimation5);
		
		setExit4 = new AnimatorSet();
		setExit4.playTogether(fadeoutAnimationQ, fadeoutAnimation1, fadeoutAnimation2, fadeoutAnimation3, fadeoutAnimation5);
		
		setExit5 = new AnimatorSet();
		setExit5.playTogether(fadeoutAnimationQ, fadeoutAnimation1, fadeoutAnimation2, fadeoutAnimation3, fadeoutAnimation4);
        
	}
	
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		
		case R.id.bt1:
			nullListners();
			setExit1.start();
			
			currentButtonPosition = bt1.getY();
			answer=2;
			buttonText="AWESOME";
			
			sendMood(buttonText, answer);
			
			bundleUp();
			startNewClass(setExit1);
			break;
			
		case R.id.bt2:
			nullListners();
			setExit2.start();
			
			currentButtonPosition = bt2.getY();
			answer=1;
			buttonText="GOOD";
			
			sendMood(buttonText, answer);
			
			bundleUp();
			startNewClass(setExit2);
			break;
		
		case R.id.bt3:
			nullListners();
			setExit3.start();
			
			currentButtonPosition = bt3.getY();
			answer=0;
			buttonText="NEUTRAL";
			
			sendMood(buttonText, answer);
			
			bundleUp();
			startNewClass(setExit3);
			break;
			
		case R.id.bt4:
			nullListners();
			setExit4.start();
			
			currentButtonPosition = bt4.getY();
			answer=-1;
			buttonText="BAD";
			
			sendMood(buttonText, answer);
			
			bundleUp();
			startNewClass(setExit4);
			break;
			
		case R.id.bt5:
			nullListners();
			setExit5.start();
			
			currentButtonPosition = bt5.getY();
			answer=-2;
			buttonText="UPSET";
			
			sendMood(buttonText, answer);
			
			bundleUp();
			startNewClass(setExit5);
			break;
		
		}
		
	}
	
	private void bundleUp()
	{
		clickOption = System.currentTimeMillis();
		timeToThink = clickOption-showOption;
		
		// stringA;answer;currentpos;finalpos;
		basket = new Bundle();
		basket.putInt(KEY_SA, stringA);
		basket.putInt(KEY_AN, answer);
		basket.putFloat(KEY_CP, currentButtonPosition);
		basket.putFloat(KEY_FP, finalButtonPosition);
		basket.putString(KEY_BT, buttonText);
		basket.putLong(KEY_ST, startTime);
		basket.putLong(KEY_TTT, timeToThink);
	}
	
	private void startNewClass(AnimatorSet aSetFinal)
	{
		aIntent = new Intent(this, FinalActivity.class);
		aIntent.putExtras(basket);
		aIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		//Check if current animation is over
		//if(animOver=true)
		//startActivity(a);	
		//this.finish();
		
		aSetFinal.addListener(new AnimatorListener() 
		{
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				startActivity(aIntent);
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void assignListners()
	{
		showOption = System.currentTimeMillis();
		
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
	}
	
	private void nullListners()
	{
		bt1.setOnClickListener(null);
		bt2.setOnClickListener(null);
		bt3.setOnClickListener(null);
		bt4.setOnClickListener(null);
		bt5.setOnClickListener(null);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main3, menu);
		return true;
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
	            // OpenDialog
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
					Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_LONG).show();
					
					//Send Daksh a PushMessage
					//sPush.sendPush(1, "daksh");
				}else
				{
					Log.i(TAG, "Error:Message" + e.getMessage());
					Log.i(TAG, "Error:Code"    + Integer.toString(e.getCode()));
					
					if(e.getCode() == 100)
					{
						Log.i(TAG, "Toast");
						Toast.makeText(MainActivity.this, "No Internet Access", Toast.LENGTH_LONG).show();
					}
					
				}
			}
		});
		
		//Send Push
		//Update status & Notify
	
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
	
	public void sendMood(String moodString, int moodInt)
	{

		//Upload to Parse
		final ParseObject pObject = new ParseObject("Mood");
		pObject.put("moodString", moodString);
		pObject.put("moodInt", moodInt);
		pObject.put("user", ParseUser.getCurrentUser());
		
		pObject.saveEventually(new SaveCallback() 
		{
			@Override
			public void done(ParseException e) 
			{
				if(e==null)
				{
					Log.i(TAG, "Mood:DONE");
					//Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_LONG).show();
					
					//Send Daksh a PushMessage
					//sPush.sendPush(1, "daksh");
				}else
				{
					Log.i(TAG, "Error:Mood" + e.getMessage());
					Log.i(TAG, "Error:Code"    + Integer.toString(e.getCode()));
					
					/*
					if(e.getCode() == 100)
					{
						Log.i(TAG, "Toast");
						Toast.makeText(MainActivity.this, "No Internet Access", Toast.LENGTH_LONG).show();
					}
					*/
				}
			}
		});
		
		//Send Push
		//Update status & Notify
	}
	
}
