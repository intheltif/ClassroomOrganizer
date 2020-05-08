package edu.cs.wcu.weball1.classroomorganizer;

/**
 * Abstract class that forces an Activity to override these methods if they're implementing the
 * SwipeController.
 *
 * @author Evert Ball
 * @version 29 April 2020
 *
 */
public abstract class SwipeControllerActions {

    /**
     * Action to be performed when the left button is clicked (item swiped to the right).
     * @param position The index of the item swiped in the RecyclerView's list.
     */
    public void onLeftClicked(int position) {}

    /**
     * Action to be performed when the right button is clicked (item swiped to the left).
     * @param position The index of the item swiped in the RecyclerView's list.
     */
    public void onRightClicked(int position) {}

} // end abstract class
