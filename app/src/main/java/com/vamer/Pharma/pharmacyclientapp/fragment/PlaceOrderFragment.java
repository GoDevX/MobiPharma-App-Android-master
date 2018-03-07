package com.vamer.Pharma.pharmacyclientapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vamer.Pharma.pharmacyclientapp.R;
import com.vamer.Pharma.pharmacyclientapp.activities.HomeActivity;
import com.vamer.Pharma.pharmacyclientapp.model.CenterRepository;
import com.vamer.Pharma.pharmacyclientapp.model.Product;
import com.vamer.Pharma.pharmacyclientapp.util.Utils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class PlaceOrderFragment extends Fragment {
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};
    private FrameLayout fragmentContainer;
    private RecyclerView.LayoutManager layoutManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    Button btnMain;
    Button btnCategory;
    Button btnOrders;
    private TextInputEditText txtSearchEveryWhere;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private Uri mCapturedImageURI;

    /**
     * Create a new instance of the fragment
     */

    int mutedColor = R.attr.colorPrimary;
    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    /**
     * The double back to exit pressed once.
     */
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable =
            new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            };
    private Handler mHandler = new Handler();


    public static PlaceOrderFragment newInstance(int index) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //	initMenuFragment();
        fragmentManager = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        initPlaceOrder(view);
        return view;
    }


    private void initPlaceOrder(View view) {
        Button btnRoshetta = view.findViewById(R.id.btnRoshetta);
        Button btnRecordeVoice = view.findViewById(R.id.btnRecordVoice);
        Button btnWriteText = view.findViewById(R.id.btnWriteText);
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.nav_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        LinearLayout linearLayOut_CheckOut = getActivity().findViewById(R.id.linearLayOut_CheckOut);
        linearLayOut_CheckOut.setVisibility(View.INVISIBLE);
        btnOrders = view.findViewById(R.id.btnOrders);

        btnMain = view.findViewById(R.id.btnMain);
        btnCategory = view.findViewById(R.id.btnCategory);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new PlaceOrderFragment(), getActivity(), Utils.PLACE_ORDER_FRAGMENT,
                        Utils.AnimationType.SLIDE_UP);

            }
        });
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new CategoryFragment(), getActivity(), Utils.HOME_FRAGMENT,
                        Utils.AnimationType.SLIDE_UP);
            }
        });

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new OrdersFragment(), getActivity(), null/*Utils.HOME_FRAGMENT*/,
                        Utils.AnimationType.SLIDE_UP);
            }
        });
        txtSearchEveryWhere = view.findViewById(R.id.txtSearchEveryWhere);


        txtSearchEveryWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new SearchProductFragment(),
                        ((HomeActivity) getActivity()), null,
                        Utils.AnimationType.SLIDE_UP);

            }
        });

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
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
                    /*Toast.makeText(getActivity(),
                            "Please click BACK again to exit",
							Toast.LENGTH_SHORT).show();*/

                    mHandler.postDelayed(mRunnable, 2000);

                }
                return true;
            }
        });


        btnRecordeVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSound();
            }
        });
        btnWriteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteText();
            }
        });
        btnRoshetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPrescription();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.context_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void refresh() {
        if (getArguments().getInt("index", 0) > 0 && recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(new OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(View clickedView, int position) {
                //zzToast.makeText(getActivity(), "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
                if (position == 1) {
                    uploadPrescription();
                    //	getPosts();
                } else if (position == 2) {
				/*	final Intent intent = new Intent(getActivity(), CountryCodeForNumberActivity.class);
					startActivityForResult(intent, 1);*/
                    recordSound();
                } else if (position == 3) {
                    //getFollowingPosts();
                    WriteText();

                }
            }
        });

    }

    public void uploadPrescription() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.custom_dialog_box))
                .setExpanded(true)
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

    public void recordSound() {
        Utils.switchFragmentWithAnimation(R.id.frag_container,
                new RecordFragment(),
                ((HomeActivity) getActivity()), Utils.RECORD_FRAGMENT,
                Utils.AnimationType.SLIDE_UP);
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private void startRecording() {
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

// If we don't have permissions, ask user for permissions
        if (permission != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    0);

        } else {
            startRecording();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getFilename());
        //	recorder.setOnErrorListener(errorListener);
        //	recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        try {
            recorder.stop();
        } catch (RuntimeException stopException) {
            //handle cleanup here
        }

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


    public void WriteText() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.write_text_dialog))
			/*.setExpanded(true) */ // This will enable the expand feature, (similar to android L share dialog)
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

    /**
     * Called when a fragment will be displayed
     */
    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject("Close");
        close.setResource(R.drawable.ic_action_close);

        MenuObject send = new MenuObject("Upload Prescription");
        send.setResource(R.drawable.ic_action_camera_alt);

        MenuObject like = new MenuObject("OrderWithYourVoice");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_keyboard_voice);
        like.setBitmap(b);
/*MenuObject like = new MenuObject("OrderWithYourVoice");
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_keyboard_voice);
		like.setBitmap(b);
		MenuObject addFr = new MenuObject("Following");
		BitmapDrawable bd = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_black_48dp));
		addFr.setDrawable(bd);*/
        MenuObject writetext = new MenuObject("Write Text");
        writetext.setResource(R.drawable.ic_action_edit);


        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(writetext);

        //	menuObjects.add(addFr);
        //  menuObjects.add(addFav);//menuObjects.add(block);
        return menuObjects;
    }

    public void willBeHidden() {
        if (fragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            fragmentContainer.startAnimation(fadeOut);
        }
    }


}
