package com.ms.module.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ms.module.base.inter.IViewModel;
import com.ms.module.base.ui.widget.progress.UIProgressDialog;
import com.ms.module.supers.client.Modules;

import org.ms.jetpack.android_module_view_mvvm.R;


public abstract class BaseAppCompatActivity<VM extends IViewModel> extends AppCompatActivity {


    protected VM viewModel;

    protected VM initViewModel() {
        return null;
    }

    protected UIProgressDialog baseDialog;

    protected FragmentManager fragmentManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = initViewModel();
        fragmentManager = getSupportFragmentManager();
        baseDialog = new UIProgressDialog.MaterialBuilder(this)
                .setMessage("加载中...")
                .setTextColor(R.color.colorAccent)
                .create();


        if (isFullScreen()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            final View decorView = getWindow().getDecorView();
            final int uiOption = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

            decorView.setSystemUiVisibility(uiOption);


            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(uiOption);
                    }
                }
            });
        }

        super.onCreate(savedInstanceState);

        if (getLayout() != 0) {
            setContentView(getLayout());
            // 设置沉浸式
            setStatusBar();
            // 初始化控件
            initView();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

    protected void initView() {
    }


    protected int getLayout() {
        return 0;
    }

    protected boolean isFullScreen() {
        return false;
    }

    protected void setStatusBar() {
        //这里做了两件事情，1.使状态栏透明并使contentView填充到状态栏 2.预留出状态栏的位置，防止界面上的控件离顶部靠的太近。这样就可以实现开头说的第二种情况的沉浸式状态栏了
        StatusBarUtil.setTransparent(this);
    }


    public void showDialog() {
        Modules.getUtilsModule().getThreadPoolUtils().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (baseDialog != null && !baseDialog.isShowing()) {
                    baseDialog.show();
                }
            }
        });
    }

    public void dismiss() {
        Modules.getUtilsModule().getThreadPoolUtils().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (baseDialog != null && baseDialog.isShowing()) {
                    baseDialog.dismiss();
                }
            }
        });
    }

    protected void baseStart(final Class<?> clazz) {
        Modules.getUtilsModule().getThreadPoolUtils().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(BaseAppCompatActivity.this, clazz));
            }
        });
    }

    public <T> T findView(int viewID) {
        return (T) findViewById(viewID);
    }


    public void showToast(String text) {
        Modules.getUtilsModule().getThreadPoolUtils().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Modules.getUtilsModule().getToastUtils().show(text);
            }
        });
    }

    protected InputFilter lengthfilter20 = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String dValue = dest.toString();
            if (dValue != null) {
                if (dValue.length() > 20) {
                    return dValue.substring(0, 20);
                }
            }
            return null;
        }
    };

    protected InputFilter lengthfilter11 = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String dValue = dest.toString();
            if (dValue != null) {
                if (dValue.length() > 11) {
                    return dValue.substring(0, 11);
                }
            }
            return null;
        }
    };

    protected InputFilter lengthfilter18 = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String dValue = dest.toString();
            if (dValue != null) {
                if (dValue.length() > 18) {
                    return dValue.substring(0, 18);
                }
            }
            return null;
        }
    };

    protected InputFilter[] userNameAndPasswordInputFilter = new InputFilter[]{lengthfilter20, new InputFilter.LengthFilter(20)};
    protected InputFilter[] phoneNumberInputFilter = new InputFilter[]{lengthfilter11, new InputFilter.LengthFilter(11)};
    protected InputFilter[] iDcardInputFilter = new InputFilter[]{lengthfilter18, new InputFilter.LengthFilter(18)};
}
