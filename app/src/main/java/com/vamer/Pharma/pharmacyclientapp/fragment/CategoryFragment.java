/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.vamer.Pharma.pharmacyclientapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.activities.GetNearPharmacies;
import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.adapter.CategoryListAdapter;
import com.vamer.Pharma.pharmacyclientapp.domain.api.ProductCategoryLoaderTask;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Pharmacy;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.model.ProductCategoryModel;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.vamer.Pharma.pharmacyclientapp.util.Utils.AnimationType;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import xyz.sahildave.widget.SearchViewLayout;

public class CategoryFragment extends Fragment {
    int mutedColor = R.attr.colorPrimary;
    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    private TextInputEditText txtSearchCategories;
    private BoomMenuButton bmb;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri mCapturedImageURI;
    FrameLayout rootlayout;
    private ArrayList<ProductCategoryModel> dataObjects = new ArrayList<>();
    CategoryListAdapter simpleRecyclerAdapter;

    int _xDelta;
    int _yDelta;
    /**
     * The double back to exit pressed once.
     */
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();
    AVLoadingIndicatorView progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_category, container, false);
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        final SearchViewLayout searchViewLayout = (SearchViewLayout) view.findViewById(R.id.search_view_container);

        searchViewLayout.setExpandedContentSupportFragment(getActivity(), new ProductListFragment("Chairs", true));
        searchViewLayout.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {

                searchViewLayout.collapse();
            }
        });

        searchViewLayout.setSearchBoxListener(new SearchViewLayout.SearchBoxListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        int paddingDp = 25;
        float density = getActivity().getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);
        progressBar= view.findViewById(R.id.loading_bar);

        // searchViewLayout.setPadding(10,10,10,10);
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);

        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        searchViewLayout.handleToolbarAnimation(toolbar);
        searchViewLayout.setCollapsedHint("Search For Item");
        searchViewLayout.setExpandedHint("Search For Item");
        initateBoomMenu(view);
        txtSearchCategories = view.findViewById(R.id.txtSearchCategories);
        txtSearchCategories.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new SearchProductFragment(),
                        ((HomeActivity) getActivity()), null,
                        AnimationType.SLIDE_UP);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }

        });


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                mutedColor = palette.getMutedColor(R.color.primary_500);
//                collapsingToolbar.setContentScrimColor(mutedColor);
                // collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.scrollableview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        simpleRecyclerAdapter = new CategoryListAdapter(
                getActivity(), dataObjects);


        recyclerView.setAdapter(simpleRecyclerAdapter);
        getCatgeories();
        //  new ProductCategoryLoaderTask(recyclerView, getActivity()).execute();

        simpleRecyclerAdapter.SetOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.switchFragmentWithAnimation(R.id.frag_container, new ProductOverviewFragment(dataObjects.get(position).getCategoryID()), getActivity(),Utils.PRODUCT_OVERVIEW_FRAGMENT_TAG, AnimationType.SLIDE_UP);
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {/*
                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((HomeActivity) (getContext())),
                            AnimationType.SLIDE_RIGHT);*/
                    getActivity().onBackPressed();
                }
                return true;
            }
        });
        return view;

    }


    public void getCatgeories() {
       /* if (null != ((HomeActivity) getActivity()).getProgressBar())
            ((HomeActivity) getActivity()).getProgressBar().setVisibility(
                    View.VISIBLE);*/
        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> postParam = new HashMap<String, String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "Products/GetCategories",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       /* if (null != ((HomeActivity) getActivity()).getProgressBar())
                            ((HomeActivity) getActivity()).getProgressBar().setVisibility(
                                    View.GONE);*/
                        progressBar.setVisibility(View.GONE);

                        try {
                            String Status = response.getString("Status");
                            if (Status.equals(AppConstants.success)) {

                                JSONObject mjsonObject = response.getJSONObject("Result");
                                JSONArray jsonArray = mjsonObject.getJSONArray("dtCategories");
                                dataObjects.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ProductCategoryModel productCategoryModel = new ProductCategoryModel();
                                    productCategoryModel.setCategoryID(jsonObject.getString("CategoryID"));
                                    productCategoryModel.setCategoryName_EN(jsonObject.getString("CategoryName_EN"));
                                    productCategoryModel.setCategoryDesc_EN(jsonObject.getString("CategoryDesc_EN"));
                                    productCategoryModel.setCategoryName_AR(jsonObject.getString("CategoryName_AR"));
                                    productCategoryModel.setCategoryDesc_AR(jsonObject.getString("CategoryDesc_AR"));
                                    productCategoryModel.setParent_CategoryID(jsonObject.getString("Parent_CategoryID"));
                                    productCategoryModel.setCategoryLevel(jsonObject.getString("CategoryLevel"));
                                    productCategoryModel.setCategoryImage(jsonObject.getString("CategoryImage"));
                                    productCategoryModel.setDefaultProductsImage(jsonObject.getString("DefaultProductsImage"));
                                    dataObjects.add(productCategoryModel);
                                }
                                //  pharmacyAdapter.addMoreDataAndSkeletonFinish(dataObjects);
                                simpleRecyclerAdapter.notifyDataSetChanged();
                                //  LocationsRecylcerview.setAdapter(locationAdapter);

                            } else {
                                // Toast.makeText(GetNearPharmacies.this, "There is an error try again later ", Toast.LENGTH_SHORT).show();
                            }
                            //Todo
                            // saveUserData(Result.getString("MobNo"),Result.getString("Name"),Result.getString("Token"),Result.getString("Gender"));

                            // Toasty.error(LoginOrRegisterActivity.this,getResources().getString(R.string.verification_code_not_sent) , Toast.LENGTH_SHORT, true).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
             /*   if (null != ((HomeActivity) getActivity()).getProgressBar())
                    ((HomeActivity) getActivity()).getProgressBar().setVisibility(
                            View.GONE);*/
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic YWhtZWQ6YWhtZWQ=");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, "tag");
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout linearLayOut_CheckOut = getActivity().findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                    Toast.makeText(getActivity(), "Prescription Added Successfully", Toast.LENGTH_LONG).show();
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Product product = new Product("2", "Prescription", "Prescription", "Prescrption", "", "", "", "", picturePath, "");
                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(product);
                    ((HomeActivity) getContext())
                            .updateItemCount(true);

                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getActivity(), "Prescription Added Successfully", Toast.LENGTH_LONG).show();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(mCapturedImageURI, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    Product product = new Product("2", "Prescription", "Prescription", "Prescrption", "0", "0", "0", "", picturePath, "");
                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(product);
                    ((HomeActivity) getContext())
                            .updateItemCount(true);

                }
        }
    }


    private void initateBoomMenu(View view) {
        bmb = (BoomMenuButton) view.findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);

        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
        bmb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

   /*BoomButton b= bmb.getBoomButton(0);

   b.setOnTouchListener(new View.OnTouchListener() {
       @Override
       public boolean onTouch(View view, MotionEvent event) {
           final int X = (int) event.getRawX();
           final int Y = (int) event.getRawY();
           switch (event.getAction() & MotionEvent.ACTION_MASK) {
               case MotionEvent.ACTION_DOWN:
                   FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                   _xDelta = X - lParams.leftMargin;
                   _yDelta = Y - lParams.topMargin;
                   break;
               case MotionEvent.ACTION_UP:
                   break;
               case MotionEvent.ACTION_POINTER_DOWN:
                   break;
               case MotionEvent.ACTION_POINTER_UP:
                   break;
               case MotionEvent.ACTION_MOVE:
                   FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view
                           .getLayoutParams();
                   layoutParams.leftMargin = X - _xDelta;
                   layoutParams.topMargin = Y - _yDelta;
                   layoutParams.rightMargin = -250;
                   layoutParams.bottomMargin = -250;
                   view.setLayoutParams(layoutParams);
                   break;
           }
           rootlayout.invalidate();
           return true;
       }
   });*/
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if (i == 0) {
                HamButton.Builder builder = new HamButton.Builder()
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                uploadPrescription();
                            }
                        })
                        .normalImageRes(R.drawable.ic_action_camera_alt)
                        .normalTextRes(R.string.upload_prescriptin)
                        .subNormalTextRes(R.string.upload_prescriptin);

                bmb.addBuilder(builder);
            } else if (i == 1) {
                HamButton.Builder builder = new HamButton.Builder()
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                recordSound();
                            }
                        })
                        .normalImageRes(R.drawable.ic_action_keyboard_voice)
                        .normalTextRes(R.string.record_voice)
                        .subNormalTextRes(R.string.record_voice);

                bmb.addBuilder(builder);
            } else {
                HamButton.Builder builder = new HamButton.Builder()
                        .listener(new OnBMClickListener() {
                            @Override
                            public void onBoomButtonClick(int index) {
                                WriteText();
                            }
                        })
                        .normalImageRes(R.drawable.ic_action_edit)
                        .normalTextRes(R.string.Write_text)
                        .subNormalTextRes(R.string.Write_text);

                bmb.addBuilder(builder);
            }
        }
    }

    public void WriteText() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.write_text_dialog))
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
        final EditText textToPrescription = (EditText) dialog.findViewById(R.id.textToPrescription);
        final Button btnAddTextToPrescription = (Button) dialog.findViewById(R.id.btnAddTextToPrescription);
        btnAddTextToPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textToPrescription.getText().toString().equals("")) {
                    dialog.dismiss();
                    Product product = new Product("4", "TextNote", textToPrescription.getText().toString(), "Prescrption", "", "", "", "", "", "");
                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(product);
                    ((HomeActivity) getContext())
                            .updateItemCount(true);
                } else {
                    Toast.makeText(getActivity(), "You have to enter Your medicine text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void recordSound() {
        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new RecordFragment(),
                ((HomeActivity) getActivity()), null,
                Utils.AnimationType.SLIDE_UP);


    }

    public void uploadPrescription() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.custom_dialog_box))

                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();


        Button btnChoosePath = (Button) dialog.findViewById(R.id.btnChoosePath);
        btnChoosePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activeGallery();
            }
        });
        Button btnTakePhoto = (Button) dialog.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activeTakePhoto();
            }
        });
    }

    private void activeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void activeTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            //String fileName =
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imageFileName);
            mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
