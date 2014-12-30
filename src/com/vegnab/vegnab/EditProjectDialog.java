package com.vegnab.vegnab;

import java.util.Calendar;

import com.vegnab.vegnab.contentprovider.ContentProvider_VegNab;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class EditProjectDialog extends DialogFragment implements OnClickListener {
	long projRecId = 0; // zero default means new or not specified yet
//	Button buttonSetDateStart;
	private EditText mProjCode, mDescription, mContext, mCaveats, mContactPerson, mStartDate, mEndDate;
	
	static EditProjectDialog newInstance(long projRecId) {
		EditProjectDialog f = new EditProjectDialog();
		// supply projRecId as an argument
		Bundle args = new Bundle();
		args.putLong("projRecId", projRecId);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		projRecId = getArguments().getLong("projRecId");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_project, root);
		final Calendar myCalendar = Calendar.getInstance();
		mProjCode = (EditText) view.findViewById(R.id.txt_projcode);
		mDescription = (EditText) view.findViewById(R.id.txt_descr);
		mContext = (EditText) view.findViewById(R.id.txt_context);
		mCaveats = (EditText) view.findViewById(R.id.txt_caveats);
		mContactPerson = (EditText) view.findViewById(R.id.txt_person);
		mStartDate = (EditText) view.findViewById(R.id.txt_date_from);
		mEndDate = (EditText) view.findViewById(R.id.txt_date_to);
		
		
		final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear,
		            int dayOfMonth) {
		        Log.v("EditProj", "Event caught in EditProjectDialog, anonymous onDateSet");
		        myCalendar.set(Calendar.YEAR, year);
		        myCalendar.set(Calendar.MONTH, monthOfYear);
		        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//		        updateLabel();
		        mStartDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		    }

		};
		
/*		mEditDateFrom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("EditProj", "Event caught in EditProjectDialog, anonymous onClick");
				DatePickerFragment newFragment = new DatePickerFragment();
				FragmentManager fm = getChildFragmentManager();
				newFragment.show(fm, "datePicker");			
			}
		}); */
		mStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("EditProj", "Event caught in EditProjectFragment, anonymous onClick");
				new DatePickerDialog(getActivity(), myDateListener,
						myCalendar.get(Calendar.YEAR),
						myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		getDialog().setTitle(R.string.edit_proj_title_edit);
		return view;
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		Log.v("EditProj", "Event caught in EditProjectFragment, DialogInterface onClick");
		
	}
	@Override
	public void onStart() {
		super.onStart();
		// during startup, check if arguments are passed to the fragment
		// this is where to do this because the layout has been applied
		// to the fragment
		Bundle args = getArguments();
		
		if (args != null) {
			projRecId = args.getLong("projRecId"); // redundant with onCreate; decide best & remove other
			// will set up the screen based on arguments passed in
			Log.v("EditProj", "In EditProjectFragment, onStart, projRecId=" + projRecId);
			String selection = "SELECT ProjCode, Description, Context, Caveats, " + 
					"ContactPerson, StartDate, EndDate FROM Projects WHERE _id = ?;";
			String selectionArgs[] = {"" + projRecId};
			ContentResolver rs = getActivity().getContentResolver();
			// use raw SQL, may change to use CursorLoader
			Uri uri = ContentProvider_VegNab.SQL_URI;
			Cursor c = rs.query(uri, null, selection, selectionArgs, null);
			if (c.moveToFirst()) {
				mProjCode.setText(c.getString(c.getColumnIndexOrThrow("ProjCode")));
				mDescription.setText(c.getString(c.getColumnIndexOrThrow("Description")));
				mContext.setText(c.getString(c.getColumnIndexOrThrow("Description")));
				mCaveats.setText(c.getString(c.getColumnIndexOrThrow("Caveats")));
				mContactPerson.setText(c.getString(c.getColumnIndexOrThrow("ContactPerson")));
				mStartDate.setText(c.getString(c.getColumnIndexOrThrow("StartDate")));
				mEndDate.setText(c.getString(c.getColumnIndexOrThrow("EndDate")));
			}
		}
	}
}