package com.vegnab.vegnab;

import java.util.List;

import com.vegnab.vegnab.contentprovider.ContentProvider_Projects;
import com.vegnab.vegnab.database.VegNabDbHelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemSelectedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class NewVisitFragment extends Fragment implements OnClickListener,
		LoaderManager.LoaderCallbacks<Cursor>{
//	, OnItemSelectedListener
	int ProjectId = 0;
	final static String ARG_SUBPLOT = "subplot";
	int mCurrentSubplot = -1;
	VegNabDbHelper DbHelper;
	Spinner projSpinner;
	SimpleCursorAdapter mProjAdapter; // to link the Projects spinner data
	OnButtonListener mButtonCallback; // declare the interface
	// declare that the container Activity must implement this interface
	public interface OnButtonListener {
		// methods that must be implemented in the container Activity
		public void onNewVisitGoButtonClicked();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// if the activity was re-created (e.g. from a screen rotate)
		// restore the previous screen, remembered by onSaveInstanceState()
		// This is mostly needed in fixed-pane layouts
		if (savedInstanceState != null) {
			mCurrentSubplot = savedInstanceState.getInt(ARG_SUBPLOT);
		}
		// inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_new_visit, container, false);
//		projSpinner.setOnItemSelectedListener((android.widget.AdapterView.OnItemSelectedListener) this);
//		loadProjSpinnerItems();
		// set click listener for the button in the view
		Button b = (Button) rootView.findViewById(R.id.new_visit_go_button);
		b.setOnClickListener(this);
		// if more, loop through all the child items of the ViewGroup rootView and 
		// set the onclicklistener for all the Button instances found
		// Create an empty adapter we will use to display the list of Projects
		mProjAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_spinner_item, null,
				new String[] {"ProjCode"},
				new int[] {android.R.id.text1}, 0);
		mProjAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		projSpinner = (Spinner) rootView.findViewById(R.id.sel_project_spinner);
		projSpinner.setAdapter(mProjAdapter);
		// Prepare the loader. Either re-connect with an existing one or start a new one
		getLoaderManager().initLoader(0, null, this);
		// Since there in no Loader yet, this will call
		// Loader<Cursor> onCreateLoader and pass it a first parameter of 0
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// during startup, check if arguments are passed to the fragment
		// this is where to do this because the layout has been applied
		// to the fragment
		Bundle args = getArguments();
		if (args != null) {
			// set up subplot based on arguments passed in
			updateSubplotViews(args.getInt(ARG_SUBPLOT));
		} else if (mCurrentSubplot != -1) {
			// set up subplot based on saved instance state defined in onCreateView
			updateSubplotViews(mCurrentSubplot);
		} else {
			updateSubplotViews(-1); // figure out what to do for default state 
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		DbHelper = new VegNabDbHelper(activity);
		// assure the container activity has implemented the callback interface
		try {
			mButtonCallback = (OnButtonListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException (activity.toString() + " must implement OnButtonListener");
		}
	}
	
	public void updateSubplotViews(int subplotNum) {
		// don't do anything yet
		// figure out how to deal with default of -1
		mCurrentSubplot = subplotNum;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save the current subplot arguments in case we need to re-create the fragment
		outState.putInt(ARG_SUBPLOT, mCurrentSubplot);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_visit_go_button:
			if (ProjectId == 0) {
				Toast.makeText(this.getActivity(),
						"" + getResources().getString(R.string.missing_project),
						Toast.LENGTH_SHORT).show();
				return;
			}
			mButtonCallback.onNewVisitGoButtonClicked();
			break;
		}
	}

	private void loadProjSpinnerItems() {
		List<String> projCodes = DbHelper.getProjectsAsList();
//		Cursor prjCursor = DbHelper.getProjectsAsCursor();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_spinner_item, projCodes);
//		String[] columns = new String[] { "Project" };
//		Int [] to = new Int[] {};
//		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(this.getActivity(), 
//				android.R.layout.simple_spinner_item, prjCursor, null, null, mCurrentSubplot);
		
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		projSpinner.setAdapter(dataAdapter);
		
	}
	// define the columns we will retrieve from the Projects table
	static final String[] PROJECTS_PROJCODES = new String[] {
		"_id", "ProjCode",
	};

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.
		// So far, there is only one loader so don't need to switch
		// based on the id.
		// First, create the base URI
		Uri baseUri;
		// could test here, based on e.g. filters
		baseUri = ContentProvider_Projects.CONTENT_URI; // get the whole list
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the dataset being displayed
		// Could build a WHERE clause such as
		// String select = "(Default = true)";
		String select = null;
		return new CursorLoader(getActivity(), baseUri,
				PROJECTS_PROJCODES, select, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor projCursor) {
		// Swap the new cursor in.
		// The framework will take care of closing the old cursor once we return.
		// if there were various Loaders, switch them out here
		mProjAdapter.swapCursor(projCursor);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// This is called when the last Cursor provided to onLoadFinished()
		// is about to be closed. Need to make sure it is no longer is use.
		mProjAdapter.swapCursor(null);
	}

	/*
	 * 
	@Override
	public void onItemSelected(AdapterViewCompat<?> parent, View view, int position,
			long id) {
		String strSel = parent.getItemAtPosition(position).toString();
		Toast.makeText(parent.getContext(), "Project selected: " + strSel, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterViewCompat<?> arg0) {
		// TODO Auto-generated method stub
	}
*/
}
