package com.pwdgame.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
/**
 * 文件读写类
 * @author admin
 *
 */
public class FileService {

	private static final String TAG="FileService";
	
	/**
	 * 将InputStream转成byte[]
	 */
	public static byte[] readStream(InputStream paramInputStream)
			throws IOException {
		byte[] arrayOfByte1 = new byte[1024];
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		while (true) {
			int i = paramInputStream.read(arrayOfByte1);
			if (i == -1) {
				byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
				localByteArrayOutputStream.close();
				paramInputStream.close();
				return arrayOfByte2;
			}
			localByteArrayOutputStream.write(arrayOfByte1, 0, i);
		}
	}

	/**
	 * 将byte[]保存成文件
	 * 
	 * @param b
	 *            字节数组
	 * @param filePath
	 *            文件路径
	 * @return File对象，失败为null
	 */
	public static File writeStream(byte[] b, String filePath) {
		BufferedOutputStream bof = null;
		File file = null;

		try {
			file = new File(filePath);
			bof = new BufferedOutputStream(new FileOutputStream(file));
			bof.write(b);
		} catch (IOException e) {
			file = null;
		} finally {
			try {
				if (bof != null)
					bof.close();
			} catch (IOException e2) {
				// TODO: handle exception
			}
		}
		return file;

	}

	/**
	 * 将文件保存至当前APP目录，data/data/com.... ..
	 * 
	 * @param context
	 *            上下文实例
	 * @param b
	 *            字节数组
	 * @param fileName
	 *            文件名
	 * @param mode
	 *            读写模式
	 * @see Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE
	 */
	public static void writeStreamToContext(Context context, byte[] b,
			String fileName, int mode) {
		BufferedOutputStream bof = null;
		
		try {
			bof = new BufferedOutputStream(context.openFileOutput(fileName,
					mode));
			bof.write(b,0,b.length);
		} catch (IOException e) {

		} finally {
			try {
				if (bof != null)
					bof.close();
			} catch (IOException e2) {
				// TODO: handle exception
			}
		}

	}

	/**
	 * 将对象转成字节数组并返回 失败返回Null 对象必须实现了 Serializable接口
	 */
	public static byte[] ObjectToBytes(Object film) {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;

		try {
			objectOutputStream = new ObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(film);
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (IOException error) {
			return null;
		}
		return byteOutputStream.toByteArray();
	}

	/**
	 * 将字节数组转成对象并返回 失败返回null 对象必须实现了 Serializable接口
	 */
	public static Object BytesToObject(byte[] bytes) {
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
		ObjectInputStream objectInputStream = null;
		Object object = null;
		try {
			objectInputStream = new ObjectInputStream(byteInputStream);
			object = objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception error) {
			object = null;
		}
		return object;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件存在并且大小>0 返回true，否则false
	 */
	public static boolean FileIsExist(String filePath) {
		boolean result = true;
		File file = new File(filePath);
		if ((result = file.exists()) == false)
			return false;
		long len = file.length();
		if (len <= 0)
			return false;

		return result;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileSize
	 *            对比的文件大小
	 * @return 文件存在并且大小=fileSize 返回true，否则false
	 */
	public static boolean FileIsExist(String filePath, int fileSize) {
		boolean result = true;
		File file = new File(filePath);
		if ((result = file.exists()) == false)
			return false;
		long len = file.length();
		if (len != fileSize || len <= 0)
			return false;

		return result;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileSize
	 *            对比的文件大小
	 * @return 文件存在并且大小=fileSize 返回true，否则false
	 */
	public static boolean FileIsExist(String filePath, long fileSize) {
		boolean result = true;
		File file = new File(filePath);
		if ((result = file.exists()) == false)
			return false;
		long len = file.length();
		if (len != fileSize || len <= 0)
			return false;

		return result;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static long getFileSize(String filePath) {
		boolean result = true;
		File file = new File(filePath);
		if ((result = file.exists()) == false)
			return 0;
		return (file.length());
	}

	/**
	 * 清空程序目录下的所有文件，包括文件夹 rootFile为程序所谓的文件目录
	 * 
	 * @param rootFile
	 *            文件目录
	 */
	public static void removeAllFile(File rootFile) {
		File[] listFiles = null;

		listFiles = rootFile.listFiles();
		for (File temp : listFiles) {
			if (temp.isFile()) {
				temp.delete();
			} else if (temp.isDirectory()) {
				Log.v("DIR", temp.toString());
				// 进行递归删除
				removeAllFile(temp);
				return;
			}
		}
	}

	/**
	 * 打开APK程序代码
	 * 
	 * @param context
	 * @param file
	 *            文件路径
	 */
	public static void openAPKSetup(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 根据path返回后缀名 如果是图片返回后缀，否则返回null
	 */
	public static String getPathsuffix(String path) {
		int last = path.lastIndexOf('.');
		if (last == -1)
			return null;
		String endsString = path.substring(last, path.length());

		return endsString;
	}
	/**
	 * 根据path返回后缀名 如果是图片返回后缀，否则返回null
	 */
	public static String getPathFileName(String path) {
		int last = path.lastIndexOf('/');
		if (last == -1)
			return null;
		String endsString = path.substring(last+1, path.length());

		return endsString;
	}
	/**
	 * 复制单个文件 成功true ，失败false
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		int readSize = 0;
		boolean copySuccess = true;

		File oldFile = new File(oldPath);
		File newFile = new File(newPath);

		if (oldFile == null || !oldFile.exists() || !oldFile.isFile()) {
			return false;
		}
		if (!newFile.exists())
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;

		try {
			inputStream = new FileInputStream(oldPath);
			outputStream = new FileOutputStream(newPath);
			byte[] buffer = new byte[1024];
			while ((readSize = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readSize);
			}
		} catch (IOException error) {
			copySuccess = false;
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (IOException r) {

			}
		}
		return copySuccess;
	}

	/**
	 * 拷贝Assets目录的文件拷贝到上下文目录,data/data/....
	 * 
	 */
	public static boolean copyAssetToContext(Context context, String srcName,
			String desk) {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;

		boolean bResult = true;
		int Size = 0;
		try {
			outputStream = context.openFileOutput(desk,
					Context.MODE_WORLD_READABLE);// Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);//new
													// FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int len = 0;
			inputStream = context.getResources().getAssets().open(srcName);

			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
				Size += len;
			}
			outputStream.flush();

			Log.v("SIZE:", Size + "");
		} catch (Exception e) {
			bResult = false;

		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (outputStream != null)
					outputStream.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}

		}
		return bResult;
	}
	/**get  file's prefix in front of the point.*/
	private static String getPrefix(String str){
		return str.substring(0,str.lastIndexOf("."));
	}
	
	/** Copy the database file to file system. */
	public static boolean copyAssetsZipToFilesystem(Context mContext,String srcFileName) {
		InputStream inStream = null;
		
		AssetManager am = mContext.getAssets();
		try {
			inStream = am.open(srcFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unZipToFilesystem(mContext, inStream);
	}
	
	/**
	 * 将ZIP文件解压到私有文件目录
	 * @param mContext
	 * @param is
	 * @return
	 */
	public static boolean unZipToFilesystem(Context mContext,InputStream is) {
		if(is==null)
			return false;
		boolean result=true;
		ZipInputStream zin = null;
		OutputStream outStream = null;
	
		try {
			zin = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[10240];
			int count;

			while ((ze=zin.getNextEntry()) != null){
				Log.i(TAG, "Unzipping " + ze.getName());
				File tmpFile=new File(mContext.getFilesDir(),ze.getName());
				if(ze.isDirectory())
					tmpFile.mkdirs();
				else{				
				    tmpFile.createNewFile();
					outStream = new FileOutputStream(tmpFile);//new FileOutputStream(mContext.getDatabasePath(desFileName));
					while ((count = zin.read(buffer)) != -1){
						outStream.write(buffer, 0, count);
					}
					outStream.close();
					outStream=null;
				}
				zin.closeEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
			result=false;
		}finally{
			try {
				if (zin != null){
					zin.close();
					zin=null;
				}
				if (outStream != null){
					outStream.close();
					outStream=null;
				}
				if(is!=null){
					is.close();
					is=null;
				}	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		return result;
	}

	/**
	 * 读取Assets目录的文件返回String
	 * 
	 */
	public static String readAssetFile(Context context, String path) {
		InputStream inputStream = null;
		String resultString = "";

		int Size = 0;
		try {

			byte[] buffer = new byte[1024];
			int len = 0;
			inputStream = context.getResources().getAssets().open(path);

			while ((len = inputStream.read(buffer)) > 0) {
				resultString += new String(buffer, 0, len);
				Size += len;
			}

			Log.v("SIZE:", Size + "");
		} catch (Exception e) {

		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}

		}
		return resultString;
	}
	
	/**
	 * 保存对象到文件
	 * @return
	 */
	public static boolean writeObjectToFile(Object obj,String savePath){
		
		ObjectOutputStream oos=null;
		boolean b=true;
		
		try {
			File file=new File(savePath);
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			if(!file.exists())
				file.createNewFile();
			
			oos=new ObjectOutputStream(new FileOutputStream(file));
		    oos.writeObject(obj);
		    
		} catch (Exception e) {
			b=false;
		}finally{
			try {
				if(oos!=null)
					oos.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}			
		}		
		return b;
	}
	
	/**
	 * 从文件中读取对象
	 * @param savePath
	 * @return
	 */
	public static Object readObjectFromFile(String savePath){
		Object obj=null;
		ObjectInputStream ois=null;
		
		try {
			ois=new ObjectInputStream(new FileInputStream(savePath));
			obj=ois.readObject();
		} catch (Exception e) {
			obj=null;
		}finally{
			try {
				if(ois!=null)
					ois.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}			
		}	
		
		return obj;
	}
	/**
	 * 判断字符创是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	 } 
	/**
	 * 获取直接目录和以子目录的所有文件
	 * @param root
	 */
	public static void getAllFiles(File root){
		File files[]=root.listFiles();
		if(files!=null){
			for(File file:files){
				if(file.isDirectory()){
					getAllFiles(file);
				}else {
					Log.e("FILE",file.getName()+"，"+file.getAbsolutePath());
				}
			}
		}
	}
	

	public static String getAllFilesIsPackagePath(Context mContext,File root,String packName){
		File files[]=root.listFiles();
		if(files!=null){
			for(File file:files){
				if(file.isDirectory()){
					String getResult=getAllFilesIsPackagePath(mContext,file,packName);
					if(getResult!=null){
						return getResult;
					}
				}else if(file.isFile()){
					String suffix=getPathsuffix(file.getName());
					Log.i("FILE", file.getAbsolutePath());
					if(suffix!=null&&suffix.equalsIgnoreCase(".apk")){
						try {
							String getPack=mContext.getPackageManager().getPackageArchiveInfo(file.getAbsolutePath(),
								    0).packageName;
							//Log.e("FILE", getPack);
							if(getPack.equalsIgnoreCase(packName)){
								String fileName=file.getName().substring(0,file.getName().lastIndexOf("."));
								if(isNumeric(fileName))
								  return fileName;								
							}
							
						} catch (Exception e) {
							//有时可能无法读取Manifest.xml而导致异常
							e.printStackTrace();
						}
						
					}
				}
			}			
		}
	
		return null;
	}
	
	/**
	 * 拷贝Assist目录的文件到私有目录
	 * @param context
	 * @param srcName Assist目录的文件名
	 * @param desk 保存的文件名
	 * @return 成功返回新文件路径，失败返回null
	 */
	public static String copyAssetToDir(Context context,String srcName,String desk)
	{
		String desFilePath=null;
		
		InputStream inputStream=null;	
		BufferedInputStream bis=null;
		
		FileOutputStream outputStream=null;	
		BufferedOutputStream bos=null;				
		
		int Size=0;
		try 
		{
		  File descFile=new File(desk);
		  if(!descFile.exists()){
			  descFile.getParentFile().mkdirs();
			  descFile.createNewFile();
		  }
		  outputStream=new FileOutputStream(descFile);			  	
		  bos=new BufferedOutputStream(outputStream);
		  
		  byte[] buffer = new byte[10240];
		  int len=0;
		  inputStream=context.getResources().getAssets().open(srcName);	
		  bis=new BufferedInputStream(inputStream);
		  
		  while((len=bis.read(buffer))>0)
		  {
			  bos.write(buffer, 0, len);
			  Size+=len;
		  }
		  bos.flush();		  

		  desFilePath=new File(context.getFilesDir(),desk).getAbsolutePath();
		  Log.v("SIZE:",Size+"");
		} catch (Exception e) {
			e.printStackTrace();
			desFilePath=null;
			
		}
		finally
		{
			try 
			{
				if(bos!=null)
					bos.close();
				if(bis!=null)
					bis.close();
				if(inputStream!=null)
					inputStream.close();
				if(outputStream!=null)
				   outputStream.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
	
		}
		return desFilePath;
	}
}
