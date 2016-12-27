package com.nagash.appwebbrowser.controller.fragments.list;


import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nagash.appwebbrowser.R;
import com.nagash.appwebbrowser.model.webapp.WebApp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
     * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
     */
    public class AppListCard extends CardWithList {

        boolean swipeable = false;
        String title = "";
    AppListItemListener itemListener = null;

        public AppListCard(Context context, String title, boolean listItemSwipeable) {
            super(context);
            this.title = title;
            this.swipeable = listItemSwipeable;
        }
        public AppListCard(Context context) {
            super(context);
        }

        @Override
        protected CardHeader initCardHeader() {

            //Add Header
            CardHeader header = new CardHeader(getContext(),R.layout.card_nearby_inner_header);

//            //Add a popup menu. This method set OverFlow button to visible
//            header.setPopupMenu(R.menu.popup_item, new CardHeader.OnClickCardHeaderPopupMenuListener() {
//                @Override
//                public void onMenuItemClick(BaseCard card, MenuItem item) {
//
//                    switch (item.getItemId()){
//                        case R.id.action_add:
//                            //Example: add an item
//                            AppObject w1= new AppObject(AppListCard.this);
//                            w1.appName ="Madrid";
//                            w1.distance = 24;
//                            w1.appIcon = R.drawable.ic_action_sun;
//                            w1.setObjectId(w1.appName);
//                            mLinearListAdapter.add(w1);
//                            break;
//                        case R.id.action_remove:
//                            //Example: remove an item
//                            mLinearListAdapter.remove(mLinearListAdapter.getItem(0));
//                            break;
//                    }
//
//                }
//            });
            header.setTitle(title); //should use R.string.
            return header;
        }

        @Override
        protected void initCard() {

            //Set the whole card as swipeable
            setUseProgressBar(true);
            setSwipeable(false);

//            setOnSwipeListener(new OnSwipeListener() {
//                @Override
//                public void onSwipe(Card card) {
//                    Toast.makeText(getContext(), "Swipe on " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });

        }

    public void setAppListItemlistener(AppListItemListener itemlistener ) {
        this.itemListener = itemlistener;
    }


        @Override
        protected List<ListObject> initChildren() {

            //Init the list
            List<ListObject> mObjects = new ArrayList<ListObject>();
            return mObjects;
        }

        @Override
        public View setupChildView(int childPosition, final ListObject object, View convertView, ViewGroup parent) {

            TextView tvAppName = (TextView) convertView.findViewById(R.id.carddemo_weather_city);
//            if(tvAppName.getLineCount() > 2)
//                tvAppName.setSingleLine();
            tvAppName.setSelected(true);

            //ImageView icon = (ImageView) convertView.findViewById(R.id.carddemo_weather_icon);
            TextView tvAppDistance = (TextView) convertView.findViewById(R.id.carddemo_weather_temperature);

            ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.buttonPlay);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(AppListCard.this.itemListener != null)
                        AppListCard.this.itemListener.onButtonClick(((AppObject)object).app);
                }
            });

            //Retrieve the values from the object
            AppObject appObject = (AppObject)object;
            //icon.setImageResource(appObject.appIcon);
            tvAppName.setText(appObject.appName);
            if(appObject.distance != null)
                tvAppDistance.setText(appObject.distance + appObject.distanceUnit);
            else if(appObject.distanceUnit.equals("Beacon!"))
            {
                tvAppDistance.setText(appObject.distanceUnit);
            }
            else
                tvAppDistance.setVisibility(View.INVISIBLE);

            return  convertView;
        }

        @Override
        public int getChildLayoutId() {
            return R.layout.card_nearby_inner_main;
        }



    public void updateItems(Collection<WebApp> appList) {
        updateItems(appList, null);
    }


    public void updateItems(Collection<WebApp> appList, Location myLocation) {
        if(appList == null) return;
        ArrayList<AppObject> objs = new ArrayList<AppObject>();

        for( WebApp app : appList)
        {
            AppObject obj = new AppObject(getParentCard()).setApp(app, myLocation);
            obj.setSwipeable(swipeable);
            objs.add(obj);
            //getLinearListAdapter().add(obj);
        }
        //Update the array inside the card
        //.....
        getLinearListAdapter().clear();
        getLinearListAdapter().addAll(objs);

        //use this line if your are using the progress bar
        updateProgressBar(true,true);
    }

    // -------------------------------------------------------------
    // Weather Object
    // -------------------------------------------------------------

    public interface AppListItemListener {
        void onItemClick(WebApp webApp);
        void onButtonClick(WebApp webApp);
        void onSwipe(WebApp webApp);

    }
        public class AppObject extends DefaultListObject{

            private WebApp app;
            private String appName;
            private int appIcon;
            private BigDecimal distance;
            private String distanceUnit ="m";

            public AppObject(Card parentCard){
                super(parentCard);
                init();
            }

            public AppObject setApp(WebApp app, Location myLocation) {
                this.app = app;
                this.appName = app.getName();
                appIcon = R.drawable.ic_cast_play_24dp;

                if(app.isUserNearBeacon())
                {
                    distance = null;
                    distanceUnit = "Beacon!";
                }
                else if(myLocation != null) {
                    float distance = this.app.distanceTo(myLocation);

                    int decimals = 0;
                    if (distance >= 1000) {
                        distance = distance / 1000;
                        distanceUnit = "km";

                        if (distance < 100)
                            decimals = 1;
                    }
                    else distanceUnit = "m";
                    this.distance = new BigDecimal(distance).setScale(decimals, BigDecimal.ROUND_HALF_UP);
                }
                else
                {
                    distance = null;
                    distanceUnit = "";
                }
                return this;
            }

            private void init(){
                // setSwipeable(swipeable);
                //OnClick Listener
                setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                        // Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                        if(AppListCard.this.itemListener != null)
                            AppListCard.this.itemListener.onItemClick(AppObject.this.app);

                    }
                });

                //OnItemSwipeListener
                setOnItemSwipeListener(new OnItemSwipeListener() {
                    @Override
                    public void onItemSwipe(ListObject object, boolean dismissRight) {
                        //Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
                        if(AppListCard.this.itemListener != null)
                            AppListCard.this.itemListener.onSwipe(AppObject.this.app);
                    }
                });
            }

        }


    }