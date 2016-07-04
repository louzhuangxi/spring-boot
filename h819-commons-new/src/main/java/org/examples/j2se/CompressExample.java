package org.examples.j2se;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;

public class CompressExample {

    private static Logger logger = LoggerFactory.getLogger(CompressExample.class);


    //多重打包
	public static void makeZip() throws IOException, ArchiveException{
		File f1 = new File("D:/compresstest.txt");
        File f2 = new File("D:/test1.xml");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        //ArchiveOutputStream ostemp = new ArchiveStreamFactory().createArchiveOutputStream("zip", baos);
        ZipArchiveOutputStream ostemp = new ZipArchiveOutputStream(baos);
        ostemp.setEncoding("GBK");
        ostemp.putArchiveEntry(new ZipArchiveEntry(f1.getName()));
        IOUtils.copy(new FileInputStream(f1), ostemp);
        ostemp.closeArchiveEntry();
        ostemp.putArchiveEntry(new ZipArchiveEntry(f2.getName()));
        IOUtils.copy(new FileInputStream(f2), ostemp);
        ostemp.closeArchiveEntry();
        ostemp.finish();
        ostemp.close();

//      final OutputStream out = new FileOutputStream("D:/testcompress.zip");
        final OutputStream out = new FileOutputStream("D:/中文名字.zip");
        ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream("zip", out);
        os.putArchiveEntry(new ZipArchiveEntry("打包.zip"));
        baos.writeTo(os);
        os.closeArchiveEntry();
        baos.close();
        os.finish();
        os.close();
	}
	
	//解压zip包中的所有文件
	public static void makeUnZip() throws IOException, ArchiveException{
		File f = new File("D:/中文名字.zip"); 
        ZipFile zf = new ZipFile(f, "GBK"); 
        File folder = new File("D:/中文名字"); 
        if (!folder.exists()) { 
            folder.mkdirs(); 
        } 

        for (Enumeration<ZipArchiveEntry> files = zf.getEntries(); files.hasMoreElements();) {
        	ZipArchiveEntry zae = files.nextElement(); 
            String zipname = zae.getName(); 
            if (zipname.endsWith(".zip")) {
                String innerzip = StringUtils.removeEnd(zipname, ".zip"); 
                File innerfolder = new File(folder + File.separator + innerzip); 
                if (!innerfolder.exists()) {
                    innerfolder.mkdirs();
                } 
                ZipArchiveInputStream zais = new ZipArchiveInputStream(zf.getInputStream(zae), "GBK", true); 
                FileOutputStream fos = null; 
                ZipArchiveEntry innerzae = null; 
                while ((innerzae = zais.getNextZipEntry()) != null) { 
                    fos = new FileOutputStream(folder + File.separator + innerzip + File.separator + innerzae.getName()); 
                    IOUtils.copy(zais, fos);
                } 
                zais.close(); 
                fos.flush(); 
                fos.close(); 
            } else { 
                ZipArchiveEntry packinfo = zf.getEntry(zipname); 
                String filename = folder + File.separator + zipname; 
                FileOutputStream fos = new FileOutputStream(filename); 
                InputStream is = zf.getInputStream(packinfo); 
                IOUtils.copy(is, fos); 
                is.close(); 
                fos.flush(); 
                fos.close(); 
            } 
        } 
        zf.close();

	}
	
	/**
     * 把一个ZIP文件解压到一个指定的目录中
     * @param zipfilename ZIP文件抽象地址
     * @param outputdir 目录绝对地址
     */
    public static void unZipToFolder(String zipfilename, String outputdir) throws IOException {
        File zipfile = new File(zipfilename);
        if (zipfile.exists()) {
            outputdir = outputdir + File.separator;
            FileUtils.forceMkdir(new File(outputdir));

            ZipFile zf = new ZipFile(zipfile, "UTF-8");
            Enumeration zipArchiveEntrys = zf.getEntries();
            while (zipArchiveEntrys.hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveEntrys.nextElement();
                if (zipArchiveEntry.isDirectory()) {
                    FileUtils.forceMkdir(new File(outputdir + zipArchiveEntry.getName() + File.separator));
                } else {
                    IOUtils.copy(zf.getInputStream(zipArchiveEntry), FileUtils.openOutputStream(new File(outputdir + zipArchiveEntry.getName())));
                }
            }
        } else {
            throw new IOException("指定的解压文件不存在：\t" + zipfilename);
        }
    }
	
	//一层打包
	public static void makeOnlyZip() throws IOException, ArchiveException{
		File f1 = new File("D:/compresstest.txt");
        File f2 = new File("D:/test1.xml");
        
        final OutputStream out = new FileOutputStream("D:/中文名字.zip");
        ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        
        os.putArchiveEntry(new ZipArchiveEntry(f1.getName()));
        IOUtils.copy(new FileInputStream(f1), os);
        os.closeArchiveEntry();

        os.putArchiveEntry(new ZipArchiveEntry(f2.getName()));
        IOUtils.copy(new FileInputStream(f2), os);
        os.closeArchiveEntry();
        os.close();
	}
    
	//解压zip包中的第一个文件
	public static void makeOnlyUnZip() throws ArchiveException, IOException{
		final InputStream is = new FileInputStream("D:/中文名字.zip");
		ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
		ZipArchiveEntry entry = entry = (ZipArchiveEntry) in.getNextEntry();
		
		String dir = "D:/cnname";
		File filedir = new File(dir);
		if(!filedir.exists()){
			filedir.mkdir();
		}
//		OutputStream out = new FileOutputStream(new File(dir, entry.getName()));
		OutputStream out = new FileOutputStream(new File(filedir, entry.getName()));
		IOUtils.copy(in, out);
		out.close();
		in.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ArchiveException {
		try {
//			makeZip();
//			makeOnlyZip();
//			makeOnlyUnZip();
//			makeUnZip();
			unZipToFolder("D:\\中文名字.zip", "D:\\中文哈");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
