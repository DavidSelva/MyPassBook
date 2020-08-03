package com.david.mypassbook.ui.passbook.pdfviewer

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.david.mypassbook.R
import com.david.mypassbook.databinding.ActivityPdfViewBinding
import com.david.mypassbook.ui.BaseActivity
import com.david.mypassbook.utils.AppUtils
import com.david.mypassbook.utils.Constants
import com.david.mypassbook.utils.StorageUtils
import kotlin.jvm.internal.Intrinsics


class PdfViewActivity : BaseActivity() {
    var TAG: String = PdfViewActivity::javaClass.name
    lateinit var binding: ActivityPdfViewBinding
    lateinit var mContext: Context
    private var filePath: String? = null
    var month: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        val inflate: ActivityPdfViewBinding = ActivityPdfViewBinding.inflate(layoutInflater)
        binding = inflate;
        setContentView(inflate.root)
        setSupportActionBar(binding.toolbar);
        filePath = intent.getStringExtra(Constants.TAG_FILE_PATH)
        month = intent.getStringExtra(Constants.TAG_MONTH)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        Intrinsics.checkExpressionValueIsNotNull(inflater, "menuInflater")
        inflater.inflate(R.menu.menu_pdf, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnDownload) {
            if (filePath != null && month != null) {
                StorageUtils.saveFileToDocs(
                    mContext, filePath!!,
                    "application/pdf", StorageUtils.DOCUMENTS,
                    month + "_" + System.currentTimeMillis() + ".pdf"
                )
                AppUtils.getInstance(mContext)
                    .makeToast(getString(R.string.pdf_success_description))
            }
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}