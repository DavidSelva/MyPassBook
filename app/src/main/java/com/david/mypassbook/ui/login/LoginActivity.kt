package com.david.mypassbook.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.david.mypassbook.R
import com.david.mypassbook.databinding.ActivityLoginBinding
import com.david.mypassbook.ui.BaseActivity
import com.david.mypassbook.ui.passbook.MainActivity
import com.david.mypassbook.utils.AppUtils
import com.david.mypassbook.utils.PrefUtils

class LoginActivity : BaseActivity() {
    var TAG: String = LoginActivity::javaClass.name
    private lateinit var loginBinding: ActivityLoginBinding
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        loginBinding = inflate;
        mContext = this
        setContentView(inflate.root)

        if (PrefUtils.readBoolean(PrefUtils.KEY_IS_LOGGED)) {
            loginBinding.edtConfirmPin.visibility = View.GONE
            loginBinding.edtPin.text =
                Editable.Factory.getInstance()
                    .newEditable(PrefUtils.readInt(PrefUtils.KEY_PIN).toString())
        }

        loginBinding.btnLogin.setOnClickListener(View.OnClickListener {
            if (PrefUtils.readBoolean(PrefUtils.KEY_IS_LOGGED)) {
                when {
                    TextUtils.isEmpty(loginBinding.edtPin.text) -> {
                        AppUtils.getInstance(mContext)
                            .makeToast(getString(R.string.enter_pin_number));
                    }
                    loginBinding.edtPin.text.toString()
                        .toInt() != PrefUtils.readInt(PrefUtils.KEY_PIN) -> {
                        AppUtils.getInstance(mContext)
                            .makeToast(getString(R.string.invalid_pin_number))
                    }
                    else -> {
                        startActivity(Intent(mContext, MainActivity::class.java))
                        finish()
                    }
                }
            } else {
                when {
                    TextUtils.isEmpty(loginBinding.edtPin.text) -> {
                        AppUtils.getInstance(mContext)
                            .makeToast(getString(R.string.enter_pin_number));
                    }
                    TextUtils.isEmpty(loginBinding.edtConfirmPin.text) -> {
                        AppUtils.getInstance(mContext)
                            .makeToast(getString(R.string.enter_confirm_pin_number));
                    }
                    loginBinding.edtPin.text.toString().toInt()
                            != (loginBinding.edtConfirmPin.text.toString().toInt()) -> {
                        AppUtils.getInstance(mContext)
                            .makeToast(getString(R.string.confirm_pin_mismatched));
                    }
                    else -> {
                        PrefUtils.writeInt(
                            PrefUtils.KEY_PIN,
                            loginBinding.edtPin.text.toString().toInt()
                        )
                        PrefUtils.writeBoolean(PrefUtils.KEY_IS_LOGGED, true);
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                }
            }
        })
    }
}