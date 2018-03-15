/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.vamer.Pharma.pharmacyclientapp.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vamer.Pharma.firebasenotifications.app.Config;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.domain.mock.FakeWebServer;
import com.vamer.Pharma.pharmacyclientapp.fragment.MyCartFragment;
import com.vamer.Pharma.pharmacyclientapp.fragment.PlaceOrderFragment;
import com.vamer.Pharma.pharmacyclientapp.domain.helper.Connectivity;
import com.vamer.Pharma.pharmacyclientapp.domain.mining.AprioriFrequentItemsetGenerator;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Money;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.PreferenceHelper;
import com.vamer.Pharma.pharmacyclientapp.util.TinyDB;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.vamer.Pharma.pharmacyclientapp.util.Utils.AnimationType;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    PlaceOrderFragment currentFragment;
    //  DemoViewPagerAdapter adapter;
    Button btnMain;
    AHBottomNavigationAdapter navigationAdapter;
    ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    boolean useMenuResource = true;
    int[] tabColors;
    Handler handler = new Handler();
    Button checkout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static final double MINIMUM_SUPPORT = 0.1;
    private static final String TAG = HomeActivity.class.getSimpleName();
    AprioriFrequentItemsetGenerator<String> generator = new AprioriFrequentItemsetGenerator<>();
    private int itemCount = 0;
    private BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private DrawerLayout mDrawerLayout;
    View notificaitons;
    private TextView checkOutAmount, itemCountTextView;
    private TextView offerBanner;
    private AVLoadingIndicatorView progressBar;
    Button btnCategory;
    private NavigationView mNavigationView;
    LinearLayout linearLayOut_CheckOut;
    public static final String RECEIVER_INTENT = "RECEIVER_INTENT";
    public static final String RECEIVER_MESSAGE = "RECEIVER_MESSAGE";
    BroadcastReceiver mBroadcastReceiver;
    PreferenceHelper pr;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecart);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                   // displayFirebaseRegId();
                } /*else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    txtMessage.setText(message);
                }*/
            }
        };

        /*if(getIntent()!=null)
        {
            if(getIntent().getStringExtra("new_cart").equals("new_cart"))
              new TinyDB(HomeActivity.this).remove(PreferenceHelper.MY_CART_LIST_LOCAL);
        }*/
        try {
            String new_cart = getIntent().getExtras().getString("new_cart");
            new TinyDB(HomeActivity.this).remove(PreferenceHelper.MY_CART_LIST_LOCAL);
        } catch (NullPointerException e ) {

        }
        pr = PreferenceHelper.getPrefernceHelperInstace();
        preferenceHelper = PreferenceHelper.getPrefernceHelperInstace();
        setfirstOpen();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateItemCount(true);
            }
        };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUI();
        CenterRepository.getCenterRepository().setListOfProductsInShoppingList(
                new TinyDB(getApplicationContext()).getListObject(
                        PreferenceHelper.MY_CART_LIST_LOCAL, Product.class));
        List<Product> cr = CenterRepository.getCenterRepository().getListOfProductsInShoppingList();
        FakeWebServer.getFakeWebServer().getAllProducts(
                AppConstants.CURRENT_CATEGORY);

        itemCount = CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                .size();
        ArrayList<Product> a= CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get("productMap");
        //   offerBanner = ((TextView) findViewById(R.id.new_offers_banner));
        linearLayOut_CheckOut = findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
        checkOutAmount = findViewById(R.id.checkoutAmount);
        checkout = findViewById(R.id.checkout);

        checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //      showPurchaseDialog();
                if (preferenceHelper.isUserLoggedIn(HomeActivity.this)) {
                    ArrayList<String> productId = new ArrayList<String>();
                    for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList())
                    //add product ids to array
                    {
                        productId.add(productFromShoppingList.getProductId());
                        CenterRepository.getCenterRepository()
                                .addToItemSetList(new HashSet<>(productId));
                    }
                    Intent i = new Intent(HomeActivity.this, CurrentLocationActivity.class);
                    startActivity(i);
                } else {
                    Toasty.info(HomeActivity.this, "You Have To Login First.", Toast.LENGTH_SHORT, true).show();
                    Intent i = new Intent(HomeActivity.this, LoginOrRegisterActivity.class);
                    startActivity(i);
                }
            }
        });
        checkOutAmount.setSelected(true);
        checkOutAmount.setText((checkoutAmount).toString());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        progressBar = (AVLoadingIndicatorView) findViewById(R.id.loading_bar);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        if (itemCount != 0) {
            for (Product product : CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList()) {
                if (!product.getQuantity().equals(""))
                    updateCheckOutAmount(
                            BigDecimal.valueOf(Double.valueOf(product.getSellMRP()) * Double.valueOf(product.getQuantity())),
                            true);
            }
        }
        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new PlaceOrderFragment(), this, Utils.HOME_FRAGMENT,
                AnimationType.SLIDE_UP);
        toggleBannerVisibility();

        mNavigationView
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                mDrawerLayout.closeDrawers();
                                Utils.switchContent(R.id.frag_container,
                                        Utils.HOME_FRAGMENT,
                                        HomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.my_cart:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SHOPPING_LIST_TAG,
                                        HomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.apriori_result:

                                mDrawerLayout.closeDrawers();

                                // startActivity(new Intent(HomeActivity.this, APrioriResultActivity.class));

                                return true;

                            case R.id.contact_us:
                                mDrawerLayout.closeDrawers();
                                Utils.switchContent(R.id.frag_container,
                                        Utils.CONTACT_US_FRAGMENT,
                                        HomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;

                            case R.id.settings:

                                mDrawerLayout.closeDrawers();

                                Utils.switchContent(R.id.frag_container,
                                        Utils.SETTINGS_FRAGMENT_TAG,
                                        HomeActivity.this,
                                        AnimationType.SLIDE_LEFT);
                                return true;
                            default:
                                return true;
                        }
                    }
                });

    }

    private void setfirstOpen() {
        pr.setString(HomeActivity.this, PreferenceHelper.FIRST_TIME, "0");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,
                R.anim.play_panel_close_background);

    }

    private void initUI() {
        pr = PreferenceHelper.getPrefernceHelperInstace();

    }


    public AVLoadingIndicatorView getProgressBar() {
        return progressBar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        notificaitons = menu.findItem(R.id.action_cart).getActionView();
        itemCountTextView = notificaitons.findViewById(R.id.item_count);
        itemCountTextView.setSelected(true);
        itemCountTextView.setText(String.valueOf(itemCount));
        notificaitons.findViewById(R.id.item_counter).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.switchFragmentWithAnimation(R.id.frag_container,
                                new MyCartFragment(),
                                HomeActivity.this, Utils.SHOPPING_LIST_TAG,
                                Utils.AnimationType.SLIDE_UP);
                    }
                });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cart:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0
                    : --itemCount));
        }

        //  toggleBannerVisibility();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mBroadcastReceiver),
                new IntentFilter(RECEIVER_INTENT)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    public void updateCheckOutAmount(BigDecimal amount, boolean increment) {

        if (increment) {

            checkoutAmount = checkoutAmount.add(amount);
        } else {
            if (checkoutAmount.signum() == 1)
                checkoutAmount = checkoutAmount.subtract(amount);
        }


        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        new TinyDB(getApplicationContext()).putListObject(
                PreferenceHelper.MY_CART_LIST_LOCAL, CenterRepository
                        .getCenterRepository().getListOfProductsInShoppingList());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {

            final Dialog dialog = new Dialog(HomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.connection_dialog);
            Button dialogButton = (Button) dialog
                    .findViewById(R.id.dialogButtonOK);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        }
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // Show Whats New Features If Requires
        //   new WhatsNewDialog(this);
    }

    /*
     * Toggles Between Offer Banner and Checkout Amount. If Cart is Empty SHow
     * Banner else display total amount and item count
     */
    public void toggleBannerVisibility() {
        if (itemCount == 0) {

            //  findViewById(R.id.checkout_item_root).setVisibility(View.GONE);
            //findViewById(R.id.new_offers_banner).setVisibility(View.VISIBLE);

        } else {
//            findViewById(R.id.checkout_item_root).setVisibility(View.VISIBLE);
            //   findViewById(R.id.new_offers_banner).setVisibility(View.GONE);
        }
    }

    /*
     * get total checkout amount
     */
    public BigDecimal getCheckoutAmount() {
        return checkoutAmount;
    }


    public int getItemCount() {
        return itemCount;
    }

    /*
     * Get Navigation drawer
     */
    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }


    public void showPurchaseDialog() {
        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(HomeActivity.this, R.style.PauseDialog);
        exitScreenDialog.setTitle("Order Confirmation")
                .setMessage("Would you like to place this order ?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Place Order",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();

                        dialog.cancel();
                        if (preferenceHelper.isUserLoggedIn(HomeActivity.this)) {
                            ArrayList<String> productId = new ArrayList<String>();
                            for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {
                                //add product ids to array
                                productId.add(productFromShoppingList.getProductId());
                                CenterRepository.getCenterRepository()
                                        .addToItemSetList(new HashSet<>(productId));
                                Intent i = new Intent(HomeActivity.this, CurrentLocationActivity.class);
                                startActivity(i);

                            }

                        } else {

                            Toasty.info(HomeActivity.this, "You Have To Login First.", Toast.LENGTH_SHORT, true).show();
                            Intent i = new Intent(HomeActivity.this, LoginOrRegisterActivity.class);
                            startActivity(i);


                        }

                    }
                });

        exitScreenDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        exitScreenDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                /*Snackbar.make(HomeActivity.this.getWindow().getDecorView().findViewById(android.R.id.content)
                        , "Order Placed Successfully, Happy Shopping !!", Snackbar.LENGTH_LONG)
                        .setAction("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               // startActivity(new Intent(HomeActivity.this, APrioriResultActivity.class));
                            }
                        }).show();*/
            }
        });

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();

    }
/*
    public void showPurchaseDialog() {
        AlertDialog.Builder exitScreenDialog = new AlertDialog.Builder(HomeActivity.this, R.style.PauseDialog);
        exitScreenDialog.setTitle("Order Confirmation")
                .setMessage("Would you like to place this order ?");
        exitScreenDialog.setCancelable(true);

        exitScreenDialog.setPositiveButton(
                "Place Order",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                        dialog.cancel();

                        ArrayList<String> productId = new ArrayList<String>();

                        for (Product productFromShoppingList : CenterRepository.getCenterRepository().getListOfProductsInShoppingList()) {

                            //add product ids to array
                            productId.add(productFromShoppingList.getProductId());
                        }
                        CenterRepository.getCenterRepository()
                                .addToItemSetList(new HashSet<>(productId));
                        FrequentItemsetData<String> data = generator.generate(
                                CenterRepository.getCenterRepository().getItemSetList()
                                , MINIMUM_SUPPORT);
                        for (Set<String> itemset : data.getFrequentItemsetList()) {
                            Log.e("APriori", "Item Set : " +
                                    itemset + "Support : " +
                                    data.getSupport(itemset));
                        }
                        CenterRepository.getCenterRepository().getListOfProductsInShoppingList().clear();

                       */
                        /* toggleBannerVisibility();
                        itemCount = 0;
                        itemCountTextView.setText(String.valueOf(0));
                        checkoutAmount = new BigDecimal(BigInteger.ZERO);
                        checkOutAmount.setText(Money.rupees(checkoutAmount).toString());*//*


                    }
                });

        exitScreenDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        exitScreenDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Snackbar.make(HomeActivity.this.getWindow().getDecorView().findViewById(android.R.id.content)
                        , "Order Placed Successfully, Happy Shopping !!", Snackbar.LENGTH_LONG)
                        .setAction("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //    startActivity(new Intent(getActivity(), APrioriResultActivity.class));
                            }
                        }).show();
            }
        });

        AlertDialog alert11 = exitScreenDialog.create();
        alert11.show();

    }
*/

}
