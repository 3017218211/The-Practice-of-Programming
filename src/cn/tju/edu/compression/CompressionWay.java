package cn.tju.edu.compression;

public interface CompressionWay {
	public abstract void compression();                                                                  //压缩文件
	public abstract void writeFile();
	public abstract void decompression();                                                                //解压缩文件
	public abstract void setSourceFilePath(String sourceFilePath);
	public abstract void setDestinationFilePath(String destinationFilePath);
	public abstract void setCompressionFilePath(String compressionFilePath);
	public abstract void setDecompressionFilePath(String decompressionFilePath);
}
