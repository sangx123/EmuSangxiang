package com.sangxiang.android.utils

import android.graphics.*
import com.sangxiang.android.App
import org.jetbrains.anko.*
import android.text.TextPaint
import java.util.*
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.widget.ImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.sangxiang.android.utils.cache.LruCacheManager
import java.lang.ref.SoftReference
import kotlin.collections.HashMap


class BitmapUtils {
    var imageCache: HashMap<String, SoftReference<Bitmap>> = HashMap()

    companion object :AnkoLogger{
        fun getNameBitmap(name: String, containerWidth: Int, containerHeight: Int, textSize: Float = 16f): Bitmap? {
            var fileName= "$name$containerWidth$containerWidth.png".replace("/","").replace("\\","")
            var filePath= Environment.getExternalStorageDirectory().absolutePath+"/ASangxiang/cacheImage/"
            var cacheFile=findCacheFile(fileName)
            //此处是用来获取的
            var bm= LruCacheManager.getLruCacheManager().getBitmap(fileName)
            if(bm!=null){
                error { "从lrucache中获取图片" }
                return bm
            }else {

                if (cacheFile != null) {
                    bm=BitmapFactory.decodeFile(filePath + fileName)
                    LruCacheManager.getLruCacheManager().putBitmap(fileName,bm)
                    error { "文件缓存中存在，将文件存入缓存：$fileName" }
                    return bm
                } else {
                    val bmp = Bitmap.createBitmap(containerWidth, containerHeight, Bitmap.Config.ARGB_4444)
                    val rect = Rect(0, 0, containerWidth, containerHeight)
                    val canvas = Canvas(bmp)
//                 val bgPaint = Paint();
//                bgPaint.color = ResourcesUtils.getColor(R.color.white)
//                bgPaint.isAntiAlias = true
//                bgPaint.style = Paint.Style.FILL
//                canvas.drawRect(rect,bgPaint)
                    //绘制圆形背景
                    val circlePaint = Paint()
                    circlePaint.color = Color.rgb(32, 143, 221)
                    circlePaint.isAntiAlias = true
                    circlePaint.style = Paint.Style.FILL
                    circlePaint.strokeWidth = 1f
                    canvas.drawColor(Color.TRANSPARENT)
                    canvas.drawCircle(containerWidth / 2.0f, containerHeight / 2.0f, containerWidth / 2.0f - 1, circlePaint)
                    //设置字体大小
                    var textSize = 0f
                    textSize = if (containerWidth > App.mInstance.dip(40)) {
                        //如果字体大小大于40的话
                        if (name.length > 4) {
                            10f
                        } else {
                            12f
                        }
                    } else {
                        if (name.length > 4) {
                            8f
                        } else {
                            10f
                        }
                    }

                    val textPaint = TextPaint();
                    textPaint.textSize = App.mInstance.sp(textSize).toFloat()
                    textPaint.color = Color.WHITE
                    textPaint.isAntiAlias = true
                    textPaint.textAlign = Paint.Align.CENTER
                    textPaint.style = Paint.Style.FILL
                    textPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                    //获得自动换行后的文字
                    val vector = getTextLinesVector(textPaint, name, rect.height().toFloat(), rect.width().toFloat())
                    var str = ""
                    for (item in vector) {
                        str += item
                    }
                    //文字自动换行
                    val layout = StaticLayout(str, textPaint, rect.width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)
                    canvas.save()
                    textPaint.textAlign = Paint.Align.CENTER
                    //文字的位置
                    canvas.translate(rect.left.toFloat() + rect.width() / 2, rect.top + (rect.height() - getFontHeight(textPaint) * vector.size) / 2)
                    layout.draw(canvas)
                    canvas.restore()
                    saveBitmap(filePath, fileName, bmp)
                    LruCacheManager.getLruCacheManager().putBitmap(fileName,bmp)
                    error { "文件缓存中创建文件，将文件存入缓存：$fileName" }
                    return bmp
                }
            }
        }

        /**
         * 保存图片
         *
         * @param path
         * @param name
         * @param bitmap
         */
        fun saveBitmap(path: String, name: String?, bitmap: Bitmap) {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }

            val _file = File(path + name!!)
            if (_file.exists()) {
                _file.delete()
            }
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(_file)
                if (name != null && "" != name) {
                    val index = name.lastIndexOf(".")
                    if (index != -1 && index + 1 < name.length) {
                        val extension = name.substring(index + 1).toLowerCase()
                        if ("png" == extension) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        } else if ("jpg" == extension || "jpeg" == extension) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
        fun findCacheFile(fileName:String): File?{
            var result: File?=null
            var cacheImage = File(Environment.getExternalStorageDirectory().absolutePath+"/ASangxiang/cacheImage/")
            var list=cacheImage.listFiles()
            list?.let {
                for(item in list!!){
                    if(item.name==fileName){
                        result= item
                        break
                    }
                }
            }
            return result
        }

        /**
         * 将文字拆分成每一行放到Vector里
         */
        fun getTextLinesVector(paint: TextPaint, content: String, maxHeight: Float,
                               maxWidth: Float): Vector<String> {
            val mString = Vector<String>()
            var mRealLine = 0// 字符串真实的行数
            var ch: Char
            var w = 0
            var istart = 0
            val mFontHeight = getFontHeight(paint)
            val mMaxLinesNum = (maxHeight / mFontHeight).toInt()//显示的最大行数
            val count = content.length
            var i = 0
            while (i < count) {
                ch = content[i]
                val widths = FloatArray(1)
                val str = ch.toString()
                paint.getTextWidths(str, widths)
                if (ch == '\n') {
                    //如果带有换行符的话就转行
                    mRealLine++// 真实的行数加一
                    mString.addElement(content.substring(istart, i))
                    istart = i + 1
                    w = 0
                } else {
                    //如果是正常的字符串的话
                    w += Math.ceil(widths[0].toDouble()).toInt()
                    if (w > maxWidth) {
                        //如果超过了一行的话就换行
                        mRealLine++// 真实的行数加一
                        mString.addElement(content.substring(istart, i))
                        istart = i
                        i--
                        w = 0
                    } else {
                        //如果字符串结束了的话就直接增加一行
                        if (i == count - 1) {
                            mRealLine++// 真实的行数加一
                            mString.addElement(content.substring(istart, count))
                        }
                    }
                }
                //当真实行数大于显示的最大行数时跳出循环
                if (mRealLine == mMaxLinesNum) {
                    var j=0
                    while (j<3){
                        var str=  mString[mMaxLinesNum-1].substring(0, mString[mMaxLinesNum-1].length-j-1)+"..."
                        val widths = FloatArray(str.length)
                        var sum=0f
                        paint.getTextWidths(str,widths)
                        for(item in widths){
                            sum+=Math.ceil(item.toDouble()).toInt()
                        }
                        if(sum<maxWidth){
                            mString[mMaxLinesNum-1]=str
                            break
                        }
                        j++
                    }
                    break
                }
                i++
            }

            return mString
        }

        /**
         * 得到文字的高度
         */
        private fun getFontHeight(paint: TextPaint): Float {
            val fm = paint.fontMetrics// 得到系统默认字体属性
            return fm.bottom - fm.top
        }

        fun loadImageOrUserNameOnIV(headUrl:String?, userName:String, imageView: ImageView, textSize:Int = 12){
            if(headUrl.isNullOrEmpty()){
                imageView.setImageBitmap(getNameBitmap(userName,App.getInstance().dip(35f),App.getInstance().dip(35f),12f))
            }else{
                Utils.loadHeadPicToVh(headUrl,imageView)
            }
        }
    }

}