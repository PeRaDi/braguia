package pt.uminho.braguia.shared.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacingPixels;
    private boolean includeEdge;

    private GridSpacingItemDecoration(int spanCount, int spacingPixels, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacingPixels = spacingPixels;
        this.includeEdge = includeEdge;
    }

    public static GridSpacingItemDecoration ofPixels(int spanCount, int spacing, boolean includeEdge) {
        return new GridSpacingItemDecoration(spanCount, spacing, includeEdge);
    }

    public static GridSpacingItemDecoration ofDP(Context context, int spanCount, int spacing, boolean includeEdge) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, displayMetrics);
        return new GridSpacingItemDecoration(spanCount, pixels.intValue(), includeEdge);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacingPixels - column * spacingPixels / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacingPixels / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacingPixels;
            }
            outRect.bottom = spacingPixels; // item bottom
        } else {
            outRect.left = column * spacingPixels / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacingPixels - (column + 1) * spacingPixels / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacingPixels; // item top
            }
        }
    }
}
