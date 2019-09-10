package kr.neko.sokcuri.naraechat.Fonts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NaraeTTF {
    private String name;
    private String path;
    private ByteBuffer data = null;
    private Boolean isInitial;

    public NaraeTTF(String name, String path) {
        this.name = name;
        this.path = path;

        if (loadTTF()) {
            isInitial = true;
        }
    }

    public boolean loadTTF() {
        File file = new File(this.path);
        if (!file.isFile())
            return false;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            this.data = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (this.data == null) {
            System.out.println("font not loaded");
            return false;
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }
}
