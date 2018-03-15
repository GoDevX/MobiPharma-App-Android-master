/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.vamer.Pharma.pharmacyclientapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cielyang.android.clearableedittext.ClearableEditText;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.vamer.Pharma.pharmacyclientapp.AppController;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.vamer.Pharma.pharmacyclientapp.util.Utils.AnimationType;
import com.vamer.Pharma.pharmacyclientapp.adapter.ProductListAdapter;
import com.vamer.Pharma.pharmacyclientapp.adapter.ProductListAdapter.OnItemClickListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;
import ru.alexbykov.nopaginate.paginate.Paginate;
import ru.alexbykov.nopaginate.paginate.PaginateBuilder;

public class ProductListFragment extends Fragment {
    private String subcategoryKey;
    private boolean isShoppingList;
    private BoomMenuButton bmb;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri mCapturedImageURI;
    private boolean isSearchList = false;
    private List<Product> productList = new ArrayList<Product>();
    View view;
    private Toolbar mToolbar;
    ProductListAdapter adapter;
    AVLoadingIndicatorView progressBar;
    String SearchKeyword;
    ClearableEditText txtSearch;
    //AVLoadingIndicatorView loading_bar_more;

    public ProductListFragment() {
        isShoppingList = true;
    }

    public static int current_page = 1;
    Paginate paginate;

    @SuppressLint("ValidFragment")
    public ProductListFragment(String subcategoryKey) {

        isShoppingList = false;
        this.subcategoryKey = subcategoryKey;
    }

    @SuppressLint("ValidFragment")
    public ProductListFragment(String subcategoryKey, boolean Search, String SearchKeyword) {
        isSearchList = true;
        isShoppingList = false;
        this.subcategoryKey = subcategoryKey;
        this.SearchKeyword = SearchKeyword;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_product_list_fragment, container,
                false);
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initateBoomMenu(view);
        progressBar = view.findViewById(R.id.loading_bar);
        txtSearch = view.findViewById(R.id.searchtxt);
        txtSearch.setVisibility(View.VISIBLE);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productList.clear();
                if (!s.toString().equals("")) {
                    if (s.toString().length() > 1) {

                        SearchKeyword = s.toString().trim();
                        current_page = 1;
                        isSearchList = true;
                        productList.clear();
                        SearchInProducts(SearchKeyword, String.valueOf(current_page));

                        // current_page++;
                    } else {
                        isSearchList = false;
                        current_page = 1;
                        productList.clear();

                        getItemsInEachCatgeory(String.valueOf(current_page));

                    }
                } else {

                    hideSoftKebad();
                    txtSearch.clearFocus();
                    isSearchList = false;
                    current_page = 1;
                    productList.clear();

                    getItemsInEachCatgeory(String.valueOf(current_page));

                    // scrollablesearch.setVisibility(View.GONE);
                    //scrollablesearch.removeOnScrollListener(scrollListener);
                    //  bmb.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    txtSearch.clearFocus();
                }

            }
        });


        //  loading_bar_more = view.findViewById(R.id.loading_bar_more);

        mToolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        if (mToolbar != null) {
            ((HomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((HomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            // mToolbar.setNavigationIcon(R.drawable.ic_action_keyboard_backspace);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });

        if (isShoppingList) {
            view.findViewById(R.id.anim_toolbar).setVisibility(View.GONE);
            view.findViewById(R.id.slide_down).setOnTouchListener(
                    new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Utils.switchFragmentWithAnimation(
                                    R.id.frag_container,
                                    new CategoryFragment(),
                                    ((HomeActivity) (getContext())), Utils.HOME_FRAGMENT,
                                    AnimationType.SLIDE_DOWN);
                            return false;
                        }
                    });
            bmb.setVisibility(View.VISIBLE);
        }


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.product_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setHasFixedSize(true);

        adapter = new ProductListAdapter(
                getActivity(), isShoppingList, productList);
        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new ProductDetailsFragment(productList.get(position), subcategoryKey, position, false),
                        ((HomeActivity) (getContext())), null,
                        AnimationType.SLIDE_LEFT);

            }
        });


        paginate = new PaginateBuilder()
                .with(recyclerView)
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        //http or db request here
                        if (!isSearchList)
                            getItemsInEachCatgeory(String.valueOf(current_page));
                        else SearchInProducts(SearchKeyword, String.valueOf(current_page));

                        //  current_page++;
                    }
                })
                .setLoadingTriggerThreshold(0)
                .build();

        if (!isSearchList) {
            getItemsInEachCatgeory(String.valueOf(current_page));

        } /*else// SearchInProducts();*/

        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().onBackPressed();
                }
                return true;
            }
        });
        return view;
    }

    void hideSoftKebad() {
        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void getItemsInEachCatgeory(String page) {

        /*if (!page.equals("1")) {
            progressBar.setVisibility(View.VISIBLE);
        }*/

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Cat_ID", subcategoryKey);
        postParam.put("Lang", "EN");
        // postParam.put("SearchString", search);
        postParam.put("PageNumber", page);
        postParam.put("RowspPage", "5");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "Products/GetProducts", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //paginate.showError(true);
                        paginate.showLoading(false);
                       /* if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }*/
                      //  progressBar.setVisibility(View.GONE);
                        try {
                            String Status = response.getString("Status");
                            JSONArray mJsonArray = response.getJSONArray("Result");
                            if (mJsonArray.length() == 0)
                                paginate.setNoMoreItems(true);
                            if (Status.equals(AppConstants.success)) {
                                // productList.clear();
                                for (int i = 0; i < mJsonArray.length(); i++) {
                                    JSONObject jsonObject = mJsonArray.getJSONObject(i);
                                    Product productModel = new Product();
                                    productModel.setOrderItemType("1");
                                    productModel.setProductId(jsonObject.getString("ProductID"));
                                    productModel.setItemName(jsonObject.getString("ProductName_EN"));
                                    productModel.setItemDetail(jsonObject.getString("ProductName_EN"));
                                    productModel.setScientificName(jsonObject.getString("ScientificName"));
                                    productModel.setQuantity("0");
                                    productModel.setSellMRP(jsonObject.getString("Price"));
                                    productModel.setItemName(jsonObject.getString("ProductName_EN"));
                                    //productModel.setImageURL(jsonObject.getString("ProductImagePath"));
                                    productList.add(productModel);


                                }
                                //  pharmacyAdapter.addMoreDataAndSkeletonFinish(dataObjects);
                                adapter.notifyDataSetChanged();
                                current_page++;
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
                // progressBar.setVisibility(View.GONE);
                paginate.showLoading(false);
                paginate.setNoMoreItems(true);
                //  paginate.showError(true);
               /* if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }*/
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
        bmb.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout linearLayOut_CheckOut = getActivity().findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
        //view.findViewById(R.id.anim_toolbar).setVisibility(View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        paginate.unbind();
    }

    public void WriteText() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.write_text_dialog))
               /* .setExpanded(true)*/  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
        final EditText textToPrescription = (EditText) dialog.findViewById(R.id.textToPrescription);
        final Button btnAddTextToPrescription = (Button) dialog.findViewById(R.id.btnAddTextToPrescription);
        btnAddTextToPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textToPrescription.getText().toString().equals("")) {
                    dialog.dismiss();
                    Product product = new Product("2", "TextNote", textToPrescription.getText().toString(), "Prescrption", "", "", "", "", "", "");
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
                new CurrentRecordFragment(),
                ((HomeActivity) getActivity()), null,
                Utils.AnimationType.SLIDE_UP);


		/*DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.record))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(DialogPlus dialog, View view) {
						ImageView v=view.findViewById(R.id.btnRecordVoice);
						v.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								switch(event.getAction()){
									*//*case MotionEvent.ACTION_DOWN:
                                        AppLog.logString("Start Recording");
										startRecording();
										break;
									case MotionEvent.ACTION_UP:
										AppLog.logString("stop Recording");
										stopRecording();
										break;*//*
                                }
								return false;
							}
						});
					}
				})

				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
						ImageView v=view.findViewById(R.id.btnRecordVoice);
						v.setOnTouchListener(new View.OnTouchListener() {
							@Override
							public boolean onTouch(View v, MotionEvent event) {


								// TODO Auto-generated method stub
								switch(event.getAction()){
									case MotionEvent.ACTION_DOWN:
								//		AppLog.logString("Start Recording");
										startRecording();
										break;
									case MotionEvent.ACTION_UP:
									//	AppLog.logString("stop Recording");
										stopRecording();
										break;
								}
								return false;
							}
						});
					}
				})
				.setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
				.create();
		dialog.show();*/
    }

    public void uploadPrescription() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.custom_dialog_box))
              /*  .setExpanded(true) */ // This will enable the expand feature, (similar to android L share dialog)
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

    private void SearchInProducts(String search, String page) {
       /* if (!page.equals("1")) {
            progressBar.setVisibility(View.VISIBLE);
        }*/
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("Lang", "EN");
        postParam.put("SearchString", search);
        postParam.put("PageNumber", page);
        postParam.put("RowspPage", "5");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppConstants.API_BASE_URL + "Products/SearchInProducts", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String Status = response.getString("Status");
                            JSONArray mJsonArray = response.getJSONArray("Result");
                            if (mJsonArray.length() == 0)
                                paginate.setNoMoreItems(true);
                            if (Status.equals(AppConstants.success)) {
                                //productList.clear();
                                for (int i = 0; i < mJsonArray.length(); i++) {
                                    JSONObject jsonObject = mJsonArray.getJSONObject(i);
                                    Product productModel = new Product();
                                    productModel.setOrderItemType("1");
                                    productModel.setProductId(jsonObject.getString("ProductID"));
                                    productModel.setItemName(jsonObject.getString("ProductName_EN"));
                                    productModel.setItemDetail(jsonObject.getString("ProductName_EN"));
                                    productModel.setScientificName(jsonObject.getString("ScientificName"));
                                    productModel.setQuantity("0");
                                    productModel.setSellMRP(jsonObject.getString("Price"));
                                    productModel.setItemName(jsonObject.getString("ProductName_EN"));
                                    //productModel.setImageURL(jsonObject.getString("ProductImagePath"));
                                    productList.add(productModel);


                                }
                                current_page++;
                               /* if (productList.size() == 0) {
                                    getActivity().findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                                } else
                                    getActivity().findViewById(R.id.empty_view).setVisibility(View.GONE);*/

                                //  pharmacyAdapter.addMoreDataAndSkeletonFinish(dataObjects);
                                adapter.notifyDataSetChanged();
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

                //paginate.showLoading(false);
                paginate.showLoading(false);
                paginate.showError(true);

                /*if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);*/
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

}