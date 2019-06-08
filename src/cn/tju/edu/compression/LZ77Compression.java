package cn.tju.edu.compression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LZ77Compression implements Runnable {
	private PressSoftware pressSoftware;                                     //LZ77压缩调用者
	private String sourceFilePath;                                           //源文件路径
	private String destinationFilePath;                                      //目的文件路径
	private static final int WINDOWLENGTH = 32767; //滑动窗口长度
	private static final int BYTENUMBER = 16;   //最大匹配字节数
	private StringBuffer codes;
	
	public LZ77Compression(PressSoftware pressSoftware) {
		this.pressSoftware = pressSoftware;
	}
	
	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}
	
	public void setDestinationFilePath(String destinationFilePath) {
		this.destinationFilePath = destinationFilePath;
	}
	
	public void run() {
		this.compression();
	}
	
	//LZ77压缩
	public void compression() {
		try {
			long startTime = System.currentTimeMillis();
			//读取文件的全部字节（原字节）
			File sourceFile = new File(sourceFilePath);
			FileInputStream istream = new FileInputStream(sourceFile);
			BufferedInputStream bistream = new BufferedInputStream(istream);
			int data = 0;
			StringBuffer bytes = new StringBuffer("");                       
			while((data = bistream.read()) != -1) {
				char temp = (char)data;
				bytes.append(temp);
			}
			bistream.close();
			
			//LZ77编码
			codes = new StringBuffer("");
			int begin = 0;                                                   //滑动窗口起始
			int gap = 0;                                                     //滑动窗口和前向缓冲区分界
			int offset = 0;
			int length = 0;
			int end = bytes.length() - 15;
			while(gap <= end) {
			    begin = gap - WINDOWLENGTH;
			    if(begin < 0) {begin = 0;}
			    for(int i = 4; i < BYTENUMBER; i++) {
			    	String str = bytes.substring(gap, gap + i);
			    	int index = bytes.indexOf(str, begin); 
			    	if(index > gap - i) {                                 //未在begin和gap之间找到匹配字符串
			    		break;
			    	}
			    	else {
			    		offset = index - begin;
			    		length = i;
			    	}
			    }
				if(length == 0) {
					codes.append('0');                                  //零标志原字节
					String lengthTemp = Integer.toBinaryString(bytes.charAt(gap));
					int zeroBits = 8 - lengthTemp.length();             //原字节省略零位
					for(int i = 0; i < zeroBits; i++) {  
						codes.append('0');
					}
					codes.append(lengthTemp);                           //写入匹配长度和原字节
					gap += 1;
				}
				else {
					codes.append('1');                                  //一标志LZ77编码
					String lengthTemp = Integer.toBinaryString(length);
					int lengthZeroBits = 4 - lengthTemp.length();
					for(int i = 0; i < lengthZeroBits; i++) {           //匹配长度省略零位
						codes.append('0');
					}
					codes.append(lengthTemp);                           //写入匹配长度
					String offsetTemp = Integer.toBinaryString(offset);
					int offsetZeroBits = 15 - offsetTemp.length();
					for(int i = 0; i < offsetZeroBits; i++) {           //偏移量省略零位
						codes.append('0');
					}
					codes.append(offsetTemp);                           //写入偏移量
					gap += length;
				}
				offset = 0;
				length = 0;
			}
			for(int i = gap; i < bytes.length(); i++) {
				codes.append('0');                            //零标志原字节
				String byteTemp = Integer.toBinaryString(bytes.charAt(i));
				int zeroBits = 8 - byteTemp.length();         //原字节省略零位
				for(int j = 0; j < zeroBits; j++) {  
					codes.append('0');
				}
				codes.append(byteTemp);                       //写入原字节
			}
			int completionBits = 8 - (codes.length()%8); //LZ77编码总位数补齐为8的整数倍
			StringBuffer completion = new StringBuffer("");
			for(int i = 0; i < completionBits; i++) {
				completion.append('1');
			}
			codes = completion.append(codes);
			long endTime = System.currentTimeMillis();
			long compressionTime = (endTime - startTime) / 1000;
			String ltime = compressionTime + "s";
			pressSoftware.addLTimeTextField(ltime);
			long codeByteNumber = codes.length() / 8;
			long fileByteNumber = sourceFile.length();
			long compressibility = codeByteNumber*100 / fileByteNumber;
			String lcompressibility = compressibility + "%";
			pressSoftware.addLCompressibilityTextField(lcompressibility);
		}catch(Exception e) {
			System.out.println("文件压缩异常");
		}
	}
	
	//写入LZ77编码
	public void writeFile() {
		try {
			FileOutputStream ostream = new FileOutputStream(destinationFilePath);
			BufferedOutputStream bostream = new BufferedOutputStream(ostream);
			for(int i = 0; i < codes.length(); i += 8) {    //LZ77编码转换为字节写入文件
				String code  = codes.substring(i, i + 8);
				int temp = Integer.parseInt(code, 2);
				bostream.write(temp);
			}
			bostream.flush();
			bostream.close();
		}catch(Exception e) {
			System.out.println("LZ77编码写入文件异常");
		}
	}
	
	public void decompression() {
		try {
			//读取文件的全部字节（LZ77编码）
			FileInputStream istream = new FileInputStream(sourceFilePath);
			BufferedInputStream bistream = new BufferedInputStream(istream);
			StringBuffer codes = new StringBuffer("");                       
			int data = 0;
			while((data = bistream.read()) != -1) {
				String temp = Integer.toBinaryString(data);
				int length = 8 - temp.length();
				for(int i = 0; i < length; i++) {
					codes.append('0');
				}
				codes.append(temp);
			}
			
			//LZ77解码
			StringBuffer bytes = new StringBuffer("");                       //文件全部原字节
			int begin = 0;                                                   //滑动窗口起始
			int gap = 0;                                                     //滑动窗口和前向缓冲区分界
			int offset = 0;
			int length = 0;
			int from = 0; 
			while(codes.charAt(from) == '1') {
				from++;
			}
			while(from < codes.length()) {
				begin = gap - WINDOWLENGTH;
			    if(begin < 0) {begin = 0;}
				if(codes.charAt(from) == '0') {
					from += 1;
					String bt = codes.substring(from, from + 8);
					char temp = (char)Integer.parseInt(bt, 2);
					bytes.append(temp);
					from += 8;
					gap += 1;
				}
				else {
					from += 1;
					String lengthTemp = codes.substring(from, from + 4);
					length = Integer.parseInt(lengthTemp, 2);
					from += 4;
					String offsetTemp = codes.substring(from, from + 15);
					offset = Integer.parseInt(offsetTemp, 2);
					from += 15;
					bytes.append(bytes.substring(begin + offset, begin + offset + length));
					gap += length;
				}
			}
			
			FileOutputStream ostream = new FileOutputStream(destinationFilePath);
			BufferedOutputStream bostream = new BufferedOutputStream(ostream);
			for(int i = 0; i < bytes.length(); i++) {
				bostream.write(bytes.charAt(i));
			}
			bostream.flush();
			bistream.close();
			bostream.close();
		}catch(Exception e) {
			System.out.println("文件解压缩异常");
		}
	}
}
