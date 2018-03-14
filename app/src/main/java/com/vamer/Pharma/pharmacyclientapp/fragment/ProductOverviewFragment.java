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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.domain.mock.FakeWebServer;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.AppConstants;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.vamer.Pharma.pharmacyclientapp.util.Utils.AnimationType;
import com.vamer.Pharma.pharmacyclientapp.adapter.ProductsInCategoryPagerAdapter;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import xyz.sahildave.widget.SearchViewLayout;

public class ProductOverviewFragment extends Fragment {

    // SimpleRecyclerAdapter adapter;
    private KenBurnsView header;
    private Bitmap bitmap;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    //  private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;
    private BoomMenuButton bmb;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri mCapturedImageURI;
    FrameLayout list_fragment_container;
    View view;
    String categoryID;
    @SuppressLint("ValidFragment")
    public ProductOverviewFragment(String categoryID) {
        this.categoryID=categoryID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_category_details, container, false);
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        list_fragment_container = (FrameLayout) view.findViewById(R.id.list_fragment_container);


        //initateBoomMenu(view);

        // getActivity().setTitle("Products");

        // Simulate Web service calls
        FakeWebServer.getFakeWebServer().getAllProducts(AppConstants.CURRENT_CATEGORY);

        // TODO We Can use Async task But pallete creation is problemitic job
        // will
        // get back to it later

        // new ProductLoaderTask(null, getActivity(), viewPager, tabLayout);

        // Volley can be used here very efficiently but Fake JSON creation is
        // time consuming Leain it now

        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);
        list_fragment_container = (FrameLayout) view.findViewById(R.id.list_fragment_container);
        //collapsingToolbarLayout = (CollapsingToolbarLayout) view
        // //        .findViewById(R.id.htab_collapse_toolbar);
        //  collapsingToolbarLayout.setTitleEnabled(false);

        header = (KenBurnsView) view.findViewById(R.id.htab_header);
        header.setImageResource(R.drawable.header);

        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);

        mToolbar = (Toolbar) view.findViewById(R.id.htab_toolbar);
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


        setUpUi();
        /*final SearchViewLayout searchViewLayout = (SearchViewLayout) view.findViewById(R.id.search_view_container);
        searchViewLayout.setExpandedContentSupportFragment(getActivity(), new ProductListFragment("Chairs",true,""));
        searchViewLayout.handleToolbarAnimation(mToolbar);
        searchViewLayout.setCollapsedHint("Search For Item");
        searchViewLayout.setExpandedHint("Search For Item");
        view.setFocusableInTouchMode(true);*/
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

    private void setUpUi() {

        // setupViewPager(viewPager);
//
        //   tabLayout.setupWithViewPager(viewPager);

        bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette
                        .getDarkVibrantColor(R.color.primary_700);
//                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                //   collapsingToolbarLayout
                //       .setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        tabLayout
                .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        viewPager.setCurrentItem(tab.getPosition());

                        switch (tab.getPosition()) {
                            case 0:

                                header.setImageResource(R.drawable.header);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                //  collapsingToolbarLayout
                                                //          .setContentScrimColor(vibrantColor);
                                                //   collapsingToolbarLayout
                                                //      .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });
                                break;
                            case 1:

                                header.setImageResource(R.drawable.header_1);

                                bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header_1);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                                //  collapsingToolbarLayout
                                                //        .setContentScrimColor(vibrantColor);
                                                // collapsingToolbarLayout
                                                //    .setStatusBarScrimColor(vibrantDarkColor);
                                            }
                                        });

                                break;
                            case 2:

                                header.setImageResource(R.drawable.header2);

                                Bitmap bitmap = BitmapFactory.decodeResource(
                                        getResources(), R.drawable.header2);

                                Palette.from(bitmap).generate(
                                        new Palette.PaletteAsyncListener() {
                                            @SuppressWarnings("ResourceType")
                                            @Override
                                            public void onGenerated(Palette palette) {

                                                int vibrantColor = palette
                                                        .getVibrantColor(R.color.primary_500);
                                                int vibrantDarkColor = palette
                                                        .getDarkVibrantColor(R.color.primary_700);
                                             /*   collapsingToolbarLayout
                                                        .setContentScrimColor(vibrantColor);
                                                collapsingToolbarLayout
                                                        .setStatusBarScrimColor(vibrantDarkColor);*/
                                            }
                                        });

                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout linearLayOut_CheckOut = getActivity().findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
        view.findViewById(R.id.anim_toolbar).setVisibility(View.GONE);


    }

/*
    private void setupViewPager(ViewPager viewPager) {
        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
                getActivity().getSupportFragmentManager());
        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .keySet();

        for (String string : keys) {
            adapter.addFrag(new ProductListFragment(string), string);
        }

        //  viewPager.setAdapter(adapter);
//		viewPager.setPageTransformer(true,
//				Utils.currentPageTransformer(getActivity()));
    }
*/


    // TODO
    //Below Code Work Well But requires JSOn to work
    // Below line of code does caching for offline usage

	
	/*void fillProductMapFromCache() {

		String cached_ProductMapJSON = PreferenceHelper
				.getPrefernceHelperInstace().getString(
						PreferenceHelper.ALL_PRODUCT_LIST_RESPONSE_JSON, null);

		if (null != cached_ProductMapJSON) {
			new JSONParser(NetworkConstants.GET_ALL_PRODUCT,
					cached_ProductMapJSON).parse();

			adapter.notifyDataSetChanged();

		}

	}

	public void fillCategoryData() {

		loadingIndicator.setVisibility(View.VISIBLE);

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				NetworkConstants.URL_GET_PRODUCTS_MAP,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						if (getView() != null && getView().isShown()) {

							new JSONParser(NetworkConstants.GET_ALL_PRODUCT,
									response.toString()).parse();

							PreferenceHelper
									.getPrefernceHelperInstace()
									.setString(
											PreferenceHelper.ALL_PRODUCT_LIST_RESPONSE_JSON,
											response.toString());
							
							setUpPager();


							if (null != loadingIndicator) {
								loadingIndicator.setVisibility(View.GONE);
							}

						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

						fillProductMapFromCache();


						if (null != loadingIndicator) {
							loadingIndicator.setVisibility(View.GONE);
						}
						if (error instanceof TimeoutError
								|| error instanceof NoConnectionError) {


							if (null != getActivity())
								((HomeActivity) getActivity())
										.ShowErrorMessage(Errorhandler.OFFLINE_MODE, true);

						} else if (error instanceof AuthFailureError) {
							// TODO
						} else if (error instanceof ServerError) {

							
							if (null != getActivity())
								((HomeActivity) getActivity())
										.ShowErrorMessage(Errorhandler.SERVER_ERROR, true);
							// TODO
						} else if (error instanceof NetworkError) {

							
							if (null != getActivity())
								((HomeActivity) getActivity())
										.ShowErrorMessage(Errorhandler.NETWORK_ERROR, true);

						} else if (error instanceof ParseError) {

							if (null != getActivity())
								Toast.makeText(
										getActivity(),
										"Parsing Error" + error.networkResponse
												+ error.getLocalizedMessage(),
										Toast.LENGTH_LONG).show();

						}
					}

				}) {

		};

		// jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000 * 2, 0, 0));

		jsonObjReq.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS
				.toMillis(60), DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		AppController.getInstance().addToRequestQueue(jsonObjReq, tagJSONReq);

	}
*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment childFragment = new ProductListFragment(categoryID);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.list_fragment_container, childFragment).commit();


       /* Utils.switchFragmentWithAnimation(R.id.list_fragment_container,
                new ProductListFragment(categoryID),
                ((HomeActivity) getActivity()), null,
                Utils.AnimationType.SLIDE_UP);*/
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
  /*  private void initateBoomMenu(View view) {
        bmb = (BoomMenuButton)view. findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            if(i==0){
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
            }
            else if(i==1){
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
            }
            else {
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
    public void WriteText(){
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.write_text_dialog))
               *//* .setExpanded(true)*//*  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
        final EditText textToPrescription=(EditText) dialog.findViewById(R.id.textToPrescription);
        final Button btnAddTextToPrescription=(Button) dialog.findViewById(R.id.btnAddTextToPrescription);
        btnAddTextToPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textToPrescription.getText().toString().equals("")) {
                    dialog.dismiss();
                    Product product = new Product("2","TextNote",textToPrescription.getText().toString() , "Prescrption", "", "", "", "", "", "");
                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(product);
                    ((HomeActivity) getContext())
                            .updateItemCount(true);
                }
                else {Toast.makeText(getActivity(),"You have to enter Your medicine text",Toast.LENGTH_SHORT).show();}
            }
        });
    }

    public void recordSound() {
        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new RecordFragment(),
                ((HomeActivity) getActivity()), null,
                Utils.AnimationType.SLIDE_UP);


		*//*DialogPlus dialog = DialogPlus.newDialog(getActivity())
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
									*//**//*case MotionEvent.ACTION_DOWN:
                                        AppLog.logString("Start Recording");
										startRecording();
										break;
									case MotionEvent.ACTION_UP:
										AppLog.logString("stop Recording");
										stopRecording();
										break;*//**//*
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
		dialog.show();*//*
    }

    public void uploadPrescription() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.custom_dialog_box))

              *//*  .setExpanded(true) *//* // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();


        Button btnChoosePath= (Button) dialog.findViewById(R.id.btnChoosePath);
        btnChoosePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activeGallery();
            }
        });
        Button btnTakePhoto=(Button) dialog.findViewById(R.id.btnTakePhoto);
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
    }*/
}