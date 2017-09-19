package org.h819.commons.file;

import com.mpatric.mp3agic.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/9/14
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
//https://github.com/mpatric/mp3agic
public class MyMp3Utils {
    private static String rootPath =  "F:\\juping\\";
    private static String album_dir = "中国经典童话故事";
    private static String basePath = rootPath + album_dir + "\\";

    /**
     * id3v1 , id3v2 是记录mp3歌曲信息的编码标准，v2 比 v1 信息更多，如多 AlbumImage
     */
    public static void main(String[] args) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {

        MyMp3Utils mp3 = new MyMp3Utils();

        //  mp3.id3v2(basePath + " 猴子和鳄鱼_.mp3", true);

        // System.out.println(FilenameUtils.getName("F:\\juping\\动物童话故事\\1.1-乌鸦找窝.mp3"));

        mp3.changeTag(new File(basePath));

    //    mp3.rename();
    }




    /**
     * 读取 id3v1 信息
     *
     * @param mp3file
     * @param save    是否写入测试信息
     * @throws IOException
     * @throws NotSupportedException
     */
    @Deprecated // id3v1 不支持 unicode, 中文出现乱码，不用
    private void id3v1(Mp3File mp3file, boolean save) throws IOException, NotSupportedException {

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8.name());

        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            out.println("Track: " + id3v1Tag.getTrack());
            out.println("Artist: " + id3v1Tag.getArtist());
            System.out.println("Title: " + id3v1Tag.getTitle());
            System.out.println("Album: " + id3v1Tag.getAlbum());
            System.out.println("Year: " + id3v1Tag.getYear());
            System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
            System.out.println("Comment: " + id3v1Tag.getComment());
        }

        if (!save)
            return;

        ID3v1 id3v1Tag;

        if (mp3file.hasId3v1Tag()) {
            id3v1Tag = mp3file.getId3v1Tag();
        } else {
            id3v1Tag = new ID3v1Tag();
            mp3file.setId3v1Tag(id3v1Tag);
        }

        id3v1Tag.setTrack("5");
        id3v1Tag.setArtist("An Artist");
        id3v1Tag.setTitle("熊猫");
        id3v1Tag.setAlbum("The Album");
        id3v1Tag.setYear("2001");
        id3v1Tag.setGenre(12);
        id3v1Tag.setComment("Some comment");
        // id3v1Tag.
        mp3file.save("F:\\juping\\动物童话故事\\1.1-乌鸦找窝_纯音频文件_纯音频输出_3.MP3");
    }

    /**
     * 写入 id3v2 tag 信息
     *
     * @param mp3fileSrc  源 mp3 文件
     * @param mp3fileDest 目标 mp3 文件
     * @param title       id3v2 tag  : title
     * @param artist      id3v2 tag  : artist
     * @param album       id3v2 tag  : album
     * @throws InvalidDataException
     * @throws IOException
     * @throws UnsupportedTagException
     * @throws NotSupportedException
     */
    private void editId3V2(File mp3fileSrc, File mp3fileDest, String title, String artist, String album) throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {

        Mp3File mp3file = new Mp3File(mp3fileSrc.getAbsoluteFile());
        ID3v2 id3v2Tag;
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
        } else {
            id3v2Tag = new ID3v24Tag();
            mp3file.setId3v2Tag(id3v2Tag);
        }

        id3v2Tag.setArtist(artist);
        id3v2Tag.setTitle(title);
        id3v2Tag.setAlbum(album);
        //  id3v2Tag.setTrack("5");
//        id3v2Tag.setYear("2001");
//        id3v2Tag.setGenre(12);
//        id3v2Tag.setComment("Some comment");
//        id3v2Tag.setComposer("The Composer");
//        id3v2Tag.setPublisher("A Publisher");
//        id3v2Tag.setOriginalArtist("Another Artist");
//        id3v2Tag.setAlbumArtist("An Artist");
//        id3v2Tag.setCopyright("Copyright");
//        id3v2Tag.setUrl("http://foobar");
//        id3v2Tag.setEncoder("The Encoder");
        // id3v2Tag.setAlbumImage(); //设置 image

        mp3file.save(mp3fileDest.getAbsolutePath());
    }

    /**
     * 读取 mp3 id3v2 tag 信息
     *
     * @param mp3file
     * @throws IOException
     * @throws NotSupportedException
     * @throws InvalidDataException
     * @throws UnsupportedTagException
     */
    private void printId3V2(File mp3file) throws IOException, NotSupportedException, InvalidDataException, UnsupportedTagException {

        Mp3File mp3file0 = new Mp3File(mp3file.getAbsolutePath());

        if (mp3file0.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file0.getId3v2Tag();
            System.out.println("Track: " + id3v2Tag.getTrack());
            System.out.println("Artist: " + id3v2Tag.getArtist());
            System.out.println("Title: " + id3v2Tag.getTitle());
            System.out.println("Album: " + id3v2Tag.getAlbum());
            System.out.println("Year: " + id3v2Tag.getYear());
            System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
            System.out.println("Comment: " + id3v2Tag.getComment());
            System.out.println("Composer: " + id3v2Tag.getComposer());
            System.out.println("Publisher: " + id3v2Tag.getPublisher());
            System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
            System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
            System.out.println("Copyright: " + id3v2Tag.getCopyright());
            System.out.println("URL: " + id3v2Tag.getUrl());
            System.out.println("Encoder: " + id3v2Tag.getEncoder());
        }

        if (mp3file0.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file0.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();
            if (imageData != null) {
                String mimeType = id3v2Tag.getAlbumImageMimeType();
                System.out.println("Mime type (can be write to local): " + mimeType);
                // Write image to file - can determine appropriate file extension from the mime type
//                RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
//                file.write(imageData);
//                file.close();
            }
        }
    }

    private void changeTag(File dir) {

        for (File file : dir.listFiles()) {

            try {
                String name = file.getName();
                String title0 = StringUtils.substringBetween(name, "-", ".");
                String title = title0;
                String artist = "鞠萍";
                String album = "鞠萍姐姐故事屋-" + album_dir;
                String destFilePath = dir + "//#" + name;
                //edit
                editId3V2(file, new File(destFilePath), title, artist, album);
                System.out.println(String.format("edit '%s' tag finished. ", file.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotSupportedException e) {
                e.printStackTrace();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                e.printStackTrace();
            }

        }

    }
}
