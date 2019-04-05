package itg8.com.nowzonedesigndemo.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import itg8.com.nowzonedesigndemo.widget.navigation.BottomBar;

/**
 * Created by swapnilmeshram on 28/09/17.
 */

public class FabLikeBehaviour extends CoordinatorLayout.Behavior<BottomBar> {

    public FabLikeBehaviour() {
    }

    public FabLikeBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Extract any custom attributes out
        // preferably prefixed with behavior_ to denote they
        // belong to a behavior
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomBar child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, BottomBar child, View dependency) {
//        float translationY = getFabTranslationYForSnackbar(parent, child);
//
//        child.setTranslationY(translationY);
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }



    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                BottomBar fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
