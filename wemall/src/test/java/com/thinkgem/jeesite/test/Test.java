package com.thinkgem.jeesite.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Test {

	public static void main(String[] args) {
		RandomAccessFile aFile;
		try {
			aFile = new RandomAccessFile("D:/homePageSVN/Test/山丽官网测试20171109/脚本说明.txt", "rw");
			FileChannel inChannel = aFile.getChannel();
			
			ByteBuffer buf = ByteBuffer.allocate(48);
			
			int bytesRead = inChannel.read(buf);
			
			while (bytesRead != -1) {
				
				System.out.println("Read " + bytesRead);
				buf.flip();
				
				while(buf.hasRemaining()){
					System.out.print((char) buf.get());
				}
				
				buf.clear();
				bytesRead = inChannel.read(buf);
			}
			aFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
