package mmu.ac.kr.muljomdao;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.overlay.NMapPOIitem;

public class NMapCalloutCustomOverlayView extends NMapCalloutOverlayView {

	/**
	 * @uml.property  name="mCalloutView"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private View mCalloutView;
	/**
	 * @uml.property  name="mCalloutText"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private TextView mCalloutText;
	/**
	 * @uml.property  name="mRightArrow"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private View mRightArrow;

	public NMapCalloutCustomOverlayView(Context context, NMapOverlay itemOverlay, NMapOverlayItem item, Rect itemBounds) {
		super(context, itemOverlay, item, itemBounds);

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.callout_overlay_view, this, true);

		mCalloutView = findViewById(R.id.callout_overlay);
		mCalloutText = (TextView)mCalloutView.findViewById(R.id.callout_text);
		mRightArrow = findViewById(R.id.callout_rightArrow);

		mCalloutView.setOnClickListener(callOutClickListener);

		mCalloutText.setText(item.getTitle());

		if (item instanceof NMapPOIitem) {
			if (((NMapPOIitem)item).hasRightAccessory() == false) {
				mRightArrow.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * @uml.property  name="callOutClickListener"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final OnClickListener callOutClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (mOnClickListener != null) {
				mOnClickListener.onClick(null, mItemOverlay, mOverlayItem);
			}
		}
	};

}
