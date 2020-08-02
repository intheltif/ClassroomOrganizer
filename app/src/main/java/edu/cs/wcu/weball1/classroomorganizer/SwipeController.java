package edu.cs.wcu.weball1.classroomorganizer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;

/**
 * An enumeration to represent the three seperate states of a button.
 */
enum ButtonState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

/**
 * Class that uses the ItemTouchHelper.Callback to create a Callback that will control swipes on
 * RecyclerViews. This allows us to draw buttons on each side of an item in the RecyclerView when
 * the item is swiped. This class can be extended to allow drag-and-drop functionality for moving
 * items in the list around.
 *
 * Original idea by Bartosz Grzybowski. Github: https://github.com/FanFataL/swipe-controller-demo/
 *
 * @author Evert Ball
 * @version 29 April 2020
 */
class SwipeController extends Callback {
    /** A constant value for the size of the button */
    private static final float buttonWidth = 300;

    /** A boolean value to determine whether an item was swiped */
    private boolean swipeBack = false;

    /** Determines whether a button is shown and on which side to show it. */
    private ButtonState buttonShowedState = ButtonState.GONE;

    /** An instance of the button to be drawn on the screen when swiped left or right */
    private RectF buttonInstance = null;

    /** The ViewHolder of the item being swiped in the RecyclerView */
    private RecyclerView.ViewHolder currentItemViewHolder = null;

    /** The listener for the action to take when an item is swiped left or right */
    private SwipeControllerActions buttonsActions = null;

    /**
     * Constructor that takes a concrete implementation of the actions the buttons perform.
     * @param buttonsActions Concrete implementation of the actions the buttons perform.
     */
    public SwipeController(SwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
    } // end constructor

    /**
     * Determines what types of actions are supported. In our case, only left and right swipe is
     * being supported.
     *
     * @param recyclerView The RecyclerView the user is interacting with.
     * @param viewHolder The ViewHolder of the item in the RecyclerView that was swiped.
     * @return 8 bit integer flag that determines which actions are supported.
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    /**
     * Required method for determining whether items in the RecyclerView can be moved around.
     *
     * @param recyclerView The RecyclerView the user is interacting with.
     * @param viewHolder The ViewHolder for the item in the RecyclerView that has been swiped.
     * @param target The new position in the RecyclerView to move this item to.
     * @return We do not move items around in the list (only between lists) so we always return
     *         false.
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Required method that is called when a ViewHolder is swiped by the user.
     *
     * @param viewHolder The ViewHolder that has been swiped by the user.
     * @param direction The direction the ViewHolder was swiped.
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    } // end onSwiped method

    /**
     * Converts a given set of flags to absolution direction which means ItemTouchHelper.START and
     * ItemTouchHelper.END are replaced with ItemTouchHelper.LEFT and ItemTouchHelper.RIGHT
     * depending on the layout direction.
     *
     * @param flags The flag value that include any number of movement flags.
     * @param layoutDirection The layout direction of the RecyclerView.
     * @return Updated flags which includes only absolute direction values.
     */
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    /**
     * Called by ItemTouchHelper on RecyclerView's onDraw callback.
     *
     * @param c The canvas which RecyclerView is drawing its children.
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted
     *                   and simply animating to its original position.
     * @param dX The amount of horizontal displacement caused by user's action.
     * @param dY The amount of vertical displacement caused by user's action.
     * @param actionState The type of interaction on the View. Is either
     *                    ItemTouchHelper.ACTION_STATE_DRAG or ItemTouchHelper.ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false
     *                           if it is simply animating back to its original state.
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonState.GONE) {
                if (buttonShowedState == ButtonState.LEFT_VISIBLE) {
                    dX = Math.max(dX, buttonWidth);
                } else if (buttonShowedState == ButtonState.RIGHT_VISIBLE) {
                    dX = Math.min(dX, -buttonWidth);
                } // end innnermost if
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } // end middle if
        } // end outter if

        if (buttonShowedState == ButtonState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    /**
     * Sets the onTouchListener for the RecyclerView that the user is interacting with.
     *
     * @param c The canvas which RecyclerView is drawing its children.
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted
     *                   and simply animating to its original position.
     * @param dX The amount of horizontal displacement caused by user's action.
     * @param dY The amount of vertical displacement caused by user's action.
     * @param actionState The type of interaction on the View. Is either
     *                    ItemTouchHelper.ACTION_STATE_DRAG or ItemTouchHelper.ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false
     *                           if it is simply animating back to its original state.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY, final int actionState,
                                  final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) {
                        buttonShowedState = ButtonState.RIGHT_VISIBLE;
                    } else if (dX > buttonWidth) {
                        buttonShowedState  = ButtonState.LEFT_VISIBLE;
                    }

                    if (buttonShowedState != ButtonState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        }); // end onTouch anonymous implementation
    } // end setTouchListener method

    /**
     * Sets the onTouchDownListener for the RecyclerView that the user is interacting with.
     * This listener controls the actions that happen when the user originally touches the screen.
     *
     * @param c The canvas which RecyclerView is drawing its children.
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted
     *                   and simply animating to its original position.
     * @param dX The amount of horizontal displacement caused by user's action.
     * @param dY The amount of vertical displacement caused by user's action.
     * @param actionState The type of interaction on the View. Is either
     *                    ItemTouchHelper.ACTION_STATE_DRAG or ItemTouchHelper.ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false
     *                           if it is simply animating back to its original state.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        }); // end onTouch anonymous implementation
    } // end setTouchDownListener method

    /**
     * Sets the onTouchUpListener of the RecyclerView that the user is interacting with.
     * This listener controls the actions that happen when the user removes lifts their finger(s)
     * off the screen.
     *
     * @param c The canvas which RecyclerView is drawing its children.
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted
     *                   and simply animating to its original position.
     * @param dX The amount of horizontal displacement caused by user's action.
     * @param dY The amount of vertical displacement caused by user's action.
     * @param actionState The type of interaction on the View. Is either
     *                    ItemTouchHelper.ACTION_STATE_DRAG or ItemTouchHelper.ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false
     *                           if it is simply animating back to its original state.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonState.LEFT_VISIBLE) {
                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowedState == ButtonState.RIGHT_VISIBLE) {
                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                    }
                    buttonShowedState = ButtonState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        }); // end onTouch anonymous implementation
    } // end setTouchUpListener method

    /**
     * Sets each item in the RecyclerView to a specified boolean value that determines if it is
     * clickable.
     *
     * @param recyclerView The RecyclerView that the user is interacting with.
     * @param isClickable Boolean value that determines whether the item is clickable or not.
     */
    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    } // end setItemsClickable method

    /**
     * Draws the left or right button depending on the current state that should be shown.
     *
     * @param c The canvas to draw the button onto.
     * @param viewHolder The ViewHolder that was interacted with that needs to have a button drawn
     *                   on it.
     */
    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        buttonInstance = null;
        if (buttonShowedState == ButtonState.LEFT_VISIBLE) {
            RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(Color.YELLOW);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("Tardy", c, leftButton, p);
            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonState.RIGHT_VISIBLE) {
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.GREEN);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("Present", c, rightButton, p);
            buttonInstance = rightButton;
        }
    } // end drawButtons method

    /**
     * The text to draw on the buttons that appear when an item is swiped.
     *
     * @param text The text to draw on the button.
     * @param c The canvas to draw the text onto.
     * @param button The rectangle that we are putting the text inside of.
     * @param p The color of the text to draw on the button.
     */
    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    } // end drawText method

    /**
     * The onDraw method that starts the process of drawing our buttons.
     * @param c The canvas on which to draw the buttons.
     */
    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    } // end onDraw method
} // end SwipeController class

