package cn.tju.edu.compression;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCompression implements Runnable {
	private PressSoftware pressSoftware;                                     //霍夫曼压缩调用者
	private String sourceFilePath;                                           //源文件路径
	private String destinationFilePath;                                      //目的文件路径
	private Node root;                                                       //霍夫曼树根节点
	private Map<Byte, Integer> byteFrequency = new HashMap<Byte, Integer>(); //文件字节频率
	private Map<Byte, String> huffmanCode = new HashMap<Byte, String>();     //文件字节编码
	private StringBuffer codes;                                              //霍夫曼编码
	
	public HuffmanCompression(PressSoftware pressSoftware) {
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
	
	//霍夫曼压缩
	public void compression() {
		try {
			long startTime = System.currentTimeMillis();
			File sourceFile = new File(sourceFilePath);
			FileInputStream istream1 = new FileInputStream(sourceFile);    //读取文件并统计字节频率
			BufferedInputStream bistream1 = new BufferedInputStream(istream1);
			int data1 = 0;
			while((data1 = bistream1.read()) != -1) {
				byte temp1 = (byte)data1;
				if(byteFrequency.get(temp1) != null) {
					byteFrequency.put(temp1, byteFrequency.get(temp1) + 1);
				}
				else {
					byteFrequency.put(temp1, 1);
				}
			}
			bistream1.close();
			this.createHuffmanTree();
			this.generateHuffmanCode(root, "");
			FileInputStream istream2 = new FileInputStream(sourceFilePath);
			BufferedInputStream bistream2 = new BufferedInputStream(istream2);
			codes = new StringBuffer("");                                      //读取文件字节并转换为霍夫曼编码
			int data2 = 0;
			while((data2 = bistream2.read()) != -1) {
				byte temp2 = (byte)data2;
				codes.append(huffmanCode.get(temp2));
			}
			bistream2.close();
			int length = 8 - (codes.length()%8);                               //霍夫曼编码总位数补齐为8的整数倍
			for(int i = 0; i < length; i++) {
				codes.append("0");
			}
			long endTime = System.currentTimeMillis();
			long compressionTime = (endTime - startTime) / 1000;
			String htime = compressionTime + "s";
			pressSoftware.addHTimeTextField(htime);
			long codeByteNumber = codes.length() / 8;
			long fileByteNumber = sourceFile.length();
			long compressibility = codeByteNumber*100 / fileByteNumber;
			String hcompressibility = compressibility + "%";
			pressSoftware.addHCompressibilityTextField(hcompressibility);
		}catch(Exception e) {
			System.out.println("霍夫曼压缩异常");
		}
	}
	
	//构建霍夫曼树
	public void createHuffmanTree() {
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();         //构建结点的优先级队列
		Iterator<Byte> iterator = byteFrequency.keySet().iterator();
		while(iterator.hasNext()) {
			Byte data = iterator.next();
			Node node = new Node(data, byteFrequency.get(data));
			priorityQueue.add(node);
		}
		
		while(priorityQueue.size() > 1) {                                     //构建结点的霍夫曼树
			Node min1 = priorityQueue.remove();
			Node min2 = priorityQueue.remove();
			Node parent = new Node((byte)0, min1.times + min2.times);
			parent.lchild = min1;
			parent.rchild = min2;
			priorityQueue.add(parent);
		}
		root = priorityQueue.remove();
	}
	
	//生成霍夫曼编码
	public void generateHuffmanCode(Node parent, String code) {
		if((parent.lchild == null) && (parent.rchild == null)){              //叶子节点，生成霍夫曼编码
			huffmanCode.put(parent.data, code);
		}
		if(parent.lchild != null) { //左子树不为空，遍历左子树
			generateHuffmanCode(parent.lchild, code + '0');
		}
		if(parent.rchild != null) { //右子树不为空，遍历右子树
			generateHuffmanCode(parent.rchild, code + '1');
		}
	}
	
	//写入霍夫曼编码
	public void writeFile() {
		try {
			FileOutputStream ostream = new FileOutputStream(destinationFilePath);
			BufferedOutputStream bostream = new BufferedOutputStream(ostream);
			for(int i = 0; i < codes.length(); i += 8) {                    //霍夫曼编码转换为字节写入文件
				String code  = codes.substring(i, i + 8);
				int temp = Integer.parseInt(code, 2);
				bostream.write(temp);
			}
			bostream.flush();
			bostream.close();
		}catch(Exception e) {
			System.out.println("霍夫曼编码写入文件异常");
		}
	}
	
	public void decompression() {
		try {
			FileInputStream istream = new FileInputStream(sourceFilePath);
			BufferedInputStream bistream = new BufferedInputStream(istream);
			FileOutputStream ostream = new FileOutputStream(destinationFilePath);
			BufferedOutputStream bostream = new BufferedOutputStream(ostream);
			
			int data = 0;
			StringBuffer codes = new StringBuffer("");      //读取文件的全部字节（霍夫曼编码）
			while((data = bistream.read()) != -1) {
				String temp = Integer.toBinaryString(data);
				int length = 8 - temp.length();
				for(int i = 0; i < length; i++) {
					codes.append("0");
				}
				codes.append(temp);
			}
			int index = 0;                                 //遍历霍夫曼树解码并写入解压文件
			while(index < codes.length()) {
				Node temp = root;
				while(temp.lchild != null && temp.rchild != null && index < codes.length()) {
					if(codes.charAt(index) == '0') {
						temp = temp.lchild;
					}
					else {
						temp = temp.rchild;
					}
					index++;
				}
				bostream.write(temp.data);
			}
			bostream.flush();
			bistream.close();
			bostream.close();
		}catch(Exception e) {
			System.out.println("霍夫曼树被修改");
		}
	}
}

class Node implements Comparable<Node> {
	public byte data;    //字节
	public int times;    //频率
	public Node lchild;  //左子树
	public Node rchild;  //右子树
	
	public Node(byte data, int times) {
		this.data = data;
		this.times = times;
	}
	
	public int compareTo(Node node) { //字节频率为比较依据
		int dif = this.times - node.times;
		return dif;
	}
}
