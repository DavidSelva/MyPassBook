package com.david.mypassbook.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build.VERSION
import android.os.Environment
import android.provider.MediaStore
import com.david.mypassbook.R
import com.david.mypassbook.utils.DateUtils.Companion.getCurrentDateTime
import java.io.*
import kotlin.jvm.internal.Intrinsics


class StorageUtils {

    companion object {
        lateinit var mContext: Context
        const val DOCUMENTS = "Documents"
        val TAG = StorageUtils::class.java.simpleName

        fun init(context: Context) {
            mContext = context
        }

        fun createPdfFile(name: String?): File {
            Intrinsics.checkParameterIsNotNull(name, "name")
            val sb = StringBuilder()
            sb.append(name)
            sb.append(" ")
            sb.append(getCurrentDateTime())
            sb.append(".pdf")
            val fileName = sb.toString()
            val mContext: Context = mContext
            val docDir: File? =
                mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(docDir, fileName)
            if (file.exists()) {
                return file
            }
            file.parentFile.createNewFile()
            return file
        }

        fun saveFileToDocs(
            context: Context,
            sourcePath: String,
            mimeType: String,
            from: String,
            fileName: String
        ) {
            if (VERSION.SDK_INT > 28) {
                val relativeLocation: String = Environment.DIRECTORY_DOCUMENTS
                val contentValues = ContentValues()
                contentValues.put("_display_name", fileName)
                contentValues.put("mime_type", mimeType)
                contentValues.put("relative_path", relativeLocation)
                val contentResolver = context.contentResolver

                val outputStream: OutputStream? = null
                val uri: Uri? = null
                try {
                    val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    if (contentResolver.insert(contentUri, contentValues) == null) {
                        throw IOException("Failed to create new MediaStore record.")
                    }
                } catch (e: IOException) {
                    if (uri != null) {
                        contentResolver.delete(uri, null, null)
                    }
                    throw e
                }
            } else {
                val destDir = getDirectory(from, context)
                val source = File(sourcePath)
                val sb = StringBuilder()
                sb.append(destDir)
                sb.append("/")
                sb.append(fileName)
                val destFile = File(sb.toString())
                if (!destFile.exists()) {
                    destFile.createNewFile()
                }
                val fis = FileInputStream(source)
                val buffer = ByteArray(1024)
                val bos =
                    BufferedOutputStream(FileOutputStream(destFile), 1024)
                while (true) {
                    val read: Int = fis.read(buffer)
                    if (read != -1) {
                        bos.write(buffer, 0, read)
                    } else {
                        fis.close()
                        bos.flush()
                        bos.close()
                        return
                    }
                }
            }
        }

        private fun getDirectory(
            from: String,
            context: Context
        ): String {
            val sb = StringBuilder()
            sb.append(Environment.getExternalStorageDirectory().toString())
            val str = "/"
            sb.append(str)
            sb.append(context.getString(R.string.app_name))
            sb.append(str)
            val root = sb.toString()
            val str2: String = StorageUtils.DOCUMENTS
            if (from != str2) {
                return ""
            }
            val sb2 = StringBuilder()
            sb2.append(root)
            sb2.append(str2)
            val myDir = File(sb2.toString())
            if (!myDir.exists()) {
                myDir.mkdirs()
            }
            val absolutePath: String = myDir.absolutePath
            Intrinsics.checkExpressionValueIsNotNull(absolutePath, "myDir.absolutePath")
            return absolutePath
        }

        fun getFileName(path: String): String {
            return path
        }
    }
}