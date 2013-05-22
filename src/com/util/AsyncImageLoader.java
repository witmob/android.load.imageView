package com.easymorse.util;import java.io.BufferedOutputStream;import java.io.File;import java.io.FileOutputStream;import java.io.IOException;import java.io.InputStream;import java.net.HttpURLConnection;import java.net.MalformedURLException;import java.net.URL;import java.util.concurrent.ExecutorService;import java.util.concurrent.Executors;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.drawable.Drawable;import android.os.Environment;import android.os.Handler;import android.os.Message;import android.util.Log;public class AsyncImageLoader {	private ExecutorService executorService = Executors.newFixedThreadPool(5);	public AsyncImageLoader(){	}	private final static String ALBUM_PATH  = Environment.getExternalStorageDirectory() + "/example/";	private final static String TAG="imageTag";	public Drawable loadDrawable(final String imageUrl,final ImageCallback callback){		final Handler handler=new Handler(){			@Override			public void handleMessage(Message msg) {				callback.imageLoaded((Bitmap) msg.obj, imageUrl);			}		};				 executorService.submit(new Runnable() {             public void run() {            	 try { 					Bitmap mBitmap = null; 					if (hasImagePath(imageUrl)) { 						//图片存在// 						  Log.v(Contants.TAG, "图片存在"); 						mBitmap=readFile(imageUrl); 					}else{ 						//图片不存在// 						 Log.v(Contants.TAG, "图片不存在,网络下载图片"); 						mBitmap = BitmapFactory.decodeStream(getImageStream(imageUrl)); 						saveFile(mBitmap, imageUrl);  					} 					handler.sendMessage(handler.obtainMessage(0,mBitmap)); 				} catch (Exception e1) { 					handler.sendMessage(handler.obtainMessage(0,null)); 				}             }		 });//		new Thread(){//			public void run() {  //				try {//					Bitmap mBitmap = null;//					if (hasImagePath(imageUrl)) {//						//图片存在////						  Log.v(Contants.TAG, "图片存在");//						mBitmap=readFile(imageUrl);//					}else{//						//图片不存在////						 Log.v(Contants.TAG, "图片不存在,网络下载图片");//						mBitmap = BitmapFactory.decodeStream(getImageStream(imageUrl));//						saveFile(mBitmap, imageUrl); //					}//					handler.sendMessage(handler.obtainMessage(0,mBitmap));//				} catch (Exception e1) {//					handler.sendMessage(handler.obtainMessage(0,null));//				}//			};//		}.start();		return null;	}		/**     *  获取loading界面的图     * @param imageUrl     * @param callback     * @return     */    public Drawable loadLoadingDrawable(final String imageUrl,final ImageCallback callback){        final Handler handler=new Handler(){            @Override            public void handleMessage(Message msg) {                callback.imageLoaded((Bitmap) msg.obj, imageUrl);            }        };        new Thread(){            public void run() {                try {                    Bitmap mBitmap = null;                    if (hasImagePath(imageUrl)) {                        //图片存在                        Log.i("ArtChina", "图片存在");                        mBitmap=readriginalFile(imageUrl);                    }else{                        //图片不存在                        mBitmap = BitmapFactory.decodeStream(getImageStream(imageUrl));                        Log.i("ArtChina", "图片不存在,网络下载图片->"+mBitmap);                        if (mBitmap!=null) {                            saveFile(mBitmap, imageUrl);                        }                    }                    handler.sendMessage(handler.obtainMessage(0,mBitmap));                } catch (Exception e1) {                    handler.sendMessage(handler.obtainMessage(0,null));                }            };        }.start();        return null;    }	    /**      * Get image from newwork      * @param path The path of image      * @return InputStream      * @throws Exception      */      public InputStream getImageStream(String path) throws Exception{          URL url = new URL(path);          HttpURLConnection conn = (HttpURLConnection) url.openConnection();          conn.setConnectTimeout(10 * 1000);          conn.setRequestMethod("GET");          if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){              return conn.getInputStream();          }          return null;      }      /**     * 判断图片是否存在     * @throws MalformedURLException      */    public boolean hasImagePath(String imagePath) throws MalformedURLException{    	if (!hasSdcard()) {		return false;		}      	URL url = new URL(imagePath);        String urlSplite[]=url.getPath().split("/");        StringBuilder filePathName=new StringBuilder();        filePathName.append(ALBUM_PATH);        for (int i = 0; i < urlSplite.length; i++) {      	  String spliteString=urlSplite[i];      	  if (spliteString.length()>0) {      		  filePathName.append(spliteString);      		  if (i!=(urlSplite.length-1)) {      			filePathName.append("/");			}  		}  	}          File dirFile = new File(filePathName.toString());      	return dirFile.exists();    }    /**     *  读取文件     * @throws MalformedURLException     */    private Bitmap readriginalFile(String fileName) throws MalformedURLException{        BitmapFactory.Options option = new BitmapFactory.Options();        Bitmap bm = BitmapFactory.decodeFile(this.getImagePath(fileName),option);        return bm;    }    /**     *  读取文件     * @throws MalformedURLException      */    private Bitmap readFile(String fileName) throws MalformedURLException{       BitmapFactory.Options option = new BitmapFactory.Options();//       if(Global.displayWidth < 321){//    	   option.inSampleSize = 2;//       }       Bitmap bm = BitmapFactory.decodeFile(this.getImagePath(fileName),option);       return bm;    }    /**     * 获取图片在sd卡上的目录     * @throws MalformedURLException      */    private String getImagePath(String imagePath) throws MalformedURLException    {    	URL url = new URL(imagePath);//        Log.v(Contants.TAG, "filename->"+url.getPath());        String urlSplite[]=url.getPath().split("/");        StringBuilder filePathName=new StringBuilder();        filePathName.append(ALBUM_PATH);        for (int i = 0; i < (urlSplite.length-1); i++) {      	  String spliteString=urlSplite[i];      	  if (spliteString.length()>0) {      		  filePathName.append(spliteString);      		  filePathName.append("/");  		}  	}          File dirFile = new File(filePathName.toString());            if(!dirFile.exists()){                dirFile.mkdirs();            }      		return filePathName.append(urlSplite[(urlSplite.length-1)]).toString();    }        /**      * 保存文件      * @param bm      * @param fileName      * @throws IOException      */      public void saveFile(Bitmap bm, String fileName) throws IOException {            File myCaptureFile = new File(this.getImagePath(fileName));            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));            bm.compress(Bitmap.CompressFormat.PNG, 80, bos);            bos.flush();            bos.close();        }    /**     * 判断SD卡是否存在     */    public boolean hasSdcard() {         String status = Environment.getExternalStorageState();         if (status.equals(Environment.MEDIA_MOUNTED)) {             return true;         } else {             return false;        }    }	public interface ImageCallback{		public void imageLoaded(Bitmap mBitmap,String imageUrl);	}		public static void deleteAllImageFile(){		File file = new File(ALBUM_PATH);		delete(file);	}		//递归删除文件及文件夹      private static void delete(File file) {          if (file.isFile()) {              file.delete();              return;          }            if(file.isDirectory()){              File[] childFiles = file.listFiles();              if (childFiles == null || childFiles.length == 0) {                  file.delete();                  return;              }                    for (int i = 0; i < childFiles.length; i++) {                  delete(childFiles[i]);              }              file.delete();          }      }}