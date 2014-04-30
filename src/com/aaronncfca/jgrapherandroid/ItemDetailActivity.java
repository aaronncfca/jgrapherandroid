package com.aaronncfca.jgrapherandroid;

import com.aaronncfca.jgrapherandroid.exceptions.InputException;
import com.aaronncfca.jgrapherandroid.function.SingleVarFunction;
import com.aaronncfca.jgrapherandroid.pieces.Piece;
import com.aaronncfca.jgrapherandroid.ui.Processor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
public class ItemDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//

		final GraphPanel gPanel = (GraphPanel) findViewById(R.id.graphPanel);
		final Button gBtn = (Button) findViewById(R.id.gButton);
		final EditText gInput = (EditText) findViewById(R.id.editText1);
		gBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String input = gInput.getText().toString();
				try {
					SingleVarFunction svf = new SingleVarFunction(
							Processor.ProcessInput(input), "x");
					gPanel.AddFunction(0, svf);
				} catch (InputException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
}
