package com.vamer.Pharma.pharmacyclientapp.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.model.Order;
import com.vamer.Pharma.pharmacyclientapp.OrdersListing.OrdersAdapter;
import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import io.rmiri.skeleton.Master.IsCanSetAdapterListener;

/**
 * Created by Ahmed.Khames on 1/30/2018.
 */
public class OrdersFragment extends Fragment {
    private RecyclerView reViewOdrers;
    // private List<Order> orderItem;
    private OrdersAdapter ordersAdapter;
    private ArrayList<Order> orderItem = new ArrayList<>();
    private boolean doubleBackToExitPressedOnce;
    private BoomMenuButton bmb;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri mCapturedImageURI;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_list, container, false);

        LinearLayout linearLayOut_CheckOut = getActivity().findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
        DrawerLayout mDrawerLayout=getActivity().findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initateBoomMenu(view);
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
      //  toolbar.setNavigationIcon(R.drawable.ic_action_keyboard_backspace);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Utils.switchFragmentWithAnimation(
                        R.id.frag_container,
                        new PlaceOrderFragment(),
                        ((HomeActivity) getActivity()), Utils.PLACE_ORDER_FRAGMENT,
                        Utils.AnimationType.SLIDE_LEFT);*/
             getActivity().onBackPressed();
            }

        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

             /*   if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (doubleBackToExitPressedOnce) {
                        // super.onBackPressed();

                        if (mHandler != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }

                        getActivity().finish();

                        return true;
                    }

                    doubleBackToExitPressedOnce = true;

                    Utils.switchFragmentWithAnimation(
                            R.id.frag_container,
                            new PlaceOrderFragment(),
                            ((HomeActivity) getActivity()), null,
                            Utils.AnimationType.SLIDE_LEFT);


                    mHandler.postDelayed(mRunnable, 2000);

                }
                return true;*/
             getActivity().onBackPressed();
                return true;
            }
        });
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       /* ObservableScrollView scrollView1=view.findViewById(R.id.scrollView2);
        scrollView1.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
                if (scrollState == ScrollState.UP) {
                    if (ab.isShowing()) {
                        ab.hide();
                    }
                } else if (scrollState == ScrollState.DOWN) {
                    if (!ab.isShowing()) {
                        ab.show();
                    }
                }
            }
        });*/
        reViewOdrers = (RecyclerView) view.findViewById(R.id.order_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        reViewOdrers.setLayoutManager(llm);
        reViewOdrers.setHasFixedSize(true);
        Order a = new Order();
        a.setOrderId("44555");
        a.setDate("10");
        a.setMonth("May");
        a.setPrice(Double.valueOf(2.2));
        a.setStatus("0");
        orderItem.add(a);
        Order b = new Order();
        b.setOrderId("44555");
        b.setDate("10");
        b.setMonth("June");
        b.setPrice(Double.valueOf(2.2));
        b.setStatus("1");
        orderItem.add(b);

        Order c = new Order();
        Order d = new Order();
        Order e = new Order();

        c.setOrderId("44555");
        c.setDate("10");
        c.setMonth("May");
        c.setPrice(Double.valueOf(2.2));
        c.setStatus("1");
        orderItem.add(c);
        d.setOrderId("44555");
        d.setDate("10");
        d.setMonth("May");
        d.setPrice(Double.valueOf(2.2));
        d.setStatus("0");
        orderItem.add(d);
        e.setOrderId("44555");
        e.setDate("10");
        e.setMonth("May");
        e.setPrice(Double.valueOf(2.2));
        e.setStatus("0");
        orderItem.add(e);
        initializeAdapter();

    }

    private void initializeAdapter() {
        ordersAdapter = new OrdersAdapter(getActivity(), orderItem, reViewOdrers, new IsCanSetAdapterListener() {
            @Override
            public void isCanSet() {
                reViewOdrers.setAdapter(ordersAdapter);
                //   reViewOdrers.notify();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                    Toast.makeText(getActivity(),"Prescription Added Successfully",Toast.LENGTH_LONG).show();
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Product product=new Product("Prescription","Prescription","Prescrption","","","","",picturePath,"");
                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(product);
                    ((HomeActivity) getContext())
                            .updateItemCount(true);

                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK&& null != data) {
                    Toast.makeText(getActivity(),"Prescription Added Successfully",Toast.LENGTH_LONG).show();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(mCapturedImageURI, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    Product product=new Product("Prescription","Prescription","Prescrption","0","0","0","0",picturePath,"");
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
        bmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

    public void recordSound() {
        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new RecordFragment(),
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


    public void WriteText(){
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.write_text_dialog))
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
        final EditText textToPrescription=(EditText) dialog.findViewById(R.id.textToPrescription);
        final Button btnAddTextToPrescription=(Button) dialog.findViewById(R.id.btnAddTextToPrescription);
        btnAddTextToPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textToPrescription.getText().toString().equals("")) {
                    dialog.dismiss();
                    Product product = new Product("TextNote",textToPrescription.getText().toString() , "Prescrption", "", "", "", "", "", "");
                    CenterRepository.getCenterRepository()
                            .getListOfProductsInShoppingList().add(product);
                    ((HomeActivity) getContext())
                            .updateItemCount(true);
                }
                else {Toast.makeText(getActivity(),"You have to enter Your medicine text",Toast.LENGTH_SHORT).show();}
            }
        });
    }

    public void uploadPrescription() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.custom_dialog_box))

               /* .setExpanded(true) */ // This will enable the expand feature, (similar to android L share dialog)
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
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI =getActivity(). getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
