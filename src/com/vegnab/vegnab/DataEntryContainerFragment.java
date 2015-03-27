package com.vegnab.vegnab;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vegnab.vegnab.contentprovider.ContentProvider_VegNab;
import com.vegnab.vegnab.database.VNContract.Loaders;
import com.vegnab.vegnab.database.VNContract.Prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataEntryContainerFragment extends Fragment
	implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String LOG_TAG = DataEntryContainerFragment.class.getSimpleName();
	public static final String TAG = DataEntryContainerFragment.class.getName();
	public static final String VISIT_ID = "VisitId";
	static long mVisitId = 0; // new or not specified yet
	static int mScreenToShow = 0; // default unless changed
//	static HashMap<String, String> mSubplotSpecs = new HashMap<String, String>();
//	static SparseArray<(HashMap<String>,<String>)> mPlotSpecs);
	static SparseArray<String> mSubplotNames = new SparseArray<String>();
	static Cursor mSubplotsCursor;
	private JSONObject mSubplotSpec = new JSONObject();
	private JSONArray mPlotSpecs = new JSONArray();


//	long mVisitId = 0, mSubplotTypeId = -1; // defaults for new or not specified yet
//	Uri mUri, mNamersUri = Uri.withAppendedPath(ContentProvider_VegNab.CONTENT_URI, "namers");
//	ContentValues mValues = new ContentValues();
//	HashMap<Long, String> mExistingNamers = new HashMap<Long, String>();
//	private EditText mEditNamerName;
//	private TextView mTxtNamerMsg;
//	String mTitle;
//	int mLayoutRes;
//	View mRootview;
	ViewPager mDataScreenPager = null;
//	FragmentStatePagerAdapter mAdapter = null;	
	
//	public static DataEntryContainerFragment newInstance(long visitId, long subplotTypeId, String stTitle) {
	public static DataEntryContainerFragment newInstance(Bundle args) {
		DataEntryContainerFragment f = new DataEntryContainerFragment();
		// supply arguments
//		Bundle args = new Bundle();
//		args.putLong("visitId", visitId);
//		args.putLong("subplotTypeId", subplotTypeId);
//		args.putString("stTitle", stTitle);
		f.setArguments(args);
		return f;
	}
	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
// set up any interfaces
//		try {
//        	mEditNamerListener = (EditNamerDialogListener) getActivity();
//        	Log.v(LOG_TAG, "(EditNamerDialogListener) getActivity()");
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Main Activity must implement EditNamerDialogListener interface");
//        }
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(LOG_TAG, "entered 'onCreateView'");
		Log.v(LOG_TAG, "in 'onCreateView' before getArguments, mVisitId = " + mVisitId);
		mVisitId = getArguments().getLong(VISIT_ID);
		Log.v(LOG_TAG, "in 'onCreateView' after getArguments, mVisitId = " + mVisitId);
		//View root = inflater.inflate(R.layout.fragment_parent_viewpager, container, false);
		View root = inflater.inflate(R.layout.fragment_data_entry_container, container, false);
// assign UI elements
		mDataScreenPager = (ViewPager) root.findViewById(R.id.data_entry_pager);
		Log.e(LOG_TAG, "About to call LoaderManager.initLoader CURRENT_SUBPLOTS");
		getLoaderManager().initLoader(Loaders.CURRENT_SUBPLOTS, null, this);
		Log.e(LOG_TAG, "Called LoaderManager.initLoader CURRENT_SUBPLOTS");
		if (savedInstanceState != null) {
			Log.v(LOG_TAG, "About to do 'getInt(dataPagePosition)', mScreenToShow=" + mScreenToShow);
			mVisitId = savedInstanceState.getLong(VISIT_ID);
			mScreenToShow = savedInstanceState.getInt("dataPagePosition");
			Log.v(LOG_TAG, "Completed 'getInt(dataPagePosition)', mScreenToShow=" + mScreenToShow);
		} else {
			Log.v(LOG_TAG, "savedInstanceState == null; mScreenToShow=" + mScreenToShow);
			// don't yet know why mScreenToShow is retained across re-entry, but explicitly reset it here
			Log.v(LOG_TAG, "resetting mScreenToShow to 0");
			mScreenToShow = 0;
		}
		// wait on the following till loader done
//        // must use ChildFragmentManager
//		mDataScreenPager.setAdapter(new dataPagerAdapter(getChildFragmentManager()));
        
//		mDataScreenPager = (ViewPager) mRootview.findViewById(R.id.data_entry_pager);
//		mDataScreenPager.setOffscreenPageLimit(1);
		
//		FragmentManager fm = getChildFragmentManager();
//		mAdapter = new dataPagerAdapter(fm);
//		Log.v(LOG_TAG, "about to do 'setAdapterTask().execute()'");
//		new setAdapterTask().execute();
//		Log.v(LOG_TAG, "did 'setAdapterTask().execute()'");
//		mTxtNamerMsg = (TextView) mRootview.findViewById(R.id.lbl_namer);
//		mEditNamerName = (EditText) mRootview.findViewById(R.id.txt_edit_namer);
//		// attempt to automatically show soft keyboard
//		mEditNamerName.requestFocus();
//		getDialog().getWindow().setSoftInputMode(android.mRootview.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//		mEditNamerName.setOnFocusChangeListener(this);

//		if (mNamerRecId == 0) { // new record
//			getDialog().setTitle(R.string.add_namer_title);
//		} else { // existing record being edited
//			getDialog().setTitle(R.string.edit_namer_title_edit);
//		}
		Log.v(LOG_TAG, "about to return from 'onCreateView'");
		return root;
	}
	
/*
	private class setAdapterTask extends AsyncTask<Void,Void,Void>{
	      protected Void doInBackground(Void... params) {
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Void result) {
	        	mDataScreenPager.setAdapter(mAdapter);
	        }
	}
*/
	
//	@Override	
//	public void onClick(View v) {
//		// don't need onClick here
//	}

/*
	@Override
	public void onStart() {
		super.onStart();
		// during startup, check if arguments are passed to the fragment
		// this is where to do this because the layout has been applied
		// to the fragment
		Bundle args = getArguments();
		
		if (args != null) {
			mVisitId = args.getLong("visitId");
			mSubplotTypeId = args.getLong("subplotTypeId");
			mTitle = args.getString("stTitle");
//			// request existing Namers ASAP, this doesn't use the UI
//			getLoaderManager().initLoader(Loaders.EXISTING_NAMERS, null, this);
//			getLoaderManager().initLoader(Loaders.NAMER_TO_EDIT, null, this);
//			// will insert values into screen when cursor is finished
		}
//		if (mVisitId == 0) { // new record
//			mTxtNamerMsg.setText(R.string.add_namer_header);
//		} else { // existing record being edited
//			mTxtNamerMsg.setText(mTitle);
//		}
	}
*/
	
	public static class dataPagerAdapter extends FragmentStatePagerAdapter {
	
		public dataPagerAdapter(FragmentManager fm) {
			super(fm);
		}
	
		@Override
        public Fragment getItem(int position) {
			Log.v(LOG_TAG, "called dataPagerAdapter 'getItem' " + position);
            Bundle args = new Bundle();
            args.putInt(VegSubplotFragment.POSITION_KEY, position);
            mSubplotsCursor.moveToPosition(position);
            args.putLong(VegSubplotFragment.VISIT_ID, mSubplotsCursor.getLong(
            		mSubplotsCursor.getColumnIndexOrThrow("VisitId")));
            args.putString(VegSubplotFragment.VISIT_NAME, mSubplotsCursor.getString(
            		mSubplotsCursor.getColumnIndexOrThrow("VisitName")));
            args.putLong(VegSubplotFragment.SUBPLOT_TYPE_ID, mSubplotsCursor.getLong(
            		mSubplotsCursor.getColumnIndexOrThrow("SubplotTypeId")));
            args.putBoolean(VegSubplotFragment.PRESENCE_ONLY, ((mSubplotsCursor.getInt(
            		mSubplotsCursor.getColumnIndexOrThrow("PresenceOnly")) == 0) ? false : true));
            return VegSubplotFragment.newInstance(args);
        }

/*		public Fragment getItem(int dataScreenIndex) {
			Log.v(LOG_TAG, "called dataPagerAdapter 'getItem' with value " + dataScreenIndex);
			Fragment dataEntryFrag = TestPagerFragment.newInstance(0, 0, "test");
	 may need some switching later		
			switch (dataScreenIndex) {
			case 0:
				dataEntryFrag = new TestPagerFragment();
				break;		
			
			case 1:
				dataEntryFrag = new TestPagerFragment();
				break;
				
			case 2:
				dataEntryFrag = new TestPagerFragment();
				break;		
			}
			
			return dataEntryFrag;
		}
*/	
		@Override
		public int getCount() {
			Log.v(LOG_TAG, "called dataPagerAdapter 'getCount'");
			return mSubplotsCursor.getCount();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			mSubplotsCursor.moveToPosition(position);
            return mSubplotsCursor.getString(
            		mSubplotsCursor.getColumnIndexOrThrow("SubplotDescription"));
		}
	}

	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.
		// switch out based on id
		CursorLoader cl = null;
		Uri baseUri;
		String select = null; // default for all-columns, unless re-assigned or overridden by raw SQL
		switch (id) {
		case Loaders.CURRENT_SUBPLOTS:
			baseUri = ContentProvider_VegNab.SQL_URI;
			select = "SELECT Visits._id AS VisitId, Visits.VisitName, SubplotTypes._id AS SubplotTypeId, "
					+ "SubplotTypes.ParentPlotCode, SubplotTypes.SubplotDescription, "
					+ "SubplotTypes.PresenceOnly, SubplotTypes.HasNested, "
					+ "SubplotTypes.NestedInId, SubplotTypes.NestedInName "
					+ "FROM Visits LEFT JOIN SubplotTypes "
					+ "ON Visits.PlotTypeID = SubplotTypes.PlotTypeID "
					+ "WHERE (((Visits._id)=" + mVisitId + ")) "
					+ "ORDER BY SubplotTypes.OrderDone, SubplotTypes._id;";
			cl = new CursorLoader(getActivity(), baseUri,
					null, select, null, null);
			break;		
		}
		return cl;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor finishedCursor) {
		// there will be various loaders, switch them out here
//		mRowCt = finishedCursor.getCount();
		switch (loader.getId()) {
		case Loaders.CURRENT_SUBPLOTS:
			// store a reference to the cursor
			mSubplotsCursor = finishedCursor;
			// store the list of subplots
			mPlotSpecs = new JSONArray(); // clear the array
			mSubplotNames.clear();
			Log.v(LOG_TAG, "In 'onLoadFinished', mPlotSpecs=" + mPlotSpecs.toString());
			while (finishedCursor.moveToNext()) {
				mSubplotNames.append(finishedCursor.getInt(finishedCursor.getColumnIndexOrThrow("SubplotTypeId")), 
						finishedCursor.getString(finishedCursor.getColumnIndexOrThrow("SubplotDescription")));
				mSubplotSpec = new JSONObject();
				// for now, only put the Subplot ID number
				try {
					mSubplotSpec.put("subplotId", finishedCursor.getInt(finishedCursor.getColumnIndexOrThrow("SubplotTypeId")));
					mSubplotSpec.put("plotTypeCode", finishedCursor.getString(finishedCursor.getColumnIndexOrThrow("ParentPlotCode")));
					mSubplotSpec.put("sbpDescription", finishedCursor.getString(finishedCursor.getColumnIndexOrThrow("SubplotDescription")));
					mSubplotSpec.put("presenceOnly", finishedCursor.getInt(finishedCursor.getColumnIndexOrThrow("PresenceOnly")));
					mSubplotSpec.put("hasNested", finishedCursor.getInt(finishedCursor.getColumnIndexOrThrow("HasNested")));
					mSubplotSpec.put("nstInId", finishedCursor.getInt(finishedCursor.getColumnIndexOrThrow("NestedInId")));
					mSubplotSpec.put("nstInName", finishedCursor.getString(finishedCursor.getColumnIndexOrThrow("NestedInName")));
				} catch (JSONException e) {
					Log.v(LOG_TAG, "In 'onLoadFinished', JSON error: " + e.getMessage());
				}
				// can put in the auxiliary data specs, here or in post-processing
				mPlotSpecs.put(mSubplotSpec);
			}
	        // must use ChildFragmentManager
			mDataScreenPager.setAdapter(new dataPagerAdapter(getChildFragmentManager()));
			Log.v(LOG_TAG, "About to do mDataScreenPager.setCurrentItem(mScreenToShow)=" + mScreenToShow);
			mDataScreenPager.setCurrentItem(mScreenToShow);
			Log.v(LOG_TAG, "Just did mDataScreenPager.setCurrentItem(mScreenToShow)=" + mScreenToShow);

//			mPlotSpecsNewlySetUp = true;
//			// use a callback to continue program flow outside this fn, where direct calls to 
//			// 'dispatchDataEntryScreen' are not legal
//			Log.v(LOG_TAG, "In 'onLoadFinished', about to create message");
//			Message msgPlotSpecsDone = Message.obtain();
//			Log.v(LOG_TAG, "In 'onLoadFinished', about to create callback");
//			Callback cbkSbsDone = new Callback() {

//				@Override
//				public boolean handleMessage(Message msg) {
//					// execute this during callback
//					Log.v(LOG_TAG, "In 'cbkSbsDone' callback, about to call 'dispatchDataEntryScreen'");
//					dispatchDataEntryScreen();
//					Log.v(LOG_TAG, "In 'cbkSbsDone' callback, after call to 'dispatchDataEntryScreen'");
//					return false;
//				}
//			};
//			Log.v(LOG_TAG, "In 'onLoadFinished', about to create handler");
//			Handler hnd = new Handler(cbkSbsDone);
//			Log.v(LOG_TAG, "In 'onLoadFinished', about to send message");
//			hnd.sendMessage(msgPlotSpecsDone);
//			Log.v(LOG_TAG, "In 'onLoadFinished', message sent");
			break;
		}
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// is about to be closed. Need to make sure it is no longer is use.
		switch (loader.getId()) {
		case Loaders.CURRENT_SUBPLOTS:
			// do we need to do any cleanup here?
			// following line crashes app:
//			mDataScreenPager.setAdapter(null);
			break;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.v(LOG_TAG, "In 'onSaveInstanceState', about to save dataPagePosition; mDataScreenPager.getCurrentItem() = " + mDataScreenPager.getCurrentItem());
		outState.putInt("dataPagePosition", mDataScreenPager.getCurrentItem());
		outState.putLong(VISIT_ID, mVisitId);
		super.onSaveInstanceState(outState);
	}
}

